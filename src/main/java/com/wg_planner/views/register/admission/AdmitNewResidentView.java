package com.wg_planner.views.register.admission;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.wg_planner.backend.resident_admission.AdmissionDetails;
import com.wg_planner.backend.utils.code_generator.custom_code_generator.CustomCodeCreator;
import com.wg_planner.views.main.MainView;
import com.wg_planner.views.utils.SessionHandler;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

@Route(value = "admit_resident", layout = MainView.class)
@RouteAlias(value = "admit_resident", layout = MainView.class)
@PageTitle("Admit Resident")
@CssImport("./styles/views/admit/admit-resident-view.css")
public class AdmitNewResidentView extends VerticalLayout {
    AutowireCapableBeanFactory beanFactory;
    private static final java.util.logging.Logger LOGGER = Logger.getLogger(AdmitNewResidentView.class
            .getName());
    TextField admissionCode = new TextField("Admission Code", "Enter admission code from resident to be admitted");
    Button submitAdmissionCodeButton = new Button("Submit");
    Button acceptButton = new Button("Accept");
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
        admissionCode.setMaxLength(CustomCodeCreator.CodeGenerationPurposes.ADMISSION_CODE.getCodeLength());
        admissionCode.setMinLength(CustomCodeCreator.CodeGenerationPurposes.ADMISSION_CODE.getCodeLength());
        admissionCode.setAutofocus(true);
        floorCodeLayout.setAlignItems(Alignment.CENTER);
        floorCodeLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        floorCodeLayout.add(admissionCode);
        floorCodeLayout.add(submitAdmissionCodeButton);
        submitAdmissionCodeButton.addClickShortcut(Key.ENTER);
        submitAdmissionCodeButton.addClickListener(this::onSubmitAdmissionCode);
        //        floorCodeLayout.setMinWidth("250px");
        return floorCodeLayout;
    }

    //todo case sentive
    private void onSubmitAdmissionCode(ClickEvent<Button> buttonClickEvent) {
        if (!admissionCode.isInvalid()) {
            AdmissionDetails admissionDetails = admitNewResidentPresenter.verifyAdmissionCodeAndGetAdmissionDetails(admissionCode.getValue());
            sanityCheckAssertions(admissionDetails);
            admissionCode.clear();
            if (admissionDetails == null) { //not present in map
                admissionCode.setErrorMessage("Invalid admission code, try again");
            } else if (!admissionDetails.getRoomToAdmit().getFloor().equals(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor())) {
                LOGGER.log(Level.SEVERE,
                        "room does not belong to this floor, must not happen. Admission Code: " + admissionCode.toString() +
                                "LoggedInResidentAccount: " + SessionHandler.getLoggedInResidentAccount().toString() +
                                "RoomToAdmit: " + admissionDetails.getRoomToAdmit().toString());
                admissionCode.setErrorMessage("Invalid admission code, try again");
            } else {
                VerticalLayout buttonsLayout = new VerticalLayout();
                removeAll();
                buttonsLayout.setAlignItems(Alignment.CENTER);
                buttonsLayout.setJustifyContentMode(JustifyContentMode.CENTER);
                acceptButton.addClickShortcut(Key.ENTER);
                acceptButton.addClickListener(this::onAccept);
                rejectButton.addClickListener(this::onReject);
                buttonsLayout.add(new HorizontalLayout(getAdmissionDescription(admissionDetails.getRoomToAdmit().getRoomName()) ,acceptButton, rejectButton));
                removeAll();
                add(buttonsLayout);
            }
        } else {
            admissionCode.setErrorMessage("Invalid admission code, try again");
        }
    }

    private void onReject(ClickEvent<Button> buttonClickEvent) {
        //todo
    }

    private void onAccept(ClickEvent<Button> buttonClickEvent) {
        //todo
    }

    private Span getAdmissionDescription(String roomName) {
        Span admissionDescription = new Span();
        admissionDescription.setText("Resident has requested admission in room " + roomName);
        return admissionDescription;
    }
    private void sanityCheckAssertions(AdmissionDetails admissionDetails) {
        assert admissionDetails.getRoomToAdmit() != null;
        assert admissionDetails.getRoomToAdmit().isOccupied() == false;
        assert admissionDetails.getRoomToAdmit().getResidentAccount() == null;
        assert admissionDetails.getRoomToAdmit().getFloor().equals(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor());
    }
}
