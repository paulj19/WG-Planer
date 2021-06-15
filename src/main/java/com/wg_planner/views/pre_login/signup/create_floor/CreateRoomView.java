package com.wg_planner.views.pre_login.signup.create_floor;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.Room;

import com.vaadin.flow.component.textfield.TextField;

public class CreateRoomView extends HorizontalLayout {
    TextField roomNameTextField = new TextField("Room number/name");
    Room roomToCreate = new Room();
    private Binder<Room> roomBinder = new BeanValidationBinder<>(Room.class);

    public CreateRoomView() {
        roomBinder.forField(roomNameTextField).bind(Room::getRoomName, Room::setRoomName);
        add(roomNameTextField);
    }

    public Room validateAndSave(Floor floorToCreated) {
        try {
            roomToCreate.setFloor(floorToCreated);
            roomBinder.writeBean(roomToCreate);
            return roomToCreate;
        } catch (ValidationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Room getRoomToCreate() {
        return roomToCreate;
    }
}
