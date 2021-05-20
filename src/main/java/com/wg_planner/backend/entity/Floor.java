package com.wg_planner.backend.entity;

import com.wg_planner.backend.utils.HelperMethods;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Range;

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

    @Column(name = "number_of_rooms", nullable = false)
    @NotNull(message = "field must not be empty")
    @Range(min = 1, message = "number of rooms should be 1 or more")
    private Integer numberOfRooms;

    @NotNull
    @NotEmpty
    private String roomStartIndex;

    //    @OneToMany(mappedBy = "floor", fetch = FetchType.EAGER)
//    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "floor", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Room> rooms = new ArrayList<>();

    //persist stops from deleting the task, dont know why. persist in room actually saves the task when saved
    // from floor. merge in room does not save the task, dont know why
    @OneToMany(mappedBy = "floor", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.DETACH})
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Task> tasks = new ArrayList<>();

    public Floor() {
    }

    public static class FloorBuilder {
        private final String floorNumber;

        private Integer numberOfRooms;

        private String firstRoomNumber;

        private List<Room> rooms;

        private List<Task> tasks;

        public FloorBuilder(String floorNumber, Integer numberOfRooms, String roomStartIndex) {
            this(floorNumber, numberOfRooms);
            setFirstRoomNumber(roomStartIndex);
        }

        public FloorBuilder(String floorNumber, Integer numberOfRooms) {
            Validate.notNull(floorNumber, "parameter floorNumber must not be %s", null);
//            Validate.notNull(numberOfRooms, "parameter numberOfRooms must not be %s", null);

            this.floorNumber = floorNumber;
            this.numberOfRooms = numberOfRooms;
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

        public Floor build() {
            return new Floor(this);
        }
    }

    private Floor(FloorBuilder builder) {
        this.floorName = builder.floorNumber;
        this.numberOfRooms = builder.numberOfRooms;
        this.roomStartIndex = builder.firstRoomNumber;
        this.rooms = builder.rooms;
        this.tasks = builder.tasks;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getFloorName() {
        return floorName;
    }

    public Integer getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(Integer numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
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

    //this would print all the floor info
    public String toString() {
        ToStringBuilder floorAsString = new ToStringBuilder(this).
                append("id", getId()).
                append("floor number", floorName).
                append("number of rooms", numberOfRooms).
                append("room start index", roomStartIndex).
                append("room ids: ");
        for (Room room : HelperMethods.safe(rooms)) {
            floorAsString.append(room.getId());
        }
        floorAsString.append("tasks ids: ");
        for (Task task : HelperMethods.safe(tasks)) {
            floorAsString.append(task.getId());
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
                .append(getId(), otherFloor.getId())
                .append(numberOfRooms, otherFloor.numberOfRooms)
                .append(floorName, otherFloor.floorName)
                .append(roomStartIndex, otherFloor.roomStartIndex)
                .append(rooms, otherFloor.rooms)
                .append(tasks, otherFloor.tasks)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getId())
                .append(floorName)
                .append(numberOfRooms)
                .append(roomStartIndex)
                .append(rooms)
                .append(tasks)
                .toHashCode();
    }
}
