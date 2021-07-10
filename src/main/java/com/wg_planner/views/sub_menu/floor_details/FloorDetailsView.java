package com.wg_planner.views.sub_menu.floor_details;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.views.create_floor.CreateTaskView;
import com.wg_planner.views.main.MainView;
import com.wg_planner.views.register.admission.AdmitNewResidentView;
import com.wg_planner.views.tasks.assign_task.AssignTaskView;
import com.wg_planner.views.utils.SessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.util.List;

import static com.vaadin.flow.component.notification.Notification.Position.BOTTOM_STRETCH;

@Route(value = "floor_details", layout = MainView.class)
@PageTitle("Floor Details")
@CssImport("./styles/views/tasks/tasks-view.css")
public class FloorDetailsView extends VerticalLayout {
    private AutowireCapableBeanFactory beanFactory;
    private FloorDetailsPresenter floorDetailsPresenter;
    private Component tasksInFloorComponent;
    private Accordion tasksAccordion;

    @Autowired
    public FloorDetailsView(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        floorDetailsPresenter = new FloorDetailsPresenter();
        beanFactory.autowireBean(floorDetailsPresenter);
        floorDetailsPresenter.init(this);
    }

    public void addFloorName(String floorName) {
        addReadOnlyTextField("Floor Name", floorName);
    }

    public void addFloorCode(String floorCode) {
        addReadOnlyTextField("Floor Code", floorCode);
    }

    private void addReadOnlyTextField(String fieldLabel, String fieldValue) {
        TextField floorCodeField = new TextField(fieldLabel);
        floorCodeField.setReadOnly(true);
        floorCodeField.setValue(fieldValue);
        add(floorCodeField);
    }

    public void addRoomsInFloor(List<Room> roomsInFloor) {
        Accordion roomsAccordion = new Accordion();
        roomsAccordion.add("Rooms", getRoomsInFloorLayout(roomsInFloor));
        roomsAccordion.close();
        add(roomsAccordion);
    }

    public void addNewRoomTextField() {
        Accordion admitNewRoomAccordion = new Accordion();
        admitNewRoomAccordion.add("Add New Resident", new AdmitNewResidentView(beanFactory));
        admitNewRoomAccordion.close();
        add(admitNewRoomAccordion);
    }

    private Component getRoomsInFloorLayout(List<Room> roomsInFloor) {
        VerticalLayout roomsInFloorLayout = new VerticalLayout();
        roomsInFloor.forEach(room -> roomsInFloorLayout.add(getRoomLayout(room.getRoomName())));
        return roomsInFloorLayout;
    }

    private Component getRoomLayout(String roomName) {
        return new Span(roomName);
    }

    public void addTasksInFloor() {
        tasksAccordion = new Accordion();
        getAndAddTasksInFloorLayout();
        tasksAccordion.close();
        add(tasksAccordion);
    }

    void refreshTasksInFloor() {
        tasksAccordion.remove(tasksInFloorComponent);
        getAndAddTasksInFloorLayout();
    }

    private Component getAndAddTasksInFloorLayout() {
        VerticalLayout tasksInFloorLayout = new VerticalLayout();
        Button addTaskButton = new Button("Add Task");
        tasksInFloorLayout.setWidthFull();
        floorDetailsPresenter.getTasksInFloor().forEach(task -> tasksInFloorLayout.add(getTaskLayout(task)));
        addTaskButton.addClickListener(event -> {
            tasksInFloorLayout.replace(addTaskButton, getNewTaskCreateLayout());
        });
        tasksInFloorLayout.add(addTaskButton);
        tasksInFloorComponent = tasksInFloorLayout;
        tasksAccordion.add("Tasks", tasksInFloorLayout);
        return tasksInFloorLayout;
    }

    private Component getNewTaskCreateLayout() {
        HorizontalLayout newTaskCreateLayout = new HorizontalLayout();
        CreateTaskView createTaskView = new CreateTaskView();
        Button saveNewTaskButton = new Button("Save");
        Button cancelAddTaskButton = new Button("Cancel");
        saveNewTaskButton.addClickListener(event -> {
            floorDetailsPresenter.saveNewlyCreatedTask(createTaskView.validateTask(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor()));
            refreshTasksInFloor();
        });
        cancelAddTaskButton.addClickListener(event -> {
            remove(newTaskCreateLayout);
            refreshTasksInFloor();
        });
        newTaskCreateLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        newTaskCreateLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        newTaskCreateLayout.add(createTaskView, saveNewTaskButton, cancelAddTaskButton);
        return newTaskCreateLayout;
    }

    private Component getTaskLayout(Task task) {
        HorizontalLayout taskLayout = new HorizontalLayout();
        taskLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        taskLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        if (floorDetailsPresenter.isObjectDeletable(task.getId())) {
            Button deleteTask = new Button("Delete");
            Button resetTask = new Button(task.getAssignedRoom() == null ? "Assign" : "Reset");
            deleteTask.addClickListener(event -> fireEvent(new TaskUpdateEvent.DeleteTaskEvent(this, task)));
            resetTask.addClickListener(event -> UI.getCurrent().navigate(AssignTaskView.class, task.getId().toString()));
            taskLayout.add(new Span(task.getTaskName()), deleteTask, resetTask);
        } else {
            taskLayout.add(new Span(task.getTaskName()));
        }
        return taskLayout;
    }

    void notify(String notificationMessage) {
        Notification.show(notificationMessage, 10000, BOTTOM_STRETCH);
    }

    public static abstract class TaskUpdateEvent extends ComponentEvent<FloorDetailsView> {
        private Task task;

        protected TaskUpdateEvent(FloorDetailsView source, Task task) {
            super(source, false);
            this.task = task;
        }

        public Task getTask() {
            return task;
        }

        public static class DeleteTaskEvent extends TaskUpdateEvent {
            DeleteTaskEvent(FloorDetailsView source, Task task) {
                super(source, task);
            }
        }

    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
