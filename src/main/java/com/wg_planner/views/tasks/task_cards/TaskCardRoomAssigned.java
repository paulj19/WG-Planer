package com.wg_planner.views.tasks.task_cards;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.wg_planner.backend.entity.Task;

//@CssImport("./styles/views/tasks/tasks-view.css")

public class TaskCardRoomAssigned extends TaskCardWithDetails {
    Div assignedRoomName = new Div();

    public TaskCardRoomAssigned(TaskCard taskCard, Task task) {
        super(taskCard, task);
        initializeWithAssignedRoom();
//        addClassName("task-box");
        super.add(assignedRoomName);
        super.getStyle().set("display", "block");
        super.getStyle().set("margin", "0");
    }

    private void initializeWithAssignedRoom() {
//        assignedRoomName.setMinWidth("10em");
        assignedRoomName.setText(task.getAssignedRoom() != null ? task.getAssignedRoom().getRoomName() : "no room assigned");
        assignedRoomName.addClassName("room-name");
    }

    @Override
    public Class getEventClass() {
        return null;
    }
}
