package com.wg_planner.backend.Service.notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.entity.NotificationChannelFirebase;
import com.wg_planner.backend.entity.ResidentAccount;
import com.wg_planner.backend.entity.ResidentDevice;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceFirebase implements NotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private final ResidentAccountService residentAccountService;

    @Autowired
    public NotificationServiceFirebase(FirebaseMessaging firebaseMessaging,
                                       ResidentAccountService residentAccountService) {
        Validate.notNull(firebaseMessaging, "parameter firebaseMessaging to add must not be %s", null);
        Validate.notNull(residentAccountService, "parameter residentAccountService to add must not be %s", null);
        this.firebaseMessaging = firebaseMessaging;
        this.residentAccountService = residentAccountService;
    }

    @Override
    public SendResult sendNotification(NotificationFirebaseType notificationFirebaseType,
                                       ResidentAccount residentAccountToNotify) {
        try {
            for (String token : getTokensFromResidentAccount(residentAccountToNotify))
                firebaseMessaging.send(notificationFirebaseType.getAsFirebaseMessage(token));

            return SendResult.SUCCESS;
        } catch (FirebaseMessagingException e) {
            return SendResult.FAILURE;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return SendResult.FAILURE;
        }
    }

    private List<String> getTokensFromResidentAccount(ResidentAccount residentAccountToNotify) {
        List<ResidentDevice> residentDevicesToNotify =
                residentAccountToNotify.getResidentDevices().stream().filter(residentDevice -> residentDevice.getDeviceNotificationChannels().stream().anyMatch(notificationChannel -> notificationChannel.getClass().equals(NotificationChannelFirebase.class))).collect(Collectors.toList());
        if (residentDevicesToNotify.isEmpty()) {
            throw new RuntimeException("no notificationChannelFirebase found for " +
                    "residentAccount");
        }
        return residentDevicesToNotify.stream().map(residentDevice -> residentDevice.getDeviceNotificationChannels().stream().map(notificationChannel -> notificationChannel.getNotificationToken()).collect(Collectors.toList())).collect(Collectors.toList()).stream().flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
