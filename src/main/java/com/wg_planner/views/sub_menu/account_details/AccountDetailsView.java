package com.wg_planner.views.sub_menu.account_details;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.main.MainView;
import com.wg_planner.views.utils.AccountDetailsHelper;
import com.wg_planner.views.utils.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

@Route(layout = MainView.class)
@PageTitle("Account Details")
@CssImport("./styles/views/account-details/account-details-view.css")
public class AccountDetailsView extends VerticalLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountDetailsView.class);
    AutowireCapableBeanFactory beanFactory;
    AccountDetailsPresenter accountDetailsPresenter;

    public AccountDetailsView(AutowireCapableBeanFactory beanFactory) {
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Account Details page view selected.",
                SessionHandler.getLoggedInResidentAccount().getId());
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
