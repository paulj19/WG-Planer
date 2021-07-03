package com.wg_planner.views.sub_menu.account_details;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.wg_planner.views.main.MainView;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

@Route(value = "account_details", layout = MainView.class)
@RouteAlias(value = "account_details", layout = MainView.class)
@PageTitle("Account Details")
@CssImport("./styles/views/tasks/tasks-view.css")
public class AccountDetailsView extends VerticalLayout {
    AutowireCapableBeanFactory beanFactory;
    AccountDetailsPresenter accountDetailsPresenter;

    public AccountDetailsView(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        accountDetailsPresenter = new AccountDetailsPresenter();
        beanFactory.autowireBean(accountDetailsPresenter);
        accountDetailsPresenter.init(this);
    }
}
