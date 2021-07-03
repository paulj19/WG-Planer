package com.wg_planner.views.utils.UINotificationHandler;

import com.vaadin.flow.component.Component;
import com.wg_planner.backend.entity.Room;

public interface UINotificationType {
    String getId();
    Room getSourceRoom();
//    Component createNotificationView();

    Component getUILayout();
}
