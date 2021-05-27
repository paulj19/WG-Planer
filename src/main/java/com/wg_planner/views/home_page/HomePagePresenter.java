package com.wg_planner.views.home_page;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.backend.entity.ResidentAccount;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.views.utils.AccountDetailsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomePagePresenter {
    @Autowired
    ResidentAccountService residentAccountService;
    @Autowired
    TaskService taskService;
    @Autowired
    FloorService floorService;

    HomePageView homePageView;

    public void init(HomePageView homePageView) {
        this.homePageView = homePageView;
        homePageView.add(new AccountDetailsView(residentAccountService));
        homePageView.add(new ResidentAvailabilityView(residentAccountService, this));
    }

    @Transactional//TODO why transactional?
    public void setResidentAwayAndSave(boolean isAway) {
        ResidentAccount currentResidentAccount = AccountDetailsHelper.getInstance().getLoggedInResidentAccount();
        if (isAway) {
            tranferTasksOfResidentToNext(currentResidentAccount);
        }
        currentResidentAccount.setAway(isAway);
        residentAccountService.save(currentResidentAccount);
    }

    private void tranferTasksOfResidentToNext(ResidentAccount currentResidentAccount) {

        List<Task> assignedTasks = new ArrayList<>(currentResidentAccount.getRoom().getAssignedTasks());
        assignedTasks.forEach(task -> taskService.transferTask(task, floorService.getNextAvailableRoom(currentResidentAccount.getRoom().getFloor(), currentResidentAccount.getRoom())));
//        taskService.transferTask(assignedTasks.get(0), floorService.getNextAvailableRoom(currentResidentAccount.getRoom().getFloor(), currentResidentAccount.getRoom()));
//        taskService.transferTask(assignedTasks.get(1), floorService.getNextAvailableRoom(currentResidentAccount.getRoom().getFloor(), currentResidentAccount.getRoom()));
//        for (Task task : assignedTasks) {
//            taskService.transferTask(task, floorService.getNextAvailableRoom(currentResidentAccount.getRoom().getFloor(), currentResidentAccount.getRoom()));
//        }
    }
}
