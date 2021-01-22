package com.wg_planner.views.FloorTasks;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.backend.entity.*;
import com.wg_planner.views.Tasks.TasksPresenter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class FloorTasksPresenter extends TasksPresenter {

    public void init(VerticalLayout allTaskLayout) {
        super.init(allTaskLayout);
    }

    @Override
    public void addAllTasks() {
        allTaskLayout.removeAll();
        for (Task task : tasks) {
            FloorTaskCard floorTaskCard = new FloorTaskCard(task, myRoom.getRoomNumber().equals(task.getAssignedRoom().getRoomNumber()));
            floorTaskCard.addListener(FloorTaskCard.TaskCardEvent.DoneEvent.class, this::taskDoneCallBackToSaveTask);
            floorTaskCard.addListener(FloorTaskCard.TaskCardEvent.RemindEvent.class, this::taskRemindCallBack);
            allTaskLayout.add(floorTaskCard.getTaskCardLayout());
        }
    }
}
