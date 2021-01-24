package com.wg_planner.views.MyTasks;

import com.wg_planner.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

@Route(value = "mytasks", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("My Tasks")
@CssImport("./styles/views/tasks/tasks-view.css")
public class MyTasksView extends VerticalLayout {
    MyTasksPresenter myTasksPresenter;
    AutowireCapableBeanFactory beanFactory;
    VerticalLayout floorTasksLayout;

    public MyTasksView(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        myTasksPresenter = new MyTasksPresenter();
        floorTasksLayout = new VerticalLayout();
        beanFactory.autowireBean(myTasksPresenter);
        myTasksPresenter.init(floorTasksLayout);
        add(floorTasksLayout);
    }
}
