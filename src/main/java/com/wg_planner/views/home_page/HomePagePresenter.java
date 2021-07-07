package com.wg_planner.views.home_page;

import com.vaadin.flow.component.UI;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.utils.consensus.ConsensusHandler;
import com.wg_planner.backend.utils.consensus.ConsensusListener;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationHandler;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationType;
import com.wg_planner.views.utils.broadcaster.UIBroadcaster;
import org.springframework.beans.factory.annotation.Autowired;

public class HomePagePresenter implements UIBroadcaster.BroadcastListener {
    private HomePageView homePageView;
    @Autowired
    private UINotificationHandler uiNotificationHandler;
    private Room attachedRoom;
    private ConsensusListener consensusListener = new ConsensusListener() {
        @Override
        public void onAccept(Long consensusObjectId, String notificationId) {
            ConsensusHandler.processAccept(consensusObjectId, attachedRoom);
            uiNotificationHandler.removeNotification(attachedRoom.getId(), notificationId);
            //todo something better than reload
            UI.getCurrent().getPage().reload();
        }

        @Override
        public void onReject(Long consensusObjectId) {
            ConsensusHandler.processReject(consensusObjectId, attachedRoom);

        }
    };

    public void init(HomePageView homePageView) {
        this.homePageView = homePageView;
        attachedRoom = SessionHandler.getLoggedInResidentAccount().getRoom();
//        uiNotificationHandler.getAllNotificationsForRoom(attachedRoom).forEach(notification -> homePageView
//        .addNotificationToView(notification.getUILayout(consensusListener)));
        homePageView.getHomeUI().addAfterNavigationListener(event -> uiNotificationHandler.getAllNotificationsForRoom(attachedRoom).forEach(notification -> homePageView.addNotificationToView(notification.getUILayout(consensusListener))));
        homePageView.addAttachListener(event -> {
            UIBroadcaster.register(this);
        });
    }

    @Override
    public void receiveBroadcast(UINotificationType uiNotification) {
        if (homePageView != null && !uiNotification.getSourceRoom().equals(attachedRoom)) {
            homePageView.addNotificationToView(uiNotification.getUILayout(consensusListener));
        }
    }

    @Override
    public Room getCorrespondingRoom() {
        return attachedRoom;
    }

    public void onDetachUI() {
        UIBroadcaster.unregister(this);
    }
}
