package com.wg_planner.backend.utils.consensus;

import com.wg_planner.backend.Service.FloorService;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.utils.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ConsensusObjectTaskDelete extends ConsensusObject {
    private final String taskStatus = "waiting to be deleted";
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsensusObjectTaskDelete.class);
    private final long timeoutIntervalToRemoveTaskFromMapInMillis = 604800000;
    private Task taskToDelete;
    private FloorService floorService;

    //TODO remove it from store once the task is deleted
    ConsensusDone consensusDone = () -> {
        floorService.deleteTaskAndUpdateFloor(taskToDelete);
        ConsensusHandler.getInstance().removeConsensusObjectByObjectToConsensus(taskToDelete);
    };

    private ConsensusObjectTaskDelete() {
    }

    public ConsensusDone getConsensusDone() {
        return consensusDone;
    }

    @Override
    public String getCurrentStatus() {
        return taskStatus;
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
    public Object getRelatedObject() {
        return taskToDelete;
    }

    @Override
    public boolean test(ConsensusObject consensusObject) {
        List<Room> roomsRequiredToAccept = floorService.getAllOccupiedAndResidentNotAwayRooms(taskToDelete.getFloor());
        roomsRequiredToAccept.remove(getRoomInitiatingConsensus());
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. ConsensusObjectTaskDelete test. Room initiating consensus {}, Consensus Type {}," +
                        "consensus object id {}, size of acceptors {}, floorService.getAllOccupiedAndResidentNotAwayRooms(): ",
                SessionHandler.getLoggedInResidentAccount().getId(), consensusObject.getRoomInitiatingConsensus().getId(), getClass().toString(),
                consensusObject.getId(), roomsRequiredToAccept.size());
        floorService.getAllOccupiedAndResidentNotAwayRooms(taskToDelete.getFloor()).forEach(room ->  LOGGER.info(LogHandler.getTestRun(), ", {}",
                room.getId()));
        assert roomsRequiredToAccept.size() == floorService.getAllOccupiedAndResidentNotAwayRooms(taskToDelete.getFloor()).size() - 1;
        return roomsAccepting.containsAll(roomsRequiredToAccept) && roomsRequiredToAccept.containsAll(roomsAccepting);
    }
}
