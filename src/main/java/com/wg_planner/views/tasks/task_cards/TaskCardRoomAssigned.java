package com.wg_planner.views.tasks.task_cards;

import com.vaadin.flow.component.html.Div;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.backend.utils.consensus.ConsensusHandler;

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
        boolean taskWaitingToBeDeleted = ConsensusHandler.getInstance().isObjectWaitingForConsensus(task.getId());
        //todo fix css alignment
        assignedRoomName.setText(taskWaitingToBeDeleted ? "waiting to be deleted" :
                task.getAssignedRoom() != null ? task.getAssignedRoom().getRoomName() : "no room assigned");
        assignedRoomName.addClassName("room-name");
    }

    @Override
    public Class getEventClass() {
        return null;
    }
}
