package com.wg_planner.backend.helpers;

public class ValidationChecks {
    public static class StringChecks {
        public static boolean isAlphaNumeric(String s){
            String pattern= "^[a-zA-Z0-9]*$";
            return s.matches(pattern);
        }
    }
}
