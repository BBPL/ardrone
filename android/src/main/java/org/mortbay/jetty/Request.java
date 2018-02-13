package org.mortbay.jetty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.mortbay.io.Buffer;
import org.mortbay.io.BufferUtil;
import org.mortbay.io.EndPoint;
import org.mortbay.io.Portable;
import org.mortbay.io.nio.DirectNIOBuffer;
import org.mortbay.io.nio.IndirectNIOBuffer;
import org.mortbay.jetty.HttpConnection.Output;
import org.mortbay.jetty.handler.ContextHandler.SContext;
import org.mortbay.jetty.security.Authenticator;
import org.mortbay.jetty.security.SecurityHandler;
import org.mortbay.jetty.security.SecurityHandler.NotChecked;
import org.mortbay.jetty.security.UserRealm;
import org.mortbay.log.Log;
import org.mortbay.util.Attributes;
import org.mortbay.util.AttributesMap;
import org.mortbay.util.LazyList;
import org.mortbay.util.MultiMap;
import org.mortbay.util.StringUtil;
import org.mortbay.util.URIUtil;
import org.mortbay.util.UrlEncoded;
import org.mortbay.util.ajax.Continuation;

public class Request implements HttpServletRequest {
    private static final int _STREAM = 1;
    private static final int __NONE = 0;
    private static final int __READER = 2;
    private static final Collection __defaultLocale = Collections.singleton(Locale.getDefault());
    private Attributes _attributes;
    private String _authType;
    private MultiMap _baseParameters;
    private String _characterEncoding;
    private HttpConnection _connection;
    private SContext _context;
    private String _contextPath;
    private Continuation _continuation;
    private Cookie[] _cookies;
    private boolean _cookiesExtracted = false;
    private boolean _dns = false;
    private EndPoint _endp;
    private boolean _handled = false;
    private int _inputState = 0;
    private String _method;
    private MultiMap _parameters;
    private boolean _paramsExtracted;
    private String _pathInfo;
    private int _port;
    private String _protocol = HttpVersions.HTTP_1_1;
    private String _queryEncoding;
    private String _queryString;
    private BufferedReader _reader;
    private String _readerEncoding;
    private String _remoteAddr;
    private String _remoteHost;
    private Object _requestAttributeListeners;
    private Object _requestListeners;
    private String _requestURI;
    private String _requestedSessionId;
    private boolean _requestedSessionIdFromCookie = false;
    private Map _roleMap;
    private Map _savedNewSessions;
    private String _scheme = "http";
    private String _serverName;
    private String _servletName;
    private String _servletPath;
    private HttpSession _session;
    private SessionManager _sessionManager;
    private long _timeStamp;
    private Buffer _timeStampBuffer;
    private String[] _unparsedCookies;
    private HttpURI _uri;
    private Principal _userPrincipal;
    private UserRealm _userRealm;

    class C13291 extends BufferedReader {
        private final Request this$0;
        private final ServletInputStream val$in;

        C13291(Request request, Reader reader, ServletInputStream servletInputStream) {
            this.this$0 = request;
            this.val$in = servletInputStream;
            super(reader);
        }

        public void close() throws IOException {
            this.val$in.close();
        }
    }

    public Request(HttpConnection httpConnection) {
        this._connection = httpConnection;
        this._endp = httpConnection.getEndPoint();
        this._dns = this._connection.getResolveNames();
    }

    private void extractParameters() {
        if (this._baseParameters == null) {
            this._baseParameters = new MultiMap(16);
        }
        if (!this._paramsExtracted) {
            int maxFormContentSize;
            this._paramsExtracted = true;
            if (this._uri != null && this._uri.hasQuery()) {
                if (this._queryEncoding == null) {
                    this._uri.decodeQueryTo(this._baseParameters);
                } else {
                    try {
                        this._uri.decodeQueryTo(this._baseParameters, this._queryEncoding);
                    } catch (Throwable e) {
                        if (Log.isDebugEnabled()) {
                            Log.warn(e);
                        } else {
                            Log.warn(e.toString());
                        }
                    }
                }
            }
            String characterEncoding = getCharacterEncoding();
            String contentType = getContentType();
            if (contentType != null && contentType.length() > 0 && "application/x-www-form-urlencoded".equalsIgnoreCase(HttpFields.valueParameters(contentType, null)) && this._inputState == 0 && ("POST".equals(getMethod()) || "PUT".equals(getMethod()))) {
                int contentLength = getContentLength();
                if (contentLength != 0) {
                    try {
                        if (this._context != null) {
                            maxFormContentSize = this._context.getContextHandler().getMaxFormContentSize();
                        } else {
                            Integer num = (Integer) this._connection.getConnector().getServer().getAttribute("org.mortbay.jetty.Request.maxFormContentSize");
                            maxFormContentSize = num != null ? num.intValue() : -1;
                        }
                        if (contentLength <= maxFormContentSize || maxFormContentSize <= 0) {
                            ServletInputStream inputStream = getInputStream();
                            MultiMap multiMap = this._baseParameters;
                            if (contentLength >= 0) {
                                maxFormContentSize = -1;
                            }
                            UrlEncoded.decodeTo(inputStream, multiMap, characterEncoding, maxFormContentSize);
                        } else {
                            throw new IllegalStateException(new StringBuffer().append("Form too large").append(contentLength).append(">").append(maxFormContentSize).toString());
                        }
                    } catch (Throwable e2) {
                        if (Log.isDebugEnabled()) {
                            Log.warn(e2);
                        } else {
                            Log.warn(e2.toString());
                        }
                    }
                }
            }
            if (this._parameters == null) {
                this._parameters = this._baseParameters;
            } else if (this._parameters != this._baseParameters) {
                for (Entry entry : this._baseParameters.entrySet()) {
                    String str = (String) entry.getKey();
                    Object value = entry.getValue();
                    for (maxFormContentSize = 0; maxFormContentSize < LazyList.size(value); maxFormContentSize++) {
                        this._parameters.add(str, LazyList.get(value, maxFormContentSize));
                    }
                }
            }
        } else if (this._parameters == null) {
            this._parameters = this._baseParameters;
        }
    }

    public static Request getRequest(HttpServletRequest httpServletRequest) {
        if (httpServletRequest instanceof Request) {
            return (Request) httpServletRequest;
        }
        HttpServletRequest httpServletRequest2 = httpServletRequest;
        while (httpServletRequest2 instanceof ServletRequestWrapper) {
            httpServletRequest2 = (HttpServletRequest) ((ServletRequestWrapper) httpServletRequest2).getRequest();
        }
        return httpServletRequest2 instanceof Request ? (Request) httpServletRequest2 : HttpConnection.getCurrentConnection().getRequest();
    }

    public void addEventListener(EventListener eventListener) {
        if (eventListener instanceof ServletRequestAttributeListener) {
            this._requestAttributeListeners = LazyList.add(this._requestAttributeListeners, eventListener);
        }
    }

    public Object getAttribute(String str) {
        return "org.mortbay.jetty.ajax.Continuation".equals(str) ? getContinuation(true) : this._attributes == null ? null : this._attributes.getAttribute(str);
    }

    public Enumeration getAttributeNames() {
        return this._attributes == null ? Collections.enumeration(Collections.EMPTY_LIST) : AttributesMap.getAttributeNamesCopy(this._attributes);
    }

    public Attributes getAttributes() {
        if (this._attributes == null) {
            this._attributes = new AttributesMap();
        }
        return this._attributes;
    }

    public String getAuthType() {
        return this._authType;
    }

    public String getCharacterEncoding() {
        return this._characterEncoding;
    }

    public HttpConnection getConnection() {
        return this._connection;
    }

    public int getContentLength() {
        return (int) this._connection.getRequestFields().getLongField(HttpHeaders.CONTENT_LENGTH_BUFFER);
    }

    public long getContentRead() {
        return (this._connection == null || this._connection.getParser() == null) ? -1 : ((HttpParser) this._connection.getParser()).getContentRead();
    }

    public String getContentType() {
        return this._connection.getRequestFields().getStringField(HttpHeaders.CONTENT_TYPE_BUFFER);
    }

    public SContext getContext() {
        return this._context;
    }

    public String getContextPath() {
        return this._contextPath;
    }

    public Continuation getContinuation() {
        return this._continuation;
    }

    public Continuation getContinuation(boolean z) {
        if (this._continuation == null && z) {
            this._continuation = getConnection().getConnector().newContinuation();
        }
        return this._continuation;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public javax.servlet.http.Cookie[] getCookies() {
        /*
        r19 = this;
        r0 = r19;
        r1 = r0._cookiesExtracted;
        if (r1 == 0) goto L_0x000b;
    L_0x0006:
        r0 = r19;
        r1 = r0._cookies;
    L_0x000a:
        return r1;
    L_0x000b:
        r0 = r19;
        r1 = r0._connection;
        r1 = r1.getRequestFields();
        r2 = org.mortbay.jetty.HttpHeaders.COOKIE_BUFFER;
        r1 = r1.containsKey(r2);
        if (r1 != 0) goto L_0x002f;
    L_0x001b:
        r1 = 0;
        r0 = r19;
        r0._cookies = r1;
        r1 = 1;
        r0 = r19;
        r0._cookiesExtracted = r1;
        r1 = 0;
        r0 = r19;
        r0._unparsedCookies = r1;
        r0 = r19;
        r1 = r0._cookies;
        goto L_0x000a;
    L_0x002f:
        r0 = r19;
        r1 = r0._unparsedCookies;
        if (r1 == 0) goto L_0x0084;
    L_0x0035:
        r1 = 0;
        r0 = r19;
        r2 = r0._connection;
        r2 = r2.getRequestFields();
        r3 = org.mortbay.jetty.HttpHeaders.COOKIE_BUFFER;
        r3 = r2.getValues(r3);
        r2 = r1;
    L_0x0045:
        r1 = r3.hasMoreElements();
        if (r1 == 0) goto L_0x0069;
    L_0x004b:
        r1 = r3.nextElement();
        r1 = (java.lang.String) r1;
        r0 = r19;
        r4 = r0._unparsedCookies;
        r4 = r4.length;
        if (r2 >= r4) goto L_0x0064;
    L_0x0058:
        r0 = r19;
        r4 = r0._unparsedCookies;
        r4 = r4[r2];
        r1 = r1.equals(r4);
        if (r1 != 0) goto L_0x0080;
    L_0x0064:
        r1 = 0;
        r0 = r19;
        r0._unparsedCookies = r1;
    L_0x0069:
        r0 = r19;
        r1 = r0._unparsedCookies;
        if (r1 == 0) goto L_0x0084;
    L_0x006f:
        r0 = r19;
        r1 = r0._unparsedCookies;
        r1 = r1.length;
        if (r1 != r2) goto L_0x0084;
    L_0x0076:
        r1 = 1;
        r0 = r19;
        r0._cookiesExtracted = r1;
        r0 = r19;
        r1 = r0._cookies;
        goto L_0x000a;
    L_0x0080:
        r1 = r2 + 1;
        r2 = r1;
        goto L_0x0045;
    L_0x0084:
        r1 = 0;
        r0 = r19;
        r0._cookies = r1;
        r2 = 0;
        r1 = 0;
        r3 = 0;
        r0 = r19;
        r4 = r0._connection;
        r4 = r4.getRequestFields();
        r5 = org.mortbay.jetty.HttpHeaders.COOKIE_BUFFER;
        r14 = r4.getValues(r5);
        r4 = r3;
        r3 = r2;
        r2 = r1;
    L_0x009d:
        r1 = r14.hasMoreElements();
        if (r1 == 0) goto L_0x0213;
    L_0x00a3:
        r1 = r14.nextElement();	 Catch:{ Exception -> 0x01be }
        r1 = (java.lang.String) r1;	 Catch:{ Exception -> 0x01be }
        r2 = org.mortbay.util.LazyList.add(r2, r1);	 Catch:{ Exception -> 0x01be }
        r5 = 0;
        r6 = 0;
        r8 = 0;
        r11 = 0;
        r9 = 0;
        r10 = -1;
        r12 = -1;
        r13 = 0;
        r15 = r1.length();	 Catch:{ Exception -> 0x01be }
        r16 = r15 + -1;
        r7 = 0;
    L_0x00bc:
        if (r13 >= r15) goto L_0x009d;
    L_0x00be:
        r17 = r1.charAt(r13);	 Catch:{ Exception -> 0x01be }
        if (r11 == 0) goto L_0x0126;
    L_0x00c4:
        if (r9 == 0) goto L_0x028f;
    L_0x00c6:
        r9 = 0;
        r18 = r5;
        r5 = r6;
        r6 = r7;
        r7 = r8;
        r8 = r9;
        r9 = r4;
        r4 = r18;
    L_0x00d0:
        r13 = r13 + 1;
        r18 = r4;
        r4 = r9;
        r9 = r8;
        r8 = r7;
        r7 = r6;
        r6 = r5;
        r5 = r18;
        goto L_0x00bc;
    L_0x00dc:
        r11 = 0;
        r0 = r16;
        if (r13 != r0) goto L_0x02c6;
    L_0x00e1:
        if (r8 == 0) goto L_0x011c;
    L_0x00e3:
        r6 = r13 + 1;
        r6 = r1.substring(r10, r6);	 Catch:{ Exception -> 0x01be }
        r12 = r13;
    L_0x00ea:
        if (r6 == 0) goto L_0x0284;
    L_0x00ec:
        if (r5 == 0) goto L_0x0284;
    L_0x00ee:
        r5 = org.mortbay.util.QuotedStringTokenizer.unquote(r5);	 Catch:{ Exception -> 0x01be }
        r17 = org.mortbay.util.QuotedStringTokenizer.unquote(r6);	 Catch:{ Exception -> 0x01be }
        r6 = "$";
        r6 = r5.startsWith(r6);	 Catch:{ Exception -> 0x01af }
        if (r6 == 0) goto L_0x0200;
    L_0x00fe:
        r5 = r5.toLowerCase();	 Catch:{ Exception -> 0x01af }
        r6 = "$path";
        r6 = r6.equals(r5);	 Catch:{ Exception -> 0x01af }
        if (r6 == 0) goto L_0x019e;
    L_0x010a:
        if (r7 == 0) goto L_0x0111;
    L_0x010c:
        r0 = r17;
        r7.setPath(r0);	 Catch:{ Exception -> 0x01af }
    L_0x0111:
        r6 = r7;
        r7 = r4;
    L_0x0113:
        r4 = 0;
        r5 = 0;
        r18 = r8;
        r8 = r9;
        r9 = r7;
        r7 = r18;
        goto L_0x00d0;
    L_0x011c:
        r5 = r13 + 1;
        r5 = r1.substring(r10, r5);	 Catch:{ Exception -> 0x01be }
        r6 = "";
        r12 = r13;
        goto L_0x00ea;
    L_0x0126:
        if (r8 == 0) goto L_0x0155;
    L_0x0128:
        switch(r17) {
            case 9: goto L_0x02a9;
            case 32: goto L_0x02a9;
            case 34: goto L_0x013a;
            case 44: goto L_0x014a;
            case 59: goto L_0x014a;
            default: goto L_0x012b;
        };
    L_0x012b:
        if (r10 >= 0) goto L_0x012e;
    L_0x012d:
        r10 = r13;
    L_0x012e:
        r0 = r16;
        if (r13 != r0) goto L_0x0283;
    L_0x0132:
        r6 = r13 + 1;
        r6 = r1.substring(r10, r6);	 Catch:{ Exception -> 0x01be }
        r12 = r13;
        goto L_0x00ea;
    L_0x013a:
        if (r10 >= 0) goto L_0x013e;
    L_0x013c:
        r11 = 1;
        r10 = r13;
    L_0x013e:
        r0 = r16;
        if (r13 != r0) goto L_0x0283;
    L_0x0142:
        r6 = r13 + 1;
        r6 = r1.substring(r10, r6);	 Catch:{ Exception -> 0x01be }
        r12 = r13;
        goto L_0x00ea;
    L_0x014a:
        if (r10 < 0) goto L_0x02b4;
    L_0x014c:
        r6 = r12 + 1;
        r6 = r1.substring(r10, r6);	 Catch:{ Exception -> 0x01be }
    L_0x0152:
        r10 = -1;
        r8 = 0;
        goto L_0x00ea;
    L_0x0155:
        switch(r17) {
            case 9: goto L_0x02b8;
            case 32: goto L_0x02b8;
            case 34: goto L_0x0169;
            case 44: goto L_0x017c;
            case 59: goto L_0x017c;
            case 61: goto L_0x0189;
            default: goto L_0x0158;
        };	 Catch:{ Exception -> 0x01be }
    L_0x0158:
        if (r10 >= 0) goto L_0x015b;
    L_0x015a:
        r10 = r13;
    L_0x015b:
        r0 = r16;
        if (r13 != r0) goto L_0x0283;
    L_0x015f:
        r5 = r13 + 1;
        r5 = r1.substring(r10, r5);	 Catch:{ Exception -> 0x01be }
        r6 = "";
        r12 = r13;
        goto L_0x00ea;
    L_0x0169:
        if (r10 >= 0) goto L_0x016d;
    L_0x016b:
        r11 = 1;
        r10 = r13;
    L_0x016d:
        r0 = r16;
        if (r13 != r0) goto L_0x0283;
    L_0x0171:
        r5 = r13 + 1;
        r5 = r1.substring(r10, r5);	 Catch:{ Exception -> 0x01be }
        r6 = "";
        r12 = r13;
        goto L_0x00ea;
    L_0x017c:
        if (r10 < 0) goto L_0x0186;
    L_0x017e:
        r5 = r12 + 1;
        r5 = r1.substring(r10, r5);	 Catch:{ Exception -> 0x01be }
        r6 = "";
    L_0x0186:
        r10 = -1;
        goto L_0x00ea;
    L_0x0189:
        if (r10 < 0) goto L_0x0191;
    L_0x018b:
        r5 = r12 + 1;
        r5 = r1.substring(r10, r5);	 Catch:{ Exception -> 0x01be }
    L_0x0191:
        r10 = -1;
        r8 = 1;
        r18 = r5;
        r5 = r6;
        r6 = r7;
        r7 = r8;
        r8 = r9;
        r9 = r4;
        r4 = r18;
        goto L_0x00d0;
    L_0x019e:
        r6 = "$domain";
        r6 = r6.equals(r5);	 Catch:{ Exception -> 0x01af }
        if (r6 == 0) goto L_0x01ce;
    L_0x01a6:
        if (r7 == 0) goto L_0x0111;
    L_0x01a8:
        r0 = r17;
        r7.setDomain(r0);	 Catch:{ Exception -> 0x01af }
        goto L_0x0111;
    L_0x01af:
        r5 = move-exception;
        r6 = r7;
    L_0x01b1:
        r7 = r5.toString();	 Catch:{ Exception -> 0x01be }
        org.mortbay.log.Log.warn(r7);	 Catch:{ Exception -> 0x01be }
        org.mortbay.log.Log.debug(r5);	 Catch:{ Exception -> 0x01be }
        r7 = r4;
        goto L_0x0113;
    L_0x01be:
        r1 = move-exception;
        r18 = r1;
        r1 = r2;
        r2 = r3;
        r3 = r4;
        r4 = r18;
        org.mortbay.log.Log.warn(r4);
        r4 = r3;
        r3 = r2;
        r2 = r1;
        goto L_0x009d;
    L_0x01ce:
        r6 = "$port";
        r6 = r6.equals(r5);	 Catch:{ Exception -> 0x01af }
        if (r6 == 0) goto L_0x01f2;
    L_0x01d6:
        if (r7 == 0) goto L_0x0111;
    L_0x01d8:
        r5 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x01af }
        r5.<init>();	 Catch:{ Exception -> 0x01af }
        r6 = "port=";
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x01af }
        r0 = r17;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x01af }
        r5 = r5.toString();	 Catch:{ Exception -> 0x01af }
        r7.setComment(r5);	 Catch:{ Exception -> 0x01af }
        goto L_0x0111;
    L_0x01f2:
        r6 = "$version";
        r5 = r6.equals(r5);	 Catch:{ Exception -> 0x01af }
        if (r5 == 0) goto L_0x0111;
    L_0x01fa:
        r4 = java.lang.Integer.parseInt(r17);	 Catch:{ Exception -> 0x01af }
        goto L_0x0111;
    L_0x0200:
        r6 = new javax.servlet.http.Cookie;	 Catch:{ Exception -> 0x01af }
        r0 = r17;
        r6.<init>(r5, r0);	 Catch:{ Exception -> 0x01af }
        if (r4 <= 0) goto L_0x020c;
    L_0x0209:
        r6.setVersion(r4);	 Catch:{ Exception -> 0x02c3 }
    L_0x020c:
        r3 = org.mortbay.util.LazyList.add(r3, r6);	 Catch:{ Exception -> 0x02c3 }
        r7 = r4;
        goto L_0x0113;
    L_0x0213:
        r5 = org.mortbay.util.LazyList.size(r3);
        r1 = 1;
        r0 = r19;
        r0._cookiesExtracted = r1;
        if (r5 <= 0) goto L_0x0263;
    L_0x021e:
        r0 = r19;
        r1 = r0._cookies;
        if (r1 == 0) goto L_0x022b;
    L_0x0224:
        r0 = r19;
        r1 = r0._cookies;
        r1 = r1.length;
        if (r1 == r5) goto L_0x0231;
    L_0x022b:
        r1 = new javax.servlet.http.Cookie[r5];
        r0 = r19;
        r0._cookies = r1;
    L_0x0231:
        r1 = 0;
        r4 = r1;
    L_0x0233:
        if (r4 >= r5) goto L_0x0245;
    L_0x0235:
        r0 = r19;
        r6 = r0._cookies;
        r1 = org.mortbay.util.LazyList.get(r3, r4);
        r1 = (javax.servlet.http.Cookie) r1;
        r6[r4] = r1;
        r1 = r4 + 1;
        r4 = r1;
        goto L_0x0233;
    L_0x0245:
        r4 = org.mortbay.util.LazyList.size(r2);
        r1 = new java.lang.String[r4];
        r0 = r19;
        r0._unparsedCookies = r1;
        r1 = 0;
        r3 = r1;
    L_0x0251:
        if (r3 >= r4) goto L_0x026d;
    L_0x0253:
        r0 = r19;
        r5 = r0._unparsedCookies;
        r1 = org.mortbay.util.LazyList.get(r2, r3);
        r1 = (java.lang.String) r1;
        r5[r3] = r1;
        r1 = r3 + 1;
        r3 = r1;
        goto L_0x0251;
    L_0x0263:
        r1 = 0;
        r0 = r19;
        r0._cookies = r1;
        r1 = 0;
        r0 = r19;
        r0._unparsedCookies = r1;
    L_0x026d:
        r0 = r19;
        r1 = r0._cookies;
        if (r1 == 0) goto L_0x027a;
    L_0x0273:
        r0 = r19;
        r1 = r0._cookies;
        r1 = r1.length;
        if (r1 != 0) goto L_0x027d;
    L_0x027a:
        r1 = 0;
        goto L_0x000a;
    L_0x027d:
        r0 = r19;
        r1 = r0._cookies;
        goto L_0x000a;
    L_0x0283:
        r12 = r13;
    L_0x0284:
        r18 = r5;
        r5 = r6;
        r6 = r7;
        r7 = r8;
        r8 = r9;
        r9 = r4;
        r4 = r18;
        goto L_0x00d0;
    L_0x028f:
        switch(r17) {
            case 34: goto L_0x00dc;
            case 92: goto L_0x029d;
            default: goto L_0x0292;
        };
    L_0x0292:
        r18 = r5;
        r5 = r6;
        r6 = r7;
        r7 = r8;
        r8 = r9;
        r9 = r4;
        r4 = r18;
        goto L_0x00d0;
    L_0x029d:
        r9 = 1;
        r18 = r5;
        r5 = r6;
        r6 = r7;
        r7 = r8;
        r8 = r9;
        r9 = r4;
        r4 = r18;
        goto L_0x00d0;
    L_0x02a9:
        r18 = r5;
        r5 = r6;
        r6 = r7;
        r7 = r8;
        r8 = r9;
        r9 = r4;
        r4 = r18;
        goto L_0x00d0;
    L_0x02b4:
        r6 = "";
        goto L_0x0152;
    L_0x02b8:
        r18 = r5;
        r5 = r6;
        r6 = r7;
        r7 = r8;
        r8 = r9;
        r9 = r4;
        r4 = r18;
        goto L_0x00d0;
    L_0x02c3:
        r5 = move-exception;
        goto L_0x01b1;
    L_0x02c6:
        r12 = r13;
        goto L_0x00ea;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.Request.getCookies():javax.servlet.http.Cookie[]");
    }

    public long getDateHeader(String str) {
        return this._connection.getRequestFields().getDateField(str);
    }

    public String getHeader(String str) {
        return this._connection.getRequestFields().getStringField(str);
    }

    public Enumeration getHeaderNames() {
        return this._connection.getRequestFields().getFieldNames();
    }

    public Enumeration getHeaders(String str) {
        Enumeration values = this._connection.getRequestFields().getValues(str);
        return values == null ? Collections.enumeration(Collections.EMPTY_LIST) : values;
    }

    public int getInputState() {
        return this._inputState;
    }

    public ServletInputStream getInputStream() throws IOException {
        if (this._inputState == 0 || this._inputState == 1) {
            this._inputState = 1;
            return this._connection.getInputStream();
        }
        throw new IllegalStateException("READER");
    }

    public int getIntHeader(String str) {
        return (int) this._connection.getRequestFields().getLongField(str);
    }

    public String getLocalAddr() {
        return this._endp == null ? null : this._endp.getLocalAddr();
    }

    public String getLocalName() {
        if (this._dns) {
            if (this._endp != null) {
                return this._endp.getLocalHost();
            }
        } else if (this._endp != null) {
            return this._endp.getLocalAddr();
        }
        return null;
    }

    public int getLocalPort() {
        return this._endp == null ? 0 : this._endp.getLocalPort();
    }

    public Locale getLocale() {
        Enumeration values = this._connection.getRequestFields().getValues("Accept-Language", HttpFields.__separators);
        if (values == null || !values.hasMoreElements()) {
            return Locale.getDefault();
        }
        List qualityList = HttpFields.qualityList(values);
        if (qualityList.size() == 0) {
            return Locale.getDefault();
        }
        if (qualityList.size() >= 0) {
            return Locale.getDefault();
        }
        String valueParameters = HttpFields.valueParameters((String) qualityList.get(0), null);
        String str = HttpVersions.HTTP_0_9;
        int indexOf = valueParameters.indexOf(45);
        if (indexOf > -1) {
            str = valueParameters.substring(indexOf + 1).trim();
            valueParameters = valueParameters.substring(0, indexOf).trim();
        }
        return new Locale(valueParameters, str);
    }

    public Enumeration getLocales() {
        Enumeration values = this._connection.getRequestFields().getValues("Accept-Language", HttpFields.__separators);
        if (values == null || !values.hasMoreElements()) {
            return Collections.enumeration(__defaultLocale);
        }
        List qualityList = HttpFields.qualityList(values);
        if (qualityList.size() == 0) {
            return Collections.enumeration(__defaultLocale);
        }
        int size = qualityList.size();
        Object obj = null;
        for (int i = 0; i < size; i++) {
            String valueParameters = HttpFields.valueParameters((String) qualityList.get(i), null);
            String str = HttpVersions.HTTP_0_9;
            int indexOf = valueParameters.indexOf(45);
            if (indexOf > -1) {
                str = valueParameters.substring(indexOf + 1).trim();
                valueParameters = valueParameters.substring(0, indexOf).trim();
            }
            obj = LazyList.add(LazyList.ensureSize(obj, size), new Locale(valueParameters, str));
        }
        return LazyList.size(obj) == 0 ? Collections.enumeration(__defaultLocale) : Collections.enumeration(LazyList.getList(obj));
    }

    public String getMethod() {
        return this._method;
    }

    public String getParameter(String str) {
        if (!this._paramsExtracted) {
            extractParameters();
        }
        return (String) this._parameters.getValue(str, 0);
    }

    public Map getParameterMap() {
        if (!this._paramsExtracted) {
            extractParameters();
        }
        return Collections.unmodifiableMap(this._parameters.toStringArrayMap());
    }

    public Enumeration getParameterNames() {
        if (!this._paramsExtracted) {
            extractParameters();
        }
        return Collections.enumeration(this._parameters.keySet());
    }

    public String[] getParameterValues(String str) {
        if (!this._paramsExtracted) {
            extractParameters();
        }
        List values = this._parameters.getValues(str);
        return values == null ? null : (String[]) values.toArray(new String[values.size()]);
    }

    public MultiMap getParameters() {
        return this._parameters;
    }

    public String getPathInfo() {
        return this._pathInfo;
    }

    public String getPathTranslated() {
        return (this._pathInfo == null || this._context == null) ? null : this._context.getRealPath(this._pathInfo);
    }

    public String getProtocol() {
        return this._protocol;
    }

    public String getQueryEncoding() {
        return this._queryEncoding;
    }

    public String getQueryString() {
        if (this._queryString == null && this._uri != null) {
            if (this._queryEncoding == null) {
                this._queryString = this._uri.getQuery();
            } else {
                this._queryString = this._uri.getQuery(this._queryEncoding);
            }
        }
        return this._queryString;
    }

    public BufferedReader getReader() throws IOException {
        if (this._inputState != 0 && this._inputState != 2) {
            throw new IllegalStateException("STREAMED");
        } else if (this._inputState == 2) {
            return this._reader;
        } else {
            String characterEncoding = getCharacterEncoding();
            if (characterEncoding == null) {
                characterEncoding = StringUtil.__ISO_8859_1;
            }
            if (this._reader == null || !characterEncoding.equalsIgnoreCase(this._readerEncoding)) {
                ServletInputStream inputStream = getInputStream();
                this._readerEncoding = characterEncoding;
                this._reader = new C13291(this, new InputStreamReader(inputStream, characterEncoding), inputStream);
            }
            this._inputState = 2;
            return this._reader;
        }
    }

    public String getRealPath(String str) {
        return this._context == null ? null : this._context.getRealPath(str);
    }

    public String getRemoteAddr() {
        return this._remoteAddr != null ? this._remoteAddr : this._endp == null ? null : this._endp.getRemoteAddr();
    }

    public String getRemoteHost() {
        return this._dns ? this._remoteHost != null ? this._remoteHost : this._endp == null ? null : this._endp.getRemoteHost() : getRemoteAddr();
    }

    public int getRemotePort() {
        return this._endp == null ? 0 : this._endp.getRemotePort();
    }

    public String getRemoteUser() {
        Principal userPrincipal = getUserPrincipal();
        return userPrincipal == null ? null : userPrincipal.getName();
    }

    public RequestDispatcher getRequestDispatcher(String str) {
        if (str == null || this._context == null) {
            return null;
        }
        if (!str.startsWith(URIUtil.SLASH)) {
            String addPaths = URIUtil.addPaths(this._servletPath, this._pathInfo);
            int lastIndexOf = addPaths.lastIndexOf(URIUtil.SLASH);
            str = URIUtil.addPaths(lastIndexOf > 1 ? addPaths.substring(0, lastIndexOf + 1) : URIUtil.SLASH, str);
        }
        return this._context.getRequestDispatcher(str);
    }

    public String getRequestURI() {
        if (this._requestURI == null && this._uri != null) {
            this._requestURI = this._uri.getPathAndParam();
        }
        return this._requestURI;
    }

    public StringBuffer getRequestURL() {
        StringBuffer stringBuffer = new StringBuffer(48);
        synchronized (stringBuffer) {
            String scheme = getScheme();
            int serverPort = getServerPort();
            stringBuffer.append(scheme);
            stringBuffer.append("://");
            stringBuffer.append(getServerName());
            if (this._port > 0 && ((scheme.equalsIgnoreCase("http") && serverPort != 80) || (scheme.equalsIgnoreCase("https") && serverPort != 443))) {
                stringBuffer.append(':');
                stringBuffer.append(this._port);
            }
            stringBuffer.append(getRequestURI());
        }
        return stringBuffer;
    }

    public String getRequestedSessionId() {
        return this._requestedSessionId;
    }

    public Map getRoleMap() {
        return this._roleMap;
    }

    public StringBuffer getRootURL() {
        StringBuffer stringBuffer = new StringBuffer(48);
        synchronized (stringBuffer) {
            String scheme = getScheme();
            int serverPort = getServerPort();
            stringBuffer.append(scheme);
            stringBuffer.append("://");
            stringBuffer.append(getServerName());
            if (serverPort > 0 && ((scheme.equalsIgnoreCase("http") && serverPort != 80) || (scheme.equalsIgnoreCase("https") && serverPort != 443))) {
                stringBuffer.append(':');
                stringBuffer.append(serverPort);
            }
        }
        return stringBuffer;
    }

    public String getScheme() {
        return this._scheme;
    }

    public String getServerName() {
        if (this._serverName != null) {
            return this._serverName;
        }
        this._serverName = this._uri.getHost();
        this._port = this._uri.getPort();
        if (this._serverName != null) {
            return this._serverName;
        }
        Buffer buffer = this._connection.getRequestFields().get(HttpHeaders.HOST_BUFFER);
        if (buffer != null) {
            int length = buffer.length();
            while (true) {
                int i = length - 1;
                if (length <= 0) {
                    break;
                } else if (buffer.peek(buffer.getIndex() + i) == HttpTokens.COLON) {
                    this._serverName = BufferUtil.to8859_1_String(buffer.peek(buffer.getIndex(), i));
                    this._port = BufferUtil.toInt(buffer.peek((buffer.getIndex() + i) + 1, (buffer.length() - i) - 1));
                    return this._serverName;
                } else {
                    length = i;
                }
            }
            if (this._serverName == null || this._port < 0) {
                this._serverName = BufferUtil.to8859_1_String(buffer);
                this._port = 0;
            }
            return this._serverName;
        }
        if (this._connection != null) {
            this._serverName = getLocalName();
            this._port = getLocalPort();
            if (!(this._serverName == null || Portable.ALL_INTERFACES.equals(this._serverName))) {
                return this._serverName;
            }
        }
        try {
            this._serverName = InetAddress.getLocalHost().getHostAddress();
        } catch (Throwable e) {
            Log.ignore(e);
        }
        return this._serverName;
    }

    public int getServerPort() {
        if (this._port <= 0) {
            if (this._serverName == null) {
                getServerName();
            }
            if (this._port <= 0) {
                if (this._serverName == null || this._uri == null) {
                    this._port = this._endp == null ? 0 : this._endp.getLocalPort();
                } else {
                    this._port = this._uri.getPort();
                }
            }
        }
        return this._port <= 0 ? getScheme().equalsIgnoreCase("https") ? 443 : 80 : this._port;
    }

    public ServletContext getServletContext() {
        return this._context;
    }

    public String getServletName() {
        return this._servletName;
    }

    public String getServletPath() {
        if (this._servletPath == null) {
            this._servletPath = HttpVersions.HTTP_0_9;
        }
        return this._servletPath;
    }

    public ServletResponse getServletResponse() {
        return this._connection.getResponse();
    }

    public HttpSession getSession() {
        return getSession(true);
    }

    public HttpSession getSession(boolean z) {
        if (this._sessionManager == null && z) {
            throw new IllegalStateException("No SessionHandler or SessionManager");
        } else if (this._session != null && this._sessionManager != null && this._sessionManager.isValid(this._session)) {
            return this._session;
        } else {
            this._session = null;
            String requestedSessionId = getRequestedSessionId();
            if (!(requestedSessionId == null || this._sessionManager == null)) {
                this._session = this._sessionManager.getHttpSession(requestedSessionId);
                if (this._session == null && !z) {
                    return null;
                }
            }
            if (this._session == null && this._sessionManager != null && z) {
                this._session = this._sessionManager.newHttpSession(this);
                Cookie sessionCookie = this._sessionManager.getSessionCookie(this._session, getContextPath(), isSecure());
                if (sessionCookie != null) {
                    this._connection.getResponse().addCookie(sessionCookie);
                }
            }
            return this._session;
        }
    }

    public SessionManager getSessionManager() {
        return this._sessionManager;
    }

    public long getTimeStamp() {
        return this._timeStamp;
    }

    public Buffer getTimeStampBuffer() {
        if (this._timeStampBuffer == null && this._timeStamp > 0) {
            this._timeStampBuffer = HttpFields.__dateCache.formatBuffer(this._timeStamp);
        }
        return this._timeStampBuffer;
    }

    public HttpURI getUri() {
        return this._uri;
    }

    public Principal getUserPrincipal() {
        if (this._userPrincipal != null && (this._userPrincipal instanceof NotChecked)) {
            NotChecked notChecked = (NotChecked) this._userPrincipal;
            this._userPrincipal = SecurityHandler.__NO_USER;
            Authenticator authenticator = notChecked.getSecurityHandler().getAuthenticator();
            UserRealm userRealm = notChecked.getSecurityHandler().getUserRealm();
            String servletPath = getPathInfo() == null ? getServletPath() : new StringBuffer().append(getServletPath()).append(getPathInfo()).toString();
            if (!(userRealm == null || authenticator == null)) {
                try {
                    authenticator.authenticate(userRealm, servletPath, this, null);
                } catch (Throwable e) {
                    Log.ignore(e);
                }
            }
        }
        return this._userPrincipal == SecurityHandler.__NO_USER ? null : this._userPrincipal;
    }

    public UserRealm getUserRealm() {
        return this._userRealm;
    }

    public boolean isHandled() {
        return this._handled;
    }

    public boolean isRequestedSessionIdFromCookie() {
        return this._requestedSessionId != null && this._requestedSessionIdFromCookie;
    }

    public boolean isRequestedSessionIdFromURL() {
        return (this._requestedSessionId == null || this._requestedSessionIdFromCookie) ? false : true;
    }

    public boolean isRequestedSessionIdFromUrl() {
        return (this._requestedSessionId == null || this._requestedSessionIdFromCookie) ? false : true;
    }

    public boolean isRequestedSessionIdValid() {
        if (this._requestedSessionId == null) {
            return false;
        }
        HttpSession session = getSession(false);
        return session != null ? this._sessionManager.getIdManager().getClusterId(this._requestedSessionId).equals(this._sessionManager.getClusterId(session)) : false;
    }

    public boolean isSecure() {
        return this._connection.isConfidential(this);
    }

    public boolean isUserInRole(String str) {
        if (this._roleMap != null) {
            String str2 = (String) this._roleMap.get(str);
            if (str2 != null) {
                str = str2;
            }
        }
        Principal userPrincipal = getUserPrincipal();
        return (this._userRealm == null || userPrincipal == null) ? false : this._userRealm.isUserInRole(userPrincipal, str);
    }

    public HttpSession recoverNewSession(Object obj) {
        return this._savedNewSessions == null ? null : (HttpSession) this._savedNewSessions.get(obj);
    }

    protected void recycle() {
        if (this._inputState == 2) {
            try {
                int read = this._reader.read();
                while (read != -1) {
                    read = this._reader.read();
                }
            } catch (Throwable e) {
                Log.ignore(e);
                this._reader = null;
            }
        }
        this._handled = false;
        if (this._context != null) {
            throw new IllegalStateException("Request in context!");
        }
        if (this._attributes != null) {
            this._attributes.clearAttributes();
        }
        this._authType = null;
        this._characterEncoding = null;
        this._queryEncoding = null;
        this._context = null;
        this._serverName = null;
        this._method = null;
        this._pathInfo = null;
        this._port = 0;
        this._protocol = HttpVersions.HTTP_1_1;
        this._queryString = null;
        this._requestedSessionId = null;
        this._requestedSessionIdFromCookie = false;
        this._session = null;
        this._sessionManager = null;
        this._requestURI = null;
        this._scheme = "http";
        this._servletPath = null;
        this._timeStamp = 0;
        this._timeStampBuffer = null;
        this._uri = null;
        this._userPrincipal = null;
        if (this._baseParameters != null) {
            this._baseParameters.clear();
        }
        this._parameters = null;
        this._paramsExtracted = false;
        this._inputState = 0;
        this._cookiesExtracted = false;
        if (this._savedNewSessions != null) {
            this._savedNewSessions.clear();
        }
        this._savedNewSessions = null;
        if (this._continuation != null && this._continuation.isPending()) {
            this._continuation.reset();
        }
    }

    public void removeAttribute(String str) {
        Object attribute = this._attributes == null ? null : this._attributes.getAttribute(str);
        if (this._attributes != null) {
            this._attributes.removeAttribute(str);
        }
        if (attribute != null && this._requestAttributeListeners != null) {
            ServletRequestAttributeEvent servletRequestAttributeEvent = new ServletRequestAttributeEvent(this._context, this, str, attribute);
            int size = LazyList.size(this._requestAttributeListeners);
            for (int i = 0; i < size; i++) {
                ServletRequestAttributeListener servletRequestAttributeListener = (ServletRequestAttributeListener) LazyList.get(this._requestAttributeListeners, i);
                if (servletRequestAttributeListener instanceof ServletRequestAttributeListener) {
                    servletRequestAttributeListener.attributeRemoved(servletRequestAttributeEvent);
                }
            }
        }
    }

    public void removeEventListener(EventListener eventListener) {
        this._requestAttributeListeners = LazyList.remove(this._requestAttributeListeners, (Object) eventListener);
    }

    public void saveNewSession(Object obj, HttpSession httpSession) {
        if (this._savedNewSessions == null) {
            this._savedNewSessions = new HashMap();
        }
        this._savedNewSessions.put(obj, httpSession);
    }

    public void setAttribute(String str, Object obj) {
        String str2 = null;
        String attribute = this._attributes == null ? null : this._attributes.getAttribute(str);
        if ("org.mortbay.jetty.Request.queryEncoding".equals(str)) {
            if (obj != null) {
                str2 = obj.toString();
            }
            setQueryEncoding(str2);
        } else if ("org.mortbay.jetty.ResponseBuffer".equals(str)) {
            try {
                ByteBuffer byteBuffer = (ByteBuffer) obj;
                synchronized (byteBuffer) {
                    Buffer directNIOBuffer;
                    if (byteBuffer.isDirect()) {
                        directNIOBuffer = new DirectNIOBuffer(byteBuffer, true);
                    } else {
                        Object indirectNIOBuffer = new IndirectNIOBuffer(byteBuffer, true);
                    }
                    ((Output) getServletResponse().getOutputStream()).sendResponse(directNIOBuffer);
                }
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        if (this._attributes == null) {
            this._attributes = new AttributesMap();
        }
        this._attributes.setAttribute(str, obj);
        if (this._requestAttributeListeners != null) {
            Object obj2;
            SContext sContext = this._context;
            if (attribute == null) {
                obj2 = obj;
            } else {
                str2 = attribute;
            }
            ServletRequestAttributeEvent servletRequestAttributeEvent = new ServletRequestAttributeEvent(sContext, this, str, obj2);
            int size = LazyList.size(this._requestAttributeListeners);
            for (int i = 0; i < size; i++) {
                ServletRequestAttributeListener servletRequestAttributeListener = (ServletRequestAttributeListener) LazyList.get(this._requestAttributeListeners, i);
                if (servletRequestAttributeListener instanceof ServletRequestAttributeListener) {
                    servletRequestAttributeListener = servletRequestAttributeListener;
                    if (attribute == null) {
                        servletRequestAttributeListener.attributeAdded(servletRequestAttributeEvent);
                    } else if (obj == null) {
                        servletRequestAttributeListener.attributeRemoved(servletRequestAttributeEvent);
                    } else {
                        servletRequestAttributeListener.attributeReplaced(servletRequestAttributeEvent);
                    }
                }
            }
        }
    }

    public void setAttributes(Attributes attributes) {
        this._attributes = attributes;
    }

    public void setAuthType(String str) {
        this._authType = str;
    }

    public void setCharacterEncoding(String str) throws UnsupportedEncodingException {
        if (this._inputState == 0) {
            this._characterEncoding = str;
            if (!StringUtil.isUTF8(str)) {
                HttpVersions.HTTP_0_9.getBytes(str);
            }
        }
    }

    public void setCharacterEncodingUnchecked(String str) {
        this._characterEncoding = str;
    }

    protected void setConnection(HttpConnection httpConnection) {
        this._connection = httpConnection;
        this._endp = httpConnection.getEndPoint();
        this._dns = httpConnection.getResolveNames();
    }

    public void setContentType(String str) {
        this._connection.getRequestFields().put(HttpHeaders.CONTENT_TYPE_BUFFER, str);
    }

    public void setContext(SContext sContext) {
        this._context = sContext;
    }

    public void setContextPath(String str) {
        this._contextPath = str;
    }

    void setContinuation(Continuation continuation) {
        this._continuation = continuation;
    }

    public void setCookies(Cookie[] cookieArr) {
        this._cookies = cookieArr;
    }

    public void setHandled(boolean z) {
        this._handled = z;
    }

    public void setMethod(String str) {
        this._method = str;
    }

    public void setParameters(MultiMap multiMap) {
        if (multiMap == null) {
            multiMap = this._baseParameters;
        }
        this._parameters = multiMap;
        if (this._paramsExtracted && this._parameters == null) {
            throw new IllegalStateException();
        }
    }

    public void setPathInfo(String str) {
        this._pathInfo = str;
    }

    public void setProtocol(String str) {
        this._protocol = str;
    }

    public void setQueryEncoding(String str) {
        this._queryEncoding = str;
        this._queryString = null;
    }

    public void setQueryString(String str) {
        this._queryString = str;
    }

    public void setRemoteAddr(String str) {
        this._remoteAddr = str;
    }

    public void setRemoteHost(String str) {
        this._remoteHost = str;
    }

    public void setRequestListeners(Object obj) {
        this._requestListeners = obj;
    }

    public void setRequestURI(String str) {
        this._requestURI = str;
    }

    public void setRequestedSessionId(String str) {
        this._requestedSessionId = str;
    }

    public void setRequestedSessionIdFromCookie(boolean z) {
        this._requestedSessionIdFromCookie = z;
    }

    public void setRoleMap(Map map) {
        this._roleMap = map;
    }

    public void setScheme(String str) {
        this._scheme = str;
    }

    public void setServerName(String str) {
        this._serverName = str;
    }

    public void setServerPort(int i) {
        this._port = i;
    }

    public void setServletName(String str) {
        this._servletName = str;
    }

    public void setServletPath(String str) {
        this._servletPath = str;
    }

    public void setSession(HttpSession httpSession) {
        this._session = httpSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this._sessionManager = sessionManager;
    }

    public void setTimeStamp(long j) {
        this._timeStamp = j;
    }

    public void setUri(HttpURI httpURI) {
        this._uri = httpURI;
    }

    public void setUserPrincipal(Principal principal) {
        this._userPrincipal = principal;
    }

    public void setUserRealm(UserRealm userRealm) {
        this._userRealm = userRealm;
    }

    public Object takeRequestListeners() {
        Object obj = this._requestListeners;
        this._requestListeners = null;
        return obj;
    }

    public String toString() {
        return new StringBuffer().append(getMethod()).append(" ").append(this._uri).append(" ").append(getProtocol()).append("\n").append(this._connection.getRequestFields().toString()).toString();
    }
}
