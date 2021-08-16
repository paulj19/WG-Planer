package com.wg_planner.views.UnauthorizedPages.create_floor;

import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;

import java.util.List;

public class NewRoomViewCreator {

    public static NewRoomView createRoomFromFloorRooms(List<Room> roomList) {
        NewRoomView newRoomView = new NewRoomView();
        newRoomView.setRoomsInFloor(roomList);
        return newRoomView;
    }

    public static NewRoomView createTaskFromOtherCreatedRooms(List<NewRoomView> otherCreatedRooms) {
        NewRoomView newRoomView = new NewRoomView();
        newRoomView.setNewRoomCreateViewList(otherCreatedRooms);
        return newRoomView;
    }
}
