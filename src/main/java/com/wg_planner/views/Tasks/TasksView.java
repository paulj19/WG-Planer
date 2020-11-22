package com.wg_planner.views.Tasks;

import com.wg_planner.backend.Service.*;
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
    TasksPresenter tasksPresenter;
    AutowireCapableBeanFactory beanFactory;
    VerticalLayout allTaskLayout = new VerticalLayout();

    public TasksView(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        tasksPresenter = new TasksPresenter();
        beanFactory.autowireBean(tasksPresenter);
        tasksPresenter.init(allTaskLayout);
        add(allTaskLayout);
    }
}
