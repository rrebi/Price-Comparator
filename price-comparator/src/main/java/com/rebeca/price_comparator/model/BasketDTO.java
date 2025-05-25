package com.rebeca.price_comparator.model;

public class BasketDTO {
    private String productId;
    private String store;
    private String productName;
    private double originalPrice;
    private double finalPrice;
    private int discount;
    //private int  quantity;

    public BasketDTO(String productId, String store, String productName, double originalPrice, double finalPrice, int discount) {
        this.productId = productId;
        this.store = store;
        this.productName = productName;
        this.originalPrice = originalPrice;
        this.finalPrice = finalPrice;
        this.discount = discount;
        //this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public String getStore() {
        return store;
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

//    public int getQuantity() {
//        return quantity;
//    }
//
//    public double getTotalCost() {
//        return finalPrice * quantity;
//    }
}
