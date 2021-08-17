package com.wg_planner.backend.utils.consensus;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public abstract class ConsensusObjectStore {
    public abstract boolean add(ConsensusObject o);

    public abstract ConsensusObject get(Long id);

    public abstract void remove(ConsensusObject consensusObject);

    public abstract boolean containsObject(Long id);

    public abstract Collection<ConsensusObject> getAllConsensusObjects();

    public static ConsensusObjectStore getInstance() {
        return ConsensusObjectStoreHashSet.getInstance();
    }
}
