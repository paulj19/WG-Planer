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
    private FloorService floorService;
    private EventTimer eventTimer;


    @Autowired
    public UIEventHandler(UIEventStore uiEventStore, FloorService floorService, EventTimer eventTimer) {
        this.floorService = floorService;
        this.uiEventStore = uiEventStore;
        this.eventTimer = eventTimer;
    }

    public synchronized UIEventType createAndSaveUINotification(UIEventType uiEventType) {
        List<Room> roomsInFloor =
                floorService.getAllRoomsInFloorByFloorId(uiEventType.getSourceRoom().getFloor().getId());
        roomsInFloor.remove(uiEventType.getSourceRoom());
        roomsInFloor.forEach(room -> uiEventStore.saveNotification(room.getId(),
                uiEventType));
        setTimer(uiEventType);
        return uiEventType;
    }

    private void setTimer(UIEventType uiEventType) {
        eventTimer.setTimer(uiEventType, o -> {
            if (o instanceof UIEventTypeTaskDelete) {
                removeAllNotificationObjectsInFloorOfNotification(((UIEventTypeTaskDelete) o).getSourceRoom().getFloor().getId(),
                        ((UIEventTypeTaskDelete) o).getId());
            }
        }, uiEventType.getTimeoutInterval());
    }

    public synchronized List<UIEventType> getAllNotificationsForRoom(Room room) {
        return uiEventStore.getAllNotificationsOfRoom(room.getId());
    }

    public synchronized void removeAllNotificationObjectsInFloorOfNotification(Long floorId, String notificationObjectId) {
        floorService.getAllRoomsInFloorByFloorId(floorId).forEach(room -> removeNotification(
                room.getId(), notificationObjectId));
    }

    public synchronized void removeNotification(Long roomId, String id) {
        uiEventStore.removeNotification(roomId, id);
    }

}
