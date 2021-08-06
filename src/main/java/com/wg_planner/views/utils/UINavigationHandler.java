package com.wg_planner.views.utils;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.QueryParameters;
import com.wg_planner.views.floor_something.FloorSomethingView;
import com.wg_planner.views.tasks.my_tasks.MyTasksView;

import java.util.*;

//todo change name to UINavigationHandler
public class UINavigationHandler {
    private static UINavigationHandler uiNavigationHandler;
    private final String LOGIN_URL = "login/";
    private final String USER_DETAILS_URL = "user_details/";

    static {
        uiNavigationHandler = new UINavigationHandler();
    }

    private UINavigationHandler() {
    }

    public static UINavigationHandler getInstance() {
        return uiNavigationHandler;
    }

    public void navigateToLoginPage() {
        UI.getCurrent().navigate(LOGIN_URL);
    }

    public void navigateToSubMenu() {
        UI.getCurrent().navigate(USER_DETAILS_URL);
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

    public void navigateToHomePage() {
        UI.getCurrent().navigate(FloorSomethingView.class);
    }
    private void navigateBackMyTasks() {
        UI.getCurrent().navigate(MyTasksView.class);
    }
}
