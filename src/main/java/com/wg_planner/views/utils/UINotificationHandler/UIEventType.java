package com.wg_planner.views.utils.UINotificationHandler;

import com.vaadin.flow.component.Component;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.utils.consensus.ConsensusListener;

public interface UIEventType {
    String getId();

    Room getSourceRoom();
    //    Component createNotificationView();

    Component getUILayout(ConsensusListener consensusListener);

    long getTimeoutInterval();
}
