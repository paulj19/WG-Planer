package com.wg_planner.views.home_page;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import com.wg_planner.views.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import static com.vaadin.flow.component.notification.Notification.Position.BOTTOM_STRETCH;

//@Push
@VaadinSessionScope
@Route(value = "home_page", layout = MainView.class)
@RouteAlias(value = "home_page", layout = MainView.class)
@PageTitle("Home")
@CssImport("./styles/views/home-page/home-view.css")
public class HomePageView extends VerticalLayout {
    private AutowireCapableBeanFactory beanFactory;
    private HomePagePresenter homePagePresenter;

    @Autowired
    public HomePageView(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        homePagePresenter = new HomePagePresenter();
        beanFactory.autowireBean(homePagePresenter);
        homePagePresenter.init(this);
    }

    void addNotificationToView(Component notificationComponent) {
        getUI().ifPresent(ui -> ui.access(() -> add(notificationComponent)));
    }

    public UI getHomeUI() {
        return UI.getCurrent();
    }

    void notify(String notificationMessage) {
        Notification.show(notificationMessage, 10000, BOTTOM_STRETCH);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        homePagePresenter.onDetachUI();
    }
}
