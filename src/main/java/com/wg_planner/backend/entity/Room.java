package com.wg_planner.backend.entity;

import com.wg_planner.backend.utils.HelperMethods;
import org.apache.commons.lang3.StringUtils;
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

@Entity
public class Room extends AbstractEntity implements Cloneable {

    @NotNull
    @NotEmpty
    @Size(max = 255)
    @Column(nullable = false)
    private String roomName;

    private Boolean occupied = false;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "room", cascade = CascadeType.ALL)
    private ResidentAccount residentAccount;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;

    @OneToMany(mappedBy = "assignedRoom", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @Fetch(value = FetchMode.SUBSELECT)
    List<Task> assignedTasks = new ArrayList<>();

    public Room() {
    }

    public Room(@NotNull @NotEmpty String roomName, @NotNull @NotEmpty Floor floor) {
        setRoomName(roomName);
        setFloor(floor);
    }

    public Room(@NotNull @NotEmpty String roomName, @NotNull @NotEmpty Floor floor, boolean isOccupied) {
        this(roomName, floor);
        setOccupied(isOccupied);
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        Validate.notNull(roomName, "parameter roomNumber to add must not be %s", null);
        Validate.notEmpty(roomName, "parameter room number must not be empty");
        Validate.isTrue(StringUtils.isAlphanumeric(roomName.trim()), "room number must be alphanumeric");
        Validate.isTrue(roomName.length() <= 250, "length of room number must not exceed 250 chars");
        this.roomName = roomName.trim();
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        Validate.notNull(floor, "parameter floor to add must not be %s", null);
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

    public List<Task> getAssignedTasks() {
        return assignedTasks;
    }

    public void setAssignedTasks(List<Task> assignedTasks) {
        Validate.notNull(assignedTasks, "parameter assignedTasks to add must not be %s", null);
        this.assignedTasks = new ArrayList<>(assignedTasks);
    }

    public void addToAssignedTasks(Task task) {
        Validate.notNull(task, "parameter task to add must not be %s", null);
        if (!assignedTasks.contains(task)) {
            assignedTasks.add(task);
        }
    }

    public void removeFromAssignedTask(Task task) {
        Validate.notNull(task, "parameter task to add must not be %s", null);
        Validate.isTrue(assignedTasks.contains(task), "task to remove must already be added");
        assignedTasks.remove(task);
    }


    @Override
    public String toString() {
        ToStringBuilder toStringBuilder = new ToStringBuilder(this).
                append("id", getId()).
                append("room number", roomName).
                append("occupied", occupied).
                append("resident account", residentAccount).
                append("floor", floor).
                append("assigned tasks: ");
        for (Task task : HelperMethods.safe(assignedTasks)) {
            toStringBuilder.append(task.getTaskName());
        }
        return toStringBuilder.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof Room))
            return false;
        Room otherRoom = (Room) other;
        return new EqualsBuilder()
                .append(getId(), otherRoom.getId())
//                .append(roomName, otherRoom.roomName)
//                .append(occupied, otherRoom.occupied)
//                .append(residentAccount.getId(), otherRoom.residentAccount.getId())
//                .append(floor.getId(), otherRoom.floor.getId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getId())
                .append(floor.getId())
                .toHashCode();
    }
}
