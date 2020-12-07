package com.wg_planner.views.Tasks;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.backend.Service.*;
import com.wg_planner.backend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.logging.Level;

@Controller
@Scope("prototype")
public class TasksPresenter {

    @Autowired
    FloorService floorService;
    @Autowired
    ResidentAccountService residentAccountService;
    @Autowired
    TaskService taskService;
    @Autowired
    RoomService roomService;

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(TasksPresenter.class
            .getName());

    Floor floor;
    Room room;
    ResidentAccount myResidentAccount;
    Room myRoom;
    Floor myFloor;
    List<Task> tasks;
    VerticalLayout allTaskLayout;

    public void init(VerticalLayout allTaskLayout) {
        this.allTaskLayout = allTaskLayout;
        myResidentAccount = residentAccountService.getResidentAccountByUsername(getUserName());
        if (myResidentAccount == null) {
            LOGGER.log(Level.SEVERE, "Logged in Account: " + ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).toString());
            throw new NullPointerException("myResidentAccount returned after fetching username from getUsername is null");
        }
        myRoom = residentAccountService.getRoomByResidentAccount(myResidentAccount);
        myFloor = myRoom.getFloor();
        if (myFloor == null) {
            LOGGER.log(Level.SEVERE, "Logged in Resident Account: " + myResidentAccount.toString());
            LOGGER.log(Level.SEVERE, "my room details: " + myRoom.toString());
            throw new NullPointerException("myFloor is null");
        }
        tasks = floorService.getAllTasksInFloor(myFloor);
        if (tasks == null || tasks.isEmpty()) {
            LOGGER.log(Level.SEVERE, "Logged in Resident Account: " + myResidentAccount.toString());
            LOGGER.log(Level.SEVERE, "my room details: " + myRoom.toString());
            LOGGER.log(Level.SEVERE, "my floor details: " + myFloor.toString());
            if (tasks == null)
                throw new NullPointerException("list of task returned by getAllTasksInFloor() is null");
            if (tasks.isEmpty())
                throw new IllegalStateException("list of task returned by getAllTasksInFloor() is empty");
        }
        addAllTasks();
    }

    private void addAllTasks() {
        allTaskLayout.removeAll();
        for (Task task :
                tasks) {
            TaskCard taskCard = new TaskCard(task, myRoom.getRoomNumber().equals(task.getAssignedRoom().getRoomNumber()));
            taskCard.addListener(TaskCard.TaskCardEvent.DoneEvent.class, this::taskDone);
            taskCard.addListener(TaskCard.TaskCardEvent.RemindEvent.class, this::taskRemind);
            allTaskLayout.add(taskCard.getTaskCardLayout());
        }
    }

    private void taskDone(TaskCard.TaskCardEvent.DoneEvent event) {
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

    private void taskRemind(TaskCard.TaskCardEvent.RemindEvent event) {
        Task task = event.getTask();
    }
}
