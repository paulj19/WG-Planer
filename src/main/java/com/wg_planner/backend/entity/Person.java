package com.wg_planner.backend.entity;

import javax.persistence.*;

@Entity
public class Person extends AbstractEntity implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @OneToOne
    private Room room;

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

}
