package com.wg_planner.backend.resident_admission;

import org.springframework.stereotype.Controller;

import java.util.Timer;
import java.util.TimerTask;

@Controller
public class EventTimerJava implements EventTimer {

    @Override
    public void setTimer(Object o, TimerRelapse onTimerRelapse, long timerDuration) {
        if (timerDuration > 0) {
            Timer initialTimer = new Timer();
            initialTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    onTimerRelapse.onTimerRelapse(o);
                }
            }, timerDuration);
        }
    }
}
