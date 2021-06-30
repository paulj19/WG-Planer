package com.wg_planner.views.home_page;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.wg_planner.views.main.MainView;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationType;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

@Route(value = "home_page", layout = MainView.class)
@RouteAlias(value = "home_page", layout = MainView.class)
@PageTitle("Home")
@CssImport("./styles/views/home-page/home-view.css")
public class HomePageView extends VerticalLayout {
    private AutowireCapableBeanFactory beanFactory;
    private HomePagePresenter homePagePresenter;

    public HomePageView(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        homePagePresenter = new HomePagePresenter();
        beanFactory.autowireBean(homePagePresenter);
        homePagePresenter.init(this);
    }

    void addNotification(Component notificationComponent) {
        getUI().ifPresent(ui -> ui.access(() -> add(notificationComponent)));
        UI.getCurrent().push();
    }
}
