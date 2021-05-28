package com.wg_planner.views.task_cards;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;
import com.wg_planner.backend.entity.Task;

public abstract class TaskCard extends HorizontalLayout {
    protected Task task;

    private TaskCard() {
    }

    public TaskCard(Task task) {
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
//    public abstract <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener);

    public abstract Class getEventClass();


//    public <T extends ComponentEvent<?>> Registration addListener(ComponentEventListener<TaskCard.TaskCardEvent> listener) {
//        return getEventBus().addListener(TaskCard.TaskCardEvent.class, listener);
//    }
}
//    public abstract <T extends ComponentEvent<?>> Registration addListener(ComponentEventListener<TaskCard.TaskCardEvent> listener);

//    public class TaskCardSplitter {
//        public void splitHorizontally(doubleHorizontalLayout horizontalLayoutToSetAsPrimaryLayout,
//                                      HorizontalLayout horizontalLayoutToSetAsSecondaryLayout) {
//
//        }
//    }
