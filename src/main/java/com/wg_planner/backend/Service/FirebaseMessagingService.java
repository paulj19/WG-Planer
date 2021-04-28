package com.wg_planner.backend.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.wg_planner.backend.utils.Note;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessagingService {
    private final FirebaseMessaging firebaseMessaging;

    public FirebaseMessagingService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    //TODO handle exception
//    public String sendNotification(Note note, String token) throws FirebaseMessagingException {
    public String sendNotification(Note note, String token) {

        Notification notification = Notification
                .builder()
                .setTitle(note.getSubject())
                .setBody(note.getContent())
                .build();

        Message message = Message
                .builder()
                .setToken(token)
                .setNotification(notification)
//                .putAllData(note.getData())
                .build();

        try {
            String return_val = firebaseMessaging.send(message);
            return "Success";
        } catch (Exception e) {
            return "Failure";
        }
    }
}
