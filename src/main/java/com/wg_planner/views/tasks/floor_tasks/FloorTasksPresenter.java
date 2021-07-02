package com.wg_planner.views.tasks.floor_tasks;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.views.tasks.TasksPresenter;
import com.wg_planner.views.tasks.task_cards.TaskCardCreator;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationHandler;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationTypeTaskDelete;
import com.wg_planner.views.utils.broadcaster.UIBroadcaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class FloorTasksPresenter extends TasksPresenter {
    VerticalLayout allTaskLayout;
    @Autowired
    UINotificationHandler uiNotificationHandler;

    public void init(VerticalLayout allTaskLayout) {
        this.allTaskLayout = allTaskLayout;
        super.init();
    }

    @Override
    public void addTasks() {
        tasks.forEach(task -> allTaskLayout.add(TaskCardCreator.createAllTaskCard(task, this)));
        UIBroadcaster.broadcast(uiNotificationHandler.createAndSaveUINotification(new UINotificationTypeTaskDelete(), SessionHandler.getLoggedInResidentAccount().getRoom(), SessionHandler.getLoggedInResidentAccount().getRoom().getAssignedTasks().get(0)));
    }


}
