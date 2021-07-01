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
    public Room sourceRoom;
    public Task taskToDelete;

    private UINotificationTypeTaskDelete() {
    }

    public UINotificationTypeTaskDelete(Room sourceRoom, Task taskToDelete) {
        this.sourceRoom = sourceRoom;
        this.taskToDelete = taskToDelete;
        createNotificationView();
    }

    private void createNotificationView() {
        layout.add(UINotificationViewCreator.createNotificationView(String.format(notificationTemplate,
                sourceRoom.getResidentAccount().getFirstName(),
                sourceRoom.getRoomName(), taskToDelete.getTaskName()), undoTaskDeleteButton));
    }

    public VerticalLayout getUILayout() {
        return layout;
    }

    @Override
    public String getId() {
        return id;
    }
}
