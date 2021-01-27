package com.wg_planner.views.tasks.reset_task;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import org.apache.commons.lang3.Validate;

import java.util.List;

public class ResetTaskPage extends VerticalLayout {

    Room roomSelected;
    ConfirmDialog resetConfirmDialog;
    public ResetTaskPage(Task taskToReset, Room roomRequestingReset, FloorService floorService) {
        resetConfirmDialog = new ConfirmDialog("Confirm Reset",
                "Are you sure you want to reset the Task?", "Reset", this::onConfirmReset,
                "Cancel", this::onCancelReset);
        add(getHeading(taskToReset), getRoomsComboBox(roomRequestingReset, floorService));
        add(getButtonsLayout());
    }

    private H1 getHeading(Task taskToReset) {
        return new H1("Assign " + taskToReset.getTaskName() + " to another roommate");
    }

    private ComboBox<Room> getRoomsComboBox(Room roomRequestingReset, FloorService floorService) {
        ComboBox<Room> roomsInFloorComboBox = new ComboBox<>("Choose a room");
        List<Room> availableRoomsInFloor = floorService.getAllOccupiedAndResidentNotAwayRooms(roomRequestingReset.getFloor());
        List<Room> allRoomsInFloor = floorService.getAllRoomsInFloor(roomRequestingReset.getFloor());
        Validate.isTrue(allRoomsInFloor.contains(roomRequestingReset), "fatal error: room requesting reset should be present in the corresponding floor, room: "+ roomRequestingReset.toString());
        availableRoomsInFloor.remove(roomRequestingReset);
        roomsInFloorComboBox.setItems(availableRoomsInFloor);
        roomsInFloorComboBox.setItemLabelGenerator(Room::getRoomNumber);
        roomsInFloorComboBox.addValueChangeListener(event -> {
            roomSelected = event.getValue();
            if (roomSelected == null) {
                roomsInFloorComboBox.setErrorMessage("room not selected");
            }
        });
//        floorComboBox.setInvalid(false);
        return roomsInFloorComboBox;
    }

    private HorizontalLayout getButtonsLayout() {
        HorizontalLayout registerCancelButtonsLayout = new HorizontalLayout();
        Button reset = new Button("Reset");
        Button cancel = new Button("Cancel");
        reset.addClickListener(event -> resetConfirmDialog.open());
        cancel.addClickListener(event -> fireEvent(new ResetTaskPageEvent.CancelEvent(this, roomSelected)));
        registerCancelButtonsLayout.add(reset, cancel);
        return registerCancelButtonsLayout;
    }

    private void onConfirmReset(ConfirmDialog.ConfirmEvent cancelEvent) {
        fireEvent(new ResetTaskPageEvent.ResetEvent(this, roomSelected));
    }

    private void onCancelReset(ConfirmDialog.CancelEvent cancelEvent) {
        resetConfirmDialog.close();
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public static abstract class ResetTaskPageEvent extends ComponentEvent<ResetTaskPage> {
        private Room roomSelected;

        protected ResetTaskPageEvent(ResetTaskPage source, Room selectedRoom) {
            super(source, false);
            this.roomSelected = selectedRoom;
        }

        public Room getRoomSelected() {
            return roomSelected;
        }

        public static class ResetEvent extends ResetTaskPage.ResetTaskPageEvent {
            ResetEvent(ResetTaskPage source, Room selectedRoom) {
                super(source, selectedRoom);
            }
        }

        public static class CancelEvent extends ResetTaskPage.ResetTaskPageEvent {
            CancelEvent(ResetTaskPage source, Room selectedRoom) {
                super(source, selectedRoom);
            }
        }
    }
}
