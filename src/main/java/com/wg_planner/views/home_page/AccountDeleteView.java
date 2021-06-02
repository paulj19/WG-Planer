package com.wg_planner.views.home_page;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UIHandler;
import com.wg_planner.views.utils.UIStringConstants;

public class AccountDeleteView extends VerticalLayout {
    Button deleteButton = new Button("Delete Account");
    FloorService floorService;
    ResidentAccountService residentAccountService;
    TaskService taskService;
    ConfirmDialog deleteConfirmDialog;


    public AccountDeleteView(FloorService floorService,
                             ResidentAccountService residentAccountService, TaskService taskService) {
        this.floorService = floorService;
        this.residentAccountService = residentAccountService;
        this.taskService = taskService;
        deleteConfirmDialog = new ConfirmDialog("Confirm Delete",
                "Are you sure you want to delete your account?",
                "Delete",
                this::onConfirmDelete,
                "Cancel", this::onCancelDelete);
        deleteButton.addClickListener(event ->  deleteConfirmDialog.open());
        add(deleteButton);
    }

    private void onConfirmDelete(ConfirmDialog.ConfirmEvent confirmEvent) {
        residentAccountService.removeResidentAccount(SessionHandler.getLoggedInResidentAccount(),
                floorService, taskService);
        Notification.show(UIStringConstants.getInstance().getAccountDeletedConfirmation());
        UIHandler.getInstance().navigateToLoginPage();
    }

    private void onCancelDelete(ConfirmDialog.CancelEvent cancelEvent) {
        deleteConfirmDialog.close();
    }
}
