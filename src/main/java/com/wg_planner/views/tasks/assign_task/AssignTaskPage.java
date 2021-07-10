package com.wg_planner.views.tasks.assign_task;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.views.utils.ConfirmationDialog;

import java.util.List;

public class AssignTaskPage extends VerticalLayout {

    Room roomSelected;
    ConfirmationDialog assignConfirmDialog;
    public AssignTaskPage(Task taskToAssign, FloorService floorService) {
        assignConfirmDialog = new ConfirmationDialog("Confirm",
                "Are you sure you want to assign the task?", "Assign", this::onConfirmAssign,
                "Cancel", this::onCancelAssign);
        add(getHeading(taskToAssign), getRoomsComboBox(taskToAssign, floorService));
        add(getButtonsLayout());
    }

    private H1 getHeading(Task taskToAssign) {
        return new H1("Assign " + taskToAssign.getTaskName());
    }

    private ComboBox<Room> getRoomsComboBox(Task taskToAssign, FloorService floorService) {
        ComboBox<Room> roomsInFloorComboBox = new ComboBox<>("Choose a room");
        List<Room> availableRoomsInFloor = floorService.getAllOccupiedAndResidentNotAwayRooms(taskToAssign.getFloor());
        roomsInFloorComboBox.setItems(availableRoomsInFloor);
        roomsInFloorComboBox.setItemLabelGenerator(Room::getRoomName);
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
        Button assign = new Button("Assign");
        Button cancel = new Button("Cancel");
        assign.addClickListener(event -> assignConfirmDialog.open());
        cancel.addClickListener(event -> fireEvent(new AssignTaskPageEvent.CancelEvent(this, roomSelected)));
        registerCancelButtonsLayout.add(assign, cancel);
        return registerCancelButtonsLayout;
    }

    private void onConfirmAssign(ConfirmationDialog.ConfirmationDialogEvent.ConfirmEvent cancelEvent) {
        fireEvent(new AssignTaskPageEvent.AssignEvent(this, roomSelected));
    }

    private void onCancelAssign(ConfirmationDialog.ConfirmationDialogEvent.CancelEvent cancelEvent) {
        assignConfirmDialog.close();
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public static abstract class AssignTaskPageEvent extends ComponentEvent<AssignTaskPage> {
        private Room roomSelected;

        protected AssignTaskPageEvent(AssignTaskPage source, Room selectedRoom) {
            super(source, false);
            this.roomSelected = selectedRoom;
        }

        public Room getRoomSelected() {
            return roomSelected;
        }

        public static class AssignEvent extends AssignTaskPageEvent {
            AssignEvent(AssignTaskPage source, Room selectedRoom) {
                super(source, selectedRoom);
            }
        }

        public static class CancelEvent extends AssignTaskPageEvent {
            CancelEvent(AssignTaskPage source, Room selectedRoom) {
                super(source, selectedRoom);
            }
        }
    }
}
