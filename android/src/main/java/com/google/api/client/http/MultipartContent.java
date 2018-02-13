package com.google.api.client.http;

import com.google.api.client.util.Preconditions;
import com.google.api.client.util.StreamingContent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import org.apache.http.entity.mime.MIME;

public class MultipartContent extends AbstractHttpContent {
    static final String NEWLINE = "\r\n";
    private static final String TWO_DASHES = "--";
    private ArrayList<Part> parts = new ArrayList();

    public static final class Part {
        HttpContent content;
        HttpEncoding encoding;
        HttpHeaders headers;

        public Part() {
            this(null);
        }

        public Part(HttpContent httpContent) {
            this(null, httpContent);
        }

        public Part(HttpHeaders httpHeaders, HttpContent httpContent) {
            setHeaders(httpHeaders);
            setContent(httpContent);
        }

        public HttpContent getContent() {
            return this.content;
        }

        public HttpEncoding getEncoding() {
            return this.encoding;
        }

        public HttpHeaders getHeaders() {
            return this.headers;
        }

        public Part setContent(HttpContent httpContent) {
            this.content = httpContent;
            return this;
        }

        public Part setEncoding(HttpEncoding httpEncoding) {
            this.encoding = httpEncoding;
            return this;
        }

        public Part setHeaders(HttpHeaders httpHeaders) {
            this.headers = httpHeaders;
            return this;
        }
    }

    public MultipartContent() {
        super(new HttpMediaType("multipart/related").setParameter("boundary", "__END_OF_PART__"));
    }

    public MultipartContent addPart(Part part) {
        this.parts.add(Preconditions.checkNotNull(part));
        return this;
    }

    public final String getBoundary() {
        return getMediaType().getParameter("boundary");
    }

    public final Collection<Part> getParts() {
        return Collections.unmodifiableCollection(this.parts);
    }

    public boolean retrySupported() {
        Iterator it = this.parts.iterator();
        while (it.hasNext()) {
            if (!((Part) it.next()).content.retrySupported()) {
                return false;
            }
        }
        return true;
    }

    public MultipartContent setBoundary(String str) {
        getMediaType().setParameter("boundary", (String) Preconditions.checkNotNull(str));
        return this;
    }

    public MultipartContent setContentParts(Collection<? extends HttpContent> collection) {
        this.parts = new ArrayList(collection.size());
        for (HttpContent part : collection) {
            addPart(new Part(part));
        }
        return this;
    }

    public MultipartContent setMediaType(HttpMediaType httpMediaType) {
        super.setMediaType(httpMediaType);
        return this;
    }

    public MultipartContent setParts(Collection<Part> collection) {
        this.parts = new ArrayList(collection);
        return this;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        Writer outputStreamWriter = new OutputStreamWriter(outputStream, getCharset());
        String boundary = getBoundary();
        Iterator it = this.parts.iterator();
        while (it.hasNext()) {
            StreamingContent streamingContent;
            Part part = (Part) it.next();
            HttpHeaders acceptEncoding = new HttpHeaders().setAcceptEncoding(null);
            if (part.headers != null) {
                acceptEncoding.fromHttpHeaders(part.headers);
            }
            acceptEncoding.setContentEncoding(null).setUserAgent(null).setContentType(null).setContentLength(null).set(MIME.CONTENT_TRANSFER_ENC, null);
            StreamingContent streamingContent2 = part.content;
            if (streamingContent2 != null) {
                long length;
                acceptEncoding.set(MIME.CONTENT_TRANSFER_ENC, Arrays.asList(new String[]{MIME.ENC_BINARY}));
                acceptEncoding.setContentType(streamingContent2.getType());
                HttpEncoding httpEncoding = part.encoding;
                if (httpEncoding == null) {
                    streamingContent = streamingContent2;
                    length = streamingContent2.getLength();
                } else {
                    acceptEncoding.setContentEncoding(httpEncoding.getName());
                    streamingContent = new HttpEncodingStreamingContent(streamingContent2, httpEncoding);
                    length = AbstractHttpContent.computeLength(streamingContent2);
                }
                if (length != -1) {
                    acceptEncoding.setContentLength(Long.valueOf(length));
                }
            } else {
                streamingContent = null;
            }
            outputStreamWriter.write(TWO_DASHES);
            outputStreamWriter.write(boundary);
            outputStreamWriter.write("\r\n");
            HttpHeaders.serializeHeadersForMultipartRequests(acceptEncoding, null, null, outputStreamWriter);
            if (streamingContent != null) {
                outputStreamWriter.write("\r\n");
                outputStreamWriter.flush();
                streamingContent.writeTo(outputStream);
                outputStreamWriter.write("\r\n");
            }
        }
        outputStreamWriter.write(TWO_DASHES);
        outputStreamWriter.write(boundary);
        outputStreamWriter.write(TWO_DASHES);
        outputStreamWriter.write("\r\n");
        outputStreamWriter.flush();
    }
}
