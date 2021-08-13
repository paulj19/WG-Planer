package com.wg_planner.views.utils.UINotificationHandler;

import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.resident_admission.EventTimer;
import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.UnauthorizedPages.register.admission.AdmitNewResidentView;
import com.wg_planner.views.utils.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UIEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UIEventHandler.class);
    //sync in function calling saveNotification
    private static UIEventHandler uiEventHandler;

    static {
        uiEventHandler = new UIEventHandler();
    }

    public static UIEventHandler getInstance() {
        return uiEventHandler;
    }

    private UIEventHandler() {
    }

    public synchronized UIEventType createAndSaveUINotification(UIEventType uiEventType, Room room) {
        return createAndSaveUINotification(uiEventType, new ArrayList<Room>(Arrays.asList(room)));
    }
    public synchronized UIEventType createAndSaveUINotification(UIEventType uiEventType, List<Room> roomsInFloor) {
        roomsInFloor.removeIf(room -> room.equals(uiEventType.getSourceRoom()));
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. New notification type {} id {} created. Creating and saving event to the following " +
                        "rooms:",
                SessionHandler.getLoggedInResidentAccount().getId(), uiEventType.getClass().toString(), uiEventType.getId());
        roomsInFloor.forEach(room -> {
            LOGGER.info(LogHandler.getTestRun(), ", {}", room.getId());
            UIEventStore.getInstance().saveNotification(room.getId(),
                    uiEventType);
        });
        setTimer(uiEventType, roomsInFloor);
        return uiEventType;
    }

    private void setTimer(UIEventType uiEventType, List<Room> roomsInFloor) {
        EventTimer.getInstance().setTimer(uiEventType, o -> {
            if (o instanceof UIEventTypeTaskDelete) {
                removeAllNotificationObjectsInFloorOfNotification(((UIEventTypeTaskDelete) o).getId(), roomsInFloor);
            }
        }, uiEventType.getTimeoutIntervalInMillis());
    }

    public synchronized List<UIEventType> getAllNotificationsForRoom(Room room) {
        return UIEventStore.getInstance().getAllNotificationsOfRoom(room.getId());
    }

    public synchronized void removeAllNotificationObjectsInFloorOfNotification(String notificationObjectId,
                                                                               List<Room> roomsInFloor) {
        roomsInFloor.forEach(room -> removeNotification(room.getId(), notificationObjectId));
    }

    public synchronized void removeNotification(Long roomId, String id) {
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Remove notification id {}. Room to be removed from id {}",
                SessionHandler.getLoggedInResidentAccount().getId(), id, roomId);
        UIEventStore.getInstance().removeNotification(roomId, id);
    }

}
