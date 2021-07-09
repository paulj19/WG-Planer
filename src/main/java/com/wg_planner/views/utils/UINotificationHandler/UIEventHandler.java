package com.wg_planner.views.utils.UINotificationHandler;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.resident_admission.EventTimer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UIEventHandler {
    //sync in function calling saveNotification
    //todo make static
    private UIEventStore uiEventStore;


    @Autowired
    public UIEventHandler(UIEventStore uiEventStore) {
        this.uiEventStore = uiEventStore;
    }

    public synchronized UIEventType createAndSaveUINotification(UIEventType uiEventType, List<Room> roomsInFloor) {
        roomsInFloor.remove(uiEventType.getSourceRoom());
        roomsInFloor.forEach(room -> uiEventStore.saveNotification(room.getId(),
                uiEventType));
        setTimer(uiEventType, roomsInFloor);
        return uiEventType;
    }

    private void setTimer(UIEventType uiEventType, List<Room> roomsInFloor) {
        EventTimer.getInstance().setTimer(uiEventType, o -> {
            if (o instanceof UIEventTypeTaskDelete) {
                removeAllNotificationObjectsInFloorOfNotification(((UIEventTypeTaskDelete) o).getId(), roomsInFloor);
            }
        }, uiEventType.getTimeoutInterval());
    }

    public synchronized List<UIEventType> getAllNotificationsForRoom(Room room) {
        return uiEventStore.getAllNotificationsOfRoom(room.getId());
    }

    public synchronized void removeAllNotificationObjectsInFloorOfNotification(String notificationObjectId,
                                                                               List<Room> roomsInFloor) {
        roomsInFloor.forEach(room -> removeNotification(room.getId(), notificationObjectId));
    }

    public synchronized void removeNotification(Long roomId, String id) {
        uiEventStore.removeNotification(roomId, id);
    }

}
