package com.parrot.ardronetool;

public class NavDataClient {
    NavDataClient() {
    }

    public final native int getNumRetries();

    public final native boolean init();

    public final native boolean resume();

    public final native boolean shutdown();

    public final native boolean suspend();
}
