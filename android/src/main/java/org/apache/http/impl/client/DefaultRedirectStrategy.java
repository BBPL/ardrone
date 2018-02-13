package org.apache.http.impl.client;

import java.net.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.CircularRedirectException;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

@Immutable
public class DefaultRedirectStrategy implements RedirectStrategy {
    public static final String REDIRECT_LOCATIONS = "http.protocol.redirect-locations";
    private static final String[] REDIRECT_METHODS = new String[]{"GET", "HEAD"};
    private final Log log = LogFactory.getLog(getClass());

    private HttpUriRequest copyEntity(HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase, HttpRequest httpRequest) {
        if (httpRequest instanceof HttpEntityEnclosingRequest) {
            httpEntityEnclosingRequestBase.setEntity(((HttpEntityEnclosingRequest) httpRequest).getEntity());
        }
        return httpEntityEnclosingRequestBase;
    }

    protected URI createLocationURI(String str) throws ProtocolException {
        try {
            return new URI(str).normalize();
        } catch (Throwable e) {
            throw new ProtocolException("Invalid redirect URI: " + str, e);
        }
    }

    public URI getLocationURI(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws ProtocolException {
        if (httpRequest == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        } else if (httpResponse == null) {
            throw new IllegalArgumentException("HTTP response may not be null");
        } else if (httpContext == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        } else {
            Header firstHeader = httpResponse.getFirstHeader("location");
            if (firstHeader == null) {
                throw new ProtocolException("Received redirect response " + httpResponse.getStatusLine() + " but no location header");
            }
            String value = firstHeader.getValue();
            if (this.log.isDebugEnabled()) {
                this.log.debug("Redirect requested to location '" + value + "'");
            }
            URI createLocationURI = createLocationURI(value);
            HttpParams params = httpRequest.getParams();
            try {
                URI rewriteURI = URIUtils.rewriteURI(createLocationURI);
                if (!rewriteURI.isAbsolute()) {
                    if (params.isParameterTrue(ClientPNames.REJECT_RELATIVE_REDIRECT)) {
                        throw new ProtocolException("Relative redirect location '" + rewriteURI + "' not allowed");
                    }
                    HttpHost httpHost = (HttpHost) httpContext.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
                    if (httpHost == null) {
                        throw new IllegalStateException("Target host not available in the HTTP context");
                    }
                    rewriteURI = URIUtils.resolve(URIUtils.rewriteURI(new URI(httpRequest.getRequestLine().getUri()), httpHost, true), rewriteURI);
                }
                RedirectLocations redirectLocations = (RedirectLocations) httpContext.getAttribute(REDIRECT_LOCATIONS);
                if (redirectLocations == null) {
                    redirectLocations = new RedirectLocations();
                    httpContext.setAttribute(REDIRECT_LOCATIONS, redirectLocations);
                }
                if (params.isParameterFalse(ClientPNames.ALLOW_CIRCULAR_REDIRECTS) && redirectLocations.contains(rewriteURI)) {
                    throw new CircularRedirectException("Circular redirect to '" + rewriteURI + "'");
                }
                redirectLocations.add(rewriteURI);
                return rewriteURI;
            } catch (Throwable e) {
                throw new ProtocolException(e.getMessage(), e);
            }
        }
    }

    public HttpUriRequest getRedirect(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws ProtocolException {
        URI locationURI = getLocationURI(httpRequest, httpResponse, httpContext);
        String method = httpRequest.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("HEAD")) {
            return new HttpHead(locationURI);
        }
        if (method.equalsIgnoreCase("GET")) {
            return new HttpGet(locationURI);
        }
        if (httpResponse.getStatusLine().getStatusCode() == 307) {
            if (method.equalsIgnoreCase("POST")) {
                return copyEntity(new HttpPost(locationURI), httpRequest);
            }
            if (method.equalsIgnoreCase("PUT")) {
                return copyEntity(new HttpPut(locationURI), httpRequest);
            }
            if (method.equalsIgnoreCase("DELETE")) {
                return new HttpDelete(locationURI);
            }
            if (method.equalsIgnoreCase("TRACE")) {
                return new HttpTrace(locationURI);
            }
            if (method.equalsIgnoreCase("OPTIONS")) {
                return new HttpOptions(locationURI);
            }
            if (method.equalsIgnoreCase("PATCH")) {
                return copyEntity(new HttpPatch(locationURI), httpRequest);
            }
        }
        return new HttpGet(locationURI);
    }

    protected boolean isRedirectable(String str) {
        for (String equalsIgnoreCase : REDIRECT_METHODS) {
            if (equalsIgnoreCase.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    public boolean isRedirected(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws ProtocolException {
        if (httpRequest == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        } else if (httpResponse == null) {
            throw new IllegalArgumentException("HTTP response may not be null");
        } else {
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            String method = httpRequest.getRequestLine().getMethod();
            Header firstHeader = httpResponse.getFirstHeader("location");
            switch (statusCode) {
                case 301:
                case 307:
                    return isRedirectable(method);
                case 302:
                    if (!isRedirectable(method) || firstHeader == null) {
                        return false;
                    }
                case 303:
                    break;
                default:
                    return false;
            }
            return true;
        }
    }
}
