package com.wg_planner.backend.utils.consensus;

import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.utils.SessionHandler;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class ConsensusObject implements Predicate<ConsensusObject> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsensusObject.class);
    private final String id = UUID.randomUUID().toString();
    protected Room roomInitiatingConsensus;
    protected List<Room> roomsAccepting = new ArrayList<>();
    protected List<Room> roomsRejecting = new ArrayList<>();

    public abstract long getTimeoutInterval();

    public String getId() {
        return id;
    };

    public abstract Object getRelatedObject();

    public abstract ConsensusDone getConsensusDone();

    public abstract String getCurrentStatus();

    Room getRoomInitiatingConsensus() {
        return roomInitiatingConsensus;
    }

    void setRoomInitiatingConsensus(Room roomInitiatingConsensus) {
        Validate.notNull(roomInitiatingConsensus);
        this.roomInitiatingConsensus = roomInitiatingConsensus;
    }

    void addAcceptingRoom(Room acceptingRoom) {
        Validate.isTrue(!roomsAccepting.contains(acceptingRoom), "room should accept only once");
        roomsAccepting.add(acceptingRoom);
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. ConsensusObject addAcceptingRoom. Room initiating consensus {}, Consensus Type {}, " +
                        "consensus object id {} Room acceptor {} Rooms in accepting list:",
                SessionHandler.getLoggedInResidentAccount().getId(), roomInitiatingConsensus.getId(), getClass().toString(),
                getId(), acceptingRoom.getId());
        roomsAccepting.forEach(room -> LOGGER.info(LogHandler.getTestRun(), ", {}", room.getId()));
    }
}
