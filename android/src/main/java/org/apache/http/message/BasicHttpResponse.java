package org.apache.http.message;

import java.util.Locale;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.ReasonPhraseCatalog;
import org.apache.http.StatusLine;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class BasicHttpResponse extends AbstractHttpMessage implements HttpResponse {
    private HttpEntity entity;
    private Locale locale;
    private ReasonPhraseCatalog reasonCatalog;
    private StatusLine statusline;

    public BasicHttpResponse(ProtocolVersion protocolVersion, int i, String str) {
        this(new BasicStatusLine(protocolVersion, i, str), null, null);
    }

    public BasicHttpResponse(StatusLine statusLine) {
        this(statusLine, null, null);
    }

    public BasicHttpResponse(StatusLine statusLine, ReasonPhraseCatalog reasonPhraseCatalog, Locale locale) {
        if (statusLine == null) {
            throw new IllegalArgumentException("Status line may not be null.");
        }
        this.statusline = statusLine;
        this.reasonCatalog = reasonPhraseCatalog;
        if (locale == null) {
            locale = Locale.getDefault();
        }
        this.locale = locale;
    }

    public HttpEntity getEntity() {
        return this.entity;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public ProtocolVersion getProtocolVersion() {
        return this.statusline.getProtocolVersion();
    }

    protected String getReason(int i) {
        return this.reasonCatalog == null ? null : this.reasonCatalog.getReason(i, this.locale);
    }

    public StatusLine getStatusLine() {
        return this.statusline;
    }

    public void setEntity(HttpEntity httpEntity) {
        this.entity = httpEntity;
    }

    public void setLocale(Locale locale) {
        if (locale == null) {
            throw new IllegalArgumentException("Locale may not be null.");
        }
        this.locale = locale;
        int statusCode = this.statusline.getStatusCode();
        this.statusline = new BasicStatusLine(this.statusline.getProtocolVersion(), statusCode, getReason(statusCode));
    }

    public void setReasonPhrase(String str) {
        if (str == null || (str.indexOf(10) < 0 && str.indexOf(13) < 0)) {
            this.statusline = new BasicStatusLine(this.statusline.getProtocolVersion(), this.statusline.getStatusCode(), str);
            return;
        }
        throw new IllegalArgumentException("Line break in reason phrase.");
    }

    public void setStatusCode(int i) {
        this.statusline = new BasicStatusLine(this.statusline.getProtocolVersion(), i, getReason(i));
    }

    public void setStatusLine(ProtocolVersion protocolVersion, int i) {
        this.statusline = new BasicStatusLine(protocolVersion, i, getReason(i));
    }

    public void setStatusLine(ProtocolVersion protocolVersion, int i, String str) {
        this.statusline = new BasicStatusLine(protocolVersion, i, str);
    }

    public void setStatusLine(StatusLine statusLine) {
        if (statusLine == null) {
            throw new IllegalArgumentException("Status line may not be null");
        }
        this.statusline = statusLine;
    }

    public String toString() {
        return this.statusline + " " + this.headergroup;
    }
}
