package com.wg_planner.views.sub_menu.account_details;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.views.utils.*;

import static com.vaadin.flow.component.notification.Notification.Position.BOTTOM_STRETCH;

public class AccountDeleteView extends VerticalLayout {
    Button deleteButton = new Button("Delete Account");
    FloorService floorService;
    ResidentAccountService residentAccountService;
    TaskService taskService;
    ConfirmationDialog deleteConfirmDialog;


    public AccountDeleteView(FloorService floorService,
                             ResidentAccountService residentAccountService, TaskService taskService) {
        this.floorService = floorService;
        this.residentAccountService = residentAccountService;
        this.taskService = taskService;
        deleteConfirmDialog = new ConfirmationDialog("Confirm Delete",
                "Are you sure you want to delete your account?",
                "Delete",
                "Cancel", SessionHandler.getLoggedInResidentAccount());
        deleteConfirmDialog.addListener(ConfirmationDialog.ConfirmationDialogEvent.ConfirmEvent.class, this::onConfirmDelete);
        deleteConfirmDialog.addListener(ConfirmationDialog.ConfirmationDialogEvent.CancelEvent.class, this::onCancelDelete);
        deleteButton.addClickListener(event -> deleteConfirmDialog.open());
        add(deleteButton);
    }

    private void onConfirmDelete(ConfirmationDialog.ConfirmationDialogEvent.ConfirmEvent confirmEvent) {
        residentAccountService.removeResidentAccount(SessionHandler.getLoggedInResidentAccount(),
                floorService, taskService);
        Notification.show(UIStringConstants.getInstance().getAccountDeletedConfirmation(), 10000, BOTTOM_STRETCH);
        AccountDetailsHelper.logoutAndNavigateToLoginPage();
    }

    private void onCancelDelete(ConfirmationDialog.ConfirmationDialogEvent.CancelEvent cancelEvent) {
        deleteConfirmDialog.close();
    }
}
