package com.wg_planner.backend.Service.notification;

import com.google.firebase.messaging.Message;

public abstract class NotificationFirebaseType {
    public abstract Message getAsFirebaseMessage(String token);
}
