package com.wg_planner.views.register;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.Service.RoomService;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.ResidentAccount;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.views.utils.ErrorScreen;
import com.wg_planner.views.utils.UIStringConstants;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;


@Route(value = "register_form")
@PageTitle("Register | WG Planner")
@CssImport("./styles/views/register/register-view.css")
public class RegisterView extends VerticalLayout implements HasUrlParameter<Long> {
    @Autowired
    ResidentAccountService residentAccountService;
    @Autowired
    RoomService roomService;
    @Autowired
    FloorService floorService;

    private RegisterForm registerForm;

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long roomIdSelected) {
        Location location = event.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        Map<String, List<String>> parametersMap = queryParameters.getParameters();
        if(parametersMap.containsKey("floor_id")) {
            if(parametersMap.get("floor_id").isEmpty()) {
                parametersMap.get("floor_id");
            }
//            init(floorService.getFloorById(floorService.getFloorById()));
        } else if(parametersMap.containsKey("room_id")) {
            Room roomSelected = roomService.getRoomById(roomIdSelected);
            init(roomSelected);
        }
        if (roomIdSelected == null) {
            add(new ErrorScreen());
            return;
        }
    }

    public RegisterView() {
    }

    public void init(Floor floorToPreset) {
        registerForm = new RegisterForm(floorToPreset);
        initListenersAndAdd();
    }

    public void init(Room roomToPreset) {
        registerForm = new RegisterForm(roomToPreset);
        initListenersAndAdd();
    }

    private void initListenersAndAdd() {
        setHeight("100%");
        getStyle().set("overflow-y", "auto");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
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
}
