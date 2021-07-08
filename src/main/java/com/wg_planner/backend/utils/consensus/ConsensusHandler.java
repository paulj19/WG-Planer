package com.wg_planner.backend.utils.consensus;

import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.backend.resident_admission.EventTimer;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsensusHandler {
    private static ConsensusObjectStore consensusObjectStore;
    private EventTimer eventTimer;//todo autowire and static together?

    @Autowired
    public ConsensusHandler(ConsensusObjectStore consensusObjectStore, EventTimer eventTimer) {
        this.consensusObjectStore = consensusObjectStore;
        this.eventTimer = eventTimer;
    }

    public static boolean processAccept(Task task, Room acceptor) {
        Validate.notNull(task, "argument must not be null");
        return processAccept(consensusObjectStore.get(task.getId()), acceptor);
    }

    public static boolean processAccept(Long id, Room acceptor) {
        if (consensusObjectStore.get(id) == null) {
            return false;
        }
        return processAccept(consensusObjectStore.get(id), acceptor);
    }

    public static boolean processAccept(ConsensusObject consensusObject, Room acceptor) {
        Validate.notNull(consensusObject, "argument must not be null");
        Validate.notNull(acceptor, "argument must not be null");
        consensusObject.addAcceptingRoom(acceptor);
        if (consensusObject.test(consensusObject)) {
            consensusObject.getConsensusDone().onConsensusDone();
        }
        return true;
    }

    public static boolean processReject(Long id) {
        Validate.notNull(id, "argument must not be null");
        return processReject(consensusObjectStore.get(id));
    }

    public static boolean processReject(ConsensusObject consensusObject) {
        return removeConsensusObjectFromStore(consensusObject);
    }

    private static boolean removeConsensusObjectFromStore(ConsensusObject consensusObject) {
        if(consensusObject == null) {
            return false;
        }
        consensusObject.roomsAccepting = null; //remove all the rooms that had accepted?
        consensusObjectStore.remove(consensusObject);//delete consensus object from consensus store
        return true;
    }

    public boolean add(ConsensusObject consensusObject) {
        boolean returnVal = consensusObjectStore.add(consensusObject);
        setTimer(consensusObject);
        return returnVal;
    }

    private void setTimer(ConsensusObject consensusObject) {
        eventTimer.setTimer(consensusObject, o -> removeConsensusObjectFromStore(consensusObject), consensusObject.getTimeoutInterval());
    }

    public ConsensusObject get(Long id) {
        return consensusObjectStore.get(id);
    }

    public boolean isObjectWaitingForConsensus(Long objectId) {
        return !consensusObjectStore.containsObject(objectId);
    }
}
