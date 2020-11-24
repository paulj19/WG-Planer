package com.wg_planner.backend.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.transaction.annotation.Transactional;

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
    @Column(unique = true, nullable = false)
    private String floorNumber;

    @NotNull
    @NotEmpty
    private String numberOfRooms;

    @NotNull
    @NotEmpty
    private String roomStartIndex;

    @OneToMany(mappedBy = "floor", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
//    @Fetch(value = FetchMode.SUBSELECT)
    private List<Room> rooms;

    @OneToMany(mappedBy = "floor", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @Fetch(value = FetchMode.SUBSELECT)
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

    //todo: see spring auto save for setters and value change
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    //this would print all the floor info
    public String toString() {
        ToStringBuilder floorAsString = new ToStringBuilder(this).
                append("floor number", floorNumber).
                append("number of rooms", numberOfRooms).
                append("room start index", roomStartIndex).
                append("rooms: ");
        for (Room room : rooms) {
            floorAsString.append(room.toString());
        }
        floorAsString.append("tasks: ");
        for (Task task : tasks) {
            floorAsString.append(task.toString());
        }
        return floorAsString.toString();
    }

    @Override
    public boolean equals(Object other) {
        if(other == this)
            return true;
        if(!(other instanceof  Floor))
            return false;
        Floor otherFloor = (Floor) other;
        return new EqualsBuilder()
                .append(numberOfRooms, otherFloor.numberOfRooms)
                .append(floorNumber, otherFloor.floorNumber)
                .append(roomStartIndex, otherFloor.roomStartIndex)
                .append(rooms, otherFloor.rooms)
                .append(tasks, otherFloor.tasks)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(floorNumber)
                .append(numberOfRooms)
                .append(roomStartIndex)
                .append(rooms)
                .append(tasks)
                .toHashCode();
    }
}
