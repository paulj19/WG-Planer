package com.wg_planner.views.utils.UINotificationHandler;

import java.util.List;

public abstract class UIEventStore {
    public abstract boolean saveNotification(Long roomId, UIEventType notificationToStore);

    public abstract void removeNotification(Long roomId, String id);

    public abstract void removeAllNotificationsOfRoom(Long roomId);

    public abstract List<UIEventType> getAllNotificationsOfRoom(Long roomId);

    public static UIEventStore getInstance() {
        return UIEventStoreConcurrentHashMap.getInstance();
    }
}
