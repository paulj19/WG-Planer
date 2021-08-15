package com.wg_planner.views.UnauthorizedPages.create_floor;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
    private TextField floorName = new TextField("Floor Name", "Enter floor name or number");
    private CreateRoomsView roomsView = new CreateRoomsView();
    private CreateTasksView tasksView = new CreateTasksView();

    Button createFloorButton = new Button("Create Floor");
    Button cancelButton = new Button("Cancel");

    private HorizontalLayout buttonLayout = new HorizontalLayout();

    public CreateFloorForm() {
        addClassName("create-floor-form");
        setFieldProperties();
        setResponsiveSteps(new ResponsiveStep("0", 1));
        floorToCreate = new Floor(CustomCodeCreator.getInstance().generateCode(CustomCodeCreator.CodeGenerationPurposes.FLOOR_CODE));
        floorBinder.bindInstanceFields(this);
        roomsView.addRoomsView();
        tasksView.addTaskView();
        createButtonLayout();
        add(floorName, roomsView, tasksView, buttonLayout);
    }
    private void setFieldProperties() {
        floorName.setClearButtonVisible(true);
        floorName.setMaxLength(255);
        floorName.setRequired(true);
    }

    private void createButtonLayout() {
        createFloorButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
//        createFloorButton.addClickShortcut(Key.ENTER);
//        cancelButton.addClickShortcut(Key.ESCAPE);
        buttonLayout.getStyle().set("margin-top", "15px");

        createFloorButton.addClickListener(event -> validateAndSave());
        cancelButton.addClickListener(event -> fireEvent(new CreateFloorFormEvent.CancelEvent(this, floorToCreate)));
        buttonLayout.add(createFloorButton, cancelButton);
    }

    private void validateAndSave() {
        try {
            floorToCreate.setRooms(roomsView.validateAndSave(floorToCreate));
            floorToCreate.setTasks(tasksView.validateAndSave(floorToCreate));
            floorBinder.writeBean(floorToCreate);
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
