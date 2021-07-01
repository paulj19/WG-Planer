package com.wg_planner.views.utils.UINotificationHandler;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UINotificationHandler {
    //sync in function calling saveNotification
    private UINotificationStore uiNotificationStore;
    FloorService floorService;

    @Autowired
    public UINotificationHandler(UINotificationStore uiNotificationStore, FloorService floorService) {
        this.floorService = floorService;
        this.uiNotificationStore = uiNotificationStore;
    }

    public synchronized UINotificationType createAndSaveUINotification(UINotificationType uiNotificationType,
                                                                       Room sourceRoom) {
        List<Room> roomsInFloor = floorService.getAllRoomsInFloor(sourceRoom.getFloor().getId());
        roomsInFloor.remove(sourceRoom);
        roomsInFloor.forEach(room -> uiNotificationStore.saveNotification(room.getId(), uiNotificationType));
        return uiNotificationType;
    }

    public synchronized List<UINotificationType> getAllNotificationsForRoom(Room room) {
        return uiNotificationStore.getAllNotifications(room.getId());
    }
}
