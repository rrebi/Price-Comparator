package com.rebeca.price_comparator.model;

import java.time.LocalDate;

public class Discount {
    private String productId;
    private String store;
    private LocalDate fromDate;
    private LocalDate toDate;
    private int percentage;

    public Discount() {}

    public Discount(String productId, String store, LocalDate fromDate, LocalDate toDate, int percentage) {
        this.productId = productId;
        this.store = store;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.percentage = percentage;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return "Discount{" +
                "productId='" + productId + '\'' +
                ", store='" + store + '\'' +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", percentage=" + percentage +
                '}';
    }
}
