package com.revshop.dao;

import com.revshop.util.DBConnection;
import java.sql.*;
import com.revshop.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class ReviewDAO {

    // ✅ Add Review (only once per buyer per product)
    public boolean addReview(int productId, int buyerId, int rating, String comment) {

        String sql = "INSERT INTO reviews (review_id, product_id, buyer_id, rating, review_comment) " +
                "VALUES (reviews_seq.NEXTVAL, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ps.setInt(2, buyerId);
            ps.setInt(3, rating);
            ps.setString(4, comment);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("❌ Add Review Error: " + e.getMessage());
            return false;
        }
    }

    // ✅ View Reviews of a Product
    public void viewReviewsByProduct(int productId) {

        String sql = "SELECT buyer_id, rating, review_comment, created_at " +
                "FROM reviews WHERE product_id=? ORDER BY review_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== PRODUCT REVIEWS =====");
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(
                        "Buyer ID: " + rs.getInt("buyer_id") +
                                " | Rating: " + rs.getInt("rating") +
                                " | Comment: " + rs.getString("review_comment") +
                                " | Date: " + rs.getTimestamp("created_at")
                );
            }

            if (!found) {
                System.out.println("⚠ No reviews yet!");
            }

        } catch (Exception e) {
            System.out.println("❌ View Review Error: " + e.getMessage());
        }
    }

    // ✅ Seller can view reviews of their products
    public void viewReviewsForSeller(int sellerId) {

        String sql = "SELECT p.product_id, p.product_name, r.rating, r.review_comment, r.created_at " +
                "FROM reviews r " +
                "JOIN products p ON r.product_id = p.product_id " +
                "WHERE p.seller_id=? " +
                "ORDER BY r.review_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sellerId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== REVIEWS FOR YOUR PRODUCTS =====");
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(
                        "Product ID: " + rs.getInt("product_id") +
                                " | Name: " + rs.getString("product_name") +
                                " | Rating: " + rs.getInt("rating") +
                                " | Comment: " + rs.getString("review_comment") +
                                " | Date: " + rs.getTimestamp("created_at")
                );
            }

            if (!found) {
                System.out.println("⚠ No reviews for your products yet!");
            }

        } catch (Exception e) {
            System.out.println("❌ Seller Review Error: " + e.getMessage());
        }
    }

    // ✅ Average Rating (optional)
    public void showAverageRating(int productId) {

        String sql = "SELECT AVG(rating) AS avg_rating FROM reviews WHERE product_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double avg = rs.getDouble("avg_rating");
                System.out.println("⭐ Average Rating: " + String.format("%.2f", avg));
            }

        } catch (Exception e) {
            System.out.println("❌ Avg Rating Error: " + e.getMessage());
        }
    }
    public double getAverageRating(int productId) {

        String sql = "SELECT NVL(AVG(rating),0) AS avg_rating FROM reviews WHERE product_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("avg_rating");
            }

        } catch (Exception e) {
            System.out.println("❌ Avg Rating Error: " + e.getMessage());
        }
        return 0;
    }

    public int getReviewCount(int productId) {

        String sql = "SELECT COUNT(*) AS cnt FROM reviews WHERE product_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("cnt");
            }

        } catch (Exception e) {
            System.out.println("❌ Review Count Error: " + e.getMessage());
        }
        return 0;
    }


}
