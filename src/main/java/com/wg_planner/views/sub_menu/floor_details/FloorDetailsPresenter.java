package com.wg_planner.views.sub_menu.floor_details;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.utils.consensus.ConsensusHandler;
import com.wg_planner.backend.utils.consensus.ConsensusObjectTaskDelete;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UINotificationHandler.UIEventHandler;
import com.wg_planner.views.utils.UINotificationHandler.UIEventTypeTaskDelete;
import com.wg_planner.views.utils.broadcaster.UIMessageBus;
import org.springframework.beans.factory.annotation.Autowired;

public class FloorDetailsPresenter {
    @Autowired
    private FloorService floorService;
    private FloorDetailsView floorDetailsView;

    public void init(FloorDetailsView floorDetailsView) {
        this.floorDetailsView = floorDetailsView;
        if (!SessionHandler.getLoggedInResidentAccount().getRoom().getFloor().getFloorName().isEmpty()) {
            floorDetailsView.addFloorName(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor().getFloorName());
        }
        floorDetailsView.addFloorCode(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor().getFloorCode());
        floorDetailsView.addRoomsInFloor(floorService.getAllRoomsInFloorByFloorId(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor()));
        floorDetailsView.addNewRoomTextField();
        floorDetailsView.addListener(FloorDetailsView.TaskUpdateEvent.DeleteTaskEvent.class, this::onTaskDelete);
        floorDetailsView.addTasksInFloor(floorService.getAllTasksInFloor(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor()));
    }

    private void onTaskDelete(FloorDetailsView.TaskUpdateEvent.DeleteTaskEvent event) {
        if (ConsensusHandler.getInstance().isObjectNotWaitingForConsensus(event.getTask().getId())) {
            UIMessageBus.broadcast(UIEventHandler.getInstance().createAndSaveUINotification(new UIEventTypeTaskDelete(SessionHandler.getLoggedInResidentAccount().getRoom(), event.getTask()), floorService.getAllRoomsInFloorByFloorId(event.getTask().getFloor().getId())));
            ConsensusHandler.getInstance().add(new ConsensusObjectTaskDelete(event.getTask(), floorService));
            floorDetailsView.notify("The task has been send for approval, all the other residents should approve before task can be " +
                    "deleted");
        } else {
            floorDetailsView.notify("Some other resident tried to delete the task and has already been send for approval by all residents");
        }
        floorDetailsView.refreshTasksInFloor(floorService.getAllTasksInFloor(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor()));
        //        floorService.getAllOccupiedAndResidentNotAwayRooms(event.getTask().getFloor()).forEach(room -> ConsensusHandler
        //        .processAccept(event.getTask().getId(), room));
    }

    boolean isObjectDeletable(Long id) {
        return ConsensusHandler.getInstance().isObjectNotWaitingForConsensus(id);
    }
}
