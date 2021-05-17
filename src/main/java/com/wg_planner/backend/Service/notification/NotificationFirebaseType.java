package com.wg_planner.backend.Service.notification;

import com.google.firebase.messaging.Message;

//TODO class NotificationMessage
public abstract class NotificationFirebaseType {
    public abstract Message getNotificationMessage(String token);
}
