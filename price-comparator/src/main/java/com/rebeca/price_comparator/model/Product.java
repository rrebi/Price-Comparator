package com.rebeca.price_comparator.model;

public class Product {
    private String productId;
    private String productName;
    private String category;
    private String brand;
    private double packageQuantity;
    private String packageUnit;

    public Product(){}
    public Product(String productId, String productName, String category, String brand,
                   double packageQuantity, String packageUnit) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.brand = brand;
        this.packageQuantity = packageQuantity;
        this.packageUnit = packageUnit;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPackageQuantity() {
        return packageQuantity;
    }

    public void setPackageQuantity(double packageQuantity) {
        this.packageQuantity = packageQuantity;
    }

    public String getPackageUnit() {
        return packageUnit;
    }

    public void setPackageUnit(String packageUnit) {
        this.packageUnit = packageUnit;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", category='" + category + '\'' +
                ", brand='" + brand + '\'' +
                ", packageQuantity=" + packageQuantity +
                ", packageUnit='" + packageUnit + '\'' +
                '}';
    }
}
