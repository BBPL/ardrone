package com.google.android.gms.internal;

public final class bc {
    public static String m760G(int i) {
        switch (i) {
            case 0:
                return "PUBLIC";
            case 1:
                return "SOCIAL";
            default:
                throw new IllegalArgumentException("Unknown leaderboard collection: " + i);
        }
    }
}
