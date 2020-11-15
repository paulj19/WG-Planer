package com.wg_planner.backend.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
@Table(name = "resident_account")
public class ResidentAccount extends Account implements Cloneable {

    private Boolean away = false;

    @OneToOne(fetch = FetchType.EAGER)//normally room is always gotten from ResidentAccount
    @JoinColumn(name = "room_id")
    private Room room;

    public ResidentAccount() {
    }

    public ResidentAccount(String firstName, String lastName, String email, String username, String password, @NotNull @NotEmpty Room room, Collection<? extends GrantedAuthority> authorities) {
        super(firstName, lastName, email, username, password, authorities);
        this.room = room;
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
        if (room == null) {
            if (this.room != null) {
                this.room.setResidentAccount(null);
            }
        } else {
            room.setResidentAccount(this);
        }
        this.room = room;
    }

    public String toString() {
        return new ToStringBuilder(this).
                append("room number", room.getRoomNumber()).
                append("floor number", room.getFloor().getFloorNumber()).
                append("away", away).
                append(super.toString()).
                toString();
    }
}
