package com.wg_planner.views.Tasks;

import com.wg_planner.backend.Service.*;
import com.wg_planner.backend.entity.Account;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "tasks", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Tasks")
@CssImport("./styles/views/tasks/tasks-view.css")
public class TasksView extends VerticalLayout {


    TasksPresenter tasksPresenter;

    VerticalLayout allTaskLayout = new VerticalLayout();

//    @Order
    public TasksView(RoomService roomService, AccountDetailsService accountDetailsService , TaskService taskService, FloorService floorService, ResidentAccountService residentAccountService) {
        tasksPresenter = new TasksPresenter();
        Account currentAccount ;
        Room room = roomService.getRoomByNumber("315");

        Floor floor = room.getFloor();
        Room room1 = floorService.getNextAvailableRoom(floor, room);
//        Room room = new Room()
        tasksPresenter.init(residentAccountService, taskService, roomService, floorService, allTaskLayout);
        add(allTaskLayout);
    }
}
