package com.wg_planner;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

/**
 * The entry point of the Spring Boot application.
 */

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@EnableTransactionManagement
public class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    //TODO app already exists?
    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        //todo remove on production code
        boolean isFireBaseAppInitialized = false;
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource("firebase_private-key.json").getInputStream());
        FirebaseOptions firebaseOptions = FirebaseOptions
                .builder()
                .setCredentials(googleCredentials)
                .build();
        FirebaseApp app = null;
        if (!isFireBaseAppInitialized) {
            app = FirebaseApp.initializeApp(firebaseOptions, "wg-planner");
            isFireBaseAppInitialized = true;
        }
        return FirebaseMessaging.getInstance(app);
    }
}
