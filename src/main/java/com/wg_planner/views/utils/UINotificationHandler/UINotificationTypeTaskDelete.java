package com.wg_planner.views.utils.UINotificationHandler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.backend.utils.consensus.ConsensusListener;

import java.util.UUID;

@PreserveOnRefresh
public class UINotificationTypeTaskDelete implements UINotificationType {

    private String id = UUID.randomUUID().toString();
    private String notificationTemplate = "%s from room %s has deleted task %s";
    public Room sourceRoom;
    public Task taskToDelete;

    private UINotificationTypeTaskDelete() {
    }

    public UINotificationTypeTaskDelete(Room sourceRoom, Task taskToDelete) {
        this.sourceRoom = sourceRoom;
        this.taskToDelete = taskToDelete;
//        createNotificationView();
    }

//    private void createNotificationView() {
//        layout.add(UINotificationViewCreator.createNotificationView(String.format(notificationTemplate,
//                sourceRoom.getResidentAccount().getFirstName(),
//                sourceRoom.getRoomName(), taskToDelete.getTaskName()), undoTaskDeleteButton));
//    }

    @Override
    public Component getUILayout(ConsensusListener consensusListener) {
        Button acceptButton= new Button("Accept");
        Button rejectButton= new Button("Reject");
        acceptButton.addClickListener(event -> consensusListener.onAccept(taskToDelete.getId(), id));
        rejectButton.addClickListener(event -> consensusListener.onReject(taskToDelete.getId(), id));
        return createNotificationView(String.format(notificationTemplate,
                sourceRoom.getResidentAccount().getFirstName(),
                sourceRoom.getRoomName(), taskToDelete.getTaskName()), acceptButton, rejectButton);
    }

    private Component createNotificationView(String notificationMessage, Component... components) {
        HorizontalLayout notificationLayout = new HorizontalLayout();
        Span message = new Span(notificationMessage);
        notificationLayout.add(message);
        notificationLayout.add(components);
        notificationLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        notificationLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        return new VerticalLayout(notificationLayout);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Room getSourceRoom() {
        return sourceRoom;
    }
}
