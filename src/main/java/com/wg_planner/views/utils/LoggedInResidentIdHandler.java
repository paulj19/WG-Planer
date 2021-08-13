package com.wg_planner.views.utils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.Page;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.utils.LogHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class LoggedInResidentIdHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggedInResidentIdHandler.class);
    @Autowired
    ResidentAccountService residentAccountService;

    public void sentResidentAccountId() {
        Page page = UI.getCurrent().getPage();
        page.executeJs("setResidentAccountId.initializeResidentAccountId($0);",
                SessionHandler.getLoggedInResidentAccount().getId().toString());
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {} set to common javascript.",
                SessionHandler.getLoggedInResidentAccount().getId());
    }

}
