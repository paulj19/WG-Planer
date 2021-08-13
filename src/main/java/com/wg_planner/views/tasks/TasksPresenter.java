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
import com.wg_planner.views.tasks.assign_task.AssignTaskView;
import com.wg_planner.views.tasks.my_tasks.MyTasksView;
import com.wg_planner.views.tasks.task_cards.TaskCard;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UINotificationHandler.UIEventHandler;
import com.wg_planner.views.utils.UINotificationHandler.UIEventType;
import com.wg_planner.views.utils.UINotificationHandler.UIEventTypeTaskRemind;
import com.wg_planner.views.utils.UINotificationMessage;
import com.wg_planner.views.utils.broadcaster.UIMessageBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

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

    protected List<Task> tasks;

    abstract public void addTasks();

    public void init() {
        tasks = floorService.getAllTasksInFloor(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor());
        addTasks();
    }

    //sync important because done and remind could happen at the same time
    //todo implement has changed with hashcode. which changes including(somewhere something in the floor is changed) should affect the sync
    public synchronized void taskDoneCallBack(TaskCard.TaskCardEvent event) {
        //synchronization issue fix?
        Task taskPossiblyDirty = taskService.getTaskById(event.getTask().getId());
        if (taskPossiblyDirty.getAssignedRoom().equals(SessionHandler.getLoggedInResidentAccount().getRoom())) {
            LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Task Done callback task {}.",
                    SessionHandler.getLoggedInResidentAccount().getId(), event.getTask().toString());
            taskService.transferTask(event.getTask(), floorService);
            addTasks();
            List<UIEventType> taskRemindNotifications =
                    UIEventHandler.getInstance().getAllNotificationsForRoom(SessionHandler.getLoggedInResidentAccount().getRoom()).stream().filter(uiEventType -> uiEventType instanceof UIEventTypeTaskRemind && uiEventType.getEventRelatedObject().equals(event.getTask())).collect(Collectors.toList());
            taskRemindNotifications.forEach(notification -> UIEventHandler.getInstance().removeNotification(SessionHandler.getLoggedInResidentAccount().getRoom().getId(), notification.getId()));
        } else {
            LOGGER.warn("invalid task on task done callback. Resident Account id {}. Task from event {}. Task from DB {}.",
                    SessionHandler.getLoggedInResidentAccount().getId(), event.getTask().toString(), taskPossiblyDirty.toString());
            UINotificationMessage.notify("A change has been made to the task, please refresh the page");
        }
    }

    //todo DialogBox are you really done/remind ----> UNDO!!
    public synchronized void taskRemindCallBack(TaskCard.TaskCardEvent event) {
        //synchronization issue fix?
        Task taskWithPossibleUpdate = taskService.getTaskById(event.getTask().getId());
        if (event.getTask().getAssignedRoom().equals(taskWithPossibleUpdate.getAssignedRoom())) {
            LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Task Remind callback task {}.",
                    SessionHandler.getLoggedInResidentAccount().getId(), event.getTask().toString());
            UIEventType uiEventTypeTaskRemind =
                    new UIEventTypeTaskRemind(SessionHandler.getLoggedInResidentAccount().getRoom(),
                            event.getTask());
            UIMessageBus.unicastTo(UIEventHandler.getInstance().createAndSaveUINotification(uiEventTypeTaskRemind,
                    event.getTask().getAssignedRoom()), event.getTask().getAssignedRoom());
            notificationServiceFirebase.sendNotification(NotificationTypeTaskReminder.getInstance(event.getTask()),
                    event.getTask().getAssignedRoom().getResidentAccount());
            UINotificationMessage.notify("A reminder has been send");
        } else {
            LOGGER.warn("invalid task on task remind callback. Resident Account id {}. Task from event {}. Task from DB {}.",
                    SessionHandler.getLoggedInResidentAccount().getId(), event.getTask().toString(), taskWithPossibleUpdate.toString());
            UINotificationMessage.notify("A change has been made to the task, please refresh the page");
        }
    }

    public void taskAssignCallBack(TaskCard.TaskCardEvent event) {
        Task task = event.getTask();
        UI.getCurrent().navigate(AssignTaskView.class, task.getId().toString());
    }
}
