package org.apache.http.impl.client;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.auth.AuthSchemeRegistry;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.BackoffManager;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ConnectionBackoffStrategy;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.RequestDirector;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionManagerFactory;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.CookieSpecRegistry;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.auth.KerberosSchemeFactory;
import org.apache.http.impl.auth.NTLMSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.impl.conn.DefaultHttpRoutePlanner;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.impl.cookie.IgnoreSpecFactory;
import org.apache.http.impl.cookie.NetscapeDraftSpecFactory;
import org.apache.http.impl.cookie.RFC2109SpecFactory;
import org.apache.http.impl.cookie.RFC2965SpecFactory;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.DefaultedHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.util.EntityUtils;

@ThreadSafe
public abstract class AbstractHttpClient implements HttpClient {
    @GuardedBy("this")
    private BackoffManager backoffManager;
    @GuardedBy("this")
    private ClientConnectionManager connManager;
    @GuardedBy("this")
    private ConnectionBackoffStrategy connectionBackoffStrategy;
    @GuardedBy("this")
    private CookieStore cookieStore;
    @GuardedBy("this")
    private CredentialsProvider credsProvider;
    @GuardedBy("this")
    private HttpParams defaultParams;
    @GuardedBy("this")
    private ConnectionKeepAliveStrategy keepAliveStrategy;
    private final Log log = LogFactory.getLog(getClass());
    @GuardedBy("this")
    private BasicHttpProcessor mutableProcessor;
    @GuardedBy("this")
    private ImmutableHttpProcessor protocolProcessor;
    @GuardedBy("this")
    private AuthenticationStrategy proxyAuthStrategy;
    @GuardedBy("this")
    private RedirectStrategy redirectStrategy;
    @GuardedBy("this")
    private HttpRequestExecutor requestExec;
    @GuardedBy("this")
    private HttpRequestRetryHandler retryHandler;
    @GuardedBy("this")
    private ConnectionReuseStrategy reuseStrategy;
    @GuardedBy("this")
    private HttpRoutePlanner routePlanner;
    @GuardedBy("this")
    private AuthSchemeRegistry supportedAuthSchemes;
    @GuardedBy("this")
    private CookieSpecRegistry supportedCookieSpecs;
    @GuardedBy("this")
    private AuthenticationStrategy targetAuthStrategy;
    @GuardedBy("this")
    private UserTokenHandler userTokenHandler;

    protected AbstractHttpClient(ClientConnectionManager clientConnectionManager, HttpParams httpParams) {
        this.defaultParams = httpParams;
        this.connManager = clientConnectionManager;
    }

    private static HttpHost determineTarget(HttpUriRequest httpUriRequest) throws ClientProtocolException {
        HttpHost httpHost = null;
        URI uri = httpUriRequest.getURI();
        if (uri.isAbsolute()) {
            httpHost = URIUtils.extractHost(uri);
            if (httpHost == null) {
                throw new ClientProtocolException("URI does not specify a valid host name: " + uri);
            }
        }
        return httpHost;
    }

    private final HttpProcessor getProtocolProcessor() {
        HttpProcessor httpProcessor;
        int i = 0;
        synchronized (this) {
            if (this.protocolProcessor == null) {
                int i2;
                BasicHttpProcessor httpProcessor2 = getHttpProcessor();
                int requestInterceptorCount = httpProcessor2.getRequestInterceptorCount();
                HttpRequestInterceptor[] httpRequestInterceptorArr = new HttpRequestInterceptor[requestInterceptorCount];
                for (i2 = 0; i2 < requestInterceptorCount; i2++) {
                    httpRequestInterceptorArr[i2] = httpProcessor2.getRequestInterceptor(i2);
                }
                i2 = httpProcessor2.getResponseInterceptorCount();
                HttpResponseInterceptor[] httpResponseInterceptorArr = new HttpResponseInterceptor[i2];
                while (i < i2) {
                    httpResponseInterceptorArr[i] = httpProcessor2.getResponseInterceptor(i);
                    i++;
                }
                this.protocolProcessor = new ImmutableHttpProcessor(httpRequestInterceptorArr, httpResponseInterceptorArr);
            }
            httpProcessor = this.protocolProcessor;
        }
        return httpProcessor;
    }

    public void addRequestInterceptor(HttpRequestInterceptor httpRequestInterceptor) {
        synchronized (this) {
            getHttpProcessor().addInterceptor(httpRequestInterceptor);
            this.protocolProcessor = null;
        }
    }

    public void addRequestInterceptor(HttpRequestInterceptor httpRequestInterceptor, int i) {
        synchronized (this) {
            getHttpProcessor().addInterceptor(httpRequestInterceptor, i);
            this.protocolProcessor = null;
        }
    }

    public void addResponseInterceptor(HttpResponseInterceptor httpResponseInterceptor) {
        synchronized (this) {
            getHttpProcessor().addInterceptor(httpResponseInterceptor);
            this.protocolProcessor = null;
        }
    }

    public void addResponseInterceptor(HttpResponseInterceptor httpResponseInterceptor, int i) {
        synchronized (this) {
            getHttpProcessor().addInterceptor(httpResponseInterceptor, i);
            this.protocolProcessor = null;
        }
    }

    public void clearRequestInterceptors() {
        synchronized (this) {
            getHttpProcessor().clearRequestInterceptors();
            this.protocolProcessor = null;
        }
    }

    public void clearResponseInterceptors() {
        synchronized (this) {
            getHttpProcessor().clearResponseInterceptors();
            this.protocolProcessor = null;
        }
    }

    protected AuthSchemeRegistry createAuthSchemeRegistry() {
        AuthSchemeRegistry authSchemeRegistry = new AuthSchemeRegistry();
        authSchemeRegistry.register(AuthPolicy.BASIC, new BasicSchemeFactory());
        authSchemeRegistry.register(AuthPolicy.DIGEST, new DigestSchemeFactory());
        authSchemeRegistry.register(AuthPolicy.NTLM, new NTLMSchemeFactory());
        authSchemeRegistry.register(AuthPolicy.SPNEGO, new SPNegoSchemeFactory());
        authSchemeRegistry.register(AuthPolicy.KERBEROS, new KerberosSchemeFactory());
        return authSchemeRegistry;
    }

    protected ClientConnectionManager createClientConnectionManager() {
        ClientConnectionManagerFactory clientConnectionManagerFactory;
        SchemeRegistry createDefault = SchemeRegistryFactory.createDefault();
        HttpParams params = getParams();
        String str = (String) params.getParameter(ClientPNames.CONNECTION_MANAGER_FACTORY_CLASS_NAME);
        if (str != null) {
            try {
                clientConnectionManagerFactory = (ClientConnectionManagerFactory) Class.forName(str).newInstance();
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Invalid class name: " + str);
            } catch (IllegalAccessException e2) {
                throw new IllegalAccessError(e2.getMessage());
            } catch (InstantiationException e3) {
                throw new InstantiationError(e3.getMessage());
            }
        }
        clientConnectionManagerFactory = null;
        return clientConnectionManagerFactory != null ? clientConnectionManagerFactory.newInstance(params, createDefault) : new BasicClientConnectionManager(createDefault);
    }

    @Deprecated
    protected RequestDirector createClientRequestDirector(HttpRequestExecutor httpRequestExecutor, ClientConnectionManager clientConnectionManager, ConnectionReuseStrategy connectionReuseStrategy, ConnectionKeepAliveStrategy connectionKeepAliveStrategy, HttpRoutePlanner httpRoutePlanner, HttpProcessor httpProcessor, HttpRequestRetryHandler httpRequestRetryHandler, RedirectHandler redirectHandler, AuthenticationHandler authenticationHandler, AuthenticationHandler authenticationHandler2, UserTokenHandler userTokenHandler, HttpParams httpParams) {
        return new DefaultRequestDirector(httpRequestExecutor, clientConnectionManager, connectionReuseStrategy, connectionKeepAliveStrategy, httpRoutePlanner, httpProcessor, httpRequestRetryHandler, redirectHandler, authenticationHandler, authenticationHandler2, userTokenHandler, httpParams);
    }

    @Deprecated
    protected RequestDirector createClientRequestDirector(HttpRequestExecutor httpRequestExecutor, ClientConnectionManager clientConnectionManager, ConnectionReuseStrategy connectionReuseStrategy, ConnectionKeepAliveStrategy connectionKeepAliveStrategy, HttpRoutePlanner httpRoutePlanner, HttpProcessor httpProcessor, HttpRequestRetryHandler httpRequestRetryHandler, RedirectStrategy redirectStrategy, AuthenticationHandler authenticationHandler, AuthenticationHandler authenticationHandler2, UserTokenHandler userTokenHandler, HttpParams httpParams) {
        return new DefaultRequestDirector(this.log, httpRequestExecutor, clientConnectionManager, connectionReuseStrategy, connectionKeepAliveStrategy, httpRoutePlanner, httpProcessor, httpRequestRetryHandler, redirectStrategy, authenticationHandler, authenticationHandler2, userTokenHandler, httpParams);
    }

    protected RequestDirector createClientRequestDirector(HttpRequestExecutor httpRequestExecutor, ClientConnectionManager clientConnectionManager, ConnectionReuseStrategy connectionReuseStrategy, ConnectionKeepAliveStrategy connectionKeepAliveStrategy, HttpRoutePlanner httpRoutePlanner, HttpProcessor httpProcessor, HttpRequestRetryHandler httpRequestRetryHandler, RedirectStrategy redirectStrategy, AuthenticationStrategy authenticationStrategy, AuthenticationStrategy authenticationStrategy2, UserTokenHandler userTokenHandler, HttpParams httpParams) {
        return new DefaultRequestDirector(this.log, httpRequestExecutor, clientConnectionManager, connectionReuseStrategy, connectionKeepAliveStrategy, httpRoutePlanner, httpProcessor, httpRequestRetryHandler, redirectStrategy, authenticationStrategy, authenticationStrategy2, userTokenHandler, httpParams);
    }

    protected ConnectionKeepAliveStrategy createConnectionKeepAliveStrategy() {
        return new DefaultConnectionKeepAliveStrategy();
    }

    protected ConnectionReuseStrategy createConnectionReuseStrategy() {
        return new DefaultConnectionReuseStrategy();
    }

    protected CookieSpecRegistry createCookieSpecRegistry() {
        CookieSpecRegistry cookieSpecRegistry = new CookieSpecRegistry();
        cookieSpecRegistry.register(CookiePolicy.BEST_MATCH, new BestMatchSpecFactory());
        cookieSpecRegistry.register(CookiePolicy.BROWSER_COMPATIBILITY, new BrowserCompatSpecFactory());
        cookieSpecRegistry.register(CookiePolicy.NETSCAPE, new NetscapeDraftSpecFactory());
        cookieSpecRegistry.register(CookiePolicy.RFC_2109, new RFC2109SpecFactory());
        cookieSpecRegistry.register(CookiePolicy.RFC_2965, new RFC2965SpecFactory());
        cookieSpecRegistry.register(CookiePolicy.IGNORE_COOKIES, new IgnoreSpecFactory());
        return cookieSpecRegistry;
    }

    protected CookieStore createCookieStore() {
        return new BasicCookieStore();
    }

    protected CredentialsProvider createCredentialsProvider() {
        return new BasicCredentialsProvider();
    }

    protected HttpContext createHttpContext() {
        HttpContext basicHttpContext = new BasicHttpContext();
        basicHttpContext.setAttribute(ClientContext.SCHEME_REGISTRY, getConnectionManager().getSchemeRegistry());
        basicHttpContext.setAttribute(ClientContext.AUTHSCHEME_REGISTRY, getAuthSchemes());
        basicHttpContext.setAttribute(ClientContext.COOKIESPEC_REGISTRY, getCookieSpecs());
        basicHttpContext.setAttribute(ClientContext.COOKIE_STORE, getCookieStore());
        basicHttpContext.setAttribute(ClientContext.CREDS_PROVIDER, getCredentialsProvider());
        return basicHttpContext;
    }

    protected abstract HttpParams createHttpParams();

    protected abstract BasicHttpProcessor createHttpProcessor();

    protected HttpRequestRetryHandler createHttpRequestRetryHandler() {
        return new DefaultHttpRequestRetryHandler();
    }

    protected HttpRoutePlanner createHttpRoutePlanner() {
        return new DefaultHttpRoutePlanner(getConnectionManager().getSchemeRegistry());
    }

    @Deprecated
    protected AuthenticationHandler createProxyAuthenticationHandler() {
        return new DefaultProxyAuthenticationHandler();
    }

    protected AuthenticationStrategy createProxyAuthenticationStrategy() {
        return new ProxyAuthenticationStrategy();
    }

    @Deprecated
    protected RedirectHandler createRedirectHandler() {
        return new DefaultRedirectHandler();
    }

    protected HttpRequestExecutor createRequestExecutor() {
        return new HttpRequestExecutor();
    }

    @Deprecated
    protected AuthenticationHandler createTargetAuthenticationHandler() {
        return new DefaultTargetAuthenticationHandler();
    }

    protected AuthenticationStrategy createTargetAuthenticationStrategy() {
        return new TargetAuthenticationStrategy();
    }

    protected UserTokenHandler createUserTokenHandler() {
        return new DefaultUserTokenHandler();
    }

    protected HttpParams determineParams(HttpRequest httpRequest) {
        return new ClientParamsStack(null, getParams(), httpRequest.getParams(), null);
    }

    public <T> T execute(HttpHost httpHost, HttpRequest httpRequest, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return execute(httpHost, httpRequest, responseHandler, null);
    }

    public <T> T execute(HttpHost httpHost, HttpRequest httpRequest, ResponseHandler<? extends T> responseHandler, HttpContext httpContext) throws IOException, ClientProtocolException {
        if (responseHandler == null) {
            throw new IllegalArgumentException("Response handler must not be null.");
        }
        HttpResponse execute = execute(httpHost, httpRequest, httpContext);
        try {
            T handleResponse = responseHandler.handleResponse(execute);
            EntityUtils.consume(execute.getEntity());
            return handleResponse;
        } catch (Throwable e) {
            try {
                EntityUtils.consume(execute.getEntity());
            } catch (Throwable e2) {
                this.log.warn("Error consuming content after an exception.", e2);
            }
            if (e instanceof RuntimeException) {
                throw ((RuntimeException) e);
            } else if (e instanceof IOException) {
                throw ((IOException) e);
            } else {
                throw new UndeclaredThrowableException(e);
            }
        }
    }

    public <T> T execute(HttpUriRequest httpUriRequest, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return execute(httpUriRequest, (ResponseHandler) responseHandler, null);
    }

    public <T> T execute(HttpUriRequest httpUriRequest, ResponseHandler<? extends T> responseHandler, HttpContext httpContext) throws IOException, ClientProtocolException {
        return execute(determineTarget(httpUriRequest), httpUriRequest, responseHandler, httpContext);
    }

    public final HttpResponse execute(HttpHost httpHost, HttpRequest httpRequest) throws IOException, ClientProtocolException {
        return execute(httpHost, httpRequest, (HttpContext) null);
    }

    public final HttpResponse execute(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext) throws IOException, ClientProtocolException {
        if (httpRequest == null) {
            throw new IllegalArgumentException("Request must not be null.");
        }
        HttpResponse execute;
        synchronized (this) {
            HttpContext createHttpContext = createHttpContext();
            if (httpContext == null) {
                HttpContext httpContext2 = createHttpContext;
            } else {
                Object defaultedHttpContext = new DefaultedHttpContext(httpContext, createHttpContext);
            }
            RequestDirector createClientRequestDirector = createClientRequestDirector(getRequestExecutor(), getConnectionManager(), getConnectionReuseStrategy(), getConnectionKeepAliveStrategy(), getRoutePlanner(), getProtocolProcessor(), getHttpRequestRetryHandler(), getRedirectStrategy(), getTargetAuthenticationStrategy(), getProxyAuthenticationStrategy(), getUserTokenHandler(), determineParams(httpRequest));
            HttpRoutePlanner routePlanner = getRoutePlanner();
            ConnectionBackoffStrategy connectionBackoffStrategy = getConnectionBackoffStrategy();
            BackoffManager backoffManager = getBackoffManager();
        }
        if (connectionBackoffStrategy == null || backoffManager == null) {
            execute = createClientRequestDirector.execute(httpHost, httpRequest, httpContext2);
        } else {
            HttpRoute determineRoute;
            try {
                determineRoute = routePlanner.determineRoute(httpHost != null ? httpHost : (HttpHost) determineParams(httpRequest).getParameter(ClientPNames.DEFAULT_HOST), httpRequest, httpContext2);
                execute = createClientRequestDirector.execute(httpHost, httpRequest, httpContext2);
                if (connectionBackoffStrategy.shouldBackoff(execute)) {
                    backoffManager.backOff(determineRoute);
                } else {
                    backoffManager.probe(determineRoute);
                }
            } catch (Throwable e) {
                if (connectionBackoffStrategy.shouldBackoff(e)) {
                    backoffManager.backOff(determineRoute);
                }
                throw e;
            } catch (Throwable e2) {
                if (connectionBackoffStrategy.shouldBackoff(e2)) {
                    backoffManager.backOff(determineRoute);
                }
                if (e2 instanceof HttpException) {
                    throw ((HttpException) e2);
                } else if (e2 instanceof IOException) {
                    throw ((IOException) e2);
                } else {
                    throw new UndeclaredThrowableException(e2);
                }
            } catch (Throwable e22) {
                throw new ClientProtocolException(e22);
            }
        }
        return execute;
    }

    public final HttpResponse execute(HttpUriRequest httpUriRequest) throws IOException, ClientProtocolException {
        return execute(httpUriRequest, (HttpContext) null);
    }

    public final HttpResponse execute(HttpUriRequest httpUriRequest, HttpContext httpContext) throws IOException, ClientProtocolException {
        if (httpUriRequest != null) {
            return execute(determineTarget(httpUriRequest), (HttpRequest) httpUriRequest, httpContext);
        }
        throw new IllegalArgumentException("Request must not be null.");
    }

    public final AuthSchemeRegistry getAuthSchemes() {
        AuthSchemeRegistry authSchemeRegistry;
        synchronized (this) {
            if (this.supportedAuthSchemes == null) {
                this.supportedAuthSchemes = createAuthSchemeRegistry();
            }
            authSchemeRegistry = this.supportedAuthSchemes;
        }
        return authSchemeRegistry;
    }

    public final BackoffManager getBackoffManager() {
        BackoffManager backoffManager;
        synchronized (this) {
            backoffManager = this.backoffManager;
        }
        return backoffManager;
    }

    public final ConnectionBackoffStrategy getConnectionBackoffStrategy() {
        ConnectionBackoffStrategy connectionBackoffStrategy;
        synchronized (this) {
            connectionBackoffStrategy = this.connectionBackoffStrategy;
        }
        return connectionBackoffStrategy;
    }

    public final ConnectionKeepAliveStrategy getConnectionKeepAliveStrategy() {
        ConnectionKeepAliveStrategy connectionKeepAliveStrategy;
        synchronized (this) {
            if (this.keepAliveStrategy == null) {
                this.keepAliveStrategy = createConnectionKeepAliveStrategy();
            }
            connectionKeepAliveStrategy = this.keepAliveStrategy;
        }
        return connectionKeepAliveStrategy;
    }

    public final ClientConnectionManager getConnectionManager() {
        ClientConnectionManager clientConnectionManager;
        synchronized (this) {
            if (this.connManager == null) {
                this.connManager = createClientConnectionManager();
            }
            clientConnectionManager = this.connManager;
        }
        return clientConnectionManager;
    }

    public final ConnectionReuseStrategy getConnectionReuseStrategy() {
        ConnectionReuseStrategy connectionReuseStrategy;
        synchronized (this) {
            if (this.reuseStrategy == null) {
                this.reuseStrategy = createConnectionReuseStrategy();
            }
            connectionReuseStrategy = this.reuseStrategy;
        }
        return connectionReuseStrategy;
    }

    public final CookieSpecRegistry getCookieSpecs() {
        CookieSpecRegistry cookieSpecRegistry;
        synchronized (this) {
            if (this.supportedCookieSpecs == null) {
                this.supportedCookieSpecs = createCookieSpecRegistry();
            }
            cookieSpecRegistry = this.supportedCookieSpecs;
        }
        return cookieSpecRegistry;
    }

    public final CookieStore getCookieStore() {
        CookieStore cookieStore;
        synchronized (this) {
            if (this.cookieStore == null) {
                this.cookieStore = createCookieStore();
            }
            cookieStore = this.cookieStore;
        }
        return cookieStore;
    }

    public final CredentialsProvider getCredentialsProvider() {
        CredentialsProvider credentialsProvider;
        synchronized (this) {
            if (this.credsProvider == null) {
                this.credsProvider = createCredentialsProvider();
            }
            credentialsProvider = this.credsProvider;
        }
        return credentialsProvider;
    }

    protected final BasicHttpProcessor getHttpProcessor() {
        BasicHttpProcessor basicHttpProcessor;
        synchronized (this) {
            if (this.mutableProcessor == null) {
                this.mutableProcessor = createHttpProcessor();
            }
            basicHttpProcessor = this.mutableProcessor;
        }
        return basicHttpProcessor;
    }

    public final HttpRequestRetryHandler getHttpRequestRetryHandler() {
        HttpRequestRetryHandler httpRequestRetryHandler;
        synchronized (this) {
            if (this.retryHandler == null) {
                this.retryHandler = createHttpRequestRetryHandler();
            }
            httpRequestRetryHandler = this.retryHandler;
        }
        return httpRequestRetryHandler;
    }

    public final HttpParams getParams() {
        HttpParams httpParams;
        synchronized (this) {
            if (this.defaultParams == null) {
                this.defaultParams = createHttpParams();
            }
            httpParams = this.defaultParams;
        }
        return httpParams;
    }

    @Deprecated
    public final AuthenticationHandler getProxyAuthenticationHandler() {
        AuthenticationHandler createProxyAuthenticationHandler;
        synchronized (this) {
            createProxyAuthenticationHandler = createProxyAuthenticationHandler();
        }
        return createProxyAuthenticationHandler;
    }

    public final AuthenticationStrategy getProxyAuthenticationStrategy() {
        AuthenticationStrategy authenticationStrategy;
        synchronized (this) {
            if (this.proxyAuthStrategy == null) {
                this.proxyAuthStrategy = createProxyAuthenticationStrategy();
            }
            authenticationStrategy = this.proxyAuthStrategy;
        }
        return authenticationStrategy;
    }

    @Deprecated
    public final RedirectHandler getRedirectHandler() {
        RedirectHandler createRedirectHandler;
        synchronized (this) {
            createRedirectHandler = createRedirectHandler();
        }
        return createRedirectHandler;
    }

    public final RedirectStrategy getRedirectStrategy() {
        RedirectStrategy redirectStrategy;
        synchronized (this) {
            if (this.redirectStrategy == null) {
                this.redirectStrategy = new DefaultRedirectStrategy();
            }
            redirectStrategy = this.redirectStrategy;
        }
        return redirectStrategy;
    }

    public final HttpRequestExecutor getRequestExecutor() {
        HttpRequestExecutor httpRequestExecutor;
        synchronized (this) {
            if (this.requestExec == null) {
                this.requestExec = createRequestExecutor();
            }
            httpRequestExecutor = this.requestExec;
        }
        return httpRequestExecutor;
    }

    public HttpRequestInterceptor getRequestInterceptor(int i) {
        HttpRequestInterceptor requestInterceptor;
        synchronized (this) {
            requestInterceptor = getHttpProcessor().getRequestInterceptor(i);
        }
        return requestInterceptor;
    }

    public int getRequestInterceptorCount() {
        int requestInterceptorCount;
        synchronized (this) {
            requestInterceptorCount = getHttpProcessor().getRequestInterceptorCount();
        }
        return requestInterceptorCount;
    }

    public HttpResponseInterceptor getResponseInterceptor(int i) {
        HttpResponseInterceptor responseInterceptor;
        synchronized (this) {
            responseInterceptor = getHttpProcessor().getResponseInterceptor(i);
        }
        return responseInterceptor;
    }

    public int getResponseInterceptorCount() {
        int responseInterceptorCount;
        synchronized (this) {
            responseInterceptorCount = getHttpProcessor().getResponseInterceptorCount();
        }
        return responseInterceptorCount;
    }

    public final HttpRoutePlanner getRoutePlanner() {
        HttpRoutePlanner httpRoutePlanner;
        synchronized (this) {
            if (this.routePlanner == null) {
                this.routePlanner = createHttpRoutePlanner();
            }
            httpRoutePlanner = this.routePlanner;
        }
        return httpRoutePlanner;
    }

    @Deprecated
    public final AuthenticationHandler getTargetAuthenticationHandler() {
        AuthenticationHandler createTargetAuthenticationHandler;
        synchronized (this) {
            createTargetAuthenticationHandler = createTargetAuthenticationHandler();
        }
        return createTargetAuthenticationHandler;
    }

    public final AuthenticationStrategy getTargetAuthenticationStrategy() {
        AuthenticationStrategy authenticationStrategy;
        synchronized (this) {
            if (this.targetAuthStrategy == null) {
                this.targetAuthStrategy = createTargetAuthenticationStrategy();
            }
            authenticationStrategy = this.targetAuthStrategy;
        }
        return authenticationStrategy;
    }

    public final UserTokenHandler getUserTokenHandler() {
        UserTokenHandler userTokenHandler;
        synchronized (this) {
            if (this.userTokenHandler == null) {
                this.userTokenHandler = createUserTokenHandler();
            }
            userTokenHandler = this.userTokenHandler;
        }
        return userTokenHandler;
    }

    public void removeRequestInterceptorByClass(Class<? extends HttpRequestInterceptor> cls) {
        synchronized (this) {
            getHttpProcessor().removeRequestInterceptorByClass(cls);
            this.protocolProcessor = null;
        }
    }

    public void removeResponseInterceptorByClass(Class<? extends HttpResponseInterceptor> cls) {
        synchronized (this) {
            getHttpProcessor().removeResponseInterceptorByClass(cls);
            this.protocolProcessor = null;
        }
    }

    public void setAuthSchemes(AuthSchemeRegistry authSchemeRegistry) {
        synchronized (this) {
            this.supportedAuthSchemes = authSchemeRegistry;
        }
    }

    public void setBackoffManager(BackoffManager backoffManager) {
        synchronized (this) {
            this.backoffManager = backoffManager;
        }
    }

    public void setConnectionBackoffStrategy(ConnectionBackoffStrategy connectionBackoffStrategy) {
        synchronized (this) {
            this.connectionBackoffStrategy = connectionBackoffStrategy;
        }
    }

    public void setCookieSpecs(CookieSpecRegistry cookieSpecRegistry) {
        synchronized (this) {
            this.supportedCookieSpecs = cookieSpecRegistry;
        }
    }

    public void setCookieStore(CookieStore cookieStore) {
        synchronized (this) {
            this.cookieStore = cookieStore;
        }
    }

    public void setCredentialsProvider(CredentialsProvider credentialsProvider) {
        synchronized (this) {
            this.credsProvider = credentialsProvider;
        }
    }

    public void setHttpRequestRetryHandler(HttpRequestRetryHandler httpRequestRetryHandler) {
        synchronized (this) {
            this.retryHandler = httpRequestRetryHandler;
        }
    }

    public void setKeepAliveStrategy(ConnectionKeepAliveStrategy connectionKeepAliveStrategy) {
        synchronized (this) {
            this.keepAliveStrategy = connectionKeepAliveStrategy;
        }
    }

    public void setParams(HttpParams httpParams) {
        synchronized (this) {
            this.defaultParams = httpParams;
        }
    }

    @Deprecated
    public void setProxyAuthenticationHandler(AuthenticationHandler authenticationHandler) {
        synchronized (this) {
            this.proxyAuthStrategy = new AuthenticationStrategyAdaptor(authenticationHandler);
        }
    }

    public void setProxyAuthenticationStrategy(AuthenticationStrategy authenticationStrategy) {
        synchronized (this) {
            this.proxyAuthStrategy = authenticationStrategy;
        }
    }

    @Deprecated
    public void setRedirectHandler(RedirectHandler redirectHandler) {
        synchronized (this) {
            this.redirectStrategy = new DefaultRedirectStrategyAdaptor(redirectHandler);
        }
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        synchronized (this) {
            this.redirectStrategy = redirectStrategy;
        }
    }

    public void setReuseStrategy(ConnectionReuseStrategy connectionReuseStrategy) {
        synchronized (this) {
            this.reuseStrategy = connectionReuseStrategy;
        }
    }

    public void setRoutePlanner(HttpRoutePlanner httpRoutePlanner) {
        synchronized (this) {
            this.routePlanner = httpRoutePlanner;
        }
    }

    @Deprecated
    public void setTargetAuthenticationHandler(AuthenticationHandler authenticationHandler) {
        synchronized (this) {
            this.targetAuthStrategy = new AuthenticationStrategyAdaptor(authenticationHandler);
        }
    }

    public void setTargetAuthenticationStrategy(AuthenticationStrategy authenticationStrategy) {
        synchronized (this) {
            this.targetAuthStrategy = authenticationStrategy;
        }
    }

    public void setUserTokenHandler(UserTokenHandler userTokenHandler) {
        synchronized (this) {
            this.userTokenHandler = userTokenHandler;
        }
    }
}
