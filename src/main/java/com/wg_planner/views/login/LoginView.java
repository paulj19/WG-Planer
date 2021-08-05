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
@PageTitle("login | WG Planner")
@JsModule("/js/loggedInResidentAccountIdHandler.js")

public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private LoginForm loginForm = new LoginForm();

    public LoginView() {
        addClassName("login-view");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        Anchor register = new Anchor("register", "Sign Up");
        loginForm.setAction("login");
        Anchor create_floor = new Anchor("create_floor", "Create Floor");
//        add(MainView.getAppNameHeader(), loginForm, register, create_floor);
        H1 header = new H1("WG Planner");
        header.getStyle().set("color", "hsl(214, 90%, 52%)");
        header.getStyle().set("text-shadow", "2px 2px 0 rgba(0, 0, 0, 0.2)");

        add(header, loginForm, register, create_floor);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginForm.setError(true);
        }
    }
}
