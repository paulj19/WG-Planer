package com.wg_planner.views.Tasks;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.wg_planner.backend.entity.Task;

public class TaskCard extends HorizontalLayout {

    Task task;
    //    TextField taskName = new TextField();
//    TextField assignedRoom = new TextField();
    Span taskName = new Span();
    Span assignedRoom = new Span();
    Button buttonDone = new Button("Done");
    Button buttonRemind = new Button("Remind");
    HorizontalLayout taskCardLayout = new HorizontalLayout();

    public TaskCard(Task task, Boolean isTaskAssignedToCurrentAccount) {
        this.task = task;
//        this.taskName.setLabel(task.getTaskName());
//        this.assignedRoom.setLabel(task.getAssignedRoom().toString());
        taskName.setMinWidth("500px");
        assignedRoom.setMinWidth("500px");
        taskName.setText(task.getTaskName());
        assignedRoom.setText(task.getAssignedRoom().toString());

        taskCardLayout.add(taskName, assignedRoom);
        if (isTaskAssignedToCurrentAccount) {
            taskCardLayout.add(buttonDone);
        } else {
            taskCardLayout.add(buttonRemind);
        }
    }

    public HorizontalLayout getTaskCardLayout() {
        return taskCardLayout;
    }
}
