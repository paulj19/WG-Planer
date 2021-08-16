package com.wg_planner.views.UnauthorizedPages.create_floor;

import com.wg_planner.backend.entity.Task;

import java.util.List;

public class NewTaskViewCreator {
    public static NewTaskView createTaskFromFloorTasks(List<Task> taskList) {
        NewTaskView newTaskView = new NewTaskView();
        newTaskView.setTasksInFloor(taskList);
        return newTaskView;
    }

    public static NewTaskView createTaskFromOtherCreatedTasks(List<NewTaskView> otherCreatedTasks) {
        NewTaskView newTaskView = new NewTaskView();
        newTaskView.setNewTaskCreateViewList(otherCreatedTasks);
        return newTaskView;
    }
}
