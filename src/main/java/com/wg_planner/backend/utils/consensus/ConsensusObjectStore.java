package com.wg_planner.backend.utils.consensus;

public interface ConsensusObjectStore {
    boolean add(ConsensusObject o);

    ConsensusObject get(Long id);

    void remove(ConsensusObject consensusObject);

    boolean containsObject(Long id);
}
