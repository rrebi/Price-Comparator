package com.rebeca.price_comparator.model;

import java.time.LocalDate;

public class PriceEntry {
    private String productId;
    private String store;
    private LocalDate date;
    private double price;
    private String currency;

    public PriceEntry() {}

    public PriceEntry(String productId, String store, LocalDate date, double price, String currency) {
        this.productId = productId;
        this.store = store;
        this.date = date;
        this.price = price;
        this.currency = currency;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "PriceEntry{" +
                "productId='" + productId + '\'' +
                ", store='" + store + '\'' +
                ", date=" + date +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                '}';
    }
}
