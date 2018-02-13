package org.mortbay.jetty.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.HandlerContainer;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.security.Constraint;
import org.mortbay.jetty.servlet.PathMap;
import org.mortbay.log.Log;
import org.mortbay.util.LazyList;
import org.mortbay.util.URIUtil;

public class ContextHandlerCollection extends HandlerCollection {
    static Class class$org$mortbay$jetty$handler$ContextHandler;
    private Class _contextClass;
    private PathMap _contextMap;

    public ContextHandlerCollection() {
        Class class$;
        if (class$org$mortbay$jetty$handler$ContextHandler == null) {
            class$ = class$("org.mortbay.jetty.handler.ContextHandler");
            class$org$mortbay$jetty$handler$ContextHandler = class$;
        } else {
            class$ = class$org$mortbay$jetty$handler$ContextHandler;
        }
        this._contextClass = class$;
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    private String normalizeHostname(String str) {
        return str == null ? null : str.endsWith(".") ? str.substring(0, str.length() - 1) : str;
    }

    public ContextHandler addContext(String str, String str2) {
        try {
            ContextHandler contextHandler = (ContextHandler) this._contextClass.newInstance();
            contextHandler.setContextPath(str);
            contextHandler.setResourceBase(str2);
            addHandler(contextHandler);
            return contextHandler;
        } catch (Throwable e) {
            Log.debug(e);
            throw new Error(e);
        }
    }

    protected void doStart() throws Exception {
        mapContexts();
        super.doStart();
    }

    public Class getContextClass() {
        return this._contextClass;
    }

    public void handle(String str, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException, ServletException {
        int i2 = 0;
        Handler[] handlers = getHandlers();
        if (handlers != null && handlers.length != 0) {
            Request request = HttpConnection.getCurrentConnection().getRequest();
            PathMap pathMap = this._contextMap;
            if (pathMap == null || str == null || !str.startsWith(URIUtil.SLASH)) {
                while (i2 < handlers.length) {
                    handlers[i2].handle(str, httpServletRequest, httpServletResponse, i);
                    if (!request.isHandled()) {
                        i2++;
                    } else {
                        return;
                    }
                }
                return;
            }
            Object lazyMatches = pathMap.getLazyMatches(str);
            for (int i3 = 0; i3 < LazyList.size(lazyMatches); i3++) {
                Object value = ((Entry) LazyList.get(lazyMatches, i3)).getValue();
                int i4;
                if (value instanceof Map) {
                    Map map = (Map) value;
                    String normalizeHostname = normalizeHostname(httpServletRequest.getServerName());
                    Object obj = map.get(normalizeHostname);
                    i4 = 0;
                    while (i4 < LazyList.size(obj)) {
                        ((Handler) LazyList.get(obj, i4)).handle(str, httpServletRequest, httpServletResponse, i);
                        if (!request.isHandled()) {
                            i4++;
                        } else {
                            return;
                        }
                    }
                    Object obj2 = map.get(new StringBuffer().append("*.").append(normalizeHostname.substring(normalizeHostname.indexOf(".") + 1)).toString());
                    i4 = 0;
                    while (i4 < LazyList.size(obj2)) {
                        ((Handler) LazyList.get(obj2, i4)).handle(str, httpServletRequest, httpServletResponse, i);
                        if (!request.isHandled()) {
                            i4++;
                        } else {
                            return;
                        }
                    }
                    Object obj3 = map.get(Constraint.ANY_ROLE);
                    int i5 = 0;
                    while (i5 < LazyList.size(obj3)) {
                        ((Handler) LazyList.get(obj3, i5)).handle(str, httpServletRequest, httpServletResponse, i);
                        if (!request.isHandled()) {
                            i5++;
                        } else {
                            return;
                        }
                    }
                    continue;
                } else {
                    i4 = 0;
                    while (i4 < LazyList.size(value)) {
                        ((Handler) LazyList.get(value, i4)).handle(str, httpServletRequest, httpServletResponse, i);
                        if (!request.isHandled()) {
                            i4++;
                        } else {
                            return;
                        }
                    }
                    continue;
                }
            }
        }
    }

    public void mapContexts() {
        PathMap pathMap = new PathMap();
        Handler[] handlers = getHandlers();
        int i = 0;
        while (handlers != null && i < handlers.length) {
            Handler[] handlerArr;
            if (handlers[i] instanceof ContextHandler) {
                handlerArr = new Handler[]{handlers[i]};
            } else if (handlers[i] instanceof HandlerContainer) {
                Class class$;
                HandlerContainer handlerContainer = (HandlerContainer) handlers[i];
                if (class$org$mortbay$jetty$handler$ContextHandler == null) {
                    class$ = class$("org.mortbay.jetty.handler.ContextHandler");
                    class$org$mortbay$jetty$handler$ContextHandler = class$;
                } else {
                    class$ = class$org$mortbay$jetty$handler$ContextHandler;
                }
                handlerArr = handlerContainer.getChildHandlersByClass(class$);
            } else {
                continue;
                i++;
            }
            for (Handler handler : handlerArr) {
                ContextHandler contextHandler = (ContextHandler) handler;
                String contextPath = contextHandler.getContextPath();
                if (contextPath == null || contextPath.indexOf(44) >= 0 || contextPath.startsWith(Constraint.ANY_ROLE)) {
                    throw new IllegalArgumentException(new StringBuffer().append("Illegal context spec:").append(contextPath).toString());
                }
                Object stringBuffer;
                String stringBuffer2;
                Object obj;
                String[] virtualHosts;
                if (!contextPath.startsWith(URIUtil.SLASH)) {
                    contextPath = new StringBuffer().append('/').append(contextPath).toString();
                }
                if (contextPath.length() > 1) {
                    if (contextPath.endsWith(URIUtil.SLASH)) {
                        stringBuffer = new StringBuffer().append(contextPath).append(Constraint.ANY_ROLE).toString();
                    } else if (!contextPath.endsWith("/*")) {
                        stringBuffer2 = new StringBuffer().append(contextPath).append("/*").toString();
                    }
                    obj = pathMap.get(stringBuffer);
                    virtualHosts = contextHandler.getVirtualHosts();
                    if (virtualHosts == null && virtualHosts.length > 0) {
                        Map map;
                        if (obj instanceof Map) {
                            map = (Map) obj;
                        } else {
                            map = new HashMap();
                            map.put(Constraint.ANY_ROLE, obj);
                            pathMap.put(stringBuffer, map);
                        }
                        for (Object stringBuffer3 : virtualHosts) {
                            map.put(stringBuffer3, LazyList.add(map.get(stringBuffer3), handlers[i]));
                        }
                    } else if (obj instanceof Map) {
                        pathMap.put(stringBuffer3, LazyList.add(obj, handlers[i]));
                    } else {
                        Map map2 = (Map) obj;
                        map2.put(Constraint.ANY_ROLE, LazyList.add(map2.get(Constraint.ANY_ROLE), handlers[i]));
                    }
                }
                stringBuffer2 = contextPath;
                obj = pathMap.get(stringBuffer3);
                virtualHosts = contextHandler.getVirtualHosts();
                if (virtualHosts == null) {
                }
                if (obj instanceof Map) {
                    pathMap.put(stringBuffer3, LazyList.add(obj, handlers[i]));
                } else {
                    Map map22 = (Map) obj;
                    map22.put(Constraint.ANY_ROLE, LazyList.add(map22.get(Constraint.ANY_ROLE), handlers[i]));
                }
            }
            continue;
            i++;
        }
        this._contextMap = pathMap;
    }

    public void setContextClass(Class cls) {
        if (cls != null) {
            Class class$;
            if (class$org$mortbay$jetty$handler$ContextHandler == null) {
                class$ = class$("org.mortbay.jetty.handler.ContextHandler");
                class$org$mortbay$jetty$handler$ContextHandler = class$;
            } else {
                class$ = class$org$mortbay$jetty$handler$ContextHandler;
            }
            if (class$.isAssignableFrom(cls)) {
                this._contextClass = cls;
                return;
            }
        }
        throw new IllegalArgumentException();
    }

    public void setHandlers(Handler[] handlerArr) {
        this._contextMap = null;
        super.setHandlers(handlerArr);
        if (isStarted()) {
            mapContexts();
        }
    }
}
