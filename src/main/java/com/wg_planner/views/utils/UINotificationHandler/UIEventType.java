package com.wg_planner.views.utils.UINotificationHandler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
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

    public abstract Object getEventRelatedObject();

    public abstract Component getUILayout(ConsensusListener consensusListener);

    public abstract long getTimeoutIntervalInMillis();

    protected Component createNotificationView(Component notificationMessage, String styleClass, Component... components) {
        HorizontalLayout notificationLayout = new HorizontalLayout();
        notificationLayout.addClassName("notification-layout-view");
        if (!styleClass.isEmpty()) {
            notificationLayout.addClassName(styleClass);
        }
        notificationLayout.add(notificationMessage);
        notificationLayout.add(components);
        //        notificationLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        return notificationLayout;
    }
}
