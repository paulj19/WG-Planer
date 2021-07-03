package com.wg_planner.views.sub_menu.floor_details;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.views.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import java.util.List;

@Route(value = "floor_details", layout = MainView.class)
@PageTitle("Floor Details")
@CssImport("./styles/views/tasks/tasks-view.css")
public class FloorDetailsView extends VerticalLayout {
    private AutowireCapableBeanFactory beanFactory;
    private FloorDetailsPresenter floorDetailsPresenter;

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

    private Component getRoomsInFloorLayout(List<Room> roomsInFloor) {
        VerticalLayout roomsInFloorLayout = new VerticalLayout();
        roomsInFloor.forEach(room -> roomsInFloorLayout.add(getRoomLayout(room.getRoomName())));
        return roomsInFloorLayout;
    }

    private Component getRoomLayout(String roomName) {
        return new Span(roomName);
    }

    public void addTasksInFloor(List<Task> tasksInFloor) {
        Accordion tasksAccordion = new Accordion();
        tasksAccordion.add("Tasks", getTasksInFloorLayout(tasksInFloor));
        tasksAccordion.close();
        add(tasksAccordion);
    }

    private Component getTasksInFloorLayout(List<Task> tasksInFloor) {
        VerticalLayout tasksInFloorLayout = new VerticalLayout();
        tasksInFloorLayout.setWidthFull();

        tasksInFloor.forEach(task -> tasksInFloorLayout.add(getTaskLayout(task)));
        return tasksInFloorLayout;
    }

    private Component getTaskLayout(Task task) {
        HorizontalLayout taskLayout = new HorizontalLayout();
        taskLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        taskLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        Button deleteTask = new Button("Delete");
        deleteTask.addClickListener(event -> fireEvent(new TaskUpdateEvent.DeleteTaskEvent(this, task)));
        taskLayout.add(new Span(task.getTaskName()), deleteTask);
        return taskLayout;
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
