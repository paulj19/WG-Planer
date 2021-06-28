package com.wg_planner.views.utils.UINotificationHandler;

import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.commons.lang3.Validate;

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
    public boolean saveNotification(Long residentId, UINotificationContent newNotification) {
        List<UINotificationContent> residentNotificationList = notificationMap.get(residentId);
        if (residentNotificationList != null) {
            return residentNotificationList.add(newNotification); //true if this collection changed as a result of the call
        }
        residentNotificationList = new ArrayList<>(2);
        residentNotificationList.add(newNotification);
        notificationMap.putIfAbsent(residentId, residentNotificationList);
        return true;
    }

    @Override
    public void removeNotification(Long residentId, String id) {
        Optional<UINotificationContent> uiNotificationContentToRemove =
                notificationMap.get(residentId).stream().filter(uiNotificationContent -> uiNotificationContent.getId() == id).collect(Collectors.reducing((a, b) -> null));
        if(!uiNotificationContentToRemove.isPresent()) {
            throw new RuntimeException("notification to remove not found in the map. id: " + id);
        }
        notificationMap.get(residentId).remove(uiNotificationContentToRemove.get());
    }

    @Override
    public void removeAllNotifications(Long residentId) {
        notificationMap.remove(residentId);
    }

    @Override
    public List<UINotificationContent> getAllNotifications(Long residentId) {
        return notificationMap.get(residentId);
    }
}
