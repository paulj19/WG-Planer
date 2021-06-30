package com.wg_planner.views.utils.UINotificationHandler;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;

public interface UINotificationType {
    String getId();

    void createNotificationView(Room roomDeletingTask, Task taskDeleted);

    VerticalLayout getUILayout();
}
