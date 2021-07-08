package com.wg_planner.views.utils.UINotificationHandler;

import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class UIEventStoreConcurrentHashMap implements UIEventStore {
    private ConcurrentHashMap<Long, List<UIEventType>> notificationMap = new ConcurrentHashMap<>();

    public UIEventStoreConcurrentHashMap() {
    }

    @Override
    public boolean saveNotification(Long roomId, UIEventType newNotification) {
        List<UIEventType> residentNotificationList = notificationMap.get(roomId);
        if (residentNotificationList != null) {
            return residentNotificationList.add(newNotification); //true if this collection changed as a result of
            // the call
        }
        residentNotificationList = new ArrayList<>(2);
        residentNotificationList.add(newNotification);
        notificationMap.putIfAbsent(roomId, residentNotificationList);
        return true;
    }

    @Override
    public void removeNotification(Long roomId, String notificationId) {
//        Optional<UINotificationType> uiNotificationContentToRemove =
//                notificationMap.get(roomId).stream().filter(uiNotificationType -> uiNotificationType.getId() == notificationId).collect(Collectors.reducing((a, b) -> null));
//        if (!uiNotificationContentToRemove.isPresent()) {
//            throw new RuntimeException("notification to remove not found in the map. notificationId: " + notificationId);
//        }
//        notificationMap.get(roomId).remove(uiNotificationContentToRemove.get());
        List<UIEventType> notificationsOfRoom = notificationMap.get(roomId);
        if(notificationsOfRoom != null){
            notificationsOfRoom.removeIf(uiNotificationType -> uiNotificationType.getId().equals(notificationId));
        }
    }

    @Override
    public void removeAllNotificationsOfRoom(Long roomId) {
        notificationMap.remove(roomId);
    }

    @Override
    public List<UIEventType> getAllNotificationsOfRoom(Long roomId) {
        if (notificationMap.get(roomId) == null) {
            return Collections.emptyList();
        }
        return notificationMap.get(roomId);
    }

}
