package com.wg_planner.views.sub_menu.floor_details;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
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
        roomsInFloor.forEach(room -> roomsInFloorLayout.add(getRoomLayout(room)));
        return roomsInFloorLayout;
    }

    private Component getRoomLayout(Room room) {
        HorizontalLayout roomLayout = new HorizontalLayout();
        roomLayout.setHeightFull();
        roomLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        //        roomLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        TextField roomNameField = new TextField("Room Name");
        roomNameField.setReadOnly(true);
        roomNameField.setValue(room.getRoomName());
        roomLayout.add(roomNameField);
        Checkbox isOccupiedCheckBox = new Checkbox("Room Occupied");
        isOccupiedCheckBox.setValue(room.isOccupied());
        isOccupiedCheckBox.setReadOnly(true);
        isOccupiedCheckBox.getStyle().set("padding-top", "32px");
        roomLayout.add(isOccupiedCheckBox);
        if (room.getResidentAccount() != null) {
            TextField residentNameField = new TextField("Resident Name");
            residentNameField.setReadOnly(true);
            residentNameField.setValue(room.getResidentAccount().getFirstName() + " " + room.getResidentAccount().getLastName());
            roomLayout.add(residentNameField);
            Checkbox isResidentAwayCheckBox = new Checkbox("Resident Away");
            isResidentAwayCheckBox.setValue(room.getResidentAccount().isAway());
            isResidentAwayCheckBox.setReadOnly(true);
            isResidentAwayCheckBox.getStyle().set("padding-top", "32px");
            roomLayout.add(isResidentAwayCheckBox);
        }
        return roomLayout;
    }
}
