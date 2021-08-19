package com.wg_planner.backend.utils.consensus;

import com.wg_planner.backend.entity.Room;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class ConsensusObject implements Predicate<ConsensusObject> {
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
    }
}
