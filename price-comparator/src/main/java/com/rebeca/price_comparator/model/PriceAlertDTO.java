package com.rebeca.price_comparator.model;

import java.time.LocalDate;

public class PriceAlertDTO {
    private String productId;
    private String store; // optional
    private double targetPrice;

    public PriceAlertDTO(String productId, String store, double targetPrice) {
        this.productId = productId;
        this.store = store;
        this.targetPrice = targetPrice;
    }

    public String getProductId() {
        return productId;
    }

    public String getStore() {
        return store;
    }

    public double getTargetPrice() {
        return targetPrice;
    }
}
