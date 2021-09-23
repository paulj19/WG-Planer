package com.wg_planner.views.UnauthorizedPages;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.wg_planner.views.utils.UINavigationHandler;

public class UnauthorizedPagesViewWithLogin extends UnauthorizedPagesView{
    public UnauthorizedPagesViewWithLogin() {
        super();
        logoLayout.add(getLoginButton());
    }
    private Component getLoginButton() {
        Button loginButton = new Button("Login");
        loginButton.getStyle().set("width", "150px");
        loginButton.addClickListener(event -> UINavigationHandler.getInstance().navigateToLoginPage());
        return loginButton;
    }
}
