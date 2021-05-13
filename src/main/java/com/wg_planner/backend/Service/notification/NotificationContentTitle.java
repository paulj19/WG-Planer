package com.wg_planner.backend.Service.notification;

import org.apache.commons.lang3.Validate;

public class NotificationContentTitle extends NotificationContent{
    private String title;

    public NotificationContentTitle(String title) {
        setTitle(title);
    }

    @Override
    public String getContentAsString() {
        return getTitle();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Validate.notNull(title, "parameter notification title to add must not be %s", null);
        Validate.notEmpty(title, "parameter notification title must not be empty");
        this.title = title;
    }
}
