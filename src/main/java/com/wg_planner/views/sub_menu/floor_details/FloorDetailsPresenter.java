package com.wg_planner.views.sub_menu.floor_details;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.backend.entity.Floor;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.backend.utils.consensus.ConsensusHandler;
import com.wg_planner.backend.utils.consensus.ConsensusObjectTaskDelete;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UINotificationHandler.UIEventHandler;
import com.wg_planner.views.utils.UINotificationHandler.UIEventTypeTaskDelete;
import com.wg_planner.views.utils.UINotificationMessage;
import com.wg_planner.views.utils.broadcaster.UIMessageBus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class FloorDetailsPresenter {
    @Autowired
    private FloorService floorService;
    @Autowired
    TaskService taskService;
    @Autowired
    ResidentAccountService residentAccountService;

    private FloorDetailsView floorDetailsView;

    public void init(FloorDetailsView floorDetailsView) {
        this.floorDetailsView = floorDetailsView;
        Floor floor =
                residentAccountService.getResidentAccountById(SessionHandler.getLoggedInResidentAccount().getId()).getRoom().getFloor();
        if (!floor.getFloorName().isEmpty()) {
            floorDetailsView.addFloorName(floor.getFloorName());
        }
        floorDetailsView.addFloorCode(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor().getFloorCode());
        floorDetailsView.addRoomsInFloor(floorService.getAllRoomsInFloorByFloorId(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor()));
        floorDetailsView.addAdmitNewResidentView();
        floorDetailsView.addListener(FloorDetailsView.TaskUpdateEvent.DeleteTaskEvent.class, this::onTaskDelete);
        floorDetailsView.addTasksInFloor();
    }

    private synchronized void onTaskDelete(FloorDetailsView.TaskUpdateEvent.DeleteTaskEvent event) {
        if (!ConsensusHandler.getInstance().isObjectWaitingForConsensus(event.getTask().getId())) {
            event.getTask().setAssignedRoom(null);
            taskService.save(event.getTask());
            UIMessageBus.broadcast(UIEventHandler.getInstance().createAndSaveUINotification(new UIEventTypeTaskDelete(SessionHandler.getLoggedInResidentAccount().getRoom(), event.getTask()), floorService.getAllRoomsInFloorByFloorId(event.getTask().getFloor().getId())));
            ConsensusHandler.getInstance().add(new ConsensusObjectTaskDelete(SessionHandler.getLoggedInResidentAccount().getRoom(),
                    event.getTask(), floorService));
            UINotificationMessage.notify("The task has been send for approval, all the other residents should approve" +
                    " before task can be deleted");
        } else {
            UINotificationMessage.notify("Some other resident tried to delete the task and has already been send for " +
                    "approval by all residents");
        }
        floorDetailsView.refreshTasksInFloor();
    }

    synchronized boolean isObjectDeletable(Long id) {
        return !ConsensusHandler.getInstance().isObjectWaitingForConsensus(id);
    }

    void saveNewlyCreatedTask(Task taskCreated) {
        taskService.save(taskCreated);
    }

    List<Task> getTasksInFloor() {
        return floorService.getAllTasksInFloor(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor());
    }

    //todo fixme: usage in view, refactor to make presenter call
    public FloorService getFloorService() {
        return floorService;
    }
}
