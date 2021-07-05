package com.wg_planner.backend.utils.consensus;

import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsensusHandler {
    private ConsensusObjectStore consensusObjectStore;

    @Autowired
    public ConsensusHandler(ConsensusObjectStore consensusObjectStore) {
        this.consensusObjectStore = consensusObjectStore;
    }

    public void processAccept(Task task, Room acceptor) {
        Validate.notNull(acceptor,
                "argument room must not be null in " + new Object() {
                }.getClass().getEnclosingMethod().getName());
        processAccept(consensusObjectStore.get(task.getId()), acceptor);
    }

    public void processAccept(Long id, Room acceptor) {
        Validate.notNull(consensusObjectStore.get(id), "id of object passed not found");
        processAccept(consensusObjectStore.get(id), acceptor);
    }

    public void processAccept(ConsensusObject consensusObject, Room acceptor) {
        Validate.notNull(consensusObject, "argument must not be null");
        Validate.notNull(acceptor, "argument must not be null");
        consensusObject.addAcceptingRoom(acceptor);
        if (consensusObject.test(consensusObject)) {
            consensusObject.getConsensusDone().onConsensusDone();
        }
    }

    public boolean add(ConsensusObject consensusObject) {
        return consensusObjectStore.add(consensusObject);
    }

    public ConsensusObject get(Long id) {
        return consensusObjectStore.get(id);
    }
}
