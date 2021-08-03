package com.wg_planner.views.sub_menu.account_details;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.wg_planner.views.main.MainView;
import com.wg_planner.views.utils.AccountDetailsHelper;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

@Route(value = "account_details", layout = MainView.class)
@RouteAlias(value = "account_details", layout = MainView.class)
@PageTitle("Account Details")
@CssImport("./styles/views/account-details/account-details-view.css")
public class AccountDetailsView extends VerticalLayout {
    AutowireCapableBeanFactory beanFactory;
    AccountDetailsPresenter accountDetailsPresenter;

    public AccountDetailsView(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        addClassName("account-details-layout-view");
        accountDetailsPresenter = new AccountDetailsPresenter();
        beanFactory.autowireBean(accountDetailsPresenter);
        accountDetailsPresenter.init(this);
    }

    public void addLogoutButton() {
        Button logoutButton = new Button("Logout");
        logoutButton.getStyle().set("margin", "0 1em");
        logoutButton.addClickListener(event -> AccountDetailsHelper.logoutAndNavigateToLoginPage());
        add(logoutButton);
    }
}
