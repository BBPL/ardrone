package org.mortbay.jetty.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.HandlerContainer;
import org.mortbay.jetty.Server;

public class HandlerWrapper extends AbstractHandlerContainer {
    private Handler _handler;

    public void addHandler(Handler handler) {
        Handler handler2 = getHandler();
        if (handler2 == null || (handler instanceof HandlerContainer)) {
            setHandler(handler);
            if (handler2 != null) {
                ((HandlerContainer) handler).addHandler(handler2);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Cannot add");
    }

    protected void doStart() throws Exception {
        if (this._handler != null) {
            this._handler.start();
        }
        super.doStart();
    }

    protected void doStop() throws Exception {
        super.doStop();
        if (this._handler != null) {
            this._handler.stop();
        }
    }

    protected Object expandChildren(Object obj, Class cls) {
        return expandHandler(this._handler, obj, cls);
    }

    public Handler getHandler() {
        return this._handler;
    }

    public void handle(String str, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException, ServletException {
        if (this._handler != null && isStarted()) {
            this._handler.handle(str, httpServletRequest, httpServletResponse, i);
        }
    }

    public void removeHandler(Handler handler) {
        Handler handler2 = getHandler();
        if (handler2 != null && (handler2 instanceof HandlerContainer)) {
            ((HandlerContainer) handler2).removeHandler(handler);
        } else if (handler2 == null || !handler.equals(handler2)) {
            throw new IllegalStateException("Cannot remove");
        } else {
            setHandler(null);
        }
    }

    public void setHandler(Handler handler) {
        try {
            Object obj = this._handler;
            if (getServer() != null) {
                getServer().getContainer().update((Object) this, obj, (Object) handler, "handler");
            }
            if (handler != null) {
                handler.setServer(getServer());
            }
            this._handler = handler;
            if (obj != null && obj.isStarted()) {
                obj.stop();
            }
        } catch (Throwable e) {
            IllegalStateException illegalStateException = new IllegalStateException();
            illegalStateException.initCause(e);
            throw illegalStateException;
        }
    }

    public void setServer(Server server) {
        Server server2 = getServer();
        super.setServer(server);
        Handler handler = getHandler();
        if (handler != null) {
            handler.setServer(server);
        }
        if (server != null && server != server2) {
            server.getContainer().update((Object) this, null, this._handler, "handler");
        }
    }
}
