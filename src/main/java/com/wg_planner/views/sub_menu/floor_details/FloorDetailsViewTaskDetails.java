package com.wg_planner.views.sub_menu.floor_details;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.ValidationException;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.views.UnauthorizedPages.create_floor.NewTaskView;
import com.wg_planner.views.UnauthorizedPages.create_floor.NewTaskViewCreator;
import com.wg_planner.views.tasks.assign_task.AssignTaskView;
import com.wg_planner.views.utils.ConfirmationDialog;
import com.wg_planner.views.utils.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CssImport(value = "./styles/views/floor-details/floor-details-view-task-details.css")
public class FloorDetailsViewTaskDetails {
    private static final Logger LOGGER = LoggerFactory.getLogger(FloorDetailsViewTaskDetails.class);
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
        if (floorDetailsPresenter.isAnyResidentPresentInFloor()) {
            tasksInFloorLayout.add(addTaskButton);
        }
        tasksInFloorComponent = tasksInFloorLayout;
        tasksAccordion.add("Edit Tasks", tasksInFloorLayout);
        return tasksInFloorLayout;
    }

    private Component getNewTaskCreateLayout() {
        HorizontalLayout newTaskCreateLayout = new HorizontalLayout();
        newTaskCreateLayout.addClassName("new-task-layout");
        NewTaskView newTaskView = NewTaskViewCreator.createTaskFromFloorTasks(floorDetailsPresenter.getTasksInFloor());
        newTaskView.getTaskNameTextField().setWidth("46vw");
        newTaskView.getTaskNameTextField().getStyle().set("padding-top", "0");
        Button saveNewTaskButton = new Button("Save");
        addButtonClass(saveNewTaskButton);
        saveNewTaskButton.getStyle().set("font-size", "0.9em");
        saveNewTaskButton.getStyle().set("margin-top", "23px");
        //        saveNewTaskButton.getStyle().set("font-size", "0.8em");
        Button cancelAddTaskButton = new Button("Cancel");
        addButtonClass(cancelAddTaskButton);
        cancelAddTaskButton.getStyle().set("font-size", "0.85em");
        cancelAddTaskButton.getStyle().set("margin-top", "23px");
        saveNewTaskButton.addClickListener(event -> {
            try {
                floorDetailsPresenter.saveNewlyCreatedTask(newTaskView.validateAndSave(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor()));
            } catch (ValidationException e) {
                e.printStackTrace();
            }
            refreshTasksInFloor();
        });
        cancelAddTaskButton.addClickListener(event -> {
            refreshTasksInFloor();
        });
        newTaskCreateLayout.add(newTaskView, saveNewTaskButton, cancelAddTaskButton);
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
        taskLayout.add(taskName);
        if (floorDetailsPresenter.isTaskEditable(task)) {
            Button resetOrAssignTask = new Button(task.getAssignedRoom() == null ? "Assign" : "Reset");
            Button deleteTask = new Button("Delete");
            addButtonClass(resetOrAssignTask);
            addButtonClass(deleteTask);
            resetOrAssignTask.addClickListener(event -> UI.getCurrent().navigate(AssignTaskView.class, task.getId().toString()));
            deleteTask.addClickListener(event -> confirmationDialog.open());
            taskLayout.add(resetOrAssignTask, deleteTask);
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
