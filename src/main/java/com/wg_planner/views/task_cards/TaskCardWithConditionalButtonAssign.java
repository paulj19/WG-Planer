package com.wg_planner.views.task_cards;

import com.wg_planner.backend.entity.Task;

public class TaskCardWithConditionalButtonAssign extends TaskCardWithConditionalButton {
    public TaskCardWithConditionalButtonAssign(TaskCard taskCard, Task task) {
        super(taskCard, task, "Assign");
    }
}
