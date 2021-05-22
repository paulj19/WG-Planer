package com.wg_planner.views.create_floor;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.Room;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreateRoomsView extends VerticalLayout {
    List<CreateRoomView> roomsView;

    public CreateRoomsView(Integer numberOfRoomsToCreate) {
        roomsView = Stream.generate(CreateRoomView::new).limit(numberOfRoomsToCreate).collect(Collectors.toList());
        roomsView.forEach(this::add);
    }

    public List<Room> validateAndSave(Floor floorCreated) {
        return roomsView.stream().map(roomView -> roomView.validateAndSave(floorCreated)).collect(Collectors.toList());
    }

    public void addRoomView(Integer numberOfRoomViewToAdd) {
        List<CreateRoomView> createdRoomViews = Stream.generate(CreateRoomView::new).limit(numberOfRoomViewToAdd).collect(Collectors.toList());
        createdRoomViews.forEach(this::add);
        roomsView.addAll(createdRoomViews);

    }

    public void removeRoomsView(Integer numberOfRoomViewToRemove) {
        List<CreateRoomView> removedRoomViews = roomsView.subList(roomsView.size() - numberOfRoomViewToRemove - 1, roomsView.size() - 1);
        removedRoomViews.forEach(this::remove);
        removedRoomViews.clear();
    }
}
