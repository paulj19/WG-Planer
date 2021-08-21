package com.wg_planner.backend.utils.locking;

import java.util.concurrent.ConcurrentHashMap;

public class LockRegisterConcurrentHashMap extends LockRegister {
    private static LockRegisterConcurrentHashMap lockStoreConcurrentHashMap;
    private ConcurrentHashMap<Long, CustomLock> lockMap = new ConcurrentHashMap<>();

    static {
        lockStoreConcurrentHashMap = new LockRegisterConcurrentHashMap();
    }

    public static LockRegisterConcurrentHashMap getInstance() {
        return lockStoreConcurrentHashMap;
    }

    @Override
    public CustomLock addLockCallingThread(Long objectToLockId, long ownerThreadId) {
        return lockMap.putIfAbsent(objectToLockId, new CustomLock(ownerThreadId));
    }

    @Override
    public CustomLock getLock(Long objectToLockId) {
        return lockMap.get(objectToLockId);
    }

    @Override
    public void removeLockCallingThread(Long objectToLockId) {
        lockMap.remove(objectToLockId);
    }
}
