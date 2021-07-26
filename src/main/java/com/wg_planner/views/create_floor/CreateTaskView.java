package com.wg_planner.views.create_floor;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.wg_planner.backend.entity.Floor;

import com.vaadin.flow.component.textfield.TextField;
import com.wg_planner.backend.entity.Task;

public class CreateTaskView extends HorizontalLayout {
    TextField taskNameTextField = new TextField("","Enter task name");
//    Icon removeTaskIcon = new Icon(VaadinIcon.MINUS_CIRCLE);
    Task taskToCreate = new Task();
    private Binder<Task> taskBinder = new BeanValidationBinder<>(Task.class);

    public CreateTaskView() {
        taskBinder.forField(taskNameTextField).bind(Task::getTaskName, Task::setTaskName);
//        removeTaskIcon.addClickListener(iconClickEvent -> remove(taskNameTextField, removeTaskIcon));
//        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(taskNameTextField);
    }

    public Task validateTask(Floor floorToCreate) {
        try {
            taskToCreate.setFloor(floorToCreate);
            taskBinder.writeBean(taskToCreate);
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

