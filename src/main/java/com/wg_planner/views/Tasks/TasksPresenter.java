package com.wg_planner.views.Tasks;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.backend.Service.*;
import com.wg_planner.backend.entity.*;
import com.wg_planner.views.FloorTasks.FloorTaskCard;
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

    Floor floor;
    Room room;
    ResidentAccount myResidentAccount;
    protected Room myRoom;
    protected Floor myFloor;
    protected List<Task> tasks;
    protected VerticalLayout allTaskLayout;

    public void init(VerticalLayout allTaskLayout) {
        this.allTaskLayout = allTaskLayout;
        myResidentAccount = residentAccountService.getResidentAccountByUsername(getUserName());
        sanityCheckResidentAccount();
        myRoom = residentAccountService.getRoomByResidentAccount(myResidentAccount);
        myFloor = myRoom.getFloor();
        sanityCheckFloor();

        tasks = floorService.getAllTasksInFloor(myFloor);
        sanityCheckTasks();
        addAllTasks();
    }

//
//    private void addAllTasks() {
//        allTaskLayout.removeAll();
//        for (Task task :
//                tasks) {
//            FloorTaskCard floorTaskCard = new FloorTaskCard(task, myRoom.getRoomNumber().equals(task.getAssignedRoom().getRoomNumber()));
//            floorTaskCard.addListener(FloorTaskCard.TaskCardEvent.DoneEvent.class, this::taskDone);
//            floorTaskCard.addListener(FloorTaskCard.TaskCardEvent.RemindEvent.class, this::taskRemind);
//            allTaskLayout.add(floorTaskCard.getTaskCardLayout());
//        }
//    }
    abstract public void addAllTasks();

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
