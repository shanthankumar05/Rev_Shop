package com.revshop.service;

import com.revshop.dao.ProductDAO;
import com.revshop.dao.ReviewDAO;
import com.revshop.model.Product;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;

public class ProductService {

    private static final Logger logger =
            LogManager.getLogger(ProductService.class);

    private ProductDAO productDAO = new ProductDAO();
    private ReviewDAO reviewDAO = new ReviewDAO();

    // ✅ Add product
    public boolean addProduct(Product p) {

        logger.info("Adding product name={} sellerId={}",
                p.getProductName(), p.getSellerId());

        try {
            boolean result = productDAO.addProduct(p);

            if (!result) {
                logger.warn("Failed to add product name={}", p.getProductName());
            }

            return result;

        } catch (Exception e) {
            logger.error("Error adding product", e);
            return false;
        }
    }

    // ✅ View all products
    public List<Product> viewAllProducts() {

        logger.debug("Fetching all products");

        try {
            return productDAO.viewAllProducts();
        } catch (Exception e) {
            logger.error("Error fetching all products", e);
            return Collections.emptyList();
        }
    }

    // ✅ Search products
    public List<Product> searchProducts(String keyword) {

        logger.debug("Searching products keyword={}", keyword);

        try {
            return productDAO.searchProducts(keyword);
        } catch (Exception e) {
            logger.error("Error searching products keyword={}", keyword, e);
            return Collections.emptyList();
        }
    }

    // ✅ Browse by category
    public List<Product> browseByCategory(int categoryId) {

        logger.debug("Browsing products categoryId={}", categoryId);

        try {
            return productDAO.getProductsByCategory(categoryId);
        } catch (Exception e) {
            logger.error("Error browsing categoryId={}", categoryId, e);
            return Collections.emptyList();
        }
    }

    // ✅ Update stock
    public boolean updateStock(int productId, int sellerId, int newStock) {

        logger.info("Updating stock productId={} sellerId={} newStock={}",
                productId, sellerId, newStock);

        try {
            boolean result =
                    productDAO.updateStock(productId, sellerId, newStock);

            if (!result) {
                logger.warn("Stock update failed productId={}", productId);
            }

            return result;

        } catch (Exception e) {
            logger.error("Error updating stock productId={}", productId, e);
            return false;
        }
    }

    // ✅ Update price
    public boolean updatePrice(int productId, int sellerId,
                               double newMrp,
                               double newDiscountedPrice) {

        logger.info("Updating price productId={} sellerId={}",
                productId, sellerId);

        try {
            boolean result =
                    productDAO.updatePrice(productId, sellerId,
                            newMrp, newDiscountedPrice);

            if (!result) {
                logger.warn("Price update failed productId={}", productId);
            }

            return result;

        } catch (Exception e) {
            logger.error("Error updating price productId={}", productId, e);
            return false;
        }
    }

    // ✅ View seller products
    public List<Product> viewSellerProducts(int sellerId) {

        logger.debug("Fetching products sellerId={}", sellerId);

        try {
            return productDAO.viewProductsBySeller(sellerId);
        } catch (Exception e) {
            logger.error("Error fetching seller products sellerId={}", sellerId, e);
            return Collections.emptyList();
        }
    }

    // ✅ Delete product
    public boolean deleteProduct(int productId, int sellerId) {

        logger.info("Deleting product productId={} sellerId={}",
                productId, sellerId);

        try {
            boolean result =
                    productDAO.deleteProduct(productId, sellerId);

            if (!result) {
                logger.warn("Delete failed productId={}", productId);
            }

            return result;

        } catch (Exception e) {
            logger.error("Error deleting product productId={}", productId, e);
            return false;
        }
    }
}
