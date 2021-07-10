package com.wg_planner.views.tasks.my_tasks;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.views.tasks.TasksPresenter;
import com.wg_planner.views.tasks.task_cards.TaskCardCreator;
import com.wg_planner.views.utils.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;

public class MyTasksPresenter extends TasksPresenter {
    VerticalLayout allTaskLayout;
    @Autowired
    TaskService taskService;

    public void init(VerticalLayout allTaskLayout) {
        this.allTaskLayout = allTaskLayout;
        super.init();
    }

    @Override
    public void addTasks() {
        allTaskLayout.removeAll();
        taskService.getAllTasksOfRoom(SessionHandler.getLoggedInResidentAccount().getRoom().getId()).forEach(task -> allTaskLayout.add(TaskCardCreator.createLoggedInResidentTaskCard(task, this)));
    }
}
