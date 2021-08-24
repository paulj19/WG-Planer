package com.wg_planner.views.notifications_page;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.backend.utils.consensus.ConsensusHandler;
import com.wg_planner.backend.utils.consensus.ConsensusListener;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UINavigationHandler;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationHandler;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationType;
import com.wg_planner.views.utils.broadcaster.UIMessageBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class NotificationsPagePresenter implements UIMessageBus.BroadcastListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationsPagePresenter.class);
    private NotificationsPageView notificationsPageView;
    @Autowired
    FloorService floorService;
    private Room attachedRoom;
    private ConsensusListener consensusListener = new ConsensusListener() {
        @Override
        public synchronized void onAccept(Object objectForConsensus, String notificationId) {
            ConsensusHandler.getInstance().processAccept(objectForConsensus, attachedRoom);
            UINotificationHandler.getInstance().removeNotification(attachedRoom.getId(), notificationId);
            //todo something better than reload
            UINavigationHandler.getInstance().reloadPage();
        }

        @Override
        public synchronized void onReject(Object objectForConsensus, String notificationId) {
            ConsensusHandler.getInstance().processReject(objectForConsensus);
            UINotificationHandler.getInstance().removeAllNotificationObjectsInFloorOfNotification(notificationId,
                    floorService.getAllRoomsInFloorByFloorId(attachedRoom.getFloor().getId()));
            UINavigationHandler.getInstance().reloadPage();
        }
    };

    public void init(NotificationsPageView notificationsPageView) {
        this.notificationsPageView = notificationsPageView;
        attachedRoom = SessionHandler.getLoggedInResidentAccount().getRoom();//room residentAccount never get outdated
        notificationsPageView.getNotificationsUI().addAfterNavigationListener(event -> {
            notificationsPageView.removeAll();
            UINotificationHandler.getInstance().getAllNotificationsForRoom(attachedRoom).forEach(notification -> {
                LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Adding notification id {}", SessionHandler.getLoggedInResidentAccount().getId(),
                        notification.getId());
                notificationsPageView.addNotificationToView(notification.getUILayout(consensusListener));
            });
        });
        notificationsPageView.addAttachListener(event -> {
            UIMessageBus.register(this);
        });
    }

    @Override
    public void receiveBroadcast(UINotificationType uiNotification) {
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Received broadcast notification id.",
                SessionHandler.getLoggedInResidentAccount().getId(), uiNotification.getId());

        if (notificationsPageView != null && !uiNotification.getSourceRoom().equals(attachedRoom)) {
            LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Adding notification id", SessionHandler.getLoggedInResidentAccount().getId(),
                    uiNotification.getId());
            notificationsPageView.addNotificationToView(uiNotification.getUILayout(consensusListener));
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
