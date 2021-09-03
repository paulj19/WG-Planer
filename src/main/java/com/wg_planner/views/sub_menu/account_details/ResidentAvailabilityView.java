package com.wg_planner.views.sub_menu.account_details;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.main.MainView;
import com.wg_planner.views.utils.ConfirmationDialog;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UINotificationMessage;
import com.wg_planner.views.utils.UIStringConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

@Route(layout = MainView.class)
@PageTitle("Resident Availability")
public class ResidentAvailabilityView extends VerticalLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResidentAvailabilityView.class);
    private ResidentAvailabilityPresenter residentAvailabilityPresenter;
    private ResidentAccountService residentAccountService;
    private AutowireCapableBeanFactory beanFactory;
    private RadioButtonGroup<Boolean> isResidentPresent = new RadioButtonGroup<>();
    private ConfirmationDialog confirmationDialogStatusChange;

    public ResidentAvailabilityView(AutowireCapableBeanFactory beanFactory,
                                    ResidentAccountService residentAccountService) {
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. ResidentAvailabilityView selected.",
                SessionHandler.getLoggedInResidentAccount().getId());
        this.beanFactory = beanFactory;
        this.residentAccountService = residentAccountService;
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
        isResidentPresent.setValue(residentAccountService.getResidentAccountById(SessionHandler.getLoggedInResidentAccount().getId()).isAway());
        isResidentPresent.addValueChangeListener(event -> {
            confirmationDialogStatusChange.open();
        });
        add(isResidentPresent);
        confirmationDialogStatusChange = new ConfirmationDialog("Confirm Availability Status Change",
                "If you are not ready to take tasks(if any), all your tasks will be transferred to the next available " +
                        "resident. Are you sure you want to change availability status?",
                "Confirm",
                "Cancel", SessionHandler.getLoggedInResidentAccount());
        confirmationDialogStatusChange.addListener(ConfirmationDialog.ConfirmationDialogEvent.ConfirmEvent.class,
                this::onConfirm);
        confirmationDialogStatusChange.addListener(ConfirmationDialog.ConfirmationDialogEvent.CancelEvent.class,
                this::onCancel);
        confirmationDialogStatusChange.addDialogCloseActionListener(event -> {
            isResidentPresent.setValue(residentAccountService.getResidentAccountById(SessionHandler.getLoggedInResidentAccount().getId()).isAway());
            confirmationDialogStatusChange.close();
        });
    }

    private <T extends ComponentEvent<?>> void onConfirm(ConfirmationDialog.ConfirmationDialogEvent.ConfirmEvent confirmEvent) {
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Confirm set away status to {}.",
                SessionHandler.getLoggedInResidentAccount().getId(), isResidentPresent.getValue());
        residentAvailabilityPresenter.setResidentAwayStatusAndSave(isResidentPresent.getValue());
        UINotificationMessage.notify(UIStringConstants.getInstance().getAvailabilityStatusChanged());
    }

    private <T extends ComponentEvent<?>> void onCancel(ConfirmationDialog.ConfirmationDialogEvent.CancelEvent cancelEvent) {
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Cancel set away status.",
                SessionHandler.getLoggedInResidentAccount().getId());
        isResidentPresent.setValue(residentAccountService.getResidentAccountById(SessionHandler.getLoggedInResidentAccount().getId()).isAway());
        confirmationDialogStatusChange.close();
    }
}
