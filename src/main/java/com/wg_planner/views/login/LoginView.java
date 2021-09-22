package com.wg_planner.views.login;

import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("login")
@PageTitle("login | WG Planer")
@JsModule("/js/loggedInResidentAccountIdHandler.js")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private LoginForm loginForm = new LoginForm();

    public LoginView() {
        addClassName("login-view");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        loginForm.setAction("login");
//        add(MainView.getAppNameHeader(), loginForm, register, create_floor);
        H1 header = new H1("WG Planer");
        header.getStyle().set("color", "#000000)");
        header.getStyle().set("text-shadow", "1px 1px 0 rgba(0, 0, 0, 0.2)");

        add(header, loginForm);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginForm.setError(true);
        }
    }
}
