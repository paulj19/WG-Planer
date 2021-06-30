package com.wg_planner.views.home_page;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.Push;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationType;
import com.wg_planner.views.utils.broadcaster.UIBroadcaster;

public class HomePagePresenter implements UIBroadcaster.BroadcastListener {
    HomePageView homePageView;

    public void init(HomePageView homePageView) {
        this.homePageView = homePageView;
        UIBroadcaster.register(this);
    }

    @Override
    public void receiveBroadcast(UINotificationType message) {
        homePageView.addNotification(message.getUILayout());
    }
}
