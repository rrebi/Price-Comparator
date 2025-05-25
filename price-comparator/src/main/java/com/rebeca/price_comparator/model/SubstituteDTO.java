package com.rebeca.price_comparator.model;

import java.time.LocalDate;

public class SubstituteDTO {
    private String productId;
    private String store;
    private LocalDate date;
    private String productName;
    private double originalPrice;
    private double finalPrice;
    private int discount;
    private double quantity;
    private String unit;
    private double pricePerUnit;

    public SubstituteDTO(String productId, String store, LocalDate date, String productName, double originalPrice, double finalPrice, int discount, double quantity, String unit, double pricePerUnit) {
        this.productId = productId;
        this.store = store;
        this.date = date;
        this.productName = productName;
        this.originalPrice = originalPrice;
        this.finalPrice = finalPrice;
        this.discount = discount;
        this.quantity = quantity;
        this.unit = unit;
        this.pricePerUnit = pricePerUnit;
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

    public String getProductName() {
        return productName;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public int getDiscount() {
        return discount;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }
}
