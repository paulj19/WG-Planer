package com.wg_planner.views.tasks.my_tasks;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.backend.entity.*;
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
                if (AccountDetailsHelper.getUserResidentAccount(residentAccountService).getRoom().getRoomNumber().equals(task.getAssignedRoom().getRoomNumber())) {
                MyTaskCard myTaskCard = new MyTaskCard(task);
                myTaskCard.addListener(MyTaskCard.TaskCardEvent.DoneEvent.class, this::taskDoneCallBackToSaveTask);
                myTaskCard.addListener(MyTaskCard.TaskCardEvent.ResetEvent.class, this::taskResetCallBack);
                allTaskLayout.add(myTaskCard.getTaskCardLayout());
            }
        }
    }
}
