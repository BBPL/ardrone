package com.parrot.ardronetool.video;

public final class VideoStageEncodedRecorder {
    private static VideoStageEncodedRecorder instance;

    private VideoStageEncodedRecorder() {
    }

    private native int getCompleteRecorderStateNative();

    public static VideoStageEncodedRecorder instance() {
        if (instance == null) {
            instance = new VideoStageEncodedRecorder();
        }
        return instance;
    }

    public final native void comTimeout();

    public final native void enable(boolean z, long j);

    public final native void forceStop();

    public VideoEncodedRecorderState getCompleteRecorderState() {
        int completeRecorderStateNative = getCompleteRecorderStateNative();
        if (completeRecorderStateNative >= 0 && completeRecorderStateNative < VideoEncodedRecorderState.values().length) {
            return VideoEncodedRecorderState.values()[completeRecorderStateNative];
        }
        throw new IllegalStateException("Invalid complete recorder state");
    }

    public final native boolean getRecorderState();
}
