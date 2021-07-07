package com.wg_planner.views.utils.UINotificationHandler;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UINotificationHandler {
    //sync in function calling saveNotification
    //todo make static
    private UINotificationStore uiNotificationStore;
    FloorService floorService;

    @Autowired
    public UINotificationHandler(UINotificationStore uiNotificationStore, FloorService floorService) {
        this.floorService = floorService;
        this.uiNotificationStore = uiNotificationStore;
    }

    public synchronized UINotificationType createAndSaveUINotification(UINotificationType uiNotificationType) {
        List<Room> roomsInFloor =
                floorService.getAllRoomsInFloorByFloorId(uiNotificationType.getSourceRoom().getFloor().getId());
        roomsInFloor.remove(uiNotificationType.getSourceRoom());
        roomsInFloor.forEach(room -> uiNotificationStore.saveNotification(room.getId(),
                uiNotificationType));
        return uiNotificationType;
    }

    public synchronized List<UINotificationType> getAllNotificationsForRoom(Room room) {
        return uiNotificationStore.getAllNotificationsOfRoom(room.getId());
    }

    public synchronized void removeAllNotificationObjectsInFloorOfNotification(Long rejectingRoomFloorId, String notificationObjectId) {
        floorService.getAllRoomsInFloorByFloorId(rejectingRoomFloorId).forEach(room -> removeNotification(
                room.getId(), notificationObjectId));
    }

    public synchronized void removeNotification(Long roomId, String id) {
        uiNotificationStore.removeNotification(roomId, id);
    }

}
