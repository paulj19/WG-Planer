package com.wg_planner.views.sub_menu.account_details;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.backend.entity.ResidentAccount;
import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.utils.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Controller
public class ResidentAvailabilityPresenter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResidentAvailabilityView.class);

    @Autowired
    FloorService floorService;
    @Autowired
    TaskService taskService;
    @Autowired
    ResidentAccountService residentAccountService;

    @Transactional
    public void setResidentAwayStatusAndSave(boolean isAway) {
        ResidentAccount currentResidentAccount =
                residentAccountService.getResidentAccountById(SessionHandler.getLoggedInResidentAccount().getId());
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Resident status set to {}.",
                SessionHandler.getLoggedInResidentAccount().getId(), isAway);
        if (isAway) {
            residentAccountService.transferTasksOfResidentToNext(currentResidentAccount, floorService, taskService);
        }
        currentResidentAccount.setAway(isAway);
        residentAccountService.save(currentResidentAccount);
    }
}
