package org.mortbay.jetty.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.jetty.HttpVersions;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Response;
import org.mortbay.jetty.RetryRequest;
import org.mortbay.util.DateCache;
import org.mortbay.util.RolloverFileOutputStream;
import org.mortbay.util.URIUtil;

public class DebugHandler extends HandlerWrapper {
    private DateCache _date = new DateCache("HH:mm:ss", Locale.US);
    private OutputStream _out;
    private PrintStream _print;

    protected void doStart() throws Exception {
        if (this._out == null) {
            this._out = new RolloverFileOutputStream("./logs/yyyy_mm_dd.debug.log", true);
        }
        this._print = new PrintStream(this._out);
        super.doStart();
    }

    protected void doStop() throws Exception {
        super.doStop();
        this._print.close();
    }

    public OutputStream getOutputStream() {
        return this._out;
    }

    public void handle(String str, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException, ServletException {
        String str2;
        Object obj;
        Throwable th;
        Request request = (Request) httpServletRequest;
        Response response = (Response) httpServletResponse;
        Thread currentThread = Thread.currentThread();
        String name = currentThread.getName();
        Object obj2 = null;
        String str3 = (String) httpServletRequest.getAttribute("org.mortbay.jetty.thread.name");
        if (str3 == null) {
            str3 = new StringBuffer().append(name).append("://").append(request.getHeader("Host")).append(request.getUri()).toString();
        } else {
            obj2 = 1;
        }
        String str4 = null;
        String str5;
        try {
            String now = this._date.now();
            int lastMs = this._date.lastMs();
            StringBuffer append;
            if (obj2 != null) {
                PrintStream printStream = this._print;
                append = new StringBuffer().append(now);
                str2 = lastMs > 99 ? "." : lastMs > 9 ? ".0" : ".00";
                printStream.println(append.append(str2).append(lastMs).append(":").append(str3).append(" RETRY").toString());
            } else {
                PrintStream printStream2 = this._print;
                append = new StringBuffer().append(now);
                str5 = lastMs > 99 ? "." : lastMs > 9 ? ".0" : ".00";
                printStream2.println(append.append(str5).append(lastMs).append(":").append(str3).append(" ").append(request.getRemoteAddr()).append(" ").append(httpServletRequest.getMethod()).append(" ").append(request.getHeader("Cookie")).append("; ").append(request.getHeader("User-Agent")).toString());
            }
            currentThread.setName(str3);
            super.handle(str, httpServletRequest, httpServletResponse, i);
            currentThread.setName(name);
            str2 = this._date.now();
            int lastMs2 = this._date.lastMs();
            PrintStream printStream3 = this._print;
            StringBuffer append2 = new StringBuffer().append(str2);
            str2 = lastMs2 > 99 ? "." : lastMs2 > 9 ? ".0" : ".00";
            printStream3.println(append2.append(str2).append(lastMs2).append(":").append(str3).append(" ").append(response.getStatus()).append(" ").append(response.getContentType()).append(" ").append(response.getContentCount()).append(HttpVersions.HTTP_0_9).toString());
            return;
        } catch (RetryRequest e) {
            httpServletRequest.setAttribute("org.mortbay.jetty.thread.name", str3);
            throw e;
        } catch (IOException e2) {
            str5 = e2.toString();
            throw e2;
        } catch (ServletException e3) {
            str5 = new StringBuffer().append(e3.toString()).append(":").append(e3.getCause()).toString();
            throw e3;
        } catch (RuntimeException e4) {
            str5 = e4.toString();
            throw e4;
        } catch (Error e5) {
            str5 = e5.toString();
            throw e5;
        } catch (Throwable th2) {
            str4 = str5;
            th = th2;
            obj = null;
        }
        currentThread.setName(name);
        String now2 = this._date.now();
        int lastMs3 = this._date.lastMs();
        if (obj != null) {
            PrintStream printStream4 = this._print;
            StringBuffer append3 = new StringBuffer().append(now2);
            str2 = lastMs3 > 99 ? "." : lastMs3 > 9 ? ".0" : ".00";
            printStream4.println(append3.append(str2).append(lastMs3).append(":").append(str3).append(" SUSPEND").toString());
        } else {
            PrintStream printStream5 = this._print;
            append2 = new StringBuffer().append(now2);
            str2 = lastMs3 > 99 ? "." : lastMs3 > 9 ? ".0" : ".00";
            printStream5.println(append2.append(str2).append(lastMs3).append(":").append(str3).append(" ").append(response.getStatus()).append(" ").append(response.getContentType()).append(" ").append(response.getContentCount()).append(str4 == null ? HttpVersions.HTTP_0_9 : new StringBuffer().append(URIUtil.SLASH).append(str4).toString()).toString());
        }
        throw th;
    }

    public void setOutputStream(OutputStream outputStream) {
        this._out = outputStream;
    }
}
