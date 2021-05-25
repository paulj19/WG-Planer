package com.wg_planner.views.tasks;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;
import com.wg_planner.backend.entity.Task;

public class TaskCard extends HorizontalLayout {


    protected Task task;
    Span taskName = new Span();
    Span assignedRoom = new Span();
    protected HorizontalLayout taskCardLayout = new HorizontalLayout();

    public TaskCard(Task task) {
        this.task = task;
        taskName.setMinWidth("500px");
        assignedRoom.setMinWidth("500px");
        taskName.setText(task.getTaskName());
        assignedRoom.setText(task.getAssignedRoom().getRoomName());
        taskCardLayout.add(taskName, assignedRoom);
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

        public static class RemindEvent extends TaskCard.TaskCardEvent {
            public RemindEvent(TaskCard source, Task task) {
                super(source, task);
            }
        }

        public static class DoneEvent extends TaskCard.TaskCardEvent {
            public DoneEvent(TaskCard source, Task task) {
                super(source, task);
            }
        }
        public static class AssignEvent extends TaskCard.TaskCardEvent {
            public AssignEvent(TaskCard source, Task task) {
                super(source, task);
            }
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
