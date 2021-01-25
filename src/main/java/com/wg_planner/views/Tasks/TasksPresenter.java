package com.wg_planner.views.Tasks;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.QueryParameters;
import com.wg_planner.backend.Service.*;
import com.wg_planner.backend.entity.*;
import com.wg_planner.views.FloorTasks.FloorTaskCard;
import com.wg_planner.views.ResetTask.ResetTaskView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    protected static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(TasksPresenter.class
            .getName());

    ResidentAccount myResidentAccount;
    protected Room myRoom;
    protected Floor myFloor;
    protected List<Task> tasks;

    abstract public void addAllTasks();

    public void init() {
        myResidentAccount = residentAccountService.getResidentAccountByUsername(getUserName());
        sanityCheckResidentAccount();
        myRoom = residentAccountService.getRoomByResidentAccount(myResidentAccount);
        myFloor = myRoom.getFloor();
        sanityCheckFloor();

        tasks = floorService.getAllTasksInFloor(myFloor);
        sanityCheckTasks();
        addAllTasks();
    }

    protected void taskDoneCallBackToSaveTask(FloorTaskCard.TaskCardEvent.DoneEvent event) {
        Task task = event.getTask();
        task.setAssignedRoom(floorService.getNextAvailableRoom(myFloor, myRoom));
        taskService.save(task);
        addAllTasks();
    }

    private String getUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            LOGGER.log(Level.SEVERE, "Logged in Account: " + ((UserDetails) principal).toString());
            throw new IllegalStateException("finding user name from getUserName failed");
        }
    }

    protected void taskRemindCallBack(FloorTaskCard.TaskCardEvent.RemindEvent event) {
        Task task = event.getTask();
    }

    protected void taskResetCallBack(FloorTaskCard.TaskCardEvent.ResetEvent event) {
        Task task = event.getTask();
//        ResetTaskView resetTask = new ResetTaskView(task);
//        QueryParameters qp = new QueryParameters(task);
//        UI.getCurrent().navigate("reset_task", task.getId());
        UI.getCurrent().navigate(ResetTaskView.class, task.getId().toString());
    }

    private void sanityCheckResidentAccount() {
        if (myResidentAccount == null) {
            LOGGER.log(Level.SEVERE, "Logged in Account: " + ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).toString());
            throw new NullPointerException("myResidentAccount returned after fetching username from getUsername is null");
        }
    }

    private void sanityCheckFloor() {
        if (myFloor == null) {
            LOGGER.log(Level.SEVERE, "Logged in Resident Account: " + myResidentAccount.toString());
            LOGGER.log(Level.SEVERE, "my room details: " + myRoom.toString());
            throw new NullPointerException("myFloor is null");
        }
    }

    private void sanityCheckTasks() {
        if (tasks == null || tasks.isEmpty()) {
            LOGGER.log(Level.SEVERE, "Logged in Resident Account: " + myResidentAccount.toString());
            LOGGER.log(Level.SEVERE, "my room details: " + myRoom.toString());
            LOGGER.log(Level.SEVERE, "my floor details: " + myFloor.toString());
            if (tasks == null)
                throw new NullPointerException("list of task returned by getAllTasksInFloor() is null");
            if (tasks.isEmpty())
                throw new IllegalStateException("list of task returned by getAllTasksInFloor() is empty");
        }
    }
}
