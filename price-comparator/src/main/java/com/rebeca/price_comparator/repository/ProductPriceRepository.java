package com.rebeca.price_comparator.repository;

import com.rebeca.price_comparator.model.PriceEntry;

import java.util.List;

public interface ProductPriceRepository {
    List<PriceEntry> getAllPriceEntries();
}
