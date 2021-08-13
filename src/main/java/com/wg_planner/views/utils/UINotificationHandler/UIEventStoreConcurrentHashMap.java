package com.wg_planner.views.utils.UINotificationHandler;

import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.utils.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class UIEventStoreConcurrentHashMap extends UIEventStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(UIEventStoreConcurrentHashMap.class);
    private static UIEventStoreConcurrentHashMap uiEventStoreConcurrentHashMap;

    private ConcurrentHashMap<Long, List<UIEventType>> notificationMap = new ConcurrentHashMap<>();

    static {
        uiEventStoreConcurrentHashMap = new UIEventStoreConcurrentHashMap();
    }

    private UIEventStoreConcurrentHashMap() {
    }

    public static UIEventStoreConcurrentHashMap getInstance() {
        return uiEventStoreConcurrentHashMap;
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

        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Notification {} saved for room {}",
                SessionHandler.getLoggedInResidentAccount().getId(), newNotification, roomId);
        logMapContents();
        return true;
    }

    @Override
    public void removeNotification(Long roomId, String notificationId) {
        //        Optional<UINotificationType> uiNotificationContentToRemove =
        //                notificationMap.get(roomId).stream().filter(uiNotificationType -> uiNotificationType.getId() == notificationId).collect(Collectors
        //                .reducing((a, b) -> null));
        //        if (!uiNotificationContentToRemove.isPresent()) {
        //            throw new RuntimeException("notification to remove not found in the map. notificationId: " + notificationId);
        //        }
        //        notificationMap.get(roomId).remove(uiNotificationContentToRemove.get());
        List<UIEventType> notificationsOfRoom = notificationMap.get(roomId);
        if (notificationsOfRoom != null) {
            notificationsOfRoom.removeIf(uiNotificationType -> uiNotificationType.getId().equals(notificationId));
            LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Notification {} removed for room {}",
                    SessionHandler.getLoggedInResidentAccount().getId(), notificationId, roomId);
            logMapContents();
        } else {
            LOGGER.warn("Resident Account id {}. Notifications of Room {} found to be null when trying to remove notification {} ",
                    SessionHandler.getLoggedInResidentAccount().getId(), roomId, notificationId);
        }
    }

    @Override
    public void removeAllNotificationsOfRoom(Long roomId) {
        notificationMap.remove(roomId);
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. removed all notifications for room {}",
                SessionHandler.getLoggedInResidentAccount().getId(), roomId);
        logMapContents();
    }

    @Override
    public List<UIEventType> getAllNotificationsOfRoom(Long roomId) {
        if (notificationMap.get(roomId) == null) {
            return Collections.emptyList();
        }
        return notificationMap.get(roomId);
    }

    private void logMapContents() {
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Map contents:",
                SessionHandler.getLoggedInResidentAccount().getId());
        notificationMap.forEach((aLong, uiEventTypes) -> {
            LOGGER.info(LogHandler.getTestRun(), "Room {}. Notifications ids in room: ", aLong);
            uiEventTypes.forEach(uiEventType -> LOGGER.info(LogHandler.getTestRun(), ", {}", uiEventType.getId()));
        });
    }
}
