package com.wg_planner.views.tasks.task_cards;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.wg_planner.backend.entity.Task;

public class TaskCardWithConditionalButton extends TaskCardWithDetails {
    private String buttonAssignLabel;
    private Button buttonAssign;
    Span roomSpaceFiller = new Span();

    public TaskCardWithConditionalButton(TaskCard taskCard, Task task, String buttonAssignLabel) {
        super(taskCard, task);
        this.buttonAssignLabel = buttonAssignLabel;
//        roomSpaceFiller.setMinWidth("500px");
        roomSpaceFiller.setText("no room assigned");
        createAndAddButtonLayout();
        super.add(roomSpaceFiller);
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
        buttonAssign.addClassName("task-card-button");
        roomSpaceFiller.addClassName("room-name");
        buttonAssign.addClickListener(event -> fireEvent(new AssignEvent(this, task)));
    }
}
