package com.revshop.model;

public class CartItem {

    private int cartItemId;
    private int cartId;
    private int productId;
    private int quantity;

    // extra display fields
    private String productName;
    private double price;

    public CartItem() {
    }

    // ================= Getters & Setters =================

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // ================= Utility Methods =================

    /**
     * Calculate subtotal for this cart item
     * (non-breaking helper)
     */
    public double getSubTotal() {
        return price * quantity;
    }

    /**
     * Safe toString() for logging/debugging
     */
    @Override
    public String toString() {
        return "CartItem{" +
                "cartItemId=" + cartItemId +
                ", cartId=" + cartId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
