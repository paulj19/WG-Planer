package com.wg_planner.backend.resident_admission;

public interface EventTimer {
    void setTimer(Object o, TimerRelapse onTimerRelapse, long timerDuration);
}
