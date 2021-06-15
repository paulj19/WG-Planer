package com.wg_planner.views.pre_login.signup.create_floor;

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
import com.wg_planner.backend.utils.code_generator.custom_code_generator.CustomCodeCreator;

public class CreateFloorForm extends FormLayout {
    private Floor floorToCreate;
    private Binder<Floor> floorBinder = new BeanValidationBinder<>(Floor.class);
    private TextField floorName = new TextField("Floor Name/Number", "Enter floor name or number " +
            "to create");
    private IntegerField numberOfRooms = new IntegerField("Number of Rooms", "Enter the number of rooms" +
            " in floor");
    private CreateRoomsView roomsView = new CreateRoomsView();
    private CreateTasksView tasksView = new CreateTasksView();

    Button createFloorButton = new Button("Create Floor");
    Button cancelButton = new Button("Cancel");

    private HorizontalLayout buttonLayout = new HorizontalLayout();

    public CreateFloorForm() {
        addClassName("create-floor-form");
        floorToCreate = new Floor(CustomCodeCreator.getInstance().generateCode(CustomCodeCreator.CodeGenerationPurposes.FLOOR_CODE));
        floorBinder.bindInstanceFields(this);
        numberOfRooms.addValueChangeListener(event -> {
            processIfNumberOfRoomsChanged(event.getValue(), event.getOldValue() != null ? event.getOldValue() : 0);
            addComponentsDown();
        });
        tasksView.addTaskView();
        tasksView.setPadding(false);
        createButtonLayout();
        add(floorName, numberOfRooms, tasksView, buttonLayout);
    }

    private void processIfNumberOfRoomsChanged(int newValue, int oldValue) {
        removeComponentsDown();
        if ((newValue < oldValue) || (newValue == 0)) {
            roomsView.removeRoomsView(oldValue - newValue);
        } else { //covers initial input and when old and new values are same
            roomsView.addRoomView(newValue - oldValue);
        }
    }

    private void addComponentsDown() {
        add(roomsView);
        add(tasksView);
        add(buttonLayout);
    }

    private void removeComponentsDown() {
        remove(roomsView);
        remove(buttonLayout);
        remove(tasksView);
    }

    private void createButtonLayout() {
        createFloorButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        createFloorButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);
//        create_floor.setEnabled(false);

        createFloorButton.addClickListener(event -> validateAndSave());
        cancelButton.addClickListener(event -> fireEvent(new CreateFloorFormEvent.CancelEvent(this, floorToCreate)));
        buttonLayout.add(createFloorButton, cancelButton);
    }

    private void validateAndSave() {
        try {
            floorBinder.writeBean(floorToCreate);
            floorToCreate.setRooms(roomsView.validateAndSave(floorToCreate));
            floorToCreate.setTasks(tasksView.validateAndSave(floorToCreate));
            fireEvent(new CreateFloorFormEvent.SaveEvent(this, floorToCreate));
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
