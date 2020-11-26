package com.wg_planner.backend.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Room extends AbstractEntity implements Cloneable {

    @NotNull
    @NotEmpty
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
        this.roomNumber = roomNumber;
        this.floor = floor;
    }

    public Room(@NotNull @NotEmpty String roomNumber, @NotNull @NotEmpty Floor floor, ResidentAccount residentAccount) {
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.residentAccount = residentAccount;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

//    @Override
//    public String toString() {
//        return roomNumber;
//    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
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
        this.assignedTasks = assignedTasks;
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
