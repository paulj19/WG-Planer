package com.wg_planner.backend.entity;

import org.springframework.beans.factory.annotation.Qualifier;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Room extends AbstractEntity implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @NotNull
    @NotEmpty
//    @UniqueConstraint("roomNumber")
    private String roomNumber;

//    @NotNull
//    @NotEmpty
    @ManyToOne
    private Floor floor;

    private Boolean occupied;

    //owning side, referencing side
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "room", cascade = CascadeType.ALL)
//    @OneToOne
    private ResidentAccount residentAccount;

//    public Room(@NotNull @NotEmpty String roomNumber) {
//        this.roomNumber = roomNumber;
//    }

    public Room() { }

    public Room(@NotNull @NotEmpty String roomNumber, @NotNull @NotEmpty Floor floor) {
        this.roomNumber = roomNumber;
        this.floor = floor;
    }

    public Room(@NotNull @NotEmpty String roomNumber, @NotNull @NotEmpty Floor floor, ResidentAccount residentAccount) {
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.residentAccount = residentAccount;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    @Override
    public String toString() {
        return roomNumber;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public ResidentAccount getResidentAccount() {
        return residentAccount;
    }

    public void setResidentAccount(ResidentAccount residentAccount) {
        this.residentAccount = residentAccount;
    }

    public Boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(Boolean occupied) {
        this.occupied = occupied;
    }
}
