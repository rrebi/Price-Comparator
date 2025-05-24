package com.rebeca.price_comparator.repository;

import com.rebeca.price_comparator.model.Discount;

import java.util.List;

public interface DiscountRepository {
    List<Discount> getAllDiscounts();
}
