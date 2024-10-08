package com.wg_planner.backend.entity;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "task")
//todo composite f.k(floor_id, room_id)
public class Task extends AbstractEntity {

    @NotNull
    @NotEmpty
    @Size(max = 24)
    private String taskName;

    //todo floor does not track track anything of the task except the list of tasks in the floor
    //hence unnecessary to save or refresh floor tasks when floor is saved
    //and also nothing really is done by taking floor from task but other way around
    //UPDATE tasks sometimes are saved alone(creation, updation etc)
    // and change need to reflect on floor BUT change seen on floor without cascading
    @NotNull
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;


    //todo BUT change seen on room without cascading and all changes to task reflect on "both" rooms
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room assignedRoom;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "correspondingTask", cascade = CascadeType.ALL)
    TaskNotificationContent taskNotificationContent;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "correspondingTask", cascade = CascadeType.ALL)
    TaskNotificationContentTaskAssign taskNotificationContentTaskAssign;

    public Task() {
    }

    public Task(@NotNull @NotEmpty String taskName, @NotNull @NotEmpty Floor floor) {
        setTaskName(taskName);
        setFloor(floor);
    }

    public Task(@NotNull @NotEmpty String taskName, @NotNull @NotEmpty Floor floor, Room assignedRoom) {
        this(taskName, floor);
        setAssignedRoom(assignedRoom);
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        Validate.notNull(taskName, "parameter taskName must not be %s", (Object) null);
        Validate.notEmpty(taskName, "parameter taskName must not be empty");
        Validate.isTrue(taskName.length() <= 250, "length of task name must not exceed 250 chars");
        this.taskName = taskName.trim();
        createTaskNotificationContent(taskName.trim());
        createTaskNotificationContentTaskAssign(taskName.trim());
    }

    private void createTaskNotificationContent(String taskName) {
        this.taskNotificationContent = new TaskNotificationContent(this, taskName + " Reminder");
    }

    private void createTaskNotificationContentTaskAssign(String taskName) {
        this.taskNotificationContentTaskAssign = new TaskNotificationContentTaskAssign(this, "Task" + taskName + " assigned to you!");
    }

    public Room getAssignedRoom() {
        return assignedRoom;
    }

    public void setAssignedRoom(Room assignedRoom) {
//        Validate.notNull(assignedRoom, "parameter assignedRoom must not be %s", (Object) null);
        this.assignedRoom = assignedRoom;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        Validate.notNull(floor, "parameter floor must not be %s", (Object) null);
        this.floor = floor;
    }

    public TaskNotificationContent getTaskNotificationContent() {
        return taskNotificationContent;
    }

    public void setTaskNotificationContent(TaskNotificationContent taskNotificationContent) {
        Validate.notNull(taskNotificationContent, "parameter taskNotificationContent must not be %s", (Object) null);
        this.taskNotificationContent = taskNotificationContent;
    }

    public TaskNotificationContentTaskAssign getTaskNotificationContentTaskAssign() {
        return taskNotificationContentTaskAssign;
    }

    public void setTaskNotificationContentTaskAssign(TaskNotificationContentTaskAssign taskNotificationContentTaskAssign) {
        Validate.notNull(taskNotificationContentTaskAssign, "parameter taskNotificationContent must not be %s", (Object) null);
        this.taskNotificationContentTaskAssign = taskNotificationContentTaskAssign;
    }

    public String toString() {
        return new ToStringBuilder(this).
                append("id", getId()).
                append("task name", taskName).
                append("floor id", floor.getId()).
//                append("assigned room id", assignedRoom.getId()).
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
                .append(getId(), otherTask.getId());
        return equalsBuilder.isEquals();
//                .append(taskName, otherTask.taskName);
//        if (floor != null && otherTask.floor != null)
//            equalsBuilder.append(floor.getId(), otherTask.floor.getId());
//        else if (floor == null && otherTask.floor == null)
//            isEqual = isEqual & true;
//        else
//            isEqual = isEqual & false;
//        if (assignedRoom != null && otherTask.assignedRoom != null)
//            equalsBuilder.append(assignedRoom.getId(), otherTask.assignedRoom.getId());
//        else if (assignedRoom == null && otherTask.assignedRoom == null)
//            isEqual = isEqual & true;
//        else
//            isEqual = isEqual & false;
//
//        return equalsBuilder.isEquals() && isEqual;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getId())
                .append(floor.getId())
                .toHashCode();
    }

}
