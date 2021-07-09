package com.wg_planner.views.utils.UINotificationHandler;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.utils.consensus.ConsensusListener;

@PreserveOnRefresh
public class UIEventTypeTaskRemind implements UIEventType{
    @Override
    public String getId() {
        return null;
    }

    @Override
    public Room getSourceRoom() {
        return null;
    }

    @Override
    public Component getUILayout(ConsensusListener consensusListener) {
        return null;
    }

    @Override
    public long getTimeoutInterval() {
        return 0;
    }
}
