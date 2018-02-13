package org.mortbay.jetty.servlet;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map.Entry;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.util.Attributes;
import org.mortbay.util.LazyList;
import org.mortbay.util.MultiMap;
import org.mortbay.util.UrlEncoded;

public class Dispatcher implements RequestDispatcher {
    public static final String __FORWARD_CONTEXT_PATH = "javax.servlet.forward.context_path";
    public static final String __FORWARD_JETTY = "org.mortbay.jetty.forwarded";
    public static final String __FORWARD_PATH_INFO = "javax.servlet.forward.path_info";
    public static final String __FORWARD_PREFIX = "javax.servlet.forward.";
    public static final String __FORWARD_QUERY_STRING = "javax.servlet.forward.query_string";
    public static final String __FORWARD_REQUEST_URI = "javax.servlet.forward.request_uri";
    public static final String __FORWARD_SERVLET_PATH = "javax.servlet.forward.servlet_path";
    public static final String __INCLUDE_CONTEXT_PATH = "javax.servlet.include.context_path";
    public static final String __INCLUDE_JETTY = "org.mortbay.jetty.included";
    public static final String __INCLUDE_PATH_INFO = "javax.servlet.include.path_info";
    public static final String __INCLUDE_PREFIX = "javax.servlet.include.";
    public static final String __INCLUDE_QUERY_STRING = "javax.servlet.include.query_string";
    public static final String __INCLUDE_REQUEST_URI = "javax.servlet.include.request_uri";
    public static final String __INCLUDE_SERVLET_PATH = "javax.servlet.include.servlet_path";
    public static final String __JSP_FILE = "org.apache.catalina.jsp_file";
    private ContextHandler _contextHandler;
    private String _dQuery;
    private String _named;
    private String _path;
    private String _uri;

    private class ForwardAttributes implements Attributes {
        Attributes _attr;
        String _contextPath;
        String _pathInfo;
        String _query;
        String _requestURI;
        String _servletPath;
        private final Dispatcher this$0;

        ForwardAttributes(Dispatcher dispatcher, Attributes attributes) {
            this.this$0 = dispatcher;
            this._attr = attributes;
        }

        public void clearAttributes() {
            throw new IllegalStateException();
        }

        public Object getAttribute(String str) {
            if (Dispatcher.access$000(this.this$0) == null) {
                if (str.equals(Dispatcher.__FORWARD_PATH_INFO)) {
                    return this._pathInfo;
                }
                if (str.equals(Dispatcher.__FORWARD_REQUEST_URI)) {
                    return this._requestURI;
                }
                if (str.equals(Dispatcher.__FORWARD_SERVLET_PATH)) {
                    return this._servletPath;
                }
                if (str.equals(Dispatcher.__FORWARD_CONTEXT_PATH)) {
                    return this._contextPath;
                }
                if (str.equals(Dispatcher.__FORWARD_QUERY_STRING)) {
                    return this._query;
                }
            }
            return (str.startsWith(Dispatcher.__INCLUDE_PREFIX) || str.equals(Dispatcher.__INCLUDE_JETTY)) ? null : str.equals(Dispatcher.__FORWARD_JETTY) ? Boolean.TRUE : this._attr.getAttribute(str);
        }

        public Enumeration getAttributeNames() {
            Collection hashSet = new HashSet();
            Enumeration attributeNames = this._attr.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String str = (String) attributeNames.nextElement();
                if (!(str.startsWith(Dispatcher.__INCLUDE_PREFIX) || str.startsWith(Dispatcher.__FORWARD_PREFIX))) {
                    hashSet.add(str);
                }
            }
            if (Dispatcher.access$000(this.this$0) == null) {
                if (this._pathInfo != null) {
                    hashSet.add(Dispatcher.__FORWARD_PATH_INFO);
                } else {
                    hashSet.remove(Dispatcher.__FORWARD_PATH_INFO);
                }
                hashSet.add(Dispatcher.__FORWARD_REQUEST_URI);
                hashSet.add(Dispatcher.__FORWARD_SERVLET_PATH);
                hashSet.add(Dispatcher.__FORWARD_CONTEXT_PATH);
                if (this._query != null) {
                    hashSet.add(Dispatcher.__FORWARD_QUERY_STRING);
                } else {
                    hashSet.remove(Dispatcher.__FORWARD_QUERY_STRING);
                }
            }
            return Collections.enumeration(hashSet);
        }

        public void removeAttribute(String str) {
            setAttribute(str, null);
        }

        public void setAttribute(String str, Object obj) {
            if (Dispatcher.access$000(this.this$0) == null && str.startsWith("javax.servlet.")) {
                if (str.equals(Dispatcher.__FORWARD_PATH_INFO)) {
                    this._pathInfo = (String) obj;
                } else if (str.equals(Dispatcher.__FORWARD_REQUEST_URI)) {
                    this._requestURI = (String) obj;
                } else if (str.equals(Dispatcher.__FORWARD_SERVLET_PATH)) {
                    this._servletPath = (String) obj;
                } else if (str.equals(Dispatcher.__FORWARD_CONTEXT_PATH)) {
                    this._contextPath = (String) obj;
                } else if (str.equals(Dispatcher.__FORWARD_QUERY_STRING)) {
                    this._query = (String) obj;
                } else if (obj == null) {
                    this._attr.removeAttribute(str);
                } else {
                    this._attr.setAttribute(str, obj);
                }
            } else if (obj == null) {
                this._attr.removeAttribute(str);
            } else {
                this._attr.setAttribute(str, obj);
            }
        }

        public String toString() {
            return new StringBuffer().append("FORWARD+").append(this._attr.toString()).toString();
        }
    }

    private class IncludeAttributes implements Attributes {
        Attributes _attr;
        String _contextPath;
        String _pathInfo;
        String _query;
        String _requestURI;
        String _servletPath;
        private final Dispatcher this$0;

        IncludeAttributes(Dispatcher dispatcher, Attributes attributes) {
            this.this$0 = dispatcher;
            this._attr = attributes;
        }

        public void clearAttributes() {
            throw new IllegalStateException();
        }

        public Object getAttribute(String str) {
            if (Dispatcher.access$000(this.this$0) == null) {
                if (str.equals(Dispatcher.__INCLUDE_PATH_INFO)) {
                    return this._pathInfo;
                }
                if (str.equals(Dispatcher.__INCLUDE_SERVLET_PATH)) {
                    return this._servletPath;
                }
                if (str.equals(Dispatcher.__INCLUDE_CONTEXT_PATH)) {
                    return this._contextPath;
                }
                if (str.equals(Dispatcher.__INCLUDE_QUERY_STRING)) {
                    return this._query;
                }
                if (str.equals(Dispatcher.__INCLUDE_REQUEST_URI)) {
                    return this._requestURI;
                }
            } else if (str.startsWith(Dispatcher.__INCLUDE_PREFIX)) {
                return null;
            }
            return str.equals(Dispatcher.__INCLUDE_JETTY) ? Boolean.TRUE : this._attr.getAttribute(str);
        }

        public Enumeration getAttributeNames() {
            Collection hashSet = new HashSet();
            Enumeration attributeNames = this._attr.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String str = (String) attributeNames.nextElement();
                if (!str.startsWith(Dispatcher.__INCLUDE_PREFIX)) {
                    hashSet.add(str);
                }
            }
            if (Dispatcher.access$000(this.this$0) == null) {
                if (this._pathInfo != null) {
                    hashSet.add(Dispatcher.__INCLUDE_PATH_INFO);
                } else {
                    hashSet.remove(Dispatcher.__INCLUDE_PATH_INFO);
                }
                hashSet.add(Dispatcher.__INCLUDE_REQUEST_URI);
                hashSet.add(Dispatcher.__INCLUDE_SERVLET_PATH);
                hashSet.add(Dispatcher.__INCLUDE_CONTEXT_PATH);
                if (this._query != null) {
                    hashSet.add(Dispatcher.__INCLUDE_QUERY_STRING);
                } else {
                    hashSet.remove(Dispatcher.__INCLUDE_QUERY_STRING);
                }
            }
            return Collections.enumeration(hashSet);
        }

        public void removeAttribute(String str) {
            setAttribute(str, null);
        }

        public void setAttribute(String str, Object obj) {
            if (Dispatcher.access$000(this.this$0) == null && str.startsWith("javax.servlet.")) {
                if (str.equals(Dispatcher.__INCLUDE_PATH_INFO)) {
                    this._pathInfo = (String) obj;
                } else if (str.equals(Dispatcher.__INCLUDE_REQUEST_URI)) {
                    this._requestURI = (String) obj;
                } else if (str.equals(Dispatcher.__INCLUDE_SERVLET_PATH)) {
                    this._servletPath = (String) obj;
                } else if (str.equals(Dispatcher.__INCLUDE_CONTEXT_PATH)) {
                    this._contextPath = (String) obj;
                } else if (str.equals(Dispatcher.__INCLUDE_QUERY_STRING)) {
                    this._query = (String) obj;
                } else if (obj == null) {
                    this._attr.removeAttribute(str);
                } else {
                    this._attr.setAttribute(str, obj);
                }
            } else if (obj == null) {
                this._attr.removeAttribute(str);
            } else {
                this._attr.setAttribute(str, obj);
            }
        }

        public String toString() {
            return new StringBuffer().append("INCLUDE+").append(this._attr.toString()).toString();
        }
    }

    public Dispatcher(ContextHandler contextHandler, String str) throws IllegalStateException {
        this._contextHandler = contextHandler;
        this._named = str;
    }

    public Dispatcher(ContextHandler contextHandler, String str, String str2, String str3) {
        this._contextHandler = contextHandler;
        this._uri = str;
        this._path = str2;
        this._dQuery = str3;
    }

    static String access$000(Dispatcher dispatcher) {
        return dispatcher._named;
    }

    public static int type(String str) {
        if ("request".equalsIgnoreCase(str)) {
            return 1;
        }
        if ("forward".equalsIgnoreCase(str)) {
            return 2;
        }
        if ("include".equalsIgnoreCase(str)) {
            return 4;
        }
        if ("error".equalsIgnoreCase(str)) {
            return 8;
        }
        throw new IllegalArgumentException(str);
    }

    public void error(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        forward(servletRequest, servletResponse, 8);
    }

    public void forward(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        forward(servletRequest, servletResponse, 2);
    }

    protected void forward(ServletRequest servletRequest, ServletResponse servletResponse, int i) throws ServletException, IOException {
        Throwable th;
        Request request = servletRequest instanceof Request ? (Request) servletRequest : HttpConnection.getCurrentConnection().getRequest();
        servletResponse.resetBuffer();
        servletRequest.removeAttribute(__JSP_FILE);
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String pathInfo = request.getPathInfo();
        String queryString = request.getQueryString();
        Attributes attributes = request.getAttributes();
        MultiMap parameters = request.getParameters();
        try {
            if (this._named != null) {
                this._contextHandler.handle(this._named, (HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, i);
            } else {
                String str = this._dQuery;
                if (str != null) {
                    MultiMap parameters2;
                    String str2;
                    int i2;
                    String str3;
                    MultiMap multiMap = new MultiMap();
                    UrlEncoded.decodeTo(str, multiMap, servletRequest.getCharacterEncoding());
                    Object obj = null;
                    if (parameters == null) {
                        request.getParameterNames();
                        parameters2 = request.getParameters();
                    } else {
                        parameters2 = parameters;
                    }
                    if (parameters2 != null) {
                        try {
                            if (parameters2.size() > 0) {
                                obj = null;
                                for (Entry entry : parameters2.entrySet()) {
                                    str2 = (String) entry.getKey();
                                    if (multiMap.containsKey(str2)) {
                                        obj = 1;
                                    }
                                    Object value = entry.getValue();
                                    for (i2 = 0; i2 < LazyList.size(value); i2++) {
                                        multiMap.add(str2, LazyList.get(value, i2));
                                    }
                                }
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            parameters = parameters2;
                            request.setRequestURI(requestURI);
                            request.setContextPath(contextPath);
                            request.setServletPath(servletPath);
                            request.setPathInfo(pathInfo);
                            request.setAttributes(attributes);
                            request.setParameters(parameters);
                            request.setQueryString(queryString);
                            throw th;
                        }
                    }
                    if (queryString == null || queryString.length() <= 0) {
                        str3 = str;
                    } else if (obj != null) {
                        StringBuffer stringBuffer = new StringBuffer();
                        MultiMap multiMap2 = new MultiMap();
                        UrlEncoded.decodeTo(queryString, multiMap2, servletRequest.getCharacterEncoding());
                        MultiMap multiMap3 = new MultiMap();
                        UrlEncoded.decodeTo(str, multiMap3, servletRequest.getCharacterEncoding());
                        for (Entry entry2 : multiMap2.entrySet()) {
                            str2 = (String) entry2.getKey();
                            if (!multiMap3.containsKey(str2)) {
                                Object value2 = entry2.getValue();
                                for (i2 = 0; i2 < LazyList.size(value2); i2++) {
                                    stringBuffer.append(new StringBuffer().append("&").append(str2).append("=").append(LazyList.get(value2, i2)).toString());
                                }
                            }
                        }
                        str3 = new StringBuffer().append(str).append(stringBuffer).toString();
                    } else {
                        str3 = new StringBuffer().append(str).append("&").append(queryString).toString();
                    }
                    request.setParameters(multiMap);
                    request.setQueryString(str3);
                    str = str3;
                    parameters = parameters2;
                }
                ForwardAttributes forwardAttributes = new ForwardAttributes(this, attributes);
                if (((String) attributes.getAttribute(__FORWARD_REQUEST_URI)) != null) {
                    forwardAttributes._pathInfo = (String) attributes.getAttribute(__FORWARD_PATH_INFO);
                    forwardAttributes._query = (String) attributes.getAttribute(__FORWARD_QUERY_STRING);
                    forwardAttributes._requestURI = (String) attributes.getAttribute(__FORWARD_REQUEST_URI);
                    forwardAttributes._contextPath = (String) attributes.getAttribute(__FORWARD_CONTEXT_PATH);
                    forwardAttributes._servletPath = (String) attributes.getAttribute(__FORWARD_SERVLET_PATH);
                } else {
                    forwardAttributes._pathInfo = pathInfo;
                    forwardAttributes._query = queryString;
                    forwardAttributes._requestURI = requestURI;
                    forwardAttributes._contextPath = contextPath;
                    forwardAttributes._servletPath = servletPath;
                }
                request.setRequestURI(this._uri);
                request.setContextPath(this._contextHandler.getContextPath());
                request.setAttributes(forwardAttributes);
                request.setQueryString(str);
                this._contextHandler.handle(this._path, (HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, i);
                if (request.getConnection().getResponse().isWriting()) {
                    servletResponse.getWriter().close();
                } else {
                    try {
                        servletResponse.getOutputStream().close();
                    } catch (IllegalStateException e) {
                        servletResponse.getWriter().close();
                    }
                }
            }
        } catch (IllegalStateException e2) {
            servletResponse.getOutputStream().close();
        } catch (Throwable th3) {
            th = th3;
            request.setRequestURI(requestURI);
            request.setContextPath(contextPath);
            request.setServletPath(servletPath);
            request.setPathInfo(pathInfo);
            request.setAttributes(attributes);
            request.setParameters(parameters);
            request.setQueryString(queryString);
            throw th;
        }
        request.setRequestURI(requestURI);
        request.setContextPath(contextPath);
        request.setServletPath(servletPath);
        request.setPathInfo(pathInfo);
        request.setAttributes(attributes);
        request.setParameters(parameters);
        request.setQueryString(queryString);
    }

    public void include(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        Request request = servletRequest instanceof Request ? (Request) servletRequest : HttpConnection.getCurrentConnection().getRequest();
        servletRequest.removeAttribute(__JSP_FILE);
        Attributes attributes = request.getAttributes();
        MultiMap parameters = request.getParameters();
        try {
            request.getConnection().include();
            if (this._named != null) {
                this._contextHandler.handle(this._named, (HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, 4);
            } else {
                String str = this._dQuery;
                if (str != null) {
                    MultiMap multiMap = new MultiMap();
                    UrlEncoded.decodeTo(str, multiMap, servletRequest.getCharacterEncoding());
                    if (parameters != null && parameters.size() > 0) {
                        for (Entry entry : parameters.entrySet()) {
                            String str2 = (String) entry.getKey();
                            Object value = entry.getValue();
                            for (int i = 0; i < LazyList.size(value); i++) {
                                multiMap.add(str2, LazyList.get(value, i));
                            }
                        }
                    }
                    request.setParameters(multiMap);
                }
                Attributes includeAttributes = new IncludeAttributes(this, attributes);
                includeAttributes._requestURI = this._uri;
                includeAttributes._contextPath = this._contextHandler.getContextPath();
                includeAttributes._servletPath = null;
                includeAttributes._pathInfo = this._path;
                includeAttributes._query = str;
                request.setAttributes(includeAttributes);
                this._contextHandler.handle(this._named == null ? this._path : this._named, (HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, 4);
            }
            request.setAttributes(attributes);
            request.getConnection().included();
            request.setParameters(parameters);
        } catch (Throwable th) {
            request.setAttributes(attributes);
            request.getConnection().included();
            request.setParameters(parameters);
        }
    }
}
