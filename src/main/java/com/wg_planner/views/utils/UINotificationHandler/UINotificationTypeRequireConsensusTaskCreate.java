package com.wg_planner.views.utils.UINotificationHandler;

import com.vaadin.flow.component.dependency.CssImport;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;

public class UINotificationTypeRequireConsensusTaskCreate extends UINotificationTypeRequireConsensus {
    private String notificationTemplate = "%s from room %s has created task %s.";
    public Task taskToCreate;

    private UINotificationTypeRequireConsensusTaskCreate() {
        super();
    }

    public UINotificationTypeRequireConsensusTaskCreate(Room sourceRoom, Task taskToCreate) {
        super(sourceRoom);
        this.taskToCreate = taskToCreate;
    }

    @Override
    public Object getEventRelatedObject() {
        return taskToCreate;
    }

    @Override
    public String getEventRelatedObjectName() {
        return taskToCreate.getTaskName();
    }

    @Override
    public String getNotificationTemplate() {
        return notificationTemplate;
    }
}
