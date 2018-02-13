package org.apache.http.impl.client;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.AuthProtocolState;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectException;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.RequestDirector;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.methods.AbortableHttpRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.BasicManagedEntity;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.BasicRouteDirector;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRouteDirector;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.util.EntityUtils;

@NotThreadSafe
public class DefaultRequestDirector implements RequestDirector {
    private final HttpAuthenticator authenticator;
    protected final ClientConnectionManager connManager;
    private int execCount;
    protected final HttpProcessor httpProcessor;
    protected final ConnectionKeepAliveStrategy keepAliveStrategy;
    private final Log log;
    protected ManagedClientConnection managedConn;
    private int maxRedirects;
    protected final HttpParams params;
    @Deprecated
    protected final AuthenticationHandler proxyAuthHandler;
    protected final AuthState proxyAuthState;
    protected final AuthenticationStrategy proxyAuthStrategy;
    private int redirectCount;
    @Deprecated
    protected final RedirectHandler redirectHandler;
    protected final RedirectStrategy redirectStrategy;
    protected final HttpRequestExecutor requestExec;
    protected final HttpRequestRetryHandler retryHandler;
    protected final ConnectionReuseStrategy reuseStrategy;
    protected final HttpRoutePlanner routePlanner;
    @Deprecated
    protected final AuthenticationHandler targetAuthHandler;
    protected final AuthState targetAuthState;
    protected final AuthenticationStrategy targetAuthStrategy;
    protected final UserTokenHandler userTokenHandler;
    private HttpHost virtualHost;

    @Deprecated
    public DefaultRequestDirector(Log log, HttpRequestExecutor httpRequestExecutor, ClientConnectionManager clientConnectionManager, ConnectionReuseStrategy connectionReuseStrategy, ConnectionKeepAliveStrategy connectionKeepAliveStrategy, HttpRoutePlanner httpRoutePlanner, HttpProcessor httpProcessor, HttpRequestRetryHandler httpRequestRetryHandler, RedirectStrategy redirectStrategy, AuthenticationHandler authenticationHandler, AuthenticationHandler authenticationHandler2, UserTokenHandler userTokenHandler, HttpParams httpParams) {
        this(LogFactory.getLog(DefaultRequestDirector.class), httpRequestExecutor, clientConnectionManager, connectionReuseStrategy, connectionKeepAliveStrategy, httpRoutePlanner, httpProcessor, httpRequestRetryHandler, redirectStrategy, new AuthenticationStrategyAdaptor(authenticationHandler), new AuthenticationStrategyAdaptor(authenticationHandler2), userTokenHandler, httpParams);
    }

    public DefaultRequestDirector(Log log, HttpRequestExecutor httpRequestExecutor, ClientConnectionManager clientConnectionManager, ConnectionReuseStrategy connectionReuseStrategy, ConnectionKeepAliveStrategy connectionKeepAliveStrategy, HttpRoutePlanner httpRoutePlanner, HttpProcessor httpProcessor, HttpRequestRetryHandler httpRequestRetryHandler, RedirectStrategy redirectStrategy, AuthenticationStrategy authenticationStrategy, AuthenticationStrategy authenticationStrategy2, UserTokenHandler userTokenHandler, HttpParams httpParams) {
        if (log == null) {
            throw new IllegalArgumentException("Log may not be null.");
        } else if (httpRequestExecutor == null) {
            throw new IllegalArgumentException("Request executor may not be null.");
        } else if (clientConnectionManager == null) {
            throw new IllegalArgumentException("Client connection manager may not be null.");
        } else if (connectionReuseStrategy == null) {
            throw new IllegalArgumentException("Connection reuse strategy may not be null.");
        } else if (connectionKeepAliveStrategy == null) {
            throw new IllegalArgumentException("Connection keep alive strategy may not be null.");
        } else if (httpRoutePlanner == null) {
            throw new IllegalArgumentException("Route planner may not be null.");
        } else if (httpProcessor == null) {
            throw new IllegalArgumentException("HTTP protocol processor may not be null.");
        } else if (httpRequestRetryHandler == null) {
            throw new IllegalArgumentException("HTTP request retry handler may not be null.");
        } else if (redirectStrategy == null) {
            throw new IllegalArgumentException("Redirect strategy may not be null.");
        } else if (authenticationStrategy == null) {
            throw new IllegalArgumentException("Target authentication strategy may not be null.");
        } else if (authenticationStrategy2 == null) {
            throw new IllegalArgumentException("Proxy authentication strategy may not be null.");
        } else if (userTokenHandler == null) {
            throw new IllegalArgumentException("User token handler may not be null.");
        } else if (httpParams == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        } else {
            this.log = log;
            this.authenticator = new HttpAuthenticator(log);
            this.requestExec = httpRequestExecutor;
            this.connManager = clientConnectionManager;
            this.reuseStrategy = connectionReuseStrategy;
            this.keepAliveStrategy = connectionKeepAliveStrategy;
            this.routePlanner = httpRoutePlanner;
            this.httpProcessor = httpProcessor;
            this.retryHandler = httpRequestRetryHandler;
            this.redirectStrategy = redirectStrategy;
            this.targetAuthStrategy = authenticationStrategy;
            this.proxyAuthStrategy = authenticationStrategy2;
            this.userTokenHandler = userTokenHandler;
            this.params = httpParams;
            if (redirectStrategy instanceof DefaultRedirectStrategyAdaptor) {
                this.redirectHandler = ((DefaultRedirectStrategyAdaptor) redirectStrategy).getHandler();
            } else {
                this.redirectHandler = null;
            }
            if (authenticationStrategy instanceof AuthenticationStrategyAdaptor) {
                this.targetAuthHandler = ((AuthenticationStrategyAdaptor) authenticationStrategy).getHandler();
            } else {
                this.targetAuthHandler = null;
            }
            if (authenticationStrategy2 instanceof AuthenticationStrategyAdaptor) {
                this.proxyAuthHandler = ((AuthenticationStrategyAdaptor) authenticationStrategy2).getHandler();
            } else {
                this.proxyAuthHandler = null;
            }
            this.managedConn = null;
            this.execCount = 0;
            this.redirectCount = 0;
            this.targetAuthState = new AuthState();
            this.proxyAuthState = new AuthState();
            this.maxRedirects = this.params.getIntParameter(ClientPNames.MAX_REDIRECTS, 100);
        }
    }

    @Deprecated
    public DefaultRequestDirector(HttpRequestExecutor httpRequestExecutor, ClientConnectionManager clientConnectionManager, ConnectionReuseStrategy connectionReuseStrategy, ConnectionKeepAliveStrategy connectionKeepAliveStrategy, HttpRoutePlanner httpRoutePlanner, HttpProcessor httpProcessor, HttpRequestRetryHandler httpRequestRetryHandler, RedirectHandler redirectHandler, AuthenticationHandler authenticationHandler, AuthenticationHandler authenticationHandler2, UserTokenHandler userTokenHandler, HttpParams httpParams) {
        this(LogFactory.getLog(DefaultRequestDirector.class), httpRequestExecutor, clientConnectionManager, connectionReuseStrategy, connectionKeepAliveStrategy, httpRoutePlanner, httpProcessor, httpRequestRetryHandler, new DefaultRedirectStrategyAdaptor(redirectHandler), new AuthenticationStrategyAdaptor(authenticationHandler), new AuthenticationStrategyAdaptor(authenticationHandler2), userTokenHandler, httpParams);
    }

    private void abortConnection() {
        ManagedClientConnection managedClientConnection = this.managedConn;
        if (managedClientConnection != null) {
            this.managedConn = null;
            try {
                managedClientConnection.abortConnection();
            } catch (Throwable e) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug(e.getMessage(), e);
                }
            }
            try {
                managedClientConnection.releaseConnection();
            } catch (Throwable e2) {
                this.log.debug("Error releasing connection", e2);
            }
        }
    }

    private void tryConnect(RoutedRequest routedRequest, HttpContext httpContext) throws HttpException, IOException {
        HttpRoute route = routedRequest.getRoute();
        RequestWrapper request = routedRequest.getRequest();
        int i = 0;
        while (true) {
            httpContext.setAttribute(ExecutionContext.HTTP_REQUEST, request);
            i++;
            try {
                break;
            } catch (Throwable e) {
                try {
                    this.managedConn.close();
                } catch (IOException e2) {
                }
                if (!this.retryHandler.retryRequest(e, i, httpContext)) {
                    throw e;
                } else if (this.log.isInfoEnabled()) {
                    this.log.info("I/O exception (" + e.getClass().getName() + ") caught when connecting to the target host: " + e.getMessage());
                    if (this.log.isDebugEnabled()) {
                        this.log.debug(e.getMessage(), e);
                    }
                    this.log.info("Retrying connect");
                }
            }
        }
        if (this.managedConn.isOpen()) {
            this.managedConn.setSocketTimeout(HttpConnectionParams.getSoTimeout(this.params));
        } else {
            this.managedConn.open(route, httpContext, this.params);
        }
        establishRoute(route, httpContext);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private org.apache.http.HttpResponse tryExecute(org.apache.http.impl.client.RoutedRequest r8, org.apache.http.protocol.HttpContext r9) throws org.apache.http.HttpException, java.io.IOException {
        /*
        r7 = this;
        r1 = 0;
        r2 = r8.getRequest();
        r3 = r8.getRoute();
        r0 = r1;
    L_0x000a:
        r4 = r7.execCount;
        r4 = r4 + 1;
        r7.execCount = r4;
        r2.incrementExecCount();
        r4 = r2.isRepeatable();
        if (r4 != 0) goto L_0x0032;
    L_0x0019:
        r1 = r7.log;
        r2 = "Cannot retry non-repeatable request";
        r1.debug(r2);
        if (r0 == 0) goto L_0x002a;
    L_0x0022:
        r1 = new org.apache.http.client.NonRepeatableRequestException;
        r2 = "Cannot retry request with a non-repeatable request entity.  The cause lists the reason the original request failed.";
        r1.<init>(r2, r0);
        throw r1;
    L_0x002a:
        r0 = new org.apache.http.client.NonRepeatableRequestException;
        r1 = "Cannot retry request with a non-repeatable request entity.";
        r0.<init>(r1);
        throw r0;
    L_0x0032:
        r0 = r7.managedConn;	 Catch:{ IOException -> 0x0087 }
        r0 = r0.isOpen();	 Catch:{ IOException -> 0x0087 }
        if (r0 != 0) goto L_0x004e;
    L_0x003a:
        r0 = r3.isTunnelled();	 Catch:{ IOException -> 0x0087 }
        if (r0 != 0) goto L_0x007f;
    L_0x0040:
        r0 = r7.log;	 Catch:{ IOException -> 0x0087 }
        r4 = "Reopening the direct connection.";
        r0.debug(r4);	 Catch:{ IOException -> 0x0087 }
        r0 = r7.managedConn;	 Catch:{ IOException -> 0x0087 }
        r4 = r7.params;	 Catch:{ IOException -> 0x0087 }
        r0.open(r3, r9, r4);	 Catch:{ IOException -> 0x0087 }
    L_0x004e:
        r0 = r7.log;	 Catch:{ IOException -> 0x0087 }
        r0 = r0.isDebugEnabled();	 Catch:{ IOException -> 0x0087 }
        if (r0 == 0) goto L_0x0076;
    L_0x0056:
        r0 = r7.log;	 Catch:{ IOException -> 0x0087 }
        r4 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0087 }
        r4.<init>();	 Catch:{ IOException -> 0x0087 }
        r5 = "Attempt ";
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x0087 }
        r5 = r7.execCount;	 Catch:{ IOException -> 0x0087 }
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x0087 }
        r5 = " to execute request";
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x0087 }
        r4 = r4.toString();	 Catch:{ IOException -> 0x0087 }
        r0.debug(r4);	 Catch:{ IOException -> 0x0087 }
    L_0x0076:
        r0 = r7.requestExec;	 Catch:{ IOException -> 0x0087 }
        r4 = r7.managedConn;	 Catch:{ IOException -> 0x0087 }
        r1 = r0.execute(r2, r4, r9);	 Catch:{ IOException -> 0x0087 }
    L_0x007e:
        return r1;
    L_0x007f:
        r0 = r7.log;	 Catch:{ IOException -> 0x0087 }
        r4 = "Proxied connection. Need to start over.";
        r0.debug(r4);	 Catch:{ IOException -> 0x0087 }
        goto L_0x007e;
    L_0x0087:
        r0 = move-exception;
        r4 = r7.log;
        r5 = "Closing the connection.";
        r4.debug(r5);
        r4 = r7.managedConn;	 Catch:{ IOException -> 0x00f1 }
        r4.close();	 Catch:{ IOException -> 0x00f1 }
    L_0x0094:
        r4 = r7.retryHandler;
        r5 = r2.getExecCount();
        r4 = r4.retryRequest(r0, r5, r9);
        if (r4 == 0) goto L_0x00f0;
    L_0x00a0:
        r4 = r7.log;
        r4 = r4.isInfoEnabled();
        if (r4 == 0) goto L_0x00d6;
    L_0x00a8:
        r4 = r7.log;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "I/O exception (";
        r5 = r5.append(r6);
        r6 = r0.getClass();
        r6 = r6.getName();
        r5 = r5.append(r6);
        r6 = ") caught when processing request: ";
        r5 = r5.append(r6);
        r6 = r0.getMessage();
        r5 = r5.append(r6);
        r5 = r5.toString();
        r4.info(r5);
    L_0x00d6:
        r4 = r7.log;
        r4 = r4.isDebugEnabled();
        if (r4 == 0) goto L_0x00e7;
    L_0x00de:
        r4 = r7.log;
        r5 = r0.getMessage();
        r4.debug(r5, r0);
    L_0x00e7:
        r4 = r7.log;
        r5 = "Retrying request";
        r4.info(r5);
        goto L_0x000a;
    L_0x00f0:
        throw r0;
    L_0x00f1:
        r4 = move-exception;
        goto L_0x0094;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.client.DefaultRequestDirector.tryExecute(org.apache.http.impl.client.RoutedRequest, org.apache.http.protocol.HttpContext):org.apache.http.HttpResponse");
    }

    private RequestWrapper wrapRequest(HttpRequest httpRequest) throws ProtocolException {
        return httpRequest instanceof HttpEntityEnclosingRequest ? new EntityEnclosingRequestWrapper((HttpEntityEnclosingRequest) httpRequest) : new RequestWrapper(httpRequest);
    }

    protected HttpRequest createConnectRequest(HttpRoute httpRoute, HttpContext httpContext) {
        HttpHost targetHost = httpRoute.getTargetHost();
        String hostName = targetHost.getHostName();
        int port = targetHost.getPort();
        if (port < 0) {
            port = this.connManager.getSchemeRegistry().getScheme(targetHost.getSchemeName()).getDefaultPort();
        }
        StringBuilder stringBuilder = new StringBuilder(hostName.length() + 6);
        stringBuilder.append(hostName);
        stringBuilder.append(':');
        stringBuilder.append(Integer.toString(port));
        return new BasicHttpRequest("CONNECT", stringBuilder.toString(), HttpProtocolParams.getVersion(this.params));
    }

    protected boolean createTunnelToProxy(HttpRoute httpRoute, int i, HttpContext httpContext) throws HttpException, IOException {
        throw new HttpException("Proxy chains are not supported.");
    }

    protected boolean createTunnelToTarget(HttpRoute httpRoute, HttpContext httpContext) throws HttpException, IOException {
        HttpHost proxyHost = httpRoute.getProxyHost();
        HttpHost targetHost = httpRoute.getTargetHost();
        while (true) {
            if (!this.managedConn.isOpen()) {
                this.managedConn.open(httpRoute, httpContext, this.params);
            }
            HttpRequest createConnectRequest = createConnectRequest(httpRoute, httpContext);
            createConnectRequest.setParams(this.params);
            httpContext.setAttribute(ExecutionContext.HTTP_TARGET_HOST, targetHost);
            httpContext.setAttribute(ExecutionContext.HTTP_PROXY_HOST, proxyHost);
            httpContext.setAttribute(ExecutionContext.HTTP_CONNECTION, this.managedConn);
            httpContext.setAttribute(ExecutionContext.HTTP_REQUEST, createConnectRequest);
            this.requestExec.preProcess(createConnectRequest, this.httpProcessor, httpContext);
            HttpResponse execute = this.requestExec.execute(createConnectRequest, this.managedConn, httpContext);
            execute.setParams(this.params);
            this.requestExec.postProcess(execute, this.httpProcessor, httpContext);
            if (execute.getStatusLine().getStatusCode() < 200) {
                throw new HttpException("Unexpected response to CONNECT request: " + execute.getStatusLine());
            } else if (HttpClientParams.isAuthenticating(this.params)) {
                if (this.authenticator.isAuthenticationRequested(proxyHost, execute, this.proxyAuthStrategy, this.proxyAuthState, httpContext) && this.authenticator.authenticate(proxyHost, execute, this.proxyAuthStrategy, this.proxyAuthState, httpContext)) {
                    if (this.reuseStrategy.keepAlive(execute, httpContext)) {
                        this.log.debug("Connection kept alive");
                        EntityUtils.consume(execute.getEntity());
                    } else {
                        this.managedConn.close();
                    }
                }
            }
        }
        if (execute.getStatusLine().getStatusCode() > 299) {
            HttpEntity entity = execute.getEntity();
            if (entity != null) {
                execute.setEntity(new BufferedHttpEntity(entity));
            }
            this.managedConn.close();
            throw new TunnelRefusedException("CONNECT refused by proxy: " + execute.getStatusLine(), execute);
        }
        this.managedConn.markReusable();
        return false;
    }

    protected HttpRoute determineRoute(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext) throws HttpException {
        HttpHost httpHost2 = httpHost == null ? (HttpHost) httpRequest.getParams().getParameter(ClientPNames.DEFAULT_HOST) : httpHost;
        if (httpHost2 != null) {
            return this.routePlanner.determineRoute(httpHost2, httpRequest, httpContext);
        }
        throw new IllegalStateException("Target host must not be null, or set in parameters.");
    }

    protected void establishRoute(HttpRoute httpRoute, HttpContext httpContext) throws HttpException, IOException {
        HttpRouteDirector basicRouteDirector = new BasicRouteDirector();
        int nextStep;
        do {
            Object route = this.managedConn.getRoute();
            nextStep = basicRouteDirector.nextStep(httpRoute, route);
            switch (nextStep) {
                case -1:
                    throw new HttpException("Unable to establish route: planned = " + httpRoute + "; current = " + route);
                case 0:
                    break;
                case 1:
                case 2:
                    this.managedConn.open(httpRoute, httpContext, this.params);
                    continue;
                case 3:
                    boolean createTunnelToTarget = createTunnelToTarget(httpRoute, httpContext);
                    this.log.debug("Tunnel to target created.");
                    this.managedConn.tunnelTarget(createTunnelToTarget, this.params);
                    continue;
                case 4:
                    int hopCount = route.getHopCount() - 1;
                    boolean createTunnelToProxy = createTunnelToProxy(httpRoute, hopCount, httpContext);
                    this.log.debug("Tunnel to proxy created.");
                    this.managedConn.tunnelProxy(httpRoute.getHopTarget(hopCount), createTunnelToProxy, this.params);
                    continue;
                case 5:
                    this.managedConn.layerProtocol(httpContext, this.params);
                    continue;
                default:
                    throw new IllegalStateException("Unknown step indicator " + nextStep + " from RouteDirector.");
            }
        } while (nextStep > 0);
    }

    public HttpResponse execute(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        httpContext.setAttribute(ClientContext.TARGET_AUTH_STATE, this.targetAuthState);
        httpContext.setAttribute(ClientContext.PROXY_AUTH_STATE, this.proxyAuthState);
        RequestWrapper wrapRequest = wrapRequest(httpRequest);
        wrapRequest.setParams(this.params);
        HttpRoute determineRoute = determineRoute(httpHost, wrapRequest, httpContext);
        this.virtualHost = (HttpHost) wrapRequest.getParams().getParameter(ClientPNames.VIRTUAL_HOST);
        if (this.virtualHost != null && this.virtualHost.getPort() == -1) {
            int port = httpHost.getPort();
            if (port != -1) {
                this.virtualHost = new HttpHost(this.virtualHost.getHostName(), port, this.virtualHost.getSchemeName());
            }
        }
        boolean z = false;
        RoutedRequest routedRequest = new RoutedRequest(wrapRequest, determineRoute);
        HttpResponse httpResponse = null;
        Object obj = null;
        RoutedRequest routedRequest2 = routedRequest;
        while (obj == null) {
            try {
                HttpRequest request = routedRequest2.getRequest();
                HttpRoute route = routedRequest2.getRoute();
                Object attribute = httpContext.getAttribute(ClientContext.USER_TOKEN);
                if (this.managedConn == null) {
                    ClientConnectionRequest requestConnection = this.connManager.requestConnection(route, attribute);
                    if (httpRequest instanceof AbortableHttpRequest) {
                        ((AbortableHttpRequest) httpRequest).setConnectionRequest(requestConnection);
                    }
                    this.managedConn = requestConnection.getConnection(HttpClientParams.getConnectionManagerTimeout(this.params), TimeUnit.MILLISECONDS);
                    if (HttpConnectionParams.isStaleCheckingEnabled(this.params) && this.managedConn.isOpen()) {
                        this.log.debug("Stale connection check");
                        if (this.managedConn.isStale()) {
                            this.log.debug("Stale connection detected");
                            this.managedConn.close();
                        }
                    }
                }
                if (httpRequest instanceof AbortableHttpRequest) {
                    ((AbortableHttpRequest) httpRequest).setReleaseTrigger(this.managedConn);
                }
                try {
                    tryConnect(routedRequest2, httpContext);
                    String userInfo = request.getURI().getUserInfo();
                    if (userInfo != null) {
                        this.targetAuthState.update(new BasicScheme(), new UsernamePasswordCredentials(userInfo));
                    }
                    request.resetHeaders();
                    rewriteRequestURI(request, route);
                    Object obj2 = this.virtualHost;
                    if (obj2 == null) {
                        obj2 = route.getTargetHost();
                    }
                    HttpHost proxyHost = route.getProxyHost();
                    httpContext.setAttribute(ExecutionContext.HTTP_TARGET_HOST, obj2);
                    httpContext.setAttribute(ExecutionContext.HTTP_PROXY_HOST, proxyHost);
                    httpContext.setAttribute(ExecutionContext.HTTP_CONNECTION, this.managedConn);
                    this.requestExec.preProcess(request, this.httpProcessor, httpContext);
                    HttpResponse tryExecute = tryExecute(routedRequest2, httpContext);
                    if (tryExecute != null) {
                        Object obj3;
                        tryExecute.setParams(this.params);
                        this.requestExec.postProcess(tryExecute, this.httpProcessor, httpContext);
                        z = this.reuseStrategy.keepAlive(tryExecute, httpContext);
                        if (z) {
                            long keepAliveDuration = this.keepAliveStrategy.getKeepAliveDuration(tryExecute, httpContext);
                            if (this.log.isDebugEnabled()) {
                                this.log.debug("Connection can be kept alive " + (keepAliveDuration > 0 ? "for " + keepAliveDuration + " " + TimeUnit.MILLISECONDS : "indefinitely"));
                            }
                            this.managedConn.setIdleDuration(keepAliveDuration, TimeUnit.MILLISECONDS);
                        }
                        RoutedRequest handleResponse = handleResponse(routedRequest2, tryExecute, httpContext);
                        if (handleResponse == null) {
                            routedRequest = routedRequest2;
                            obj3 = 1;
                            handleResponse = routedRequest;
                        } else {
                            if (z) {
                                EntityUtils.consume(tryExecute.getEntity());
                                this.managedConn.markReusable();
                            } else {
                                this.managedConn.close();
                                if (this.proxyAuthState.getState().compareTo(AuthProtocolState.CHALLENGED) > 0 && this.proxyAuthState.getAuthScheme() != null && this.proxyAuthState.getAuthScheme().isConnectionBased()) {
                                    this.log.debug("Resetting proxy auth state");
                                    this.proxyAuthState.reset();
                                }
                                if (this.targetAuthState.getState().compareTo(AuthProtocolState.CHALLENGED) > 0 && this.targetAuthState.getAuthScheme() != null && this.targetAuthState.getAuthScheme().isConnectionBased()) {
                                    this.log.debug("Resetting target auth state");
                                    this.targetAuthState.reset();
                                }
                            }
                            if (!handleResponse.getRoute().equals(routedRequest2.getRoute())) {
                                releaseConnection();
                            }
                            obj3 = obj;
                        }
                        if (this.managedConn != null) {
                            if (attribute == null) {
                                obj = this.userTokenHandler.getUserToken(httpContext);
                                httpContext.setAttribute(ClientContext.USER_TOKEN, obj);
                            } else {
                                obj = attribute;
                            }
                            if (obj != null) {
                                this.managedConn.setState(obj);
                                obj = obj3;
                                routedRequest2 = handleResponse;
                                httpResponse = tryExecute;
                            }
                        }
                        obj = obj3;
                        routedRequest2 = handleResponse;
                        httpResponse = tryExecute;
                    } else {
                        httpResponse = tryExecute;
                    }
                } catch (TunnelRefusedException e) {
                    if (this.log.isDebugEnabled()) {
                        this.log.debug(e.getMessage());
                    }
                    httpResponse = e.getResponse();
                }
            } catch (Throwable e2) {
                InterruptedIOException interruptedIOException = new InterruptedIOException();
                interruptedIOException.initCause(e2);
                throw interruptedIOException;
            } catch (Throwable e22) {
                interruptedIOException = new InterruptedIOException("Connection has been shut down");
                interruptedIOException.initCause(e22);
                throw interruptedIOException;
            } catch (HttpException e3) {
                abortConnection();
                throw e3;
            } catch (IOException e4) {
                abortConnection();
                throw e4;
            } catch (RuntimeException e5) {
                abortConnection();
                throw e5;
            }
        }
        if (httpResponse == null || httpResponse.getEntity() == null || !httpResponse.getEntity().isStreaming()) {
            if (z) {
                this.managedConn.markReusable();
            }
            releaseConnection();
        } else {
            httpResponse.setEntity(new BasicManagedEntity(httpResponse.getEntity(), this.managedConn, z));
        }
        return httpResponse;
    }

    protected RoutedRequest handleResponse(RoutedRequest routedRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        HttpRoute route = routedRequest.getRoute();
        Object request = routedRequest.getRequest();
        HttpParams params = request.getParams();
        if (HttpClientParams.isAuthenticating(params)) {
            HttpHost httpHost;
            HttpHost httpHost2 = (HttpHost) httpContext.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
            if (httpHost2 == null) {
                httpHost2 = route.getTargetHost();
            }
            if (httpHost2.getPort() < 0) {
                httpHost = new HttpHost(httpHost2.getHostName(), this.connManager.getSchemeRegistry().getScheme(httpHost2).getDefaultPort(), httpHost2.getSchemeName());
            } else {
                httpHost = httpHost2;
            }
            if (this.authenticator.isAuthenticationRequested(httpHost, httpResponse, this.targetAuthStrategy, this.targetAuthState, httpContext)) {
                if (this.authenticator.authenticate(httpHost, httpResponse, this.targetAuthStrategy, this.targetAuthState, httpContext)) {
                    return routedRequest;
                }
            }
            httpHost = route.getProxyHost();
            if (this.authenticator.isAuthenticationRequested(httpHost, httpResponse, this.proxyAuthStrategy, this.proxyAuthState, httpContext)) {
                if (this.authenticator.authenticate(httpHost, httpResponse, this.proxyAuthStrategy, this.proxyAuthState, httpContext)) {
                    return routedRequest;
                }
            }
        }
        if (!HttpClientParams.isRedirecting(params) || !this.redirectStrategy.isRedirected(request, httpResponse, httpContext)) {
            return null;
        }
        if (this.redirectCount >= this.maxRedirects) {
            throw new RedirectException("Maximum redirects (" + this.maxRedirects + ") exceeded");
        }
        this.redirectCount++;
        this.virtualHost = null;
        HttpRequest redirect = this.redirectStrategy.getRedirect(request, httpResponse, httpContext);
        redirect.setHeaders(request.getOriginal().getAllHeaders());
        URI uri = redirect.getURI();
        if (uri.getHost() == null) {
            throw new ProtocolException("Redirect URI does not specify a valid host name: " + uri);
        }
        HttpHost httpHost3 = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
        if (!route.getTargetHost().equals(httpHost3)) {
            this.log.debug("Resetting target auth state");
            this.targetAuthState.reset();
            AuthScheme authScheme = this.proxyAuthState.getAuthScheme();
            if (authScheme != null && authScheme.isConnectionBased()) {
                this.log.debug("Resetting proxy auth state");
                this.proxyAuthState.reset();
            }
        }
        Object wrapRequest = wrapRequest(redirect);
        wrapRequest.setParams(params);
        HttpRoute determineRoute = determineRoute(httpHost3, wrapRequest, httpContext);
        routedRequest = new RoutedRequest(wrapRequest, determineRoute);
        if (!this.log.isDebugEnabled()) {
            return routedRequest;
        }
        this.log.debug("Redirecting to '" + uri + "' via " + determineRoute);
        return routedRequest;
    }

    protected void releaseConnection() {
        try {
            this.managedConn.releaseConnection();
        } catch (Throwable e) {
            this.log.debug("IOException releasing connection", e);
        }
        this.managedConn = null;
    }

    protected void rewriteRequestURI(RequestWrapper requestWrapper, HttpRoute httpRoute) throws ProtocolException {
        try {
            URI uri = requestWrapper.getURI();
            uri = (httpRoute.getProxyHost() == null || httpRoute.isTunnelled()) ? uri.isAbsolute() ? URIUtils.rewriteURI(uri, null) : URIUtils.rewriteURI(uri) : !uri.isAbsolute() ? URIUtils.rewriteURI(uri, httpRoute.getTargetHost(), true) : URIUtils.rewriteURI(uri);
            requestWrapper.setURI(uri);
        } catch (Throwable e) {
            throw new ProtocolException("Invalid URI: " + requestWrapper.getRequestLine().getUri(), e);
        }
    }
}
