package com.wg_planner.backend.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "task")
//todo composite f.k(floor_id, room_id)
public class Task extends AbstractEntity {

    @NotNull
    @NotEmpty
    private String taskName;

    @NotNull
    @NotEmpty
    @ManyToOne//lazy not working (fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;

    @ManyToOne//lazy not working
    @JoinColumn(name = "room_id")
    private Room assignedRoom;

    public Task() {

    }

    public Task(@NotNull @NotEmpty String taskName, @NotNull @NotEmpty Floor floor) {
        this.taskName = taskName;
        this.floor = floor;
    }

    public Task(@NotNull @NotEmpty String taskName, @NotNull @NotEmpty Floor floor, Room assignedRoom) {
        this(taskName, floor);
        this.assignedRoom = assignedRoom;
    }


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Room getAssignedRoom() {
        return assignedRoom;
    }

    public void setAssignedRoom(Room assignedRoom) {
        this.assignedRoom = assignedRoom;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public String toString() {
        return new ToStringBuilder(this).
                append("task name", taskName).
                append("floor", floor).
                append("assigned room", assignedRoom).
                toString();
    }

    @Override
    public boolean equals(Object other) {
        //object taken from the database is the same as the created one
        boolean isEqual = true;
        if (other == this)
            return true;
        if (!(other instanceof Task))
            return false;
        Task otherTask = (Task) other;
        EqualsBuilder equalsBuilder = new EqualsBuilder()
                .append(taskName, otherTask.taskName);
        if (floor != null && otherTask.floor != null)
            equalsBuilder.append(floor.getId(), otherTask.floor.getId());
        else if (floor == null && otherTask.floor == null)
            isEqual = isEqual & true;
        else
            isEqual = isEqual & false;
        if (assignedRoom != null && otherTask.assignedRoom != null)
            equalsBuilder.append(assignedRoom.getId(), otherTask.assignedRoom.getId());
        else if (assignedRoom == null && otherTask.assignedRoom == null)
            isEqual = isEqual & true;
        else
            isEqual = isEqual & false;

        return equalsBuilder.isEquals() && isEqual;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(taskName)
                .append(floor)
                .append(assignedRoom)
                .toHashCode();
    }

}
