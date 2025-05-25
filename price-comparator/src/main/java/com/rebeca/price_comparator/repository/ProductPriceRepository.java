package com.rebeca.price_comparator.repository;

import com.rebeca.price_comparator.model.PriceEntry;
import com.rebeca.price_comparator.model.Product;

import java.util.List;
import java.util.Map;

public interface ProductPriceRepository {
    List<PriceEntry> getAllPriceEntries();

    Map<String, Product> getProductsById();
}
