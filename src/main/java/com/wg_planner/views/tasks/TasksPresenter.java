package com.wg_planner.views.tasks;

import com.vaadin.flow.component.UI;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.Service.RoomService;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.backend.Service.notification.NotificationServiceFirebase;
import com.wg_planner.backend.Service.notification.NotificationTypeTaskReminder;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.backend.utils.locking.LockRegisterHandler;
import com.wg_planner.views.tasks.assign_task.AssignTaskView;
import com.wg_planner.views.tasks.task_cards.TaskCard;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationHandler;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationType;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationTypeTaskRemind;
import com.wg_planner.views.utils.UINotificationMessage;
import com.wg_planner.views.utils.broadcaster.UIMessageBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public abstract class TasksPresenter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TasksPresenter.class);
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

    abstract public void addTasks();

    public void init() {
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
                    LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Task Done callback task {}.",
                            SessionHandler.getLoggedInResidentAccount().getId(), taskPossiblyDirty.toString());
                    UINotificationHandler.getInstance().removeAllRemindNotificationsForObject(taskPossiblyDirty, taskPossiblyDirty.getAssignedRoom());
                    taskService.transferTask(taskPossiblyDirty, floorService);
                    UINotificationMessage.notify("The task is passed to next available resident");
                    addTasks();
                } else {
                    LOGGER.warn("invalid task on task done callback. Resident Account id {}. Task from event {}. Task from DB {}.",
                            SessionHandler.getLoggedInResidentAccount().getId(), taskPossiblyDirty.toString(), taskPossiblyDirty.toString());
                    UINotificationMessage.notifyTaskChange();
                }
            }
        } finally {
            LockRegisterHandler.getInstance().unregisterLock(event.getTask().getId());
        }
    }

    //todo DialogBox are you really done/remind ----> UNDO!!
    public synchronized void taskRemindCallBack(TaskCard.TaskCardEvent event) {
        try {
            Object taskLock = LockRegisterHandler.getInstance().registerLock(event.getTask().getId());
            synchronized (taskLock) {
                Task taskPossiblyDirty = taskService.getTaskById(event.getTask().getId());
                if (event.getTask().getAssignedRoom().equals(taskPossiblyDirty.getAssignedRoom())) {
                    LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Task Remind callback task {}.",
                            SessionHandler.getLoggedInResidentAccount().getId(), taskPossiblyDirty.toString());
                    UINotificationType uiNotificationTypeTaskRemind =
                            new UINotificationTypeTaskRemind(SessionHandler.getLoggedInResidentAccount().getRoom(), taskPossiblyDirty);
                    UIMessageBus.unicastTo(UINotificationHandler.getInstance().createAndSaveUINotification(uiNotificationTypeTaskRemind,
                            taskPossiblyDirty.getAssignedRoom()), taskPossiblyDirty.getAssignedRoom());
                    notificationServiceFirebase.sendNotification(NotificationTypeTaskReminder.getInstance(taskPossiblyDirty),
                            taskPossiblyDirty.getAssignedRoom().getResidentAccount());
                    UINotificationMessage.notify("A reminder has been send");
                    return;
                } else {
                    LOGGER.warn("invalid task on task remind callback. Resident Account id {}. Task from event {}. Task from DB {}.",
                            SessionHandler.getLoggedInResidentAccount().getId(), taskPossiblyDirty.toString(), taskPossiblyDirty.toString());
                    UINotificationMessage.notifyTaskChange();
                }
            }
        } finally {
            LockRegisterHandler.getInstance().unregisterLock(event.getTask().getId());
        }
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
