package com.wg_planner.views.FloorTasks;

import com.wg_planner.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

@Route(value = "tasks", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Tasks")
@CssImport("./styles/views/tasks/tasks-view.css")
public class TasksView extends VerticalLayout {
    FloorTasksPresenter floorTasksPresenter;
    AutowireCapableBeanFactory beanFactory;
    VerticalLayout allTaskLayout = new VerticalLayout();

    public TasksView(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        floorTasksPresenter = new FloorTasksPresenter();
        beanFactory.autowireBean(floorTasksPresenter);
        floorTasksPresenter.init(allTaskLayout);
        add(allTaskLayout);
    }
}
