package com.wg_planner.views.tasks.floor_tasks;

import com.vaadin.flow.component.dependency.CssImport;
import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.tasks.TasksPresenter;
import com.wg_planner.views.tasks.task_cards.TaskCard;
import com.wg_planner.views.tasks.task_cards.TaskCardCreator;
import com.wg_planner.views.utils.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
@CssImport("./styles/views/tasks/tasks-view.css")
public class FloorTasksPresenter extends TasksPresenter {
    private static final Logger LOGGER = LoggerFactory.getLogger(FloorTasksView.class);
    FloorTasksView floorTasksView;

    public void init(FloorTasksView floorTasksView) {
        this.floorTasksView = floorTasksView;
        super.init();
    }

    @Override
    public void addTasks() {
        floorTasksView.removeAll();
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Adding following tasks to floorTaskView.",
                SessionHandler.getLoggedInResidentAccount().getId());
        floorService.getAllTasksInFloor(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor()).forEach(task -> {
            LOGGER.info(LogHandler.getTestRun(), ", {}", task.getId());
            TaskCard taskCard = TaskCardCreator.createAllTaskCard(task, this);
            taskCard.addClassName("task-card");
            floorTasksView.add(taskCard);
        });
    }
}
