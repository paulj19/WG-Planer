package com.wg_planner.views.tasks.assign_task;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.views.tasks.my_tasks.MyTasksView;
import com.wg_planner.views.main.MainView;
import com.wg_planner.views.utils.UINotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Level;

@Route(value = "assign_task", layout = MainView.class)
@RouteAlias(value = "assign_task", layout = MainView.class)
@PageTitle("Assign Task")
@CssImport("./styles/views/tasks/tasks-view.css")
public class AssignTaskView extends VerticalLayout implements HasUrlParameter<String> {
    private Task taskToAssign;
    private Room assignRequestingRoom;
    @Autowired
    TaskService taskService;
    @Autowired
    FloorService floorService;

    protected static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(AssignTaskView.class
            .getName());

    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {
        if (parameter != null && !parameter.isEmpty()) {
            taskToAssign = taskService.getTaskById(Long.parseLong(parameter));
            assignRequestingRoom = taskToAssign.getAssignedRoom();
            addAssignPage();
        } else {
            LOGGER.log(Level.SEVERE, "url parameter in AssignTaskView must not be null or empty");
        }
    }

    public void addAssignPage() {
        AssignTaskPage assignTaskPage = new AssignTaskPage(taskToAssign, floorService);
        assignTaskPage.addListener(AssignTaskPage.AssignTaskPageEvent.AssignEvent.class, this::assignTask);
        assignTaskPage.addListener(AssignTaskPage.AssignTaskPageEvent.CancelEvent.class, this::cancelAssign);
        add(assignTaskPage);
    }

    private void assignTask(AssignTaskPage.AssignTaskPageEvent.AssignEvent event) {
        taskService.assignTask(taskToAssign, event.getRoomSelected());
        navigateBackMyTasks();
        UINotificationMessage.notify("Task " + event.getTaskToAssign().getTaskName() + " assigned to room " + event.getRoomSelected().getRoomName());
    }

    private void cancelAssign(AssignTaskPage.AssignTaskPageEvent.CancelEvent event) {
        navigateBackMyTasks();
    }

    private void navigateBackMyTasks() {
        UI.getCurrent().navigate(MyTasksView.class);
    }

}
