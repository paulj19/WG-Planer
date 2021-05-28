package com.wg_planner.views.tasks.my_tasks;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.views.task_cards.TaskCardCreator;
import com.wg_planner.views.tasks.TasksPresenter;
import com.wg_planner.views.utils.AccountDetailsHelper;

public class MyTasksPresenter extends TasksPresenter {
    VerticalLayout allTaskLayout;

    public void init(VerticalLayout allTaskLayout) {
        this.allTaskLayout = allTaskLayout;
        super.init();
    }

    @Override
    public void addTasks() {
        allTaskLayout.removeAll();
        AccountDetailsHelper.getInstance().getLoggedInResidentAccount().getRoom().getAssignedTasks().forEach(task -> allTaskLayout.add(TaskCardCreator.createLoggedInResidentTaskCard(task, this)));
//        tasks.stream().filter(task -> TaskCardCreator.createLoggedInResidentTaskCard(task, this) != null).map(allTaskLayout.add();

//            if (AccountDetailsHelper.getLoggedInResidentAccount(residentAccountService).getRoom().getRoomName().equals(task.getAssignedRoom().getRoomName())) {
//                MyTaskCard myTaskCard = new MyTaskCard(task);
//                myTaskCard.addListener((TaskCard.TaskCardEvent.AssignEvent.class, this::taskAssignCallBack );
//                myTaskCard.addListener(TaskCard.TaskCardEvent.AssignEvent.class, this::taskAssignCallBack);
//                allTaskLayout.add(myTaskCard.getTaskCardLayout());
    }
}
