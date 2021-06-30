package com.wg_planner.views.utils.UINotificationHandler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class UINotificationViewCreator {
    public static Component createNotificationView(String notificationMessage, Component... components) {
        Span message = new Span(notificationMessage);
        HorizontalLayout notificationLayout = new HorizontalLayout();
        notificationLayout.add(message);
        notificationLayout.add(components);
        return notificationLayout;
    }
}
