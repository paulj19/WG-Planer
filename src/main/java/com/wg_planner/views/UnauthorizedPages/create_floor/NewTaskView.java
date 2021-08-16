package com.wg_planner.views.UnauthorizedPages.create_floor;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.backend.utils.LogHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class NewTaskView extends HorizontalLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewTaskView.class);
    TextField taskNameTextField = new TextField("Task Name", "Enter task name");
    Task taskToCreate = new Task();
    private Binder<Task> taskBinder = new BeanValidationBinder<>(Task.class);

    NewTaskView() {
    }

    public void setNewTaskCreateViewList(List<NewTaskView> tasksView) {
        init();
        taskBinder.forField(taskNameTextField).withValidator(taskName -> taskName.length() <= 16,
                "task name should not exceed 16 characters").withValidator(taskName -> taskName.length() >= 1,
                "task name should not be empty").withValidator(taskName -> isTaskNameUniqueWithInFloorCreateForm(taskName.toLowerCase(Locale.GERMANY),
                tasksView),
                "task names in a floor must be unique").bind(Task::getTaskName, Task::setTaskName);
    }


    public void setTasksInFloor(List<Task> tasksAlreadyCreated) {
        init();
        //since lambda captures the variables this will not work with newly created task which should bind later
        taskBinder.forField(taskNameTextField).withValidator(taskName -> taskName.length() <= 16,
                "task name should not exceed 16 characters").withValidator(taskName -> taskName.length() >= 1,
                "task name should not be empty").withValidator(taskName -> isTaskNameUnique(taskName.toLowerCase(Locale.GERMANY), tasksAlreadyCreated),
                "task names in a floor must be unique").bind(Task::getTaskName, Task::setTaskName);
    }

    private void init() {
        setFieldProperties();
        setWidthFull();
        taskNameTextField.setWidthFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(taskNameTextField);
        LOGGER.info(LogHandler.getTestRun(), "create task view called");

    }

    public boolean isTaskNameUnique(String taskName, List<Task> tasksAlreadyCreated) {
        return tasksAlreadyCreated.stream().filter(task -> !Objects.equals(task, taskToCreate)).map(task -> task.getTaskName().toLowerCase(Locale.GERMANY)).noneMatch(taskName::equals);
    }

    public boolean isTaskNameUniqueWithInFloorCreateForm(String taskName, List<NewTaskView> tasksView) {
        return tasksView.stream().filter(taskView -> !Objects.equals(taskView, this)).map(taskView -> {
            if (taskView.getTaskToCreate().getTaskName() != null) {
                return taskView.getTaskToCreate().getTaskName().toLowerCase(Locale.GERMANY);
            }
            return null;
        }).noneMatch(taskName::equals);
    }


    private void setFieldProperties() {
        taskNameTextField.setClearButtonVisible(true);
        taskNameTextField.setMaxLength(16);
        taskNameTextField.setRequired(true);
    }

    public Task validateAndSave(Floor floorToCreate) throws ValidationException {
        try {
            taskToCreate.setFloor(floorToCreate);
            taskBinder.writeBean(taskToCreate);
            LOGGER.info(LogHandler.getTestRun(), "create task validate and save. Task details: {}", taskToCreate.toString());
            return taskToCreate;
        } catch (ValidationException e) {
            throw e;
        }
    }

    public Task getTaskToCreate() {
        return taskToCreate;
    }

    public TextField getTaskNameTextField() {
        return taskNameTextField;
    }

}

