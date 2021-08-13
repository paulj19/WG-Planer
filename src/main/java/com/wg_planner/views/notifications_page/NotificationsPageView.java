package com.wg_planner.views.notifications_page;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.main.MainView;
import com.wg_planner.views.utils.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

@VaadinSessionScope
@Route(value = "notifications", layout = MainView.class)
@PageTitle("Notifications")
@CssImport("./styles/views/notifications-page/notifications-view.css")
public class NotificationsPageView extends VerticalLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationsPageView.class);
    private AutowireCapableBeanFactory beanFactory;
    private NotificationsPagePresenter notificationsPagePresenter;

    @Autowired
    public NotificationsPageView(AutowireCapableBeanFactory beanFactory) {
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Notifications page view selected.", SessionHandler.getLoggedInResidentAccount().getId());
        this.beanFactory = beanFactory;
        addClassName("uinotification-page-layout");
        notificationsPagePresenter = new NotificationsPagePresenter();
        beanFactory.autowireBean(notificationsPagePresenter);
        notificationsPagePresenter.init(this);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    void addNotificationToView(Component notificationComponent) {
        getUI().ifPresent(ui -> ui.access(() -> {
//            removeAll();
            add(notificationComponent);
        }));
    }

    public UI getNotificationsUI() {
        return UI.getCurrent();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        notificationsPagePresenter.onDetachUI();
    }
}
