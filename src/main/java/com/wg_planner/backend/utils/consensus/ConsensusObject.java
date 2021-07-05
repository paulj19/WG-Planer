package com.wg_planner.backend.utils.consensus;

import com.wg_planner.backend.entity.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class ConsensusObject implements Predicate<ConsensusObject> {
    protected List<Room> roomsAccepting = new ArrayList<>();
    protected List<Room> roomsRejecting = new ArrayList<>();

    public abstract long getTimeoutInterval();

    public abstract Long getId();

    public abstract ConsensusDone getConsensusDone();

    void addAcceptingRoom(Room acceptingRoom) {
        roomsAccepting.add(acceptingRoom);
    }
}
