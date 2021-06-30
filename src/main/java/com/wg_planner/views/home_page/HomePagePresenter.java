package com.wg_planner.views.home_page;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.backend.entity.ResidentAccount;
import com.wg_planner.views.utils.SessionHandler;
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

    AccountDetailsView accountDetailsView;

    public void init(AccountDetailsView accountDetailsView) {
        this.accountDetailsView = accountDetailsView;
        accountDetailsView.add(new ResidentDetailsView(residentAccountService));
        accountDetailsView.add(new ResidentAvailabilityView(residentAccountService, this));
        accountDetailsView.add(new AccountDeleteView(floorService, residentAccountService, taskService));
    }

    @Transactional
    public void setResidentAwayAndSave(boolean isAway) {
        ResidentAccount currentResidentAccount = SessionHandler.getLoggedInResidentAccount();
        if (isAway) {
            residentAccountService.transferTasksOfResidentToNext(currentResidentAccount,
                    floorService, taskService);
        }
        currentResidentAccount.setAway(isAway);
        residentAccountService.save(currentResidentAccount);
    }


}
