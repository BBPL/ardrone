package org.mortbay.io.bio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class StringEndPoint extends StreamEndPoint {
    ByteArrayInputStream _bin;
    ByteArrayOutputStream _bout;
    String _encoding;

    public StringEndPoint() {
        super(null, null);
        this._encoding = "UTF-8";
        this._bin = new ByteArrayInputStream(new byte[0]);
        this._bout = new ByteArrayOutputStream();
        this._in = this._bin;
        this._out = this._bout;
    }

    public StringEndPoint(String str) throws IOException {
        this();
        if (str != null) {
            this._encoding = str;
        }
    }

    public String getOutput() {
        try {
            String str = new String(this._bout.toByteArray(), this._encoding);
            this._bout.reset();
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(new StringBuffer().append(this._encoding).append(": ").append(e.toString()).toString());
        }
    }

    public boolean hasMore() {
        return this._bin.available() > 0;
    }

    public void setInput(String str) {
        try {
            this._bin = new ByteArrayInputStream(str.getBytes(this._encoding));
            this._in = this._bin;
            this._bout = new ByteArrayOutputStream();
            this._out = this._bout;
        } catch (Exception e) {
            throw new IllegalStateException(e.toString());
        }
    }
}
