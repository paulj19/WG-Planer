package com.wg_planner.views.utils.UINotificationHandler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.utils.consensus.ConsensusListener;

import java.util.UUID;

public abstract class UIEventType {
    private final String id = UUID.randomUUID().toString();
    public Room sourceRoom;

    public String getId() {
        return id;
    }

    public Room getSourceRoom() {
        return sourceRoom;
    }

    public abstract Component getUILayout(ConsensusListener consensusListener);

    public abstract long getTimeoutIntervalInMillis();

    protected Component createNotificationView(String notificationMessage, Component... components) {
        HorizontalLayout notificationLayout = new HorizontalLayout();
        Span message = new Span(notificationMessage);
        notificationLayout.add(message);
        notificationLayout.add(components);
        notificationLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        notificationLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        return new VerticalLayout(notificationLayout);
    }
}
