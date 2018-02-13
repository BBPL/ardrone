package com.parrot.ardronetool.video;

public class VideoPrrt {
    private int frames;
    private final boolean initialized;
    private int prrtHandle = 0;

    public VideoPrrt(String str) {
        this.initialized = initPrrtData(str);
    }

    private native void freePrrtData();

    public static int getFrameRate(String str) {
        return getVideoFramerate(str);
    }

    private native int getVideoAltitude(int i);

    private static native int getVideoFramerate(String str);

    private native double getVideoTimestamp(int i);

    private native boolean initPrrtData(String str);

    protected void finalize() throws Throwable {
        try {
            release();
        } finally {
            super.finalize();
        }
    }

    public int getAltitude(int i) {
        if (this.prrtHandle != 0) {
            return getVideoAltitude(i);
        }
        throw new IllegalStateException("Object is released");
    }

    public int getFrames() {
        return this.frames;
    }

    public double getTimestamp(int i) {
        if (this.prrtHandle != 0) {
            return getVideoTimestamp(i);
        }
        throw new IllegalStateException("Object is released");
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public void release() {
        if (this.prrtHandle != 0) {
            freePrrtData();
        }
    }
}
