package com.wg_planner.views.UnauthorizedPages.create_floor;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.ValidationException;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.Room;

import java.util.ArrayList;
import java.util.List;

public class NewRoomsView extends VerticalLayout {
    List<NewRoomView> roomsView = new ArrayList<>();
    Icon addRoomIcon = new Icon(VaadinIcon.PLUS_CIRCLE);

    public NewRoomsView() {
        addClassName("create-room-task-view");
        addRoomIcon.addClassName("add-icon");
        addRoomIcon.addClickListener(iconClickEvent -> addRoomsView());
    }

    public List<Room> validateAndSave(Floor floorToCreate) throws ValidationException {
        List<Room> validatedRooms = new ArrayList<>();
        for(NewRoomView r : roomsView) {
            validatedRooms.add(r.validateAndSave(floorToCreate));
        }
        return validatedRooms;
    }


    public void addRoomsView() {
        NewRoomView roomView = NewRoomViewCreator.createTaskFromOtherCreatedRooms(roomsView);
        //reposition the + icon to add rooms
        if (!roomsView.isEmpty() && roomsView.get(roomsView.size() - 1) != null) {
            roomsView.get(roomsView.size() - 1).remove(addRoomIcon);
            roomView.getRoomNameTextField().setAutofocus(true);
        }
        remove(addRoomIcon);
        roomsView.add(roomView);
        roomView.add(addRoomIcon);
        add(roomView);
    }

    public void removeRoomsView(Integer numberOfRoomViewToRemove) {
        List<NewRoomView> removedRoomViews = roomsView.subList(roomsView.size() - numberOfRoomViewToRemove, roomsView.size());
        removedRoomViews.forEach(this::remove);
        removedRoomViews.clear();
    }
}