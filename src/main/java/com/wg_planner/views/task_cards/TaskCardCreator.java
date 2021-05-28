package com.wg_planner.views.task_cards;

import com.wg_planner.backend.entity.Task;
import com.wg_planner.views.tasks.TasksPresenter;
import com.wg_planner.views.utils.SessionHandler;

public class TaskCardCreator {
    public static TaskCard createLoggedInResidentTaskCard(Task task, TasksPresenter tasksPresenter) {
        //task assigned to the logged in resident
        TaskCard taskCardDone = new TaskCardWithButtonDone(new TaskCardRoomAssigned(new TaskCardWithTaskLabel(task), task), task);
        taskCardDone.addListener(tasksPresenter::taskDoneCallBack);
        TaskCard taskCard = new TaskCardWithConditionalButtonReset(taskCardDone, task);
        taskCard.addListener(tasksPresenter::taskAssignCallBack);
        return taskCard;
    }

    public static TaskCard createAllTaskCard(Task task, TasksPresenter tasksPresenter) {
        if (task.getAssignedRoom() == null) {
            TaskCard taskCard = new TaskCardWithConditionalButtonAssign(new TaskCardWithTaskLabel(task), task);
            taskCard.addListener(tasksPresenter::taskAssignCallBack);
            return taskCard;
        }
        if (SessionHandler.getLoggedInResidentAccount().getRoom().getId().equals(task.getAssignedRoom().getId())) {
            TaskCard taskCard =
                    new TaskCardWithButtonDone(new TaskCardRoomAssigned(new TaskCardWithTaskLabel(task), task), task);
            taskCard.addListener(tasksPresenter::taskDoneCallBack);
            return taskCard;
        } else {
            TaskCard taskCard = new TaskCardWithButtonRemind(new TaskCardRoomAssigned(new TaskCardWithTaskLabel(task), task), task);
            taskCard.addListener(tasksPresenter::taskRemindCallBack);
            return taskCard;
        }
    }
}
