package com.wg_planner.backend.utils.consensus;

import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.resident_admission.EventTimer;
import org.apache.commons.lang3.Validate;

import java.util.Collection;

public class ConsensusHandler {
    private static ConsensusHandler consensusHandler;

    static {
        consensusHandler = new ConsensusHandler();
    }

    private ConsensusHandler() {
    }

    public static ConsensusHandler getInstance() {
        return consensusHandler;
    }

    public ConsensusObject getConsensusObject(Object objectForConsensus) {
        return ConsensusObjectStore.getInstance().getByObjectForConsensus(objectForConsensus);
    }

    public boolean processAccept(Object objectForConsensus, Room acceptor) {
        Validate.notNull(objectForConsensus, "argument must not be null");
        return processAccept(getConsensusObject(objectForConsensus), acceptor);
    }

    public boolean processAccept(Long consensusObjectId, Room acceptor) {
        if (getConsensusObject(consensusObjectId) == null) {
            return false;
        }
        return processAccept(getConsensusObject(consensusObjectId), acceptor);
    }

    public boolean processAccept(ConsensusObject consensusObject, Room acceptor) {
        Validate.notNull(consensusObject, "argument must not be null");
        Validate.notNull(acceptor, "argument must not be null");
        consensusObject.addAcceptingRoom(acceptor);
        if (consensusObject.test(consensusObject)) {
            consensusObject.getConsensusDone().onConsensusDone();
        }
        return true;
    }

    public boolean processReject(Object objectForConsensus) {
        Validate.notNull(objectForConsensus, "argument must not be null");
        return processReject(getConsensusObject(objectForConsensus));
    }

    public boolean processReject(ConsensusObject consensusObject) {
        return removeConsensusObjectByObjectToConsensus(consensusObject);
    }

    public boolean removeConsensusObjectByObjectToConsensus(Object objectForConsensus) {
        return removeConsensusObjectByObjectToConsensus(getConsensusObject(objectForConsensus));
    }

    private boolean removeConsensusObjectByObjectToConsensus(ConsensusObject consensusObject) {
        if (consensusObject == null) {
            return false;
        }
        consensusObject.roomsAccepting = null; //remove all the rooms that had accepted?
        ConsensusObjectStore.getInstance().remove(consensusObject);//delete consensus object from consensus store
        return true;
    }

    public boolean add(ConsensusObject consensusObject) {
        boolean returnVal = ConsensusObjectStore.getInstance().add(consensusObject);
        setTimer(consensusObject);
        return returnVal;
    }

    private void setTimer(ConsensusObject consensusObject) {
        EventTimer.getInstance().setTimer(consensusObject, o -> removeConsensusObjectByObjectToConsensus(consensusObject),
                consensusObject.getTimeoutInterval());
    }

    public boolean isObjectWaitingForConsensus(Object objectForConsensus) {
        return ConsensusObjectStore.getInstance().containsObject(objectForConsensus);
    }

    public Collection<ConsensusObject> getAllConsensusObjects() {
        return ConsensusObjectStore.getInstance().getAllConsensusObjects();
    }
}
