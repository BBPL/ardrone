package org.mortbay.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.mortbay.jetty.HttpVersions;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.util.LazyList;
import org.mortbay.util.MultiMap;
import org.mortbay.util.StringUtil;
import org.mortbay.util.TypeUtil;

public class MultiPartFilter implements Filter {
    private static final String FILES = "org.mortbay.servlet.MultiPartFilter.files";
    private ServletContext _context;
    private boolean _deleteFiles;
    private int _fileOutputBuffer = 0;
    private File tempdir;

    private static class Wrapper extends HttpServletRequestWrapper {
        String encoding = "UTF-8";
        MultiMap map;

        public Wrapper(HttpServletRequest httpServletRequest, MultiMap multiMap) {
            super(httpServletRequest);
            this.map = multiMap;
        }

        public int getContentLength() {
            return 0;
        }

        public String getParameter(String str) {
            Object obj = this.map.get(str);
            if (!(obj instanceof byte[]) && LazyList.size(obj) > 0) {
                obj = LazyList.get(obj, 0);
            }
            if (obj instanceof byte[]) {
                try {
                    return new String((byte[]) obj, this.encoding);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (obj != null) {
                    return String.valueOf(obj);
                }
                return null;
            }
        }

        public Map getParameterMap() {
            return Collections.unmodifiableMap(this.map.toStringArrayMap());
        }

        public Enumeration getParameterNames() {
            return Collections.enumeration(this.map.keySet());
        }

        public String[] getParameterValues(String str) {
            List values = this.map.getValues(str);
            if (values == null || values.size() == 0) {
                return new String[0];
            }
            String[] strArr = new String[values.size()];
            for (int i = 0; i < values.size(); i++) {
                Object obj = values.get(i);
                if (obj instanceof byte[]) {
                    try {
                        strArr[i] = new String((byte[]) obj, this.encoding);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (obj instanceof String) {
                    strArr[i] = (String) obj;
                }
            }
            return strArr;
        }

        public void setCharacterEncoding(String str) throws UnsupportedEncodingException {
            this.encoding = str;
        }
    }

    private void deleteFiles(ServletRequest servletRequest) {
        ArrayList arrayList = (ArrayList) servletRequest.getAttribute(FILES);
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                File file = (File) it.next();
                try {
                    file.delete();
                } catch (Throwable e) {
                    this._context.log(new StringBuffer().append("failed to delete ").append(file).toString(), e);
                }
            }
        }
    }

    private String value(String str) {
        String trim = str.substring(str.indexOf(61) + 1).trim();
        int indexOf = trim.indexOf(59);
        if (indexOf > 0) {
            trim = trim.substring(0, indexOf);
        }
        if (trim.startsWith("\"")) {
            return trim.substring(1, trim.indexOf(34, 1));
        }
        indexOf = trim.indexOf(32);
        return indexOf > 0 ? trim.substring(0, indexOf) : trim;
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        OutputStream fileOutputStream;
        Throwable th;
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        if (httpServletRequest.getContentType() == null || !httpServletRequest.getContentType().startsWith("multipart/form-data")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        InputStream bufferedInputStream = new BufferedInputStream(servletRequest.getInputStream());
        String contentType = httpServletRequest.getContentType();
        String stringBuffer = new StringBuffer().append("--").append(value(contentType.substring(contentType.indexOf("boundary=")))).toString();
        byte[] bytes = new StringBuffer().append(stringBuffer).append("--").toString().getBytes(StringUtil.__ISO_8859_1);
        MultiMap multiMap = new MultiMap();
        for (Entry entry : servletRequest.getParameterMap().entrySet()) {
            Object value = entry.getValue();
            if (value instanceof String[]) {
                multiMap.addValues(entry.getKey(), (String[]) value);
            } else {
                multiMap.add(entry.getKey(), value);
            }
        }
        byte[] readLine = TypeUtil.readLine(bufferedInputStream);
        contentType = readLine == null ? null : new String(readLine, "UTF-8");
        if (contentType == null || !contentType.equals(stringBuffer)) {
            throw new IOException("Missing initial multi part boundary");
        }
        Object obj = null;
        contentType = null;
        while (obj == null) {
            String str = contentType;
            while (true) {
                byte[] readLine2 = TypeUtil.readLine(bufferedInputStream);
                if (readLine2 == null) {
                    break;
                } else if (readLine2.length == 0) {
                    break;
                } else {
                    String str2 = new String(readLine2, "UTF-8");
                    int indexOf = str2.indexOf(58, 0);
                    if (indexOf > 0) {
                        stringBuffer = str2.substring(0, indexOf).trim().toLowerCase();
                        contentType = str2.substring(indexOf + 1, str2.length()).trim();
                        if (stringBuffer.equals("content-disposition")) {
                            str = contentType;
                        }
                    }
                }
            }
            if (str == null) {
                throw new IOException("Missing content-disposition");
            }
            OutputStream outputStream;
            StringTokenizer stringTokenizer = new StringTokenizer(str, ";");
            String str3 = null;
            String str4 = null;
            value = null;
            while (stringTokenizer.hasMoreTokens()) {
                contentType = stringTokenizer.nextToken().trim();
                stringBuffer = contentType.toLowerCase();
                if (contentType.startsWith("form-data")) {
                    value = 1;
                } else if (stringBuffer.startsWith("name=")) {
                    str3 = value(contentType);
                } else if (stringBuffer.startsWith("filename=")) {
                    str4 = value(contentType);
                }
            }
            if (value == null || str3 == null) {
                contentType = str;
            } else {
                File file;
                int i;
                Object obj2;
                Object obj3;
                int i2;
                Object obj4;
                byte read;
                if (str4 != null) {
                    try {
                        if (str4.length() > 0) {
                            File createTempFile = File.createTempFile("MultiPart", HttpVersions.HTTP_0_9, this.tempdir);
                            fileOutputStream = new FileOutputStream(createTempFile);
                            try {
                                if (this._fileOutputBuffer > 0) {
                                    fileOutputStream = new BufferedOutputStream(fileOutputStream, this._fileOutputBuffer);
                                }
                                try {
                                    servletRequest.setAttribute(str3, createTempFile);
                                    multiMap.add(str3, str4);
                                    if (this._deleteFiles) {
                                        createTempFile.deleteOnExit();
                                        ArrayList arrayList = (ArrayList) servletRequest.getAttribute(FILES);
                                        if (arrayList == null) {
                                            arrayList = new ArrayList();
                                            servletRequest.setAttribute(FILES, arrayList);
                                        }
                                        arrayList.add(createTempFile);
                                        outputStream = fileOutputStream;
                                        file = createTempFile;
                                    } else {
                                        outputStream = fileOutputStream;
                                        file = createTempFile;
                                    }
                                    i = -2;
                                    obj2 = null;
                                    obj3 = null;
                                    while (true) {
                                        i2 = 0;
                                        obj4 = obj3;
                                        obj3 = obj2;
                                        obj2 = obj4;
                                        while (true) {
                                            read = i == -2 ? i : bufferedInputStream.read();
                                            if (read == (byte) -1) {
                                                break;
                                            }
                                            i = -2;
                                            if (read != (byte) 13 && read != (byte) 10) {
                                                if (i2 < 0 || i2 >= bytes.length || read != bytes[i2]) {
                                                    if (obj3 != null) {
                                                        try {
                                                            outputStream.write(13);
                                                        } catch (Throwable th2) {
                                                            th = th2;
                                                        }
                                                    }
                                                    if (obj2 != null) {
                                                        outputStream.write(10);
                                                    }
                                                    obj2 = null;
                                                    obj3 = null;
                                                    if (i2 > 0) {
                                                        outputStream.write(bytes, 0, i2);
                                                    }
                                                    i2 = -1;
                                                    outputStream.write(read);
                                                } else {
                                                    i2++;
                                                }
                                            }
                                        }
                                        if (read == (byte) 13) {
                                            i = bufferedInputStream.read();
                                        }
                                        if ((i2 > 0 && i2 < bytes.length - 2) || i2 == bytes.length - 1) {
                                            if (obj3 != null) {
                                                outputStream.write(13);
                                            }
                                            if (obj2 != null) {
                                                outputStream.write(10);
                                            }
                                            obj2 = null;
                                            obj3 = null;
                                            outputStream.write(bytes, 0, i2);
                                            i2 = -1;
                                        }
                                        if (i2 <= 0 && read != (byte) -1) {
                                            if (obj3 != null) {
                                                outputStream.write(13);
                                            }
                                            if (obj2 != null) {
                                                outputStream.write(10);
                                            }
                                            obj2 = read == (byte) 13 ? 1 : null;
                                            obj3 = (read == (byte) 10 || i == 10) ? 1 : null;
                                            if (i == 10) {
                                                i = -2;
                                            }
                                        }
                                    }
                                    obj2 = i2 != bytes.length ? 1 : obj;
                                    if (i != 10) {
                                        try {
                                        } finally {
                                            deleteFiles(servletRequest);
                                        }
                                    }
                                    outputStream.close();
                                    if (file != null) {
                                        multiMap.add(str3, ((ByteArrayOutputStream) outputStream).toByteArray());
                                        contentType = str;
                                        obj = obj2;
                                    } else {
                                        contentType = str;
                                        obj = obj2;
                                    }
                                } catch (Throwable th3) {
                                    th = th3;
                                    outputStream = fileOutputStream;
                                }
                            } catch (Throwable th4) {
                                th = th4;
                            }
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        outputStream = null;
                    }
                }
                outputStream = new ByteArrayOutputStream();
                file = null;
                i = -2;
                obj2 = null;
                obj3 = null;
                while (true) {
                    i2 = 0;
                    obj4 = obj3;
                    obj3 = obj2;
                    obj2 = obj4;
                    while (true) {
                        if (i == -2) {
                        }
                        if (read == (byte) -1) {
                            i = -2;
                            if (read != (byte) 13) {
                                break;
                            }
                            break;
                        }
                        break;
                    }
                    if (read == (byte) 13) {
                        i = bufferedInputStream.read();
                    }
                    if (obj3 != null) {
                        outputStream.write(13);
                    }
                    if (obj2 != null) {
                        outputStream.write(10);
                    }
                    obj2 = null;
                    obj3 = null;
                    outputStream.write(bytes, 0, i2);
                    i2 = -1;
                    if (i2 <= 0) {
                        break;
                    }
                    break;
                }
                if (i2 != bytes.length) {
                }
                if (i != 10) {
                }
                outputStream.close();
                if (file != null) {
                    contentType = str;
                    obj = obj2;
                } else {
                    multiMap.add(str3, ((ByteArrayOutputStream) outputStream).toByteArray());
                    contentType = str;
                    obj = obj2;
                }
            }
        }
        filterChain.doFilter(new Wrapper(httpServletRequest, multiMap), servletResponse);
        return;
        fileOutputStream.close();
        throw th;
        fileOutputStream = outputStream;
        fileOutputStream.close();
        throw th;
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.tempdir = (File) filterConfig.getServletContext().getAttribute(ServletHandler.__J_S_CONTEXT_TEMPDIR);
        this._deleteFiles = "true".equals(filterConfig.getInitParameter("deleteFiles"));
        String initParameter = filterConfig.getInitParameter("fileOutputBuffer");
        if (initParameter != null) {
            this._fileOutputBuffer = Integer.parseInt(initParameter);
        }
        this._context = filterConfig.getServletContext();
    }
}
