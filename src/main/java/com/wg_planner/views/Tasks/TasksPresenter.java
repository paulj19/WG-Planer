package com.wg_planner.views.Tasks;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.backend.Service.AccountService;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.Service.RoomService;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.backend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Scope("prototype")
public class TasksPresenter {

    @Autowired
    RoomService roomService;

    @Autowired
    TasksView allTasksView;
    AccountService accountService;
    ResidentAccountService residentAccountService;
    TaskService taskService;

    Floor floor;
    Room room;
    ResidentAccount residentAccount;
    List<Task> tasks;


    public void init(ResidentAccountService residentAccountService, TaskService taskService, VerticalLayout allTaskLayout) {
        this.accountService = accountService;
        this.taskService = taskService;
        this.residentAccountService = residentAccountService;
//        todo the id of the resident account should be passed here to get the current residents room
//        residentAccount = residentAccountService.getResidentAccount(currentAccount.getId());
        residentAccount = residentAccountService.getResidentAccountByRoom(roomService.getRoomByRoomNumber("310"));
        tasks = taskService.findAll();
        for (Task task :
                tasks) {
            allTaskLayout.add(new TaskCard(task, residentAccountService.getMyRoom(residentAccount).getRoomNumber().equals(task.getAssignedRoom().toString())).getTaskCardLayout());
        }
    }
}
