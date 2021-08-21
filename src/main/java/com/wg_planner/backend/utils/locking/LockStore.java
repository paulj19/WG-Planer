package com.wg_planner.backend.utils.locking;

public abstract class LockStore {
    public abstract CustomLock addLock(Long objectToLockId, long ownerThreadId);

    public abstract CustomLock getLock(Long objectToLockId);

    public abstract void removeLock(Long objectToLockId);

    public static LockStore getInstance() {
        return LockStoreConcurrentHashMap.getInstance();
    }
}
