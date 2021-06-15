package com.wg_planner.views.tasks.task_cards;

import com.vaadin.flow.component.button.Button;
import com.wg_planner.backend.entity.Task;

public class TaskCardWithButtonRemind extends TaskCardWithDetails {
    private final String buttonRemindLabel = "Remind";
    private Button buttonRemind;

    public TaskCardWithButtonRemind(TaskCard taskCard, Task task) {
        super(taskCard, task);
        createAndAddButtonLayout();
        super.add(buttonRemind);
    }

    public static class RemindEvent extends TaskCard.TaskCardEvent {
        public RemindEvent(TaskCard source, Task task) {
            super(source, task);
        }
    }

    @Override
    public Class getEventClass() {
        return RemindEvent.class;
    }

    private void createAndAddButtonLayout() {
        buttonRemind = new Button(buttonRemindLabel);
        buttonRemind.addClickListener(event -> fireEvent(new RemindEvent(this, task)));
    }
}