package com.wg_planner.backend.entity;

import org.apache.commons.lang3.Validate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "resident_device")
public class ResidentDevice extends AbstractEntity implements Cloneable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resident_account_id", nullable = false)
    ResidentAccount ownerResidentAccount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "residentDevice", cascade = CascadeType.ALL)
    List<NotificationChannel> deviceNotificationChannels = new ArrayList<>();

    public ResidentDevice() {

    }

    public ResidentDevice(ResidentAccount ownerResidentAccount) {
        setOwnerResidentAccount(ownerResidentAccount);
    }

    public ResidentAccount getOwnerResidentAccount() {
        return ownerResidentAccount;
    }

    public void setOwnerResidentAccount(ResidentAccount ownerResidentAccount) {
        Validate.notNull(ownerResidentAccount, "parameter ownerResidentAccounts to add must not " +
                "be %s", null);
        this.ownerResidentAccount = ownerResidentAccount;
    }

    public List<NotificationChannel> getDeviceNotificationChannels() {
        return deviceNotificationChannels;
    }

    public void setDeviceNotificationChannels(List<NotificationChannel> notificationChannels) {
        Validate.notNull(notificationChannels, "parameter notificationChannels to add must not be" +
                " %s", null);
        this.deviceNotificationChannels = new ArrayList<>(deviceNotificationChannels);
    }

    public void addToDeviceNotificationChannels(NotificationChannel notificationChannelToAdd) {
        Validate.notNull(notificationChannelToAdd, "parameter notificationChannelToAdd to add " +
                "must not be %s", null);
        if (!deviceNotificationChannels.contains(notificationChannelToAdd)) {
            deviceNotificationChannels.add(notificationChannelToAdd);
        }
    }

    public void removeResidentDevices(NotificationChannel notificationChannelToRemove) {
        Validate.notNull(notificationChannelToRemove, "parameter notificationChannelToRemove to " +
                "add must not be %s", null);
        Validate.isTrue(deviceNotificationChannels.contains(notificationChannelToRemove),
                "residentDevice to remove must be already present");
        deviceNotificationChannels.remove(notificationChannelToRemove);
    }
}
