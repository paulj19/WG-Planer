package com.wg_planner.views.sub_menu.account_details;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.data.renderer.TextRenderer;
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
    RadioButtonGroup<Boolean> isResidentPresent = new RadioButtonGroup<>();
    ConfirmationDialog confirmationDialogStatusChange;

    public ResidentAvailabilityView(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        residentAvailabilityPresenter = new ResidentAvailabilityPresenter();
        beanFactory.autowireBean(residentAvailabilityPresenter);
        isResidentPresent.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        isResidentPresent.setItems(Boolean.TRUE, Boolean.FALSE);
        isResidentPresent.setRenderer(new TextRenderer<>(isAway -> {
            if (isAway) {
                return "I am not ready to take tasks";
            } else {
                return "I am ready to take tasks";
            }
        }));
        isResidentPresent.setValue(SessionHandler.getLoggedInResidentAccount().isAway());
        isResidentPresent.addValueChangeListener(event -> {
            confirmationDialogStatusChange.open();
        });
        add(isResidentPresent);
        confirmationDialogStatusChange = new ConfirmationDialog("Confirm Availability Status Change",
                "Are you sure you want to change availability status?",
                "Confirm",
                "Cancel", SessionHandler.getLoggedInResidentAccount());
        confirmationDialogStatusChange.addListener(ConfirmationDialog.ConfirmationDialogEvent.ConfirmEvent.class,
                this::onConfirm);
        confirmationDialogStatusChange.addListener(ConfirmationDialog.ConfirmationDialogEvent.CancelEvent.class,
                this::onCancel);
        confirmationDialogStatusChange.addDialogCloseActionListener(event -> {
            isResidentPresent.setValue(SessionHandler.getLoggedInResidentAccount().isAway());
            confirmationDialogStatusChange.close();
        });
    }

    private <T extends ComponentEvent<?>> void onConfirm(ConfirmationDialog.ConfirmationDialogEvent.ConfirmEvent confirmEvent) {
        residentAvailabilityPresenter.setResidentAwayStatusAndSave(isResidentPresent.getValue());
        Notification.show(UIStringConstants.getInstance().getAvailabilityStatusChanged());
    }

    private <T extends ComponentEvent<?>> void onCancel(ConfirmationDialog.ConfirmationDialogEvent.CancelEvent cancelEvent) {
        isResidentPresent.setValue(SessionHandler.getLoggedInResidentAccount().isAway());
        confirmationDialogStatusChange.close();
    }
}
