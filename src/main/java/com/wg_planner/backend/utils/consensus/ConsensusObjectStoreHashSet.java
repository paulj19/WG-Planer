package com.wg_planner.backend.utils.consensus;

import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.utils.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ConsensusObjectStoreHashSet extends ConsensusObjectStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsensusObjectStoreHashSet.class);
    private static ConsensusObjectStoreHashSet consensusObjectStoreHashSet;

    private HashSet<ConsensusObject> consensusEntities = new HashSet();

    static {
        consensusObjectStoreHashSet = new ConsensusObjectStoreHashSet();
    }

    private ConsensusObjectStoreHashSet() {
    }

    @Override
    public boolean add(ConsensusObject consensusObject) {
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. ConsensusObjectStore addobject. Room initiating consensus {}, Consensus Type {}," +
                        "consensus object id {}",
                SessionHandler.getLoggedInResidentAccount().getId(), consensusObject.getRoomInitiatingConsensus().getId(), getClass().toString(),
                consensusObject.getId());
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
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. ConsensusObjectStore remove. Room initiating consensus {}, Consensus Type {}," +
                        "consensus object id {}",
                SessionHandler.getLoggedInResidentAccount().getId(), consensusObject.getRoomInitiatingConsensus().getId(), getClass().toString(),
                consensusObject.getId());
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
