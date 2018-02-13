package com.google.gson.internal;

public final class Pair<FIRST, SECOND> {
    public final FIRST first;
    public final SECOND second;

    public Pair(FIRST first, SECOND second) {
        this.first = first;
        this.second = second;
    }

    private static boolean equal(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public boolean equals(Object obj) {
        if (obj instanceof Pair) {
            Pair pair = (Pair) obj;
            if (equal(this.first, pair.first) && equal(this.second, pair.second)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = this.first != null ? this.first.hashCode() : 0;
        if (this.second != null) {
            i = this.second.hashCode();
        }
        return (hashCode * 17) + (i * 17);
    }

    public String toString() {
        return String.format("{%s,%s}", new Object[]{this.first, this.second});
    }
}
