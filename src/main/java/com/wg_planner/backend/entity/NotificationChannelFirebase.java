package com.wg_planner.backend.entity;

import org.apache.commons.lang3.Validate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "notification_channel_firebase")
public class NotificationChannelFirebase extends NotificationChannel{
    @NotBlank
    @Column(unique = true, nullable = false)
    private String deviceRegistrationToken;

    public NotificationChannelFirebase() {
    }

    public NotificationChannelFirebase(@NotNull ResidentDevice owningResidentDevice, @NotBlank @NotNull String deviceRegistrationToken) {
        super.setResidentDevice(owningResidentDevice);
        setDeviceRegistrationToken(deviceRegistrationToken);
    }
    public String getDeviceRegistrationToken() {
        return deviceRegistrationToken;
    }

    public void setDeviceRegistrationToken(String registrationToken) {
        Validate.notNull(registrationToken, "parameter registration token must not be %s", (Object) null);
        Validate.notEmpty(registrationToken, "parameter registration  token must not be empty");
        this.deviceRegistrationToken = registrationToken;
    }

    @Override
    public String getNotificationToken() {
        return getDeviceRegistrationToken();
    }
}
