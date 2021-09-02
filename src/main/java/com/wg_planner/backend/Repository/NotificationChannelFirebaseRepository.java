package com.wg_planner.backend.Repository;

import com.wg_planner.backend.entity.NotificationChannelFirebase;
import com.wg_planner.backend.entity.ResidentDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationChannelFirebaseRepository extends JpaRepository<NotificationChannelFirebase, Long> {
    @Query("select nc.residentDevice from NotificationChannelFirebase nc where nc.deviceRegistrationToken = :registrationToken and nc.active = true and nc" +
            ".residentDevice.active = true ")
    ResidentDevice findResidentDevice(String registrationToken);
    @Query("select nc from NotificationChannelFirebase nc where nc.deviceRegistrationToken = :registrationToken and nc.active = true ")
    NotificationChannelFirebase findNotificationChannel(String registrationToken);
}
