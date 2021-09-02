package com.wg_planner.backend.entity;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "resident_device")
public class ResidentDevice extends AbstractEntity implements Cloneable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resident_account_id", nullable = false)
    ResidentAccount ownerResidentAccount;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "residentDevice", cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
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

    public void removeNotificationChannel(String notificationChannelToRemoveToken) {
        Validate.notNull(notificationChannelToRemoveToken, "parameter notificationChannelToRemove to " +
                "add must not be %s", null);
        deviceNotificationChannels.removeIf(notificationChannel -> notificationChannel.getNotificationToken().equals(notificationChannelToRemoveToken));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("Resident device id: ", getId()).
                toString();
    }
}
