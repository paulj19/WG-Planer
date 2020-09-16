package com.wg_planner.backend.entity;

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

    @NotNull
    @NotEmpty
    @ManyToOne
    private Floor floor;

    @OneToOne
    private Resident resident;

//    public Room(@NotNull @NotEmpty String roomNumber) {
//        this.roomNumber = roomNumber;
//    }

    public Room() { }

    public Room(@NotNull @NotEmpty String roomNumber, @NotNull @NotEmpty Floor floor) {
        this.roomNumber = roomNumber;
        this.floor = floor;
    }

    public Room(@NotNull @NotEmpty String roomNumber, @NotNull @NotEmpty Floor floor, Resident resident) {
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.resident = resident;
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

    public Resident getResident() {
        return resident;
    }

    public void setResident(Resident resident) {
        this.resident = resident;
    }
}
