package com.wg_planner.views.tasks.my_tasks;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.views.tasks.TaskCard;

public class MyTaskCard extends TaskCard {

    Button buttonDone = new Button("Done");
    Button buttonReassign = new Button("Reassign");

    public MyTaskCard(Task task) {
        super(task);
        createButtonLayout();
        taskCardLayout.add(buttonDone);
        taskCardLayout.add(buttonReassign);
    }

    private void createButtonLayout() {
        buttonDone.addClickListener(event -> fireEvent(new TaskCardEvent.DoneEvent(this, task)));
        buttonReassign.addClickListener(event -> fireEvent(new TaskCardEvent.AssignEvent(this, task)));
    }

    public HorizontalLayout getTaskCardLayout() {
        return taskCardLayout;
    }

}
