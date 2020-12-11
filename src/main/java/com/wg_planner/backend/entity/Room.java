package com.wg_planner.backend.entity;

import com.wg_planner.backend.helpers.ValidationChecks;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;

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
    private String roomNumber;

    private Boolean occupied = false;

    //owning side, referencing side
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "room", cascade = CascadeType.MERGE)
    private ResidentAccount residentAccount;

    @NotNull
    @NotEmpty
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;

    @OneToMany(mappedBy = "assignedRoom", fetch = FetchType.EAGER)
//    @LazyCollection(LazyCollectionOption.FALSE)
    @Fetch(value = FetchMode.SUBSELECT)
    List<Task> assignedTasks;


    public Room() {
    }

    public Room(@NotNull @NotEmpty String roomNumber, @NotNull @NotEmpty Floor floor) {
        setRoomNumber(roomNumber);
        setFloor(floor);
    }

//    public Room(@NotNull @NotEmpty String roomNumber, @NotNull @NotEmpty Floor floor, ResidentAccount residentAccount) {
//        this(roomNumber, floor);
//        setResidentAccount(residentAccount);
//    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        Validate.notNull(roomNumber, "parameter roomNumber to add must not be %s", null);
        Validate.notEmpty(roomNumber, "parameter room number must not be empty");
        Validate.isTrue(StringUtils.isAlphanumeric(roomNumber), "room number must be alphanumeric");
        Validate.isTrue(roomNumber.length() <= 250, "length of room number must not exceed 250 chars");
        this.roomNumber = roomNumber;
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
        Validate.notNull(residentAccount, "parameter residentAccount to add must not be %s", null);

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

    public void addAssignedTasks(Task task) {
        Validate.notNull(task, "parameter task to add must not be %s", null);
        Validate.isTrue(!assignedTasks.contains(task), "task to add must not already be added");
        assignedTasks.add(task);
    }


    @Override
    public String toString() {
        ToStringBuilder toStringBuilder = new ToStringBuilder(this).
                append("room number", roomNumber).
                append("occupied", occupied).
                append("resident account", residentAccount).
                append("floor", floor).
                append("assigned tasks: ");
        for (Task task : assignedTasks) {
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
                .append(roomNumber, otherRoom.roomNumber)
                .append(occupied, otherRoom.occupied)
                .append(residentAccount.getId(), otherRoom.residentAccount.getId())
                .append(floor.getId(), otherRoom.floor.getId())
                .append(assignedTasks, otherRoom.assignedTasks)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(roomNumber)
                .append(occupied)
                .append(residentAccount)
                .append(floor)
                .append(assignedTasks)
                .toHashCode();
    }
}
