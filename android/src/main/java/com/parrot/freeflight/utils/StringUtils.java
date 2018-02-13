package com.parrot.freeflight.utils;

public class StringUtils {
    private StringUtils() {
    }

    public static String capitalize(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        char charAt = str.charAt(0);
        return !Character.isUpperCase(charAt) ? Character.toUpperCase(charAt) + str.substring(1) : str;
    }

    public static String getExceptionDetailedMessage(Throwable th) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(th.getLocalizedMessage());
        for (Throwable cause = th.getCause(); cause != null; cause = cause.getCause()) {
            stringBuilder.append("\nCaused by: ").append(cause.getLocalizedMessage());
        }
        return stringBuilder.toString();
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
