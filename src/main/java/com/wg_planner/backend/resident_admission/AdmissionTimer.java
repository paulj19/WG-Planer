package com.wg_planner.backend.resident_admission;

public interface AdmissionTimer {
    void setTimer(AdmissionCode admissionCode, TimerRelapse onTimerRelapse, long timerDuration);
}
