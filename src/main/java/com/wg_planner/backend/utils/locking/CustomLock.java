package com.wg_planner.backend.utils.locking;

import java.util.HashSet;

public class CustomLock {
    HashSet<Long> threadsRequestingLock = new HashSet<>();

    public CustomLock(Long threadId) {
        addToThreadsRequestingLock(threadId);
    }

    public synchronized boolean addToThreadsRequestingLock(Long threadId) {
        return threadsRequestingLock.add(threadId);
    }

    public synchronized boolean containsLockRequestFromThread(Long threadId) {
        return threadsRequestingLock.contains(threadId);
    }

    public synchronized boolean removeLockRequestFromThread(Long threadId) {
        return threadsRequestingLock.remove(threadId);
    }
    public synchronized boolean isThreadsRequestingLocksEmpty() {
        return threadsRequestingLock.isEmpty();
    }
}
