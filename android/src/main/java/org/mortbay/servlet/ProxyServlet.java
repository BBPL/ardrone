package org.mortbay.servlet;

import com.google.common.net.HttpHeaders;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashSet;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.client.cache.HeaderConstants;
import org.mortbay.jetty.HttpHeaderValues;
import org.mortbay.jetty.HttpVersions;
import org.mortbay.util.IO;

public class ProxyServlet implements Servlet {
    protected HashSet _DontProxyHeaders = new HashSet();
    protected ServletConfig _config;
    protected ServletContext _context;

    public static class Transparent extends ProxyServlet {
        String _prefix;
        String _proxyTo;

        public Transparent(String str, String str2, int i) {
            this._prefix = str;
            this._proxyTo = new StringBuffer().append("http://").append(str2).append(":").append(i).toString();
        }

        public void init(ServletConfig servletConfig) throws ServletException {
            if (servletConfig.getInitParameter("ProxyTo") != null) {
                this._proxyTo = servletConfig.getInitParameter("ProxyTo");
            }
            if (servletConfig.getInitParameter("Prefix") != null) {
                this._prefix = servletConfig.getInitParameter("Prefix");
            }
            if (this._proxyTo == null) {
                throw new UnavailableException("No ProxyTo");
            }
            super.init(servletConfig);
            this._context.log(new StringBuffer().append("Transparent ProxyServlet @ ").append(this._prefix == null ? "-" : this._prefix).append(" to ").append(this._proxyTo).toString());
        }

        protected URL proxyHttpURL(String str, String str2, int i, String str3) throws MalformedURLException {
            return (this._prefix == null || str3.startsWith(this._prefix)) ? this._prefix != null ? new URL(new StringBuffer().append(this._proxyTo).append(str3.substring(this._prefix.length())).toString()) : new URL(new StringBuffer().append(this._proxyTo).append(str3).toString()) : null;
        }
    }

    public ProxyServlet() {
        this._DontProxyHeaders.add("proxy-connection");
        this._DontProxyHeaders.add("connection");
        this._DontProxyHeaders.add(HttpHeaderValues.KEEP_ALIVE);
        this._DontProxyHeaders.add("transfer-encoding");
        this._DontProxyHeaders.add("te");
        this._DontProxyHeaders.add("trailer");
        this._DontProxyHeaders.add("proxy-authorization");
        this._DontProxyHeaders.add("proxy-authenticate");
        this._DontProxyHeaders.add("upgrade");
    }

    public void destroy() {
    }

    public ServletConfig getServletConfig() {
        return this._config;
    }

    public String getServletInfo() {
        return "Proxy Servlet";
    }

    public void handleConnect(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String requestURI = httpServletRequest.getRequestURI();
        String str = HttpVersions.HTTP_0_9;
        String str2 = HttpVersions.HTTP_0_9;
        int indexOf = requestURI.indexOf(58);
        if (indexOf >= 0) {
            str = requestURI.substring(indexOf + 1);
            str2 = requestURI.substring(0, indexOf);
            if (str2.indexOf(47) > 0) {
                str2 = str2.substring(str2.indexOf(47) + 1);
            }
        }
        InetSocketAddress inetSocketAddress = new InetSocketAddress(str2, Integer.parseInt(str));
        InputStream inputStream = httpServletRequest.getInputStream();
        OutputStream outputStream = httpServletResponse.getOutputStream();
        Socket socket = new Socket(inetSocketAddress.getAddress(), inetSocketAddress.getPort());
        httpServletResponse.setStatus(200);
        httpServletResponse.setHeader("Connection", HttpHeaderValues.CLOSE);
        httpServletResponse.flushBuffer();
        IO.copyThread(socket.getInputStream(), outputStream);
        IO.copy(inputStream, socket.getOutputStream());
    }

    public void init(ServletConfig servletConfig) throws ServletException {
        this._config = servletConfig;
        this._context = servletConfig.getServletContext();
    }

    protected URL proxyHttpURL(String str, String str2, int i, String str3) throws MalformedURLException {
        return new URL(str, str2, i, str3);
    }

    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        HttpURLConnection httpURLConnection;
        int i = 0;
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        if ("CONNECT".equalsIgnoreCase(httpServletRequest.getMethod())) {
            handleConnect(httpServletRequest, httpServletResponse);
            return;
        }
        String str;
        Enumeration headerNames;
        int i2;
        int i3;
        String toLowerCase;
        Enumeration headers;
        String str2;
        InputStream inputStream;
        String headerFieldKey;
        String requestURI = httpServletRequest.getRequestURI();
        if (httpServletRequest.getQueryString() != null) {
            requestURI = new StringBuffer().append(requestURI).append("?").append(httpServletRequest.getQueryString()).toString();
        }
        URLConnection openConnection = proxyHttpURL(httpServletRequest.getScheme(), httpServletRequest.getServerName(), httpServletRequest.getServerPort(), requestURI).openConnection();
        openConnection.setAllowUserInteraction(false);
        if (openConnection instanceof HttpURLConnection) {
            HttpURLConnection httpURLConnection2 = (HttpURLConnection) openConnection;
            httpURLConnection2.setRequestMethod(httpServletRequest.getMethod());
            httpURLConnection2.setInstanceFollowRedirects(false);
            httpURLConnection = httpURLConnection2;
        } else {
            httpURLConnection = null;
        }
        requestURI = httpServletRequest.getHeader("Connection");
        if (requestURI != null) {
            requestURI = requestURI.toLowerCase();
            if (requestURI.equals(HttpHeaderValues.KEEP_ALIVE) || requestURI.equals(HttpHeaderValues.CLOSE)) {
                str = null;
                headerNames = httpServletRequest.getHeaderNames();
                i2 = 0;
                i3 = 0;
                while (headerNames.hasMoreElements()) {
                    requestURI = (String) headerNames.nextElement();
                    toLowerCase = requestURI.toLowerCase();
                    if (!this._DontProxyHeaders.contains(toLowerCase) && (r5 == null || r5.indexOf(toLowerCase) < 0)) {
                        int i4 = "content-type".equals(toLowerCase) ? 1 : i2;
                        headers = httpServletRequest.getHeaders(requestURI);
                        while (headers.hasMoreElements()) {
                            str2 = (String) headers.nextElement();
                            if (str2 != null) {
                                openConnection.addRequestProperty(requestURI, str2);
                                i3 |= "X-Forwarded-For".equalsIgnoreCase(requestURI);
                            }
                        }
                        i2 = i4;
                    }
                }
                openConnection.setRequestProperty("Via", "1.1 (jetty)");
                if (i3 == 0) {
                    openConnection.addRequestProperty("X-Forwarded-For", httpServletRequest.getRemoteAddr());
                    openConnection.addRequestProperty(HttpHeaders.X_FORWARDED_PROTO, httpServletRequest.getScheme());
                    openConnection.addRequestProperty("X-Forwarded-Host", httpServletRequest.getServerName());
                    openConnection.addRequestProperty("X-Forwarded-Server", httpServletRequest.getLocalName());
                }
                requestURI = httpServletRequest.getHeader("Cache-Control");
                if (requestURI != null && (requestURI.indexOf("no-cache") >= 0 || requestURI.indexOf(HeaderConstants.CACHE_CONTROL_NO_STORE) >= 0)) {
                    openConnection.setUseCaches(false);
                }
                openConnection.setDoInput(true);
                inputStream = httpServletRequest.getInputStream();
                if (i2 != 0) {
                    openConnection.setDoOutput(true);
                    IO.copy(inputStream, openConnection.getOutputStream());
                }
                openConnection.connect();
                if (httpURLConnection == null) {
                    inputStream = httpURLConnection.getErrorStream();
                    httpServletResponse.setStatus(httpURLConnection.getResponseCode(), httpURLConnection.getResponseMessage());
                } else {
                    inputStream = null;
                }
                if (inputStream == null) {
                    try {
                        inputStream = openConnection.getInputStream();
                    } catch (Throwable e) {
                        this._context.log("stream", e);
                        inputStream = httpURLConnection.getErrorStream();
                    }
                }
                httpServletResponse.setHeader("Date", null);
                httpServletResponse.setHeader("Server", null);
                headerFieldKey = openConnection.getHeaderFieldKey(0);
                str2 = openConnection.getHeaderField(0);
                while (true) {
                    if (headerFieldKey != null && str2 == null) {
                        break;
                    }
                    Object toLowerCase2 = headerFieldKey == null ? headerFieldKey.toLowerCase() : null;
                    if (!(headerFieldKey == null || str2 == null || this._DontProxyHeaders.contains(toLowerCase2))) {
                        httpServletResponse.addHeader(headerFieldKey, str2);
                    }
                    i++;
                    headerFieldKey = openConnection.getHeaderFieldKey(i);
                    str2 = openConnection.getHeaderField(i);
                }
                httpServletResponse.addHeader("Via", "1.1 (jetty)");
                if (inputStream != null) {
                    IO.copy(inputStream, httpServletResponse.getOutputStream());
                }
            }
        }
        str = requestURI;
        headerNames = httpServletRequest.getHeaderNames();
        i2 = 0;
        i3 = 0;
        while (headerNames.hasMoreElements()) {
            requestURI = (String) headerNames.nextElement();
            toLowerCase = requestURI.toLowerCase();
            if ("content-type".equals(toLowerCase)) {
            }
            headers = httpServletRequest.getHeaders(requestURI);
            while (headers.hasMoreElements()) {
                str2 = (String) headers.nextElement();
                if (str2 != null) {
                    openConnection.addRequestProperty(requestURI, str2);
                    i3 |= "X-Forwarded-For".equalsIgnoreCase(requestURI);
                }
            }
            i2 = i4;
        }
        openConnection.setRequestProperty("Via", "1.1 (jetty)");
        if (i3 == 0) {
            openConnection.addRequestProperty("X-Forwarded-For", httpServletRequest.getRemoteAddr());
            openConnection.addRequestProperty(HttpHeaders.X_FORWARDED_PROTO, httpServletRequest.getScheme());
            openConnection.addRequestProperty("X-Forwarded-Host", httpServletRequest.getServerName());
            openConnection.addRequestProperty("X-Forwarded-Server", httpServletRequest.getLocalName());
        }
        requestURI = httpServletRequest.getHeader("Cache-Control");
        openConnection.setUseCaches(false);
        try {
            openConnection.setDoInput(true);
            inputStream = httpServletRequest.getInputStream();
            if (i2 != 0) {
                openConnection.setDoOutput(true);
                IO.copy(inputStream, openConnection.getOutputStream());
            }
            openConnection.connect();
        } catch (Throwable e2) {
            this._context.log("proxy", e2);
        }
        if (httpURLConnection == null) {
            inputStream = null;
        } else {
            inputStream = httpURLConnection.getErrorStream();
            httpServletResponse.setStatus(httpURLConnection.getResponseCode(), httpURLConnection.getResponseMessage());
        }
        if (inputStream == null) {
            inputStream = openConnection.getInputStream();
        }
        httpServletResponse.setHeader("Date", null);
        httpServletResponse.setHeader("Server", null);
        headerFieldKey = openConnection.getHeaderFieldKey(0);
        str2 = openConnection.getHeaderField(0);
        while (true) {
            if (headerFieldKey != null) {
            }
            if (headerFieldKey == null) {
            }
            httpServletResponse.addHeader(headerFieldKey, str2);
            i++;
            headerFieldKey = openConnection.getHeaderFieldKey(i);
            str2 = openConnection.getHeaderField(i);
        }
        httpServletResponse.addHeader("Via", "1.1 (jetty)");
        if (inputStream != null) {
            IO.copy(inputStream, httpServletResponse.getOutputStream());
        }
    }
}
