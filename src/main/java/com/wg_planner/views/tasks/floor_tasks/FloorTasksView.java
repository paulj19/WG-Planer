package com.wg_planner.views.tasks.floor_tasks;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.main.MainView;
import com.wg_planner.views.tasks.assign_task.AssignTaskView;
import com.wg_planner.views.utils.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

@Route(layout = MainView.class)
@PageTitle("Floor Tasks")
@CssImport("./styles/views/tasks/tasks-view.css")
public class FloorTasksView extends VerticalLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(FloorTasksView.class);
    FloorTasksPresenter floorTasksPresenter;
    AutowireCapableBeanFactory beanFactory;

    public FloorTasksView(AutowireCapableBeanFactory beanFactory) {
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Adding following tasks to floorTaskView.",
                SessionHandler.getLoggedInResidentAccount().getId());
        this.beanFactory = beanFactory;
        addClassName("floor-tasks-layout");
        floorTasksPresenter = new FloorTasksPresenter();
        beanFactory.autowireBean(floorTasksPresenter);
        floorTasksPresenter.init(this);
    }
}
