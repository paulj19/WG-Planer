package com.wg_planner.views.UnauthorizedPages.create_floor;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.ValidationException;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CreateRoomsView extends VerticalLayout {
    List<CreateRoomView> roomsView = new ArrayList<>();
    Icon addRoomIcon = new Icon(VaadinIcon.PLUS_CIRCLE);

    public CreateRoomsView() {
        addClassName("create-room-task-view");
        addRoomIcon.addClassName("add-icon");
        addRoomIcon.addClickListener(iconClickEvent -> addRoomsView());
    }

    public List<Room> validateAndSave(Floor floorToCreate) throws ValidationException {
        List<Room> validatedRooms = new ArrayList<>();
        for(CreateRoomView r : roomsView) {
            validatedRooms.add(r.validateAndSave(floorToCreate));
        }
        return validatedRooms;
    }


    public void addRoomsView() {
        CreateRoomView roomView = new CreateRoomView(roomsView);
        //reposition the + icon to add rooms
        if (!roomsView.isEmpty() && roomsView.get(roomsView.size() - 1) != null) {
            roomsView.get(roomsView.size() - 1).remove(addRoomIcon);
        }
        remove(addRoomIcon);
        roomsView.add(roomView);
        roomView.add(addRoomIcon);
        add(roomView);
    }

    public void removeRoomsView(Integer numberOfRoomViewToRemove) {
        List<CreateRoomView> removedRoomViews = roomsView.subList(roomsView.size() - numberOfRoomViewToRemove, roomsView.size());
        removedRoomViews.forEach(this::remove);
        removedRoomViews.clear();
    }
}