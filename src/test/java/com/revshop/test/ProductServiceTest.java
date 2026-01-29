package com.revshop.test;

import com.revshop.model.Product;
import com.revshop.service.ProductService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    ProductService productService = new ProductService();

    @Test
    void testAddProduct() {
        Product p = new Product();

        // ✅ use a valid SELLER user_id from your USERS table
        p.setSellerId(2);

        // ✅ use valid category_id from CATEGORIES
        p.setCategoryId(1);

        p.setProductName("JUnit Product " + System.currentTimeMillis());
        p.setDescription("Test product for JUnit");

        p.setMrp(1000);
        p.setDiscountedPrice(900);

        p.setStock(10);
        p.setThresholdStock(5);

        boolean added = productService.addProduct(p);
        assertTrue(added);
    }

    @Test
    void testSearchProduct() {
        var list = productService.searchProducts("phone");
        assertNotNull(list);
    }
}
