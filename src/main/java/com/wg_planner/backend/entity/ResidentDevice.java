package com.wg_planner.backend.entity;

import javax.persistence.*;
@Entity
@Table(name = "resident_device")
public class ResidentDevice extends AbstractEntity implements Cloneable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resident_account_id", nullable = false)
    ResidentAccount residentAccountOwningDevice;

    public ResidentAccount getResidentAccountOwningDevice() {
        return residentAccountOwningDevice;
    }

    public void setResidentAccountOwningDevice(ResidentAccount residentAccountOwningDevice) {
        this.residentAccountOwningDevice = residentAccountOwningDevice;
    }
}
