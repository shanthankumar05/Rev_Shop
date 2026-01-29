package com.revshop.dao;

import com.revshop.model.CartItem;
import com.revshop.util.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class OrderDAO {

    private static final Logger logger = LogManager.getLogger(OrderDAO.class);

    // ✅ Place Order from Cart (Transaction)
    public boolean placeOrder(int buyerId, String shipAddr, String billAddr, String paymentMethod) {

        CartDAO cartDAO = new CartDAO();
        int cartId = cartDAO.getOrCreateCart(buyerId);

        List<CartItem> cartItems = cartDAO.viewCartItems(cartId);

        if (cartItems.isEmpty()) {
            System.out.println("🛒 Cart is empty. Cannot checkout!");
            logger.warn("Checkout blocked: Cart empty for buyerId={}", buyerId);
            return false;
        }

        Connection con = null;

        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            logger.info("Checkout started for buyerId={} cartId={} items={}", buyerId, cartId, cartItems.size());

            // ✅ 1) Calculate total
            double total = 0;
            for (CartItem item : cartItems) {
                total += item.getPrice() * item.getQuantity();
            }

            logger.info("Total calculated buyerId={} total={}", buyerId, total);

            // ✅ 2) Insert into orders
            String orderSql = "INSERT INTO orders (buyer_id, shipping_address, billing_address, payment_method, order_status, total_amount) " +
                    "VALUES (?, ?, ?, ?, 'PLACED', ?)";

            try (PreparedStatement ps = con.prepareStatement(orderSql)) {
                ps.setInt(1, buyerId);
                ps.setString(2, shipAddr);
                ps.setString(3, billAddr);
                ps.setString(4, paymentMethod);
                ps.setDouble(5, total);
                ps.executeUpdate();
            }

            // ✅ 3) Get latest order_id
            int orderId = -1;
            String getIdSql = "SELECT MAX(order_id) AS order_id FROM orders WHERE buyer_id=?";

            try (PreparedStatement ps = con.prepareStatement(getIdSql)) {
                ps.setInt(1, buyerId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    orderId = rs.getInt("order_id");
                }
            }

            if (orderId == -1) {
                con.rollback();
                logger.error("Order ID not generated for buyerId={}", buyerId);
                System.out.println("❌ Order ID not generated!");
                return false;
            }

            logger.info("Order created orderId={} buyerId={}", orderId, buyerId);

            // ✅ 4) Insert into order_items + reduce stock
            for (CartItem item : cartItems) {

                int sellerId = -1;
                String sellerSql = "SELECT seller_id FROM products WHERE product_id=?";

                try (PreparedStatement ps = con.prepareStatement(sellerSql)) {
                    ps.setInt(1, item.getProductId());
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        sellerId = rs.getInt("seller_id");
                    }
                }

                if (sellerId == -1) {
                    con.rollback();
                    logger.error("Seller not found for productId={}", item.getProductId());
                    System.out.println("❌ Seller not found for product: " + item.getProductId());
                    return false;
                }

                // insert order_items
                String itemSql = "INSERT INTO order_items (order_id, product_id, seller_id, quantity, price) VALUES (?, ?, ?, ?, ?)";

                try (PreparedStatement ps = con.prepareStatement(itemSql)) {
                    ps.setInt(1, orderId);
                    ps.setInt(2, item.getProductId());
                    ps.setInt(3, sellerId);
                    ps.setInt(4, item.getQuantity());
                    ps.setDouble(5, item.getPrice());
                    ps.executeUpdate();
                }

                // reduce stock
                String stockSql = "UPDATE products SET stock = stock - ? WHERE product_id=? AND stock >= ?";

                try (PreparedStatement ps = con.prepareStatement(stockSql)) {
                    ps.setInt(1, item.getQuantity());
                    ps.setInt(2, item.getProductId());
                    ps.setInt(3, item.getQuantity());

                    int updated = ps.executeUpdate();
                    if (updated == 0) {
                        con.rollback();
                        logger.warn("Not enough stock for productId={} requestedQty={}", item.getProductId(), item.getQuantity());
                        System.out.println("❌ Not enough stock for product: " + item.getProductId());
                        return false;
                    }
                }

                // ✅ seller notification
                String notifySeller = "INSERT INTO notifications (notification_id, user_id, message, is_read) " +
                        "VALUES (notifications_seq.NEXTVAL, ?, ?, 'N')";

                try (PreparedStatement ps = con.prepareStatement(notifySeller)) {
                    ps.setInt(1, sellerId);
                    ps.setString(2, "New order placed! Order ID: " + orderId + " Product ID: " + item.getProductId());
                    ps.executeUpdate();
                }

                logger.info("OrderItem inserted orderId={} productId={} sellerId={} qty={}",
                        orderId, item.getProductId(), sellerId, item.getQuantity());
            }

            // ✅ 5) buyer notification
            String notifyBuyer = "INSERT INTO notifications (notification_id, user_id, message, is_read) " +
                    "VALUES (notifications_seq.NEXTVAL, ?, ?, 'N')";

            try (PreparedStatement ps = con.prepareStatement(notifyBuyer)) {
                ps.setInt(1, buyerId);
                ps.setString(2, "✅ Order placed successfully! Your Order ID: " + orderId);
                ps.executeUpdate();
            }

            // ✅ 6) Clear cart items
            String clearCart = "DELETE FROM cart_items WHERE cart_id=?";

            try (PreparedStatement ps = con.prepareStatement(clearCart)) {
                ps.setInt(1, cartId);
                ps.executeUpdate();
            }

            con.commit();
            logger.info("Checkout committed successfully orderId={}", orderId);

            System.out.println("✅ Order placed successfully! Order ID: " + orderId);
            return true;

        } catch (Exception e) {
            try {
                if (con != null) con.rollback();
            } catch (Exception ignored) {}

            logger.error("Checkout Error buyerId={} : {}", buyerId, e.getMessage());
            System.out.println("❌ Checkout Error: " + e.getMessage());
            return false;

        } finally {
            try {
                if (con != null) con.close();
            } catch (Exception ignored) {}
        }
    }

    // ✅ Buyer Order History
    public void viewBuyerOrders(int buyerId) {

        String sql = "SELECT order_id, total_amount, order_status, created_at " +
                "FROM orders WHERE buyer_id=? ORDER BY order_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, buyerId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== YOUR ORDER HISTORY =====");
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println("Order ID: " + rs.getInt("order_id")
                        + " | Total: " + rs.getDouble("total_amount")
                        + " | Status: " + rs.getString("order_status")
                        + " | Date: " + rs.getTimestamp("created_at"));
            }

            if (!found) {
                System.out.println("⚠ No orders found!");
            }

        } catch (Exception e) {
            logger.error("Buyer Order History Error buyerId={} : {}", buyerId, e.getMessage());
            System.out.println("❌ Buyer Order History Error: " + e.getMessage());
        }
    }

    // ✅ Seller Orders
    public void viewSellerOrders(int sellerId) {

        String sql = "SELECT oi.order_id, oi.product_id, oi.quantity, oi.price, o.buyer_id, o.created_at " +
                "FROM order_items oi " +
                "JOIN orders o ON oi.order_id = o.order_id " +
                "WHERE oi.seller_id=? " +
                "ORDER BY oi.order_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sellerId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== SELLER ORDERS =====");
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println("Order ID: " + rs.getInt("order_id")
                        + " | Product ID: " + rs.getInt("product_id")
                        + " | Qty: " + rs.getInt("quantity")
                        + " | Price: " + rs.getDouble("price")
                        + " | Buyer ID: " + rs.getInt("buyer_id")
                        + " | Date: " + rs.getTimestamp("created_at"));
            }

            if (!found) {
                System.out.println("⚠ No orders for your products yet!");
            }

        } catch (Exception e) {
            logger.error("Seller Orders Error sellerId={} : {}", sellerId, e.getMessage());
            System.out.println("❌ Seller Orders Error: " + e.getMessage());
        }
    }
}
