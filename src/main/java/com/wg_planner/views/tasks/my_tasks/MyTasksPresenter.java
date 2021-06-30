package com.wg_planner.views.tasks.my_tasks;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.views.tasks.TasksPresenter;
import com.wg_planner.views.tasks.task_cards.TaskCardCreator;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationHandler;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationTypeTaskDelete;
import org.springframework.beans.factory.annotation.Autowired;

public class MyTasksPresenter extends TasksPresenter {
    VerticalLayout allTaskLayout;
    @Autowired
    UINotificationHandler uiNotificationHandler;

    public void init(VerticalLayout allTaskLayout) {
        this.allTaskLayout = allTaskLayout;
        super.init();
    }

    @Override
    public void addTasks() {
        allTaskLayout.removeAll();
        SessionHandler.getLoggedInResidentAccount().getRoom().getAssignedTasks().forEach(task -> allTaskLayout.add(TaskCardCreator.createLoggedInResidentTaskCard(task, this)));
        allTaskLayout.add(uiNotificationHandler.createAndSaveUINotification(new UINotificationTypeTaskDelete(),
                SessionHandler.getLoggedInResidentAccount().getRoom(),
                SessionHandler.getLoggedInResidentAccount().getRoom().getAssignedTasks().get(0)));
    }
}
