package com.wg_planner.backend.utils.consensus;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;

@Component
public class ConsensusObjectStoreHashSet extends ConsensusObjectStore {
    private static ConsensusObjectStoreHashSet consensusObjectStoreHashSet;

    private HashSet<ConsensusObject> consensusEntities = new HashSet();

    static {
        consensusObjectStoreHashSet = new ConsensusObjectStoreHashSet();
    }

    private ConsensusObjectStoreHashSet() {
    }

    @Override
    public boolean add(ConsensusObject consensusObject) {
        return consensusEntities.add(consensusObject);
    }

    @Override
    public ConsensusObject get(Long id) {
        Optional<ConsensusObject> searchResult =
                consensusEntities.stream().filter(consensusObject -> consensusObject.getId().equals(id)).findFirst();
        return searchResult.isPresent() ? searchResult.get() : null;
    }

    @Override
    public void remove(ConsensusObject consensusObject) {
        consensusEntities.remove(consensusObject);
    }

    @Override
    public boolean containsObject(Long id) {
        return consensusEntities.stream().anyMatch(consensusObject -> consensusObject.getId().equals(id));
    }

    public static ConsensusObjectStoreHashSet getInstance() {
        return consensusObjectStoreHashSet;
    }
}
