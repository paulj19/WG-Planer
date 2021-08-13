package com.wg_planner.views.tasks.my_tasks;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.tasks.TasksPresenter;
import com.wg_planner.views.tasks.floor_tasks.FloorTasksView;
import com.wg_planner.views.tasks.task_cards.TaskCard;
import com.wg_planner.views.tasks.task_cards.TaskCardCreator;
import com.wg_planner.views.utils.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@CssImport("./styles/views/tasks/tasks-view.css")
public class MyTasksPresenter extends TasksPresenter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyTasksPresenter.class);
    MyTasksView myTasksView;
    @Autowired
    TaskService taskService;

    public void init(MyTasksView myTasksView) {
        this.myTasksView = myTasksView;
        super.init();
    }

    @Override
    public void addTasks() {
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Adding following tasks to myTasksView.",
                SessionHandler.getLoggedInResidentAccount().getId());
        myTasksView.removeAll();
        taskService.getAllTasksOfRoom(SessionHandler.getLoggedInResidentAccount().getRoom().getId()).forEach(task ->
        {
            LOGGER.info(LogHandler.getTestRun(), ", {}", task.getId());
            TaskCard taskCard = TaskCardCreator.createLoggedInResidentTaskCard(task, this);
            taskCard.addClassName("task-card");
            myTasksView.add(taskCard);
        });
    }
}
