package com.rebeca.price_comparator.repository;

import com.rebeca.price_comparator.model.PriceEntry;
import com.rebeca.price_comparator.model.Product;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Repository
public class CsvProductPriceRepository implements ProductPriceRepository {

    private final Map<String, Product> productsById = new HashMap<>();
    private final List<PriceEntry> priceEntries = new ArrayList<>();

    public CsvProductPriceRepository() {
        // Mock products
        productsById.put("P001", new Product("P001", "Lapte Zuzu", "lactate", "Zuzu", 1, "l"));
        productsById.put("P002", new Product("P002", "Cola", "bauturi", "Coca-Cola", 2, "l"));
        productsById.put("P003", new Product("P003", "Lapte Pilos", "lactate", "Pilos", 2, "l"));
        productsById.put("P004", new Product("P004", "Branza", "lactate", "Pilos", 2, "l"));

        // Mock price
        priceEntries.add(new PriceEntry("P001", "lidl", LocalDate.now().minusDays(3), 8.99, "RON"));
        priceEntries.add(new PriceEntry("P001", "kaufland", LocalDate.now().minusDays(2), 9.29, "RON"));
        priceEntries.add(new PriceEntry("P001", "lidl", LocalDate.now().minusDays(1), 8.89, "RON"));
        priceEntries.add(new PriceEntry("P003", "lidl", LocalDate.now().minusDays(3), 11, "RON"));
        priceEntries.add(new PriceEntry("P004", "lidl", LocalDate.now().minusDays(3), 11, "RON"));

        priceEntries.add(new PriceEntry("P001", "lidl", LocalDate.now(), 8.79, "RON"));
        priceEntries.add(new PriceEntry("P002", "lidl", LocalDate.now(), 11.29, "RON"));


        priceEntries.add(new PriceEntry("P002", "lidl", LocalDate.now().minusDays(2), 11.49, "RON"));
        priceEntries.add(new PriceEntry("P002", "kaufland", LocalDate.now().minusDays(1), 10.99, "RON"));
    }

    @Override
    public List<PriceEntry> getAllPriceEntries() {
        return priceEntries;
    }

    public Map<String, Product> getProductsById() {
        return productsById;
    }
}
