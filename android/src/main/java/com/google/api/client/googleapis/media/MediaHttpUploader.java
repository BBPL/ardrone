package com.google.api.client.googleapis.media;

import com.google.api.client.googleapis.MethodOverride;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.EmptyContent;
import com.google.api.client.http.ExponentialBackOffPolicy;
import com.google.api.client.http.GZipEncoding;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.MultipartContent;
import com.google.api.client.util.ByteStreams;
import com.google.api.client.util.Preconditions;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.mortbay.jetty.security.Constraint;
import org.mortbay.util.URIUtil;

public final class MediaHttpUploader {
    public static final String CONTENT_LENGTH_HEADER = "X-Upload-Content-Length";
    public static final String CONTENT_TYPE_HEADER = "X-Upload-Content-Type";
    public static final int DEFAULT_CHUNK_SIZE = 10485760;
    private static final int KB = 1024;
    static final int MB = 1048576;
    public static final int MINIMUM_CHUNK_SIZE = 262144;
    private boolean backOffPolicyEnabled = true;
    private long bytesUploaded;
    private Byte cachedByte;
    private int chunkSize = DEFAULT_CHUNK_SIZE;
    private InputStream contentInputStream;
    private HttpRequest currentRequest;
    private byte[] currentRequestContentBuffer;
    private boolean directUploadEnabled;
    private boolean disableGZipContent;
    private HttpHeaders initiationHeaders = new HttpHeaders();
    private String initiationRequestMethod = "POST";
    private boolean isMediaContentLengthCalculated;
    private final AbstractInputStreamContent mediaContent;
    private long mediaContentLength;
    private HttpContent metadata;
    private MediaHttpUploaderProgressListener progressListener;
    private final HttpRequestFactory requestFactory;
    private final HttpTransport transport;
    private UploadState uploadState = UploadState.NOT_STARTED;

    public enum UploadState {
        NOT_STARTED,
        INITIATION_STARTED,
        INITIATION_COMPLETE,
        MEDIA_IN_PROGRESS,
        MEDIA_COMPLETE
    }

    public MediaHttpUploader(AbstractInputStreamContent abstractInputStreamContent, HttpTransport httpTransport, HttpRequestInitializer httpRequestInitializer) {
        this.mediaContent = (AbstractInputStreamContent) Preconditions.checkNotNull(abstractInputStreamContent);
        this.transport = (HttpTransport) Preconditions.checkNotNull(httpTransport);
        this.requestFactory = httpRequestInitializer == null ? httpTransport.createRequestFactory() : httpTransport.createRequestFactory(httpRequestInitializer);
    }

    private HttpResponse executeCurrentRequest(HttpRequest httpRequest) throws IOException {
        new MethodOverride().intercept(httpRequest);
        httpRequest.setThrowExceptionOnExecuteError(false);
        httpRequest.setRetryOnExecuteIOException(true);
        return httpRequest.execute();
    }

    private HttpResponse executeCurrentRequestWithBackOffAndGZip(HttpRequest httpRequest) throws IOException {
        if (this.backOffPolicyEnabled) {
            httpRequest.setBackOffPolicy(new ExponentialBackOffPolicy());
        }
        if (!(this.disableGZipContent || (httpRequest.getContent() instanceof EmptyContent))) {
            httpRequest.setEncoding(new GZipEncoding());
        }
        return executeCurrentRequest(httpRequest);
    }

    private HttpResponse executeUploadInitiation(GenericUrl genericUrl) throws IOException {
        updateStateAndNotifyListener(UploadState.INITIATION_STARTED);
        genericUrl.put("uploadType", (Object) "resumable");
        HttpRequest buildRequest = this.requestFactory.buildRequest(this.initiationRequestMethod, genericUrl, this.metadata == null ? new EmptyContent() : this.metadata);
        this.initiationHeaders.set(CONTENT_TYPE_HEADER, this.mediaContent.getType());
        if (getMediaContentLength() >= 0) {
            this.initiationHeaders.set(CONTENT_LENGTH_HEADER, Long.valueOf(getMediaContentLength()));
        }
        buildRequest.getHeaders().putAll(this.initiationHeaders);
        HttpResponse executeCurrentRequestWithBackOffAndGZip = executeCurrentRequestWithBackOffAndGZip(buildRequest);
        try {
            updateStateAndNotifyListener(UploadState.INITIATION_COMPLETE);
            return executeCurrentRequestWithBackOffAndGZip;
        } catch (Throwable th) {
            executeCurrentRequestWithBackOffAndGZip.disconnect();
        }
    }

    private long getMediaContentLength() throws IOException {
        if (!this.isMediaContentLengthCalculated) {
            this.mediaContentLength = this.mediaContent.getLength();
            this.isMediaContentLengthCalculated = true;
        }
        return this.mediaContentLength;
    }

    private long getNextByteIndex(String str) {
        return str == null ? 0 : Long.parseLong(str.substring(str.indexOf(45) + 1)) + 1;
    }

    private void setContentAndHeadersOnCurrentRequest(long j) throws IOException {
        String valueOf;
        int i;
        HttpContent httpContent;
        int min = getMediaContentLength() >= 0 ? (int) Math.min((long) this.chunkSize, getMediaContentLength() - j) : this.chunkSize;
        if (getMediaContentLength() >= 0) {
            this.contentInputStream.mark(min);
            InputStreamContent closeInputStream = new InputStreamContent(this.mediaContent.getType(), ByteStreams.limit(this.contentInputStream, (long) min)).setRetrySupported(true).setLength((long) min).setCloseInputStream(false);
            valueOf = String.valueOf(getMediaContentLength());
            i = min;
            httpContent = closeInputStream;
        } else {
            int i2;
            int read;
            int i3;
            if (this.currentRequestContentBuffer == null) {
                i2 = this.cachedByte == null ? min + 1 : min;
                this.currentRequestContentBuffer = new byte[(min + 1)];
                if (this.cachedByte != null) {
                    this.currentRequestContentBuffer[0] = this.cachedByte.byteValue();
                }
                read = ByteStreams.read(this.contentInputStream, this.currentRequestContentBuffer, (min + 1) - i2, i2);
                i3 = 0;
            } else {
                i2 = (int) ((((long) this.chunkSize) - (j - this.bytesUploaded)) + 1);
                i3 = (int) (j - this.bytesUploaded);
                read = i2;
            }
            if (read < i2) {
                min = Math.max(0, read);
                if (this.cachedByte != null) {
                    min++;
                }
                valueOf = String.valueOf(((long) min) + j);
                i = min;
            } else {
                this.cachedByte = Byte.valueOf(this.currentRequestContentBuffer[min]);
                valueOf = Constraint.ANY_ROLE;
                i = min;
            }
            httpContent = new ByteArrayContent(this.mediaContent.getType(), this.currentRequestContentBuffer, i3, i);
        }
        this.currentRequest.setContent(httpContent);
        if (i == 0) {
            this.currentRequest.getHeaders().setContentRange("bytes */0");
        } else {
            this.currentRequest.getHeaders().setContentRange("bytes " + j + "-" + ((((long) i) + j) - 1) + URIUtil.SLASH + valueOf);
        }
    }

    private void updateStateAndNotifyListener(UploadState uploadState) throws IOException {
        this.uploadState = uploadState;
        if (this.progressListener != null) {
            this.progressListener.progressChanged(this);
        }
    }

    public int getChunkSize() {
        return this.chunkSize;
    }

    public boolean getDisableGZipContent() {
        return this.disableGZipContent;
    }

    public HttpHeaders getInitiationHeaders() {
        return this.initiationHeaders;
    }

    public String getInitiationRequestMethod() {
        return this.initiationRequestMethod;
    }

    public HttpContent getMediaContent() {
        return this.mediaContent;
    }

    public HttpContent getMetadata() {
        return this.metadata;
    }

    public long getNumBytesUploaded() {
        return this.bytesUploaded;
    }

    public double getProgress() throws IOException {
        Preconditions.checkArgument(getMediaContentLength() >= 0, "Cannot call getProgress() if the specified AbstractInputStreamContent has no content length. Use  getNumBytesUploaded() to denote progress instead.");
        return getMediaContentLength() == 0 ? 0.0d : ((double) this.bytesUploaded) / ((double) getMediaContentLength());
    }

    public MediaHttpUploaderProgressListener getProgressListener() {
        return this.progressListener;
    }

    public HttpTransport getTransport() {
        return this.transport;
    }

    public UploadState getUploadState() {
        return this.uploadState;
    }

    public boolean isBackOffPolicyEnabled() {
        return this.backOffPolicyEnabled;
    }

    public boolean isDirectUploadEnabled() {
        return this.directUploadEnabled;
    }

    public void serverErrorCallback() throws IOException {
        Preconditions.checkNotNull(this.currentRequest, "The current request should not be null");
        HttpRequest buildPutRequest = this.requestFactory.buildPutRequest(this.currentRequest.getUrl(), new EmptyContent());
        buildPutRequest.getHeaders().setContentRange("bytes */" + (getMediaContentLength() >= 0 ? Long.valueOf(getMediaContentLength()) : Constraint.ANY_ROLE));
        HttpResponse executeCurrentRequestWithBackOffAndGZip = executeCurrentRequestWithBackOffAndGZip(buildPutRequest);
        try {
            long nextByteIndex = getNextByteIndex(executeCurrentRequestWithBackOffAndGZip.getHeaders().getRange());
            String location = executeCurrentRequestWithBackOffAndGZip.getHeaders().getLocation();
            if (location != null) {
                this.currentRequest.setUrl(new GenericUrl(location));
            }
            if (getMediaContentLength() >= 0) {
                this.contentInputStream.reset();
                long j = this.bytesUploaded - nextByteIndex;
                Preconditions.checkState(j == this.contentInputStream.skip(j));
            }
            setContentAndHeadersOnCurrentRequest(nextByteIndex);
        } finally {
            executeCurrentRequestWithBackOffAndGZip.disconnect();
        }
    }

    public MediaHttpUploader setBackOffPolicyEnabled(boolean z) {
        this.backOffPolicyEnabled = z;
        return this;
    }

    public MediaHttpUploader setChunkSize(int i) {
        boolean z = i > 0 && i % 262144 == 0;
        Preconditions.checkArgument(z);
        this.chunkSize = i;
        return this;
    }

    public MediaHttpUploader setDirectUploadEnabled(boolean z) {
        this.directUploadEnabled = z;
        return this;
    }

    public MediaHttpUploader setDisableGZipContent(boolean z) {
        this.disableGZipContent = z;
        return this;
    }

    public MediaHttpUploader setInitiationHeaders(HttpHeaders httpHeaders) {
        this.initiationHeaders = httpHeaders;
        return this;
    }

    public MediaHttpUploader setInitiationRequestMethod(String str) {
        boolean z = str.equals("POST") || str.equals("PUT");
        Preconditions.checkArgument(z);
        this.initiationRequestMethod = str;
        return this;
    }

    public MediaHttpUploader setMetadata(HttpContent httpContent) {
        this.metadata = httpContent;
        return this;
    }

    public MediaHttpUploader setProgressListener(MediaHttpUploaderProgressListener mediaHttpUploaderProgressListener) {
        this.progressListener = mediaHttpUploaderProgressListener;
        return this;
    }

    public HttpResponse upload(GenericUrl genericUrl) throws IOException {
        HttpResponse executeCurrentRequestWithBackOffAndGZip;
        Preconditions.checkArgument(this.uploadState == UploadState.NOT_STARTED);
        if (this.directUploadEnabled) {
            updateStateAndNotifyListener(UploadState.MEDIA_IN_PROGRESS);
            HttpContent httpContent = this.mediaContent;
            if (this.metadata != null) {
                httpContent = new MultipartContent().setContentParts(Arrays.asList(new HttpContent[]{this.metadata, this.mediaContent}));
                genericUrl.put("uploadType", (Object) "multipart");
            } else {
                genericUrl.put("uploadType", (Object) "media");
            }
            HttpRequest buildRequest = this.requestFactory.buildRequest(this.initiationRequestMethod, genericUrl, httpContent);
            buildRequest.getHeaders().putAll(this.initiationHeaders);
            executeCurrentRequestWithBackOffAndGZip = executeCurrentRequestWithBackOffAndGZip(buildRequest);
            try {
                if (getMediaContentLength() >= 0) {
                    this.bytesUploaded = getMediaContentLength();
                }
                updateStateAndNotifyListener(UploadState.MEDIA_COMPLETE);
            } catch (Throwable th) {
                executeCurrentRequestWithBackOffAndGZip.disconnect();
            }
        } else {
            HttpResponse executeUploadInitiation = executeUploadInitiation(genericUrl);
            if (!executeUploadInitiation.isSuccessStatusCode()) {
                return executeUploadInitiation;
            }
            try {
                GenericUrl genericUrl2 = new GenericUrl(executeUploadInitiation.getHeaders().getLocation());
                this.contentInputStream = this.mediaContent.getInputStream();
                if (!this.contentInputStream.markSupported() && getMediaContentLength() >= 0) {
                    this.contentInputStream = new BufferedInputStream(this.contentInputStream);
                }
                while (true) {
                    this.currentRequest = this.requestFactory.buildPutRequest(genericUrl2, null);
                    setContentAndHeadersOnCurrentRequest(this.bytesUploaded);
                    if (this.backOffPolicyEnabled) {
                        this.currentRequest.setBackOffPolicy(new MediaUploadExponentialBackOffPolicy(this));
                    }
                    if (getMediaContentLength() < 0 && !this.disableGZipContent) {
                        this.currentRequest.setEncoding(new GZipEncoding());
                    }
                    executeUploadInitiation = executeCurrentRequest(this.currentRequest);
                    try {
                        if (!executeUploadInitiation.isSuccessStatusCode()) {
                            if (executeUploadInitiation.getStatusCode() != 308) {
                                break;
                            }
                            String location = executeUploadInitiation.getHeaders().getLocation();
                            if (location != null) {
                                genericUrl2 = new GenericUrl(location);
                            }
                            this.bytesUploaded = getNextByteIndex(executeUploadInitiation.getHeaders().getRange());
                            this.currentRequestContentBuffer = null;
                            updateStateAndNotifyListener(UploadState.MEDIA_IN_PROGRESS);
                            executeUploadInitiation.disconnect();
                        } else {
                            break;
                        }
                    } catch (Throwable th2) {
                        executeUploadInitiation.disconnect();
                    }
                }
                this.bytesUploaded = getMediaContentLength();
                if (this.mediaContent.getCloseInputStream()) {
                    this.contentInputStream.close();
                }
                updateStateAndNotifyListener(UploadState.MEDIA_COMPLETE);
                executeCurrentRequestWithBackOffAndGZip = executeUploadInitiation;
            } finally {
                executeUploadInitiation.disconnect();
            }
        }
        return executeCurrentRequestWithBackOffAndGZip;
    }
}
