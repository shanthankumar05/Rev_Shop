package com.revshop.dao;

import com.revshop.model.User;
import com.revshop.util.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    private static final Logger logger = LogManager.getLogger(UserDAO.class);

    // ✅ Register User
    public boolean registerUser(User user) {

        String sql = "INSERT INTO users " +
                "(name, email, password, role, business_name, phone, security_question, security_answer) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            logger.info("Registering user email={} role={}", user.getEmail(), user.getRole());

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            ps.setString(5, user.getBusinessName());
            ps.setString(6, user.getPhone());
            ps.setString(7, user.getSecurityQuestion());
            ps.setString(8, user.getSecurityAnswer());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                logger.info("User registered successfully email={}", user.getEmail());
                return true;
            } else {
                logger.warn("User registration failed (0 rows) email={}", user.getEmail());
                return false;
            }

        } catch (Exception e) {
            logger.error("Register error email={}", user.getEmail(), e);
            return false;
        }
    }

    // ✅ Login User
    public User login(String email, String password) {

        String sql = "SELECT * FROM users WHERE email=? AND password=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            logger.info("Login attempt email={}", email);

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setBusinessName(rs.getString("business_name"));
                user.setPhone(rs.getString("phone"));

                logger.info("Login success email={} role={}", email, user.getRole());
                return user;
            }

            logger.warn("Login failed email={}", email);
            return null;

        } catch (Exception e) {
            logger.error("Login error email={}", email, e);
            return null;
        }
    }

    // ✅ Get Security Question
    public String getSecurityQuestionByEmail(String email) {

        String sql = "SELECT security_question FROM users WHERE email=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            logger.info("Fetching security question email={}", email);

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("security_question");
            }

            logger.warn("Security question not found email={}", email);
            return null;

        } catch (Exception e) {
            logger.error("Fetch security question error email={}", email, e);
            return null;
        }
    }

    // ✅ Reset Password
    public boolean resetPassword(String email, String securityAnswer, String newPassword) {

        String sql = "UPDATE users SET password=? WHERE email=? AND security_answer=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            logger.info("Reset password attempt email={}", email);

            ps.setString(1, newPassword);
            ps.setString(2, email);
            ps.setString(3, securityAnswer);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                logger.info("Password reset success email={}", email);
                return true;
            } else {
                logger.warn("Password reset failed email={}", email);
                return false;
            }

        } catch (Exception e) {
            logger.error("Reset password error email={}", email, e);
            return false;
        }
    }

    // ✅ Change Password
    public boolean changePassword(int userId, String oldPassword, String newPassword) {

        String sql = "UPDATE users SET password=? WHERE user_id=? AND password=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            logger.info("Change password attempt userId={}", userId);

            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            ps.setString(3, oldPassword);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                logger.info("Password changed userId={}", userId);
                return true;
            } else {
                logger.warn("Password change failed userId={}", userId);
                return false;
            }

        } catch (Exception e) {
            logger.error("Change password error userId={}", userId, e);
            return false;
        }
    }
}
