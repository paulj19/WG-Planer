package com.wg_planner.views.utils.UINotificationHandler;

import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.resident_admission.EventTimer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UINotificationHandler {
    //sync in function calling saveNotification
    private static UINotificationHandler uiNotificationHandler;

    static {
        uiNotificationHandler = new UINotificationHandler();
    }

    public static UINotificationHandler getInstance() {
        return uiNotificationHandler;
    }

    private UINotificationHandler() {
    }

    public synchronized UINotificationType createAndSaveUINotification(UINotificationType uiNotificationType, Room room) {
        return createAndSaveUINotification(uiNotificationType, new ArrayList<>(Arrays.asList(room)));
    }

    public synchronized UINotificationType createAndSaveUINotification(UINotificationType uiNotificationType, List<Room> roomsInFloor) {
        roomsInFloor.forEach(room -> UINotificationStore.getInstance().saveNotification(room.getId(), uiNotificationType));
        setTimer(uiNotificationType, roomsInFloor);
        return uiNotificationType;
    }

    private void setTimer(UINotificationType uiNotificationType, List<Room> roomsInFloor) {
        EventTimer.getInstance().setTimer(uiNotificationType, o -> {
            if (o instanceof UINotificationTypeTaskDelete) {
                removeAllNotificationObjectsInFloorOfNotification(((UINotificationTypeTaskDelete) o).getId(), roomsInFloor);
            }
        }, uiNotificationType.getTimeoutIntervalInMillis());
    }

    public synchronized List<UINotificationType> getAllNotificationsForRoom(Room room) {
        return UINotificationStore.getInstance().getAllNotificationsOfRoom(room.getId());
    }

    public synchronized void removeAllNotificationObjectsInFloorOfNotification(String notificationObjectId,
                                                                               List<Room> roomsInFloor) {
        roomsInFloor.forEach(room -> removeNotification(room.getId(), notificationObjectId));
    }

    public synchronized void removeNotification(Long roomId, String id) {
        UINotificationStore.getInstance().removeNotification(roomId, id);
    }

}
