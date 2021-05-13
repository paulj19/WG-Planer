package com.wg_planner.backend.Service.notification;

import org.apache.commons.lang3.Validate;

import java.awt.*;

public class NotificationContentImage extends NotificationContent{
    private Image notificationImage;

    public NotificationContentImage(Image notificationImageUri) {
        setNotificationImage(notificationImageUri);
    }

    @Override
    public String getContentAsString() {
        return getNotificationImage().toString();
    }

    public Image getNotificationImage() {
        return notificationImage;
    }

    public void setNotificationImage(Image notificationImage) {
        Validate.notNull(notificationImage, "parameter notificationImage to add must not be %s", null);
        this.notificationImage = notificationImage;
    }
}
