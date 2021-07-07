package com.wg_planner.backend.utils.consensus;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;

@Component
public class ConsensusObjectStoreHashSet implements ConsensusObjectStore {
    private HashSet<ConsensusObject> consensusEntities = new HashSet();

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
    public boolean containsObject(Long id) {
        return consensusEntities.stream().anyMatch(consensusObject -> consensusObject.getId().equals(id));
    }
}
