package com.wg_planner.views.UnauthorizedPages.register;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.entity.Account;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.ResidentAccount;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.views.utils.ErrorScreen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

//todo registration form for different roles(combo box options) this is for residents, change name accordingly?
// todo are you currently in the room and ready to take the tasks
public class RegisterForm extends FormLayout {
    //todo check why autowired not working
    @Autowired
    PasswordEncoder passwordEncoder;
    FloorService floorService;

    TextField firstName = new TextField("First Name", "Enter your first name");
    TextField lastName = new TextField("Last Name", "Enter your last name");
    EmailField email = new EmailField("Email", "Enter your email address");
    TextField username = new TextField("Username", "Enter user name");
    PasswordField password = new PasswordField("Password", "Enter password");
    //    ComboBox<Floor> floorComboBox = new ComboBox<>("Floor Name");
    TextField floorTextField = new TextField("Floor");
    ComboBox<Room> roomsRoomComboBox = new ComboBox<>("Room Name");
    Checkbox isReadyToAcceptTasks = new Checkbox();
    boolean isAway = true;
    Account account;
    Floor floorPreset;
    List<Room> rooms = new ArrayList<>();

    Button register = new Button("Register");
    Button cancel = new Button("Cancel");

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private RegisterForm(FloorService floorService) {
        this.floorService = floorService;
    }


    public RegisterForm(Floor floorToPreset, FloorService floorService) {
        this(floorService);
        floorPreset = floorToPreset;
        sanityChecksInvalidParameters(floorToPreset);
        floorTextField.setValue(floorToPreset.getFloorName());
        roomsRoomComboBox.setRequiredIndicatorVisible(true);
        setRoomsInComboBoxFromSelectedFloor(floorToPreset);
        init();
    }

    public RegisterForm(Room roomToPreset, FloorService floorService) {
        this(floorService);
        floorPreset = roomToPreset.getFloor();
        sanityChecksInvalidParameters(roomToPreset);
        floorTextField.setValue(roomToPreset.getFloor().getFloorName());
        floorTextField.setReadOnly(true);
        setRoomsInComboBoxFromSelectedFloor(roomToPreset.getFloor());
        roomsRoomComboBox.setValue(roomToPreset);
        roomsRoomComboBox.setReadOnly(true);
        init();
    }

    private void init() {
        addClassName("register-form");
        setResponsiveSteps(new ResponsiveStep("0", 1));
        firstName.setValueChangeMode(ValueChangeMode.EAGER);
        firstName.setMaxLength(250);
        firstName.addValueChangeListener(textFieldBlurEvent -> {
            if (!isNameValid(firstName.getValue())) {
                firstName.setErrorMessage("invalid name");
                firstName.setInvalid(true);
            } else {
                firstName.setInvalid(false);
            }
            checkAndSetRegisterButton();
        });
        lastName.setValueChangeMode(ValueChangeMode.EAGER);
        lastName.setMaxLength(250);
        lastName.addValueChangeListener(textFieldBlurEvent -> {
            if (!isNameValid(lastName.getValue())) {
                lastName.setErrorMessage("invalid name");
                lastName.setInvalid(true);
            } else {
                lastName.setInvalid(false);
            }
            checkAndSetRegisterButton();
        });
        email.addValueChangeListener(textFieldBlurEvent -> {
            if (!isEmailValid(email.getValue())) {
                email.setErrorMessage("invalid email address");
                email.setInvalid(true);
            } else {
                email.setInvalid(false);
            }
            checkAndSetRegisterButton();
        });
        username.setValueChangeMode(ValueChangeMode.EAGER);
        username.setMaxLength(250);
        username.addValueChangeListener(textFieldBlurEvent -> {
            if (!isUsernameValid(username.getValue())) {
                username.setErrorMessage("invalid username");
                username.setInvalid(true);
            } else {
                username.setInvalid(false);
            }
            checkAndSetRegisterButton();
        });
        password.setValueChangeMode(ValueChangeMode.EAGER);
        password.setMinLength(8);
        password.addValueChangeListener(textFieldBlurEvent -> {
            if (!isPasswordValid(password.getValue())) {
                password.setErrorMessage("invalid password");
                password.setInvalid(true);
            } else {
                password.setInvalid(false);
            }
            checkAndSetRegisterButton();
        });
        isReadyToAcceptTasks.setLabel("I am ready to accept tasks");
        isReadyToAcceptTasks.addValueChangeListener(checkboxBooleanComponentValueChangeEvent -> isAway =
                !checkboxBooleanComponentValueChangeEvent.getValue());
//        setWidth("500px");
        add(firstName, lastName, email, username, password, floorTextField, roomsRoomComboBox, isReadyToAcceptTasks,
                createButtonLayout());
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
        roomsRoomComboBox.setItems(rooms);
        roomsRoomComboBox.setItemLabelGenerator(Room::getRoomName);
    }


    private void checkAndSetRegisterButton() {
        register.setEnabled(isAllFieldsValid() && isAllFieldsFilled());
    }

    //TODO make better
    private boolean isAllFieldsValid() {
        return !firstName.isInvalid() && !lastName.isInvalid() && !email.isInvalid() && !username.isInvalid() && !password.isInvalid();
    }

    private boolean isAllFieldsFilled() {
        return !firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !username.isEmpty() && !password.isEmpty();
    }

    private HorizontalLayout createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        register.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        register.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);
        register.setEnabled(false);
        buttonLayout.getStyle().set("margin-top", "15px");


        register.addClickListener(event -> validateAndSave());
        cancel.addClickListener(event -> fireEvent(new RegisterFormEvent.CancelEvent(this)));
        buttonLayout.add(register, cancel);
        return buttonLayout;
    }

    private boolean isNameValid(String nameToVerify) {
        return nameToVerify.matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$");
    }

    private boolean isEmailValid(String emailToVerify) {
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        return emailToVerify.matches(EMAIL_PATTERN);
    }

    private boolean isUsernameValid(String usernameToVerify) {
        return true;
    }

    private boolean isPasswordValid(String usernameToVerify) {
        return true;
    }

    private Room getSelectedRoom() throws RuntimeException {
        Room selectedRoom = roomsRoomComboBox.getValue();
        //        Floor selectedFloor = floorComboBox.getValue();
        if (selectedRoom == null || floorPreset == null) {
            throw new RuntimeException("selected room or floor null");
        }
        selectedRoom.setOccupied(true);
        return selectedRoom;
    }

    private List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        return authorities;
    }

    private Account getEnteredValuesAsAccount() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        ResidentAccount residentAccount = new ResidentAccount(firstName.getValue(), lastName.getValue(),
                email.getValue(),
                username.getValue(), encoder.encode(password.getValue()), getSelectedRoom(), isAway, getAuthorities());
        return residentAccount;
    }

    private void validateAndSave() {
        account = getEnteredValuesAsAccount();
        fireEvent(new RegisterFormEvent.SaveEvent(this, account, floorPreset, getSelectedRoom()));
    }

    public static abstract class RegisterFormEvent extends ComponentEvent<RegisterForm> {
        private Account account;
        private Floor selectedFloor;
        private Room selectedRoom;

        protected RegisterFormEvent(RegisterForm source, Account account, Floor selectedFloor, Room selectedRoom) {
            super(source, false);
            this.account = account;
            this.selectedFloor = selectedFloor;
            this.selectedRoom = selectedRoom;
        }

        public Account getAccount() {
            return account;
        }

        public Floor getSelectedFloor() {
            return selectedFloor;
        }

        public Room getSelectedRoom() {
            return selectedRoom;
        }

        public static class SaveEvent extends RegisterFormEvent {
            SaveEvent(RegisterForm source, Account account, Floor selectedFloor, Room selectedRoom) {
                super(source, account, selectedFloor, selectedRoom);
            }
        }

        public static class CancelEvent extends RegisterFormEvent {
            CancelEvent(RegisterForm source) {
                super(source, null, null, null);
            }
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}