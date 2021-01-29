package com.wg_planner.views.home_page;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.backend.entity.ResidentAccount;
import com.wg_planner.views.utils.AccountDetailsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

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
    }

    @Transactional
    public void setResidentAwayAndSave(boolean isAway) {
        ResidentAccount currentResidentAccount = AccountDetailsHelper.getUserResidentAccount(residentAccountService);
        tranferTasksOfResidentToNext(currentResidentAccount);
        currentResidentAccount.setAway(isAway);
        residentAccountService.save(currentResidentAccount);
    }

    private void tranferTasksOfResidentToNext(ResidentAccount currentResidentAccount) {
        currentResidentAccount.getRoom().getAssignedTasks().forEach(task -> taskService.transferTask(task, floorService.getNextAvailableRoom(currentResidentAccount.getRoom().getFloor(), currentResidentAccount.getRoom())));
    }
}
