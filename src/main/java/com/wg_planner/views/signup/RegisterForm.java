package com.wg_planner.views.signup;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static com.wg_planner.views.utils.SessionHandler.getFloorFromSession;

//todo registration form for different roles(combo box options) this is for residents, change name accordingly?
// todo are you currently in the room and ready to take the tasks
public class RegisterForm extends FormLayout {
    //todo check why autowired not working
    @Autowired
    PasswordEncoder passwordEncoder;

    TextField firstName = new TextField("First Name", "Enter your first name");
    TextField lastName = new TextField("Last Name", "Enter your last name");
    EmailField email = new EmailField("Email", "Enter your company email address");
    TextField username = new TextField("Username", "Enter a user name");
    PasswordField password = new PasswordField("Password", "min 6 characters");
    ComboBox<Floor> floorComboBox = new ComboBox<>("Floor Name");
    ComboBox<Room> roomsRoomComboBox = new ComboBox<>("Room Name");
    Checkbox isReadyToAcceptTasks = new Checkbox();
    boolean isAway = true;
    Account account;
    Floor selectedFloor;
    Room selectedRoom;
    List<Room> rooms = new ArrayList<>();

    Button register = new Button("Register");
    Button cancel = new Button("Cancel");

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    public RegisterForm() {
        addClassName("register-form");

        setFloorComboBox(getFloorFromSession());
        roomsRoomComboBox.setRequiredIndicatorVisible(true);
        roomsRoomComboBox.addFocusListener(event -> {
            if (selectedFloor == null) {
                //TODO not working
                floorComboBox.setErrorMessage("floor not selected");
                floorComboBox.setInvalid(true);
            } else {
                floorComboBox.setInvalid(false);
            }
        });
        roomsRoomComboBox.addValueChangeListener(event -> {
            selectedRoom = event.getValue();
            if (selectedRoom == null) {
                roomsRoomComboBox.setErrorMessage("room not selected");
                floorComboBox.setInvalid(true);
            } else {
                floorComboBox.setInvalid(false);
            }
        });
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
        isReadyToAcceptTasks.setLabel("I am in the room and ready to accept tasks");
        isReadyToAcceptTasks.addValueChangeListener(checkboxBooleanComponentValueChangeEvent -> isAway = !checkboxBooleanComponentValueChangeEvent.getValue());
        setWidth("500px");
        add(firstName, lastName, email, username, password, floorComboBox, roomsRoomComboBox, isReadyToAcceptTasks, createButtonLayout());
    }

    private void setFloorComboBox(Floor floorToPreset) {
        List<Floor> floors = FloorService.getAllFloors();
        floorComboBox.setItems(floors);
        floorComboBox.setItemLabelGenerator(Floor::getFloorName);
        if (floorToPreset != null) {
            selectedFloor = floorToPreset;
            setRoomsInRoomComboBoxFromOnSelectedFloor(floorToPreset);
            floorComboBox.setValue(floorToPreset);
//            floorComboBox.setEnabled(false);
            floorComboBox.setReadOnly(true);
        } else {
            floorComboBox.setAllowCustomValue(false);
            floorComboBox.addValueChangeListener(event -> {
                selectedFloor = event.getValue();
                if (selectedFloor != null) {
                    setRoomsInRoomComboBoxFromOnSelectedFloor(selectedFloor);
                } else {
                    floorComboBox.setInvalid(true);
                }
            });
        }
    }

    private void setRoomsInRoomComboBoxFromOnSelectedFloor(Floor selectedFloor) {
        rooms = FloorService.getAllNonOccupiedRoomsInFloor(selectedFloor);
        roomsRoomComboBox.setItems(rooms);
        roomsRoomComboBox.setItemLabelGenerator(Room::getRoomName);
        floorComboBox.setInvalid(false);
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
        register.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        register.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);
        register.setEnabled(false);

        register.addClickListener(event -> validateAndSave());
        cancel.addClickListener(event -> fireEvent(new RegisterFormEvent.CancelEvent(this, account, selectedFloor, selectedRoom)));
        return new HorizontalLayout(register, cancel);
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
        Floor selectedFloor = floorComboBox.getValue();
        if (selectedRoom == null || selectedFloor == null) {
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
        ResidentAccount residentAccount = new ResidentAccount(firstName.getValue(), lastName.getValue(), email.getValue(), username.getValue(), encoder.encode(password.getValue()), getSelectedRoom(), isAway, getAuthorities());
        return residentAccount;
    }

    private void validateAndSave() {
        account = getEnteredValuesAsAccount();
        fireEvent(new RegisterFormEvent.SaveEvent(this, account, selectedFloor, selectedRoom));
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
            CancelEvent(RegisterForm source, Account account, Floor selectedFloor, Room selectedRoom) {
                super(source, account, selectedFloor, selectedRoom);
            }
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}