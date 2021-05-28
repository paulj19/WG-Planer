package com.wg_planner.views.task_cards;

import com.vaadin.flow.component.button.Button;
import com.wg_planner.backend.entity.Task;

public class TaskCardWithButtonDone extends TaskCardWithDetails {
    private final String buttonDoneLabel = "Done";
    private Button buttonDone;

    public TaskCardWithButtonDone(TaskCard taskCard, Task task) {
        super(taskCard, task);
        createAndAddButtonLayout();
        super.add(buttonDone);
    }

    @Override
    public Class getEventClass() {
        return DoneEvent.class;
    }

    public static class DoneEvent extends TaskCard.TaskCardEvent {
        public DoneEvent(TaskCard source, Task task) {
            super(source, task);
        }
    }

    private void createAndAddButtonLayout() {
        buttonDone = new Button(buttonDoneLabel);
        buttonDone.addClickListener(event -> fireEvent(new DoneEvent(this, task)));
    }
}
