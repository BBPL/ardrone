package org.mortbay.jetty.webapp;

import org.mortbay.log.Log;
import org.mortbay.resource.Resource;

public class WebInfConfiguration implements Configuration {
    protected WebAppContext _context;

    public void configureClassLoader() throws Exception {
        if (!this._context.isStarted()) {
            Resource webInf = this._context.getWebInf();
            if (webInf != null && webInf.isDirectory() && (this._context.getClassLoader() instanceof WebAppClassLoader)) {
                Resource addPath = webInf.addPath("classes/");
                if (addPath.exists()) {
                    ((WebAppClassLoader) this._context.getClassLoader()).addClassPath(addPath.toString());
                }
                webInf = webInf.addPath("lib/");
                if (webInf.exists() || webInf.isDirectory()) {
                    ((WebAppClassLoader) this._context.getClassLoader()).addJars(webInf);
                }
            }
        } else if (Log.isDebugEnabled()) {
            Log.debug("Cannot configure webapp after it is started");
        }
    }

    public void configureDefaults() throws Exception {
    }

    public void configureWebApp() throws Exception {
    }

    public void deconfigureWebApp() throws Exception {
    }

    public WebAppContext getWebAppContext() {
        return this._context;
    }

    public void setWebAppContext(WebAppContext webAppContext) {
        this._context = webAppContext;
    }
}
