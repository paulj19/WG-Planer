package com.wg_planner.views.tasks.floor_tasks;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.views.tasks.TasksPresenter;
import com.wg_planner.views.tasks.task_cards.TaskCard;
import com.wg_planner.views.tasks.task_cards.TaskCardCreator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
@CssImport("./styles/views/tasks/tasks-view.css")
public class FloorTasksPresenter extends TasksPresenter {
    FloorTasksView floorTasksView;

    public void init(FloorTasksView floorTasksView) {
        this.floorTasksView = floorTasksView;
        super.init();
    }

    @Override
    public void addTasks() {
        floorTasksView.removeAll();
        tasks.forEach(task -> {
            TaskCard taskCard = TaskCardCreator.createAllTaskCard(task, this);
            taskCard.addClassName("task-card");
            floorTasksView.add(taskCard);
        });
    }
}
