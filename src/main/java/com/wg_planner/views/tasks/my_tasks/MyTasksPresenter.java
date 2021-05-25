package com.wg_planner.views.tasks.my_tasks;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.backend.entity.*;
import com.wg_planner.views.tasks.TaskCard;
import com.wg_planner.views.tasks.TasksPresenter;
import com.wg_planner.views.utils.AccountDetailsHelper;

public class MyTasksPresenter extends TasksPresenter {
    VerticalLayout allTaskLayout;

    public void init(VerticalLayout allTaskLayout) {
        this.allTaskLayout = allTaskLayout;
        super.init();
    }

    @Override
    public void addAllTasks() {
        allTaskLayout.removeAll();

        for (Task task : tasks) {
            if (AccountDetailsHelper.getLoggedInResidentAccount(residentAccountService).getRoom().getRoomName().equals(task.getAssignedRoom().getRoomName())) {
                MyTaskCard myTaskCard = new MyTaskCard(task);
                myTaskCard.addListener(MyTaskCard.TaskCardEvent.DoneEvent.class, this::taskDoneCallBack);
                myTaskCard.addListener(TaskCard.TaskCardEvent.AssignEvent.class, this::taskAssignCallBack);
                allTaskLayout.add(myTaskCard.getTaskCardLayout());
            }
        }
    }
}
