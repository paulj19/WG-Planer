package com.wg_planner.views.tasks;

import com.vaadin.flow.component.UI;
import com.wg_planner.backend.Service.*;
import com.wg_planner.backend.Service.notification.NotificationServiceFirebase;
import com.wg_planner.backend.Service.notification.NotificationTypeTaskReminder;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.backend.utils.consensus.ConsensusHandler;
import com.wg_planner.backend.utils.consensus.ConsensusObjectTaskDelete;
import com.wg_planner.views.tasks.task_cards.TaskCard;
import com.wg_planner.views.tasks.assign_task.AssignTaskView;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UINotificationHandler.UIEventHandler;
import com.wg_planner.views.utils.UINotificationHandler.UIEventType;
import com.wg_planner.views.utils.UINotificationHandler.UIEventTypeTaskDelete;
import com.wg_planner.views.utils.UINotificationHandler.UIEventTypeTaskRemind;
import com.wg_planner.views.utils.UINotificationMessage;
import com.wg_planner.views.utils.broadcaster.UIMessageBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Arrays;
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

    public void taskDoneCallBack(TaskCard.TaskCardEvent event) {
        taskService.transferTask(event.getTask(), floorService);
        addTasks();
    }

    //todo DialogBox are you really done/remind ----> UNDO!!
    public void taskRemindCallBack(TaskCard.TaskCardEvent event) {
        UIEventType uiEventTypeTaskRemind = new UIEventTypeTaskRemind(SessionHandler.getLoggedInResidentAccount().getRoom(),
                event.getTask());
        UIMessageBus.unicastTo(UIEventHandler.getInstance().createAndSaveUINotification(uiEventTypeTaskRemind,
                event.getTask().getAssignedRoom()), event.getTask().getAssignedRoom());
        notificationServiceFirebase.sendNotification(NotificationTypeTaskReminder.getInstance(event.getTask()),
                event.getTask().getAssignedRoom().getResidentAccount());
        UINotificationMessage.notify("A reminder has been send");
    }

    public void taskAssignCallBack(TaskCard.TaskCardEvent event) {
        Task task = event.getTask();
        UI.getCurrent().navigate(AssignTaskView.class, task.getId().toString());
    }

    private void sanityCheckTasks() {
        //        if (tasks == null || tasks.isEmpty()) {
        //            LOGGER.log(Level.SEVERE, "Logged in Resident Account: " + AccountDetailsHelper.getLoggedInResidentAccount
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
