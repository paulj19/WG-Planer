package com.wg_planner.views.utils.UINotificationHandler;

import com.wg_planner.backend.Service.notification.NotificationContent;

import java.util.List;

public interface NotificationStore {
    boolean saveNotification(Long residentId, UINotificationContent notificationToStore);

    void removeNotification(Long residentId, String id);

    void removeAllNotifications(Long residentId);

    List<UINotificationContent> getAllNotifications(Long residentId);
}
