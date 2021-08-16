package com.wg_planner.views.UnauthorizedPages.create_floor;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.Room;

import com.vaadin.flow.component.textfield.TextField;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.backend.utils.LogHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class NewRoomView extends HorizontalLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewRoomView.class);
    TextField roomNameTextField = new TextField("Room Name", "Enter Room number or name");
    Room roomToCreate = new Room();
    private Binder<Room> roomBinder = new BeanValidationBinder<>(Room.class);

    NewRoomView() {
    }

    public void setNewRoomCreateViewList(List<NewRoomView> roomsView) {
        init();
        roomBinder.forField(roomNameTextField).withValidator(roomName -> roomName.length() <= 16,
                "room name should not exceed 16 characters").withValidator(roomName -> roomName.length() >= 1,
                "room name should not be empty").withValidator(roomName -> (roomName != null && !roomName.isEmpty()),
                "room name should not be empty").withValidator(roomName -> isRoomNameUniqueWithFloorCreateForm(roomName.toLowerCase(Locale.GERMANY), roomsView), "room names in a floor must be unique")
                .bind(Room::getRoomName, Room::setRoomName);
    }

    public void setRoomsInFloor(List<Room> roomsAlreadyCreated) {
        init();
        roomBinder.forField(roomNameTextField).withValidator(roomName -> roomName.length() <= 16,
                "room name should not exceed 16 characters").withValidator(roomName -> roomName.length() >= 1,
                "room name should not be empty").withValidator(roomName -> (roomName != null && !roomName.isEmpty()),
                "room name should not be empty").withValidator(roomName -> isTaskNameUnique(roomName.toLowerCase(Locale.GERMANY), roomsAlreadyCreated), "room names" +
                " in a floor " +
                "must be" +
                " unique")
                .bind(Room::getRoomName, Room::setRoomName);
    }

    private void init() {
        setFieldProperties();
        setWidthFull();
        roomNameTextField.setWidthFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(roomNameTextField);
        LOGGER.info(LogHandler.getTestRun(), "create room view called");
    }

    public boolean isTaskNameUnique(String roomName, List<Room> roomsAlreadyCreated) {
        return roomsAlreadyCreated.stream().filter(room -> !Objects.equals(room, roomToCreate)).map(room -> room.getRoomName().toLowerCase(Locale.GERMANY)).noneMatch(roomName::equals);
    }

    public boolean isRoomNameUniqueWithFloorCreateForm(String roomName, List<NewRoomView> roomsView) {
        return roomsView.stream().filter(roomView -> !Objects.equals(roomView, this)).map(roomView -> {
            if(roomView.getRoomToCreate().getRoomName() != null) {
                return roomView.getRoomToCreate().getRoomName().toLowerCase(Locale.GERMANY);
            }
            return null;
        }).noneMatch(roomName::equals);
    }

    private void setFieldProperties() {
        roomNameTextField.setClearButtonVisible(true);
        roomNameTextField.setMaxLength(16);
        roomNameTextField.setRequired(true);
    }

    TextField getRoomNameTextField() {
        return roomNameTextField;
    }

    public Room validateAndSave(Floor floorToCreated) throws ValidationException {
        try {
            roomToCreate.setFloor(floorToCreated);
            roomBinder.writeBean(roomToCreate);
            LOGGER.info(LogHandler.getTestRun(), "create room validate and save. Room details: {}", roomToCreate.toString());
            return roomToCreate;
        } catch (ValidationException e) {
            throw e;
        }
    }

    public Room getRoomToCreate() {
        return roomToCreate;
    }
}
