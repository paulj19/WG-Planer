package com.wg_planner.views.create_floor;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.RoomService;
import org.springframework.util.Assert;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.wg_planner.views.utils.SessionHandler.saveCreatedFloorToSession;

@Route(value = "create_floor")
@PageTitle("Create Floor | WG Planner")
@CssImport("./styles/views/create-floor/create-floor-view.css")
public class CreateFloorView extends VerticalLayout implements BeforeEnterObserver {

    private CreateFloorForm createFloorForm = new CreateFloorForm();
    private FloorService floorService;
    private RoomService roomService;


    public CreateFloorView(FloorService floorService, RoomService roomService) {
        this.floorService = floorService;
        this.roomService = roomService;
        setWidth("500px");
        setHeight("100%");
        getStyle().set("overflow-y", "auto");
//        setAlignItems(Alignment.CENTER);
//        setJustifyContentMode(JustifyContentMode.CENTER);
        createFloorForm.addListener(CreateFloorForm.CreateFloorFormEvent.SaveEvent.class,
                this::saveFloorAndNavigateToRegister);
        createFloorForm.addListener(CreateFloorForm.CreateFloorFormEvent.CancelEvent.class,
                this::clearCreateFloorForm);
        add(new H1("Create New Floor"), createFloorForm);
    }

    private void saveFloorAndNavigateToRegister(CreateFloorForm.CreateFloorFormEvent.SaveEvent saveEvent) {
        Assert.notNull(saveEvent.getFloorToCreate(), "floor passed to saveFloor event must not be null");
        floorService.save(saveEvent.getFloorToCreate());
        Logger.getLogger(CreateFloorView.class.getName()).log(Level.INFO, "new floor created and " +
                "saved: " + saveEvent.getFloorToCreate().toString());
        Notification.show("Floor created");
        saveCreatedFloorToSession(saveEvent.getFloorToCreate());
        UI.getCurrent().navigate("register/");
    }


    private void clearCreateFloorForm(CreateFloorForm.CreateFloorFormEvent.CancelEvent cancelEvent) {
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
        }
    }
}
