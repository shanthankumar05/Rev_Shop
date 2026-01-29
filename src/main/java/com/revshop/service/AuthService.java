package com.revshop.service;

import com.revshop.dao.UserDAO;
import com.revshop.model.User;

public class AuthService {

    private UserDAO userDAO = new UserDAO();

    // ✅ Buyer Registration
    public boolean registerBuyer(String name, String email, String password,
                                 String secQ, String secA) {

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("BUYER");
        user.setSecurityQuestion(secQ);
        user.setSecurityAnswer(secA);

        return userDAO.registerUser(user);
    }

    // ✅ Seller Registration
    public boolean registerSeller(String name, String email, String password,
                                  String businessName, String phone,
                                  String secQ, String secA) {

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("SELLER");
        user.setBusinessName(businessName);
        user.setPhone(phone);
        user.setSecurityQuestion(secQ);
        user.setSecurityAnswer(secA);

        return userDAO.registerUser(user);
    }

    // ✅ Login
    public User login(String email, String password) {
        return userDAO.login(email, password);
    }
    public String getSecurityQuestion(String email) {
        return userDAO.getSecurityQuestionByEmail(email);
    }

    public boolean forgotPasswordReset(String email, String secAnswer, String newPassword) {
        return userDAO.resetPassword(email, secAnswer, newPassword);
    }

    public boolean changePassword(int userId, String oldPass, String newPass) {
        return userDAO.changePassword(userId, oldPass, newPass);
    }

}
