package com.revshop.dao;

import com.revshop.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NotificationDAO {

    // ✅ View notifications
    public void viewNotifications(int userId) {
        String sql = "SELECT notification_id, message, is_read, created_at " +
                "FROM notifications WHERE user_id=? ORDER BY notification_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n===== NOTIFICATIONS =====");
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println("ID: " + rs.getInt("notification_id")
                        + " | Read: " + rs.getString("is_read")
                        + " | Msg: " + rs.getString("message")
                        + " | Date: " + rs.getTimestamp("created_at"));
            }

            if (!found) {
                System.out.println("⚠ No notifications yet!");
            }

        } catch (Exception e) {
            System.out.println("❌ Notification Error: " + e.getMessage());
        }
    }

    // ✅ Mark as read
    public boolean markAsRead(int notificationId, int userId) {
        String sql = "UPDATE notifications SET is_read='Y' WHERE notification_id=? AND user_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, notificationId);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("❌ Mark read error: " + e.getMessage());
            return false;
        }
    }
}
