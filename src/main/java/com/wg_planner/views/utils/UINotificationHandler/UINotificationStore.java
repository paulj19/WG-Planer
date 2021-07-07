package com.wg_planner.views.utils.UINotificationHandler;

import java.util.List;

public interface UINotificationStore {
    boolean saveNotification(Long roomId, UINotificationType notificationToStore);

    void removeNotification(Long roomId, String id);

    void removeAllNotificationsOfRoom(Long roomId);

    List<UINotificationType> getAllNotificationsOfRoom(Long roomId);
}
