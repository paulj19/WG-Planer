package com.wg_planner.backend.utils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class HelperMethods {
    public static class StringChecks {
        public static boolean isAlphaNumeric(String s) {
            String pattern = "^[a-zA-Z0-9]*$";
            return s.matches(pattern);
        }

    }

    public static <T extends Object> List<T> safe(List<T> other) {
        return other == null ? Collections.EMPTY_LIST : other;
    }

    public static <T> Collector<T, ?, T> toSingleton() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }
}
