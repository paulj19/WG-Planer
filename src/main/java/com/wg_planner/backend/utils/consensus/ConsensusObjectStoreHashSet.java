package com.wg_planner.backend.utils.consensus;

import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.utils.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;

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
    public ConsensusObject get(Long id) {
        Optional<ConsensusObject> searchResult =
                consensusEntities.stream().filter(consensusObject -> consensusObject.getId().equals(id)).findFirst();
        return searchResult.isPresent() ? searchResult.get() : null;
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
    public boolean containsObject(Long id) {
        return consensusEntities.stream().anyMatch(consensusObject -> consensusObject.getId().equals(id));
    }

    public static ConsensusObjectStoreHashSet getInstance() {
        return consensusObjectStoreHashSet;
    }
}
