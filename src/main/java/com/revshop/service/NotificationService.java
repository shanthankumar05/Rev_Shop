package com.revshop.service;

import com.revshop.dao.NotificationDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotificationService {

    private static final Logger logger =
            LogManager.getLogger(NotificationService.class);

    private NotificationDAO notificationDAO = new NotificationDAO();

    // ✅ View notifications
    public void viewNotifications(int userId) {

        logger.info("Viewing notifications userId={}", userId);

        try {
            notificationDAO.viewNotifications(userId);
        } catch (Exception e) {
            logger.error("Error viewing notifications userId={}", userId, e);
            System.out.println("❌ Unable to load notifications.");
        }
    }

    // ✅ Mark notification as read
    public boolean markRead(int notificationId, int userId) {

        logger.info("Mark notification read notificationId={} userId={}",
                notificationId, userId);

        try {
            boolean result =
                    notificationDAO.markAsRead(notificationId, userId);

            if (!result) {
                logger.warn("Failed to mark notification read notificationId={}",
                        notificationId);
            }

            return result;

        } catch (Exception e) {
            logger.error("Error marking notification read notificationId={}",
                    notificationId, e);
            return false;
        }
    }
}
