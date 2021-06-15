package com.wg_planner.views.tasks.task_cards;

import com.vaadin.flow.component.button.Button;
import com.wg_planner.backend.entity.Task;

public class TaskCardWithConditionalButton extends TaskCardWithDetails {
    private String buttonAssignLabel;
    private Button buttonAssign;

    public TaskCardWithConditionalButton(TaskCard taskCard, Task task, String buttonAssignLabel) {
        super(taskCard, task);
        this.buttonAssignLabel = buttonAssignLabel;
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
        buttonAssign = new Button(buttonAssignLabel);
        buttonAssign.addClickListener(event -> fireEvent(new AssignEvent(this, task)));
    }
}
