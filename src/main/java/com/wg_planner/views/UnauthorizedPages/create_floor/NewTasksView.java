package com.wg_planner.views.UnauthorizedPages.create_floor;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.ValidationException;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.Task;

import java.util.ArrayList;
import java.util.List;

public class NewTasksView extends VerticalLayout {
    List<NewTaskView> tasksView = new ArrayList<>();
    Icon addTaskIcon = new Icon(VaadinIcon.PLUS_CIRCLE);

    public NewTasksView() {
        addClassName("create-room-task-view");
        addTaskIcon.addClassName("add-icon");
        addTaskIcon.addClickListener(iconClickEvent -> addTaskView());
    }

    public List<Task> validateAndSave(Floor floorCreated) throws ValidationException {
        List<Task> validatedTasks = new ArrayList<>();
        for(NewTaskView t : tasksView) {
            validatedTasks.add(t.validateAndSave(floorCreated));
        }
        return validatedTasks;
    }

    public void addTaskView() {
        NewTaskView taskView = NewTaskViewCreator.createTaskFromOtherCreatedTasks(tasksView);
        if (!tasksView.isEmpty() && tasksView.get(tasksView.size() - 1) != null) {
            tasksView.get(tasksView.size() - 1).remove(addTaskIcon);
            taskView.getTaskNameTextField().setAutofocus(true);
        }
        remove(addTaskIcon);
        tasksView.add(taskView);
        taskView.add(addTaskIcon);
        add(taskView);
    }
}
