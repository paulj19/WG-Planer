package com.wg_planner.backend.Service.notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.wg_planner.backend.entity.NotificationChannel;
import com.wg_planner.backend.entity.NotificationChannelFirebase;
import com.wg_planner.backend.entity.ResidentAccount;
import com.wg_planner.backend.entity.ResidentDevice;
import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.UnauthorizedPages.create_floor.NewRoomView;
import org.apache.commons.lang3.Validate;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class NotificationServiceFirebase implements NotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(NewRoomView.class);

    @Autowired
    public NotificationServiceFirebase(FirebaseMessaging firebaseMessaging) {
        Validate.notNull(firebaseMessaging, "parameter firebaseMessaging to add must not be %s", null);
        this.firebaseMessaging = firebaseMessaging;
    }

    @Override
    public void sendNotification(NotificationFirebaseType notificationFirebaseType,
                                 ResidentAccount residentAccountToNotify) {
        for (String token : getTokensFromResidentAccount(residentAccountToNotify)) {
            if (sendNotificationToSingleDevice(notificationFirebaseType.getNotificationMessage(token)) == SendResult.FAILURE) {
                LOGGER.error(LogHandler.getNotificationError(),
                        "Notification send failed, Resident Account: {}, Notification Details: {}, Token: {}." + residentAccountToNotify.toString(),
                        notificationFirebaseType.toString(), token);
            }
            LOGGER.info(LogHandler.getTestRun(),
                    "Sending notification successful, Resident Account: {}, Notification Details: {}" + residentAccountToNotify.toString(),
                    notificationFirebaseType.toString());
        }
    }

    private SendResult sendNotificationToSingleDevice(Message messageToSend) {
        try {
            firebaseMessaging.send(messageToSend);
            return SendResult.SUCCESS;
        } catch (FirebaseMessagingException e) {
            System.out.println(e.getMessage() + e.getCause());
            return SendResult.FAILURE;
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return SendResult.FAILURE;
        }
    }

    private List<String> getTokensFromResidentAccount(ResidentAccount residentAccountToNotify) {
        List<ResidentDevice> residentDevicesToNotify =
                residentAccountToNotify.getResidentDevicesActive().stream().filter(residentDevice -> residentDevice.getDeviceNotificationChannels().stream().anyMatch(notificationChannel -> notificationChannel.getClass().equals(NotificationChannelFirebase.class))).collect(Collectors.toList());
        if (residentDevicesToNotify.isEmpty()) {
            LOGGER.error("Notification send not successful no notificationChannelFirebase found for " +
                    "Resident Account: {} ", residentAccountToNotify.toString());
        }
        residentDevicesToNotify.forEach(residentDevice -> LOGGER.info(LogHandler.getNotificationError(),
                "getTokensFromResidentAccount returning resident devices" + residentDevice.toString()));
        return residentDevicesToNotify.stream().map(residentDevice -> residentDevice.getDeviceNotificationChannels().stream().map(NotificationChannel::getNotificationToken).collect(Collectors.toList())).collect(Collectors.toList()).stream().flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
