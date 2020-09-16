package com.wg_planner.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Floor extends AbstractEntity implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    //    @UniqueConstraint("roomNumber")
    @NotNull
    @NotEmpty
    private String floorNumber;

    @NotNull
    @NotEmpty
    private String numberOfRooms;

    @NotNull
    @NotEmpty
    private String roomStartIndex;

    @OneToMany(mappedBy = "floor", fetch = FetchType.EAGER)
    private List<Room> rooms = new LinkedList<>();

    @OneToMany
    private List<Task> tasks = new LinkedList<>();

    public Floor() {
    }

    public Floor(@NotNull @NotEmpty String floorNumber, @NotNull @NotEmpty String numberOfRooms, @NotNull @NotEmpty String roomStartIndex) {
        this.floorNumber = floorNumber;
        this.numberOfRooms = numberOfRooms;
        this.roomStartIndex = roomStartIndex;
    }

    public Floor(Long id, @NotNull @NotEmpty String floorNumber, @NotNull @NotEmpty String numberOfRooms, @NotNull @NotEmpty String roomStartIndex, List<Room> rooms) {
        this.id = id;
        this.floorNumber = floorNumber;
        this.numberOfRooms = numberOfRooms;
        this.roomStartIndex = roomStartIndex;
        this.rooms = rooms;
    }

    public Floor(Long id, @NotNull @NotEmpty String floorNumber, @NotNull @NotEmpty String numberOfRooms, @NotNull @NotEmpty String roomStartIndex, List<Room> rooms, List<Task> tasks) {
        this.id = id;
        this.floorNumber = floorNumber;
        this.numberOfRooms = numberOfRooms;
        this.roomStartIndex = roomStartIndex;
        this.rooms = rooms;
        this.tasks = tasks;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

    public void addRooms(Room room) {
        rooms.add(room);
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }


}
