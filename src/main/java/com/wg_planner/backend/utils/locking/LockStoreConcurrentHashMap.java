package com.wg_planner.backend.utils.locking;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class LockStoreConcurrentHashMap extends LockStore {
    private static LockStoreConcurrentHashMap lockStoreConcurrentHashMap;
    private ConcurrentHashMap<Long, CustomLock> lockMap = new ConcurrentHashMap<>();

    static {
        lockStoreConcurrentHashMap = new LockStoreConcurrentHashMap();
    }

    public static LockStoreConcurrentHashMap getInstance() {
        return lockStoreConcurrentHashMap;
    }

    @Override
    public CustomLock addLock(Long objectToLockId, long ownerThreadId) {
        return lockMap.putIfAbsent(objectToLockId, new CustomLock(ownerThreadId));
    }

    @Override
    public CustomLock getLock(Long objectToLockId) {
        return lockMap.get(objectToLockId);
    }

    @Override
    public void removeLock(Long objectToLockId) {
        lockMap.remove(objectToLockId);
    }
}
