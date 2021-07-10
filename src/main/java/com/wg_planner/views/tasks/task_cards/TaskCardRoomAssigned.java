package com.wg_planner.views.tasks.task_cards;

import com.vaadin.flow.component.html.Span;
import com.wg_planner.backend.entity.Task;

public class TaskCardRoomAssigned extends TaskCardWithDetails {
    Span assignedRoomName = new Span();

    public TaskCardRoomAssigned(TaskCard taskCard, Task task) {
        super(taskCard, task);
        initializeWithAssignedRoom();
        super.add(assignedRoomName);
    }

    private void initializeWithAssignedRoom() {
        assignedRoomName.setMinWidth("500px");
        assignedRoomName.setText(task.getAssignedRoom() != null ? task.getAssignedRoom().getRoomName() : "");
    }

    @Override
    public Class getEventClass() {
        return null;
    }
}
