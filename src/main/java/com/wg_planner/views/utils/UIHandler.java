package com.wg_planner.views.utils;

import com.vaadin.flow.component.UI;
import com.wg_planner.views.register.RegisterView;

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
    public void navigateToRegisterPageParamRoomId(Long roomId) {
        UI.getCurrent().navigate(RegisterView.class, roomId);
    }
}
