package com.rebeca.price_comparator.service;

import com.rebeca.price_comparator.model.Discount;
import com.rebeca.price_comparator.model.PriceEntry;
import com.rebeca.price_comparator.repository.DiscountRepository;
import com.rebeca.price_comparator.repository.ProductPriceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
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


}
