package com.rebeca.price_comparator.model;

import java.time.LocalDate;

public class PriceAlertDTO {
    private String productId;
    private String store;
    private LocalDate date;
    private double originalPrice;
    private double finalPrice;
    private int discount;

    public PriceAlertDTO(String productId, String store, LocalDate date, double originalPrice, double finalPrice, int discount) {
        this.productId = productId;
        this.store = store;
        this.date = date;
        this.originalPrice = originalPrice;
        this.finalPrice = finalPrice;
        this.discount = discount;
    }

    public String getProductId() {
        return productId;
    }

    public String getStore() {
        return store;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public int getDiscount() {
        return discount;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

}
