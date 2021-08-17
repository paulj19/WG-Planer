package com.wg_planner.views.utils.UINotificationHandler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.wg_planner.backend.entity.AbstractEntity;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.backend.utils.HelperMethods;
import com.wg_planner.backend.utils.consensus.ConsensusListener;
import com.wg_planner.backend.utils.consensus.ConsensusObject;

@CssImport("./styles/views/notifications-page/notification-layout.css")
public abstract class UINotificationTypeRequireConsensus extends UINotificationType {
    private final long timeoutIntervalInMillis = 604800000; //7 days

    public UINotificationTypeRequireConsensus(Room sourceRoom) {
        this.sourceRoom = sourceRoom;
    }

    public UINotificationTypeRequireConsensus() {
    }

    abstract public Object getEventRelatedObject();

    abstract public String getEventRelatedObjectName();

    abstract public String getNotificationTemplate();

    @Override
    public Component getUILayout(ConsensusListener consensusListener) {
        Div buttonLayout = new Div();
        buttonLayout.addClassName("notifications-page-button-layout");
        Button acceptButton = new Button("Accept");
        Button rejectButton = new Button("Reject");
        acceptButton.addClassName("notifications-page-button");
        rejectButton.addClassName("notifications-page-button");
        buttonLayout.add(acceptButton, rejectButton);
        //todo fix me: there is no guarentee that a consensus object will be an AbstractEntity
        acceptButton.addClickListener(event -> consensusListener.onAccept(((AbstractEntity) getEventRelatedObject()).getId(), getId()));
        rejectButton.addClickListener(event -> consensusListener.onReject(((AbstractEntity) getEventRelatedObject()).getId(), getId()));

        Span notificationMessage = new Span(String.format(getNotificationTemplate(),
                HelperMethods.getFirstLetterUpperCase(sourceRoom.getResidentAccount().getFirstName()),
                sourceRoom.getRoomName(), getEventRelatedObjectName()));
        notificationMessage.addClassName("notification-message");
        return createNotificationView(notificationMessage, "consensus-type", buttonLayout);
    }

    @Override
    public long getTimeoutIntervalInMillis() {
        return timeoutIntervalInMillis;
    }
}
