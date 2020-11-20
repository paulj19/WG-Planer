package com.wg_planner.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

//TODO change members to final
@Entity
public class Floor extends AbstractEntity implements Cloneable {
    @NotNull
    @NotEmpty
    @Size(max = 255)
    @Column(unique = true)
    private String floorNumber;

    @NotNull
    @NotEmpty
    private String numberOfRooms;

    @NotNull
    @NotEmpty
    private String roomStartIndex;

    @OneToMany(mappedBy = "floor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Room> rooms;

    @OneToMany(mappedBy = "floor", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Task> tasks = new ArrayList<>();

    public Floor() {
    }

    public static class FloorBuilder {
        private final String floorNumber;

        private final String numberOfRooms;

        private final String roomStartIndex;

        private List<Room> rooms;

        private List<Task> tasks;

        public FloorBuilder(String floorNumber, String numberOfRooms, String roomStartIndex) {
            this.floorNumber = floorNumber;
            this.numberOfRooms = numberOfRooms;
            this.roomStartIndex = roomStartIndex;
        }

        public void setRooms(List<Room> rooms) {
            this.rooms = rooms;
        }

        public FloorBuilder setTasks(List<Task> tasks) {
            this.tasks = tasks;
            return this;
        }

        public Floor build() {
            return new Floor(this);
        }
    }

    private Floor(FloorBuilder builder) {
        this.floorNumber = builder.floorNumber;
        this.numberOfRooms = builder.numberOfRooms;
        this.roomStartIndex = builder.roomStartIndex;
        this.rooms = builder.rooms;
        this.tasks = builder.tasks;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public String getNumberOfRooms() {
        return numberOfRooms;
    }

    public String getRoomStartIndex() {
        return roomStartIndex;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public void addTask(Task task) {
        tasks.add(task);

    }
}
