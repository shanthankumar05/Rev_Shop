package com.revshop.service;

import com.revshop.dao.OrderDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OrderService {

    private static final Logger logger =
            LogManager.getLogger(OrderService.class);

    private OrderDAO orderDAO = new OrderDAO();

    // ✅ Checkout / place order
    public boolean checkout(int buyerId,
                            String shipAddr,
                            String billAddr,
                            String paymentMethod) {

        logger.info("Checkout started buyerId={} paymentMethod={}",
                buyerId, paymentMethod);

        try {
            boolean result =
                    orderDAO.placeOrder(buyerId, shipAddr, billAddr, paymentMethod);

            if (!result) {
                logger.warn("Checkout failed buyerId={}", buyerId);
            }

            return result;

        } catch (Exception e) {
            logger.error("Checkout error buyerId={}", buyerId, e);
            return false;
        }
    }

    // ✅ Buyer order history
    public void showBuyerOrderHistory(int buyerId) {

        logger.info("Viewing buyer order history buyerId={}", buyerId);

        try {
            orderDAO.viewBuyerOrders(buyerId);
        } catch (Exception e) {
            logger.error("Error viewing buyer orders buyerId={}", buyerId, e);
        }
    }

    // ✅ Seller order history
    public void showSellerOrders(int sellerId) {

        logger.info("Viewing seller orders sellerId={}", sellerId);

        try {
            orderDAO.viewSellerOrders(sellerId);
        } catch (Exception e) {
            logger.error("Error viewing seller orders sellerId={}", sellerId, e);
        }
    }
}
