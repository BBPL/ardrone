package org.apache.http.impl.client.cache;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.cache.CacheResponseStatus;
import org.apache.http.client.cache.HeaderConstants;
import org.apache.http.client.cache.HttpCacheEntry;
import org.apache.http.client.cache.HttpCacheStorage;
import org.apache.http.client.cache.ResourceFactory;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.VersionInfo;
import org.mortbay.jetty.HttpStatus;
import org.mortbay.jetty.security.Constraint;

@ThreadSafe
public class CachingHttpClient implements HttpClient {
    public static final String CACHE_RESPONSE_STATUS = "http.cache.response.status";
    private static final boolean SUPPORTS_RANGE_AND_CONTENT_RANGE_HEADERS = false;
    private final AsynchronousValidator asynchRevalidator;
    private final HttpClient backend;
    private final AtomicLong cacheHits;
    private final AtomicLong cacheMisses;
    private final AtomicLong cacheUpdates;
    private final CacheableRequestPolicy cacheableRequestPolicy;
    private final ConditionalRequestBuilder conditionalRequestBuilder;
    private final Log log;
    private final long maxObjectSizeBytes;
    private final RequestProtocolCompliance requestCompliance;
    private final HttpCache responseCache;
    private final ResponseCachingPolicy responseCachingPolicy;
    private final ResponseProtocolCompliance responseCompliance;
    private final CachedHttpResponseGenerator responseGenerator;
    private final boolean sharedCache;
    private final CachedResponseSuitabilityChecker suitabilityChecker;
    private final CacheValidityPolicy validityPolicy;
    private final Map<ProtocolVersion, String> viaHeaders;

    public CachingHttpClient() {
        this(new DefaultHttpClient(), new BasicHttpCache(), new CacheConfig());
    }

    public CachingHttpClient(HttpClient httpClient) {
        this(httpClient, new BasicHttpCache(), new CacheConfig());
    }

    public CachingHttpClient(HttpClient httpClient, HttpCacheStorage httpCacheStorage, CacheConfig cacheConfig) {
        this(httpClient, new BasicHttpCache(new HeapResourceFactory(), httpCacheStorage, cacheConfig), cacheConfig);
    }

    public CachingHttpClient(HttpClient httpClient, ResourceFactory resourceFactory, HttpCacheStorage httpCacheStorage, CacheConfig cacheConfig) {
        this(httpClient, new BasicHttpCache(resourceFactory, httpCacheStorage, cacheConfig), cacheConfig);
    }

    public CachingHttpClient(HttpClient httpClient, CacheConfig cacheConfig) {
        this(httpClient, new BasicHttpCache(cacheConfig), cacheConfig);
    }

    CachingHttpClient(HttpClient httpClient, CacheValidityPolicy cacheValidityPolicy, ResponseCachingPolicy responseCachingPolicy, HttpCache httpCache, CachedHttpResponseGenerator cachedHttpResponseGenerator, CacheableRequestPolicy cacheableRequestPolicy, CachedResponseSuitabilityChecker cachedResponseSuitabilityChecker, ConditionalRequestBuilder conditionalRequestBuilder, ResponseProtocolCompliance responseProtocolCompliance, RequestProtocolCompliance requestProtocolCompliance) {
        this.cacheHits = new AtomicLong();
        this.cacheMisses = new AtomicLong();
        this.cacheUpdates = new AtomicLong();
        this.viaHeaders = new HashMap(4);
        this.log = LogFactory.getLog(getClass());
        CacheConfig cacheConfig = new CacheConfig();
        this.maxObjectSizeBytes = cacheConfig.getMaxObjectSize();
        this.sharedCache = cacheConfig.isSharedCache();
        this.backend = httpClient;
        this.validityPolicy = cacheValidityPolicy;
        this.responseCachingPolicy = responseCachingPolicy;
        this.responseCache = httpCache;
        this.responseGenerator = cachedHttpResponseGenerator;
        this.cacheableRequestPolicy = cacheableRequestPolicy;
        this.suitabilityChecker = cachedResponseSuitabilityChecker;
        this.conditionalRequestBuilder = conditionalRequestBuilder;
        this.responseCompliance = responseProtocolCompliance;
        this.requestCompliance = requestProtocolCompliance;
        this.asynchRevalidator = makeAsynchronousValidator(cacheConfig);
    }

    CachingHttpClient(HttpClient httpClient, HttpCache httpCache, CacheConfig cacheConfig) {
        this.cacheHits = new AtomicLong();
        this.cacheMisses = new AtomicLong();
        this.cacheUpdates = new AtomicLong();
        this.viaHeaders = new HashMap(4);
        this.log = LogFactory.getLog(getClass());
        if (httpClient == null) {
            throw new IllegalArgumentException("HttpClient may not be null");
        } else if (httpCache == null) {
            throw new IllegalArgumentException("HttpCache may not be null");
        } else if (cacheConfig == null) {
            throw new IllegalArgumentException("CacheConfig may not be null");
        } else {
            this.maxObjectSizeBytes = cacheConfig.getMaxObjectSize();
            this.sharedCache = cacheConfig.isSharedCache();
            this.backend = httpClient;
            this.responseCache = httpCache;
            this.validityPolicy = new CacheValidityPolicy();
            this.responseCachingPolicy = new ResponseCachingPolicy(this.maxObjectSizeBytes, this.sharedCache);
            this.responseGenerator = new CachedHttpResponseGenerator(this.validityPolicy);
            this.cacheableRequestPolicy = new CacheableRequestPolicy();
            this.suitabilityChecker = new CachedResponseSuitabilityChecker(this.validityPolicy, cacheConfig);
            this.conditionalRequestBuilder = new ConditionalRequestBuilder();
            this.responseCompliance = new ResponseProtocolCompliance();
            this.requestCompliance = new RequestProtocolCompliance();
            this.asynchRevalidator = makeAsynchronousValidator(cacheConfig);
        }
    }

    public CachingHttpClient(CacheConfig cacheConfig) {
        this(new DefaultHttpClient(), new BasicHttpCache(cacheConfig), cacheConfig);
    }

    private boolean alreadyHaveNewerCacheEntry(HttpHost httpHost, HttpRequest httpRequest, HttpResponse httpResponse) {
        boolean z = false;
        HttpCacheEntry httpCacheEntry = null;
        try {
            httpCacheEntry = this.responseCache.getCacheEntry(httpHost, httpRequest);
        } catch (IOException e) {
        }
        if (httpCacheEntry != null) {
            Header firstHeader = httpCacheEntry.getFirstHeader("Date");
            if (firstHeader != null) {
                Header firstHeader2 = httpResponse.getFirstHeader("Date");
                if (firstHeader2 != null) {
                    try {
                        z = DateUtils.parseDate(firstHeader2.getValue()).before(DateUtils.parseDate(firstHeader.getValue()));
                    } catch (DateParseException e2) {
                    }
                }
            }
        }
        return z;
    }

    private boolean explicitFreshnessRequest(HttpRequest httpRequest, HttpCacheEntry httpCacheEntry, Date date) {
        for (Header elements : httpRequest.getHeaders("Cache-Control")) {
            for (HeaderElement headerElement : elements.getElements()) {
                if (HeaderConstants.CACHE_CONTROL_MAX_STALE.equals(headerElement.getName())) {
                    try {
                        if (this.validityPolicy.getCurrentAgeSecs(httpCacheEntry, date) - this.validityPolicy.getFreshnessLifetimeSecs(httpCacheEntry) > ((long) Integer.parseInt(headerElement.getValue()))) {
                            return true;
                        }
                    } catch (NumberFormatException e) {
                        return true;
                    }
                } else if (HeaderConstants.CACHE_CONTROL_MIN_FRESH.equals(headerElement.getName()) || "max-age".equals(headerElement.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void flushEntriesInvalidatedByRequest(HttpHost httpHost, HttpRequest httpRequest) {
        try {
            this.responseCache.flushInvalidatedCacheEntriesFor(httpHost, httpRequest);
        } catch (Throwable e) {
            this.log.warn("Unable to flush invalidated entries from cache", e);
        }
    }

    private HttpResponse generateCachedResponse(HttpRequest httpRequest, HttpContext httpContext, HttpCacheEntry httpCacheEntry, Date date) {
        HttpResponse generateNotModifiedResponse = (httpRequest.containsHeader("If-None-Match") || httpRequest.containsHeader("If-Modified-Since")) ? this.responseGenerator.generateNotModifiedResponse(httpCacheEntry) : this.responseGenerator.generateResponse(httpCacheEntry);
        setResponseStatus(httpContext, CacheResponseStatus.CACHE_HIT);
        if (this.validityPolicy.getStalenessSecs(httpCacheEntry, date) > 0) {
            generateNotModifiedResponse.addHeader("Warning", "110 localhost \"Response is stale\"");
        }
        return generateNotModifiedResponse;
    }

    private HttpResponse generateGatewayTimeout(HttpContext httpContext) {
        setResponseStatus(httpContext, CacheResponseStatus.CACHE_MODULE_RESPONSE);
        return new BasicHttpResponse(HttpVersion.HTTP_1_1, 504, HttpStatus.Gateway_Timeout);
    }

    private String generateViaHeader(HttpMessage httpMessage) {
        ProtocolVersion protocolVersion = httpMessage.getProtocolVersion();
        String str = (String) this.viaHeaders.get(protocolVersion);
        if (str == null) {
            VersionInfo loadVersionInfo = VersionInfo.loadVersionInfo("org.apache.http.client", getClass().getClassLoader());
            str = loadVersionInfo != null ? loadVersionInfo.getRelease() : VersionInfo.UNAVAILABLE;
            str = "http".equalsIgnoreCase(protocolVersion.getProtocol()) ? String.format("%d.%d localhost (Apache-HttpClient/%s (cache))", new Object[]{Integer.valueOf(protocolVersion.getMajor()), Integer.valueOf(protocolVersion.getMinor()), str}) : String.format("%s/%d.%d localhost (Apache-HttpClient/%s (cache))", new Object[]{protocolVersion.getProtocol(), Integer.valueOf(protocolVersion.getMajor()), Integer.valueOf(protocolVersion.getMinor()), str});
            this.viaHeaders.put(protocolVersion, str);
        }
        return str;
    }

    private Map<String, Variant> getExistingCacheVariants(HttpHost httpHost, HttpRequest httpRequest) {
        try {
            return this.responseCache.getVariantCacheEntriesWithEtags(httpHost, httpRequest);
        } catch (Throwable e) {
            this.log.warn("Unable to retrieve variant entries from cache", e);
            return null;
        }
    }

    private HttpResponse getFatallyNoncompliantResponse(HttpRequest httpRequest, HttpContext httpContext) {
        HttpResponse httpResponse = null;
        for (RequestProtocolError requestProtocolError : this.requestCompliance.requestIsFatallyNonCompliant(httpRequest)) {
            setResponseStatus(httpContext, CacheResponseStatus.CACHE_MODULE_RESPONSE);
            httpResponse = this.requestCompliance.getErrorForRequest(requestProtocolError);
        }
        return httpResponse;
    }

    private HttpCacheEntry getUpdatedVariantEntry(HttpHost httpHost, HttpRequest httpRequest, Date date, Date date2, HttpResponse httpResponse, Variant variant, HttpCacheEntry httpCacheEntry) {
        try {
            httpCacheEntry = this.responseCache.updateVariantCacheEntry(httpHost, httpRequest, httpCacheEntry, httpResponse, date, date2, variant.getCacheKey());
        } catch (Throwable e) {
            this.log.warn("Could not update cache entry", e);
        }
        return httpCacheEntry;
    }

    private <T> T handleAndConsume(ResponseHandler<? extends T> responseHandler, HttpResponse httpResponse) throws Error, IOException {
        try {
            T handleResponse = responseHandler.handleResponse(httpResponse);
            EntityUtils.consume(httpResponse.getEntity());
            return handleResponse;
        } catch (Throwable e) {
            try {
                EntityUtils.consume(httpResponse.getEntity());
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

    private HttpResponse handleCacheHit(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext, HttpCacheEntry httpCacheEntry) throws ClientProtocolException, IOException {
        HttpResponse generateCachedResponse;
        recordCacheHit(httpHost, httpRequest);
        Date currentDate = getCurrentDate();
        if (this.suitabilityChecker.canCachedResponseBeUsed(httpHost, httpRequest, httpCacheEntry, currentDate)) {
            this.log.debug("Cache hit");
            generateCachedResponse = generateCachedResponse(httpRequest, httpContext, httpCacheEntry, currentDate);
        } else if (!mayCallBackend(httpRequest)) {
            this.log.debug("Cache entry not suitable but only-if-cached requested");
            generateCachedResponse = generateGatewayTimeout(httpContext);
        } else if (this.validityPolicy.isRevalidatable(httpCacheEntry)) {
            this.log.debug("Revalidating cache entry");
            return revalidateCacheEntry(httpHost, httpRequest, httpContext, httpCacheEntry, currentDate);
        } else {
            this.log.debug("Cache entry not usable; calling backend");
            return callBackend(httpHost, httpRequest, httpContext);
        }
        if (httpContext == null) {
            return generateCachedResponse;
        }
        httpContext.setAttribute(ExecutionContext.HTTP_TARGET_HOST, httpHost);
        httpContext.setAttribute(ExecutionContext.HTTP_REQUEST, httpRequest);
        httpContext.setAttribute(ExecutionContext.HTTP_RESPONSE, generateCachedResponse);
        httpContext.setAttribute(ExecutionContext.HTTP_REQ_SENT, Boolean.valueOf(true));
        return generateCachedResponse;
    }

    private HttpResponse handleCacheMiss(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext) throws IOException {
        recordCacheMiss(httpHost, httpRequest);
        if (!mayCallBackend(httpRequest)) {
            return new BasicHttpResponse(HttpVersion.HTTP_1_1, 504, HttpStatus.Gateway_Timeout);
        }
        Map existingCacheVariants = getExistingCacheVariants(httpHost, httpRequest);
        return (existingCacheVariants == null || existingCacheVariants.size() <= 0) ? callBackend(httpHost, httpRequest, httpContext) : negotiateResponseFromVariants(httpHost, httpRequest, httpContext, existingCacheVariants);
    }

    private HttpResponse handleRevalidationFailure(HttpRequest httpRequest, HttpContext httpContext, HttpCacheEntry httpCacheEntry, Date date) {
        return staleResponseNotAllowed(httpRequest, httpCacheEntry, date) ? generateGatewayTimeout(httpContext) : unvalidatedCacheHit(httpContext, httpCacheEntry);
    }

    private AsynchronousValidator makeAsynchronousValidator(CacheConfig cacheConfig) {
        return cacheConfig.getAsynchronousWorkersMax() > 0 ? new AsynchronousValidator(this, cacheConfig) : null;
    }

    private boolean mayCallBackend(HttpRequest httpRequest) {
        for (Header elements : httpRequest.getHeaders("Cache-Control")) {
            for (HeaderElement name : elements.getElements()) {
                if ("only-if-cached".equals(name.getName())) {
                    this.log.trace("Request marked only-if-cached");
                    return false;
                }
            }
        }
        return true;
    }

    private void recordCacheHit(HttpHost httpHost, HttpRequest httpRequest) {
        this.cacheHits.getAndIncrement();
        if (this.log.isTraceEnabled()) {
            this.log.trace("Cache hit [host: " + httpHost + "; uri: " + httpRequest.getRequestLine().getUri() + "]");
        }
    }

    private void recordCacheMiss(HttpHost httpHost, HttpRequest httpRequest) {
        this.cacheMisses.getAndIncrement();
        if (this.log.isTraceEnabled()) {
            this.log.trace("Cache miss [host: " + httpHost + "; uri: " + httpRequest.getRequestLine().getUri() + "]");
        }
    }

    private void recordCacheUpdate(HttpContext httpContext) {
        this.cacheUpdates.getAndIncrement();
        setResponseStatus(httpContext, CacheResponseStatus.VALIDATED);
    }

    private HttpResponse retryRequestUnconditionally(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext, HttpCacheEntry httpCacheEntry) throws IOException {
        return callBackend(httpHost, this.conditionalRequestBuilder.buildUnconditionalRequest(httpRequest, httpCacheEntry), httpContext);
    }

    private HttpResponse revalidateCacheEntry(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext, HttpCacheEntry httpCacheEntry, Date date) throws ClientProtocolException {
        try {
            if (this.asynchRevalidator == null || staleResponseNotAllowed(httpRequest, httpCacheEntry, date) || !this.validityPolicy.mayReturnStaleWhileRevalidating(httpCacheEntry, date)) {
                return revalidateCacheEntry(httpHost, httpRequest, httpContext, httpCacheEntry);
            }
            this.log.trace("Serving stale with asynchronous revalidation");
            HttpResponse generateCachedResponse = generateCachedResponse(httpRequest, httpContext, httpCacheEntry, date);
            this.asynchRevalidator.revalidateCacheEntry(httpHost, httpRequest, httpContext, httpCacheEntry);
            return generateCachedResponse;
        } catch (IOException e) {
            return handleRevalidationFailure(httpRequest, httpContext, httpCacheEntry, date);
        } catch (Throwable e2) {
            throw new ClientProtocolException(e2);
        }
    }

    private boolean revalidationResponseIsTooOld(HttpResponse httpResponse, HttpCacheEntry httpCacheEntry) {
        Header firstHeader = httpCacheEntry.getFirstHeader("Date");
        Header firstHeader2 = httpResponse.getFirstHeader("Date");
        if (!(firstHeader == null || firstHeader2 == null)) {
            try {
                if (DateUtils.parseDate(firstHeader2.getValue()).before(DateUtils.parseDate(firstHeader.getValue()))) {
                    return true;
                }
            } catch (DateParseException e) {
            }
        }
        return false;
    }

    private HttpCacheEntry satisfyFromCache(HttpHost httpHost, HttpRequest httpRequest) {
        try {
            return this.responseCache.getCacheEntry(httpHost, httpRequest);
        } catch (Throwable e) {
            this.log.warn("Unable to retrieve entries from cache", e);
            return null;
        }
    }

    private void setResponseStatus(HttpContext httpContext, CacheResponseStatus cacheResponseStatus) {
        if (httpContext != null) {
            httpContext.setAttribute(CACHE_RESPONSE_STATUS, cacheResponseStatus);
        }
    }

    private boolean shouldSendNotModifiedResponse(HttpRequest httpRequest, HttpCacheEntry httpCacheEntry) {
        return this.suitabilityChecker.isConditional(httpRequest) && this.suitabilityChecker.allConditionalsMatch(httpRequest, httpCacheEntry, new Date());
    }

    private boolean staleIfErrorAppliesTo(int i) {
        return i == 500 || i == 502 || i == 503 || i == 504;
    }

    private boolean staleResponseNotAllowed(HttpRequest httpRequest, HttpCacheEntry httpCacheEntry, Date date) {
        return this.validityPolicy.mustRevalidate(httpCacheEntry) || ((isSharedCache() && this.validityPolicy.proxyRevalidate(httpCacheEntry)) || explicitFreshnessRequest(httpRequest, httpCacheEntry, date));
    }

    private void tryToUpdateVariantMap(HttpHost httpHost, HttpRequest httpRequest, Variant variant) {
        try {
            this.responseCache.reuseVariantEntryFor(httpHost, httpRequest, variant);
        } catch (Throwable e) {
            this.log.warn("Could not update cache entry to reuse variant", e);
        }
    }

    private HttpResponse unvalidatedCacheHit(HttpContext httpContext, HttpCacheEntry httpCacheEntry) {
        HttpResponse generateResponse = this.responseGenerator.generateResponse(httpCacheEntry);
        setResponseStatus(httpContext, CacheResponseStatus.CACHE_HIT);
        generateResponse.addHeader("Warning", "111 localhost \"Revalidation failed\"");
        return generateResponse;
    }

    HttpResponse callBackend(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext) throws IOException {
        Date currentDate = getCurrentDate();
        this.log.trace("Calling the backend");
        HttpResponse execute = this.backend.execute(httpHost, httpRequest, httpContext);
        execute.addHeader("Via", generateViaHeader(execute));
        return handleBackendResponse(httpHost, httpRequest, currentDate, getCurrentDate(), execute);
    }

    boolean clientRequestsOurOptions(HttpRequest httpRequest) {
        RequestLine requestLine = httpRequest.getRequestLine();
        return "OPTIONS".equals(requestLine.getMethod()) && Constraint.ANY_ROLE.equals(requestLine.getUri()) && "0".equals(httpRequest.getFirstHeader("Max-Forwards").getValue());
    }

    public <T> T execute(HttpHost httpHost, HttpRequest httpRequest, ResponseHandler<? extends T> responseHandler) throws IOException {
        return execute(httpHost, httpRequest, responseHandler, null);
    }

    public <T> T execute(HttpHost httpHost, HttpRequest httpRequest, ResponseHandler<? extends T> responseHandler, HttpContext httpContext) throws IOException {
        return handleAndConsume(responseHandler, execute(httpHost, httpRequest, httpContext));
    }

    public <T> T execute(HttpUriRequest httpUriRequest, ResponseHandler<? extends T> responseHandler) throws IOException {
        return execute(httpUriRequest, (ResponseHandler) responseHandler, null);
    }

    public <T> T execute(HttpUriRequest httpUriRequest, ResponseHandler<? extends T> responseHandler, HttpContext httpContext) throws IOException {
        return handleAndConsume(responseHandler, execute(httpUriRequest, httpContext));
    }

    public HttpResponse execute(HttpHost httpHost, HttpRequest httpRequest) throws IOException {
        return execute(httpHost, httpRequest, null);
    }

    public HttpResponse execute(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext) throws IOException {
        setResponseStatus(httpContext, CacheResponseStatus.CACHE_MISS);
        String generateViaHeader = generateViaHeader(httpRequest);
        if (clientRequestsOurOptions(httpRequest)) {
            setResponseStatus(httpContext, CacheResponseStatus.CACHE_MODULE_RESPONSE);
            return new OptionsHttp11Response();
        }
        HttpResponse fatallyNoncompliantResponse = getFatallyNoncompliantResponse(httpRequest, httpContext);
        if (fatallyNoncompliantResponse != null) {
            return fatallyNoncompliantResponse;
        }
        HttpRequest makeRequestCompliant = this.requestCompliance.makeRequestCompliant(httpRequest);
        makeRequestCompliant.addHeader("Via", generateViaHeader);
        flushEntriesInvalidatedByRequest(httpHost, makeRequestCompliant);
        if (this.cacheableRequestPolicy.isServableFromCache(makeRequestCompliant)) {
            HttpCacheEntry satisfyFromCache = satisfyFromCache(httpHost, makeRequestCompliant);
            if (satisfyFromCache != null) {
                return handleCacheHit(httpHost, makeRequestCompliant, httpContext, satisfyFromCache);
            }
            this.log.debug("Cache miss");
            return handleCacheMiss(httpHost, makeRequestCompliant, httpContext);
        }
        this.log.debug("Request is not servable from cache");
        return callBackend(httpHost, makeRequestCompliant, httpContext);
    }

    public HttpResponse execute(HttpUriRequest httpUriRequest) throws IOException {
        return execute(httpUriRequest, null);
    }

    public HttpResponse execute(HttpUriRequest httpUriRequest, HttpContext httpContext) throws IOException {
        URI uri = httpUriRequest.getURI();
        return execute(new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme()), (HttpRequest) httpUriRequest, httpContext);
    }

    public long getCacheHits() {
        return this.cacheHits.get();
    }

    public long getCacheMisses() {
        return this.cacheMisses.get();
    }

    public long getCacheUpdates() {
        return this.cacheUpdates.get();
    }

    public ClientConnectionManager getConnectionManager() {
        return this.backend.getConnectionManager();
    }

    Date getCurrentDate() {
        return new Date();
    }

    public HttpParams getParams() {
        return this.backend.getParams();
    }

    HttpResponse handleBackendResponse(HttpHost httpHost, HttpRequest httpRequest, Date date, Date date2, HttpResponse httpResponse) throws IOException {
        this.log.trace("Handling Backend response");
        this.responseCompliance.ensureProtocolCompliance(httpRequest, httpResponse);
        boolean isResponseCacheable = this.responseCachingPolicy.isResponseCacheable(httpRequest, httpResponse);
        this.responseCache.flushInvalidatedCacheEntriesFor(httpHost, httpRequest, httpResponse);
        if (isResponseCacheable && !alreadyHaveNewerCacheEntry(httpHost, httpRequest, httpResponse)) {
            try {
                httpResponse = this.responseCache.cacheAndReturnResponse(httpHost, httpRequest, httpResponse, date, date2);
            } catch (Throwable e) {
                this.log.warn("Unable to store entries in cache", e);
            }
            return httpResponse;
        }
        if (!isResponseCacheable) {
            try {
                this.responseCache.flushCacheEntriesFor(httpHost, httpRequest);
            } catch (Throwable e2) {
                this.log.warn("Unable to flush invalid cache entries", e2);
            }
        }
        return httpResponse;
    }

    public boolean isSharedCache() {
        return this.sharedCache;
    }

    HttpResponse negotiateResponseFromVariants(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext, Map<String, Variant> map) throws IOException {
        HttpRequest buildConditionalRequestFromVariants = this.conditionalRequestBuilder.buildConditionalRequestFromVariants(httpRequest, map);
        Date currentDate = getCurrentDate();
        HttpResponse execute = this.backend.execute(httpHost, buildConditionalRequestFromVariants, httpContext);
        Date currentDate2 = getCurrentDate();
        execute.addHeader("Via", generateViaHeader(execute));
        if (execute.getStatusLine().getStatusCode() != 304) {
            return handleBackendResponse(httpHost, httpRequest, currentDate, currentDate2, execute);
        }
        Header firstHeader = execute.getFirstHeader("ETag");
        if (firstHeader == null) {
            this.log.warn("304 response did not contain ETag");
            return callBackend(httpHost, httpRequest, httpContext);
        }
        Variant variant = (Variant) map.get(firstHeader.getValue());
        if (variant == null) {
            this.log.debug("304 response did not contain ETag matching one sent in If-None-Match");
            return callBackend(httpHost, httpRequest, httpContext);
        }
        HttpCacheEntry entry = variant.getEntry();
        if (revalidationResponseIsTooOld(execute, entry)) {
            EntityUtils.consume(execute.getEntity());
            return retryRequestUnconditionally(httpHost, httpRequest, httpContext, entry);
        }
        recordCacheUpdate(httpContext);
        HttpCacheEntry updatedVariantEntry = getUpdatedVariantEntry(httpHost, buildConditionalRequestFromVariants, currentDate, currentDate2, execute, variant, entry);
        HttpResponse generateResponse = this.responseGenerator.generateResponse(updatedVariantEntry);
        tryToUpdateVariantMap(httpHost, httpRequest, variant);
        return shouldSendNotModifiedResponse(httpRequest, updatedVariantEntry) ? this.responseGenerator.generateNotModifiedResponse(updatedVariantEntry) : generateResponse;
    }

    HttpResponse revalidateCacheEntry(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext, HttpCacheEntry httpCacheEntry) throws IOException, ProtocolException {
        HttpRequest buildConditionalRequest = this.conditionalRequestBuilder.buildConditionalRequest(httpRequest, httpCacheEntry);
        Date currentDate = getCurrentDate();
        HttpResponse execute = this.backend.execute(httpHost, buildConditionalRequest, httpContext);
        Date currentDate2 = getCurrentDate();
        if (revalidationResponseIsTooOld(execute, httpCacheEntry)) {
            EntityUtils.consume(execute.getEntity());
            HttpRequest buildUnconditionalRequest = this.conditionalRequestBuilder.buildUnconditionalRequest(httpRequest, httpCacheEntry);
            currentDate = getCurrentDate();
            execute = this.backend.execute(httpHost, buildUnconditionalRequest, httpContext);
            currentDate2 = getCurrentDate();
        }
        execute.addHeader("Via", generateViaHeader(execute));
        int statusCode = execute.getStatusLine().getStatusCode();
        if (statusCode == 304 || statusCode == 200) {
            recordCacheUpdate(httpContext);
        }
        if (statusCode == 304) {
            HttpCacheEntry updateCacheEntry = this.responseCache.updateCacheEntry(httpHost, httpRequest, httpCacheEntry, execute, currentDate, currentDate2);
            if (this.suitabilityChecker.isConditional(httpRequest)) {
                if (this.suitabilityChecker.allConditionalsMatch(httpRequest, updateCacheEntry, new Date())) {
                    return this.responseGenerator.generateNotModifiedResponse(updateCacheEntry);
                }
            }
            return this.responseGenerator.generateResponse(updateCacheEntry);
        }
        if (staleIfErrorAppliesTo(statusCode)) {
            if (!staleResponseNotAllowed(httpRequest, httpCacheEntry, getCurrentDate()) && this.validityPolicy.mayReturnStaleIfError(httpRequest, httpCacheEntry, currentDate2)) {
                HttpResponse generateResponse = this.responseGenerator.generateResponse(httpCacheEntry);
                generateResponse.addHeader("Warning", "110 localhost \"Response is stale\"");
                HttpEntity entity = execute.getEntity();
                if (entity == null) {
                    return generateResponse;
                }
                EntityUtils.consume(entity);
                return generateResponse;
            }
        }
        return handleBackendResponse(httpHost, buildConditionalRequest, currentDate, currentDate2, execute);
    }

    public boolean supportsRangeAndContentRangeHeaders() {
        return false;
    }
}
