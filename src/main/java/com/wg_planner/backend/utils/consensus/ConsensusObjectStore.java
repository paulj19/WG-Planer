package com.wg_planner.backend.utils.consensus;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public abstract class ConsensusObjectStore {
    public abstract boolean add(ConsensusObject o);

    public abstract ConsensusObject getById(String id);

    public abstract ConsensusObject getByObjectForConsensus(Object objectForConsensus);

    public abstract void remove(ConsensusObject consensusObject);

    public abstract boolean containsObject(Object objectForConsensus);

    public abstract Collection<ConsensusObject> getAllConsensusObjects();

    public static ConsensusObjectStore getInstance() {
        return ConsensusObjectStoreHashSet.getInstance();
    }
}
