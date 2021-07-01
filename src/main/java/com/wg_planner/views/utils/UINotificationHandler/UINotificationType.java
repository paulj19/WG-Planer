package com.wg_planner.views.utils.UINotificationHandler;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public interface UINotificationType {
    String getId();

//    Component createNotificationView();

    VerticalLayout getUILayout();
}
