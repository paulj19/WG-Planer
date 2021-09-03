package com.wg_planner.views.UnauthorizedPages.create_floor;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.RoomService;
import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.UnauthorizedPages.UnauthorizedPagesView;
import com.wg_planner.views.utils.UINavigationHandler;
import com.wg_planner.views.utils.UINotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


@Route(value = "create_floor", layout = UnauthorizedPagesView.class)
@PageTitle("Create Floor | WG Planner")
@CssImport("./styles/views/create-floor/create-floor-view.css")
public class CreateFloorView extends VerticalLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateFloorView.class);
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
        LOGGER.info(LogHandler.getTestRun(), "floor creation view called");
    }

    private void saveFloorAndNavigateToRegister(CreateFloorForm.CreateFloorFormEvent.SaveEvent saveEvent) {
        Assert.notNull(saveEvent.getFloorToCreate(), "floor passed to saveFloor event must not be null");
        floorService.save(saveEvent.getFloorToCreate());
        UINotificationMessage.notify("Floor created");
        LOGGER.info("new floor created and saved. Floor details: {}", saveEvent.getFloorToCreate().toString());
        UINavigationHandler.getInstance().navigateToRegisterPageParamFloorId(saveEvent.getFloorToCreate().getId());
    }

    private void clearCreateFloorForm(CreateFloorForm.CreateFloorFormEvent.CancelEvent cancelEvent) {
        LOGGER.info(LogHandler.getTestRun(), "floor creation cancelled");
        UINavigationHandler.getInstance().navigateToLoginPage();
    }
}
