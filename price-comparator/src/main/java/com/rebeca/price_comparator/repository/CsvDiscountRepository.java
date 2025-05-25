package com.rebeca.price_comparator.repository;

import com.rebeca.price_comparator.model.Discount;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class CsvDiscountRepository implements DiscountRepository {

    // mock discounts
    @Override
    public List<Discount> getAllDiscounts() {
        return List.of(
                new Discount("P001", "lidl", LocalDate.now().minusDays(1), LocalDate.now().plusDays(2), 25),
                new Discount("P002", "profi", LocalDate.now(), LocalDate.now().plusDays(3), 15),
                new Discount("P002", "lidl", LocalDate.now(), LocalDate.now().plusDays(3), 25),
                new Discount("P003", "kaufland", LocalDate.now().minusDays(2), LocalDate.now().minusDays(1), 10)
        );
    }
}
