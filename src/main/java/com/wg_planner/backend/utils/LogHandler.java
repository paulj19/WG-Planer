package com.wg_planner.backend.utils;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class LogHandler {
    private static final Marker TEST_RUN = MarkerFactory.getMarker("TEST_RUN");
    private static final Marker STRANGE = MarkerFactory.getMarker("STRANGE");

    public static Marker getTestRun() {
        return TEST_RUN;
    }

    public static Marker getStrange() {
        return STRANGE;
    }
}
