package com.wg_planner.views.utils.UINotificationHandler;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;

import java.util.UUID;

public class UINotificationTypeTaskDelete implements UINotificationType {

    private String id = UUID.randomUUID().toString();
    private String notificationTemplate = "%s from room %s has deleted task %s";
    private Button undoTaskDeleteButton = new Button("Undo");
    private VerticalLayout layout = new VerticalLayout();

    public UINotificationTypeTaskDelete() {
    }

    public void createNotificationView(Room roomDeletingTask, Task taskDeleted) {
        layout.add(UINotificationViewCreator.createNotificationView(String.format(notificationTemplate,
                roomDeletingTask.getResidentAccount().getFirstName(),
                roomDeletingTask.getRoomName(), taskDeleted.getTaskName()), undoTaskDeleteButton));
    }

    public VerticalLayout getUILayout() {
        return layout;
    }

    @Override
    public String getId() {
        return id;
    }
}
