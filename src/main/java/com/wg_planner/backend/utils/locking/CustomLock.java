package com.wg_planner.backend.utils.locking;

public class CustomLock {
    long id;

    public CustomLock(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
