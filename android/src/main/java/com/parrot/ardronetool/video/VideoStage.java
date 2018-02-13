package com.parrot.ardronetool.video;

public class VideoStage {
    public static final native int getVideoMaxRetries();

    public final native int getNumRetries();

    public final native void resumeThread();

    public final native void suspendThread();
}
