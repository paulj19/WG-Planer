package com.wg_planner.backend.utils.consensus;

import com.wg_planner.backend.Service.ResidentAccountService;
import com.wg_planner.backend.entity.Room;
import com.wg_planner.backend.entity.Task;
import com.wg_planner.backend.resident_admission.EventTimer;
import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.utils.SessionHandler;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class ConsensusHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsensusHandler.class);
    private static ConsensusHandler consensusHandler;

    static {
        consensusHandler = new ConsensusHandler();
    }

    private ConsensusHandler() {
    }

    public static ConsensusHandler getInstance() {
        return consensusHandler;
    }


    public boolean processAccept(Task task, Room acceptor) {
        Validate.notNull(task, "argument must not be null");
        return processAccept(ConsensusObjectStore.getInstance().get(task.getId()), acceptor);
    }

    public boolean processAccept(Long id, Room acceptor) {
        if (ConsensusObjectStore.getInstance().get(id) == null) {
            return false;
        }
        return processAccept(ConsensusObjectStore.getInstance().get(id), acceptor);
    }

    public boolean processAccept(ConsensusObject consensusObject, Room acceptor) {
        Validate.notNull(consensusObject, "argument must not be null");
        Validate.notNull(acceptor, "argument must not be null");
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. ConsensusHandler processAccept. Room initiating consensus {}, Consensus Type {}, " +
                        "consensus object id {} Room acceptor {}.",
                SessionHandler.getLoggedInResidentAccount().getId(), consensusObject.getRoomInitiatingConsensus().getId(), consensusObject.getClass().toString(),
                consensusObject.getId(), acceptor.getId());
        consensusObject.addAcceptingRoom(acceptor);
        if (consensusObject.test(consensusObject)) {
            consensusObject.getConsensusDone().onConsensusDone();
        }
        return true;
    }

    public boolean processReject(Long id) {
        Validate.notNull(id, "argument must not be null");
        return processReject(ConsensusObjectStore.getInstance().get(id));
    }

    public boolean processReject(ConsensusObject consensusObject) {
        LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. ConsensusHandler processAccept. Room initiating consensus {}, Consensus Type {}, " +
                        "consensus object id {}.",
                SessionHandler.getLoggedInResidentAccount().getId(), consensusObject.getRoomInitiatingConsensus(), consensusObject.getClass().toString(),
                consensusObject.getId());
        return removeConsensusObjectFromStore(consensusObject);
    }

    private boolean removeConsensusObjectFromStore(ConsensusObject consensusObject) {
        if (consensusObject == null) {
            return false;
        }
        consensusObject.roomsAccepting = null; //remove all the rooms that had accepted?
        ConsensusObjectStore.getInstance().remove(consensusObject);//delete consensus object from consensus store
        return true;
    }

    public boolean add(ConsensusObject consensusObject) {
        boolean returnVal = ConsensusObjectStore.getInstance().add(consensusObject);
        setTimer(consensusObject);
        return returnVal;
    }

    private void setTimer(ConsensusObject consensusObject) {
        EventTimer.getInstance().setTimer(consensusObject, o -> removeConsensusObjectFromStore(consensusObject),
                consensusObject.getTimeoutInterval());
    }

    public ConsensusObject get(Long id) {
        return ConsensusObjectStore.getInstance().get(id);
    }

    public boolean isObjectWaitingForConsensus(Long objectId) {
        return ConsensusObjectStore.getInstance().containsObject(objectId);
    }
}
