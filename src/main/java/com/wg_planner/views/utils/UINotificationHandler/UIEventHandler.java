package com.wg_planner.views.utils.UINotificationHandler;

import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.resident_admission.EventTimer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UIEventHandler {
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
        return createAndSaveUINotification(uiEventType, new ArrayList<>(Arrays.asList(room)));
    }

    public synchronized UIEventType createAndSaveUINotification(UIEventType uiEventType, List<Room> roomsInFloor) {
        roomsInFloor.forEach(room -> UIEventStore.getInstance().saveNotification(room.getId(), uiEventType));
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
        UIEventStore.getInstance().removeNotification(roomId, id);
    }

}
