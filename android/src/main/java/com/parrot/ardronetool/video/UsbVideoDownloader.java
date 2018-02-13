package com.parrot.ardronetool.video;

import java.util.List;

public class UsbVideoDownloader {
    private static final String TAG = UsbVideoDownloader.class.getSimpleName();
    private UsbVideoDownloadProgressListener listener;

    public enum Status {
        OK,
        FAIL,
        PROGRESS,
        TIMEOUT,
        CANCEL
    }

    public interface UsbVideoDownloadProgressListener {
        void onProgress(Status status, VideoItem videoItem, float f);
    }

    public static class VideoItem {
        public int size;
        public String userboxName;
        public String videoName;

        public boolean equals(Object obj) {
            if (!(obj instanceof VideoItem)) {
                return false;
            }
            VideoItem videoItem = (VideoItem) obj;
            return this.userboxName.equals(videoItem.userboxName) && this.videoName.equals(videoItem.videoName);
        }
    }

    private final native int deleteItemNative(String str, String str2);

    public native int cancelAllDownloads();

    public boolean deleteItem(VideoItem videoItem) {
        if (videoItem != null) {
            return deleteItemNative(videoItem.userboxName, videoItem.videoName) >= 0;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public native void downloadItem(VideoItem videoItem, String str);

    public native List<VideoItem> getMediaItems();

    protected void onVideoProgressChanged(VideoItem videoItem, int i, float f) {
        if (this.listener != null) {
            this.listener.onProgress(Status.values()[i], videoItem, f);
        }
    }

    public void setVideoDownloadProgressListener(UsbVideoDownloadProgressListener usbVideoDownloadProgressListener) {
        this.listener = usbVideoDownloadProgressListener;
    }
}
