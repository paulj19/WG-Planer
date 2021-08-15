package com.wg_planner.views.UnauthorizedPages.create_floor;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.Room;

import com.vaadin.flow.component.textfield.TextField;
import com.wg_planner.backend.utils.LogHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateRoomView extends HorizontalLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateRoomView.class);
    TextField roomNameTextField = new TextField("Room Name", "Enter Room number or name");
    Room roomToCreate = new Room();
    private Binder<Room> roomBinder = new BeanValidationBinder<>(Room.class);

    public CreateRoomView() {
        roomBinder.forField(roomNameTextField).withValidator(roomName -> roomName.length() <= 16,
                "room name should not exceed 16 characters").withValidator(roomName -> roomName.length() >= 1,
                "room name should not be less than 1 character").bind(Room::getRoomName, Room::setRoomName);
        setWidthFull();
        roomNameTextField.setWidthFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(roomNameTextField);
        LOGGER.info(LogHandler.getTestRun(), "create room view called");
    }

    public Room validateAndSave(Floor floorToCreated) {
        try {
            roomToCreate.setFloor(floorToCreated);
            roomBinder.writeBean(roomToCreate);
            LOGGER.info(LogHandler.getTestRun(), "create room validate and save. Room details: {}", roomToCreate.toString() );
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
