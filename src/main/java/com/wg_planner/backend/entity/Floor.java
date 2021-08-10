package com.wg_planner.backend.entity;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
    @Size(max = 255)
    @NotEmpty(message = "field must not be empty")
    @Column(nullable = false)
    private String floorName;

    @NotNull
    @NotEmpty
    private String roomStartIndex;

    @NotEmpty
    @Column(unique = true, nullable = false)
    @Size(min = 4, max = 4)
    private String floorCode;

    @OneToMany(mappedBy = "floor", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Room> rooms = new ArrayList<>();

    //TODO inserting persist to save the task from floor_creation. todo, look into deleting problem
    // from floor. merge in room does not save the task, dont know why
    @OneToMany(mappedBy = "floor", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Task> tasks = new ArrayList<>();

    private Floor() {
    }

    public Floor(String floorCode) {
        this.floorCode = floorCode;
    }

    public static class FloorBuilder {
        private String floorNumber;

        private String floorCode;

        private String firstRoomNumber;

        private List<Room> rooms;

        private List<Task> tasks;

        private FloorBuilder() {
        }

        private FloorBuilder(String floorCode) {
            setFloorCode(floorCode);
        }

        public FloorBuilder(String floorNumber, String floorCode) {
            Validate.notNull(floorNumber, "parameter floorNumber must not be %s", null);
            this.floorNumber = floorNumber;
            setFloorCode(floorCode);
        }

        public void setFloorNumber(String floorNumber) {
            this.floorNumber = floorNumber;
        }

        public void setRooms(List<Room> rooms) {
            Validate.notNull(rooms, "parameter rooms must not be %s", null);
            if (!rooms.isEmpty())
                setFirstRoomNumber(rooms.get(0).getRoomName());
            this.rooms = new ArrayList<>(rooms);
        }

        public void setFirstRoomNumber(String firstRoomNumber) {
            Validate.notNull(firstRoomNumber, "parameter roomStartIndex must not be %s", null);
            Validate.notEmpty(firstRoomNumber, "parameter roomStartIndex must not be empty");
            this.firstRoomNumber = firstRoomNumber;
        }

        public FloorBuilder setTasks(List<Task> tasks) {
            Validate.notNull(tasks, "parameter tasks to add must not be %s", null);
            this.tasks = new ArrayList<>(tasks);
            return this;
        }

        public void setFloorCode(String floorCode) {
            Validate.notNull(floorCode, "parameter floorCode must not be %s", null);
            Validate.notEmpty(floorCode, "parameter floorCode must not be empty");
            this.floorCode = floorCode;
        }

        public Floor build() {
            return new Floor(this);
        }
    }

    private Floor(FloorBuilder builder) {
        this.floorName = builder.floorNumber;
        this.roomStartIndex = builder.firstRoomNumber;
        this.rooms = builder.rooms;
        this.tasks = builder.tasks;
        setFloorCode(builder.floorCode);
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName.trim();
    }

    public String getFloorName() {
        return floorName;
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

    public void setRooms(List<Room> rooms) {
        Validate.notNull(rooms, "parameter rooms must not be %s", null);
        if (!rooms.isEmpty())
            setFirstRoomNumber(rooms.get(0).getRoomName());
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

    public void removeTaskFromFloor(Task task) {
        Validate.notNull(task, "parameter task to add must not be %s", null);
        Validate.notNull(tasks, "list of tasks in floor must not be null");
        Validate.isTrue(tasks.contains(task), "list of tasks in floor must contain the task to remove");
        tasks.remove(task);
    }

    public void setFirstRoomNumber(String roomStartIndex) {
        Validate.notNull(roomStartIndex, "parameter roomStartIndex must not be %s", null);
        Validate.notEmpty(roomStartIndex, "parameter roomStartIndex must not be empty");
        this.roomStartIndex = roomStartIndex;
    }

    public String getFloorCode() {
        return floorCode;
    }

    public void setFloorCode(String floorCode) {
        Validate.notNull(floorCode, "parameter floorCode must not be %s", null);
        Validate.notEmpty(floorCode, "parameter floorCode must not be empty");
        this.floorCode = floorCode;
    }

    //this would print all the floor info
    public String toString() {
        ToStringBuilder floorAsString = new ToStringBuilder(this).
                append("id", getId()).
                append("floor number", floorName).
                append("room start index", roomStartIndex).
                append("room ids: ");
        rooms.forEach(room -> floorAsString.append(room.getId()));
        floorAsString.append("tasks ids: ");
        tasks.forEach(task -> floorAsString.append(task.getId()));
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
                .append(getId(), otherFloor.getId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getId())
                .toHashCode();
    }
}
