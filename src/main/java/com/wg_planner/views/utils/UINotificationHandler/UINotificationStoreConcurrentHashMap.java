package com.wg_planner.views.utils.UINotificationHandler;

import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Controller
public class UINotificationStoreConcurrentHashMap implements UINotificationStore {
    private ConcurrentHashMap<Long, List<UINotificationType>> notificationMap = new ConcurrentHashMap<>();

    public UINotificationStoreConcurrentHashMap() {
    }

    @Override
    public boolean saveNotification(Long roomId, UINotificationType newNotification) {
        List<UINotificationType> residentNotificationList = notificationMap.get(roomId);
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
        List<UINotificationType> notificationsOfRoom = notificationMap.get(roomId);
        if(notificationsOfRoom != null){
            notificationsOfRoom.removeIf(uiNotificationType -> uiNotificationType.getId().equals(notificationId));
        }
    }

    @Override
    public void removeAllNotificationsOfRoom(Long roomId) {
        notificationMap.remove(roomId);
    }

    @Override
    public List<UINotificationType> getAllNotificationsOfRoom(Long roomId) {
        if (notificationMap.get(roomId) == null) {
            return Collections.emptyList();
        }
        return notificationMap.get(roomId);
    }

}
