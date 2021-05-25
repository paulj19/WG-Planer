package com.wg_planner.views.tasks.floor_tasks;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.views.tasks.TaskCard;

public class FloorTaskCardUnassigned extends TaskCard {

    Button buttonAssign = new Button("Assign");

    public FloorTaskCardUnassigned(Task task) {
        super(task);
        createAndAddButtonLayout();
    }

    private void createAndAddButtonLayout() {
        buttonAssign.addClickListener(event -> fireEvent(new TaskCardEvent.DoneEvent(this, task)));
        taskCardLayout.add(buttonAssign);
    }

    public HorizontalLayout getTaskCardLayout() {
        return taskCardLayout;
    }
}