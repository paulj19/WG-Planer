package com.wg_planner.views.utils.UINotificationHandler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.backend.utils.HelperMethods;
import com.wg_planner.backend.utils.consensus.ConsensusListener;

@PreserveOnRefresh
@CssImport("./styles/views/home-page/notification/notification-layout.css")
public class UIEventTypeTaskRemind extends UIEventType {
//    private String notificationTemplate = "%s from room %s has reminded you about task %s which you are assigned that needs attention";
    private String notificationTemplate = "You have been reminded about task %s which you are assigned that needs attention.";
    private Task taskToRemind;
    private final long timeoutIntervalInMillis = 172800000; //7 days

    private UIEventTypeTaskRemind() {
    }

    public UIEventTypeTaskRemind(Room sourceRoom, Task taskToRemind) {
        this.sourceRoom = sourceRoom;
        this.taskToRemind = taskToRemind;
    }

    @Override
    public Component getUILayout(ConsensusListener consensusListener) {
        return createNotificationView(new Span(String.format(notificationTemplate,
//                HelperMethods.getFirstLetterUpperCase(sourceRoom.getResidentAccount().getFirstName()),
//                sourceRoom.getRoomName(),
                taskToRemind.getTaskName())), "task-remind-type");
    }

    @Override
    public long getTimeoutIntervalInMillis() {
        return timeoutIntervalInMillis;
    }
}
