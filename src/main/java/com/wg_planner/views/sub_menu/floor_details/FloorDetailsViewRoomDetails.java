package com.wg_planner.views.sub_menu.floor_details;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.views.register.admission.AdmitNewResidentView;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.util.List;

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
        admitNewRoomAccordion.add("Add New Resident", new AdmitNewResidentView(beanFactory));
        admitNewRoomAccordion.close();
        return admitNewRoomAccordion;
    }

    private Component getRoomsInFloorLayout(List<Room> roomsInFloor) {
        VerticalLayout roomsInFloorLayout = new VerticalLayout();
        roomsInFloor.forEach(room -> roomsInFloorLayout.add(getRoomLayout(room.getRoomName())));
        return roomsInFloorLayout;
    }

    private Component getRoomLayout(String roomName) {
        return new Span(roomName);
    }
}
