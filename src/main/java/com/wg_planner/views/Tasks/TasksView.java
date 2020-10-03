package com.wg_planner.views.Tasks;

import com.wg_planner.backend.Service.AccountService;
import com.wg_planner.backend.Service.RoomService;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.backend.entity.Account;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.core.annotation.Order;

@Route(value = "tasks", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Tasks")
@CssImport("./styles/views/tasks/tasks-view.css")
public class TasksView extends VerticalLayout {


    TasksPresenter tasksPresenter;

    VerticalLayout allTaskLayout = new VerticalLayout();

//    @Order
    public TasksView(RoomService roomService, AccountService accountService , TaskService taskService) {
        tasksPresenter = new TasksPresenter();
        Account currentAccount ;
//        Room room = roomService.findByRoomNumber("311");
//        Room room = new Room()
        tasksPresenter.init(currentAccount, taskService, allTaskLayout);
        add(allTaskLayout);
    }
}
