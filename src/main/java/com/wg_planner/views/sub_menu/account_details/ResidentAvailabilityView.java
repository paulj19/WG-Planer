package com.wg_planner.views.sub_menu.account_details;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wg_planner.views.main.MainView;
import com.wg_planner.views.utils.ConfirmationDialog;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UIStringConstants;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

@Route(layout = MainView.class)
@PageTitle("Resident Availability")
public class ResidentAvailabilityView extends VerticalLayout {
    ResidentAvailabilityPresenter residentAvailabilityPresenter;
    private AutowireCapableBeanFactory beanFactory;
    boolean isAway;
    Checkbox isNotReadyCheckBox = new Checkbox("I am not ready to take tasks");
    Checkbox isReadyCheckBox = new Checkbox("I am ready to take tasks");
    ConfirmationDialog confirmationDialogStatusChange;

    public ResidentAvailabilityView(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        residentAvailabilityPresenter = new ResidentAvailabilityPresenter();
        beanFactory.autowireBean(residentAvailabilityPresenter);
        confirmationDialogStatusChange = new ConfirmationDialog("Confirm Availability Status Change",
                "Are you sure you want to change availability status?",
                "Confirm",
                "Cancel", SessionHandler.getLoggedInResidentAccount());
        confirmationDialogStatusChange.addListener(ConfirmationDialog.ConfirmationDialogEvent.ConfirmEvent.class, this::onConfirm);
        confirmationDialogStatusChange.addListener(ConfirmationDialog.ConfirmationDialogEvent.CancelEvent.class, this::onCancel);
        addAvailabilityCheckBox();
    }

    private <T extends ComponentEvent<?>> void onConfirm(ConfirmationDialog.ConfirmationDialogEvent.ConfirmEvent confirmEvent) {
        residentAvailabilityPresenter.setResidentAwayStatusAndSave(!SessionHandler.getLoggedInResidentAccount().isAway());
        isNotReadyCheckBox.setValue(SessionHandler.getLoggedInResidentAccount().isAway());
        isReadyCheckBox.setValue(!SessionHandler.getLoggedInResidentAccount().isAway());
        Notification.show(UIStringConstants.getInstance().getAvailabilityStatusChanged());
    }

    private <T extends ComponentEvent<?>> void onCancel(ConfirmationDialog.ConfirmationDialogEvent.CancelEvent cancelEvent) {
        isNotReadyCheckBox.setValue(SessionHandler.getLoggedInResidentAccount().isAway());
        isReadyCheckBox.setValue(!SessionHandler.getLoggedInResidentAccount().isAway());
        confirmationDialogStatusChange.close();
    }

    private void addAvailabilityCheckBox() {
        isNotReadyCheckBox.setValue(SessionHandler.getLoggedInResidentAccount().isAway());
        isReadyCheckBox.setValue(!SessionHandler.getLoggedInResidentAccount().isAway());
        isNotReadyCheckBox.addValueChangeListener(event -> {
            if(!SessionHandler.getLoggedInResidentAccount().isAway()) {
                confirmationDialogStatusChange.open();
            }
        });
        isReadyCheckBox.addValueChangeListener(event -> {
                confirmationDialogStatusChange.open();
            if(SessionHandler.getLoggedInResidentAccount().isAway()) {
            }
        });
        add(isNotReadyCheckBox, isReadyCheckBox);
    }
}
