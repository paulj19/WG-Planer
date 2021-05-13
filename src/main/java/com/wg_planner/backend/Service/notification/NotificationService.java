package com.wg_planner.backend.Service.notification;

import com.wg_planner.backend.entity.ResidentAccount;

public interface NotificationService {
    enum SendResult {
        SUCCESS(0), FAILURE(1);
        SendResult(int i) {
        }
    }

    SendResult sendNotification(NotificationFirebaseType notificationFirebaseType,
                                ResidentAccount residentAccountToNotify);
}
