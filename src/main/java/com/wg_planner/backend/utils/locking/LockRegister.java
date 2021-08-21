package com.wg_planner.backend.utils.locking;

public abstract class LockRegister {
    public abstract CustomLock addLockCallingThread(Long objectToLockId, long ownerThreadId);

    public abstract CustomLock getLock(Long objectToLockId);

    public abstract void removeLockCallingThread(Long objectToLockId);

    public static LockRegister getInstance() {
        return LockRegisterConcurrentHashMap.getInstance();
    }
}
