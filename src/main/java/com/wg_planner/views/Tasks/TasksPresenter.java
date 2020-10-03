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


    public void init(Account currentAccount, ResidentAccountService residentAccountService, TaskService taskService, VerticalLayout allTaskLayout) {
        this.accountService = accountService;
        this.taskService = taskService;
        this.residentAccountService = residentAccountService;
        residentAccount =  residentAccountService.getResidentAccount(currentAccount.getId());
        tasks = taskService.findAll();
//        Room myRoom = accountService.getMyRoom();

//        TaskCard taskCard1 = new TaskCard(tasks.get(0));
//        TaskCard taskCard2 = new TaskCard(tasks.get(1));
//        TaskCard taskCard3 = new TaskCard(tasks.get(2));
//        allTaskLayout.add(taskCard1.getTaskCardLayout(), taskCard2.getTaskCardLayout(), taskCard3.getTaskCardLayout());
        for (Task task :
                tasks) {
            allTaskLayout.add(new TaskCard(task, accountService.getMyRoom().getRoomNumber().equals(task.getAssignedRoom().toString())).getTaskCardLayout());
        }
    }
}
