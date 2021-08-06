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
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.resident_admission.AdmissionCode;
import com.wg_planner.backend.utils.code_generator.custom_code_generator.CustomCodeCreator;
import com.wg_planner.views.utils.UINavigationHandler;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.util.List;

@Push
@Route(value = "register")
@PageTitle("Register | WG Planner")
@CssImport("./styles/views/register/register-view.css")
public class NewResidentAdmissionView extends VerticalLayout {
    private NewResidentAdmissionPresenter newResidentAdmissionPresenter;
    private AutowireCapableBeanFactory beanFactory;
    private TextField floorCode = new TextField("Floor Code", "Enter your floor code");
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
    }

    private VerticalLayout getFloorCodeLayout() {
        VerticalLayout floorCodeLayout = new VerticalLayout();
        floorCode.setMaxLength(CustomCodeCreator.CodeGenerationPurposes.FLOOR_CODE.getCodeLength());
        floorCode.setMinLength(CustomCodeCreator.CodeGenerationPurposes.FLOOR_CODE.getCodeLength());
        floorCode.setAutofocus(true);
        floorCodeLayout.setAlignItems(Alignment.CENTER);
        floorCodeLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        floorCodeLayout.add(floorCode);
        floorCodeLayout.add(submitFloorCodeButton);
        submitFloorCodeButton.addClickShortcut(Key.ENTER);
        submitFloorCodeButton.addClickListener(this::onSubmitFloorCode);
        return floorCodeLayout;
    }

    public void onSubmitFloorCode(ClickEvent<Button> buttonClickEvent) {
        if (!floorCode.isInvalid()) {
            List<Room> nonOccupiedRooms =
                    newResidentAdmissionPresenter.verifyFloorCodeAndGetNonOccupiedRooms(floorCode.getValue());
            floorCode.clear();
            if (nonOccupiedRooms == null) {
                floorCode.setErrorMessage("Invalid floor code, try again");
            } else if (nonOccupiedRooms.isEmpty()) {
                floorCode.setErrorMessage("No free rooms available in this floor");
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
            }
        } else {
            floorCode.setErrorMessage("Invalid floor code, try again");
        }
    }

    private void onRoomSelect(ClickEvent<Button> buttonClickEvent) {
        if (!nonOccupiedRoomsComboBox.isInvalid() && nonOccupiedRoomsComboBox.getValue() != null) {
            roomSelected = nonOccupiedRoomsComboBox.getValue();
            AdmissionCode admissionCode =
                    newResidentAdmissionPresenter.generateAndSaveAdmissionCode(roomSelected);
            if (admissionCode != null && !admissionCode.toString().isEmpty()) {
                printAdmissionCodeAndWaitForConfirmation(roomSelected, admissionCode);
            }
        } else {
            nonOccupiedRoomsComboBox.clear();
            nonOccupiedRoomsComboBox.setErrorMessage("An error occurred, try again");
        }
    }

    private void printAdmissionCodeAndWaitForConfirmation(Room roomNameSelected, AdmissionCode admissionCode) {
        Span admissionCodeDescription = new Span();
        admissionCodeDescription.setText("The one time code generated for room " + roomNameSelected.getRoomName() + " is " + admissionCode.toString() +
                ". Ask one of your room mates to make use of the Admit Resident option in FloorDetails page to verify the code and " +
                "admit you in.");
        removeAll();
        add(admissionCodeDescription);
        UI.getCurrent().push();
        newResidentAdmissionPresenter.waitForAdmissionStatusChange(admissionCode);
    }

    public void onAccept() {
        UINavigationHandler.getInstance().navigateToRegisterPageParamRoomId(roomSelected.getId());
    }

    public void onReject() {
        printMessage("Your admission to room " + roomSelected.getRoomName() + " has been rejected");
        backToRegisterPage();
    }

    public void onTimeOut() {
        printMessage("The one time code generated has timed out, please try again");
        backToRegisterPage();
    }
    public void printErrorOccurred() {
        printMessage("An error occurred, please try again");
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