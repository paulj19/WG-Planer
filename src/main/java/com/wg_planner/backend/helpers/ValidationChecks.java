package com.wg_planner.backend.helpers;

import java.util.Collections;
import java.util.List;

public class ValidationChecks {
    public static class StringChecks {
        public static boolean isAlphaNumeric(String s) {
            String pattern = "^[a-zA-Z0-9]*$";
            return s.matches(pattern);
        }

    }

    public static <T extends Object> List<T> safe(List<T> other) {
        return other == null ? Collections.EMPTY_LIST : other;
    }
}
