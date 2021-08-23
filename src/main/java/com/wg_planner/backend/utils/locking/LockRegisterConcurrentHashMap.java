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
    public CustomLock addLockCallingThread(Long objectToLockId, CustomLock customLock) {
        return lockMap.putIfAbsent(objectToLockId, customLock);
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
