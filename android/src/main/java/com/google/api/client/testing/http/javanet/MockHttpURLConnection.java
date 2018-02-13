package com.google.api.client.testing.http.javanet;

import com.google.api.client.util.Preconditions;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MockHttpURLConnection extends HttpURLConnection {
    private boolean doOutputCalled;
    private OutputStream outputStream = new ByteArrayOutputStream(0);

    public MockHttpURLConnection(URL url) {
        super(url);
    }

    public void connect() throws IOException {
    }

    public void disconnect() {
    }

    public final boolean doOutputCalled() {
        return this.doOutputCalled;
    }

    public OutputStream getOutputStream() throws IOException {
        return this.outputStream != null ? this.outputStream : super.getOutputStream();
    }

    public int getResponseCode() throws IOException {
        return this.responseCode;
    }

    public void setDoOutput(boolean z) {
        this.doOutputCalled = true;
    }

    public MockHttpURLConnection setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
        return this;
    }

    public MockHttpURLConnection setResponseCode(int i) {
        Preconditions.checkArgument(i >= -1);
        this.responseCode = i;
        return this;
    }

    public boolean usingProxy() {
        return false;
    }
}
