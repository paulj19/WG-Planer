package com.wg_planner.views.signup;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.RoomService;
import com.wg_planner.backend.entity.Account;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.ResidentAccount;
import com.wg_planner.backend.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

//todo registration form for different roles(combo box options) this is for residents, change name accordingly?
// todo are you currently in the room and ready to take the tasks
public class RegisterForm extends FormLayout {
    @Autowired
    RoomService roomService;

    TextField firstName = new TextField("First Name", "please enter your first name");
    TextField lastName = new TextField("Last Name", "please enter your last name");
    EmailField email = new EmailField("Email", "enter your company email address");
    TextField username = new TextField("Username", "please enter a user name");
    PasswordField password = new PasswordField("Password", "min 6 characters");
    ComboBox<Floor> floorComboBox = new ComboBox<>("Floor");
    ComboBox<Room> roomsRoomComboBox = new ComboBox<>("Room");
    Account account;
    Floor selectedFloor;
    Room selectedRoom;
    List<Room> rooms = new ArrayList<>();

    Button register = new Button("Register");
    Button cancel = new Button("Cancel");

    //    Binder<Account> binder = new BeanValidationBinder<>(Account.class);
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public RegisterForm() {
        addClassName("register-form");
        List<Floor> floors = FloorService.getAllFloors();

//        binder.bindInstanceFields(this);
//        rooms = FloorService.getAllNonOccupiedRoomsInFloor(floors.get(0));
//
        floorComboBox.setItems(floors);
        floorComboBox.setItemLabelGenerator(Floor::getFloorNumber);
        floorComboBox.setAllowCustomValue(false);

        floorComboBox.addValueChangeListener(event -> {
            selectedFloor = event.getValue();
            if (selectedFloor != null) {
                rooms = FloorService.getAllNonOccupiedRoomsInFloor(selectedFloor);
                roomsRoomComboBox.setItems(rooms);
                roomsRoomComboBox.setItemLabelGenerator(Room::getRoomNumber);
                floorComboBox.setInvalid(false);
            } else {
                floorComboBox.setInvalid(true);
            }
        });
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
        lastName.addValueChangeListener(textFieldBlurEvent -> {
            if (!isNameValid(lastName.getValue())) {
                lastName.setErrorMessage("invalid name");
                lastName.setInvalid(true);
            } else {
                lastName.setInvalid(false);
            }
            checkAndSetRegisterButton();
        });
//        email.setValueChangeMode(ValueChangeMode.EAGER);
        email.addValueChangeListener(textFieldBlurEvent -> {
            if (!isEmailValid(email.getValue())) {
                email.setErrorMessage("invalid email address");
                email.setInvalid(true);
            } else {
                email.setInvalid(false);
            }
            checkAndSetRegisterButton();
        });
//        username.addValueChangeListener()
        username.setValueChangeMode(ValueChangeMode.EAGER);
        username.addValueChangeListener(textFieldBlurEvent -> {
            if (!isUsernameValid(username.getValue())) {
                username.setErrorMessage("invalid username");
                username.setInvalid(true);
            } else {
                username.setInvalid(false);
            }
            checkAndSetRegisterButton();
        });
//        password.addFocusListener()
        password.setValueChangeMode(ValueChangeMode.EAGER);
        password.addValueChangeListener(textFieldBlurEvent -> {
            if (!isPasswordValid(password.getValue())) {
                password.setErrorMessage("invalid password");
                password.setInvalid(true);
            } else {
                password.setInvalid(false);
            }
            checkAndSetRegisterButton();
        });
        setWidth("500px");
//        floorComboBox, roomsRoomComboBox,
        add(firstName, lastName, email, username, password, floorComboBox, roomsRoomComboBox, createButtonLayout());
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
//!floorComboBox.isEmpty() && !roomComboBox.isEmpty();
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

    //    private Room getSelectedRoomByName(String roomName) throws RuntimeException {
//        if (roomName != null && !roomName.trim().isEmpty()) {
//            return roomService.getRoomByNumber(roomName);
//        } else {
//            throw new RuntimeException("roomName is invalid");
//        }
//    }
    private Room getSelectedRoom() throws RuntimeException {
        Room selectedRoom = roomsRoomComboBox.getValue();
        Floor selectedFloor = floorComboBox.getValue();
        if (selectedRoom == null || selectedFloor == null) {
            throw new RuntimeException("selected room or floor null");
        }
        selectedRoom.setFloor(selectedFloor);
        return selectedRoom;
    }

    private List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        return authorities;
    }

    private Account getEnteredValuesAsAccount() {
        ResidentAccount residentAccount = new ResidentAccount(firstName.getValue(), lastName.getValue(), email.getValue(), username.getValue(), password.getValue(), getSelectedRoom(), getAuthorities());
        return residentAccount;
    }

    private void validateAndSave() {
        account = getEnteredValuesAsAccount();
//            binder.writeBean(account);
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