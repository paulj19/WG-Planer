package com.wg_planner.backend.utils.locking;

public class LockRegisterHandler {
    private static LockRegisterHandler lockRegisterHandler;

    static {
        lockRegisterHandler = new LockRegisterHandler();
    }

    public static LockRegisterHandler getInstance() {
        return lockRegisterHandler;
    }

    public synchronized Object registerLock(Long objectToLockId) {
        if (LockRegister.getInstance().getLock(objectToLockId) == null) {
            LockRegister.getInstance().addLockCallingThread(objectToLockId, Thread.currentThread().getId());
        }
        return LockRegister.getInstance().getLock(objectToLockId);
    }

    public synchronized void unregisterLock(Long objectToLockId) {
        if (LockRegister.getInstance().getLock(objectToLockId) != null && LockRegister.getInstance().getLock(objectToLockId).getId() == Thread.currentThread().getId()) {
            LockRegister.getInstance().removeLockCallingThread(objectToLockId);
        }
    }
}
