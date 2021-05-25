package com.wg_planner.views.tasks.floor_tasks;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.views.tasks.TaskCard;

public class FloorTaskCardRestRoomAssigned extends TaskCard {

    Button buttonRemind = new Button("Remind");

    public FloorTaskCardRestRoomAssigned(Task task) {
        super(task);
        createAndAddButtonLayout();
        taskCardLayout.add(buttonRemind);
    }

    private void createAndAddButtonLayout() {
        buttonRemind.addClickListener(event -> fireEvent(new TaskCardEvent.RemindEvent(this, task)));
    }

    public HorizontalLayout getTaskCardLayout() {
        return taskCardLayout;
    }

}

