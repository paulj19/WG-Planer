package com.wg_planner.views.utils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.QueryParameters;
import com.wg_planner.views.register.RegisterView;

import java.util.*;

//todo change name to UINavigationHandler
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
        Map<String, List<String>> parametersMap = new HashMap<>();
        parametersMap.put("room_id", new ArrayList<>(Collections.singleton(Long.toString(roomId))));
        UI.getCurrent().navigate("register_form", new QueryParameters(parametersMap));
    }

    public void navigateToRegisterPageParamFloorId(Long floorId) {
        Map<String, List<String>> parametersMap = new HashMap<>();
        parametersMap.put("floor_id", new ArrayList<>(Collections.singleton(Long.toString(floorId))));
        UI.getCurrent().navigate("register_form", new QueryParameters(parametersMap));
    }
}
