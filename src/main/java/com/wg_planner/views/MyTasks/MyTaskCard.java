package com.wg_planner.views.MyTasks;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.views.Tasks.TaskCard;

public class MyTaskCard extends TaskCard {

    Button buttonDone = new Button("Done");
    Button buttonReset = new Button("Reset");

    public MyTaskCard(Task task) {
        super(task);
        createButtonLayout();
        taskCardLayout.add(buttonDone);
        taskCardLayout.add(buttonReset);
    }

    private void createButtonLayout() {
        buttonDone.addClickListener(event -> fireEvent(new TaskCardEvent.DoneEvent(this, task)));
        buttonReset.addClickListener(event -> fireEvent(new TaskCardEvent.ResetEvent(this, task)));
    }

    public HorizontalLayout getTaskCardLayout() {
        return taskCardLayout;
    }

}
