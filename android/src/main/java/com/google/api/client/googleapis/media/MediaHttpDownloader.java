package com.google.api.client.googleapis.media;

import com.google.api.client.http.ExponentialBackOffPolicy;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.IOUtils;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.io.OutputStream;

public final class MediaHttpDownloader {
    public static final int MAXIMUM_CHUNK_SIZE = 33554432;
    private boolean backOffPolicyEnabled = true;
    private long bytesDownloaded;
    private int chunkSize = MAXIMUM_CHUNK_SIZE;
    private boolean directDownloadEnabled = false;
    private DownloadState downloadState = DownloadState.NOT_STARTED;
    private long lastBytePos = -1;
    private long mediaContentLength;
    private MediaHttpDownloaderProgressListener progressListener;
    private final HttpRequestFactory requestFactory;
    private final HttpTransport transport;

    public enum DownloadState {
        NOT_STARTED,
        MEDIA_IN_PROGRESS,
        MEDIA_COMPLETE
    }

    public MediaHttpDownloader(HttpTransport httpTransport, HttpRequestInitializer httpRequestInitializer) {
        this.transport = (HttpTransport) Preconditions.checkNotNull(httpTransport);
        this.requestFactory = httpRequestInitializer == null ? httpTransport.createRequestFactory() : httpTransport.createRequestFactory(httpRequestInitializer);
    }

    private HttpResponse executeCurrentRequest(long j, GenericUrl genericUrl, HttpHeaders httpHeaders, OutputStream outputStream) throws IOException {
        HttpRequest buildGetRequest = this.requestFactory.buildGetRequest(genericUrl);
        if (httpHeaders != null) {
            buildGetRequest.getHeaders().putAll(httpHeaders);
        }
        if (!(this.bytesDownloaded == 0 && j == -1)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("bytes=").append(this.bytesDownloaded).append("-");
            if (j != -1) {
                stringBuilder.append(j);
            }
            buildGetRequest.getHeaders().setRange(stringBuilder.toString());
        }
        if (this.backOffPolicyEnabled) {
            buildGetRequest.setBackOffPolicy(new ExponentialBackOffPolicy());
        }
        HttpResponse execute = buildGetRequest.execute();
        try {
            IOUtils.copy(execute.getContent(), outputStream);
            return execute;
        } finally {
            execute.disconnect();
        }
    }

    private long getNextByteIndex(String str) {
        return str == null ? 0 : Long.parseLong(str.substring(str.indexOf(45) + 1, str.indexOf(47))) + 1;
    }

    private void setMediaContentLength(String str) {
        if (str != null && this.mediaContentLength == 0) {
            this.mediaContentLength = Long.parseLong(str.substring(str.indexOf(47) + 1));
        }
    }

    private void updateStateAndNotifyListener(DownloadState downloadState) throws IOException {
        this.downloadState = downloadState;
        if (this.progressListener != null) {
            this.progressListener.progressChanged(this);
        }
    }

    public void download(GenericUrl genericUrl, HttpHeaders httpHeaders, OutputStream outputStream) throws IOException {
        Preconditions.checkArgument(this.downloadState == DownloadState.NOT_STARTED);
        genericUrl.put("alt", (Object) "media");
        if (this.directDownloadEnabled) {
            updateStateAndNotifyListener(DownloadState.MEDIA_IN_PROGRESS);
            this.mediaContentLength = executeCurrentRequest(this.lastBytePos, genericUrl, httpHeaders, outputStream).getHeaders().getContentLength().longValue();
            this.bytesDownloaded = this.mediaContentLength;
            updateStateAndNotifyListener(DownloadState.MEDIA_COMPLETE);
            return;
        }
        while (true) {
            long j = (this.bytesDownloaded + ((long) this.chunkSize)) - 1;
            if (this.lastBytePos != -1) {
                j = Math.min(this.lastBytePos, j);
            }
            String contentRange = executeCurrentRequest(j, genericUrl, httpHeaders, outputStream).getHeaders().getContentRange();
            j = getNextByteIndex(contentRange);
            setMediaContentLength(contentRange);
            if (this.mediaContentLength <= j) {
                this.bytesDownloaded = this.mediaContentLength;
                updateStateAndNotifyListener(DownloadState.MEDIA_COMPLETE);
                return;
            }
            this.bytesDownloaded = j;
            updateStateAndNotifyListener(DownloadState.MEDIA_IN_PROGRESS);
        }
    }

    public void download(GenericUrl genericUrl, OutputStream outputStream) throws IOException {
        download(genericUrl, null, outputStream);
    }

    public int getChunkSize() {
        return this.chunkSize;
    }

    public DownloadState getDownloadState() {
        return this.downloadState;
    }

    public long getLastBytePosition() {
        return this.lastBytePos;
    }

    public long getNumBytesDownloaded() {
        return this.bytesDownloaded;
    }

    public double getProgress() {
        return this.mediaContentLength == 0 ? 0.0d : ((double) this.bytesDownloaded) / ((double) this.mediaContentLength);
    }

    public MediaHttpDownloaderProgressListener getProgressListener() {
        return this.progressListener;
    }

    public HttpTransport getTransport() {
        return this.transport;
    }

    public boolean isBackOffPolicyEnabled() {
        return this.backOffPolicyEnabled;
    }

    public boolean isDirectDownloadEnabled() {
        return this.directDownloadEnabled;
    }

    public MediaHttpDownloader setBackOffPolicyEnabled(boolean z) {
        this.backOffPolicyEnabled = z;
        return this;
    }

    public MediaHttpDownloader setBytesDownloaded(long j) {
        Preconditions.checkArgument(j >= 0);
        this.bytesDownloaded = j;
        return this;
    }

    public MediaHttpDownloader setChunkSize(int i) {
        boolean z = i > 0 && i <= MAXIMUM_CHUNK_SIZE;
        Preconditions.checkArgument(z);
        this.chunkSize = i;
        return this;
    }

    public MediaHttpDownloader setContentRange(long j, int i) {
        Preconditions.checkArgument(((long) i) >= j);
        setBytesDownloaded(j);
        this.lastBytePos = (long) i;
        return this;
    }

    public MediaHttpDownloader setDirectDownloadEnabled(boolean z) {
        this.directDownloadEnabled = z;
        return this;
    }

    public MediaHttpDownloader setProgressListener(MediaHttpDownloaderProgressListener mediaHttpDownloaderProgressListener) {
        this.progressListener = mediaHttpDownloaderProgressListener;
        return this;
    }
}
