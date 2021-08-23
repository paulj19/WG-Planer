package com.wg_planner.views.tasks.assign_task;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.backend.utils.locking.LockRegisterHandler;
import com.wg_planner.views.main.MainView;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UINavigationHandler;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationHandler;
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
            //TODO create roles and privelages
            if (floorService.getAllTasksInFloor(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor()).contains(taskToAssign)) {
                addAssignPage();
            }
        } else {
            LOGGER.log(Level.SEVERE, "url parameter in AssignTaskView must not be null or empty");
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
        try {
            Object taskLock = LockRegisterHandler.getInstance().registerLock(taskToAssign.getId());
            synchronized (taskLock) {
                Task taskPossiblyDirty = taskService.getTaskById(event.getTaskToAssign().getId());
                if (Objects.equals(taskPossiblyDirty.getAssignedRoom(), taskToAssign.getAssignedRoom())) {
                    taskService.assignTask(taskToAssign, event.getRoomSelected());
                    UINotificationHandler.getInstance().removeAllRemindNotificationsForObject(event.getTaskToAssign(), taskToAssign.getAssignedRoom());
                    UINavigationHandler.getInstance().navigateToHomePage();
                    UINotificationMessage.notify("Task " + event.getTaskToAssign().getTaskName() + " assigned to room " + event.getRoomSelected().getRoomName());
                    return;
                }
            }
        } finally {
            LockRegisterHandler.getInstance().unregisterLock(taskToAssign.getId());
        }
        UINotificationMessage.notifyTaskChange();
    }

    private void cancelAssign(AssignRoomToTaskPage.AssignTaskPageEvent.CancelEvent event) {
        UINavigationHandler.getInstance().navigateToHomePage();
    }
}
