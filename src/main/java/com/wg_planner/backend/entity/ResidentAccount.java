package com.wg_planner.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "resident_account")
@PrimaryKeyJoinColumns(
        {
                @PrimaryKeyJoinColumn(name = "id")
        }
)
public class ResidentAccount extends Account implements Cloneable {

    private Boolean away = false;

    @OneToOne
//    @JoinColumn(name = "room_id")//this id to be used in select?
    private Room room;

    public ResidentAccount() {
    }

    public ResidentAccount(@NotNull @NotEmpty Room room) {
        this.room = room;
    }

//    @Id
//    public Long getId() {
//        return super.getId();
//    }

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
}
