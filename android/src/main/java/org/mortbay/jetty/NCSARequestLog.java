package org.mortbay.jetty;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;
import javax.servlet.http.Cookie;
import org.mortbay.component.AbstractLifeCycle;
import org.mortbay.jetty.servlet.PathMap;
import org.mortbay.log.Log;
import org.mortbay.util.DateCache;
import org.mortbay.util.RolloverFileOutputStream;
import org.mortbay.util.StringUtil;
import org.mortbay.util.TypeUtil;
import org.mortbay.util.Utf8StringBuffer;

public class NCSARequestLog extends AbstractLifeCycle implements RequestLog {
    private boolean _append = true;
    private transient ArrayList _buffers;
    private boolean _closeOut;
    private transient char[] _copy;
    private boolean _extended = true;
    private transient OutputStream _fileOut;
    private String _filename;
    private String _filenameDateFormat = null;
    private transient PathMap _ignorePathMap;
    private String[] _ignorePaths;
    private boolean _logCookies = false;
    private transient DateCache _logDateCache;
    private String _logDateFormat = "dd/MMM/yyyy:HH:mm:ss Z";
    private boolean _logLatency = false;
    private Locale _logLocale = Locale.getDefault();
    private boolean _logServer = false;
    private String _logTimeZone = "GMT";
    private transient OutputStream _out;
    private boolean _preferProxiedForAddress;
    private int _retainDays = 31;
    private transient Writer _writer;

    public NCSARequestLog(String str) {
        setFilename(str);
    }

    protected void doStart() throws Exception {
        if (this._logDateFormat != null) {
            this._logDateCache = new DateCache(this._logDateFormat, this._logLocale);
            this._logDateCache.setTimeZoneID(this._logTimeZone);
        }
        if (this._filename != null) {
            this._fileOut = new RolloverFileOutputStream(this._filename, this._append, this._retainDays, TimeZone.getTimeZone(this._logTimeZone), this._filenameDateFormat, null);
            this._closeOut = true;
            Log.info(new StringBuffer().append("Opened ").append(getDatedFilename()).toString());
        } else {
            this._fileOut = System.err;
        }
        this._out = this._fileOut;
        if (this._ignorePaths == null || this._ignorePaths.length <= 0) {
            this._ignorePathMap = null;
        } else {
            this._ignorePathMap = new PathMap();
            for (int i = 0; i < this._ignorePaths.length; i++) {
                this._ignorePathMap.put(this._ignorePaths[i], this._ignorePaths[i]);
            }
        }
        this._writer = new OutputStreamWriter(this._out);
        this._buffers = new ArrayList();
        this._copy = new char[1024];
        super.doStart();
    }

    protected void doStop() throws Exception {
        super.doStop();
        try {
            if (this._writer != null) {
                this._writer.flush();
            }
        } catch (Throwable e) {
            Log.ignore(e);
        }
        if (this._out != null && this._closeOut) {
            try {
                this._out.close();
            } catch (Throwable e2) {
                Log.ignore(e2);
            }
        }
        this._out = null;
        this._fileOut = null;
        this._closeOut = false;
        this._logDateCache = null;
        this._writer = null;
        this._buffers = null;
        this._copy = null;
    }

    public String getDatedFilename() {
        return this._fileOut instanceof RolloverFileOutputStream ? ((RolloverFileOutputStream) this._fileOut).getDatedFilename() : null;
    }

    public String getFilename() {
        return this._filename;
    }

    public String getFilenameDateFormat() {
        return this._filenameDateFormat;
    }

    public String[] getIgnorePaths() {
        return this._ignorePaths;
    }

    public boolean getLogCookies() {
        return this._logCookies;
    }

    public String getLogDateFormat() {
        return this._logDateFormat;
    }

    public boolean getLogLatency() {
        return this._logLatency;
    }

    public Locale getLogLocale() {
        return this._logLocale;
    }

    public boolean getLogServer() {
        return this._logServer;
    }

    public String getLogTimeZone() {
        return this._logTimeZone;
    }

    public int getRetainDays() {
        return this._retainDays;
    }

    public boolean isAppend() {
        return this._append;
    }

    public boolean isExtended() {
        return this._extended;
    }

    public void log(Request request, Response response) {
        if (isStarted()) {
            try {
                if ((this._ignorePathMap == null || this._ignorePathMap.getMatch(request.getRequestURI()) == null) && this._fileOut != null) {
                    int size;
                    Utf8StringBuffer utf8StringBuffer;
                    StringBuffer stringBuffer;
                    synchronized (this._writer) {
                        size = this._buffers.size();
                        utf8StringBuffer = size == 0 ? new Utf8StringBuffer(160) : (Utf8StringBuffer) this._buffers.remove(size - 1);
                        stringBuffer = utf8StringBuffer.getStringBuffer();
                    }
                    synchronized (stringBuffer) {
                        if (this._logServer) {
                            stringBuffer.append(request.getServerName());
                            stringBuffer.append(' ');
                        }
                        String str = null;
                        if (this._preferProxiedForAddress) {
                            str = request.getHeader("X-Forwarded-For");
                        }
                        if (str == null) {
                            str = request.getRemoteAddr();
                        }
                        stringBuffer.append(str);
                        stringBuffer.append(" - ");
                        str = request.getRemoteUser();
                        if (str == null) {
                            str = " - ";
                        }
                        stringBuffer.append(str);
                        stringBuffer.append(" [");
                        if (this._logDateCache != null) {
                            stringBuffer.append(this._logDateCache.format(request.getTimeStamp()));
                        } else {
                            stringBuffer.append(request.getTimeStampBuffer().toString());
                        }
                        stringBuffer.append("] \"");
                        stringBuffer.append(request.getMethod());
                        stringBuffer.append(' ');
                        request.getUri().writeTo(utf8StringBuffer);
                        stringBuffer.append(' ');
                        stringBuffer.append(request.getProtocol());
                        stringBuffer.append("\" ");
                        size = response.getStatus();
                        if (size <= 0) {
                            size = 404;
                        }
                        stringBuffer.append((char) (((size / 100) % 10) + 48));
                        stringBuffer.append((char) (((size / 10) % 10) + 48));
                        stringBuffer.append((char) ((size % 10) + 48));
                        long contentCount = response.getContentCount();
                        if (contentCount >= 0) {
                            stringBuffer.append(' ');
                            if (contentCount > 99999) {
                                stringBuffer.append(Long.toString(contentCount));
                            } else {
                                if (contentCount > 9999) {
                                    stringBuffer.append((char) ((int) (48 + ((contentCount / 10000) % 10))));
                                }
                                if (contentCount > 999) {
                                    stringBuffer.append((char) ((int) (48 + ((contentCount / 1000) % 10))));
                                }
                                if (contentCount > 99) {
                                    stringBuffer.append((char) ((int) (48 + ((contentCount / 100) % 10))));
                                }
                                if (contentCount > 9) {
                                    stringBuffer.append((char) ((int) (48 + ((contentCount / 10) % 10))));
                                }
                                stringBuffer.append((char) ((int) ((contentCount % 10) + 48)));
                            }
                            stringBuffer.append(' ');
                        } else {
                            stringBuffer.append(" - ");
                        }
                    }
                    if (this._extended || this._logCookies || this._logLatency) {
                        synchronized (this._writer) {
                            size = stringBuffer.length();
                            if (size > this._copy.length) {
                                size = this._copy.length;
                            }
                            stringBuffer.getChars(0, size, this._copy, 0);
                            this._writer.write(this._copy, 0, size);
                            utf8StringBuffer.reset();
                            this._buffers.add(utf8StringBuffer);
                            if (this._extended) {
                                logExtended(request, response, this._writer);
                            }
                            if (this._logCookies) {
                                Cookie[] cookies = request.getCookies();
                                if (cookies == null || cookies.length == 0) {
                                    this._writer.write(" -");
                                } else {
                                    this._writer.write(" \"");
                                    for (size = 0; size < cookies.length; size++) {
                                        if (size != 0) {
                                            this._writer.write(59);
                                        }
                                        this._writer.write(cookies[size].getName());
                                        this._writer.write(61);
                                        this._writer.write(cookies[size].getValue());
                                    }
                                    this._writer.write(34);
                                }
                            }
                            if (this._logLatency) {
                                this._writer.write(32);
                                this._writer.write(TypeUtil.toString(System.currentTimeMillis() - request.getTimeStamp()));
                            }
                            this._writer.write(StringUtil.__LINE_SEPARATOR);
                            this._writer.flush();
                        }
                        return;
                    }
                    synchronized (this._writer) {
                        stringBuffer.append(StringUtil.__LINE_SEPARATOR);
                        size = stringBuffer.length();
                        if (size > this._copy.length) {
                            size = this._copy.length;
                        }
                        stringBuffer.getChars(0, size, this._copy, 0);
                        this._writer.write(this._copy, 0, size);
                        this._writer.flush();
                        utf8StringBuffer.reset();
                        this._buffers.add(utf8StringBuffer);
                    }
                }
            } catch (Throwable e) {
                Log.warn(e);
            }
        }
    }

    protected void logExtended(Request request, Response response, Writer writer) throws IOException {
        String header = request.getHeader("Referer");
        if (header == null) {
            writer.write("\"-\" ");
        } else {
            writer.write(34);
            writer.write(header);
            writer.write("\" ");
        }
        header = request.getHeader("User-Agent");
        if (header == null) {
            writer.write("\"-\" ");
            return;
        }
        writer.write(34);
        writer.write(header);
        writer.write(34);
    }

    public void setAppend(boolean z) {
        this._append = z;
    }

    public void setExtended(boolean z) {
        this._extended = z;
    }

    public void setFilename(String str) {
        if (str != null) {
            str = str.trim();
            if (str.length() == 0) {
                str = null;
            }
        }
        this._filename = str;
    }

    public void setFilenameDateFormat(String str) {
        this._filenameDateFormat = str;
    }

    public void setIgnorePaths(String[] strArr) {
        this._ignorePaths = strArr;
    }

    public void setLogCookies(boolean z) {
        this._logCookies = z;
    }

    public void setLogDateFormat(String str) {
        this._logDateFormat = str;
    }

    public void setLogLatency(boolean z) {
        this._logLatency = z;
    }

    public void setLogLocale(Locale locale) {
        this._logLocale = locale;
    }

    public void setLogServer(boolean z) {
        this._logServer = z;
    }

    public void setLogTimeZone(String str) {
        this._logTimeZone = str;
    }

    public void setPreferProxiedForAddress(boolean z) {
        this._preferProxiedForAddress = z;
    }

    public void setRetainDays(int i) {
        this._retainDays = i;
    }
}
