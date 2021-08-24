package com.wg_planner.backend.resident_admission;

import com.wg_planner.backend.utils.LogHandler;
import com.wg_planner.views.utils.SessionHandler;
import com.wg_planner.views.utils.UINotificationHandler.UIEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

public class EventTimerJava extends EventTimer {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventTimerJava.class);
    private static EventTimerJava eventTimerJava;

    static {
        eventTimerJava = new EventTimerJava();
    }

    private EventTimerJava() {
    }

    @Override
    public void setTimer(Object o, TimerRelapse onTimerRelapse, long timerDuration) {
        if (timerDuration > 0) {
            Timer initialTimer = new Timer();
            LOGGER.info(LogHandler.getTestRun(), "Resident Account id {}. Setting timer duration{}.",
                    SessionHandler.getLoggedInResidentAccount().getId(), timerDuration);
            initialTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    onTimerRelapse.onTimerRelapse(o);
                }
            }, timerDuration);
        }
    }

    public static EventTimer getInstance() {
        return eventTimerJava;
    }
}
