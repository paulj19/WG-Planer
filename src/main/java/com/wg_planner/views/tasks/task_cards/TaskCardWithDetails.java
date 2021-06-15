package com.wg_planner.views.tasks.task_cards;

import com.wg_planner.backend.entity.Task;

public abstract class TaskCardWithDetails extends TaskCard {
    private TaskCard taskCard;

    public TaskCardWithDetails(TaskCard taskCard, Task task) {
        super(task);
        this.taskCard = taskCard;
        addComponentsInTaskCardToNew();
    }

    private void addComponentsInTaskCardToNew() {
        super.add(taskCard);
    }
}