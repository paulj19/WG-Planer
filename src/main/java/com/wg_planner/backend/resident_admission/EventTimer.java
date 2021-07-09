package com.wg_planner.backend.resident_admission;

public abstract class EventTimer {
    public abstract void setTimer(Object o, TimerRelapse onTimerRelapse, long timerDuration);
    public static EventTimer getInstance() {
        return EventTimerJava.getInstance();
    }
}
