package com.wg_planner.views.utils.UINotificationHandler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.backend.utils.consensus.ConsensusListener;

@PreserveOnRefresh
public class UIEventTypeTaskRemind extends UIEventType {
    private String notificationTemplate = "%s from room %s has reminded you about task %s which you are assigned that needs attention";
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
        return createNotificationView(String.format(notificationTemplate,
                sourceRoom.getResidentAccount().getFirstName(),
                sourceRoom.getRoomName(), taskToRemind.getTaskName()));
    }

    @Override
    public long getTimeoutIntervalInMillis() {
        return timeoutIntervalInMillis;
    }
}
