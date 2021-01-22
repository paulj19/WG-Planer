package com.wg_planner.views.FloorTasks;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.views.Tasks.TaskCard;

public class FloorTaskCard extends TaskCard {

    Button buttonDone = new Button("Done");
    Button buttonReset = new Button("Reset");
    Button buttonRemind = new Button("Remind");

    public FloorTaskCard(Task task, Boolean isTaskAssignedToCurrentAccount) {
        super(task);
        createButtonLayout();
        if (isTaskAssignedToCurrentAccount) {
            taskCardLayout.add(buttonDone);
            taskCardLayout.add(buttonReset);
        } else {
            taskCardLayout.add(buttonRemind);
        }
    }

    private void createButtonLayout() {
        buttonDone.addClickListener(event -> fireEvent(new TaskCardEvent.DoneEvent(this, task)));
        buttonRemind.addClickListener(event -> fireEvent(new TaskCardEvent.RemindEvent(this, task)));
        buttonRemind.addClickListener(event -> fireEvent(new TaskCardEvent.ResetEvent(this, task)));
    }

    public HorizontalLayout getTaskCardLayout() {
        return taskCardLayout;
    }

}
