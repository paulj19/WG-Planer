package com.wg_planner.backend.entity;

import com.wg_planner.backend.helpers.ValidationChecks;
import org.apache.commons.lang3.Validate;
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

//    @OneToMany(mappedBy = "floor", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @OneToMany(mappedBy = "floor", fetch = FetchType.EAGER)
//    @Fetch(value = FetchMode.SUBSELECT)
    private List<Room> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "floor", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Task> tasks = new ArrayList<>();

    public Floor() {
    }

    public static class FloorBuilder {
        private final String floorNumber;

        private final String numberOfRooms;

        private  String roomStartIndex;

        private List<Room> rooms;

        private List<Task> tasks;

        public FloorBuilder(String floorNumber, String numberOfRooms) {
            Validate.notNull(floorNumber, "parameter floorNumber must not be %s", null);
            Validate.notNull(numberOfRooms, "parameter numberOfRooms must not be %s", null);

            this.floorNumber = floorNumber;
            this.numberOfRooms = numberOfRooms;
        }

        public void setRooms(List<Room> rooms) {
            Validate.notNull(rooms, "parameter rooms must not be %s", null);
            this.rooms = new ArrayList<>(rooms);
        }

        public void setRoomStartIndex(String roomStartIndex) {
            Validate.notNull(roomStartIndex, "parameter roomStartIndex must not be %s", null);
            Validate.notEmpty(roomStartIndex, "parameter roomStartIndex must not be empty");
            this.roomStartIndex = roomStartIndex;
        }

        public FloorBuilder setTasks(List<Task> tasks) {
            Validate.notNull(tasks, "parameter tasks to add must not be %s", null);
            this.tasks = new ArrayList<>(tasks);
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

    public void setRooms(List<Room> rooms)
    {
        Validate.notNull(rooms, "parameter rooms must not be %s", null);
        this.rooms = new ArrayList<>(rooms);
    }
    public void addRoom(Room room) {
        Validate.notNull(room, "parameter room to add must not be %s", null);
        rooms.add(room);
    }

    //todo: see spring auto save for setters and value change
    public void setTasks(List<Task> tasks) {
        Validate.notNull(tasks, "parameter tasks to add must not be %s", null);
        this.tasks = new ArrayList<>(tasks);
    }

    public void addTask(Task task) {
        Validate.notNull(task, "parameter task to add must not be %s", null);
        tasks.add(task);
    }

    public void setRoomStartIndex(String roomStartIndex) {
        Validate.notNull(roomStartIndex, "parameter roomStartIndex must not be %s", null);
        Validate.notEmpty(roomStartIndex, "parameter roomStartIndex must not be empty");
        this.roomStartIndex = roomStartIndex;
    }

    //this would print all the floor info
    public String toString() {
        ToStringBuilder floorAsString = new ToStringBuilder(this).
                append("floor number", floorNumber).
                append("number of rooms", numberOfRooms).
                append("room start index", roomStartIndex).
                append("rooms: ");
        for (Room room : ValidationChecks.safe(rooms)) {
            floorAsString.append(room.toString());
        }
        floorAsString.append("tasks: ");
        for (Task task : ValidationChecks.safe(tasks)) {
            floorAsString.append(task.toString());
        }
        return floorAsString.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof Floor))
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
