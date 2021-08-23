package com.wg_planner.views.tasks;

import com.vaadin.flow.component.UI;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.Service.RoomService;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.backend.Service.notification.NotificationServiceFirebase;
import com.wg_planner.backend.Service.notification.NotificationTypeTaskReminder;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.backend.utils.locking.LockRegisterHandler;
import com.wg_planner.views.tasks.assign_task.AssignTaskView;
import com.wg_planner.views.tasks.task_cards.TaskCard;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationHandler;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationType;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationTypeTaskRemind;
import com.wg_planner.views.utils.UINotificationMessage;
import com.wg_planner.views.utils.broadcaster.UIMessageBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

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
    NotificationServiceFirebase notificationServiceFirebase;

    protected static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(TasksPresenter.class
            .getName());

    protected List<Task> tasks;

    abstract public void addTasks();

    public void init() {
        tasks = floorService.getAllTasksInFloor(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor());
        sanityCheckTasks();
        addTasks();
    }

    //sync important because done and remind could happen at the same time
    //todo implement has changed with hashcode. which changes including(somewhere something in the floor is changed) should affect the sync
    public synchronized void taskDoneCallBack(TaskCard.TaskCardEvent event) {
        try {
            Object taskLock = LockRegisterHandler.getInstance().registerLock(event.getTask().getId());
            synchronized (taskLock) {
                Task taskPossiblyDirty = taskService.getTaskById(event.getTask().getId());
                if (taskPossiblyDirty.getAssignedRoom().equals(SessionHandler.getLoggedInResidentAccount().getRoom())) {
                    try {
                        Thread.sleep(40000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    taskService.transferTask(event.getTask(), floorService);
                    UINotificationHandler.getInstance().removeAllRemindNotificationsForObject(event.getTask(), event.getTask().getAssignedRoom());
                    UINotificationMessage.notify("The task is passed to next available resident");
                    addTasks();
                    return;
                }
            }
        } finally {
            LockRegisterHandler.getInstance().unregisterLock(event.getTask().getId());
        }
        UINotificationMessage.notifyTaskChange();
    }

    //todo DialogBox are you really done/remind ----> UNDO!!
    public synchronized void taskRemindCallBack(TaskCard.TaskCardEvent event) {
        try {
            Object taskLock = LockRegisterHandler.getInstance().registerLock(event.getTask().getId());
            synchronized (taskLock) {
                Task taskPossiblyDirty = taskService.getTaskById(event.getTask().getId());
                if (event.getTask().getAssignedRoom().equals(taskPossiblyDirty.getAssignedRoom())) {
                    UINotificationType uiNotificationTypeTaskRemind =
                            new UINotificationTypeTaskRemind(SessionHandler.getLoggedInResidentAccount().getRoom(), event.getTask());
                    UIMessageBus.unicastTo(UINotificationHandler.getInstance().createAndSaveUINotification(uiNotificationTypeTaskRemind,
                            event.getTask().getAssignedRoom()), event.getTask().getAssignedRoom());
                    notificationServiceFirebase.sendNotification(NotificationTypeTaskReminder.getInstance(event.getTask()),
                            event.getTask().getAssignedRoom().getResidentAccount());
                    UINotificationMessage.notify("A reminder has been send");
                    return;
                }
            }
        } finally {
            LockRegisterHandler.getInstance().unregisterLock(event.getTask().getId());
        }
        UINotificationMessage.notifyTaskChange();
    }

    public void taskAssignCallBack(TaskCard.TaskCardEvent event) {
        Task task = event.getTask();
        UI.getCurrent().navigate(AssignTaskView.class, task.getId().toString());
    }

    private void sanityCheckTasks() {
        //        if (tasks == null || tasks.isEmpty()) {
        //            LOGGER.log(Level.SEVERE, "Logged in Resident Account: " + AccountDetailsHelper
        //            .getLoggedInResidentAccount
        //            (residentAccountService).toString());
        //            LOGGER.log(Level.SEVERE, "my room details: " + AccountDetailsHelper.getLoggedInResidentAccount
        //            (residentAccountService).getRoom().toString());
        //            LOGGER.log(Level.SEVERE, "my floor details: " + AccountDetailsHelper.getLoggedInResidentAccount
        //            (residentAccountService).getRoom().getFloor().toString());
        //            if (tasks == null)
        //                throw new NullPointerException("list of task returned by getAllTasksInFloor() is null");
        //            if (tasks.isEmpty())
        //                throw new IllegalStateException("list of task returned by getAllTasksInFloor() is empty");
        //        }
    }
}
