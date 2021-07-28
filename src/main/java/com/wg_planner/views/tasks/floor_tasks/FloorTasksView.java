package com.wg_planner.views.tasks.floor_tasks;

import com.wg_planner.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

@Route(value = "floor_tasks", layout = MainView.class)
@RouteAlias(value = "floor_tasks", layout = MainView.class)
@PageTitle("Floor Tasks")
@CssImport("./styles/views/tasks/tasks-view.css")
public class FloorTasksView extends VerticalLayout {
    FloorTasksPresenter floorTasksPresenter;
    AutowireCapableBeanFactory beanFactory;

    public FloorTasksView(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        floorTasksPresenter = new FloorTasksPresenter();
        beanFactory.autowireBean(floorTasksPresenter);
        floorTasksPresenter.init(this);
    }
}
