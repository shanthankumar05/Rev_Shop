package com.revshop.service;

import com.revshop.dao.ProductDAO;
import com.revshop.model.Product;

import java.util.List;

public class ProductService {

    private ProductDAO productDAO = new ProductDAO();

    public boolean addProduct(Product p) {
        return productDAO.addProduct(p);
    }

    public List<Product> viewAllProducts() {
        return productDAO.viewAllProducts();
    }

    public List<Product> searchProducts(String keyword) {
        return productDAO.searchProducts(keyword);
    }

    public List<Product> browseByCategory(int categoryId) {
        return productDAO.getProductsByCategory(categoryId);
    }
    public boolean updateStock(int productId, int sellerId, int newStock) {
        return productDAO.updateStock(productId, sellerId, newStock);
    }

    public boolean updatePrice(int productId, int sellerId, double newMrp, double newDiscountedPrice) {
        return productDAO.updatePrice(productId, sellerId, newMrp, newDiscountedPrice);
    }

    public boolean deleteProduct(int productId, int sellerId) {
        return productDAO.deleteProduct(productId, sellerId);
    }

}
