package com.wg_planner.backend.entity;

import javax.persistence.*;

@Entity
@Table(name = "notification_channel")
abstract public class NotificationChannel extends AbstractEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_device_id", nullable = false)
    ResidentDevice residentDevice;
}
