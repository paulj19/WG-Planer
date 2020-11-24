package com.wg_planner.views.Tasks;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;
import com.wg_planner.backend.entity.Task;

public class TaskCard extends HorizontalLayout {

    Task task;
    Span taskName = new Span();
    Span assignedRoom = new Span();
    Button buttonDone = new Button("Done");
    Button buttonRemind = new Button("Remind");
    HorizontalLayout taskCardLayout = new HorizontalLayout();

    public TaskCard(Task task, Boolean isTaskAssignedToCurrentAccount) {
        this.task = task;
        taskName.setMinWidth("500px");
        assignedRoom.setMinWidth("500px");
        taskName.setText(task.getTaskName());
        assignedRoom.setText(task.getAssignedRoom().getRoomNumber());

        taskCardLayout.add(taskName, assignedRoom);
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

    public static abstract class TaskCardEvent extends ComponentEvent<TaskCard> {
        private Task task;

        protected TaskCardEvent(TaskCard source, Task task) {
            super(source, false);
            this.task = task;
        }

        public Task getTask() {
            return task;
        }

        public static class RemindEvent extends TaskCardEvent {
            RemindEvent(TaskCard source, Task task) {
                super(source, task);
            }
        }

        public static class DoneEvent extends TaskCardEvent {
            DoneEvent(TaskCard source, Task task) {
                super(source, task);
            }
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
