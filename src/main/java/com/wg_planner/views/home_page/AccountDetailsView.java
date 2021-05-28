package com.wg_planner.views.home_page;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.views.utils.SessionHandler;

public class AccountDetailsView extends VerticalLayout {
    ResidentAccountService residentAccountService;

    public AccountDetailsView(ResidentAccountService residentAccountService) {
        this.residentAccountService = residentAccountService;
        add(getHeading());
        add(getResidentNameAsTextField());
        add(getResidentRoomAsTextField());
        add(getResidentUsernameAsTextField());
        add(getResidentPasswordAsPasswordField());
    }

    private TextField getResidentNameAsTextField() {
        TextField nameField = new TextField("Name");
        nameField.setReadOnly(true);
        nameField.setValue(SessionHandler.getLoggedInResidentAccount().getFirstName() + " " + SessionHandler.getLoggedInResidentAccount().getLastName());
        return nameField;
    }

    private TextField getResidentRoomAsTextField() {
        TextField RoomField = new TextField("Room");
        RoomField.setReadOnly(true);
        RoomField.setValue(SessionHandler.getLoggedInResidentAccount().getRoom().getRoomName());
        return RoomField;
    }

    private TextField getResidentUsernameAsTextField() {
        TextField UsernameField = new TextField("Username");
        UsernameField.setReadOnly(true);
        UsernameField.setValue(SessionHandler.getLoggedInResidentAccount().getUsername());
        return UsernameField;
    }

    private PasswordField getResidentPasswordAsPasswordField() {
        PasswordField passwordField = new PasswordField("Password");
        passwordField.setReadOnly(true);
        passwordField.setValue(SessionHandler.getLoggedInResidentAccount().getPassword());
        return passwordField;
    }

    private H1 getHeading() {
        return new H1("Account Details to another roommate");
    }

}
