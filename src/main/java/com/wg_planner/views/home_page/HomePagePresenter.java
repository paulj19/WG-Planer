package com.wg_planner.views.home_page;

import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationHandler;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationType;
import com.wg_planner.views.utils.broadcaster.UIBroadcaster;
import org.springframework.beans.factory.annotation.Autowired;

public class HomePagePresenter implements UIBroadcaster.BroadcastListener {
    HomePageView homePageView;
    @Autowired
    UINotificationHandler uiNotificationHandler;


    public void init(HomePageView homePageView) {
        this.homePageView = homePageView;
        UIBroadcaster.register(this);
        uiNotificationHandler.getAllNotificationsForRoom(SessionHandler.getLoggedInResidentAccount().getRoom()).forEach(notification -> homePageView.addNotificationToView(notification.getUILayout()));
    }

    @Override
    public void receiveBroadcast(UINotificationType message) {
        if (homePageView != null){
            homePageView.getUI().ifPresent(ui -> ui.access(() -> homePageView.addNotificationToView(message.getUILayout())));
        }
    }
}
