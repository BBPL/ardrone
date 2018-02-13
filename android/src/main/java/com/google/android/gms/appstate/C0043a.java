package com.google.android.gms.appstate;

import com.google.android.gms.internal.C0241r;

public final class C0043a implements AppState {
    private final int f8h;
    private final String f9i;
    private final byte[] f10j;
    private final boolean f11k;
    private final String f12l;
    private final byte[] f13m;

    public C0043a(AppState appState) {
        this.f8h = appState.getKey();
        this.f9i = appState.getLocalVersion();
        this.f10j = appState.getLocalData();
        this.f11k = appState.hasConflict();
        this.f12l = appState.getConflictVersion();
        this.f13m = appState.getConflictData();
    }

    static int m0a(AppState appState) {
        return C0241r.hashCode(Integer.valueOf(appState.getKey()), appState.getLocalVersion(), appState.getLocalData(), Boolean.valueOf(appState.hasConflict()), appState.getConflictVersion(), appState.getConflictData());
    }

    static boolean m1a(AppState appState, Object obj) {
        if (!(obj instanceof AppState)) {
            return false;
        }
        if (appState != obj) {
            AppState appState2 = (AppState) obj;
            if (!(C0241r.m1200a(Integer.valueOf(appState2.getKey()), Integer.valueOf(appState.getKey())) && C0241r.m1200a(appState2.getLocalVersion(), appState.getLocalVersion()) && C0241r.m1200a(appState2.getLocalData(), appState.getLocalData()) && C0241r.m1200a(Boolean.valueOf(appState2.hasConflict()), Boolean.valueOf(appState.hasConflict())) && C0241r.m1200a(appState2.getConflictVersion(), appState.getConflictVersion()) && C0241r.m1200a(appState2.getConflictData(), appState.getConflictData()))) {
                return false;
            }
        }
        return true;
    }

    static String m2b(AppState appState) {
        return C0241r.m1201c(appState).m1199a("Key", Integer.valueOf(appState.getKey())).m1199a("LocalVersion", appState.getLocalVersion()).m1199a("LocalData", appState.getLocalData()).m1199a("HasConflict", Boolean.valueOf(appState.hasConflict())).m1199a("ConflictVersion", appState.getConflictVersion()).m1199a("ConflictData", appState.getConflictData()).toString();
    }

    public AppState m3a() {
        return this;
    }

    public boolean equals(Object obj) {
        return C0043a.m1a(this, obj);
    }

    public /* synthetic */ Object freeze() {
        return m3a();
    }

    public byte[] getConflictData() {
        return this.f13m;
    }

    public String getConflictVersion() {
        return this.f12l;
    }

    public int getKey() {
        return this.f8h;
    }

    public byte[] getLocalData() {
        return this.f10j;
    }

    public String getLocalVersion() {
        return this.f9i;
    }

    public boolean hasConflict() {
        return this.f11k;
    }

    public int hashCode() {
        return C0043a.m0a(this);
    }

    public boolean isDataValid() {
        return true;
    }

    public String toString() {
        return C0043a.m2b(this);
    }
}
