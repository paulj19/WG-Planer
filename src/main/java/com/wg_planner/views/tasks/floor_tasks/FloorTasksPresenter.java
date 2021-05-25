package com.wg_planner.views.tasks.floor_tasks;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.backend.entity.*;
import com.wg_planner.views.tasks.TasksPresenter;
import com.wg_planner.views.utils.AccountDetailsHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class FloorTasksPresenter extends TasksPresenter {
    VerticalLayout allTaskLayout;

    public void init(VerticalLayout allTaskLayout) {
        this.allTaskLayout = allTaskLayout;
        super.init();
    }

    @Override
    public void addAllTasks() {
        allTaskLayout.removeAll();
        for (Task task : tasks) {
            FloorTaskCard floorTaskCard = new FloorTaskCard(task, AccountDetailsHelper.getLoggedInResidentAccount(residentAccountService).getRoom().getRoomName().equals(task.getAssignedRoom().getRoomName()));
            floorTaskCard.addListener(FloorTaskCard.TaskCardEvent.DoneEvent.class, this::taskDoneCallBack);
            floorTaskCard.addListener(FloorTaskCard.TaskCardEvent.RemindEvent.class, this::taskRemindCallBack);
            allTaskLayout.add(floorTaskCard.getTaskCardLayout());
        }
    }
}
