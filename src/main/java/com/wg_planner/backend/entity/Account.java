package com.wg_planner.backend.entity;

import javax.persistence.*;

@Entity
public class Account extends AbstractEntity implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

//    public Account(Room room) {
//        this.room = room;
//    }

//    private String roomNumber;
//
//    public String getRoom() {
//        return roomNumber;
//    }
//
//    public void setRoom(String roomNumber) {
//        this.roomNumber = roomNumber;
//    }

    @OneToOne
    @JoinColumn(name = "roomId")
    private Room room;

    public Account() {
    }

    public Account(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
