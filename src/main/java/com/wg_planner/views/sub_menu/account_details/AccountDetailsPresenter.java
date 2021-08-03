package com.wg_planner.views.sub_menu.account_details;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.backend.entity.ResidentAccount;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UIHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Controller
public class AccountDetailsPresenter {
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
        accountDetailsView.addLogoutButton();
        //        accountDetailsView.add(new ResidentAvailabilityView(residentAccountService, this));
        accountDetailsView.add(new AccountDeleteView(floorService, residentAccountService, taskService));
    }
}
