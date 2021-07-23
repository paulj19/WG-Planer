package com.wg_planner.views.sub_menu.account_details;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.backend.entity.ResidentAccount;
import com.wg_planner.views.utils.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Controller
public class ResidentAvailabilityPresenter {
    @Autowired
    FloorService floorService;
    @Autowired
    TaskService taskService;
    @Autowired
    ResidentAccountService residentAccountService;

    @Transactional
    public void setResidentAwayStatusAndSave(boolean isAway) {
        ResidentAccount currentResidentAccount = SessionHandler.getLoggedInResidentAccount();
        if (isAway) {
            residentAccountService.transferTasksOfResidentToNext(currentResidentAccount, floorService, taskService);
        }
        currentResidentAccount.setAway(isAway);
        residentAccountService.save(currentResidentAccount);
    }
}
