package com.wg_planner.views.utils.UINotificationHandler;

import java.util.List;

public interface UIEventStore {
    boolean saveNotification(Long roomId, UIEventType notificationToStore);

    void removeNotification(Long roomId, String id);

    void removeAllNotificationsOfRoom(Long roomId);

    List<UIEventType> getAllNotificationsOfRoom(Long roomId);
}
