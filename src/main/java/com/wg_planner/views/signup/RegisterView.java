package com.wg_planner.views.signup;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.Service.RoomService;
import com.wg_planner.backend.entity.ResidentAccount;

@Route(value = "register")
@PageTitle("Register | WG Planner")
@CssImport("./styles/views/register/register-view.css")
public class RegisterView extends VerticalLayout implements BeforeEnterObserver {
    ResidentAccountService residentAccountService;
    RoomService roomService;

    private RegisterForm registerForm = new RegisterForm();

    public RegisterView(ResidentAccountService residentAccountService, RoomService roomService) {
        this.residentAccountService = residentAccountService;
        this.roomService = roomService;
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        registerForm.addListener(RegisterForm.RegisterFormEvent.SaveEvent.class, this::saveAccount);
        registerForm.addListener(RegisterForm.RegisterFormEvent.CancelEvent.class, this::clearRegistrationForm);
        add(new H1("Register"), registerForm);
    }

    private void saveAccount(RegisterForm.RegisterFormEvent.SaveEvent event) {
        residentAccountService.save((ResidentAccount) event.getAccount());
        roomService.save(event.getSelectedRoom());
    }

    private void clearRegistrationForm(RegisterForm.RegisterFormEvent.CancelEvent event) {
//TODO
    }
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
        }
    }
}
