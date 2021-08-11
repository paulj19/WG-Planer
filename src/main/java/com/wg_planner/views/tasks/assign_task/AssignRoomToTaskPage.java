package com.wg_planner.views.tasks.assign_task;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;

import java.util.List;

public class AssignRoomToTaskPage extends VerticalLayout {
    private Room roomSelected;
    private Task taskToAssign;

    public AssignRoomToTaskPage(Task taskToAssign, FloorService floorService) {
        this.taskToAssign = taskToAssign;
        addClassName("assign-task-view");
        add(getHeading(taskToAssign), getAssignedRoomName(taskToAssign), getRoomsComboBox(taskToAssign, floorService));
        add(getButtonsLayout());
    }

    private Div getAssignedRoomName(Task task) {
        Div assignedRoomName = new Div();
        assignedRoomName.addClassName("room-name");
        assignedRoomName.getStyle().set("margin", "0");
        assignedRoomName.setText(task.getAssignedRoom() != null ? "Currently assigned room: " + task.getAssignedRoom().getRoomName() :
                "Currently assigned room: none");
        return assignedRoomName;
    }

    private H1 getHeading(Task taskToAssign) {
        H1 heading = new H1("Assign Task: " + taskToAssign.getTaskName());
        heading.addClassName("assign-task-heading");
        return heading;
    }

    private ComboBox<Room> getRoomsComboBox(Task taskToAssign, FloorService floorService) {
        ComboBox<Room> roomsInFloorComboBox = new ComboBox<>("Rooms ready to accept tasks:");
        List<Room> availableRoomsInFloor = floorService.getAllOccupiedAndResidentNotAwayRooms(taskToAssign.getFloor());
        if (taskToAssign.getAssignedRoom() != null) {
            availableRoomsInFloor.remove(taskToAssign.getAssignedRoom());
        }
        roomsInFloorComboBox.setItems(availableRoomsInFloor);
        roomsInFloorComboBox.setItemLabelGenerator(Room::getRoomName);
        roomsInFloorComboBox.setValue(null);
        roomsInFloorComboBox.addValueChangeListener(event -> {
            roomSelected = event.getValue();
            if (roomSelected == null) {
                roomsInFloorComboBox.setErrorMessage("room not selected");
            }
        });
        return roomsInFloorComboBox;
    }

    private HorizontalLayout getButtonsLayout() {
        HorizontalLayout registerCancelButtonsLayout = new HorizontalLayout();
        Button assign = new Button("Assign");
        Button cancel = new Button("Cancel");
        assign.addClickListener(event -> fireEvent(new AssignTaskPageEvent.AssignEvent(this, roomSelected, taskToAssign)));
        cancel.addClickListener(event -> fireEvent(new AssignTaskPageEvent.CancelEvent(this, roomSelected, taskToAssign)));
        registerCancelButtonsLayout.add(assign, cancel);
        return registerCancelButtonsLayout;
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public static abstract class AssignTaskPageEvent extends ComponentEvent<AssignRoomToTaskPage> {
        private Room roomSelected;
        private Task taskToAssign;

        protected AssignTaskPageEvent(AssignRoomToTaskPage source, Room selectedRoom, Task taskToAssign) {
            super(source, false);
            this.roomSelected = selectedRoom;
            this.taskToAssign = taskToAssign;
        }

        public Room getRoomSelected() {
            return roomSelected;
        }

        public Task getTaskToAssign() {
            return taskToAssign;
        }

        public static class AssignEvent extends AssignTaskPageEvent {
            AssignEvent(AssignRoomToTaskPage source, Room selectedRoom, Task taskToAssign) {
                super(source, selectedRoom, taskToAssign);
            }
        }

        public static class CancelEvent extends AssignTaskPageEvent {
            CancelEvent(AssignRoomToTaskPage source, Room selectedRoom, Task taskToAssign) {
                super(source, selectedRoom, taskToAssign);
            }
        }
    }
}
