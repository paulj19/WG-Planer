package com.wg_planner.views.home_page;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.views.utils.AccountDetailsHelper;

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
        nameField.setValue(AccountDetailsHelper.getUserResidentAccount(residentAccountService).getFirstName() + " " + AccountDetailsHelper.getUserResidentAccount(residentAccountService).getLastName());
        return nameField;
    }

    private TextField getResidentRoomAsTextField() {
        TextField RoomField = new TextField("Room");
        RoomField.setReadOnly(true);
        RoomField.setValue(AccountDetailsHelper.getUserResidentAccount(residentAccountService).getRoom().getRoomNumber());
        return RoomField;
    }

    private TextField getResidentUsernameAsTextField() {
        TextField UsernameField = new TextField("Username");
        UsernameField.setReadOnly(true);
        UsernameField.setValue(AccountDetailsHelper.getUserResidentAccount(residentAccountService).getUsername());
        return UsernameField;
    }

    private PasswordField getResidentPasswordAsPasswordField() {
        PasswordField passwordField = new PasswordField("Password");
        passwordField.setReadOnly(true);
        passwordField.setValue(AccountDetailsHelper.getUserResidentAccount(residentAccountService).getPassword());
        return passwordField;
    }

    private H1 getHeading() {
        return new H1("Account Details to another roommate");
    }

}
