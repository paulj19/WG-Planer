package com.wg_planner.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "notification_channel_firebase")
public class NotificationChannelFirebase extends NotificationChannel{
    @NotBlank
    @Column(unique = true, nullable = false)
    private String deviceRegistrationToken;

    public String getDeviceRegistrationToken() {
        return deviceRegistrationToken;
    }

    public void setDeviceRegistrationToken(String deviceRegistrationToken) {
        this.deviceRegistrationToken = deviceRegistrationToken;
    }
}
