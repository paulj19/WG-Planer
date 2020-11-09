package com.wg_planner.views.signup;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "register")
@PageTitle("Register | WG Planner")
@CssImport("./styles/views/register/register-view.css")
public class RegisterView extends VerticalLayout implements BeforeEnterObserver {

    private RegisterForm registerForm = new RegisterForm();

    public RegisterView() {
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(new H1("Register"), registerForm);

    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
//            loginForm.setError(true);
        }
    }
}
