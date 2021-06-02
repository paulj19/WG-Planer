package com.wg_planner.views.signup;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.Service.RoomService;
import com.wg_planner.backend.entity.ResidentAccount;
import com.wg_planner.views.utils.UIStringConstants;


@Route(value = "register")
@PageTitle("Register | WG Planner")
@CssImport("./styles/views/register/register-view.css")
public class RegisterView extends VerticalLayout implements BeforeEnterObserver {
    ResidentAccountService residentAccountService;
    RoomService roomService;
    FloorService floorService;

    private RegisterForm registerForm;

    public RegisterView(ResidentAccountService residentAccountService, RoomService roomService,
                        FloorService floorService) {
        this.residentAccountService = residentAccountService;
        this.roomService = roomService;
        this.floorService = floorService;
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        registerForm = new RegisterForm();
        registerForm.addListener(RegisterForm.RegisterFormEvent.SaveEvent.class, this::saveAccount);
        registerForm.addListener(RegisterForm.RegisterFormEvent.CancelEvent.class, this::clearRegistrationForm);
        add(new H1("Register"), registerForm);
    }

    private void saveAccount(RegisterForm.RegisterFormEvent.SaveEvent event) {
        residentAccountService.save((ResidentAccount) event.getAccount());
        roomService.save(event.getSelectedRoom());
        Notification.show(UIStringConstants.getInstance().getAccountCreatedConfirmation());
        UI.getCurrent().navigate("login/");
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
