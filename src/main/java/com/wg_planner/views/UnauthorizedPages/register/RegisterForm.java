package com.wg_planner.views.UnauthorizedPages.register;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.shared.Registration;
import com.wg_planner.backend.Service.AccountDetailsService;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.ResidentAccount;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.views.utils.ErrorScreen;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

//todo registration form for different roles(combo box options) this is for residents, change name accordingly?
public class RegisterForm extends FormLayout {
    FloorService floorService;
    AccountDetailsService accountDetailsService;

    TextField firstName = new TextField("First Name", "Enter your first name");
    TextField lastName = new TextField("Last Name", "Enter your last name");
    EmailField email = new EmailField("Email", "Enter your email address");
    TextField username = new TextField("Username", "Enter user name");
    PasswordField passwordField = new PasswordField("Password", "Minimum 6 characters");
    TextField floorTextField = new TextField("Floor");
    ComboBox<Room> roomComboBox = new ComboBox<>("Room");
    Checkbox isReadyToAcceptTasks = new Checkbox("I am ready to accept tasks");
    Floor floorPreset;
    List<Room> rooms = new ArrayList<>();
    private Binder<ResidentAccount> residentAccountBinder = new BeanValidationBinder<>(ResidentAccount.class);
    ResidentAccount residentAccount = new ResidentAccount();
    Button register = new Button("Register");
    Button cancel = new Button("Cancel");

    private RegisterForm(FloorService floorService, AccountDetailsService accountDetailsService) {
        this.floorService = floorService;
        this.accountDetailsService = accountDetailsService;
    }


    public RegisterForm(Floor floorToPreset, FloorService floorService, AccountDetailsService accountDetailsService) {
        this(floorService, accountDetailsService);
        floorPreset = floorToPreset;
        sanityChecksInvalidParameters(floorToPreset);
        floorTextField.setValue(floorToPreset.getFloorName());
        floorTextField.setReadOnly(true);
        roomComboBox.setRequiredIndicatorVisible(true);
        roomComboBox.setRequired(true);
        setRoomsInComboBoxFromSelectedFloor(floorToPreset);
        init();
    }

    public RegisterForm(Room roomToPreset, FloorService floorService, AccountDetailsService accountDetailsService) {
        this(floorService, accountDetailsService);
        floorPreset = roomToPreset.getFloor();
        sanityChecksInvalidParameters(roomToPreset);
        floorTextField.setValue(roomToPreset.getFloor().getFloorName());
        floorTextField.setReadOnly(true);
        setRoomsInComboBoxFromSelectedFloor(roomToPreset.getFloor());
        roomComboBox.setValue(roomToPreset);
        roomComboBox.setReadOnly(true);
        init();
    }

    private void init() {
        addClassName("register-form");
        setFieldProperties();
        residentAccountBinder.forField(firstName).withValidator(firstName -> firstName.length() <= 64,
                "first name must not exceed 64 characters").withValidator(firstName -> (firstName != null && !firstName.isEmpty()),
                "first name must not be empty").bind(ResidentAccount::getFirstName, ResidentAccount::setFirstName);
        residentAccountBinder.forField(lastName).withValidator(lastName -> lastName.length() <= 64,
                "last name must not exceed 64 characters").withValidator(lastName -> (lastName != null && !lastName.isEmpty()),
                "last name must not be empty").bind(ResidentAccount::getLastName, ResidentAccount::setLastName);
        residentAccountBinder.forField(email).withValidator(new EmailValidator("Not a valid email address")).bind(ResidentAccount::getEmail,
                ResidentAccount::setEmail);
        residentAccountBinder.forField(username).withValidator(username -> accountDetailsService.isUsernameUnique(username.trim()),
                "username already taken").withValidator(username -> username.matches("[A-Za-z0-9_]+"),
                "only alphanumeric and underscore permitted in username").bind(ResidentAccount::getUsername, ResidentAccount::setUsername);
        residentAccountBinder.forField(passwordField).withValidator(password -> password.length() >= 6, "password should be at least 6 " +
                "characters").bind(ResidentAccount::getPassword, ResidentAccount::setPassword);
        residentAccountBinder.forField(roomComboBox).withValidator(Objects::nonNull, "room must not be empty").bind(ResidentAccount::getRoom,
                ResidentAccount::setRoom);
        residentAccountBinder.forField(isReadyToAcceptTasks).bind(ResidentAccount::isPresent,
                ResidentAccount::setPresent);

        setResponsiveSteps(new ResponsiveStep("0", 1));
        add(firstName, lastName, email, username, passwordField, floorTextField, roomComboBox,
                isReadyToAcceptTasks,
                createButtonLayout());
    }

    private void setFieldProperties() {
        firstName.setClearButtonVisible(true);
        firstName.setMaxLength(64);
        lastName.setClearButtonVisible(true);
        lastName.setMaxLength(64);
        username.setMaxLength(255);
        passwordField.setMinLength(6);
        roomComboBox.setPlaceholder("Select a room");
    }

    private void sanityChecksInvalidParameters(Object object) {
        assert object != null;
        if (object == null) { // assert to be disabled during runtime
            removeAll();
            add(new ErrorScreen());
            return;
        }
    }

    private void setRoomsInComboBoxFromSelectedFloor(Floor selectedFloor) {
        rooms = floorService.getAllNonOccupiedRoomsInFloor(selectedFloor);
        roomComboBox.setItems(rooms);
        roomComboBox.setItemLabelGenerator(Room::getRoomName);
    }


    private HorizontalLayout createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        register.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        register.setEnabled(true);
        buttonLayout.getStyle().set("margin-top", "15px");
        register.addClickListener(event -> validateAndSave());
        cancel.addClickListener(event -> fireEvent(new RegisterFormEvent.CancelEvent(this)));
        buttonLayout.add(register, cancel);
        return buttonLayout;
    }

    private Set<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        return authorities;
    }

    private void validateAndSave() {
        try {
            residentAccount.setAuthorities((getAuthorities()));
            residentAccountBinder.writeBean(residentAccount);
            fireEvent(new RegisterFormEvent.SaveEvent(this, residentAccount));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class RegisterFormEvent extends ComponentEvent<RegisterForm> {
        private ResidentAccount residentAccount;

        protected RegisterFormEvent(RegisterForm source, ResidentAccount residentAccount) {
            super(source, false);
            this.residentAccount = residentAccount;
        }

        public ResidentAccount getResidentAccount() {
            return residentAccount;
        }

        public static class SaveEvent extends RegisterFormEvent {
            SaveEvent(RegisterForm source, ResidentAccount residentAccount) {
                super(source, residentAccount);
            }
        }

        public static class CancelEvent extends RegisterFormEvent {
            CancelEvent(RegisterForm source) {
                super(source, null);
            }
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}