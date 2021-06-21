package com.wg_planner.backend.resident_admission;

import org.springframework.stereotype.Controller;

import java.util.Timer;
import java.util.TimerTask;

@Controller
public class AdmissionTimerJava implements AdmissionTimer {

    @Override
    public void setTimer(AdmissionCode admissionCode, TimerRelapse onTimerRelapse, long timerDuration) {
        Timer initialTimer = new Timer();
        initialTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                onTimerRelapse.onTimerRelapse(admissionCode);
            }
        }, timerDuration);
    }
}
