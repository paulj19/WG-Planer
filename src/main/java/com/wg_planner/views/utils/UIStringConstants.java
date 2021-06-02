package com.wg_planner.views.utils;

public class UIStringConstants {
    private static UIStringConstants uiStringConstants;
    private final String ACCOUNT_CREATED_CONFIRMATION = "Account created";
    private final String AVAILABILITY_STATUS_CHANGED = "Availability status changed";

    static {
        uiStringConstants = new UIStringConstants();
    }

    private UIStringConstants() {
    }

    public static UIStringConstants getInstance() {
        return uiStringConstants;
    }

    public String getAccountCreatedConfirmation() {
        return ACCOUNT_CREATED_CONFIRMATION;
    }

    public String getAvailabilityStatusChanged() {
        return AVAILABILITY_STATUS_CHANGED;
    }
}
