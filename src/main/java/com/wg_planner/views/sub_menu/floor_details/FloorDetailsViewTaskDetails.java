package com.wg_planner.views.sub_menu.floor_details;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.views.UnauthorizedPages.create_floor.CreateTaskView;
import com.wg_planner.views.tasks.assign_task.AssignTaskView;
import com.wg_planner.views.utils.ConfirmationDialog;
import com.wg_planner.views.utils.SessionHandler;

@CssImport(value = "./styles/views/floor-details/floor-details-view-task-details.css")
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
        tasksInFloorLayout.addClassName("floor-details-tasks-layout");
        Button addTaskButton = new Button("Add Task");
        addTaskButton.addClassName("floor-details-add-task-button");
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
        newTaskCreateLayout.addClassName("new-task-layout");
        CreateTaskView createTaskView = new CreateTaskView();
        createTaskView.getTaskNameTextField().setWidth("46vw");
        createTaskView.getTaskNameTextField().getStyle().set("padding-top", "0");
        Button saveNewTaskButton = new Button("Save");
        addButtonClass(saveNewTaskButton);
        saveNewTaskButton.getStyle().set("font-size", "0.9em");
//        saveNewTaskButton.getStyle().set("font-size", "0.8em");
        Button cancelAddTaskButton = new Button("Cancel");
        addButtonClass(cancelAddTaskButton);
        cancelAddTaskButton.getStyle().set("font-size", "0.85em");
        saveNewTaskButton.addClickListener(event -> {
            floorDetailsPresenter.saveNewlyCreatedTask(createTaskView.validateTask(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor()));
            refreshTasksInFloor();
        });
        cancelAddTaskButton.addClickListener(event -> {
            refreshTasksInFloor();
        });
        newTaskCreateLayout.add(createTaskView, saveNewTaskButton, cancelAddTaskButton);
        return newTaskCreateLayout;
    }

    private Component getTaskLayout(Task task) {
        HorizontalLayout taskLayout = new HorizontalLayout();
        ConfirmationDialog confirmationDialog = new ConfirmationDialog("Confirm Delete", "The task will be assigned to no one until " +
                "everyone accepts to delete the task. Are you sure to delete this task?",
                "Delete", "Cancel", task);
        confirmationDialog.addListener(ConfirmationDialog.ConfirmationDialogEvent.ConfirmEvent.class, this::onTaskDeleteConfirm);
        confirmationDialog.addListener(ConfirmationDialog.ConfirmationDialogEvent.CancelEvent.class, this::onTaskDeleteCancel);
        taskLayout.addClassName("floor-details-task-card");
        Span taskName = new Span(task.getTaskName());
        taskName.addClassName("floor-details-task-name");
        if (floorDetailsPresenter.isObjectDeletable(task.getId())) {
            Button deleteTask = new Button("Delete");
            addButtonClass(deleteTask);
            Button resetOrAssignTask = new Button(task.getAssignedRoom() == null ? "Assign" : "Reset");
            addButtonClass(resetOrAssignTask);
            deleteTask.addClickListener(event -> confirmationDialog.open());
            resetOrAssignTask.addClickListener(event -> UI.getCurrent().navigate(AssignTaskView.class,
                    task.getId().toString()));
            taskLayout.add(taskName, resetOrAssignTask, deleteTask);
        } else {
            taskLayout.add(taskName);
        }
        return taskLayout;
    }

    private void onTaskDeleteConfirm(ConfirmationDialog.ConfirmationDialogEvent.ConfirmEvent confirmEvent) {
        floorDetailsView.fireEvent(new FloorDetailsView.TaskUpdateEvent.DeleteTaskEvent(floorDetailsView,
                (Task) confirmEvent.getObjectCorrespondingEvent()));
    }

    private void onTaskDeleteCancel(ConfirmationDialog.ConfirmationDialogEvent.CancelEvent cancelEvent) {
    }

    private void addButtonClass(Button button) {
        button.addClassName("floor-details-task-card-button");
    }
}
