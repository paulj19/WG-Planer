package com.wg_planner.backend.utils.locking;

public class LockHandler {
    private static LockHandler lockHandler;

    static {
        lockHandler = new LockHandler();
    }

    public static LockHandler getInstance() {
        return lockHandler;
    }

    public synchronized Object getLock(Long objectToLockId) {
        if (LockStore.getInstance().getLock(objectToLockId) == null) {
            LockStore.getInstance().addLock(objectToLockId, Thread.currentThread().getId());
        }
        return LockStore.getInstance().getLock(objectToLockId);
    }

    public synchronized void unlock(Long objectToLockId) {
        if (LockStore.getInstance().getLock(objectToLockId) != null && LockStore.getInstance().getLock(objectToLockId).getId() == Thread.currentThread().getId()) {
            LockStore.getInstance().removeLock(objectToLockId);
        }
    }
}
