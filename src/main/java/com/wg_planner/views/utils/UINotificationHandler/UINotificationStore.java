package com.wg_planner.views.utils.UINotificationHandler;

import java.util.List;

public interface UINotificationStore {
    boolean saveNotification(Long roomId, UINotificationType notificationToStore);

    void removeNotification(Long roomId, String id);

    void removeAllNotifications(Long roomId);

    List<UINotificationType> getAllNotifications(Long roomId);
}
