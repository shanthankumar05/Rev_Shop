package com.revshop.service;

import com.revshop.dao.UserDAO;
import com.revshop.model.User;
import com.revshop.exception.RevShopException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuthService {

    private static final Logger logger =
            LogManager.getLogger(AuthService.class);

    private UserDAO userDAO = new UserDAO();

    // ✅ Buyer Registration
    public boolean registerBuyer(String name, String email, String password,
                                 String secQ, String secA) {

        logger.info("Registering buyer with email={}", email);

        try {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole("BUYER");
            user.setSecurityQuestion(secQ);
            user.setSecurityAnswer(secA);

            boolean result = userDAO.registerUser(user);

            if (!result) {
                logger.warn("Buyer registration failed for email={}", email);
            }

            return result;

        } catch (Exception e) {
            logger.error("Error during buyer registration email={}", email, e);
            return false;
        }
    }

    // ✅ Seller Registration
    public boolean registerSeller(String name, String email, String password,
                                  String businessName, String phone,
                                  String secQ, String secA) {

        logger.info("Registering seller with email={}", email);

        try {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole("SELLER");
            user.setBusinessName(businessName);
            user.setPhone(phone);
            user.setSecurityQuestion(secQ);
            user.setSecurityAnswer(secA);

            boolean result = userDAO.registerUser(user);

            if (!result) {
                logger.warn("Seller registration failed for email={}", email);
            }

            return result;

        } catch (Exception e) {
            logger.error("Error during seller registration email={}", email, e);
            return false;
        }
    }

    // ✅ Login
    public User login(String email, String password) {

        logger.info("Login attempt for email={}", email);

        try {
            User user = userDAO.login(email, password);

            if (user == null) {
                logger.warn("Login failed for email={}", email);
            } else {
                logger.info("Login successful for userId={}", user.getUserId());
            }

            return user;

        } catch (Exception e) {
            logger.error("Login error for email={}", email, e);
            return null;
        }
    }

    // ✅ Get security question
    public String getSecurityQuestion(String email) {

        logger.info("Fetching security question for email={}", email);

        try {
            return userDAO.getSecurityQuestionByEmail(email);
        } catch (Exception e) {
            logger.error("Failed to fetch security question for email={}", email, e);
            return null;
        }
    }

    // ✅ Forgot password reset
    public boolean forgotPasswordReset(String email,
                                       String secAnswer,
                                       String newPassword) {

        logger.info("Forgot password reset attempt for email={}", email);

        try {
            boolean result =
                    userDAO.resetPassword(email, secAnswer, newPassword);

            if (!result) {
                logger.warn("Password reset failed for email={}", email);
            }

            return result;

        } catch (Exception e) {
            logger.error("Password reset error for email={}", email, e);
            return false;
        }
    }

    // ✅ Change password
    public boolean changePassword(int userId,
                                  String oldPass,
                                  String newPass) {

        logger.info("Change password attempt userId={}", userId);

        try {
            boolean result =
                    userDAO.changePassword(userId, oldPass, newPass);

            if (!result) {
                logger.warn("Change password failed userId={}", userId);
            }

            return result;

        } catch (Exception e) {
            logger.error("Change password error userId={}", userId, e);
            return false;
        }
    }
}
