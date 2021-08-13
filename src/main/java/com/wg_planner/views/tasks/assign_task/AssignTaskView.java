package com.wg_planner.views.tasks.assign_task;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.main.MainView;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UINavigationHandler;
import com.wg_planner.views.utils.UINotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;
import java.util.logging.Level;

@Route(value = "assign_task", layout = MainView.class)
@RouteAlias(value = "assign_task", layout = MainView.class)
@PageTitle("Assign Task")
@CssImport("./styles/views/tasks/tasks-view.css")
public class AssignTaskView extends VerticalLayout implements HasUrlParameter<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssignTaskView.class);
    private Task taskToAssign;
    @Autowired
    TaskService taskService;
    @Autowired
    FloorService floorService;

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (parameter != null && !parameter.isEmpty()) {
            taskToAssign = taskService.getTaskById(Long.parseLong(parameter));
            //TODO create roles and privelages
            if (floorService.getAllTasksInFloor(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor()).contains(taskToAssign)) {
                addAssignPage();
            } else {
                LOGGER.error(MarkerFactory.getMarker("UNAUTHORIZED URL ACCESS"), "task to assign {}", taskService.toString());
            }
        } else {
            LOGGER.error("url parameter in AssignTaskView must not be null or empty");
        }
    }

    public void addAssignPage() {
        AssignRoomToTaskPage assignRoomToTaskPage = new AssignRoomToTaskPage(taskToAssign, floorService);
        assignRoomToTaskPage.addListener(AssignRoomToTaskPage.AssignTaskPageEvent.AssignEvent.class, this::assignTask);
        assignRoomToTaskPage.addListener(AssignRoomToTaskPage.AssignTaskPageEvent.CancelEvent.class,
                this::cancelAssign);
        add(assignRoomToTaskPage);
    }

    private synchronized void assignTask(AssignRoomToTaskPage.AssignTaskPageEvent.AssignEvent event) {
        //synchronization issue fix: the method sync ensures contention between assigns
        // and if task has changed between opening assign page and clicking
        Task taskPossiblyDirty = taskService.getTaskById(event.getTaskToAssign().getId());
        if (Objects.equals(event.getTaskToAssign().getAssignedRoom(), taskPossiblyDirty.getAssignedRoom())) {
            LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Assign Task assign click Task to Assign {}. Room selected {}",
                    SessionHandler.getLoggedInResidentAccount().getId(), taskToAssign.toString(), event.getRoomSelected().toString());
            taskService.assignTask(taskToAssign, event.getRoomSelected());
            UINavigationHandler.getInstance().navigateToHomePage();
            UINotificationMessage.notify("Task " + event.getTaskToAssign().getTaskName() + " assigned to room " + event.getRoomSelected().getRoomName());
        } else {
            LOGGER.warn("invalid task on assign task. Resident Account id {}. Task from event {}. Task from DB {}. Room selected {}",
                    SessionHandler.getLoggedInResidentAccount().getId(), taskToAssign.toString(), taskPossiblyDirty.toString(),
                    event.getRoomSelected().toString());
            UINotificationMessage.notify("A change has been made to the task, please refresh the page");
        }
    }

    private void cancelAssign(AssignRoomToTaskPage.AssignTaskPageEvent.CancelEvent event) {
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Assign Task cancel click Task to Assign {}.",
                SessionHandler.getLoggedInResidentAccount().getId(), taskToAssign.toString());
        UINavigationHandler.getInstance().navigateToHomePage();
    }
}
