package com.wg_planner.views.register.admission;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
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
import com.wg_planner.backend.utils.code_generator.custom_code_generator.CustomCodeCreator;
import com.wg_planner.views.main.MainView;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

@Route(value = "admit_resident", layout = MainView.class)
@RouteAlias(value = "admit_resident", layout = MainView.class)
@PageTitle("Admit Resident")
@CssImport("./styles/views/admit/admit-resident-view.css")
public class AdmitNewResidentView extends VerticalLayout {
    AutowireCapableBeanFactory beanFactory;
    TextField admissionCodeField = new TextField("Admission Code", "Enter admission code from resident to be admitted");
    Button submitAdmissionCodeButton = new Button("Submit");
    Button acceptButton = new Button("Admit");
    Button rejectButton = new Button("Reject");
    private AdmitNewResidentPresenter admitNewResidentPresenter;

    public AdmitNewResidentView(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        admitNewResidentPresenter = new AdmitNewResidentPresenter(this);
        beanFactory.autowireBean(admitNewResidentPresenter);
        add(getAdmissionCodeLayout());
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
        //        floorCodeLayout.setMinWidth("250px");
        return floorCodeLayout;
    }

    private void onSubmitAdmissionCode(ClickEvent<Button> buttonClickEvent) {
        if (!admissionCodeField.isInvalid()) {
            admitNewResidentPresenter.onSubmitAdmissionCode(admissionCodeField.getValue());
            admissionCodeField.clear();
        } else {
            setInvalidCodeMessage();
        }
    }

    public void addAcceptRejectButtons(AdmissionCode admissionCode, String roomSelected) {
        VerticalLayout buttonsLayout = new VerticalLayout();
        removeAll();
        buttonsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        buttonsLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        acceptButton.addClickShortcut(Key.ENTER);
        acceptButton.addClickListener(event -> onAccept(event, admissionCode));
        rejectButton.addClickListener(event -> onReject(event, admissionCode));
        buttonsLayout.add(new HorizontalLayout(getAdmissionDescription(roomSelected),
                acceptButton, rejectButton));
        removeAll();
        add(buttonsLayout);
    }

    private void onAccept(ClickEvent<Button> buttonClickEvent, AdmissionCode admissionCode) {
        if(admitNewResidentPresenter.isAdmissionCodeInvalid(admissionCode)) {
            printAdmissionCodeInvalidMessage();
        }
        admitNewResidentPresenter.setAdmissionStatus(admissionCode, AdmissionDetails.AdmissionStatus.ADMITTED);
    }

    private void onReject(ClickEvent<Button> buttonClickEvent, AdmissionCode admissionCode) {
        if(admitNewResidentPresenter.isAdmissionCodeInvalid(admissionCode)) {
            printAdmissionCodeInvalidMessage();
        }
        admitNewResidentPresenter.setAdmissionStatus(admissionCode, AdmissionDetails.AdmissionStatus.REJECTED);
    }

    private Span getAdmissionDescription(String roomName) {
        Span admissionDescription = new Span();
        admissionDescription.setText("Resident has requested admission in room " + roomName);
        return admissionDescription;
    }

    public void printAlreadyDoneMessage(String roomName, String admissionStatus) {
        printMessage("Admission to room " + roomName + " was already " + admissionStatus);// todo by? add admittedRoom in AdDetails
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
