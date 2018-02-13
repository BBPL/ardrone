package org.mortbay.jetty;

import java.io.IOException;
import org.mortbay.io.Buffer;

public interface Generator {
    public static final boolean LAST = true;
    public static final boolean MORE = false;

    void addContent(Buffer buffer, boolean z) throws IOException;

    boolean addContent(byte b) throws IOException;

    void complete() throws IOException;

    void completeHeader(HttpFields httpFields, boolean z) throws IOException;

    long flush() throws IOException;

    int getContentBufferSize();

    long getContentWritten();

    void increaseContentBufferSize(int i);

    boolean isBufferFull();

    boolean isCommitted();

    boolean isComplete();

    boolean isContentWritten();

    boolean isIdle();

    boolean isPersistent();

    void reset(boolean z);

    void resetBuffer();

    void sendError(int i, String str, String str2, boolean z) throws IOException;

    void setContentLength(long j);

    void setHead(boolean z);

    void setPersistent(boolean z);

    void setRequest(String str, String str2);

    void setResponse(int i, String str);

    void setSendServerVersion(boolean z);

    void setVersion(int i);
}
