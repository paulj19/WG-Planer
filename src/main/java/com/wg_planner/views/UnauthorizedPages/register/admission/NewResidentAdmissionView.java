package com.wg_planner.views.UnauthorizedPages.register.admission;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.resident_admission.AdmissionCode;
import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.backend.utils.code_generator.custom_code_generator.CustomCodeCreator;
import com.wg_planner.views.UnauthorizedPages.UnauthorizedPagesView;
import com.wg_planner.views.utils.UINavigationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.util.List;

@Route(value = "register", layout = UnauthorizedPagesView.class)
@PageTitle("Register | WG Planer")
@CssImport("./styles/views/register/register-view.css")
public class NewResidentAdmissionView extends VerticalLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewResidentAdmissionView.class);
    private NewResidentAdmissionPresenter newResidentAdmissionPresenter;
    private AutowireCapableBeanFactory beanFactory;
    private TextField floorCodeField = new TextField("Floor Code", "Enter your floor code");
    Span helperText = new Span("Find it in Floor Details in Home page in a floor already created.");
    private ComboBox<Room> nonOccupiedRoomsComboBox;
    private Button submitFloorCodeButton = new Button("Submit");
    private Button selectRoomButton = new Button("Select");
    private Room roomSelected;

    public NewResidentAdmissionView(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        newResidentAdmissionPresenter = new NewResidentAdmissionPresenter();
        beanFactory.autowireBean(newResidentAdmissionPresenter);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(getFloorCodeLayout());
        newResidentAdmissionPresenter.init(this);
        LOGGER.info(LogHandler.getTestRun(), "new resident admit screen to enter floor code");
    }

    private VerticalLayout getFloorCodeLayout() {
        VerticalLayout floorCodeLayout = new VerticalLayout();
        floorCodeField.setMaxLength(CustomCodeCreator.CodeGenerationPurposes.FLOOR_CODE.getCodeLength());
        floorCodeField.setMinLength(CustomCodeCreator.CodeGenerationPurposes.FLOOR_CODE.getCodeLength());
        floorCodeField.setAutofocus(true);
        helperText.addClassName("helper-text");
        floorCodeLayout.setAlignItems(Alignment.CENTER);
        floorCodeLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        floorCodeLayout.add(floorCodeField);
        floorCodeLayout.add(helperText);
        floorCodeLayout.add(helperText);
        floorCodeLayout.add(submitFloorCodeButton);
        submitFloorCodeButton.addClickShortcut(Key.ENTER);
        submitFloorCodeButton.addClickListener(this::onSubmitFloorCode);
        return floorCodeLayout;
    }

    public void onSubmitFloorCode(ClickEvent<Button> buttonClickEvent) {
        if (!floorCodeField.isInvalid()) {
            List<Room> nonOccupiedRooms =
                    newResidentAdmissionPresenter.verifyFloorCodeAndGetNonOccupiedRooms(floorCodeField.getValue());
            String floorCodeValue = floorCodeField.getValue();
            floorCodeField.clear();
            if (nonOccupiedRooms == null) {
                floorCodeField.setErrorMessage("Invalid floor code, try again");
                LOGGER.info(LogHandler.getStrange(), "floor code {} valid. Non occupied rooms returned null.", floorCodeValue);
            } else if (nonOccupiedRooms.isEmpty()) {
                floorCodeField.setErrorMessage("Floor not available.");
                LOGGER.info(LogHandler.getTestRun(), "No free rooms available in this floor. Floor code {}.", floorCodeValue);
            } else {
                VerticalLayout selectRoomVerticalLayout = new VerticalLayout();
                nonOccupiedRoomsComboBox = new ComboBox<>("select a room");
                nonOccupiedRoomsComboBox.setItems(nonOccupiedRooms);
                nonOccupiedRoomsComboBox.setItemLabelGenerator(Room::getRoomName);
                nonOccupiedRoomsComboBox.setAllowCustomValue(false);
                nonOccupiedRoomsComboBox.setRequired(true);
                selectRoomVerticalLayout.setAlignItems(Alignment.CENTER);
                selectRoomVerticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
                selectRoomVerticalLayout.add(nonOccupiedRoomsComboBox);
                selectRoomVerticalLayout.add(selectRoomButton);
                selectRoomButton.addClickShortcut(Key.ENTER);
                selectRoomButton.addClickListener(this::onRoomSelect);
                removeAll();
                add(selectRoomVerticalLayout);
                LOGGER.info(LogHandler.getTestRun(), "Floor code entered valid. Floor code {} available room ids ", floorCodeValue);
                nonOccupiedRooms.forEach(room -> LOGGER.info(LogHandler.getTestRun(), ", {}", room.getId()));
            }
        } else {
            floorCodeField.setErrorMessage("Invalid floor code, try again");
            LOGGER.info(LogHandler.getTestRun(), "Invalid floor code {}.", floorCodeField.getValue());
        }
    }

    private void onRoomSelect(ClickEvent<Button> buttonClickEvent) {
        if (!nonOccupiedRoomsComboBox.isInvalid() && nonOccupiedRoomsComboBox.getValue() != null) {
            roomSelected = nonOccupiedRoomsComboBox.getValue();
            AdmissionCode admissionCode =
                    newResidentAdmissionPresenter.generateAndSaveAdmissionCode(roomSelected);
            if (admissionCode != null && !admissionCode.toString().isEmpty()) {
                printAdmissionCodeAndWaitForConfirmation(roomSelected, admissionCode);
                LOGGER.info(LogHandler.getTestRun(), "admission code {}. Selected room {}.", admissionCode, roomSelected.toString());
            }
        } else if (nonOccupiedRoomsComboBox.getValue() == null) {
            nonOccupiedRoomsComboBox.setErrorMessage("Room not selected");
        } else {
            nonOccupiedRoomsComboBox.clear();
            nonOccupiedRoomsComboBox.setErrorMessage("An error occurred, try again");
            LOGGER.info(LogHandler.getStrange(), "Error occurred, room combobox is either invalid or room combobox getValue is null.");
        }
    }

    private void printAdmissionCodeAndWaitForConfirmation(Room roomNameSelected, AdmissionCode admissionCode) {
        Span admissionCodeDescription = new Span();
        admissionCodeDescription.setText("The one time code generated for room " + roomNameSelected.getRoomName() + " is " + admissionCode.toString() +
                ". Ask one of your room mates to make use of the Admit Resident option in FloorDetails page to verify the code and " +
                "admit you in. The validity of this one time code is " + newResidentAdmissionPresenter.getValidityOfAdmissionCodeInMinutes() + " minutes. " +
                "Please do not refresh or close this page before that.");
        removeAll();
        add(admissionCodeDescription);
        UI.getCurrent().push();
        newResidentAdmissionPresenter.waitForAdmissionStatusChange(admissionCode);
    }

    public void onAccept() {
        LOGGER.debug("Admission request accepted. Room {}.", roomSelected.getId());
        UINavigationHandler.getInstance().navigateToRegisterPageParamRoomId(roomSelected.getId());
    }

    public void onReject() {
        printMessage("Your admission to room " + roomSelected.getRoomName() + " has been rejected.");
        LOGGER.debug("Admission request rejected. Room {}.", roomSelected.getId());
        backToRegisterPage();
    }

    public void onTimeOut() {
        printMessage("The one time code generated has timed out, please try again.");
        backToRegisterPage();
    }

    public void printErrorOccurred() {
        printMessage("An error occurred, please try again.");
        backToRegisterPage();
    }

    private void backToRegisterPage() {
        Span backTo = new Span("back to ");
        Anchor anchor = new Anchor("register", "register page");
        add(new HorizontalLayout(backTo, anchor));
    }

    private void printMessage(String message) {
        Span messageHolder = new Span();
        messageHolder.setText(message);
        removeAll();
        add(messageHolder);
    }
}