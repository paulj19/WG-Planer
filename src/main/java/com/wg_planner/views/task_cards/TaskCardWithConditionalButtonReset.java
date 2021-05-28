package com.wg_planner.views.task_cards;

import com.wg_planner.backend.entity.Task;


public class TaskCardWithConditionalButtonReset extends TaskCardWithConditionalButton {
    public TaskCardWithConditionalButtonReset(TaskCard taskCard, Task task) {
        super(taskCard, task, "Reset");
    }
}
