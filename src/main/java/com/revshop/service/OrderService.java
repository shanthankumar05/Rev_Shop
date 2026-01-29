package com.revshop.service;

import com.revshop.dao.OrderDAO;

public class OrderService {

    private OrderDAO orderDAO = new OrderDAO();

    public boolean checkout(int buyerId, String shipAddr, String billAddr, String paymentMethod) {
        return orderDAO.placeOrder(buyerId, shipAddr, billAddr, paymentMethod);
    }
    public void showBuyerOrderHistory(int buyerId) {
        orderDAO.viewBuyerOrders(buyerId);
    }

    public void showSellerOrders(int sellerId) {
        orderDAO.viewSellerOrders(sellerId);
    }

}
