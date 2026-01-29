package com.revshop.dao;

import com.revshop.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FavoritesDAO {

    // ✅ Add to favorites
    public boolean addFavorite(int buyerId, int productId) {
        String sql = "INSERT INTO favorites (favorite_id, buyer_id, product_id) VALUES (favorites_seq.NEXTVAL, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, buyerId);
            ps.setInt(2, productId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("❌ Add Favorite Error: " + e.getMessage());
            return false;
        }
    }

    // ✅ View favorites
    public void viewFavorites(int buyerId) {

        String sql = """
                SELECT f.favorite_id, p.product_id, p.product_name, p.discounted_price
                FROM favorites f
                JOIN products p ON f.product_id = p.product_id
                WHERE f.buyer_id=?
                ORDER BY f.favorite_id DESC
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, buyerId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== YOUR FAVORITES =====");
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(
                        "FavID: " + rs.getInt("favorite_id") +
                                " | ProductID: " + rs.getInt("product_id") +
                                " | Name: " + rs.getString("product_name") +
                                " | Price: " + rs.getDouble("discounted_price")
                );
            }

            if (!found) System.out.println("⚠ No favorites yet!");

        } catch (Exception e) {
            System.out.println("❌ View Favorites Error: " + e.getMessage());
        }
    }

    // ✅ Remove favorite
    public boolean removeFavorite(int buyerId, int productId) {
        String sql = "DELETE FROM favorites WHERE buyer_id=? AND product_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, buyerId);
            ps.setInt(2, productId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("❌ Remove Favorite Error: " + e.getMessage());
            return false;
        }
    }
}
