package com.wg_planner.views.sub_menu.account_details;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.views.main.MainView;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UIStringConstants;

@Route(value = "resident_availability", layout = MainView.class)
@PageTitle("Resident Availability")
public class ResidentAvailabilityView extends VerticalLayout {
    ResidentAccountService residentAccountService;
    ConfirmDialog setAvailabilityConfirmDialog;
    AccountDetailsPresenter accountDetailsPresenter;
    boolean isAway;

    public ResidentAvailabilityView(ResidentAccountService residentAccountService, AccountDetailsPresenter accountDetailsPresenter) {
        this.residentAccountService = residentAccountService;
        this.accountDetailsPresenter = accountDetailsPresenter;
        setAvailabilityConfirmDialog = new ConfirmDialog("Confirm change",
                "Tasks currently assigned to you will be transferred to next available room. Are " +
                        "you sure you want change availability status?",
                "Change",
                this::onConfirmChange,
                "Cancel", this::onCancelChange);
        add(getHeading());
        addAvailabilityCheckBox();
    }

    private void addAvailabilityCheckBox() {
        Checkbox isAwayCheckBox = new Checkbox("I am away");
        isAwayCheckBox.setValue(SessionHandler.getLoggedInResidentAccount().isAway());
        Checkbox isBackCheckBox = new Checkbox("I am ready to take tasks");
        isBackCheckBox.setValue(!SessionHandler.getLoggedInResidentAccount().isAway());
        isAwayCheckBox.addValueChangeListener(event -> {
            isAway = isAwayCheckBox.getValue();
            setAvailabilityConfirmDialog.open();
            isBackCheckBox.setValue(!isAway);
        });
        isBackCheckBox.addValueChangeListener(event -> {
            isAway = !isBackCheckBox.getValue();
            setAvailabilityConfirmDialog.open();
            isAwayCheckBox.setValue(isAway);
        });
        add(isAwayCheckBox, isBackCheckBox);
    }

    private void onCancelChange(ConfirmDialog.CancelEvent cancelEvent) {
        setAvailabilityConfirmDialog.close();
    }

    private void onConfirmChange(ConfirmDialog.ConfirmEvent confirmEvent) {
        accountDetailsPresenter.setResidentAwayAndSave(isAway);
        Notification.show(UIStringConstants.getInstance().getAvailabilityStatusChanged());
    }


    private H1 getHeading() {
        return new H1("Change availability status");
    }

}
