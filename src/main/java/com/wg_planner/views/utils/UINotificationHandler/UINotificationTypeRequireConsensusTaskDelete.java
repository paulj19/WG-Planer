package com.wg_planner.views.utils.UINotificationHandler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.backend.utils.HelperMethods;
import com.wg_planner.backend.utils.consensus.ConsensusListener;

public class UINotificationTypeRequireConsensusTaskDelete extends UINotificationTypeRequireConsensus {
    private String notificationTemplate = "%s from room %s has deleted task %s.";
    public Task taskToDelete;

    private UINotificationTypeRequireConsensusTaskDelete() {
        super();
    }

    public UINotificationTypeRequireConsensusTaskDelete(Room sourceRoom, Task taskToDelete) {
        super(sourceRoom);
        this.taskToDelete = taskToDelete;
    }

    @Override
    public Object getEventRelatedObject() {
        return taskToDelete;
    }

    @Override
    public String getEventRelatedObjectName() {
        return taskToDelete.getTaskName();
    }

    @Override
    public String getNotificationTemplate() {
        return notificationTemplate;
    }
}
