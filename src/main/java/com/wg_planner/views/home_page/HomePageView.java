package com.wg_planner.views.home_page;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.views.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

@Route(value = "home", layout = MainView.class)
@RouteAlias(value = "home", layout = MainView.class)
@PageTitle("Home")
@CssImport("./styles/views/tasks/tasks-view.css")
public class HomePageView extends VerticalLayout {
    AutowireCapableBeanFactory beanFactory;
    HomePagePresenter homePagePresenter;
    public HomePageView(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        homePagePresenter = new HomePagePresenter();
        beanFactory.autowireBean(homePagePresenter);
        homePagePresenter.init(this);
//        homePageLayout = new VerticalLayout();

    }
}
