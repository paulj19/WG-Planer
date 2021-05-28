package com.wg_planner.views.task_cards;

import com.vaadin.flow.component.html.Span;
import com.wg_planner.backend.entity.Task;

public class TaskCardWithTaskLabel extends TaskCard {
    Span labelTaskName = new Span();

    public TaskCardWithTaskLabel(Task task) {
        super(task);
        initializeLabel();
        super.add(labelTaskName);
    }

    private void initializeLabel() {
        labelTaskName.setMinWidth("500px");
        labelTaskName.setText(task.getTaskName());
    }

    @Override
    public Class getEventClass() {
        return null;
    }
}
