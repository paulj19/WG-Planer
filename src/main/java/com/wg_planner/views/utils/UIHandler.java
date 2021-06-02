package com.wg_planner.views.utils;

import com.vaadin.flow.component.UI;

public class UIHandler {
    private static UIHandler uiHandler;
    private final String LOGIN_URL = "login/";

    static {
        uiHandler = new UIHandler();
    }

    private UIHandler() {
    }

    public static UIHandler getInstance() {
        return uiHandler;
    }
    public void navigateToLoginPage() {
        UI.getCurrent().navigate(LOGIN_URL);
    }
}
