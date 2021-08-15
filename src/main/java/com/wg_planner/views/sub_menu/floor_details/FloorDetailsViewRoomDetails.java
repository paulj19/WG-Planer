package com.wg_planner.views.sub_menu.floor_details;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.views.UnauthorizedPages.register.admission.AdmitNewResidentView;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.util.List;

@CssImport(value = "./styles/views/floor-details/floor-details-view.css")
public class FloorDetailsViewRoomDetails {
    private AutowireCapableBeanFactory beanFactory;

    private FloorDetailsViewRoomDetails() {
    }

    public FloorDetailsViewRoomDetails(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Component addRoomsInFloor(List<Room> roomsInFloor) {
        Accordion roomsAccordion = new Accordion();
        roomsAccordion.add("Rooms", getRoomsInFloorLayout(roomsInFloor));
        roomsAccordion.close();
        return roomsAccordion;
    }

    public Component addAdmitNewResidentView() {
        Accordion admitNewRoomAccordion = new Accordion();
        Span helperText = new Span("Admission code is used to add new residents to the floor. At the end of the registration process the new resident gets an" +
                " admission code which is to be entered here to complete the admission process.");
        admitNewRoomAccordion.setWidthFull();
        AdmitNewResidentView admitNewResidentView = new AdmitNewResidentView(beanFactory);
        admitNewResidentView.addClassName("floor-view-admit-new-resident");
        admitNewRoomAccordion.add("Add New Resident", new VerticalLayout(helperText, admitNewResidentView));
        admitNewRoomAccordion.close();
        return admitNewRoomAccordion;
    }

    private Component getRoomsInFloorLayout(List<Room> roomsInFloor) {
        Grid<Room> roomListGrid = new Grid<>();
        roomListGrid.addClassName("room-details-grid");
        roomListGrid.setItems(roomsInFloor);
        Div roomNameHeading = new Div();
        roomNameHeading.getElement().setProperty("innerHTML", "Room <br />Name");

        roomListGrid.addColumn(Room::getRoomName).setHeader(roomNameHeading).setKey("Room Name");
        roomListGrid.addColumn(new ComponentRenderer<>(room -> {
            if (room.getResidentAccount() != null) {
                return new Span(room.getResidentAccount().getFullName());
            } else {
                return new Span("Not Occupied");
            }
        })).setHeader("Resident Name").setKey("Resident Name");
        Div residentPresentHeading = new Div();
        residentPresentHeading.getElement().setProperty("innerHTML", "Resident <br />Present");
        residentPresentHeading.addClassName("resident-present-heading");
        roomListGrid.addColumn(new ComponentRenderer<>(room -> {
            if (room.getResidentAccount() != null) {
                Checkbox isAwayCheckBox = new Checkbox(!room.getResidentAccount().isAway());
                isAwayCheckBox.setReadOnly(true);
                return isAwayCheckBox;
            } else {
                return new Span("-");
            }
        })).setHeader(residentPresentHeading).setKey("Resident Present");
        addGridStyle(roomListGrid);
        return roomListGrid;
    }

    private void addGridStyle(Grid<Room> roomListGrid) {
        //        roomListGrid.getStyle().set("column-gap", "10px");
        roomListGrid.getColumnByKey("Room Name").setWidth("10vw");
        //        roomListGrid.getColumnByKey("Room Occupied").setWidth("15px");
        roomListGrid.getColumnByKey("Resident Name").setAutoWidth(true);
        roomListGrid.getColumnByKey("Resident Present").setWidth("3vw");
    }

}
