package com.wg_planner.views.utils;

import com.vaadin.flow.component.UI;
import com.wg_planner.backend.entity.Floor;

public class SessionHelper {
    public static final String FLOOR_CREATED = "floor_created";

    public static void saveCreatedFloorToSession(Floor floorCreated) {
        UI.getCurrent().getSession().setAttribute(FLOOR_CREATED, floorCreated);
    }

    public static Floor getFloorFromSession() {
        return (Floor) UI.getCurrent().getSession().getAttribute(FLOOR_CREATED);
    }
}
