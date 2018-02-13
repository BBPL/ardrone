package org.apache.http;

import java.io.Serializable;
import java.util.Locale;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.LangUtils;

@Immutable
public final class HttpHost implements Cloneable, Serializable {
    public static final String DEFAULT_SCHEME_NAME = "http";
    private static final long serialVersionUID = -7529410654042457626L;
    protected final String hostname;
    protected final String lcHostname;
    protected final int port;
    protected final String schemeName;

    public HttpHost(String str) {
        this(str, -1, null);
    }

    public HttpHost(String str, int i) {
        this(str, i, null);
    }

    public HttpHost(String str, int i, String str2) {
        if (str == null) {
            throw new IllegalArgumentException("Host name may not be null");
        }
        this.hostname = str;
        this.lcHostname = str.toLowerCase(Locale.ENGLISH);
        if (str2 != null) {
            this.schemeName = str2.toLowerCase(Locale.ENGLISH);
        } else {
            this.schemeName = "http";
        }
        this.port = i;
    }

    public HttpHost(HttpHost httpHost) {
        this(httpHost.hostname, httpHost.port, httpHost.schemeName);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            if (!(obj instanceof HttpHost)) {
                return false;
            }
            HttpHost httpHost = (HttpHost) obj;
            if (!this.lcHostname.equals(httpHost.lcHostname) || this.port != httpHost.port) {
                return false;
            }
            if (!this.schemeName.equals(httpHost.schemeName)) {
                return false;
            }
        }
        return true;
    }

    public String getHostName() {
        return this.hostname;
    }

    public int getPort() {
        return this.port;
    }

    public String getSchemeName() {
        return this.schemeName;
    }

    public int hashCode() {
        return LangUtils.hashCode(LangUtils.hashCode(LangUtils.hashCode(17, this.lcHostname), this.port), this.schemeName);
    }

    public String toHostString() {
        if (this.port == -1) {
            return this.hostname;
        }
        StringBuilder stringBuilder = new StringBuilder(this.hostname.length() + 6);
        stringBuilder.append(this.hostname);
        stringBuilder.append(":");
        stringBuilder.append(Integer.toString(this.port));
        return stringBuilder.toString();
    }

    public String toString() {
        return toURI();
    }

    public String toURI() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.schemeName);
        stringBuilder.append("://");
        stringBuilder.append(this.hostname);
        if (this.port != -1) {
            stringBuilder.append(':');
            stringBuilder.append(Integer.toString(this.port));
        }
        return stringBuilder.toString();
    }
}
