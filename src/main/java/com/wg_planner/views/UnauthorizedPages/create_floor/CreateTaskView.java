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

public class CreateTaskView extends HorizontalLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateTaskView.class);
    TextField taskNameTextField = new TextField("Task Name", "Enter task name");
    Task taskToCreate = new Task();
    private Binder<Task> taskBinder = new BeanValidationBinder<>(Task.class);

    public CreateTaskView() {
        setFieldProperties();
        taskBinder.forField(taskNameTextField).withValidator(taskName -> taskName.length() <= 16,
                "task name should not exceed 16 characters").withValidator(taskName -> taskName.length() >= 1,
                "task name should not be empty").bind(Task::getTaskName,
                Task::setTaskName);
        setWidthFull();
        taskNameTextField.setWidthFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(taskNameTextField);
    }

    private void setFieldProperties() {
        taskNameTextField.setClearButtonVisible(true);
        taskNameTextField.setMaxLength(16);
        taskNameTextField.setRequired(true);
    }

    public Task validateTask(Floor floorToCreate) {
        try {
            taskToCreate.setFloor(floorToCreate);
            taskBinder.writeBean(taskToCreate);
            LOGGER.info(LogHandler.getTestRun(), "create task validate and save. Task details: {}", taskToCreate.toString());
            return taskToCreate;
        } catch (ValidationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Task getTaskToCreate() {
        return taskToCreate;
    }

    public TextField getTaskNameTextField() {
        return taskNameTextField;
    }
}

