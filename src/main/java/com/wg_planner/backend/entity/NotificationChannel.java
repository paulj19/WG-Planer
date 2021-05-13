package com.wg_planner.backend.entity;

import org.apache.commons.lang3.Validate;

import javax.persistence.*;

@Entity
@Table(name = "notification_channel")
abstract public class NotificationChannel extends AbstractEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_device_id", nullable = false)
    ResidentDevice residentDevice;
    public abstract String getNotificationToken();

    public ResidentDevice getResidentDevice() {
        return residentDevice;
    }

    public void setResidentDevice(ResidentDevice residentDevice) {
        Validate.notNull(residentDevice, "parameter residentDevice must not be %s", (Object) null);
        this.residentDevice = residentDevice;
    }
}
