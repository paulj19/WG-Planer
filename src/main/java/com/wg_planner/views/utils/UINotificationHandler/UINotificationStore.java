package com.wg_planner.views.utils.UINotificationHandler;

import java.util.List;

public abstract class UINotificationStore {
    public abstract boolean saveNotification(Long roomId, UINotificationType notificationToStore);

    public abstract void removeNotification(Long roomId, String id);

    public abstract void removeAllNotificationsOfRoom(Long roomId);

    public abstract List<UINotificationType> getAllNotificationsOfRoom(Long roomId);

    public static UINotificationStore getInstance() {
        return UINotificationStoreConcurrentHashMap.getInstance();
    }
}
