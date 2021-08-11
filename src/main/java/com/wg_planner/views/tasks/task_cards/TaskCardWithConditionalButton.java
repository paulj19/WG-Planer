package com.wg_planner.views.tasks.task_cards;

import com.vaadin.flow.component.button.Button;
import com.wg_planner.backend.entity.Task;

public class TaskCardWithConditionalButton extends TaskCardWithDetails {
    private String buttonLabel;
    private Button buttonAssign;

    public TaskCardWithConditionalButton(TaskCard taskCard, Task task, String buttonLabel) {
        super(taskCard, task);
        this.buttonLabel = buttonLabel;
        createAndAddButtonLayout();
        super.add(buttonAssign);
    }

    public static class AssignEvent extends TaskCard.TaskCardEvent {
        public AssignEvent(TaskCard source, Task task) {
            super(source, task);
        }
    }

    @Override
    public Class getEventClass() {
        return AssignEvent.class;
    }

    private void createAndAddButtonLayout() {
        buttonAssign = new Button(buttonLabel);
        buttonAssign.addClassName("task-card-button");
        buttonAssign.addClickListener(event -> fireEvent(new AssignEvent(this, task)));
    }
}
