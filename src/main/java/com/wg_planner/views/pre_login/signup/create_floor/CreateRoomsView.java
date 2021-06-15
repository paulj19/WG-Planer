package com.wg_planner.views.pre_login.signup.create_floor;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreateRoomsView extends VerticalLayout {
    List<CreateRoomView> roomsView = new ArrayList<>();

    public CreateRoomsView() {
    }

    public List<Room> validateAndSave(Floor floorToCreate) {
        return roomsView.stream().map(roomView -> roomView.validateAndSave(floorToCreate)).collect(Collectors.toList());
    }

    public void addRoomView(Integer numberOfRoomViewToAdd) {
        List<CreateRoomView> createdRoomViews = Stream.generate(CreateRoomView::new).limit(numberOfRoomViewToAdd).collect(Collectors.toList());
        createdRoomViews.forEach(this::add);
        roomsView.addAll(createdRoomViews);
    }

    public void removeRoomsView(Integer numberOfRoomViewToRemove) {
        List<CreateRoomView> removedRoomViews = roomsView.subList(roomsView.size() - numberOfRoomViewToRemove, roomsView.size());
        removedRoomViews.forEach(this::remove);
        removedRoomViews.clear();
    }
}