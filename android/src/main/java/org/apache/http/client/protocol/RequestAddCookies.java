package org.apache.http.client.protocol;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.HttpRoutedConnection;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecRegistry;
import org.apache.http.cookie.SetCookie2;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

@Immutable
public class RequestAddCookies implements HttpRequestInterceptor {
    private final Log log = LogFactory.getLog(getClass());

    public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        Object obj = null;
        if (httpRequest == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        } else if (httpContext == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        } else if (!httpRequest.getRequestLine().getMethod().equalsIgnoreCase("CONNECT")) {
            CookieStore cookieStore = (CookieStore) httpContext.getAttribute(ClientContext.COOKIE_STORE);
            if (cookieStore == null) {
                this.log.debug("Cookie store not specified in HTTP context");
                return;
            }
            CookieSpecRegistry cookieSpecRegistry = (CookieSpecRegistry) httpContext.getAttribute(ClientContext.COOKIESPEC_REGISTRY);
            if (cookieSpecRegistry == null) {
                this.log.debug("CookieSpec registry not specified in HTTP context");
                return;
            }
            HttpHost httpHost = (HttpHost) httpContext.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
            if (httpHost == null) {
                this.log.debug("Target host not set in the context");
                return;
            }
            HttpRoutedConnection httpRoutedConnection = (HttpRoutedConnection) httpContext.getAttribute(ExecutionContext.HTTP_CONNECTION);
            if (httpRoutedConnection == null) {
                this.log.debug("HTTP connection not set in the context");
                return;
            }
            URI uri;
            int i;
            Header versionHeader;
            String cookiePolicy = HttpClientParams.getCookiePolicy(httpRequest.getParams());
            if (this.log.isDebugEnabled()) {
                this.log.debug("CookieSpec selected: " + cookiePolicy);
            }
            if (httpRequest instanceof HttpUriRequest) {
                uri = ((HttpUriRequest) httpRequest).getURI();
            } else {
                try {
                    uri = new URI(httpRequest.getRequestLine().getUri());
                } catch (Throwable e) {
                    throw new ProtocolException("Invalid request URI: " + httpRequest.getRequestLine().getUri(), e);
                }
            }
            String hostName = httpHost.getHostName();
            int port = httpHost.getPort();
            if (port >= 0) {
                i = port;
            } else if (httpRoutedConnection.getRoute().getHopCount() == 1) {
                i = httpRoutedConnection.getRemotePort();
            } else {
                String schemeName = httpHost.getSchemeName();
                i = schemeName.equalsIgnoreCase("http") ? 80 : schemeName.equalsIgnoreCase("https") ? 443 : 0;
            }
            CookieOrigin cookieOrigin = new CookieOrigin(hostName, i, uri.getPath(), httpRoutedConnection.isSecure());
            CookieSpec cookieSpec = cookieSpecRegistry.getCookieSpec(cookiePolicy, httpRequest.getParams());
            List<Cookie> arrayList = new ArrayList(cookieStore.getCookies());
            List<Cookie> arrayList2 = new ArrayList();
            Date date = new Date();
            for (Cookie cookie : arrayList) {
                if (cookie.isExpired(date)) {
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("Cookie " + cookie + " expired");
                    }
                } else if (cookieSpec.match(cookie, cookieOrigin)) {
                    if (this.log.isDebugEnabled()) {
                        this.log.debug("Cookie " + cookie + " match " + cookieOrigin);
                    }
                    arrayList2.add(cookie);
                }
            }
            if (!arrayList2.isEmpty()) {
                for (Header versionHeader2 : cookieSpec.formatCookies(arrayList2)) {
                    httpRequest.addHeader(versionHeader2);
                }
            }
            i = cookieSpec.getVersion();
            if (i > 0) {
                for (Cookie cookie2 : arrayList2) {
                    if (i != cookie2.getVersion() || !(cookie2 instanceof SetCookie2)) {
                        obj = 1;
                    }
                }
                if (obj != null) {
                    versionHeader2 = cookieSpec.getVersionHeader();
                    if (versionHeader2 != null) {
                        httpRequest.addHeader(versionHeader2);
                    }
                }
            }
            httpContext.setAttribute(ClientContext.COOKIE_SPEC, cookieSpec);
            httpContext.setAttribute(ClientContext.COOKIE_ORIGIN, cookieOrigin);
        }
    }
}
