package com.revshop.service;

import com.revshop.dao.NotificationDAO;

public class NotificationService {

    private NotificationDAO notificationDAO = new NotificationDAO();

    public void viewNotifications(int userId) {
        notificationDAO.viewNotifications(userId);
    }

    public boolean markRead(int notificationId, int userId) {
        return notificationDAO.markAsRead(notificationId, userId);
    }
}
