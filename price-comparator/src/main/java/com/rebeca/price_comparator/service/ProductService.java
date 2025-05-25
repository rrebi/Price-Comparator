package com.rebeca.price_comparator.service;

import com.rebeca.price_comparator.model.Discount;
import com.rebeca.price_comparator.model.PriceEntry;
import com.rebeca.price_comparator.model.Product;
import com.rebeca.price_comparator.repository.DiscountRepository;
import com.rebeca.price_comparator.repository.ProductPriceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

    public List<PriceEntry> getSubstitutes(String productId, String store) {
        Map<String, Product> products = prodPriceRepo.getProductsById();
        List<PriceEntry> allPrices = prodPriceRepo.getAllPriceEntries();

        Product original = products.get(productId);
        if (original == null) return List.of();

        String category = original.getCategory();
        String name= original.getProductName();
        String unit = original.getPackageUnit();

        List<String> name2 = Arrays.stream(name.toLowerCase().split("\\s+")).limit(2).toList();


        return allPrices.stream()
                .filter(pe -> !pe.getProductId().equals(productId)) // don't includw original product
                .filter(pe -> store == null || pe.getStore().equalsIgnoreCase(store))
                .filter(pe -> {
                    Product p = products.get(pe.getProductId());
                    return p != null
                            && p.getCategory().equalsIgnoreCase(category)
                            && p.getPackageUnit().equalsIgnoreCase(unit)
                            && p.getPackageQuantity() > 0
                            && name2.stream().anyMatch(word -> p.getProductName().toLowerCase().contains(word));
                    //add something to match the name ex lapte to be just lapte not all lactate with cascaval
                })
                .sorted(Comparator.comparingDouble(pe -> {
                    Product p = products.get(pe.getProductId());
                    return pe.getPrice() / p.getPackageQuantity();
                }))
                .collect(Collectors.toList());
    }

    public List<PriceEntry> getPriceAlerts(String productId, double targetPrice, String store) {
        return prodPriceRepo.getAllPriceEntries().stream()
                .filter(pe -> pe.getProductId().equals(productId))
                .filter(pe -> store == null || pe.getStore().equalsIgnoreCase(store))
                .filter(pe -> pe.getPrice() <= targetPrice)
                .sorted(Comparator.comparing(PriceEntry::getPrice))
                .collect(Collectors.toList());
    }


}
