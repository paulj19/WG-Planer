package com.wg_planner.views.create_floor;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import com.wg_planner.backend.entity.Floor;

public class CreateFloorForm extends FormLayout {
    private Floor floor = new Floor();
    private Binder<Floor> floorBinder = new BeanValidationBinder<>(Floor.class);
    private TextField floorName = new TextField("Floor Name/Number", "Enter floor name or number " +
            "to create");
    private IntegerField numberOfRooms = new IntegerField("Number of Rooms", "Enter the number of rooms" +
            " in floor");
    private CreateRoomsView roomsView;

    Button create_floor = new Button("Create Floor");
    Button cancel = new Button("Cancel");

    private HorizontalLayout buttonLayout = new HorizontalLayout();

    public CreateFloorForm() {
        addClassName("create-floor-form");
        floorBinder.bindInstanceFields(this);
        numberOfRooms.addValueChangeListener(event -> {
            processIfNumberOfRoomsChanged(event.getValue(), event.getOldValue());
            add(roomsView);
            add(buttonLayout);
        });
        setWidth("500px");
        createButtonLayout();
        add(floorName, numberOfRooms, buttonLayout);
    }

    private void processIfNumberOfRoomsChanged(Integer newValue, Integer oldValue) {
        if (roomsView != null && oldValue != null) { //if the number of rooms is changed
            remove(roomsView);
            remove(buttonLayout);
            if (newValue > oldValue) {
                roomsView.addRoomView(newValue - oldValue);
            } else if (newValue < oldValue) {
                roomsView.removeRoomsView(oldValue - newValue);
            }
        } else {
            roomsView = new CreateRoomsView(newValue);
        }
    }

    private void createButtonLayout() {
        create_floor.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        create_floor.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);
//        create_floor.setEnabled(false);

        create_floor.addClickListener(event -> validateAndSave());
        cancel.addClickListener(event -> fireEvent(new CreateFloorFormEvent.CancelEvent(this, floor)));
        buttonLayout.add(create_floor, cancel);
    }

    private void validateAndSave() {
        try {
            floorBinder.writeBean(floor);
            floor.setRooms(roomsView.validateAndSave(floor));
            fireEvent(new CreateFloorFormEvent.SaveEvent(this, floor));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class CreateFloorFormEvent extends ComponentEvent<CreateFloorForm> {
        private Floor floorToCreate;

        protected CreateFloorFormEvent(CreateFloorForm source, Floor floorToCreate) {
            super(source, false);
            this.floorToCreate = floorToCreate;
        }

        public Floor getFloorToCreate() {
            return floorToCreate;
        }

        public void setFloorToCreate(Floor floorToCreate) {
            this.floorToCreate = floorToCreate;
        }

        public static class SaveEvent extends CreateFloorForm.CreateFloorFormEvent {
            SaveEvent(CreateFloorForm source, Floor selectedFloor) {
                super(source, selectedFloor);
            }
        }

        public static class CancelEvent extends CreateFloorForm.CreateFloorFormEvent {
            CancelEvent(CreateFloorForm source, Floor selectedFloor) {
                super(source, selectedFloor);
            }
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
