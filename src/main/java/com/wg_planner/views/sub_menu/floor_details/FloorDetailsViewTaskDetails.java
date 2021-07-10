package com.wg_planner.views.sub_menu.floor_details;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.views.create_floor.CreateTaskView;
import com.wg_planner.views.tasks.assign_task.AssignTaskView;
import com.wg_planner.views.utils.SessionHandler;

public class FloorDetailsViewTaskDetails {
    private Accordion tasksAccordion;
    private Component tasksInFloorComponent;
    private FloorDetailsView floorDetailsView;
    private FloorDetailsPresenter floorDetailsPresenter;

    private FloorDetailsViewTaskDetails() {
    }

    public FloorDetailsViewTaskDetails(FloorDetailsView floorDetailsView, FloorDetailsPresenter floorDetailsPresenter) {
        this.floorDetailsView = floorDetailsView;
        this.floorDetailsPresenter = floorDetailsPresenter;
    }

    Component addTasksInFloor() {
        tasksAccordion = new Accordion();
        getAndAddTasksInFloorLayout();
        tasksAccordion.close();
        return tasksAccordion;
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
            floorDetailsView.remove(newTaskCreateLayout);
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
        taskLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        if (floorDetailsPresenter.isObjectDeletable(task.getId())) {
            Button deleteTask = new Button("Delete");
            Button resetTask = new Button(task.getAssignedRoom() == null ? "Assign" : "Reset");
            deleteTask.addClickListener(event -> floorDetailsView.fireEvent(new FloorDetailsView.TaskUpdateEvent.DeleteTaskEvent(floorDetailsView, task)));
            resetTask.addClickListener(event -> UI.getCurrent().navigate(AssignTaskView.class,
                    task.getId().toString()));
            taskLayout.add(new Span(task.getTaskName()), deleteTask, resetTask);
        } else {
            taskLayout.add(new Span(task.getTaskName()));
        }
        return taskLayout;
    }
}
