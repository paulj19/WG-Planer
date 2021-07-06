package com.wg_planner.views.sub_menu.floor_details;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.utils.consensus.ConsensusHandler;
import com.wg_planner.backend.utils.consensus.ConsensusObjectTaskDelete;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationHandler;
import com.wg_planner.views.utils.UINotificationHandler.UINotificationTypeTaskDelete;
import com.wg_planner.views.utils.broadcaster.UIBroadcaster;
import org.springframework.beans.factory.annotation.Autowired;

public class FloorDetailsPresenter {
    @Autowired
    private FloorService floorService;
    @Autowired
    UINotificationHandler uiNotificationHandler;
    @Autowired
    ConsensusHandler consensusHandler;
    private FloorDetailsView floorDetailsView;

    public void init(FloorDetailsView floorDetailsView) {
        this.floorDetailsView = floorDetailsView;
        if (!SessionHandler.getLoggedInResidentAccount().getRoom().getFloor().getFloorName().isEmpty()) {
            floorDetailsView.addFloorName(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor().getFloorName());
        }
        floorDetailsView.addFloorCode(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor().getFloorCode());
        floorDetailsView.addRoomsInFloor(floorService.getAllRoomsInFloor(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor()));
        floorDetailsView.addListener(FloorDetailsView.TaskUpdateEvent.DeleteTaskEvent.class, this::onTaskDelete);
        floorDetailsView.addTasksInFloor(floorService.getAllTasksInFloor(SessionHandler.getLoggedInResidentAccount().getRoom().getFloor()));
    }

    private void onTaskDelete(FloorDetailsView.TaskUpdateEvent.DeleteTaskEvent event) {
        UIBroadcaster.broadcast(uiNotificationHandler.createAndSaveUINotification(new UINotificationTypeTaskDelete(SessionHandler.getLoggedInResidentAccount().getRoom(), event.getTask())));
        consensusHandler.add(new ConsensusObjectTaskDelete(event.getTask(), floorService));
        floorService.getAllOccupiedAndResidentNotAwayRooms(event.getTask().getFloor()).forEach(room -> consensusHandler.processAccept(event.getTask().getId(), room));
    }
}
