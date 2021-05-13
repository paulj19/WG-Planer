package com.wg_planner.backend.Service.notification;

import org.apache.commons.lang3.Validate;

public class NotificationContentBody extends NotificationContent{
    private String body;

    public NotificationContentBody(String body) {
        setBody(body);
    }

    @Override
    public String getContentAsString() {
        return getBody();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        Validate.notEmpty(body, "parameter notification body must not be empty");
        Validate.notNull(body, "parameter notification body to add must not be %s", null);
        this.body = body;
    }
}
