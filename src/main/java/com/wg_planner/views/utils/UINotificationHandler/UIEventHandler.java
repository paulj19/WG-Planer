package com.wg_planner.views.utils.UINotificationHandler;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UIEventHandler {
    //sync in function calling saveNotification
    //todo make static
    private UIEventStore uiEventStore;
    FloorService floorService;

    @Autowired
    public UIEventHandler(UIEventStore uiEventStore, FloorService floorService) {
        this.floorService = floorService;
        this.uiEventStore = uiEventStore;
    }

    public synchronized UIEventType createAndSaveUINotification(UIEventType uiEventType) {
        List<Room> roomsInFloor =
                floorService.getAllRoomsInFloorByFloorId(uiEventType.getSourceRoom().getFloor().getId());
        roomsInFloor.remove(uiEventType.getSourceRoom());
        roomsInFloor.forEach(room -> uiEventStore.saveNotification(room.getId(),
                uiEventType));
        return uiEventType;
    }

    public synchronized List<UIEventType> getAllNotificationsForRoom(Room room) {
        return uiEventStore.getAllNotificationsOfRoom(room.getId());
    }

    public synchronized void removeAllNotificationObjectsInFloorOfNotification(Long rejectingRoomFloorId, String notificationObjectId) {
        floorService.getAllRoomsInFloorByFloorId(rejectingRoomFloorId).forEach(room -> removeNotification(
                room.getId(), notificationObjectId));
    }

    public synchronized void removeNotification(Long roomId, String id) {
        uiEventStore.removeNotification(roomId, id);
    }

}
