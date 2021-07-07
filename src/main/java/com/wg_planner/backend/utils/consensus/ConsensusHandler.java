package com.wg_planner.backend.utils.consensus;

import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsensusHandler {
    private static ConsensusObjectStore consensusObjectStore;

    @Autowired
    public ConsensusHandler(ConsensusObjectStore consensusObjectStore) {
        this.consensusObjectStore = consensusObjectStore;
    }

    public static void processAccept(Task task) {
        Validate.notNull(task, "argument must not be null");
        processAccept(consensusObjectStore.get(task.getId()));
    }

    public static void processAccept(Long id) {
        Validate.notNull(consensusObjectStore.get(id), "id of object passed not found");
        processAccept(consensusObjectStore.get(id));
    }

    public static void processAccept(ConsensusObject consensusObject) {
        Validate.notNull(consensusObject, "argument must not be null");
        if (consensusObject.test(consensusObject)) {
            consensusObject.getConsensusDone().onConsensusDone();
        }
    }

    public static void processReject(Long id) {
        Validate.notNull(id, "argument must not be null");
        processReject(consensusObjectStore.get(id));
    }

    public static void processReject(ConsensusObject consensusObject) {
        consensusObject.roomsAccepting = null; //remove all the rooms that had accepted?
        consensusObjectStore.remove(consensusObject);//delete consensus object from consensus store
    }

    public boolean add(ConsensusObject consensusObject) {
        return consensusObjectStore.add(consensusObject);
    }

    public ConsensusObject get(Long id) {
        return consensusObjectStore.get(id);
    }

    public boolean isObjectWaitingForConsensus(Long objectId) {
        return consensusObjectStore.containsObject(objectId);
    }
}
