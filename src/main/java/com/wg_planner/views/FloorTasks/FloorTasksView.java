package com.wg_planner.views.FloorTasks;

import com.wg_planner.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

@Route(value = "floor_tasks", layout = MainView.class)
@RouteAlias(value = "floortasks", layout = MainView.class)
@PageTitle("Floor Tasks")
@CssImport("./styles/views/tasks/tasks-view.css")
public class FloorTasksView extends VerticalLayout {
    FloorTasksPresenter floorTasksPresenter;
    AutowireCapableBeanFactory beanFactory;
    VerticalLayout myTasksLayout;

    public FloorTasksView(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        floorTasksPresenter = new FloorTasksPresenter();
        myTasksLayout = new VerticalLayout();
        beanFactory.autowireBean(floorTasksPresenter);
        floorTasksPresenter.init(myTasksLayout);
        add(myTasksLayout);
    }
}
