package com.wg_planner.backend.Service.notification;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.wg_planner.backend.entity.Task;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;

//TODO the only thing "firebase" here is the message in the getNotificationMessage; refactor
public class NotificationTypeTaskReminder extends NotificationFirebaseType {
    private NotificationContentTitle notificationContentTitle;
    private NotificationContentBody notificationContentBody;

    public NotificationTypeTaskReminder(NotificationContentTitle notificationContentTitle, NotificationContentBody notificationContentBody) {
        setNotificationContentTitle(notificationContentTitle);
        setNotificationContentBody(notificationContentBody);
    }

    public NotificationTypeTaskReminder(String title, String body) {
        Validate.notEmpty(title, "parameter notification title must not be empty");
        Validate.notNull(title, "parameter notification title to add must not be %s", null);
        Validate.notEmpty(body, "parameter notification body must not be empty");
        Validate.notNull(body, "parameter notification body to add must not be %s", null);
        this.notificationContentTitle = new NotificationContentTitle(title);
        this.notificationContentBody = new NotificationContentBody(body);
    }

    public static NotificationTypeTaskReminder getInstance(Task taskToRemind) {
        return new NotificationTypeTaskReminder(taskToRemind.getTaskNotificationContent().getTitle(),
                taskToRemind.getTaskNotificationContent().getBody());
    }

    @Override
    public Message getNotificationMessage(String token) {
        Validate.notEmpty(token, "parameter notification token must not be empty");
        Validate.notNull(token, "parameter notification token to add must not be %s", null);

        Notification notification = Notification
                .builder()
                .setTitle(getNotificationContentTitle().getContentAsString())
                .setBody(getNotificationContentBody().getContentAsString())
                .build();
        return Message
                .builder()
                .setToken(token)
                .setNotification(notification)
                .build();
    }

    public NotificationContentTitle getNotificationContentTitle() {
        return notificationContentTitle;
    }

    public void setNotificationContentTitle(NotificationContentTitle notificationContentTitle) {
        Validate.notNull(notificationContentBody, "parameter notificationContentBody to add must not be %s", null);
        this.notificationContentTitle = notificationContentTitle;
    }

    public NotificationContentBody getNotificationContentBody() {
        return notificationContentBody;
    }

    public void setNotificationContentBody(NotificationContentBody notificationContentBody) {
        Validate.notNull(notificationContentBody, "parameter notificationContentBody to add must not be %s", null);
        this.notificationContentBody = notificationContentBody;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("Notification Type: Task Reminder").
                append("Title", notificationContentTitle.getContentAsString()).
                toString();
    }

}
