package com.wg_planner.views.home_page;

import com.wg_planner.backend.entity.Room;
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

    public void init(HomePageView homePageView) {
        this.homePageView = homePageView;
        attachedRoom = SessionHandler.getLoggedInResidentAccount().getRoom();
        uiNotificationHandler.getAllNotificationsForRoom(attachedRoom).forEach(notification -> homePageView.addNotificationToView(notification.getUILayout()));
        homePageView.getHomeUI().addAfterNavigationListener(event -> uiNotificationHandler.getAllNotificationsForRoom(attachedRoom).forEach(notification -> homePageView.addNotificationToView(notification.getUILayout())));
        homePageView.addAttachListener(event -> {
            UIBroadcaster.register(this);
        });
    }

    @Override
    public void receiveBroadcast(UINotificationType uiNotification) {
        if (homePageView != null && !uiNotification.getSourceRoom().equals(attachedRoom)) {
            homePageView.addNotificationToView(uiNotification.getUILayout());
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
