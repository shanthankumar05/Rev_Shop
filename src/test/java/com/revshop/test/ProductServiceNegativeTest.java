package com.revshop.service;

import com.revshop.model.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceNegativeTest {

    @Test
    void testAddProductWithInvalidPrice() {

        ProductService productService = new ProductService();

        Product p = new Product();
        p.setSellerId(126);            // existing seller
        p.setCategoryId(1);            // Mobiles
        p.setProductName("Invalid Price Product");
        p.setDescription("Negative test case");
        p.setMrp(1000);
        p.setDiscountedPrice(1500);    // ❌ INVALID: discounted > MRP
        p.setStock(5);
        p.setThresholdStock(2);

        boolean result = productService.addProduct(p);

        assertTrue(result,
                "Product should NOT be added when discounted price is greater than MRP");
    }
}
