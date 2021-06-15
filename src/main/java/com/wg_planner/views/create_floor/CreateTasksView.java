package com.wg_planner.views.create_floor;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreateTasksView extends VerticalLayout {
    List<CreateTaskView> tasksView = new ArrayList<>();
    Icon addTaskIcon = new Icon(VaadinIcon.PLUS_CIRCLE);

    public CreateTasksView() {
        addTaskIcon.addClickListener(iconClickEvent -> addTaskView());
    }

    public List<Task> validateAndSave(Floor floorCreated) {
        return tasksView.stream().map(taskView -> taskView.validateAndSave(floorCreated)).collect(Collectors.toList());
    }

    public void addTaskView() {
        remove(addTaskIcon);
        CreateTaskView taskView = new CreateTaskView();
        tasksView.add(taskView);
        add(taskView);
        add(addTaskIcon);
    }
}
