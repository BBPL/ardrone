package org.mortbay.jetty.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.util.LazyList;
import org.mortbay.util.MultiException;

public class HandlerCollection extends AbstractHandlerContainer {
    static Class class$org$mortbay$jetty$Handler;
    private Handler[] _handlers;

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    public void addHandler(Handler handler) {
        Class class$;
        Handler[] handlers = getHandlers();
        if (class$org$mortbay$jetty$Handler == null) {
            class$ = class$("org.mortbay.jetty.Handler");
            class$org$mortbay$jetty$Handler = class$;
        } else {
            class$ = class$org$mortbay$jetty$Handler;
        }
        setHandlers((Handler[]) LazyList.addToArray(handlers, handler, class$));
    }

    protected void doStart() throws Exception {
        MultiException multiException = new MultiException();
        if (this._handlers != null) {
            for (Handler start : this._handlers) {
                try {
                    start.start();
                } catch (Throwable th) {
                    multiException.add(th);
                }
            }
        }
        super.doStart();
        multiException.ifExceptionThrow();
    }

    protected void doStop() throws Exception {
        MultiException multiException = new MultiException();
        try {
            super.doStop();
        } catch (Throwable th) {
            multiException.add(th);
        }
        if (this._handlers != null) {
            int length = this._handlers.length;
            while (true) {
                int i = length - 1;
                if (length <= 0) {
                    break;
                }
                try {
                    this._handlers[i].stop();
                    length = i;
                } catch (Throwable th2) {
                    multiException.add(th2);
                    length = i;
                }
            }
        }
        multiException.ifExceptionThrow();
    }

    protected Object expandChildren(Object obj, Class cls) {
        Handler[] handlers = getHandlers();
        int i = 0;
        while (handlers != null && i < handlers.length) {
            obj = expandHandler(handlers[i], obj, cls);
            i++;
        }
        return obj;
    }

    public Handler[] getHandlers() {
        return this._handlers;
    }

    public void handle(String str, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException, ServletException {
        if (this._handlers != null && isStarted()) {
            MultiException multiException = null;
            for (Handler handle : this._handlers) {
                try {
                    handle.handle(str, httpServletRequest, httpServletResponse, i);
                } catch (IOException e) {
                    throw e;
                } catch (RuntimeException e2) {
                    throw e2;
                } catch (Throwable e3) {
                    if (multiException == null) {
                        multiException = new MultiException();
                    }
                    multiException.add(e3);
                }
            }
            if (multiException == null) {
                return;
            }
            if (multiException.size() == 1) {
                throw new ServletException(multiException.getThrowable(0));
            }
            throw new ServletException(multiException);
        }
    }

    public void removeHandler(Handler handler) {
        Handler[] handlers = getHandlers();
        if (handlers != null && handlers.length > 0) {
            setHandlers((Handler[]) LazyList.removeFromArray(handlers, handler));
        }
    }

    public void setHandlers(Handler[] handlerArr) {
        if (this._handlers == null) {
            Object[] objArr = null;
        } else {
            Handler[] handlerArr2 = (Handler[]) this._handlers.clone();
        }
        if (getServer() != null) {
            getServer().getContainer().update((Object) this, objArr, (Object[]) handlerArr, "handler");
        }
        Server server = getServer();
        MultiException multiException = new MultiException();
        int i = 0;
        while (handlerArr != null && i < handlerArr.length) {
            if (handlerArr[i].getServer() != server) {
                handlerArr[i].setServer(server);
            }
            i++;
        }
        this._handlers = handlerArr;
        i = 0;
        while (objArr != null && i < objArr.length) {
            if (objArr[i] != null) {
                try {
                    if (objArr[i].isStarted()) {
                        objArr[i].stop();
                    }
                } catch (Throwable th) {
                    multiException.add(th);
                }
            }
            i++;
        }
        multiException.ifExceptionThrowRuntime();
    }

    public void setServer(Server server) {
        Server server2 = getServer();
        super.setServer(server);
        Handler[] handlers = getHandlers();
        int i = 0;
        while (handlers != null && i < handlers.length) {
            handlers[i].setServer(server);
            i++;
        }
        if (server != null && server != server2) {
            server.getContainer().update((Object) this, null, this._handlers, "handler");
        }
    }
}
