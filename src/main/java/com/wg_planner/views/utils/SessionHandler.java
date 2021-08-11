package com.wg_planner.views.utils;

import com.vaadin.flow.server.VaadinSession;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.ResidentAccount;
import org.apache.commons.lang3.Validate;

public class SessionHandler {
    public static final String FLOOR_CREATED = "floor_created";
    public static final String LOGGED_IN_RESIDENT_ACCOUNT = "logged_in_resident_account";

    public static void saveCreatedFloorToSession(Floor floorCreated) {
        Validate.notNull("floor created to save in the session must not be null");
        VaadinSession.getCurrent().getSession().setAttribute(FLOOR_CREATED, floorCreated);
    }

    public static void saveLoggedInResidentAccount(ResidentAccount residentAccountToSave) {
        Validate.notNull(residentAccountToSave, "loggedIn resident account to save in the session" +
                " must not be null");
        VaadinSession.getCurrent().getSession().setAttribute(LOGGED_IN_RESIDENT_ACCOUNT, residentAccountToSave);
    }

    //loading residentAccount always from DB might be unnecessary and heavy load on the DB
    public static ResidentAccount getLoggedInResidentAccount() {
        return (ResidentAccount) VaadinSession.getCurrent().getSession().getAttribute(LOGGED_IN_RESIDENT_ACCOUNT);
    }
}
