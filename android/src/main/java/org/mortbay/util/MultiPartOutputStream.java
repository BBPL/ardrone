package org.mortbay.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MultiPartOutputStream extends FilterOutputStream {
    public static String MULTIPART_MIXED = "multipart/mixed";
    public static String MULTIPART_X_MIXED_REPLACE = "multipart/x-mixed-replace";
    private static byte[] __CRLF;
    private static byte[] __DASHDASH;
    private String boundary;
    private byte[] boundaryBytes;
    private boolean inPart;

    static {
        try {
            __CRLF = "\r\n".getBytes(StringUtil.__ISO_8859_1);
            __DASHDASH = "--".getBytes(StringUtil.__ISO_8859_1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public MultiPartOutputStream(OutputStream outputStream) throws IOException {
        super(outputStream);
        this.inPart = false;
        this.boundary = new StringBuffer().append("jetty").append(System.identityHashCode(this)).append(Long.toString(System.currentTimeMillis(), 36)).toString();
        this.boundaryBytes = this.boundary.getBytes(StringUtil.__ISO_8859_1);
        this.inPart = false;
    }

    public void close() throws IOException {
        if (this.inPart) {
            this.out.write(__CRLF);
        }
        this.out.write(__DASHDASH);
        this.out.write(this.boundaryBytes);
        this.out.write(__DASHDASH);
        this.out.write(__CRLF);
        this.inPart = false;
        super.close();
    }

    public String getBoundary() {
        return this.boundary;
    }

    public OutputStream getOut() {
        return this.out;
    }

    public void startPart(String str) throws IOException {
        if (this.inPart) {
            this.out.write(__CRLF);
        }
        this.inPart = true;
        this.out.write(__DASHDASH);
        this.out.write(this.boundaryBytes);
        this.out.write(__CRLF);
        this.out.write(new StringBuffer().append("Content-Type: ").append(str).toString().getBytes(StringUtil.__ISO_8859_1));
        this.out.write(__CRLF);
        this.out.write(__CRLF);
    }

    public void startPart(String str, String[] strArr) throws IOException {
        if (this.inPart) {
            this.out.write(__CRLF);
        }
        this.inPart = true;
        this.out.write(__DASHDASH);
        this.out.write(this.boundaryBytes);
        this.out.write(__CRLF);
        this.out.write(new StringBuffer().append("Content-Type: ").append(str).toString().getBytes(StringUtil.__ISO_8859_1));
        this.out.write(__CRLF);
        int i = 0;
        while (strArr != null && i < strArr.length) {
            this.out.write(strArr[i].getBytes(StringUtil.__ISO_8859_1));
            this.out.write(__CRLF);
            i++;
        }
        this.out.write(__CRLF);
    }
}
