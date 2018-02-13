package org.mortbay.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpVersion;
import org.mortbay.jetty.HttpVersions;
import org.mortbay.log.Log;
import org.mortbay.util.IO;
import org.mortbay.util.StringUtil;
import org.mortbay.util.URIUtil;

public class CGI extends HttpServlet {
    private String _cmdPrefix;
    private File _docRoot;
    private EnvList _env;
    private boolean _ignoreExitState;
    private boolean _ok;
    private String _path;

    class C13471 implements Runnable {
        private final CGI this$0;
        private final InputStream val$inFromReq;
        private final int val$inLength;
        private final OutputStream val$outToCgi;

        C13471(CGI cgi, int i, InputStream inputStream, OutputStream outputStream) {
            this.this$0 = cgi;
            this.val$inLength = i;
            this.val$inFromReq = inputStream;
            this.val$outToCgi = outputStream;
        }

        public void run() {
            try {
                if (this.val$inLength > 0) {
                    IO.copy(this.val$inFromReq, this.val$outToCgi, (long) this.val$inLength);
                }
                this.val$outToCgi.close();
            } catch (Throwable e) {
                Log.ignore(e);
            }
        }
    }

    private static class EnvList {
        private Map envMap;

        EnvList() {
            this.envMap = new HashMap();
        }

        EnvList(EnvList envList) {
            this.envMap = new HashMap(envList.envMap);
        }

        static Map access$000(EnvList envList) {
            return envList.envMap;
        }

        public String[] getEnvArray() {
            return (String[]) this.envMap.values().toArray(new String[this.envMap.size()]);
        }

        public void set(String str, String str2) {
            this.envMap.put(str, new StringBuffer().append(str).append("=").append(StringUtil.nonNull(str2)).toString());
        }

        public String toString() {
            return this.envMap.toString();
        }
    }

    private void exec(File file, String str, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        Throwable e;
        OutputStream outputStream;
        String absolutePath = file.getAbsolutePath();
        File parentFile = file.getParentFile();
        String substring = httpServletRequest.getRequestURI().substring(0, httpServletRequest.getRequestURI().length() - str.length());
        String realPath = getServletContext().getRealPath(substring);
        String pathTranslated = httpServletRequest.getPathTranslated();
        int contentLength = httpServletRequest.getContentLength();
        if (contentLength < 0) {
            contentLength = 0;
        }
        if (pathTranslated == null || pathTranslated.length() == 0) {
            pathTranslated = absolutePath;
        }
        EnvList envList = new EnvList(this._env);
        envList.set("AUTH_TYPE", httpServletRequest.getAuthType());
        envList.set("CONTENT_LENGTH", Integer.toString(contentLength));
        envList.set("CONTENT_TYPE", httpServletRequest.getContentType());
        envList.set("GATEWAY_INTERFACE", "CGI/1.1");
        if (str != null && str.length() > 0) {
            envList.set("PATH_INFO", str);
        }
        envList.set("PATH_TRANSLATED", pathTranslated);
        envList.set("QUERY_STRING", httpServletRequest.getQueryString());
        envList.set("REMOTE_ADDR", httpServletRequest.getRemoteAddr());
        envList.set("REMOTE_HOST", httpServletRequest.getRemoteHost());
        envList.set("REMOTE_USER", httpServletRequest.getRemoteUser());
        envList.set("REQUEST_METHOD", httpServletRequest.getMethod());
        envList.set("SCRIPT_NAME", substring);
        envList.set("SCRIPT_FILENAME", realPath);
        envList.set("SERVER_NAME", httpServletRequest.getServerName());
        envList.set("SERVER_PORT", Integer.toString(httpServletRequest.getServerPort()));
        envList.set("SERVER_PROTOCOL", httpServletRequest.getProtocol());
        envList.set("SERVER_SOFTWARE", getServletContext().getServerInfo());
        Enumeration headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            pathTranslated = (String) headerNames.nextElement();
            envList.set(new StringBuffer().append("HTTP_").append(pathTranslated.toUpperCase().replace('-', '_')).toString(), httpServletRequest.getHeader(pathTranslated));
        }
        envList.set("HTTPS", httpServletRequest.isSecure() ? "ON" : "OFF");
        pathTranslated = (absolutePath.charAt(0) == '\"' || absolutePath.indexOf(" ") < 0) ? absolutePath : new StringBuffer().append("\"").append(absolutePath).append("\"").toString();
        if (this._cmdPrefix != null) {
            pathTranslated = new StringBuffer().append(this._cmdPrefix).append(" ").append(pathTranslated).toString();
        }
        Process exec = parentFile == null ? Runtime.getRuntime().exec(pathTranslated, envList.getEnvArray()) : Runtime.getRuntime().exec(pathTranslated, envList.getEnvArray(), parentFile);
        ServletInputStream inputStream = httpServletRequest.getInputStream();
        OutputStream outputStream2 = exec.getOutputStream();
        IO.copyThread(exec.getErrorStream(), System.err);
        new Thread(new C13471(this, contentLength, inputStream, outputStream2)).start();
        OutputStream outputStream3 = null;
        try {
            InputStream inputStream2 = exec.getInputStream();
            while (true) {
                String textLineFromStream = getTextLineFromStream(inputStream2);
                if (textLineFromStream.length() <= 0) {
                    break;
                } else if (!textLineFromStream.startsWith(HttpVersion.HTTP)) {
                    int indexOf = textLineFromStream.indexOf(58);
                    if (indexOf > 0) {
                        String trim = textLineFromStream.substring(0, indexOf).trim();
                        textLineFromStream = textLineFromStream.substring(indexOf + 1).trim();
                        if ("Location".equals(trim)) {
                            httpServletResponse.sendRedirect(textLineFromStream);
                        } else if ("Status".equals(trim)) {
                            httpServletResponse.setStatus(Integer.parseInt(textLineFromStream.split(" ")[0]));
                        } else {
                            httpServletResponse.addHeader(trim, textLineFromStream);
                        }
                    } else {
                        continue;
                    }
                }
            }
            outputStream3 = httpServletResponse.getOutputStream();
            try {
                IO.copy(inputStream2, outputStream3);
                exec.waitFor();
                if (!this._ignoreExitState) {
                    int exitValue = exec.exitValue();
                    if (exitValue != 0) {
                        Log.warn(new StringBuffer().append("Non-zero exit status (").append(exitValue).append(") from CGI program: ").append(absolutePath).toString());
                        if (!httpServletResponse.isCommitted()) {
                            httpServletResponse.sendError(500, "Failed to exec CGI");
                        }
                    }
                }
                if (outputStream3 != null) {
                    try {
                        outputStream3.close();
                    } catch (Throwable e2) {
                        Log.ignore(e2);
                    }
                }
            } catch (IOException e3) {
                outputStream = outputStream3;
                try {
                    Log.debug("CGI: Client closed connection!");
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (Throwable e22) {
                            Log.ignore(e22);
                        }
                    }
                    exec.destroy();
                } catch (Throwable th) {
                    Throwable th2 = th;
                    outputStream3 = outputStream;
                    e22 = th2;
                    if (outputStream3 != null) {
                        try {
                            outputStream3.close();
                        } catch (Throwable th3) {
                            Log.ignore(th3);
                        }
                    }
                    exec.destroy();
                    throw e22;
                }
            } catch (InterruptedException e4) {
                try {
                    Log.debug("CGI: interrupted!");
                    if (outputStream3 != null) {
                        try {
                            outputStream3.close();
                        } catch (Throwable e222) {
                            Log.ignore(e222);
                        }
                    }
                    exec.destroy();
                } catch (Throwable th4) {
                    e222 = th4;
                    if (outputStream3 != null) {
                        outputStream3.close();
                    }
                    exec.destroy();
                    throw e222;
                }
            }
        } catch (IOException e5) {
            outputStream = outputStream3;
        } catch (InterruptedException e6) {
            outputStream3 = null;
        } catch (Throwable th5) {
            e222 = th5;
            outputStream3 = null;
        }
        exec.destroy();
    }

    private String getTextLineFromStream(InputStream inputStream) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        while (true) {
            int read = inputStream.read();
            if (read != -1 && read != 10) {
                stringBuffer.append((char) read);
            }
        }
        return stringBuffer.toString().trim();
    }

    public void init() throws ServletException {
        this._env = new EnvList();
        this._cmdPrefix = getInitParameter("commandPrefix");
        String initParameter = getInitParameter("cgibinResourceBase");
        if (initParameter == null) {
            initParameter = getInitParameter("resourceBase");
            if (initParameter == null) {
                initParameter = getServletContext().getRealPath(URIUtil.SLASH);
            }
        }
        if (initParameter == null) {
            Log.warn("CGI: no CGI bin !");
            return;
        }
        File file = new File(initParameter);
        if (!file.exists()) {
            Log.warn(new StringBuffer().append("CGI: CGI bin does not exist - ").append(file).toString());
        } else if (!file.canRead()) {
            Log.warn(new StringBuffer().append("CGI: CGI bin is not readable - ").append(file).toString());
        } else if (file.isDirectory()) {
            try {
                this._docRoot = file.getCanonicalFile();
                this._path = getInitParameter("Path");
                if (this._path != null) {
                    this._env.set("PATH", this._path);
                }
                this._ignoreExitState = "true".equalsIgnoreCase(getInitParameter("ignoreExitState"));
                Enumeration initParameterNames = getInitParameterNames();
                while (initParameterNames.hasMoreElements()) {
                    initParameter = (String) initParameterNames.nextElement();
                    if (initParameter != null && initParameter.startsWith("ENV_")) {
                        this._env.set(initParameter.substring(4), getInitParameter(initParameter));
                    }
                }
                if (!EnvList.access$000(this._env).containsKey("SystemRoot")) {
                    initParameter = System.getProperty("os.name");
                    if (!(initParameter == null || initParameter.toLowerCase().indexOf("windows") == -1)) {
                        initParameter = System.getProperty("windir");
                        EnvList envList = this._env;
                        if (initParameter == null) {
                            initParameter = "C:\\WINDOWS";
                        }
                        envList.set("SystemRoot", initParameter);
                    }
                }
                this._ok = true;
            } catch (Throwable e) {
                Log.warn(new StringBuffer().append("CGI: CGI bin failed - ").append(file).toString(), e);
            }
        } else {
            Log.warn(new StringBuffer().append("CGI: CGI bin is not a directory - ").append(file).toString());
        }
    }

    public void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        if (this._ok) {
            String stringBuffer = new StringBuffer().append(StringUtil.nonNull(httpServletRequest.getServletPath())).append(StringUtil.nonNull(httpServletRequest.getPathInfo())).toString();
            if (Log.isDebugEnabled()) {
                Log.debug(new StringBuffer().append("CGI: ContextPath : ").append(httpServletRequest.getContextPath()).toString());
                Log.debug(new StringBuffer().append("CGI: ServletPath : ").append(httpServletRequest.getServletPath()).toString());
                Log.debug(new StringBuffer().append("CGI: PathInfo    : ").append(httpServletRequest.getPathInfo()).toString());
                Log.debug(new StringBuffer().append("CGI: _docRoot    : ").append(this._docRoot).toString());
                Log.debug(new StringBuffer().append("CGI: _path       : ").append(this._path).toString());
                Log.debug(new StringBuffer().append("CGI: _ignoreExitState: ").append(this._ignoreExitState).toString());
            }
            String str = HttpVersions.HTTP_0_9;
            File file = new File(this._docRoot, stringBuffer);
            String str2 = stringBuffer;
            while (true) {
                if ((str2.endsWith(URIUtil.SLASH) || !file.exists()) && str2.length() >= 0) {
                    int lastIndexOf = str2.lastIndexOf(47);
                    str2 = str2.substring(0, lastIndexOf);
                    str = stringBuffer.substring(lastIndexOf, stringBuffer.length());
                    file = new File(this._docRoot, str2);
                }
            }
            if (str2.length() == 0 || !file.exists() || file.isDirectory() || !file.getCanonicalPath().equals(file.getAbsolutePath())) {
                httpServletResponse.sendError(404);
                return;
            }
            if (Log.isDebugEnabled()) {
                Log.debug(new StringBuffer().append("CGI: script is ").append(file).toString());
                Log.debug(new StringBuffer().append("CGI: pathInfo is ").append(str).toString());
            }
            exec(file, str, httpServletRequest, httpServletResponse);
            return;
        }
        httpServletResponse.sendError(503);
    }
}
