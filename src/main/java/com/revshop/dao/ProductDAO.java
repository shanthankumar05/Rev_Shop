package com.revshop.dao;

import com.revshop.model.Product;
import com.revshop.util.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    private static final Logger logger = LogManager.getLogger(ProductDAO.class);

    // ✅ Seller Add Product
    public boolean addProduct(Product p) {

        String sql = "INSERT INTO products (seller_id, category_id, product_name, description, mrp, discounted_price, stock, threshold_stock) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            logger.info("Adding product: {} (sellerId={})", p.getProductName(), p.getSellerId());

            ps.setInt(1, p.getSellerId());
            ps.setInt(2, p.getCategoryId());
            ps.setString(3, p.getProductName());
            ps.setString(4, p.getDescription());
            ps.setDouble(5, p.getMrp());
            ps.setDouble(6, p.getDiscountedPrice());
            ps.setInt(7, p.getStock());
            ps.setInt(8, p.getThresholdStock());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                logger.info("Product added successfully: {}", p.getProductName());
                return true;
            } else {
                logger.warn("Product add failed (0 rows affected): {}", p.getProductName());
                return false;
            }

        } catch (Exception e) {
            logger.error("Add Product Error: {}", e.getMessage());
            return false;
        }
    }

    // ✅ View All Products (Buyer)
    public List<Product> viewAllProducts() {

        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY product_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setSellerId(rs.getInt("seller_id"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setProductName(rs.getString("product_name"));
                p.setDescription(rs.getString("description"));
                p.setMrp(rs.getDouble("mrp"));
                p.setDiscountedPrice(rs.getDouble("discounted_price"));
                p.setStock(rs.getInt("stock"));
                p.setThresholdStock(rs.getInt("threshold_stock"));
                list.add(p);
            }

            logger.info("Fetched total products: {}", list.size());

        } catch (Exception e) {
            logger.error("View Products Error: {}", e.getMessage());
        }

        return list;
    }

    // ✅ Search Products by Keyword
    public List<Product> searchProducts(String keyword) {

        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE LOWER(product_name) LIKE ? OR LOWER(description) LIKE ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            logger.info("Searching products with keyword={}", keyword);

            ps.setString(1, "%" + keyword.toLowerCase() + "%");
            ps.setString(2, "%" + keyword.toLowerCase() + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setSellerId(rs.getInt("seller_id"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setProductName(rs.getString("product_name"));
                p.setDescription(rs.getString("description"));
                p.setMrp(rs.getDouble("mrp"));
                p.setDiscountedPrice(rs.getDouble("discounted_price"));
                p.setStock(rs.getInt("stock"));
                p.setThresholdStock(rs.getInt("threshold_stock"));
                list.add(p);
            }

            logger.info("Search results count={}", list.size());

        } catch (Exception e) {
            logger.error("Search Error: {}", e.getMessage());
        }

        return list;
    }

    // ✅ Browse by Category
    public List<Product> getProductsByCategory(int categoryId) {

        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            logger.info("Fetching products by categoryId={}", categoryId);

            ps.setInt(1, categoryId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setSellerId(rs.getInt("seller_id"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setProductName(rs.getString("product_name"));
                p.setDescription(rs.getString("description"));
                p.setMrp(rs.getDouble("mrp"));
                p.setDiscountedPrice(rs.getDouble("discounted_price"));
                p.setStock(rs.getInt("stock"));
                p.setThresholdStock(rs.getInt("threshold_stock"));
                list.add(p);
            }

            logger.info("Category browse results count={}", list.size());

        } catch (Exception e) {
            logger.error("Category Browse Error: {}", e.getMessage());
        }

        return list;
    }

    // ✅ Update Stock
    public boolean updateStock(int productId, int sellerId, int newStock) {

        String sql = "UPDATE products SET stock=? WHERE product_id=? AND seller_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            logger.info("Updating stock productId={} sellerId={} newStock={}", productId, sellerId, newStock);

            ps.setInt(1, newStock);
            ps.setInt(2, productId);
            ps.setInt(3, sellerId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                logger.info("Stock updated successfully for productId={}", productId);
                return true;
            } else {
                logger.warn("Stock update failed for productId={} sellerId={}", productId, sellerId);
                return false;
            }

        } catch (Exception e) {
            logger.error("Update Stock Error: {}", e.getMessage());
            return false;
        }
    }

    // ✅ Update Price
    public boolean updatePrice(int productId, int sellerId, double newMrp, double newDiscountedPrice) {

        String sql = "UPDATE products SET mrp=?, discounted_price=? WHERE product_id=? AND seller_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            logger.info("Updating price productId={} sellerId={} newMrp={} newDiscount={}",
                    productId, sellerId, newMrp, newDiscountedPrice);

            ps.setDouble(1, newMrp);
            ps.setDouble(2, newDiscountedPrice);
            ps.setInt(3, productId);
            ps.setInt(4, sellerId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                logger.info("Price updated successfully for productId={}", productId);
                return true;
            } else {
                logger.warn("Price update failed for productId={} sellerId={}", productId, sellerId);
                return false;
            }

        } catch (Exception e) {
            logger.error("Update Price Error: {}", e.getMessage());
            return false;
        }
    }

    // ✅ Delete Product
    public boolean deleteProduct(int productId, int sellerId) {

        String sql = "DELETE FROM products WHERE product_id=? AND seller_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            logger.info("Deleting product productId={} sellerId={}", productId, sellerId);

            ps.setInt(1, productId);
            ps.setInt(2, sellerId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                logger.info("Product deleted successfully productId={}", productId);
                return true;
            } else {
                logger.warn("Product delete failed productId={} sellerId={}", productId, sellerId);
                return false;
            }

        } catch (Exception e) {
            logger.error("Delete Product Error: {}", e.getMessage());
            return false;
        }
    }
}
