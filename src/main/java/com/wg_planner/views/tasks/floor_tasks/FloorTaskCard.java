package com.wg_planner.views.tasks.floor_tasks;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.views.tasks.TaskCard;

public class FloorTaskCard extends TaskCard {

    Button buttonDone = new Button("Done");
    Button buttonRemind = new Button("Remind");

    public FloorTaskCard(Task task, Boolean isTaskAssignedToCurrentAccount) {
        super(task);
        createButtonLayout();
        if (isTaskAssignedToCurrentAccount) {
            taskCardLayout.add(buttonDone);
        } else {
            taskCardLayout.add(buttonRemind);
        }
    }

    private void createButtonLayout() {
        buttonDone.addClickListener(event -> fireEvent(new TaskCardEvent.DoneEvent(this, task)));
        buttonRemind.addClickListener(event -> fireEvent(new TaskCardEvent.RemindEvent(this, task)));
    }

    public HorizontalLayout getTaskCardLayout() {
        return taskCardLayout;
    }

}
