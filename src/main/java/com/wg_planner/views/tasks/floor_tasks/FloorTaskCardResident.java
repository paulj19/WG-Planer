package com.wg_planner.views.tasks.floor_tasks;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.views.tasks.TaskCard;

public class FloorTaskCardResident extends TaskCard {

    Button buttonDone = new Button("Done");

    public FloorTaskCardResident(Task task, Boolean isTaskAssignedToCurrentAccount) {
        super(task);
        createAndAddButtonLayout();
    }

    private void createAndAddButtonLayout() {
        buttonDone.addClickListener(event -> fireEvent(new TaskCardEvent.DoneEvent(this, task)));
        taskCardLayout.add(buttonDone);
    }

    public HorizontalLayout getTaskCardLayout() {
        return taskCardLayout;
    }
}

