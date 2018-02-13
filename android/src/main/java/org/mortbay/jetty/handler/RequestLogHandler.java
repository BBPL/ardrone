package org.mortbay.jetty.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.RequestLog;
import org.mortbay.jetty.Response;
import org.mortbay.jetty.Server;
import org.mortbay.log.Log;

public class RequestLogHandler extends HandlerWrapper {
    private RequestLog _requestLog;

    protected void doStart() throws Exception {
        super.doStart();
        if (this._requestLog != null) {
            this._requestLog.start();
        }
    }

    protected void doStop() throws Exception {
        super.doStop();
        if (this._requestLog != null) {
            this._requestLog.stop();
        }
    }

    public RequestLog getRequestLog() {
        return this._requestLog;
    }

    public void handle(String str, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException, ServletException {
        super.handle(str, httpServletRequest, httpServletResponse, i);
        if (i == 1 && this._requestLog != null) {
            this._requestLog.log((Request) httpServletRequest, (Response) httpServletResponse);
        }
    }

    public void setRequestLog(RequestLog requestLog) {
        try {
            if (this._requestLog != null) {
                this._requestLog.stop();
            }
        } catch (Throwable e) {
            Log.warn(e);
        }
        if (getServer() != null) {
            getServer().getContainer().update((Object) this, this._requestLog, (Object) requestLog, "logimpl", true);
        }
        this._requestLog = requestLog;
        try {
            if (isStarted() && this._requestLog != null) {
                this._requestLog.start();
            }
        } catch (Throwable e2) {
            throw new RuntimeException(e2);
        }
    }

    public void setServer(Server server) {
        if (this._requestLog != null) {
            if (!(getServer() == null || getServer() == server)) {
                getServer().getContainer().update((Object) this, this._requestLog, null, "logimpl", true);
            }
            super.setServer(server);
            if (server != null && server != getServer()) {
                server.getContainer().update((Object) this, null, this._requestLog, "logimpl", true);
                return;
            }
            return;
        }
        super.setServer(server);
    }
}
