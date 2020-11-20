package com.wg_planner.backend.entity;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id")
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
}
