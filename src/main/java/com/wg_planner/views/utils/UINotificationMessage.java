package com.wg_planner.views.utils;

import com.vaadin.flow.component.notification.Notification;

import static com.vaadin.flow.component.notification.Notification.Position.BOTTOM_STRETCH;

public class UINotificationMessage {
    public static void notify(String notificationMessage) {
        Notification.show(notificationMessage, 10000, BOTTOM_STRETCH);
    }

    public static void notifyTaskChange() {
        UINotificationMessage.notify("A change was made to the task since this page was last loaded, please refresh the page.");
    }
}
