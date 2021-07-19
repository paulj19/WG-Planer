package com.wg_planner.views.tasks.task_cards;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;
import com.wg_planner.backend.entity.Task;

@CssImport("./styles/views/tasks/tasks-view.css")
public abstract class TaskCard extends HorizontalLayout {
    protected Task task;

    private TaskCard() {
    }

    public TaskCard(Task task) {
        addClassName("task-box");
        this.task = task;
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
    }

    public <T extends ComponentEvent<?>> Registration addListener(ComponentEventListener<T> listener) {
        if (TaskCard.TaskCardEvent.class.isAssignableFrom(getEventClass()))
            return getEventBus().addListener(getEventClass(), listener);
        else
            throw new RuntimeException("Event class must be of type TaskCard.TaskCardEvent");
    }
    public abstract Class getEventClass();
}
