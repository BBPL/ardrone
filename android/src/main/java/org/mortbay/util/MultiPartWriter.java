package org.mortbay.util;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

public class MultiPartWriter extends FilterWriter {
    public static String MULTIPART_MIXED = MultiPartOutputStream.MULTIPART_MIXED;
    public static String MULTIPART_X_MIXED_REPLACE = MultiPartOutputStream.MULTIPART_X_MIXED_REPLACE;
    private static final String __CRLF = "\r\n";
    private static final String __DASHDASH = "--";
    private String boundary;
    private boolean inPart;

    public MultiPartWriter(Writer writer) throws IOException {
        super(writer);
        this.inPart = false;
        this.boundary = new StringBuffer().append("jetty").append(System.identityHashCode(this)).append(Long.toString(System.currentTimeMillis(), 36)).toString();
        this.inPart = false;
    }

    public void close() throws IOException {
        if (this.inPart) {
            this.out.write("\r\n");
        }
        this.out.write(__DASHDASH);
        this.out.write(this.boundary);
        this.out.write(__DASHDASH);
        this.out.write("\r\n");
        this.inPart = false;
        super.close();
    }

    public void endPart() throws IOException {
        if (this.inPart) {
            this.out.write("\r\n");
        }
        this.inPart = false;
    }

    public String getBoundary() {
        return this.boundary;
    }

    public void startPart(String str) throws IOException {
        if (this.inPart) {
            this.out.write("\r\n");
        }
        this.out.write(__DASHDASH);
        this.out.write(this.boundary);
        this.out.write("\r\n");
        this.out.write("Content-Type: ");
        this.out.write(str);
        this.out.write("\r\n");
        this.out.write("\r\n");
        this.inPart = true;
    }

    public void startPart(String str, String[] strArr) throws IOException {
        if (this.inPart) {
            this.out.write("\r\n");
        }
        this.out.write(__DASHDASH);
        this.out.write(this.boundary);
        this.out.write("\r\n");
        this.out.write("Content-Type: ");
        this.out.write(str);
        this.out.write("\r\n");
        int i = 0;
        while (strArr != null && i < strArr.length) {
            this.out.write(strArr[i]);
            this.out.write("\r\n");
            i++;
        }
        this.out.write("\r\n");
        this.inPart = true;
    }
}
