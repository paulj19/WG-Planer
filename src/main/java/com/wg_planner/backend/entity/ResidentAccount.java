package com.wg_planner.backend.entity;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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
    //, cascade = CascadeType.ALL
//    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)//normally room is always gotten from ResidentAccount
    //PERSIST: create a new room if the resident account creation(via signup page) creates one
//    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    public ResidentAccount() {
    }

    public ResidentAccount(String firstName, String lastName, String email, String username, String password, @NotNull @NotEmpty Room room,
                           boolean away, Collection<? extends GrantedAuthority> authorities) {
        super(firstName, lastName, email, username, password, authorities);
        Validate.notNull(room);
        Validate.notNull(away);
        this.room = room;
        this.away = away;
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

    public String toString() {
        return new ToStringBuilder(this).
                append("room number", room.getRoomNumber()).
                append("floor number", room.getFloor().getFloorNumber()).
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
                .append(room, otherResidentAccount.room)
                .append(away, otherResidentAccount.away)
                .isEquals() && super.equals(otherResidentAccount);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
//                .append(room)
                .append(away)
                .append(super.hashCode())//TODo verify
                .toHashCode();
    }
}
