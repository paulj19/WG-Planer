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
        if (other == this)
            return true;
        if (!(other instanceof Task))
            return false;
        Task otherTask = (Task) other;
        return new EqualsBuilder()
                .append(taskName, otherTask.taskName)
                .append(floor, otherTask.floor)
                .append(assignedRoom, otherTask.assignedRoom)
                .isEquals();
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
