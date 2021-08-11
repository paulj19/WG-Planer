package com.wg_planner.views.tasks.assign_task;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.views.main.MainView;
import com.wg_planner.views.utils.UINavigationHandler;
import com.wg_planner.views.utils.UINotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;
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
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (parameter != null && !parameter.isEmpty()) {
            taskToAssign = taskService.getTaskById(Long.parseLong(parameter));
            assignRequestingRoom = taskToAssign.getAssignedRoom();
            addAssignPage();
        } else {
            LOGGER.log(Level.SEVERE, "url parameter in AssignTaskView must not be null or empty");
        }
    }

    public void addAssignPage() {
        AssignRoomToTaskPage assignRoomToTaskPage = new AssignRoomToTaskPage(taskToAssign, floorService);
        assignRoomToTaskPage.addListener(AssignRoomToTaskPage.AssignTaskPageEvent.AssignEvent.class, this::assignTask);
        assignRoomToTaskPage.addListener(AssignRoomToTaskPage.AssignTaskPageEvent.CancelEvent.class, this::cancelAssign);
        add(assignRoomToTaskPage);
    }

    private synchronized void assignTask(AssignRoomToTaskPage.AssignTaskPageEvent.AssignEvent event) {
        //synchronization issue fix: the method sync ensures contention between assigns
        // and if task has changed between opening assign page and clicking
        Task taskPossiblyDirty = taskService.getTaskById(event.getTaskToAssign().getId());
        if(Objects.equals(event.getTaskToAssign().getAssignedRoom(), taskPossiblyDirty.getAssignedRoom())) {
            taskService.assignTask(taskToAssign, event.getRoomSelected());
            UINavigationHandler.getInstance().navigateToHomePage();
            UINotificationMessage.notify("Task " + event.getTaskToAssign().getTaskName() + " assigned to room " + event.getRoomSelected().getRoomName());
        } else {
            UINotificationMessage.notify("A change has been made to the task, please refresh the page");
        }
    }

    private void cancelAssign(AssignRoomToTaskPage.AssignTaskPageEvent.CancelEvent event) {
            UINavigationHandler.getInstance().navigateToHomePage();
    }

}
