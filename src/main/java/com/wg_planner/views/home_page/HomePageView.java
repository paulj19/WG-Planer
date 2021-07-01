package com.wg_planner.views.home_page;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.wg_planner.views.main.MainView;
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

    //    @Override
//    public void receiveBroadcast(UINotificationType message) {
////        addNotificationToView(message.createNotificationView());
//        getUI().ifPresent(ui -> ui.access(() -> add(message.createNotificationView())));
//        UI.getCurrent().push();
//    }
    void addNotificationToView(Component notificationComponent) {
        add(notificationComponent);
//        getUI().ifPresent(ui -> ui.access(() -> add(notificationComponent)));
        UI.getCurrent().push();
    }
}
