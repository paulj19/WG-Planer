package com.wg_planner.views.utils.UINotificationHandler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.backend.utils.consensus.ConsensusListener;

@PreserveOnRefresh
public class UIEventTypeTaskDelete extends UIEventType {
    private String notificationTemplate = "%s from room %s has deleted task %s";
    public Task taskToDelete;
    private final long timeoutIntervalInMillis = 604800000; //7 days

    private UIEventTypeTaskDelete() {
    }

    public UIEventTypeTaskDelete(Room sourceRoom, Task taskToDelete) {
        this.sourceRoom = sourceRoom;
        this.taskToDelete = taskToDelete;
    }

    @Override
    public Component getUILayout(ConsensusListener consensusListener) {
        Button acceptButton = new Button("Accept");
        Button rejectButton = new Button("Reject");
        acceptButton.addClickListener(event -> consensusListener.onAccept(taskToDelete.getId(), getId()));
        rejectButton.addClickListener(event -> consensusListener.onReject(taskToDelete.getId(), getId()));
        return createNotificationView(String.format(notificationTemplate,
                sourceRoom.getResidentAccount().getFirstName(),
                sourceRoom.getRoomName(), taskToDelete.getTaskName()), acceptButton, rejectButton);
    }

    @Override
    public long getTimeoutIntervalInMillis() {
        return timeoutIntervalInMillis;
    }
}
