package com.wg_planner.views.sub_menu.account_details;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.utils.AccountDetailsHelper;
import com.wg_planner.views.utils.ConfirmationDialog;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UIStringConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.vaadin.flow.component.notification.Notification.Position.BOTTOM_STRETCH;

public class AccountDeleteView extends VerticalLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountDeleteView.class);
    Button deleteButton = new Button("Delete Account");
    FloorService floorService;
    ResidentAccountService residentAccountService;
    TaskService taskService;
    ConfirmationDialog deleteConfirmDialog;

    public AccountDeleteView(FloorService floorService,
                             ResidentAccountService residentAccountService, TaskService taskService) {
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Account Delete page view selected.",
                SessionHandler.getLoggedInResidentAccount().getId());
        this.floorService = floorService;
        this.residentAccountService = residentAccountService;
        this.taskService = taskService;
        deleteConfirmDialog = new ConfirmationDialog("Confirm Delete",
                "Are you sure you want to delete your account?",
                "Delete",
                "Cancel", SessionHandler.getLoggedInResidentAccount());
        deleteConfirmDialog.addListener(ConfirmationDialog.ConfirmationDialogEvent.ConfirmEvent.class,
                this::onConfirmDeleteAccount);
        deleteConfirmDialog.addListener(ConfirmationDialog.ConfirmationDialogEvent.CancelEvent.class,
                this::onCancelDelete);
        deleteButton.addClickListener(event -> deleteConfirmDialog.open());
        add(deleteButton);
    }

    private void onConfirmDeleteAccount(ConfirmationDialog.ConfirmationDialogEvent.ConfirmEvent confirmEvent) {
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. on confirm resident account delete",
                SessionHandler.getLoggedInResidentAccount().getId());
        residentAccountService.removeResidentAccount(residentAccountService.getResidentAccountById(SessionHandler.getLoggedInResidentAccount().getId()),
                floorService, taskService);//residentAccount could be outdated thus loading from DB
        Notification.show(UIStringConstants.getInstance().getAccountDeletedConfirmation(), 10000, BOTTOM_STRETCH);
        AccountDetailsHelper.logoutAndNavigateToLoginPage();
    }

    private void onCancelDelete(ConfirmationDialog.ConfirmationDialogEvent.CancelEvent cancelEvent) {
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. on cancel resident account delete",
                SessionHandler.getLoggedInResidentAccount().getId());
        deleteConfirmDialog.close();
    }
}
