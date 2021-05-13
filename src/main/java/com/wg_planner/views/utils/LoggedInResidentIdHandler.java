package com.wg_planner.views.utils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.Page;
import com.wg_planner.backend.Service.ResidentAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class LoggedInResidentIdHandler {
    @Autowired
    ResidentAccountService residentAccountService;

    public void sentResidentAccountId() {
        Page page = UI.getCurrent().getPage();
        page.executeJs("setResidentAccountId.initializeResidentAccountId($0);",
                AccountDetailsHelper.getUserResidentAccount(residentAccountService).getId().toString());
    }

}
