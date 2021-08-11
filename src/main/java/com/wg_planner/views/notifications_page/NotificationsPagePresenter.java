package com.wg_planner.views.notifications_page;

import com.vaadin.flow.component.UI;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.utils.consensus.ConsensusHandler;
import com.wg_planner.backend.utils.consensus.ConsensusListener;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UINotificationHandler.UIEventHandler;
import com.wg_planner.views.utils.UINotificationHandler.UIEventType;
import com.wg_planner.views.utils.broadcaster.UIMessageBus;
import org.springframework.beans.factory.annotation.Autowired;

public class NotificationsPagePresenter implements UIMessageBus.BroadcastListener {
    private NotificationsPageView NotificationsPageView;
    @Autowired
    FloorService floorService;
    private Room attachedRoom;
    private ConsensusListener consensusListener = new ConsensusListener() {
        @Override
        public synchronized void onAccept(Long consensusObjectId, String notificationId) {
            ConsensusHandler.getInstance().processAccept(consensusObjectId, attachedRoom);
            UIEventHandler.getInstance().removeNotification(attachedRoom.getId(), notificationId);
            //todo something better than reload
            UI.getCurrent().getPage().reload();
        }

        @Override
        public synchronized void onReject(Long consensusObjectId, String notificationId) {
            ConsensusHandler.getInstance().processReject(consensusObjectId);
            UIEventHandler.getInstance().removeAllNotificationObjectsInFloorOfNotification(notificationId,
                    floorService.getAllRoomsInFloorByFloorId(attachedRoom.getFloor().getId()));
            UI.getCurrent().getPage().reload();
        }
    };

    public void init(NotificationsPageView NotificationsPageView) {
        this.NotificationsPageView = NotificationsPageView;
        attachedRoom = SessionHandler.getLoggedInResidentAccount().getRoom();//room residentAccount never get outdated
        NotificationsPageView.getNotificationsUI().addAfterNavigationListener(event -> UIEventHandler.getInstance().getAllNotificationsForRoom(attachedRoom).forEach(notification -> NotificationsPageView.addNotificationToView(notification.getUILayout(consensusListener))));
        NotificationsPageView.addAttachListener(event -> {
            UIMessageBus.register(this);
        });
    }

    @Override
    public void receiveBroadcast(UIEventType uiNotification) {
        if (NotificationsPageView != null && !uiNotification.getSourceRoom().equals(attachedRoom)) {
            NotificationsPageView.addNotificationToView(uiNotification.getUILayout(consensusListener));
        }
    }

    @Override
    public Room getCorrespondingRoom() {
        return attachedRoom;
    }

    public void onDetachUI() {
        UIMessageBus.unregister(this);
    }
}
