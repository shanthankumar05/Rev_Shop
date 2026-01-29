package com.revshop.dao;

import com.revshop.model.CartItem;
import com.revshop.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    // ✅ Create cart if not exists, return cart_id
    public int getOrCreateCart(int buyerId) {

        String checkSql = "SELECT cart_id FROM cart WHERE buyer_id=?";
        String insertSql = "INSERT INTO cart (buyer_id) VALUES (?)";

        try (Connection con = DBConnection.getConnection()) {

            // 1) check existing cart
            try (PreparedStatement ps = con.prepareStatement(checkSql)) {
                ps.setInt(1, buyerId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("cart_id");
                }
            }

            // 2) create new cart
            try (PreparedStatement ps2 = con.prepareStatement(insertSql)) {
                ps2.setInt(1, buyerId);
                ps2.executeUpdate();
            }

            // 3) fetch new cart id
            try (PreparedStatement ps3 = con.prepareStatement(checkSql)) {
                ps3.setInt(1, buyerId);
                ResultSet rs2 = ps3.executeQuery();
                if (rs2.next()) {
                    return rs2.getInt("cart_id");
                }
            }

        } catch (Exception e) {
            System.out.println("❌ Cart create error: " + e.getMessage());
        }

        return -1;
    }

    // ✅ Add item to cart (if exists update quantity)
    public boolean addToCart(int cartId, int productId, int quantity) {

        String checkSql = "SELECT quantity FROM cart_items WHERE cart_id=? AND product_id=?";
        String insertSql = "INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (?, ?, ?)";
        String updateSql = "UPDATE cart_items SET quantity = quantity + ? WHERE cart_id=? AND product_id=?";

        try (Connection con = DBConnection.getConnection()) {

            // check item exists
            try (PreparedStatement ps = con.prepareStatement(checkSql)) {
                ps.setInt(1, cartId);
                ps.setInt(2, productId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    // update qty
                    try (PreparedStatement ps2 = con.prepareStatement(updateSql)) {
                        ps2.setInt(1, quantity);
                        ps2.setInt(2, cartId);
                        ps2.setInt(3, productId);
                        return ps2.executeUpdate() > 0;
                    }
                }
            }

            // insert new item
            try (PreparedStatement ps3 = con.prepareStatement(insertSql)) {
                ps3.setInt(1, cartId);
                ps3.setInt(2, productId);
                ps3.setInt(3, quantity);
                return ps3.executeUpdate() > 0;
            }

        } catch (Exception e) {
            System.out.println("❌ Add to cart error: " + e.getMessage());
            return false;
        }
    }

    // ✅ View cart items
    public List<CartItem> viewCartItems(int cartId) {

        List<CartItem> list = new ArrayList<>();

        String sql = """
                SELECT ci.cart_item_id, ci.cart_id, ci.product_id, ci.quantity,
                       p.product_name, p.discounted_price
                FROM cart_items ci
                JOIN products p ON ci.product_id = p.product_id
                WHERE ci.cart_id = ?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CartItem item = new CartItem();
                item.setCartItemId(rs.getInt("cart_item_id"));
                item.setCartId(rs.getInt("cart_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setProductName(rs.getString("product_name"));
                item.setPrice(rs.getDouble("discounted_price"));
                list.add(item);
            }

        } catch (Exception e) {
            System.out.println("❌ View cart error: " + e.getMessage());
        }

        return list;
    }

    // ✅ Update quantity
    public boolean updateQuantity(int cartId, int productId, int newQty) {

        String sql = "UPDATE cart_items SET quantity=? WHERE cart_id=? AND product_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, newQty);
            ps.setInt(2, cartId);
            ps.setInt(3, productId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("❌ Update qty error: " + e.getMessage());
            return false;
        }
    }

    // ✅ Remove from cart
    public boolean removeFromCart(int cartId, int productId) {

        String sql = "DELETE FROM cart_items WHERE cart_id=? AND product_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            ps.setInt(2, productId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("❌ Remove cart error: " + e.getMessage());
            return false;
        }
    }
}
