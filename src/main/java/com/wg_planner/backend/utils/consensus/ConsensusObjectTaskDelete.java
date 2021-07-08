package com.wg_planner.backend.utils.consensus;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.entity.Task;

public class ConsensusObjectTaskDelete extends ConsensusObject {
    private final long timeoutIntervalToRemoveTaskFromMapInMillis = 604800000;
    private Task taskToDelete;
    private FloorService floorService;

    ConsensusDone consensusDone = () -> floorService.deleteTaskAndUpdateFloor(taskToDelete);

    public ConsensusDone getConsensusDone() {
        return consensusDone;
    }

    public ConsensusObjectTaskDelete(Task taskToDelete, FloorService floorService) {
        this.floorService = floorService;
        this.taskToDelete = taskToDelete;
    }

    @Override
    public long getTimeoutInterval() {
        return timeoutIntervalToRemoveTaskFromMapInMillis;
    }

    @Override
    public Long getId() {
        return taskToDelete.getId();
    }

    @Override
    public boolean test(ConsensusObject consensusObject) {//all the rooms should accept
        return roomsAccepting.containsAll(floorService.getAllOccupiedAndResidentNotAwayRooms(taskToDelete.getFloor())) && floorService.getAllOccupiedAndResidentNotAwayRooms(taskToDelete.getFloor()).containsAll(roomsAccepting);
    }
}
