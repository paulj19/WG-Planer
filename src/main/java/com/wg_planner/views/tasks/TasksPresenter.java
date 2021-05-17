package com.wg_planner.views.tasks;

import com.vaadin.flow.component.UI;
import com.wg_planner.backend.Service.*;
import com.wg_planner.backend.Service.notification.NotificationTypeTaskReminder;
import com.wg_planner.backend.Service.notification.NotificationServiceFirebase;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.views.tasks.floor_tasks.FloorTaskCard;
import com.wg_planner.views.tasks.reset_task.ResetTaskView;
import com.wg_planner.views.utils.AccountDetailsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.logging.Level;

@Controller
@Scope("prototype")
public abstract class TasksPresenter {

    @Autowired
    protected FloorService floorService;
    @Autowired
    protected ResidentAccountService residentAccountService;
    @Autowired
    protected TaskService taskService;
    @Autowired
    protected RoomService roomService;
    @Autowired
    FirebaseMessagingService firebaseMessagingService;
    @Autowired
    NotificationServiceFirebase notificationServiceFirebase;

    protected static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(TasksPresenter.class
            .getName());

    protected List<Task> tasks;

    abstract public void addAllTasks();

    public void init() {
        tasks = floorService.getAllTasksInFloor(AccountDetailsHelper.getLoggedInResidentAccount(residentAccountService).getRoom().getFloor());
        sanityCheckTasks();
        addAllTasks();
    }

    protected void taskDoneCallBackToSaveTask(FloorTaskCard.TaskCardEvent.DoneEvent event) {
        taskService.transferTask(event.getTask(), floorService.getNextAvailableRoom(AccountDetailsHelper.getLoggedInResidentAccount(residentAccountService).getRoom().getFloor(), AccountDetailsHelper.getLoggedInResidentAccount(residentAccountService).getRoom()));
        addAllTasks();
    }

    //todo are you really done/remind ----> UNDO!!
    protected void taskRemindCallBack(FloorTaskCard.TaskCardEvent.RemindEvent event) {
        notificationServiceFirebase.sendNotification(NotificationTypeTaskReminder.getInstance(event.getTask()), event.getTask().getAssignedRoom().getResidentAccount());
    }

    protected void taskResetCallBack(FloorTaskCard.TaskCardEvent.ResetEvent event) {
        Task task = event.getTask();
        UI.getCurrent().navigate(ResetTaskView.class, task.getId().toString());
    }

    private void sanityCheckTasks() {
        if (tasks == null || tasks.isEmpty()) {
            LOGGER.log(Level.SEVERE, "Logged in Resident Account: " + AccountDetailsHelper.getLoggedInResidentAccount(residentAccountService).toString());
            LOGGER.log(Level.SEVERE, "my room details: " + AccountDetailsHelper.getLoggedInResidentAccount(residentAccountService).getRoom().toString());
            LOGGER.log(Level.SEVERE, "my floor details: " + AccountDetailsHelper.getLoggedInResidentAccount(residentAccountService).getRoom().getFloor().toString());
            if (tasks == null)
                throw new NullPointerException("list of task returned by getAllTasksInFloor() is null");
            if (tasks.isEmpty())
                throw new IllegalStateException("list of task returned by getAllTasksInFloor() is empty");
        }
    }
}
