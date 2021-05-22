package com.wg_planner.views.create_floor;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.RoomService;

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
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        createFloorForm.addListener(CreateFloorForm.CreateFloorFormEvent.SaveEvent.class,
                this::saveFloor);
        createFloorForm.addListener(CreateFloorForm.CreateFloorFormEvent.CancelEvent.class,
                this::clearCreateFloorForm);
        add(new H1("Create New Floor"), createFloorForm);
    }

    private void saveFloor(CreateFloorForm.CreateFloorFormEvent.SaveEvent saveEvent) {
        floorService.save(saveEvent.getFloorToCreate());
        saveEvent.getFloorToCreate().getRooms().forEach(room -> roomService.save(room));
    }

    private void clearCreateFloorForm(CreateFloorForm.CreateFloorFormEvent.CancelEvent cancelEvent) {

    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
        }
    }
}
