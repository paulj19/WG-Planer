package com.wg_planner.views.home_page;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.views.utils.AccountDetailsHelper;

public class ResidentAvailabilityView extends VerticalLayout {
    ResidentAccountService residentAccountService;
    ConfirmDialog setAvailabilityConfirmDialog;
    HomePagePresenter homePagePresenter;
    boolean isAway;

    public ResidentAvailabilityView(ResidentAccountService residentAccountService, HomePagePresenter homePagePresenter) {
        this.residentAccountService = residentAccountService;
        this.homePagePresenter = homePagePresenter;
        setAvailabilityConfirmDialog = new ConfirmDialog("Confirm change",
                "Are you sure you want to  in availability status?", "Change", this::onConfirmChange,
                "Cancel", this::onCancelChange);
        add(getHeading());
        addAvailabilityCheckBox();
    }

    private void addAvailabilityCheckBox() {
        Checkbox isAwayCheckBox = new Checkbox("I am away");
        isAwayCheckBox.setValue(AccountDetailsHelper.getUserResidentAccount(residentAccountService).isAway());
        Checkbox isBackCheckBox = new Checkbox("I am ready to take tasks");
        isBackCheckBox.setValue(!AccountDetailsHelper.getUserResidentAccount(residentAccountService).isAway());
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
        homePagePresenter.setResidentAwayAndSave(isAway);
        //TODO dialog box, your things have been changed
    }


    private H1 getHeading() {
        return new H1("Change availability status");
    }

}
