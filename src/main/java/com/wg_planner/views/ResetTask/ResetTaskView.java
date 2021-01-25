package com.wg_planner.views.ResetTask;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.views.Tasks.TasksPresenter;
import com.wg_planner.views.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Level;

@Route(value = "reset_task", layout = MainView.class)
@RouteAlias(value = "reset_task", layout = MainView.class)
@PageTitle("Reset Task")
@CssImport("./styles/views/tasks/tasks-view.css")
public class ResetTaskView extends VerticalLayout implements HasUrlParameter<String> {
    private Task taskToReset;
    private Room resetRequestingRoom;
    H1 heading = new H1("Assign Biomüll to another roommate");
    Label label = new Label("manchester united");
    TextField firstName = new TextField("First Name", "please enter your first name");


    @Autowired
    TaskService taskService;
    protected static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(ResetTaskView.class
            .getName());

    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {
        if (parameter != null && !parameter.isEmpty()) {
            taskToReset = taskService.getTaskById(Long.parseLong(parameter));
            addResetPage();
        } else {
            LOGGER.log(Level.SEVERE, "url parameter in ResetTaskView must not be null or empty");
        }
    }

    public void addResetPage() {
        ResetTaskPage resetTaskPage = new ResetTaskPage();
        add(resetTaskPage);
    }
}
