package com.revshop.test;

import com.revshop.model.User;
import com.revshop.service.AuthService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    AuthService authService = new AuthService();

    // ================= POSITIVE TEST =================

    @Test
    void testRegisterBuyer_success() {

        String email = "testbuyer_" + System.currentTimeMillis() + "@gmail.com";

        boolean registered = authService.registerBuyer(
                "Test Buyer",
                email,
                "1234",
                "What is your pet name?",
                "dog"
        );

        assertTrue(registered, "Buyer registration should succeed");
    }

    @Test
    void testLoginSeller_success() {

        // Use EXISTING seller credentials from DB
        User user = authService.login("seller1@gmail.com", "1234");

        assertNotNull(user, "User should not be null");
        assertEquals("SELLER", user.getRole(), "User role should be SELLER");
        assertEquals("seller1@gmail.com", user.getEmail(), "Email should match");
    }

    // ================= NEGATIVE TEST =================

    @Test
    void testLogin_invalidPassword_shouldFail() {

        User user = authService.login("seller1@gmail.com", "wrongpass");

        assertNull(user, "Login should fail for wrong password");
    }
}
