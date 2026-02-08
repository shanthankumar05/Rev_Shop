package com.revshop.test;

import com.revshop.model.Product;
import com.revshop.service.ProductService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    ProductService productService = new ProductService();

    @Test
    void testAddProductAndVerify() {

        String name = "JUnit Product " + System.currentTimeMillis();

        Product p = new Product();
        p.setSellerId(2);
        p.setCategoryId(1);
        p.setProductName(name);
        p.setDescription("Test product for JUnit");
        p.setMrp(1000);
        p.setDiscountedPrice(900);
        p.setStock(10);
        p.setThresholdStock(5);

        boolean added = productService.addProduct(p);
        assertTrue(added, "Product should be added successfully");

        // ✅ VERIFY FROM DB
        var results = productService.searchProducts(name);

        assertFalse(results.isEmpty(), "Product must exist in DB after insert");
    }
    @Test
    void testSearchProduct() {

        var list = productService.searchProducts("phone");

        assertNotNull(list, "Search result should not be null");
        assertTrue(list.size() > 0, "Search should return at least one product");
    }

}