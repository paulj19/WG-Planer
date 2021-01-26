package com.wg_planner.views.ResetTask;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.views.MyTasks.MyTasksView;
import com.wg_planner.views.Tasks.TasksPresenter;
import com.wg_planner.views.main.MainView;
import com.wg_planner.views.signup.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.logging.Level;

@Route(value = "reset_task", layout = MainView.class)
@RouteAlias(value = "reset_task", layout = MainView.class)
@PageTitle("Reset Task")
@CssImport("./styles/views/tasks/tasks-view.css")
public class ResetTaskView extends VerticalLayout implements HasUrlParameter<String> {
    private Task taskToReset;
    private Room resetRequestingRoom;

    @Autowired
    TaskService taskService;
    @Autowired
    FloorService floorService;

    protected static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(ResetTaskView.class
            .getName());

    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {
        if (parameter != null && !parameter.isEmpty()) {
            taskToReset = taskService.getTaskById(Long.parseLong(parameter));
            resetRequestingRoom = taskToReset.getAssignedRoom();
            addResetPage();
        } else {
            LOGGER.log(Level.SEVERE, "url parameter in ResetTaskView must not be null or empty");
        }
    }

    public void addResetPage() {
        ResetTaskPage resetTaskPage = new ResetTaskPage(taskToReset, resetRequestingRoom, floorService);
        resetTaskPage.addListener(ResetTaskPage.ResetTaskPageEvent.ResetEvent.class, this::resetTask);
        resetTaskPage.addListener(ResetTaskPage.ResetTaskPageEvent.CancelEvent.class, this::backToMyTasks);
        add(resetTaskPage);
    }

    private void resetTask(ResetTaskPage.ResetTaskPageEvent.ResetEvent event)
    {
        taskService.resetTask(taskToReset, event.getRoomSelected());
        UI.getCurrent().navigate(MyTasksView.class);
    }

    private void backToMyTasks(ResetTaskPage.ResetTaskPageEvent.CancelEvent event)
    {
        UI.getCurrent().navigate(MyTasksView.class);
    }

}
