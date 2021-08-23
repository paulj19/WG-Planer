package com.wg_planner.backend.utils.locking;

import com.wg_planner.views.utils.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LockRegisterHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LockRegisterHandler.class);
    private static LockRegisterHandler lockRegisterHandler;

    static {
        lockRegisterHandler = new LockRegisterHandler();
    }

    public static LockRegisterHandler getInstance() {
        return lockRegisterHandler;
    }

    //blocking the entire store DS. it is called on every thread operation, performance issue?
    public synchronized Object registerLock(Long objectToLockId) {
        if (LockRegister.getInstance().getLock(objectToLockId) == null) {
            LockRegister.getInstance().addLockCallingThread(objectToLockId, new CustomLock(Thread.currentThread().getId()));
        } else {
            LockRegister.getInstance().getLock(objectToLockId).addToThreadsRequestingLock(Thread.currentThread().getId());
        }
        return LockRegister.getInstance().getLock(objectToLockId);
    }

    public synchronized void unregisterLock(Long objectToLockId) {
        CustomLock customLock = LockRegister.getInstance().getLock(objectToLockId);
        if (customLock != null) {
            customLock.removeLockRequestFromThread(Thread.currentThread().getId());
            if (customLock.isThreadsRequestingLocksEmpty()) {
                LockRegister.getInstance().removeLockCallingThread(objectToLockId);
            }
        } else {
            LOGGER.warn("Resident Account id {}. Thread {} tried to unregister a lock which it has not registered",
                    SessionHandler.getLoggedInResidentAccount().getId(), Thread.currentThread().getId());
        }
    }
}
