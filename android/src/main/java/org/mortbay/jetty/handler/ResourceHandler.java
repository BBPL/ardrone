package org.mortbay.jetty.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.io.ByteArrayBuffer;
import org.mortbay.io.WriterOutputStream;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.HttpConnection.Output;
import org.mortbay.jetty.HttpFields;
import org.mortbay.jetty.HttpHeaders;
import org.mortbay.jetty.MimeTypes;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Response;
import org.mortbay.jetty.handler.ContextHandler.SContext;
import org.mortbay.log.Log;
import org.mortbay.resource.FileResource;
import org.mortbay.resource.Resource;
import org.mortbay.util.TypeUtil;
import org.mortbay.util.URIUtil;

public class ResourceHandler extends AbstractHandler {
    boolean _aliases;
    Resource _baseResource;
    ByteArrayBuffer _cacheControl;
    ContextHandler _context;
    MimeTypes _mimeTypes = new MimeTypes();
    String[] _welcomeFiles = new String[]{"index.html"};

    protected void doResponseHeaders(HttpServletResponse httpServletResponse, Resource resource, String str) {
        if (str != null) {
            httpServletResponse.setContentType(str);
        }
        long length = resource.length();
        if (httpServletResponse instanceof Response) {
            HttpFields httpFields = ((Response) httpServletResponse).getHttpFields();
            if (length > 0) {
                httpFields.putLongField(HttpHeaders.CONTENT_LENGTH_BUFFER, length);
            }
            if (this._cacheControl != null) {
                httpFields.put(HttpHeaders.CACHE_CONTROL_BUFFER, this._cacheControl);
                return;
            }
            return;
        }
        if (length > 0) {
            httpServletResponse.setHeader("Content-Length", TypeUtil.toString(length));
        }
        if (this._cacheControl != null) {
            httpServletResponse.setHeader("Cache-Control", this._cacheControl.toString());
        }
    }

    public void doStart() throws Exception {
        SContext currentContext = ContextHandler.getCurrentContext();
        this._context = currentContext == null ? null : currentContext.getContextHandler();
        if (this._aliases || FileResource.getCheckAliases()) {
            super.doStart();
            return;
        }
        throw new IllegalStateException("Alias checking disabled");
    }

    public Resource getBaseResource() {
        return this._baseResource == null ? null : this._baseResource;
    }

    public String getCacheControl() {
        return this._cacheControl.toString();
    }

    public MimeTypes getMimeTypes() {
        return this._mimeTypes;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.mortbay.resource.Resource getResource(java.lang.String r4) throws java.net.MalformedURLException {
        /*
        r3 = this;
        r0 = 0;
        if (r4 == 0) goto L_0x000b;
    L_0x0003:
        r1 = "/";
        r1 = r4.startsWith(r1);
        if (r1 != 0) goto L_0x0011;
    L_0x000b:
        r0 = new java.net.MalformedURLException;
        r0.<init>(r4);
        throw r0;
    L_0x0011:
        r1 = r3._baseResource;
        if (r1 != 0) goto L_0x0022;
    L_0x0015:
        r1 = r3._context;
        if (r1 != 0) goto L_0x001a;
    L_0x0019:
        return r0;
    L_0x001a:
        r1 = r3._context;
        r1 = r1.getBaseResource();
        if (r1 == 0) goto L_0x0019;
    L_0x0022:
        r2 = org.mortbay.util.URIUtil.canonicalPath(r4);	 Catch:{ Exception -> 0x002b }
        r0 = r1.addPath(r2);	 Catch:{ Exception -> 0x002b }
        goto L_0x0019;
    L_0x002b:
        r1 = move-exception;
        org.mortbay.log.Log.ignore(r1);
        goto L_0x0019;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.handler.ResourceHandler.getResource(java.lang.String):org.mortbay.resource.Resource");
    }

    protected Resource getResource(HttpServletRequest httpServletRequest) throws MalformedURLException {
        String pathInfo = httpServletRequest.getPathInfo();
        return pathInfo == null ? null : getResource(pathInfo);
    }

    public String getResourceBase() {
        return this._baseResource == null ? null : this._baseResource.toString();
    }

    protected Resource getWelcome(Resource resource) throws MalformedURLException, IOException {
        for (String addPath : this._welcomeFiles) {
            Resource addPath2 = resource.addPath(addPath);
            if (addPath2.exists() && !addPath2.isDirectory()) {
                return addPath2;
            }
        }
        return null;
    }

    public String[] getWelcomeFiles() {
        return this._welcomeFiles;
    }

    public void handle(String str, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException, ServletException {
        Request request = httpServletRequest instanceof Request ? (Request) httpServletRequest : HttpConnection.getCurrentConnection().getRequest();
        if (!request.isHandled()) {
            Object obj = null;
            if (!"GET".equals(httpServletRequest.getMethod())) {
                if ("HEAD".equals(httpServletRequest.getMethod())) {
                    obj = 1;
                } else {
                    return;
                }
            }
            Resource resource = getResource(httpServletRequest);
            if (resource != null && resource.exists()) {
                if (this._aliases || resource.getAlias() == null) {
                    Resource resource2;
                    request.setHandled(true);
                    if (!resource.isDirectory()) {
                        resource2 = resource;
                    } else if (httpServletRequest.getPathInfo().endsWith(URIUtil.SLASH)) {
                        resource2 = getWelcome(resource);
                        if (resource2 == null || !resource2.exists() || resource2.isDirectory()) {
                            httpServletResponse.sendError(403);
                            return;
                        }
                    } else {
                        httpServletResponse.sendRedirect(httpServletResponse.encodeRedirectURL(URIUtil.addPaths(httpServletRequest.getRequestURI(), URIUtil.SLASH)));
                        return;
                    }
                    long lastModified = resource2.lastModified();
                    if (lastModified > 0) {
                        long dateHeader = httpServletRequest.getDateHeader("If-Modified-Since");
                        if (dateHeader > 0 && lastModified / 1000 <= dateHeader / 1000) {
                            httpServletResponse.setStatus(304);
                            return;
                        }
                    }
                    Object mimeByExtension = this._mimeTypes.getMimeByExtension(resource2.toString());
                    if (mimeByExtension == null) {
                        mimeByExtension = this._mimeTypes.getMimeByExtension(httpServletRequest.getPathInfo());
                    }
                    doResponseHeaders(httpServletResponse, resource2, mimeByExtension != null ? mimeByExtension.toString() : null);
                    httpServletResponse.setDateHeader("Last-Modified", lastModified);
                    if (obj == null) {
                        OutputStream outputStream;
                        try {
                            outputStream = httpServletResponse.getOutputStream();
                        } catch (IllegalStateException e) {
                            outputStream = new WriterOutputStream(httpServletResponse.getWriter());
                        }
                        if (outputStream instanceof Output) {
                            ((Output) outputStream).sendContent(resource2.getInputStream());
                            return;
                        } else {
                            resource2.writeTo(outputStream, 0, resource2.length());
                            return;
                        }
                    }
                    return;
                }
                Log.info(new StringBuffer().append(resource).append(" aliased to ").append(resource.getAlias()).toString());
            }
        }
    }

    public boolean isAliases() {
        return this._aliases;
    }

    public void setAliases(boolean z) {
        this._aliases = z;
    }

    public void setBaseResource(Resource resource) {
        this._baseResource = resource;
    }

    public void setCacheControl(String str) {
        this._cacheControl = str == null ? null : new ByteArrayBuffer(str);
    }

    public void setMimeTypes(MimeTypes mimeTypes) {
        this._mimeTypes = mimeTypes;
    }

    public void setResourceBase(String str) {
        try {
            setBaseResource(Resource.newResource(str));
        } catch (Throwable e) {
            Log.warn(e.toString());
            Log.debug(e);
            throw new IllegalArgumentException(str);
        }
    }

    public void setWelcomeFiles(String[] strArr) {
        this._welcomeFiles = strArr;
    }
}
