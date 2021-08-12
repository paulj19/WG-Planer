package com.wg_planner.views.main;

import com.wg_planner.views.utils.LoggedInResidentIdHandler;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UINotificationHandler.UIEventHandler;
import com.wg_planner.views.utils.UINotificationHandler.UIEventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MainViewPresenter {
    @Autowired
    LoggedInResidentIdHandler loggedInResidentIdHandler;

    public void init() {
        loggedInResidentIdHandler.sentResidentAccountId();
    }

    int getNumberOfNotificationsOfResident() {
        List<UIEventType> residentNotifications =
                UIEventHandler.getInstance().getAllNotificationsForRoom(SessionHandler.getLoggedInResidentAccount().getRoom());
        return residentNotifications == null ? 0 : residentNotifications.size();
    }
}
