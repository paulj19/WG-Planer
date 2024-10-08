package com.wg_planner.views.sub_menu.account_details;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.entity.ResidentAccount;
import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.utils.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResidentDetailsView extends VerticalLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResidentDetailsView.class);
    ResidentAccountService residentAccountService;
    ResidentAccount residentAccount;

    public ResidentDetailsView(ResidentAccountService residentAccountService) {
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. ResidentDetailsView selected.",
                SessionHandler.getLoggedInResidentAccount().getId());
        this.residentAccountService = residentAccountService;
        residentAccount =
                residentAccountService.getResidentAccountById(SessionHandler.getLoggedInResidentAccount().getId());
        add(getResidentNameAsTextField());
        add(getResidentFloorCodeAsTextField());
        add(getResidentRoomAsTextField());
        add(getResidentUsernameAsTextField());
    }

    private TextField getResidentNameAsTextField() {
        TextField nameField = new TextField("Name");
        nameField.setReadOnly(true);
        nameField.setValue(residentAccount.getFirstName() + " " + residentAccount.getLastName());
        return nameField;
    }

    private TextField getResidentFloorCodeAsTextField() {
        TextField floorCodeField = new TextField("Floor Code");
        floorCodeField.setReadOnly(true);
        floorCodeField.setValue(residentAccount.getRoom().getFloor().getFloorCode());
        return floorCodeField;
    }

    private TextField getResidentRoomAsTextField() {
        TextField RoomField = new TextField("Room");
        RoomField.setReadOnly(true);
        RoomField.setValue(residentAccount.getRoom().getRoomName());
        return RoomField;
    }

    private TextField getResidentUsernameAsTextField() {
        TextField UsernameField = new TextField("Username");
        UsernameField.setReadOnly(true);
        UsernameField.setValue(residentAccount.getUsername());
        return UsernameField;
    }
}
