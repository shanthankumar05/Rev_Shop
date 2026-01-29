package com.revshop.model;

public class Product {

    private int productId;
    private int sellerId;
    private int categoryId;

    private String productName;
    private String description;

    private double mrp;
    private double discountedPrice;

    private int stock;
    private int thresholdStock;

    public Product() {}

    // Getters & Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getSellerId() { return sellerId; }
    public void setSellerId(int sellerId) { this.sellerId = sellerId; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getMrp() { return mrp; }
    public void setMrp(double mrp) { this.mrp = mrp; }

    public double getDiscountedPrice() { return discountedPrice; }
    public void setDiscountedPrice(double discountedPrice) { this.discountedPrice = discountedPrice; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getThresholdStock() { return thresholdStock; }
    public void setThresholdStock(int thresholdStock) { this.thresholdStock = thresholdStock; }
}
