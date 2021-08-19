package com.wg_planner.backend.utils.consensus;

import org.springframework.stereotype.Component;

import java.util.*;

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
    public ConsensusObject getById(String id) {
        Optional<ConsensusObject> searchResult =
                consensusEntities.stream().filter(consensusObject -> consensusObject.getId().equals(id)).findFirst();
        return searchResult.orElse(null);
    }

    @Override
    public ConsensusObject getByObjectForConsensus(Object objectForConsensus) {
        return consensusEntities.stream().filter(consensusObject -> Objects.equals(consensusObject.getRelatedObject(), objectForConsensus)).findFirst().get();
    }

    @Override
    public void remove(ConsensusObject consensusObject) {
        consensusEntities.remove(consensusObject);
    }

    @Override
    public boolean containsObject(Object objectForConsensus) {
        return consensusEntities.stream().anyMatch(consensusObject -> Objects.equals(consensusObject.getRelatedObject(),objectForConsensus));
    }

    @Override
    public Collection<ConsensusObject> getAllConsensusObjects() {
        return Collections.unmodifiableSet(consensusEntities);
    }

    public static ConsensusObjectStoreHashSet getInstance() {
        return consensusObjectStoreHashSet;
    }

}
