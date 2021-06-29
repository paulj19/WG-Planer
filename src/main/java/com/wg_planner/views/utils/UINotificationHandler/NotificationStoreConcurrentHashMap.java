package com.wg_planner.views.utils.UINotificationHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class NotificationStoreConcurrentHashMap implements NotificationStore {
    private ConcurrentHashMap<Long, List<UINotificationContent>> notificationMap = new ConcurrentHashMap<>();

    public NotificationStoreConcurrentHashMap() {
    }

    @Override
    public boolean saveNotification(Long roomId, UINotificationContent newNotification) {
        List<UINotificationContent> residentNotificationList = notificationMap.get(roomId);
        if (residentNotificationList != null) {
            return residentNotificationList.add(newNotification); //true if this collection changed as a result of the call
        }
        residentNotificationList = new ArrayList<>(2);
        residentNotificationList.add(newNotification);
        notificationMap.putIfAbsent(roomId, residentNotificationList);
        return true;
    }

    @Override
    public void removeNotification(Long roomId, String notificationId) {
        Optional<UINotificationContent> uiNotificationContentToRemove =
                notificationMap.get(roomId).stream().filter(uiNotificationContent -> uiNotificationContent.getId() == notificationId).collect(Collectors.reducing((a, b) -> null));
        if(!uiNotificationContentToRemove.isPresent()) {
            throw new RuntimeException("notification to remove not found in the map. notificationId: " + notificationId);
        }
        notificationMap.get(roomId).remove(uiNotificationContentToRemove.get());
    }

    @Override
    public void removeAllNotifications(Long roomId) {
        notificationMap.remove(roomId);
    }

    @Override
    public List<UINotificationContent> getAllNotifications(Long roomId) {
        return notificationMap.get(roomId);
    }
}
