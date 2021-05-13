package com.wg_planner.views.main;

import com.wg_planner.views.utils.LoggedInResidentIdHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MainViewPresenter {
    @Autowired
    LoggedInResidentIdHandler loggedInResidentIdHandler;

    public void init() {
        loggedInResidentIdHandler.sentResidentAccountId();
    }
}
