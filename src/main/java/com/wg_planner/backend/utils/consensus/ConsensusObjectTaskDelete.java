package com.wg_planner.backend.utils.consensus;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;

import java.util.List;

public class ConsensusObjectTaskDelete extends ConsensusObject {
    private final long timeoutIntervalToRemoveTaskFromMapInMillis = 604800000;
    private Task taskToDelete;
    private FloorService floorService;

    //TODO remove it from store once the task is deleted
    ConsensusDone consensusDone = () -> floorService.deleteTaskAndUpdateFloor(taskToDelete);

    private ConsensusObjectTaskDelete() {
    }

    public ConsensusDone getConsensusDone() {
        return consensusDone;
    }

    public ConsensusObjectTaskDelete(Room roomInitialingConsensus, Task taskToDelete, FloorService floorService) {
        setRoomInitiatingConsensus(roomInitialingConsensus);
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
        List<Room> roomsRequiredToAccept = floorService.getAllOccupiedAndResidentNotAwayRooms(taskToDelete.getFloor());
        roomsRequiredToAccept.remove(getRoomInitiatingConsensus());
        assert roomsRequiredToAccept.size() == floorService.getAllOccupiedAndResidentNotAwayRooms(taskToDelete.getFloor()).size() - 1;
        return roomsAccepting.containsAll(roomsRequiredToAccept) && roomsRequiredToAccept.containsAll(roomsAccepting);
    }
}
