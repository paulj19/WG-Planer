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

@Controller
@Scope("prototype")
public class TasksPresenter {

    RoomService roomService;

    @Autowired
    TasksView allTasksView; //Todo check
    FloorService floorService;
    ResidentAccountService residentAccountService;
    TaskService taskService;

    Floor floor;
    Room room;
    ResidentAccount myResidentAccount;
    Room myRoom;
    Floor myFloor;
    List<Task> tasks;
    VerticalLayout allTaskLayout;


    public void init(ResidentAccountService residentAccountService, TaskService taskService, RoomService roomService, FloorService floorService, VerticalLayout allTaskLayout) {
        this.taskService = taskService;
        this.residentAccountService = residentAccountService;
        this.roomService = roomService;
        this.floorService = floorService;
        this.allTaskLayout = allTaskLayout;
//        todo the id of the resident account should be passed here to get the current residents room
//        residentAccount = residentAccountService.getResidentAccount(currentAccount.getId());
//        myResidentAccount = residentAccountService.getResidentAccountByRoom(roomService.getRoomByNumber("309"));
        myResidentAccount = residentAccountService.getResidentAccountByUsername(getUserName());
        myRoom = residentAccountService.getMyRoom(myResidentAccount);
        myFloor = myRoom.getFloor();
        tasks = taskService.findAll();
        addAllTasks();
    }

    private void addAllTasks() {
        allTaskLayout.removeAll();
        for (Task task :
                tasks) {
            TaskCard taskCard = new TaskCard(task, myRoom.getRoomNumber().equals(task.getAssignedRoom().toString()));
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
            return principal.toString();
        }
    }

    private void taskRemind(TaskCard.TaskCardEvent.RemindEvent event) {
        Task task = event.getTask();
    }
}
