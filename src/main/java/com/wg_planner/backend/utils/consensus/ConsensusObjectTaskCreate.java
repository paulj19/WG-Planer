package com.wg_planner.backend.utils.consensus;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.Service.TaskService;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;

import java.util.List;

public class ConsensusObjectTaskCreate extends ConsensusObject {
    private final String taskStatus = "waiting to be created";
    private final long timeoutIntervalToRemoveTaskFromMapInMillis = 604800000;
    private Task taskToCreate;
    private TaskService taskService;
    private FloorService floorService;

    //TODO remove it from store once the task is created
    ConsensusDone consensusDone = () -> {
        taskService.save(taskToCreate);
        ConsensusHandler.getInstance().removeConsensusObjectByObjectToConsensus(taskToCreate);
    };;

    private ConsensusObjectTaskCreate() {
    }

    public ConsensusDone getConsensusDone() {
        return consensusDone;
    }

    @Override
    public String getCurrentStatus() {
        return taskStatus;
    }

    public ConsensusObjectTaskCreate(Room roomInitialingConsensus, Task taskToCreate, FloorService floorService, TaskService taskService) {
        setRoomInitiatingConsensus(roomInitialingConsensus);
        this.floorService = floorService;
        this.taskService = taskService;
        this.taskToCreate = taskToCreate;
    }

    @Override
    public long getTimeoutInterval() {
        return timeoutIntervalToRemoveTaskFromMapInMillis;
    }

    @Override
    public Object getRelatedObject() {
        return taskToCreate;
    }

    @Override
    public boolean test(ConsensusObject consensusObject) {
        List<Room> roomsRequiredToAccept = floorService.getAllOccupiedAndResidentNotAwayRooms(taskToCreate.getFloor());
        roomsRequiredToAccept.remove(getRoomInitiatingConsensus());
        assert roomsRequiredToAccept.size() == floorService.getAllOccupiedAndResidentNotAwayRooms(taskToCreate.getFloor()).size() - 1;
        return roomsAccepting.containsAll(roomsRequiredToAccept) && roomsRequiredToAccept.containsAll(roomsAccepting);
    }
}

