package org.apache.http.client.params;

import org.apache.http.annotation.Immutable;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

@Immutable
public class HttpClientParams {
    private HttpClientParams() {
    }

    public static long getConnectionManagerTimeout(HttpParams httpParams) {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        Long l = (Long) httpParams.getParameter("http.conn-manager.timeout");
        return l != null ? l.longValue() : (long) HttpConnectionParams.getConnectionTimeout(httpParams);
    }

    public static String getCookiePolicy(HttpParams httpParams) {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        String str = (String) httpParams.getParameter(ClientPNames.COOKIE_POLICY);
        return str == null ? CookiePolicy.BEST_MATCH : str;
    }

    public static boolean isAuthenticating(HttpParams httpParams) {
        if (httpParams != null) {
            return httpParams.getBooleanParameter(ClientPNames.HANDLE_AUTHENTICATION, true);
        }
        throw new IllegalArgumentException("HTTP parameters may not be null");
    }

    public static boolean isRedirecting(HttpParams httpParams) {
        if (httpParams != null) {
            return httpParams.getBooleanParameter(ClientPNames.HANDLE_REDIRECTS, true);
        }
        throw new IllegalArgumentException("HTTP parameters may not be null");
    }

    public static void setAuthenticating(HttpParams httpParams, boolean z) {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        httpParams.setBooleanParameter(ClientPNames.HANDLE_AUTHENTICATION, z);
    }

    public static void setConnectionManagerTimeout(HttpParams httpParams, long j) {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        httpParams.setLongParameter("http.conn-manager.timeout", j);
    }

    public static void setCookiePolicy(HttpParams httpParams, String str) {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        httpParams.setParameter(ClientPNames.COOKIE_POLICY, str);
    }

    public static void setRedirecting(HttpParams httpParams, boolean z) {
        if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        httpParams.setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, z);
    }
}
