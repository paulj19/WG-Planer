package com.wg_planner.views.UnauthorizedPages.create_floor;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.RoomService;
import com.wg_planner.views.UnauthorizedPages.UnauthorizedPagesView;
import com.wg_planner.views.utils.UINavigationHandler;
import org.springframework.util.Assert;

import java.util.logging.Level;
import java.util.logging.Logger;

@Route(value = "create_floor", layout = UnauthorizedPagesView.class)
@PageTitle("Create Floor | WG Planner")
@CssImport("./styles/views/create-floor/create-floor-view.css")
public class CreateFloorView extends VerticalLayout {
    private CreateFloorForm createFloorForm = new CreateFloorForm();
    private FloorService floorService;
    private RoomService roomService;
    private H1 heading = new H1("Create New Floor");

    public CreateFloorView(FloorService floorService, RoomService roomService) {
        this.floorService = floorService;
        this.roomService = roomService;
        heading.addClassName("create-floor-heading");
        createFloorForm.addListener(CreateFloorForm.CreateFloorFormEvent.SaveEvent.class,
                this::saveFloorAndNavigateToRegister);
        createFloorForm.addListener(CreateFloorForm.CreateFloorFormEvent.CancelEvent.class,
                this::clearCreateFloorForm);
        add(heading, createFloorForm);
    }

    private void saveFloorAndNavigateToRegister(CreateFloorForm.CreateFloorFormEvent.SaveEvent saveEvent) {
        Assert.notNull(saveEvent.getFloorToCreate(), "floor passed to saveFloor event must not be null");
        floorService.save(saveEvent.getFloorToCreate());
        Logger.getLogger(CreateFloorView.class.getName()).log(Level.INFO, "new floor created and " +
                "saved: " + saveEvent.getFloorToCreate().toString());
        Notification.show("Floor created");
        UINavigationHandler.getInstance().navigateToRegisterPageParamFloorId(saveEvent.getFloorToCreate().getId());
    }

    private void clearCreateFloorForm(CreateFloorForm.CreateFloorFormEvent.CancelEvent cancelEvent) {
        UINavigationHandler.getInstance().navigateToLoginPage();
    }
}
