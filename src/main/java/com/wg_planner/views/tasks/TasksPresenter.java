package com.wg_planner.views.tasks;

import com.vaadin.flow.component.UI;
import com.wg_planner.backend.Service.*;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.backend.utils.Note;
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

    protected static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(TasksPresenter.class
            .getName());

    protected List<Task> tasks;

    abstract public void addAllTasks();

    public void init() {
        tasks = floorService.getAllTasksInFloor(AccountDetailsHelper.getUserResidentAccount(residentAccountService).getRoom().getFloor());
        sanityCheckTasks();
        addAllTasks();
    }

    protected void taskDoneCallBackToSaveTask(FloorTaskCard.TaskCardEvent.DoneEvent event) {
        Task taskToTransfer = event.getTask();
        taskService.transferTask(taskToTransfer, floorService.getNextAvailableRoom(AccountDetailsHelper.getUserResidentAccount(residentAccountService).getRoom().getFloor(), AccountDetailsHelper.getUserResidentAccount(residentAccountService).getRoom()));
        addAllTasks();
    }


    protected void taskRemindCallBack(FloorTaskCard.TaskCardEvent.RemindEvent event) {
        Task task = event.getTask();
        String token = "fo6TyIPmT6-oh2Z-9FPh2Q:APA91bEx8FU3a-a114VccF-DsJ2TkQ0NKvmeCeHyvJJNWsNrYuxaW30DaE-6X9EOOFhWVwNCsHbyPTmTjrxWS6GyO5E1x73nOgHGA54zWLe9ESz06hv0U1nhLJURMkY8jh9T6iTaG5gR";
        firebaseMessagingService.sendNotification(new Note("Biomüll full", "Please empty it soon"), token);
    }

    protected void taskResetCallBack(FloorTaskCard.TaskCardEvent.ResetEvent event) {
        Task task = event.getTask();
        UI.getCurrent().navigate(ResetTaskView.class, task.getId().toString());
    }

    private void sanityCheckTasks() {
        if (tasks == null || tasks.isEmpty()) {
            LOGGER.log(Level.SEVERE, "Logged in Resident Account: " + AccountDetailsHelper.getUserResidentAccount(residentAccountService).toString());
            LOGGER.log(Level.SEVERE, "my room details: " + AccountDetailsHelper.getUserResidentAccount(residentAccountService).getRoom().toString());
            LOGGER.log(Level.SEVERE, "my floor details: " + AccountDetailsHelper.getUserResidentAccount(residentAccountService).getRoom().getFloor().toString());
            if (tasks == null)
                throw new NullPointerException("list of task returned by getAllTasksInFloor() is null");
            if (tasks.isEmpty())
                throw new IllegalStateException("list of task returned by getAllTasksInFloor() is empty");
        }
    }
}
