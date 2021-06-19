package com.wg_planner.backend.resident_admission;

public interface TimerRelapse {
    void onTimerElapse(AdmissionCode admissionCode);
}
