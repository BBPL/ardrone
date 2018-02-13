package org.mortbay.jetty.deployer;

import java.util.ArrayList;
import org.mortbay.component.AbstractLifeCycle;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.HandlerContainer;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.log.Log;
import org.mortbay.resource.Resource;
import org.mortbay.util.URIUtil;

public class WebAppDeployer extends AbstractLifeCycle {
    static Class class$org$mortbay$jetty$handler$ContextHandler;
    static Class class$org$mortbay$jetty$webapp$WebAppContext;
    private boolean _allowDuplicates;
    private String[] _configurationClasses;
    private HandlerContainer _contexts;
    private String _defaultsDescriptor;
    private ArrayList _deployed;
    private boolean _extract;
    private boolean _parentLoaderPriority;
    private String _webAppDir;

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    public void doStart() throws Exception {
        this._deployed = new ArrayList();
        scan();
    }

    public void doStop() throws Exception {
        int size = this._deployed.size();
        while (true) {
            int i = size - 1;
            if (size > 0) {
                ((ContextHandler) this._deployed.get(i)).stop();
                size = i;
            } else {
                return;
            }
        }
    }

    public boolean getAllowDuplicates() {
        return this._allowDuplicates;
    }

    public String[] getConfigurationClasses() {
        return this._configurationClasses;
    }

    public HandlerContainer getContexts() {
        return this._contexts;
    }

    public String getDefaultsDescriptor() {
        return this._defaultsDescriptor;
    }

    public String getWebAppDir() {
        return this._webAppDir;
    }

    public boolean isExtract() {
        return this._extract;
    }

    public boolean isParentLoaderPriority() {
        return this._parentLoaderPriority;
    }

    public void scan() throws Exception {
        if (this._contexts == null) {
            throw new IllegalArgumentException("No HandlerContainer");
        }
        Resource newResource = Resource.newResource(this._webAppDir);
        if (!newResource.exists()) {
            throw new IllegalArgumentException(new StringBuffer().append("No such webapps resource ").append(newResource).toString());
        } else if (newResource.isDirectory()) {
            String[] list = newResource.list();
            int i = 0;
            while (list != null && i < list.length) {
                String str = list[i];
                if (!str.equalsIgnoreCase("CVS/") && !str.equalsIgnoreCase("CVS") && !str.startsWith(".")) {
                    Class class$;
                    Object obj;
                    Resource addPath = newResource.addPath(newResource.encode(str));
                    if (str.toLowerCase().endsWith(".war") || str.toLowerCase().endsWith(".jar")) {
                        str = str.substring(0, str.length() - 4);
                        Resource addPath2 = newResource.addPath(str);
                        if (addPath2 != null && addPath2.exists() && addPath2.isDirectory()) {
                        }
                    } else if (!addPath.isDirectory()) {
                    }
                    str = (str.equalsIgnoreCase("root") || str.equalsIgnoreCase("root/")) ? URIUtil.SLASH : new StringBuffer().append(URIUtil.SLASH).append(str).toString();
                    String substring = (!str.endsWith(URIUtil.SLASH) || str.length() <= 0) ? str : str.substring(0, str.length() - 1);
                    if (!this._allowDuplicates) {
                        HandlerContainer handlerContainer = this._contexts;
                        if (class$org$mortbay$jetty$handler$ContextHandler == null) {
                            class$ = class$("org.mortbay.jetty.handler.ContextHandler");
                            class$org$mortbay$jetty$handler$ContextHandler = class$;
                        } else {
                            class$ = class$org$mortbay$jetty$handler$ContextHandler;
                        }
                        Handler[] childHandlersByClass = handlerContainer.getChildHandlersByClass(class$);
                        for (Handler handler : childHandlersByClass) {
                            ContextHandler contextHandler = (ContextHandler) handler;
                            if (substring.equals(contextHandler.getContextPath())) {
                                break;
                            }
                            try {
                                str = contextHandler instanceof WebAppContext ? Resource.newResource(((WebAppContext) contextHandler).getWar()).getFile().getAbsolutePath() : contextHandler.getBaseResource() != null ? contextHandler.getBaseResource().getFile().getAbsolutePath() : null;
                                if (str != null && str.equals(addPath.getFile().getAbsolutePath())) {
                                    break;
                                }
                            } catch (Throwable e) {
                                Log.ignore(e);
                            }
                        }
                    }
                    if (this._contexts instanceof ContextHandlerCollection) {
                        Class cls;
                        if (class$org$mortbay$jetty$webapp$WebAppContext == null) {
                            class$ = class$("org.mortbay.jetty.webapp.WebAppContext");
                            class$org$mortbay$jetty$webapp$WebAppContext = class$;
                            cls = class$;
                        } else {
                            cls = class$org$mortbay$jetty$webapp$WebAppContext;
                        }
                        if (cls.isAssignableFrom(((ContextHandlerCollection) this._contexts).getContextClass())) {
                            try {
                                obj = (WebAppContext) ((ContextHandlerCollection) this._contexts).getContextClass().newInstance();
                                obj.setContextPath(substring);
                                if (this._configurationClasses != null) {
                                    obj.setConfigurationClasses(this._configurationClasses);
                                }
                                if (this._defaultsDescriptor != null) {
                                    obj.setDefaultsDescriptor(this._defaultsDescriptor);
                                }
                                obj.setExtractWAR(this._extract);
                                obj.setWar(addPath.toString());
                                obj.setParentLoaderPriority(this._parentLoaderPriority);
                                this._contexts.addHandler(obj);
                                this._deployed.add(obj);
                                if (this._contexts.isStarted()) {
                                    obj.start();
                                }
                            } catch (Throwable e2) {
                                throw new Error(e2);
                            }
                        }
                    }
                    obj = new WebAppContext();
                    obj.setContextPath(substring);
                    if (this._configurationClasses != null) {
                        obj.setConfigurationClasses(this._configurationClasses);
                    }
                    if (this._defaultsDescriptor != null) {
                        obj.setDefaultsDescriptor(this._defaultsDescriptor);
                    }
                    obj.setExtractWAR(this._extract);
                    obj.setWar(addPath.toString());
                    obj.setParentLoaderPriority(this._parentLoaderPriority);
                    this._contexts.addHandler(obj);
                    this._deployed.add(obj);
                    if (this._contexts.isStarted()) {
                        obj.start();
                    }
                }
                i++;
            }
        } else {
            throw new IllegalArgumentException(new StringBuffer().append("Not directory webapps resource ").append(newResource).toString());
        }
    }

    public void setAllowDuplicates(boolean z) {
        this._allowDuplicates = z;
    }

    public void setConfigurationClasses(String[] strArr) {
        this._configurationClasses = strArr;
    }

    public void setContexts(HandlerContainer handlerContainer) {
        this._contexts = handlerContainer;
    }

    public void setDefaultsDescriptor(String str) {
        this._defaultsDescriptor = str;
    }

    public void setExtract(boolean z) {
        this._extract = z;
    }

    public void setParentLoaderPriority(boolean z) {
        this._parentLoaderPriority = z;
    }

    public void setWebAppDir(String str) {
        this._webAppDir = str;
    }
}
