package com.wg_planner.views.tasks.my_tasks;

import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.wg_planner.views.utils.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

@Route(value = "my_tasks", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("My Tasks")
@CssImport("./styles/views/tasks/tasks-view.css")
public class MyTasksView extends VerticalLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyTasksView.class);
    MyTasksPresenter myTasksPresenter;
    AutowireCapableBeanFactory beanFactory;

    public MyTasksView(AutowireCapableBeanFactory beanFactory) {
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Adding following tasks to floorTaskView.",
                SessionHandler.getLoggedInResidentAccount().getId());
        this.beanFactory = beanFactory;
        myTasksPresenter = new MyTasksPresenter();
        beanFactory.autowireBean(myTasksPresenter);
        myTasksPresenter.init(this);
    }
}
