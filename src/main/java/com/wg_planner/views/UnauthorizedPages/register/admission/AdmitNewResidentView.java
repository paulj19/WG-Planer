package com.wg_planner.views.UnauthorizedPages.register.admission;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.wg_planner.backend.resident_admission.AdmissionCode;
import com.wg_planner.backend.resident_admission.AdmissionDetails;
import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.backend.utils.code_generator.custom_code_generator.CustomCodeCreator;
import com.wg_planner.views.main.MainView;
import com.wg_planner.views.utils.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;


@Route(value = "admit_resident", layout = MainView.class)
@RouteAlias(value = "admit_resident", layout = MainView.class)
@PageTitle("Admit Resident")
@CssImport("./styles/views/admit/admit-resident-view.css")
public class AdmitNewResidentView extends VerticalLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdmitNewResidentView.class);
    AutowireCapableBeanFactory beanFactory;
    TextField admissionCodeField = new TextField("Admission Code", "New Resident Admission Code");
    Button submitAdmissionCodeButton = new Button("Submit");
    Button acceptButton = new Button("Admit");
    Button rejectButton = new Button("Reject");
    private AdmitNewResidentPresenter admitNewResidentPresenter;

    public AdmitNewResidentView(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;

        admitNewResidentPresenter = new AdmitNewResidentPresenter(this);
        beanFactory.autowireBean(admitNewResidentPresenter);
        add(getAdmissionCodeLayout());
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Admitter field to admit new resident open.",
                SessionHandler.getLoggedInResidentAccount().getId());
    }

    private VerticalLayout getAdmissionCodeLayout() {
        VerticalLayout floorCodeLayout = new VerticalLayout();
        admissionCodeField.setMaxLength(CustomCodeCreator.CodeGenerationPurposes.ADMISSION_CODE.getCodeLength());
        admissionCodeField.setMinLength(CustomCodeCreator.CodeGenerationPurposes.ADMISSION_CODE.getCodeLength());
        admissionCodeField.setAutofocus(true);
        floorCodeLayout.setAlignItems(Alignment.CENTER);
        floorCodeLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        floorCodeLayout.add(admissionCodeField);
        floorCodeLayout.add(submitAdmissionCodeButton);
        submitAdmissionCodeButton.addClickShortcut(Key.ENTER);
        submitAdmissionCodeButton.addClickListener(this::onSubmitAdmissionCode);
        return floorCodeLayout;
    }

    private void onSubmitAdmissionCode(ClickEvent<Button> buttonClickEvent) {
        if (!admissionCodeField.isInvalid()) {
            LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Admission code {} entered",SessionHandler.getLoggedInResidentAccount().getId(), admissionCodeField.getValue());
            admitNewResidentPresenter.onSubmitAdmissionCode(admissionCodeField.getValue());
            admissionCodeField.clear();
        } else {
            setInvalidCodeMessage();
            LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Admission code field is invalid", SessionHandler.getLoggedInResidentAccount().getId());
        }
    }

    public void addAcceptRejectButtons(AdmissionCode admissionCode, String roomSelected) {
        VerticalLayout buttonsLayout = new VerticalLayout();
        Div buttonLayout = new Div();
        buttonLayout.addClassName("admit-page-button-layout");
        acceptButton.addClassName("admit-page-button");
        rejectButton.addClassName("admit-page-button");
        buttonLayout.add(acceptButton, rejectButton);
        removeAll();
        buttonsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        buttonsLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        acceptButton.addClickShortcut(Key.ENTER);
        acceptButton.addClickListener(event -> onAccept(event, admissionCode));
        rejectButton.addClickListener(event -> onReject(event, admissionCode));
        buttonsLayout.add(new HorizontalLayout(getAdmissionDescription(roomSelected),
                buttonLayout));
        removeAll();
        add(buttonsLayout);
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Accept Reject buttons added", SessionHandler.getLoggedInResidentAccount().getId());
    }

    private void onAccept(ClickEvent<Button> buttonClickEvent, AdmissionCode admissionCode) {
        if (admitNewResidentPresenter.isAdmissionCodeInvalid(admissionCode)) {
            printAdmissionCodeInvalidMessage();
            LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. onAccept admission code {} is invalid",
                    SessionHandler.getLoggedInResidentAccount().getId(), admissionCode.toString());
        }
        admitNewResidentPresenter.setAdmissionStatus(admissionCode, AdmissionDetails.AdmissionStatus.ADMITTED);
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. onAccept status set to admitted", SessionHandler.getLoggedInResidentAccount().getId());
    }

    private void onReject(ClickEvent<Button> buttonClickEvent, AdmissionCode admissionCode) {
        if (admitNewResidentPresenter.isAdmissionCodeInvalid(admissionCode)) {
            printAdmissionCodeInvalidMessage();
            LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. onAccept admission code {} is invalid",SessionHandler.getLoggedInResidentAccount().getId(), admissionCode.toString());
        }
        admitNewResidentPresenter.setAdmissionStatus(admissionCode, AdmissionDetails.AdmissionStatus.REJECTED);
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. onAccept status set to rejected", SessionHandler.getLoggedInResidentAccount().getId());
    }

    private Span getAdmissionDescription(String roomName) {
        Span admissionDescription = new Span();
        admissionDescription.setText("Resident has requested admission in room " + roomName);
        return admissionDescription;
    }

    public void printAlreadyDoneMessage(String roomName, String admissionStatus) {
        printMessage("Room " + roomName + " was already " + admissionStatus);// todo by? add admittedRoom in AdDetails
    }

    public void printAdmissionCodeInvalidMessage() {
        printMessage("The entered code is now invalid, please try again");
    }

    public void setInvalidCodeMessage() {
        admissionCodeField.setErrorMessage("Invalid admission code, try again");
    }

    public void printMessage(String message) {
        Span messageHolder = new Span();
        messageHolder.setText(message);
        removeAll();
        add(messageHolder);
    }
}
