package com.wg_planner.views.utils.UINotificationHandler;

import com.vaadin.flow.component.Component;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UINotificationHandler {
    //sync in function calling saveNotification
    private UINotificationStore uiNotificationStore;

    @Autowired
    public UINotificationHandler(UINotificationStore uiNotificationStore) {
        this.uiNotificationStore = uiNotificationStore;
    }

    public synchronized Component createAndSaveUINotification(UINotificationType uiNotificationType, Room roomDeletingTask,
                                                              Task taskDeleted) {
        uiNotificationType.createNotificationView(roomDeletingTask, taskDeleted);
        uiNotificationStore.saveNotification(roomDeletingTask.getId(), uiNotificationType);
        return uiNotificationType.getUILayout();
    }
}
