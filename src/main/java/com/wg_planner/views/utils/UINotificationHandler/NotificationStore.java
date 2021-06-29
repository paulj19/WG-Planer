package com.wg_planner.views.utils.UINotificationHandler;

import com.wg_planner.backend.Service.notification.NotificationContent;

import java.util.List;

public interface NotificationStore {
    boolean saveNotification(Long roomId, UINotificationContent notificationToStore);

    void removeNotification(Long roomId, String id);

    void removeAllNotifications(Long roomId);

    List<UINotificationContent> getAllNotifications(Long roomId);
}
