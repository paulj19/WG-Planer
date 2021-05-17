package com.wg_planner.backend.entity.ServiceTest;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.wg_planner.backend.Service.FirebaseMessagingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class FirebaseMessagingServiceTest {
//    @Autowired
//    FirebaseMessagingService firebaseMessagingService;
//
//    @Before
//    private Note getNotificationNote() {
//        return new Note("TestSubject", "TestContent");
//    }
//
//    @Test
//    public void FirebaseMessagingService_sentNotification_sentsNotificationSuccessfully() throws FirebaseMessagingException {
//        firebaseMessagingService.sendNotification(getNotificationNote(), "mobileapptoken");
//    }
}
