package com.rebeca.price_comparator.service;

import com.rebeca.price_comparator.model.*;
import com.rebeca.price_comparator.repository.DiscountRepository;
import com.rebeca.price_comparator.repository.ProductPriceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final DiscountRepository discountRepo;
    private final ProductPriceRepository prodPriceRepo;

    public ProductService(DiscountRepository discountRepo, ProductPriceRepository prodPriceRepo) {
        this.discountRepo = discountRepo;
        this.prodPriceRepo = prodPriceRepo;
    }

    public List<Discount> getBestDiscounts() {
        LocalDate today = LocalDate.now();
        return discountRepo.getAllDiscounts().stream()
                .filter(d -> !d.getFromDate().isAfter(today) && !d.getToDate().isBefore(today))
                .sorted(Comparator.comparingInt(Discount::getPercentage).reversed())
                .collect(Collectors.toList());
    }

    public List<Discount> getNewDiscounts() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return discountRepo.getAllDiscounts().stream()
                .filter(d -> d.getFromDate().isAfter(yesterday))
                .collect(Collectors.toList());
    }

    public List<PriceEntry> getPriceHistory(String productId, String store) {
        return prodPriceRepo.getAllPriceEntries().stream()
                .filter(p -> p.getProductId().equals(productId))
                .filter(p -> store == null || p.getStore().equalsIgnoreCase(store))
                .sorted(Comparator.comparing(PriceEntry::getDate))
                .collect(Collectors.toList());
    }

    //trying price history based on store/brand/category
    public List<PriceEntry> getPriceHistoryFiltered(String store, String brand, String category) {
        Map<String, Product> products = prodPriceRepo.getProductsById();

        return prodPriceRepo.getAllPriceEntries().stream()
                .filter(entry -> {
                    Product p = products.get(entry.getProductId());
                    if (p == null) return false;

                    boolean tryStore = store == null || entry.getStore().equalsIgnoreCase(store);
                    boolean tryBrand = brand == null || p.getBrand().equalsIgnoreCase(brand);
                    boolean tryCategory = category == null || p.getCategory().equalsIgnoreCase(category);

                    return tryBrand && tryCategory && tryStore;
                })
                .sorted(Comparator.comparing(PriceEntry::getDate))
                .collect(Collectors.toList());
    }

    public List<SubstituteDTO> getSubstitutes(String productId, String store) {
        Map<String, Product> products = prodPriceRepo.getProductsById();
        List<PriceEntry> allPrices = prodPriceRepo.getAllPriceEntries();
        List<Discount> allDiscounts = discountRepo.getAllDiscounts();

        Product original = products.get(productId);
        if (original == null) return List.of();

        String category = original.getCategory();
        String name = original.getProductName();
        String unit = original.getPackageUnit();
        List<String> nameParts = Arrays.stream(name.toLowerCase().split("\\s+")).limit(2).toList();

        List<SubstituteDTO> substitutes = allPrices.stream()
                .filter(pe -> !pe.getProductId().equals(productId))
                .filter(pe -> store == null || pe.getStore().equalsIgnoreCase(store))
                .map(pe -> {
                    Product p = products.get(pe.getProductId());
                    if (p == null || !p.getCategory().equalsIgnoreCase(category) ||
                            !p.getPackageUnit().equalsIgnoreCase(unit) || p.getPackageQuantity() <= 0)
                        return null;

                    boolean nameMatches = nameParts.stream()
                            .anyMatch(word -> p.getProductName().toLowerCase().contains(word));
                    if (!nameMatches) return null;

                    int discount = allDiscounts.stream()
                            .filter(d -> d.getProductId().equals(pe.getProductId()))
                            .filter(d -> d.getStore().equalsIgnoreCase(pe.getStore()))
                            .filter(d -> !d.getFromDate().isAfter(pe.getDate()) &&
                                    !d.getToDate().isBefore(pe.getDate()))
                            .mapToInt(Discount::getPercentage)
                            .max()
                            .orElse(0);

                    double originalPrice = pe.getPrice();
                    double finalPrice = originalPrice * (1 - discount / 100.0);
                    double pricePerUnit = finalPrice / p.getPackageQuantity();

                    return new SubstituteDTO(
                            pe.getProductId(),
                            pe.getStore(),
                            pe.getDate(),
                            p.getProductName(),
                            originalPrice,
                            finalPrice,
                            discount,
                            p.getPackageQuantity(),
                            p.getPackageUnit(),
                            pricePerUnit
                    );
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return substitutes.stream()
                .sorted(Comparator.comparingDouble(SubstituteDTO::getPricePerUnit))
                .collect(Collectors.toList());
    }


    //gets price below target for a specific product
    public List<PriceDiscountedDTO> getPriceAlerts(String productId, double targetPrice, String store) {
        List<Discount> allDiscounts = discountRepo.getAllDiscounts();

        List<PriceDiscountedDTO> dtos = prodPriceRepo.getAllPriceEntries().stream()
                .filter(pe -> pe.getProductId().equals(productId))
                .filter(pe -> store == null || pe.getStore().equalsIgnoreCase(store))
                .map(pe -> {
                    double original = pe.getPrice();
                    int discount = allDiscounts.stream()
                            .filter(d -> d.getProductId().equals(pe.getProductId()))
                            .filter(d -> d.getStore().equalsIgnoreCase(pe.getStore()))
                            .filter(d -> !d.getFromDate().isAfter(pe.getDate()) &&
                                    !d.getToDate().isBefore(pe.getDate()))
                            .mapToInt(Discount::getPercentage)
                            .max()
                            .orElse(0);

                    double finalPrice = original * (1 - discount / 100.0);

                    return new PriceDiscountedDTO(
                            pe.getProductId(),
                            pe.getStore(),
                            pe.getDate(),
                            original,
                            finalPrice,
                            discount
                    );
                })
                .collect(Collectors.toList());

        return dtos.stream()
                .filter(dto -> dto.getFinalPrice() < targetPrice)
                .sorted(Comparator.comparingDouble(PriceDiscountedDTO::getFinalPrice))
                .collect(Collectors.toList());
    }

    //price alerts for products
    private final List<PriceAlertDTO> alertRequests = new ArrayList<>();
    public void registerAlert(PriceAlertDTO alert) {
        alertRequests.add(alert);
    }

    public List<Map<String, Object>> getTriggeredAlerts() {
        List<Discount> allDiscounts = discountRepo.getAllDiscounts();
        List<PriceEntry> allPrices = prodPriceRepo.getAllPriceEntries();

        List<Map<String, Object>> triggered = new ArrayList<>();

        for (PriceAlertDTO request : alertRequests) {
            for (PriceEntry pe : allPrices) {
                if (!pe.getProductId().equals(request.getProductId())) continue;
                if (request.getStore() != null && !pe.getStore().equalsIgnoreCase(request.getStore())) continue;

                // Find best discount for that entry
                int discount = allDiscounts.stream()
                        .filter(d -> d.getProductId().equals(pe.getProductId()))
                        .filter(d -> d.getStore().equalsIgnoreCase(pe.getStore()))
                        .filter(d -> !d.getFromDate().isAfter(pe.getDate()) &&
                                !d.getToDate().isBefore(pe.getDate()))
                        .mapToInt(Discount::getPercentage)
                        .max()
                        .orElse(0);

                double finalPrice = pe.getPrice() * (1 - discount / 100.0);

                if (finalPrice < request.getTargetPrice()) {
                    Map<String, Object> result = new LinkedHashMap<>();
                    result.put("productId", pe.getProductId());
                    result.put("store", pe.getStore());
                    result.put("date", pe.getDate());
                    result.put("targetPrice", request.getTargetPrice());
                    result.put("finalPrice", finalPrice);
                    result.put("discount", discount);
                    triggered.add(result);
                }
            }
        }

        return triggered;
    }

    //delete alert
    public boolean deleteAlertByProductId(String productId) {
        return alertRequests.removeIf(alert -> alert.getProductId().equalsIgnoreCase(productId));
    }




}
