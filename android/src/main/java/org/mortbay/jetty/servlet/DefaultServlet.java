package org.mortbay.jetty.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.io.Buffer;
import org.mortbay.io.ByteArrayBuffer;
import org.mortbay.io.WriterOutputStream;
import org.mortbay.io.nio.DirectNIOBuffer;
import org.mortbay.io.nio.IndirectNIOBuffer;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.HttpConnection.Output;
import org.mortbay.jetty.HttpContent;
import org.mortbay.jetty.HttpFields;
import org.mortbay.jetty.HttpHeaderValues;
import org.mortbay.jetty.HttpHeaders;
import org.mortbay.jetty.InclusiveByteRange;
import org.mortbay.jetty.MimeTypes;
import org.mortbay.jetty.ResourceCache;
import org.mortbay.jetty.ResourceCache.Content;
import org.mortbay.jetty.Response;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.handler.ContextHandler.SContext;
import org.mortbay.jetty.nio.NIOConnector;
import org.mortbay.log.Log;
import org.mortbay.resource.FileResource;
import org.mortbay.resource.Resource;
import org.mortbay.resource.ResourceFactory;
import org.mortbay.util.IO;
import org.mortbay.util.MultiPartOutputStream;
import org.mortbay.util.TypeUtil;
import org.mortbay.util.URIUtil;

public class DefaultServlet extends HttpServlet implements ResourceFactory {
    static Class class$org$mortbay$jetty$servlet$ServletHandler;
    private boolean _acceptRanges = true;
    private boolean _aliases = false;
    private ResourceCache _bioCache;
    ByteArrayBuffer _cacheControl;
    private SContext _context;
    private ServletHolder _defaultHolder;
    private boolean _dirAllowed = true;
    private boolean _gzip = true;
    private MimeTypes _mimeTypes;
    private NIOResourceCache _nioCache;
    private boolean _redirectWelcome = false;
    private Resource _resourceBase;
    private ServletHandler _servletHandler;
    private boolean _useFileMappedBuffer = false;
    private boolean _welcomeServlets = false;
    private String[] _welcomes;

    class NIOResourceCache extends ResourceCache {
        private final DefaultServlet this$0;

        public NIOResourceCache(DefaultServlet defaultServlet, MimeTypes mimeTypes) {
            this.this$0 = defaultServlet;
            super(mimeTypes);
        }

        protected void fill(Content content) throws IOException {
            Buffer directNIOBuffer;
            Resource resource = content.getResource();
            long length = resource.length();
            if (!DefaultServlet.access$100(this.this$0) || resource.getFile() == null) {
                InputStream inputStream = resource.getInputStream();
                try {
                    directNIOBuffer = ((NIOConnector) HttpConnection.getCurrentConnection().getConnector()).getUseDirectBuffers() ? new DirectNIOBuffer((int) length) : new IndirectNIOBuffer((int) length);
                } catch (Throwable e) {
                    Log.warn(e.toString());
                    Log.debug(e);
                    directNIOBuffer = new IndirectNIOBuffer((int) length);
                }
                directNIOBuffer.readFrom(inputStream, (int) length);
                inputStream.close();
            } else {
                directNIOBuffer = new DirectNIOBuffer(resource.getFile());
            }
            content.setBuffer(directNIOBuffer);
        }
    }

    private class UnCachedContent implements HttpContent {
        Resource _resource;
        private final DefaultServlet this$0;

        UnCachedContent(DefaultServlet defaultServlet, Resource resource) {
            this.this$0 = defaultServlet;
            this._resource = resource;
        }

        public Buffer getBuffer() {
            return null;
        }

        public long getContentLength() {
            return this._resource.length();
        }

        public Buffer getContentType() {
            return DefaultServlet.access$000(this.this$0).getMimeByExtension(this._resource.toString());
        }

        public InputStream getInputStream() throws IOException {
            return this._resource.getInputStream();
        }

        public Buffer getLastModified() {
            return null;
        }

        public Resource getResource() {
            return this._resource;
        }

        public void release() {
            this._resource.release();
            this._resource = null;
        }
    }

    static MimeTypes access$000(DefaultServlet defaultServlet) {
        return defaultServlet._mimeTypes;
    }

    static boolean access$100(DefaultServlet defaultServlet) {
        return defaultServlet._useFileMappedBuffer;
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    private boolean getInitBoolean(String str, boolean z) {
        String initParameter = getInitParameter(str);
        return (initParameter == null || initParameter.length() == 0) ? z : initParameter.startsWith("t") || initParameter.startsWith("T") || initParameter.startsWith("y") || initParameter.startsWith("Y") || initParameter.startsWith("1");
    }

    private int getInitInt(String str, int i) {
        String initParameter = getInitParameter(str);
        if (initParameter == null) {
            initParameter = getInitParameter(str);
        }
        return (initParameter == null || initParameter.length() <= 0) ? i : Integer.parseInt(initParameter);
    }

    private String getWelcomeFile(String str) throws MalformedURLException, IOException {
        String str2 = null;
        if (this._welcomes == null) {
            return null;
        }
        for (int i = 0; i < this._welcomes.length; i++) {
            String addPaths = URIUtil.addPaths(str, this._welcomes[i]);
            Resource resource = getResource(addPaths);
            if (resource != null && resource.exists()) {
                return this._welcomes[i];
            }
            if (this._welcomeServlets && str2 == null) {
                Entry holderEntry = this._servletHandler.getHolderEntry(addPaths);
                if (!(holderEntry == null || holderEntry.getValue() == this._defaultHolder)) {
                    str2 = addPaths;
                }
            }
        }
        return str2;
    }

    public void destroy() {
        try {
            if (this._nioCache != null) {
                this._nioCache.stop();
            }
            try {
                if (this._bioCache != null) {
                    this._bioCache.stop();
                }
            } catch (Throwable e) {
                Log.warn(Log.EXCEPTION, e);
            } catch (Throwable th) {
                super.destroy();
            }
        } catch (Throwable e2) {
            Log.warn(Log.EXCEPTION, e2);
            try {
                if (this._bioCache != null) {
                    this._bioCache.stop();
                }
            } catch (Throwable e22) {
                Log.warn(Log.EXCEPTION, e22);
            } catch (Throwable th2) {
                super.destroy();
            }
        } catch (Throwable th3) {
            try {
                if (this._bioCache != null) {
                    this._bioCache.stop();
                }
            } catch (Throwable e3) {
                Log.warn(Log.EXCEPTION, e3);
            } catch (Throwable th4) {
                super.destroy();
            }
            super.destroy();
        }
        super.destroy();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void doGet(javax.servlet.http.HttpServletRequest r14, javax.servlet.http.HttpServletResponse r15) throws javax.servlet.ServletException, java.io.IOException {
        /*
        r13 = this;
        r5 = 1;
        r7 = 0;
        r3 = 0;
        r0 = "org.mortbay.jetty.included";
        r0 = r14.getAttribute(r0);
        r0 = (java.lang.Boolean) r0;
        if (r0 == 0) goto L_0x00dd;
    L_0x000d:
        r1 = r0.booleanValue();
        if (r1 == 0) goto L_0x00dd;
    L_0x0013:
        r1 = "javax.servlet.include.servlet_path";
        r1 = r14.getAttribute(r1);
        r1 = (java.lang.String) r1;
        r2 = "javax.servlet.include.path_info";
        r2 = r14.getAttribute(r2);
        r2 = (java.lang.String) r2;
        if (r1 != 0) goto L_0x02f3;
    L_0x0025:
        r1 = r14.getServletPath();
        r2 = r14.getPathInfo();
        r6 = r3;
    L_0x002e:
        r9 = org.mortbay.util.URIUtil.addPaths(r1, r2);
        r1 = "/";
        r10 = r9.endsWith(r1);
        r1 = r0.booleanValue();
        if (r1 != 0) goto L_0x02f0;
    L_0x003e:
        r1 = r13._gzip;
        if (r1 == 0) goto L_0x02f0;
    L_0x0042:
        if (r6 != 0) goto L_0x02f0;
    L_0x0044:
        if (r10 != 0) goto L_0x02f0;
    L_0x0046:
        r1 = "Accept-Encoding";
        r1 = r14.getHeader(r1);
        if (r1 == 0) goto L_0x02f0;
    L_0x004e:
        r2 = "gzip";
        r1 = r1.indexOf(r2);
        if (r1 < 0) goto L_0x02f0;
    L_0x0056:
        r1 = r5;
    L_0x0057:
        r2 = org.mortbay.jetty.HttpConnection.getCurrentConnection();
        r2 = r2.getConnector();
        r2 = r2 instanceof org.mortbay.jetty.nio.NIOConnector;
        if (r2 == 0) goto L_0x00f8;
    L_0x0063:
        r2 = r13._nioCache;
    L_0x0065:
        if (r1 == 0) goto L_0x02ec;
    L_0x0067:
        r4 = new java.lang.StringBuffer;	 Catch:{ IllegalArgumentException -> 0x02ce, all -> 0x02be }
        r4.<init>();	 Catch:{ IllegalArgumentException -> 0x02ce, all -> 0x02be }
        r4 = r4.append(r9);	 Catch:{ IllegalArgumentException -> 0x02ce, all -> 0x02be }
        r8 = ".gz";
        r4 = r4.append(r8);	 Catch:{ IllegalArgumentException -> 0x02ce, all -> 0x02be }
        r8 = r4.toString();	 Catch:{ IllegalArgumentException -> 0x02ce, all -> 0x02be }
        r4 = r13.getResource(r8);	 Catch:{ IllegalArgumentException -> 0x02ce, all -> 0x02be }
        if (r4 == 0) goto L_0x008c;
    L_0x0080:
        r11 = r4.exists();	 Catch:{ IllegalArgumentException -> 0x02d3, all -> 0x02c2 }
        if (r11 == 0) goto L_0x008c;
    L_0x0086:
        r11 = r4.isDirectory();	 Catch:{ IllegalArgumentException -> 0x02d3, all -> 0x02c2 }
        if (r11 == 0) goto L_0x00fc;
    L_0x008c:
        r1 = r7;
    L_0x008d:
        if (r4 == 0) goto L_0x009b;
    L_0x008f:
        r8 = r4.exists();	 Catch:{ IllegalArgumentException -> 0x02d7, all -> 0x02c2 }
        if (r8 == 0) goto L_0x009b;
    L_0x0095:
        r8 = r4.isDirectory();	 Catch:{ IllegalArgumentException -> 0x02d7, all -> 0x02c2 }
        if (r8 == 0) goto L_0x02e9;
    L_0x009b:
        r8 = r7;
    L_0x009c:
        if (r8 != 0) goto L_0x02bb;
    L_0x009e:
        if (r2 != 0) goto L_0x0109;
    L_0x00a0:
        r4 = r13.getResource(r9);	 Catch:{ IllegalArgumentException -> 0x02d7, all -> 0x02c2 }
        r1 = r3;
    L_0x00a5:
        r2 = org.mortbay.log.Log.isDebugEnabled();	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        if (r2 == 0) goto L_0x00c9;
    L_0x00ab:
        r2 = new java.lang.StringBuffer;	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r2.<init>();	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r3 = "resource=";
        r2 = r2.append(r3);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r3 = r2.append(r4);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        if (r1 == 0) goto L_0x011b;
    L_0x00bc:
        r2 = " content";
    L_0x00be:
        r2 = r3.append(r2);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r2 = r2.toString();	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        org.mortbay.log.Log.debug(r2);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
    L_0x00c9:
        if (r4 == 0) goto L_0x00d1;
    L_0x00cb:
        r2 = r4.exists();	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        if (r2 != 0) goto L_0x011e;
    L_0x00d1:
        r0 = 404; // 0x194 float:5.66E-43 double:1.996E-321;
        r15.sendError(r0);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r0 = r1;
    L_0x00d7:
        if (r0 == 0) goto L_0x02b4;
    L_0x00d9:
        r0.release();
    L_0x00dc:
        return;
    L_0x00dd:
        r0 = java.lang.Boolean.FALSE;
        r1 = r14.getServletPath();
        r2 = r14.getPathInfo();
        r4 = "Range";
        r6 = r14.getHeaders(r4);
        if (r6 == 0) goto L_0x002e;
    L_0x00ef:
        r4 = r6.hasMoreElements();
        if (r4 != 0) goto L_0x002e;
    L_0x00f5:
        r6 = r3;
        goto L_0x002e;
    L_0x00f8:
        r2 = r13._bioCache;
        goto L_0x0065;
    L_0x00fc:
        if (r2 == 0) goto L_0x008d;
    L_0x00fe:
        r3 = r2.lookup(r8, r4);	 Catch:{ IllegalArgumentException -> 0x02d3, all -> 0x02c2 }
        if (r3 == 0) goto L_0x008d;
    L_0x0104:
        r4 = r3.getResource();	 Catch:{ IllegalArgumentException -> 0x02d7, all -> 0x02c2 }
        goto L_0x008d;
    L_0x0109:
        r3 = r2.lookup(r9, r13);	 Catch:{ IllegalArgumentException -> 0x02d7, all -> 0x02c2 }
        if (r3 == 0) goto L_0x0115;
    L_0x010f:
        r4 = r3.getResource();	 Catch:{ IllegalArgumentException -> 0x02d7, all -> 0x02c2 }
        r1 = r3;
        goto L_0x00a5;
    L_0x0115:
        r4 = r13.getResource(r9);	 Catch:{ IllegalArgumentException -> 0x02d7, all -> 0x02c2 }
        r1 = r3;
        goto L_0x00a5;
    L_0x011b:
        r2 = "";
        goto L_0x00be;
    L_0x011e:
        r2 = r4.isDirectory();	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        if (r2 != 0) goto L_0x01c7;
    L_0x0124:
        if (r10 == 0) goto L_0x0172;
    L_0x0126:
        r2 = r13._aliases;	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        if (r2 == 0) goto L_0x0172;
    L_0x012a:
        r2 = r9.length();	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        if (r2 <= r5) goto L_0x0172;
    L_0x0130:
        r2 = r14.getQueryString();	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r0 = 0;
        r3 = r9.length();	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r3 = r3 + -1;
        r0 = r9.substring(r0, r3);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        if (r2 == 0) goto L_0x015e;
    L_0x0141:
        r3 = r2.length();	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        if (r3 == 0) goto L_0x015e;
    L_0x0147:
        r3 = new java.lang.StringBuffer;	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r3.<init>();	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r0 = r3.append(r0);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r3 = "?";
        r0 = r0.append(r3);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r0 = r0.append(r2);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r0 = r0.toString();	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
    L_0x015e:
        r2 = r13._context;	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r2 = r2.getContextPath();	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r0 = org.mortbay.util.URIUtil.addPaths(r2, r0);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r0 = r15.encodeRedirectURL(r0);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r15.sendRedirect(r0);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r0 = r1;
        goto L_0x00d7;
    L_0x0172:
        if (r1 != 0) goto L_0x02b8;
    L_0x0174:
        r5 = new org.mortbay.jetty.servlet.DefaultServlet$UnCachedContent;	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r5.<init>(r13, r4);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
    L_0x0179:
        r1 = r0.booleanValue();	 Catch:{ IllegalArgumentException -> 0x02df, all -> 0x02c5 }
        if (r1 != 0) goto L_0x0185;
    L_0x017f:
        r1 = r13.passConditionalHeaders(r14, r15, r4, r5);	 Catch:{ IllegalArgumentException -> 0x02df, all -> 0x02c5 }
        if (r1 == 0) goto L_0x02e3;
    L_0x0185:
        if (r8 == 0) goto L_0x0199;
    L_0x0187:
        r1 = "Content-Encoding";
        r2 = "gzip";
        r15.setHeader(r1, r2);	 Catch:{ IllegalArgumentException -> 0x02df, all -> 0x02c5 }
        r1 = r13._context;	 Catch:{ IllegalArgumentException -> 0x02df, all -> 0x02c5 }
        r1 = r1.getMimeType(r9);	 Catch:{ IllegalArgumentException -> 0x02df, all -> 0x02c5 }
        if (r1 == 0) goto L_0x0199;
    L_0x0196:
        r15.setContentType(r1);	 Catch:{ IllegalArgumentException -> 0x02df, all -> 0x02c5 }
    L_0x0199:
        r3 = r0.booleanValue();	 Catch:{ IllegalArgumentException -> 0x02df, all -> 0x02c5 }
        r0 = r13;
        r1 = r14;
        r2 = r15;
        r0.sendData(r1, r2, r3, r4, r5, r6);	 Catch:{ IllegalArgumentException -> 0x02df, all -> 0x02c5 }
        r0 = r5;
        goto L_0x00d7;
    L_0x01a6:
        r0 = move-exception;
        r12 = r1;
        r1 = r0;
        r0 = r12;
    L_0x01aa:
        r2 = "EXCEPTION ";
        org.mortbay.log.Log.warn(r2, r1);	 Catch:{ all -> 0x02c9 }
        r2 = r15.isCommitted();	 Catch:{ all -> 0x02c9 }
        if (r2 != 0) goto L_0x01be;
    L_0x01b5:
        r2 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
        r1 = r1.getMessage();	 Catch:{ all -> 0x02c9 }
        r15.sendError(r2, r1);	 Catch:{ all -> 0x02c9 }
    L_0x01be:
        if (r0 != 0) goto L_0x00d9;
    L_0x01c0:
        if (r4 == 0) goto L_0x00dc;
    L_0x01c2:
        r4.release();
        goto L_0x00dc;
    L_0x01c7:
        if (r10 == 0) goto L_0x01d7;
    L_0x01c9:
        r2 = r9.length();	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        if (r2 != r5) goto L_0x021c;
    L_0x01cf:
        r2 = "org.mortbay.jetty.nullPathInfo";
        r2 = r14.getAttribute(r2);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        if (r2 == 0) goto L_0x021c;
    L_0x01d7:
        r0 = r14.getRequestURL();	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r2 = ";";
        r2 = r0.lastIndexOf(r2);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        if (r2 >= 0) goto L_0x020e;
    L_0x01e3:
        r2 = 47;
        r0.append(r2);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
    L_0x01e8:
        r2 = r14.getQueryString();	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        if (r2 == 0) goto L_0x01fc;
    L_0x01ee:
        r3 = r2.length();	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        if (r3 == 0) goto L_0x01fc;
    L_0x01f4:
        r3 = 63;
        r0.append(r3);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r0.append(r2);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
    L_0x01fc:
        r2 = 0;
        r15.setContentLength(r2);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r0 = r0.toString();	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r0 = r15.encodeRedirectURL(r0);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r15.sendRedirect(r0);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r0 = r1;
        goto L_0x00d7;
    L_0x020e:
        r3 = 47;
        r0.insert(r2, r3);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        goto L_0x01e8;
    L_0x0214:
        r0 = move-exception;
        r3 = r1;
    L_0x0216:
        if (r3 == 0) goto L_0x02ad;
    L_0x0218:
        r3.release();
    L_0x021b:
        throw r0;
    L_0x021c:
        r2 = r13.getWelcomeFile(r9);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        if (r2 == 0) goto L_0x028e;
    L_0x0222:
        r3 = r13._redirectWelcome;	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        if (r3 == 0) goto L_0x0273;
    L_0x0226:
        r0 = 0;
        r15.setContentLength(r0);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r0 = r14.getQueryString();	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        if (r0 == 0) goto L_0x0261;
    L_0x0230:
        r3 = r0.length();	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        if (r3 == 0) goto L_0x0261;
    L_0x0236:
        r3 = new java.lang.StringBuffer;	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r3.<init>();	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r5 = r13._context;	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r5 = r5.getContextPath();	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r2 = org.mortbay.util.URIUtil.addPaths(r5, r2);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r2 = r3.append(r2);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r3 = "?";
        r2 = r2.append(r3);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r0 = r2.append(r0);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r0 = r0.toString();	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r0 = r15.encodeRedirectURL(r0);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r15.sendRedirect(r0);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
    L_0x025e:
        r0 = r1;
        goto L_0x00d7;
    L_0x0261:
        r0 = r13._context;	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r0 = r0.getContextPath();	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r0 = org.mortbay.util.URIUtil.addPaths(r0, r2);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r0 = r15.encodeRedirectURL(r0);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r15.sendRedirect(r0);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        goto L_0x025e;
    L_0x0273:
        r3 = r14.getRequestDispatcher(r2);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        if (r3 == 0) goto L_0x0282;
    L_0x0279:
        r0 = r0.booleanValue();	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        if (r0 == 0) goto L_0x0285;
    L_0x027f:
        r3.include(r14, r15);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
    L_0x0282:
        r0 = r1;
        goto L_0x00d7;
    L_0x0285:
        r0 = "org.mortbay.jetty.welcome";
        r14.setAttribute(r0, r2);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r3.forward(r14, r15);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        goto L_0x0282;
    L_0x028e:
        r3 = new org.mortbay.jetty.servlet.DefaultServlet$UnCachedContent;	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r3.<init>(r13, r4);	 Catch:{ IllegalArgumentException -> 0x01a6, all -> 0x0214 }
        r0 = r0.booleanValue();	 Catch:{ IllegalArgumentException -> 0x02db, all -> 0x02c2 }
        if (r0 != 0) goto L_0x029f;
    L_0x0299:
        r0 = r13.passConditionalHeaders(r14, r15, r4, r3);	 Catch:{ IllegalArgumentException -> 0x02db, all -> 0x02c2 }
        if (r0 == 0) goto L_0x02e6;
    L_0x029f:
        r0 = r9.length();	 Catch:{ IllegalArgumentException -> 0x02db, all -> 0x02c2 }
        if (r0 <= r5) goto L_0x02ab;
    L_0x02a5:
        r13.sendDirectory(r14, r15, r4, r5);	 Catch:{ IllegalArgumentException -> 0x02db, all -> 0x02c2 }
        r0 = r3;
        goto L_0x00d7;
    L_0x02ab:
        r5 = r7;
        goto L_0x02a5;
    L_0x02ad:
        if (r4 == 0) goto L_0x021b;
    L_0x02af:
        r4.release();
        goto L_0x021b;
    L_0x02b4:
        if (r4 == 0) goto L_0x00dc;
    L_0x02b6:
        goto L_0x01c2;
    L_0x02b8:
        r5 = r1;
        goto L_0x0179;
    L_0x02bb:
        r1 = r3;
        goto L_0x00a5;
    L_0x02be:
        r0 = move-exception;
        r4 = r3;
        goto L_0x0216;
    L_0x02c2:
        r0 = move-exception;
        goto L_0x0216;
    L_0x02c5:
        r0 = move-exception;
        r3 = r5;
        goto L_0x0216;
    L_0x02c9:
        r1 = move-exception;
        r3 = r0;
        r0 = r1;
        goto L_0x0216;
    L_0x02ce:
        r1 = move-exception;
        r4 = r3;
        r0 = r3;
        goto L_0x01aa;
    L_0x02d3:
        r1 = move-exception;
        r0 = r3;
        goto L_0x01aa;
    L_0x02d7:
        r1 = move-exception;
        r0 = r3;
        goto L_0x01aa;
    L_0x02db:
        r1 = move-exception;
        r0 = r3;
        goto L_0x01aa;
    L_0x02df:
        r1 = move-exception;
        r0 = r5;
        goto L_0x01aa;
    L_0x02e3:
        r0 = r5;
        goto L_0x00d7;
    L_0x02e6:
        r0 = r3;
        goto L_0x00d7;
    L_0x02e9:
        r8 = r1;
        goto L_0x009c;
    L_0x02ec:
        r4 = r3;
        r8 = r1;
        goto L_0x009c;
    L_0x02f0:
        r1 = r7;
        goto L_0x0057;
    L_0x02f3:
        r6 = r3;
        goto L_0x002e;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.servlet.DefaultServlet.doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse):void");
    }

    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        doGet(httpServletRequest, httpServletResponse);
    }

    protected void doTrace(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.sendError(405);
    }

    public String getInitParameter(String str) {
        String initParameter = getServletContext().getInitParameter(new StringBuffer().append("org.mortbay.jetty.servlet.Default.").append(str).toString());
        return initParameter == null ? super.getInitParameter(str) : initParameter;
    }

    public Resource getResource(String str) {
        Throwable th;
        Resource resource = null;
        if (this._resourceBase == null) {
            return null;
        }
        try {
            Resource addPath = this._resourceBase.addPath(str);
            try {
                if (this._aliases || addPath.getAlias() == null) {
                    if (!Log.isDebugEnabled()) {
                        return addPath;
                    }
                    Log.debug(new StringBuffer().append("RESOURCE=").append(addPath).toString());
                    return addPath;
                } else if (!addPath.exists()) {
                    return null;
                } else {
                    Log.warn(new StringBuffer().append("Aliased resource: ").append(addPath).append("==").append(addPath.getAlias()).toString());
                    return null;
                }
            } catch (Throwable e) {
                Throwable th2 = e;
                resource = addPath;
                th = th2;
                Log.ignore(th);
                return resource;
            }
        } catch (IOException e2) {
            th = e2;
            Log.ignore(th);
            return resource;
        }
    }

    public void init() throws UnavailableException {
        ServletContext servletContext = getServletContext();
        this._context = (SContext) servletContext;
        this._mimeTypes = this._context.getContextHandler().getMimeTypes();
        this._welcomes = this._context.getContextHandler().getWelcomeFiles();
        if (this._welcomes == null) {
            this._welcomes = new String[]{"index.jsp", "index.html"};
        }
        this._acceptRanges = getInitBoolean("acceptRanges", this._acceptRanges);
        this._dirAllowed = getInitBoolean("dirAllowed", this._dirAllowed);
        this._welcomeServlets = getInitBoolean("welcomeServlets", this._welcomeServlets);
        this._redirectWelcome = getInitBoolean("redirectWelcome", this._redirectWelcome);
        this._gzip = getInitBoolean(HttpHeaderValues.GZIP, this._gzip);
        this._aliases = getInitBoolean("aliases", this._aliases);
        if (this._aliases || FileResource.getCheckAliases()) {
            if (this._aliases) {
                servletContext.log("Aliases are enabled");
            }
            this._useFileMappedBuffer = getInitBoolean("useFileMappedBuffer", this._useFileMappedBuffer);
            String initParameter = getInitParameter("relativeResourceBase");
            if (initParameter != null) {
                try {
                    Resource resource = this._context.getContextHandler().getResource(URIUtil.SLASH);
                    if (resource == null) {
                        throw new UnavailableException(new StringBuffer().append("No base resourceBase for relativeResourceBase in").append(this._context.getContextPath()).toString());
                    }
                    this._resourceBase = resource.addPath(initParameter);
                } catch (Throwable e) {
                    Log.warn(Log.EXCEPTION, e);
                    throw new UnavailableException(e.toString());
                }
            }
            String initParameter2 = getInitParameter("resourceBase");
            if (initParameter == null || initParameter2 == null) {
                if (initParameter2 != null) {
                    try {
                        this._resourceBase = Resource.newResource(initParameter2);
                    } catch (Throwable e2) {
                        Log.warn(Log.EXCEPTION, e2);
                        throw new UnavailableException(e2.toString());
                    }
                }
                initParameter = getInitParameter("cacheControl");
                if (initParameter != null) {
                    this._cacheControl = new ByteArrayBuffer(initParameter);
                }
                try {
                    Class class$;
                    if (this._resourceBase == null) {
                        this._resourceBase = this._context.getContextHandler().getResource(URIUtil.SLASH);
                    }
                    initParameter = getInitParameter("cacheType");
                    int initInt = getInitInt("maxCacheSize", -2);
                    int initInt2 = getInitInt("maxCachedFileSize", -2);
                    int initInt3 = getInitInt("maxCachedFiles", -2);
                    if ((initParameter == null || "nio".equals(initParameter) || "both".equals(initParameter)) && (initInt == -2 || initInt > 0)) {
                        this._nioCache = new NIOResourceCache(this, this._mimeTypes);
                        if (initInt > 0) {
                            this._nioCache.setMaxCacheSize(initInt);
                        }
                        if (initInt2 >= -1) {
                            this._nioCache.setMaxCachedFileSize(initInt2);
                        }
                        if (initInt3 >= -1) {
                            this._nioCache.setMaxCachedFiles(initInt3);
                        }
                        this._nioCache.start();
                    }
                    if (("bio".equals(initParameter) || "both".equals(initParameter)) && (initInt == -2 || initInt > 0)) {
                        this._bioCache = new ResourceCache(this._mimeTypes);
                        if (initInt > 0) {
                            this._bioCache.setMaxCacheSize(initInt);
                        }
                        if (initInt2 >= -1) {
                            this._bioCache.setMaxCachedFileSize(initInt2);
                        }
                        if (initInt3 >= -1) {
                            this._bioCache.setMaxCachedFiles(initInt3);
                        }
                        this._bioCache.start();
                    }
                    if (this._nioCache == null) {
                        this._bioCache = null;
                    }
                    ContextHandler contextHandler = this._context.getContextHandler();
                    if (class$org$mortbay$jetty$servlet$ServletHandler == null) {
                        class$ = class$("org.mortbay.jetty.servlet.ServletHandler");
                        class$org$mortbay$jetty$servlet$ServletHandler = class$;
                    } else {
                        class$ = class$org$mortbay$jetty$servlet$ServletHandler;
                    }
                    this._servletHandler = (ServletHandler) contextHandler.getChildHandlerByClass(class$);
                    ServletHolder[] servlets = this._servletHandler.getServlets();
                    int length = servlets.length;
                    while (true) {
                        initInt = length - 1;
                        if (length <= 0) {
                            break;
                        } else if (servlets[initInt].getServletInstance() == this) {
                            this._defaultHolder = servlets[initInt];
                            length = initInt;
                        } else {
                            length = initInt;
                        }
                    }
                    if (Log.isDebugEnabled()) {
                        Log.debug(new StringBuffer().append("resource base = ").append(this._resourceBase).toString());
                        return;
                    }
                    return;
                } catch (Throwable e22) {
                    Log.warn(Log.EXCEPTION, e22);
                    throw new UnavailableException(e22.toString());
                }
            }
            throw new UnavailableException("resourceBase & relativeResourceBase");
        }
        throw new IllegalStateException("Alias checking disabled");
    }

    protected boolean passConditionalHeaders(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Resource resource, HttpContent httpContent) throws IOException {
        try {
            if (!httpServletRequest.getMethod().equals("HEAD")) {
                long dateHeader;
                String header = httpServletRequest.getHeader("If-Modified-Since");
                if (header != null) {
                    if (httpContent != null) {
                        Buffer lastModified = httpContent.getLastModified();
                        if (lastModified != null && header.equals(lastModified.toString())) {
                            httpServletResponse.reset();
                            httpServletResponse.setStatus(304);
                            httpServletResponse.flushBuffer();
                            return false;
                        }
                    }
                    dateHeader = httpServletRequest.getDateHeader("If-Modified-Since");
                    if (dateHeader != -1 && resource.lastModified() / 1000 <= dateHeader / 1000) {
                        httpServletResponse.reset();
                        httpServletResponse.setStatus(304);
                        httpServletResponse.flushBuffer();
                        return false;
                    }
                }
                dateHeader = httpServletRequest.getDateHeader("If-Unmodified-Since");
                if (dateHeader != -1 && resource.lastModified() / 1000 > dateHeader / 1000) {
                    httpServletResponse.sendError(412);
                    return false;
                }
            }
            return true;
        } catch (IllegalArgumentException e) {
            if (!httpServletResponse.isCommitted()) {
                httpServletResponse.sendError(400, e.getMessage());
            }
            throw e;
        }
    }

    protected void sendData(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, boolean z, Resource resource, HttpContent httpContent, Enumeration enumeration) throws IOException {
        OutputStream outputStream;
        long length = httpContent == null ? resource.length() : httpContent.getContentLength();
        try {
            outputStream = httpServletResponse.getOutputStream();
        } catch (IllegalStateException e) {
            outputStream = new WriterOutputStream(httpServletResponse.getWriter());
        }
        if (enumeration != null && enumeration.hasMoreElements() && length >= 0) {
            List satisfiableRanges = InclusiveByteRange.satisfiableRanges(enumeration, length);
            if (satisfiableRanges == null || satisfiableRanges.size() == 0) {
                writeHeaders(httpServletResponse, httpContent, length);
                httpServletResponse.setStatus(416);
                httpServletResponse.setHeader("Content-Range", InclusiveByteRange.to416HeaderRangeString(length));
                resource.writeTo(outputStream, 0, length);
            } else if (satisfiableRanges.size() == 1) {
                r4 = (InclusiveByteRange) satisfiableRanges.get(0);
                long size = r4.getSize(length);
                writeHeaders(httpServletResponse, httpContent, size);
                httpServletResponse.setStatus(206);
                httpServletResponse.setHeader("Content-Range", r4.toHeaderRangeString(length));
                resource.writeTo(outputStream, r4.getFirst(length), size);
            } else {
                writeHeaders(httpServletResponse, httpContent, -1);
                String obj = httpContent.getContentType().toString();
                OutputStream multiPartOutputStream = new MultiPartOutputStream(outputStream);
                httpServletResponse.setStatus(206);
                httpServletResponse.setContentType(new StringBuffer().append(httpServletRequest.getHeader(HttpHeaders.REQUEST_RANGE) != null ? "multipart/x-byteranges; boundary=" : "multipart/byteranges; boundary=").append(multiPartOutputStream.getBoundary()).toString());
                InputStream inputStream = resource.getInputStream();
                String[] strArr = new String[satisfiableRanges.size()];
                int i = 0;
                int i2 = 0;
                while (i < satisfiableRanges.size()) {
                    r4 = (InclusiveByteRange) satisfiableRanges.get(i);
                    strArr[i] = r4.toHeaderRangeString(length);
                    long length2 = (long) (((((((((((((i > 0 ? 2 : 0) + 2) + multiPartOutputStream.getBoundary().length()) + 2) + "Content-Type".length()) + 2) + obj.length()) + 2) + "Content-Range".length()) + 2) + strArr[i].length()) + 2) + 2);
                    i++;
                    i2 = (int) ((((r4.getLast(length) - r4.getFirst(length)) + length2) + 1) + ((long) i2));
                }
                httpServletResponse.setContentLength((((multiPartOutputStream.getBoundary().length() + 4) + 2) + 2) + i2);
                int i3 = 0;
                InputStream inputStream2 = inputStream;
                long j = 0;
                while (i3 < satisfiableRanges.size()) {
                    long j2;
                    r4 = (InclusiveByteRange) satisfiableRanges.get(i3);
                    multiPartOutputStream.startPart(obj, new String[]{new StringBuffer().append("Content-Range: ").append(strArr[i3]).toString()});
                    long first = r4.getFirst(length);
                    long size2 = r4.getSize(length);
                    if (inputStream2 != null) {
                        if (first < j) {
                            inputStream2.close();
                            inputStream = resource.getInputStream();
                            j2 = 0;
                        } else {
                            inputStream = inputStream2;
                            j2 = j;
                        }
                        if (j2 < first) {
                            inputStream.skip(first - j2);
                        } else {
                            first = j2;
                        }
                        IO.copy(inputStream, multiPartOutputStream, size2);
                        j2 = first + size2;
                    } else {
                        resource.writeTo(multiPartOutputStream, first, size2);
                        inputStream = inputStream2;
                        j2 = j;
                    }
                    i3++;
                    inputStream2 = inputStream;
                    j = j2;
                }
                if (inputStream2 != null) {
                    inputStream2.close();
                }
                multiPartOutputStream.close();
            }
        } else if (z) {
            resource.writeTo(outputStream, 0, length);
        } else if (!(outputStream instanceof Output)) {
            writeHeaders(httpServletResponse, httpContent, length);
            resource.writeTo(outputStream, 0, length);
        } else if (httpServletResponse instanceof Response) {
            writeOptionHeaders(((Response) httpServletResponse).getHttpFields());
            ((Output) outputStream).sendContent(httpContent);
        } else if (httpContent.getBuffer() != null) {
            writeHeaders(httpServletResponse, httpContent, length);
            ((Output) outputStream).sendContent(httpContent.getBuffer());
        } else {
            writeHeaders(httpServletResponse, httpContent, length);
            resource.writeTo(outputStream, 0, length);
        }
    }

    protected void sendDirectory(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Resource resource, boolean z) throws IOException {
        if (this._dirAllowed) {
            String listHTML = resource.getListHTML(URIUtil.addPaths(httpServletRequest.getRequestURI(), URIUtil.SLASH), z);
            if (listHTML == null) {
                httpServletResponse.sendError(403, "No directory");
                return;
            }
            byte[] bytes = listHTML.getBytes("UTF-8");
            httpServletResponse.setContentType("text/html; charset=UTF-8");
            httpServletResponse.setContentLength(bytes.length);
            httpServletResponse.getOutputStream().write(bytes);
            return;
        }
        httpServletResponse.sendError(403);
    }

    protected void writeHeaders(HttpServletResponse httpServletResponse, HttpContent httpContent, long j) throws IOException {
        if (httpContent.getContentType() != null && httpServletResponse.getContentType() == null) {
            httpServletResponse.setContentType(httpContent.getContentType().toString());
        }
        if (httpServletResponse instanceof Response) {
            Response response = (Response) httpServletResponse;
            HttpFields httpFields = response.getHttpFields();
            if (httpContent.getLastModified() != null) {
                httpFields.put(HttpHeaders.LAST_MODIFIED_BUFFER, httpContent.getLastModified(), httpContent.getResource().lastModified());
            } else if (httpContent.getResource() != null) {
                long lastModified = httpContent.getResource().lastModified();
                if (lastModified != -1) {
                    httpFields.putDateField(HttpHeaders.LAST_MODIFIED_BUFFER, lastModified);
                }
            }
            if (j != -1) {
                response.setLongContentLength(j);
            }
            writeOptionHeaders(httpFields);
            return;
        }
        long lastModified2 = httpContent.getResource().lastModified();
        if (lastModified2 >= 0) {
            httpServletResponse.setDateHeader("Last-Modified", lastModified2);
        }
        if (j != -1) {
            if (j < 2147483647L) {
                httpServletResponse.setContentLength((int) j);
            } else {
                httpServletResponse.setHeader("Content-Length", TypeUtil.toString(j));
            }
        }
        writeOptionHeaders(httpServletResponse);
    }

    protected void writeOptionHeaders(HttpServletResponse httpServletResponse) throws IOException {
        if (this._acceptRanges) {
            httpServletResponse.setHeader("Accept-Ranges", HttpHeaderValues.BYTES);
        }
        if (this._cacheControl != null) {
            httpServletResponse.setHeader("Cache-Control", this._cacheControl.toString());
        }
    }

    protected void writeOptionHeaders(HttpFields httpFields) throws IOException {
        if (this._acceptRanges) {
            httpFields.put(HttpHeaders.ACCEPT_RANGES_BUFFER, HttpHeaderValues.BYTES_BUFFER);
        }
        if (this._cacheControl != null) {
            httpFields.put(HttpHeaders.CACHE_CONTROL_BUFFER, this._cacheControl);
        }
    }
}
