package com.revshop.test;

import com.revshop.service.AuthService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    AuthService authService = new AuthService();

    @Test
    void testRegisterBuyer() {
        String email = "testbuyer_" + System.currentTimeMillis() + "@gmail.com";

        boolean registered = authService.registerBuyer(
                "Test Buyer",
                email,
                "1234",
                "1",
                "1"
        );

        assertTrue(registered);
    }

    @Test
    void testLoginBuyer() {
        // ✅ Use an already registered buyer email/password
        var user = authService.login("seller1@gmail.com", "1234");

        assertNotNull(user);
        assertEquals("SELLER", user.getRole());
    }
}
