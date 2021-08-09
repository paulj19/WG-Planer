package com.wg_planner.views.utils.UINotificationHandler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.backend.utils.HelperMethods;
import com.wg_planner.backend.utils.consensus.ConsensusListener;

@PreserveOnRefresh
@CssImport("./styles/views/notifications-page/notification-layout.css")
public class UIEventTypeTaskDelete extends UIEventType {
    private String notificationTemplate = "%s from room %s has deleted task %s.";
    public Task taskToDelete;
    private final long timeoutIntervalInMillis = 604800000; //7 days

    private UIEventTypeTaskDelete() {
    }

    public UIEventTypeTaskDelete(Room sourceRoom, Task taskToDelete) {
        this.sourceRoom = sourceRoom;
        this.taskToDelete = taskToDelete;
    }

    @Override
    public Object getEventRelatedObject() {
        return taskToDelete;
    }

    @Override
    public Component getUILayout(ConsensusListener consensusListener) {
        Div buttonLayout = new Div();
        buttonLayout.addClassName("notifications-page-button-layout");
        Button acceptButton = new Button("Accept");
        Button rejectButton = new Button("Reject");
        acceptButton.addClassName("notifications-page-button");
        rejectButton.addClassName("notifications-page-button");
        buttonLayout.add(acceptButton, rejectButton);
        acceptButton.addClickListener(event -> consensusListener.onAccept(taskToDelete.getId(), getId()));
        rejectButton.addClickListener(event -> consensusListener.onReject(taskToDelete.getId(), getId()));

        Span notificationMessage = new Span(String.format(notificationTemplate,
                HelperMethods.getFirstLetterUpperCase(sourceRoom.getResidentAccount().getFirstName()),
                sourceRoom.getRoomName(), taskToDelete.getTaskName()));
        notificationMessage.addClassName("notification-message");
        return createNotificationView(notificationMessage, "task-delete-type", buttonLayout);
    }

    @Override
    public long getTimeoutIntervalInMillis() {
        return timeoutIntervalInMillis;
    }
}
