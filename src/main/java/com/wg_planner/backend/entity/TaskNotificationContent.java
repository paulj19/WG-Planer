package com.wg_planner.backend.entity;

import org.apache.commons.lang3.Validate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "task_notification_content")
public class TaskNotificationContent extends AbstractEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task correspondingTask;

    @NotNull
    private String title;

    @NotNull
    private String body;

    public TaskNotificationContent() {
    }

    public TaskNotificationContent(Task correspondingTask, @NotNull String title, @NotNull String body) {
        setCorrespondingTask(correspondingTask);
        setTitle(title);
        setBody(body);
    }

    public Task getCorrespondingTask() {
        return correspondingTask;
    }

    public void setCorrespondingTask(Task correspondingTask) {
        Validate.notNull(correspondingTask, "parameter correspondingTask to add must not be %s", null);
        this.correspondingTask = correspondingTask;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Validate.notNull(title, "parameter title to add must not be %s", null);
        Validate.notEmpty(title, "parameter title must not be empty");
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        Validate.notNull(body, "parameter body to add must not be %s", null);
        Validate.notEmpty(body, "parameter body must not be empty");
        this.body = body;
    }
}
