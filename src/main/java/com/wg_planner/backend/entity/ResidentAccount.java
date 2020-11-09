package com.wg_planner.backend.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Entity
@Table(name = "resident_account")
@PrimaryKeyJoinColumns(
        {
                @PrimaryKeyJoinColumn(name = "id")
        }
)
public class ResidentAccount extends Account implements Cloneable {

    private Boolean away = false;

    //    @JoinColumn(name = "room_id")//this id to be used in select?
    @OneToOne
    private Room room;

    //    private User user;
    public ResidentAccount() {
    }

    //    public ResidentAccount(String username, String password,
//                   Collection<? extends GrantedAuthority> authorities) {
//        super(username, password, authorities);
//    }
    public ResidentAccount(String firstName, String lastName, String email, String username, String password, @NotNull @NotEmpty Room room, Collection<? extends GrantedAuthority> authorities) {
        super(firstName, lastName, email, username, password, authorities);
        this.room = room;
    }

//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }

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
