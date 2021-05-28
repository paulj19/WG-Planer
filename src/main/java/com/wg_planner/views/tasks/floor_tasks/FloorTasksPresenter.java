package com.wg_planner.views.tasks.floor_tasks;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.wg_planner.views.task_cards.TaskCardCreator;
import com.wg_planner.views.tasks.TasksPresenter;
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
    public void addTasks() {
        tasks.stream().forEach(task -> allTaskLayout.add(TaskCardCreator.createAllTaskCard(task, this)));

//        allTaskLayout.removeAll();
//        tasks.forEach(task -> new );
//        for (Task task : tasks) {
//            FloorTaskCard floorTaskCard = new FloorTaskCard(task, AccountDetailsHelper.getLoggedInResidentAccount(residentAccountService).getRoom().getRoomName().equals(task.getAssignedRoom().getRoomName()));
//            floorTaskCard.addListener(FloorTaskCard.TaskCardEvent.DoneEvent.class, this::taskDoneCallBack);
//            floorTaskCard.addListener(FloorTaskCard.TaskCardEvent.RemindEvent.class, this::taskRemindCallBack);
//            allTaskLayout.add(floorTaskCard.getTaskCardLayout());
//        }
    }


}
