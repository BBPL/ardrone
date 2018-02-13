package org.apache.http.params;

import java.nio.charset.CodingErrorAction;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.protocol.HTTP;

public final class HttpProtocolParams implements CoreProtocolPNames {
    private HttpProtocolParams() {
    }

    public static String getContentCharset(HttpParams httpParams) {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        String str = (String) httpParams.getParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET);
        return str == null ? HTTP.DEF_CONTENT_CHARSET.name() : str;
    }

    public static String getHttpElementCharset(HttpParams httpParams) {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        String str = (String) httpParams.getParameter(CoreProtocolPNames.HTTP_ELEMENT_CHARSET);
        return str == null ? HTTP.DEF_PROTOCOL_CHARSET.name() : str;
    }

    public static CodingErrorAction getMalformedInputAction(HttpParams httpParams) {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        Object parameter = httpParams.getParameter(CoreProtocolPNames.HTTP_MALFORMED_INPUT_ACTION);
        return parameter == null ? CodingErrorAction.REPORT : (CodingErrorAction) parameter;
    }

    public static CodingErrorAction getUnmappableInputAction(HttpParams httpParams) {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        Object parameter = httpParams.getParameter(CoreProtocolPNames.HTTP_UNMAPPABLE_INPUT_ACTION);
        return parameter == null ? CodingErrorAction.REPORT : (CodingErrorAction) parameter;
    }

    public static String getUserAgent(HttpParams httpParams) {
        if (httpParams != null) {
            return (String) httpParams.getParameter(CoreProtocolPNames.USER_AGENT);
        }
        throw new IllegalArgumentException("HTTP parameters may not be null");
    }

    public static ProtocolVersion getVersion(HttpParams httpParams) {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        Object parameter = httpParams.getParameter(CoreProtocolPNames.PROTOCOL_VERSION);
        return parameter == null ? HttpVersion.HTTP_1_1 : (ProtocolVersion) parameter;
    }

    public static void setContentCharset(HttpParams httpParams, String str) {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        httpParams.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, str);
    }

    public static void setHttpElementCharset(HttpParams httpParams, String str) {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        httpParams.setParameter(CoreProtocolPNames.HTTP_ELEMENT_CHARSET, str);
    }

    public static void setMalformedInputAction(HttpParams httpParams, CodingErrorAction codingErrorAction) {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        httpParams.setParameter(CoreProtocolPNames.HTTP_MALFORMED_INPUT_ACTION, codingErrorAction);
    }

    public static void setUnmappableInputAction(HttpParams httpParams, CodingErrorAction codingErrorAction) {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may no be null");
        }
        httpParams.setParameter(CoreProtocolPNames.HTTP_UNMAPPABLE_INPUT_ACTION, codingErrorAction);
    }

    public static void setUseExpectContinue(HttpParams httpParams, boolean z) {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        httpParams.setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, z);
    }

    public static void setUserAgent(HttpParams httpParams, String str) {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        httpParams.setParameter(CoreProtocolPNames.USER_AGENT, str);
    }

    public static void setVersion(HttpParams httpParams, ProtocolVersion protocolVersion) {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        httpParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, protocolVersion);
    }

    public static boolean useExpectContinue(HttpParams httpParams) {
        if (httpParams != null) {
            return httpParams.getBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
        }
        throw new IllegalArgumentException("HTTP parameters may not be null");
    }
}
