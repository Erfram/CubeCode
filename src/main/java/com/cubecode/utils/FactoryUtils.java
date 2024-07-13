package com.cubecode.utils;

public class FactoryUtils {
    public static boolean isValidRegistriesId(String id) {
        if (id == null || id.isEmpty()) {
            return false;
        }

        String regex = "^[a-z0-9_.-]+$";

        return id.matches(regex);
    }
}
