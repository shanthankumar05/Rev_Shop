package com.revshop.model;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private int orderId;
    private int buyerId;

    private String shippingAddress;
    private String billingAddress;

    private String paymentMethod;   // COD / UPI / CARD
    private String orderStatus;     // PLACED / CONFIRMED / SHIPPED etc.

    private double totalAmount;

    private List<OrderItem> items = new ArrayList<>();

    public Order() {
    }

    // ================= Getters & Setters =================

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    // ================= Utility Methods =================

    /**
     * Safe add item (non-breaking helper)
     */
    public void addItem(OrderItem item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(item);
    }

    /**
     * Safe item count
     */
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    /**
     * Safe toString() for logging/debugging
     * (No sensitive data)
     */
    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", buyerId=" + buyerId +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", totalAmount=" + totalAmount +
                ", itemCount=" + getItemCount() +
                '}';
    }
}
