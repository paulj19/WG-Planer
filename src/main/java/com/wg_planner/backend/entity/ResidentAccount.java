package com.wg_planner.backend.entity;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "resident_account")
public class ResidentAccount extends Account implements Cloneable {

    private Boolean away = false;
    //, cascade = CascadeType.ALL
//    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)//normally room is always
//    gotten from ResidentAccount
    //PERSIST: create a new room if the resident account creation(via signup page) creates one
//    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
//    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE,
//    CascadeType.REFRESH})
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "ownerResidentAccount", cascade = CascadeType.ALL)
    List<ResidentDevice> residentDevices = new ArrayList<>();

    public ResidentAccount() {
    }

    public ResidentAccount(String firstName, String lastName, String email, String username,
                           String password, @NotNull @NotEmpty Room room,
                           boolean away, Collection<? extends GrantedAuthority> authorities) {
        super(firstName, lastName, email, username, password, authorities);
        Validate.notNull(room);
        this.room = room;
        this.away = away;
    }

    public ResidentAccount(String firstName, String lastName, String email, String username,
                           String password, boolean enabled, boolean accountNonExpired,
                           boolean credentialsNonExpired, boolean accountNonLocked, Collection<?
            extends GrantedAuthority> authorities, Boolean away, Room room,
                           List<ResidentDevice> residentDevices) {
        this(firstName, lastName, email, username, password, room, away, authorities);
        setResidentDevices(residentDevices);
    }

    public Boolean isAway() {
        return away;
    }

    public void setAway(Boolean away) {
        this.away = away;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        Validate.notNull(room);
        this.room = room;
    }

    public List<ResidentDevice> getResidentDevices() {
        return residentDevices;
    }

    public List<ResidentDevice> getResidentDevicesActive() {
        return residentDevices.stream().filter(residentDevice -> residentDevice.isActive()).collect(Collectors.toList());
    }

    public void setResidentDevices(List<ResidentDevice> residentDevices) {
        Validate.notNull(residentDevices, "parameter residentDevices to add must not be %s", null);
        this.residentDevices = new ArrayList<>(residentDevices);
    }

    public void addToResidentDevices(ResidentDevice residentDevice) {
        Validate.notNull(residentDevice, "parameter residentDevice to add must not be %s", null);
        if (!residentDevices.contains(residentDevice)) {
            residentDevices.add(residentDevice);
        }
    }

    public void removeResidentDevices(ResidentDevice residentDevice) {
        Validate.notNull(residentDevice, "parameter residentDevice to add must not be %s", null);
        Validate.isTrue(residentDevices.contains(residentDevice), "residentDevice to remove must " +
                "be already present");
        residentDevices.remove(residentDevice);
    }

    public String toString() {
        return new ToStringBuilder(this).
                append("id", getId()).
                append("room id", room.getId()).
                append("floor id", room.getFloor().getId()).
                append("away", away).
                append(super.toString()).
                toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof ResidentAccount))
            return false;
        ResidentAccount otherResidentAccount = (ResidentAccount) other;
        return new EqualsBuilder()
                .append(getId(), otherResidentAccount.getId())
                .append(room, otherResidentAccount.room)
                .append(away, otherResidentAccount.away)
                .isEquals() && super.equals(otherResidentAccount);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
//                .append(room)
                .append(getId())
                .append(away)
                .append(super.hashCode())//TODo verify
                .toHashCode();
    }
}
