package org.mortbay.jetty.deployer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;
import org.mortbay.component.AbstractLifeCycle;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.log.Log;
import org.mortbay.resource.Resource;
import org.mortbay.util.Scanner;
import org.mortbay.util.Scanner.DiscreteListener;
import org.mortbay.xml.XmlConfiguration;

public class ContextDeployer extends AbstractLifeCycle {
    public static final String NAME = "ConfiguredDeployer";
    private ConfigurationManager _configMgr;
    private Resource _configurationDir;
    private ContextHandlerCollection _contexts;
    private Map _currentDeployments = new HashMap();
    private boolean _recursive = false;
    private int _scanInterval = 10;
    private Scanner _scanner = new Scanner();
    private ScannerListener _scannerListener;

    class C13321 implements FilenameFilter {
        private final ContextDeployer this$0;

        C13321(ContextDeployer contextDeployer) {
            this.this$0 = contextDeployer;
        }

        public boolean accept(File file, String str) {
            try {
                return str.endsWith(".xml");
            } catch (Throwable e) {
                Log.warn(e);
                return false;
            }
        }
    }

    protected class ScannerListener implements DiscreteListener {
        private final ContextDeployer this$0;

        protected ScannerListener(ContextDeployer contextDeployer) {
            this.this$0 = contextDeployer;
        }

        public void fileAdded(String str) throws Exception {
            ContextDeployer.access$000(this.this$0, str);
        }

        public void fileChanged(String str) throws Exception {
            ContextDeployer.access$100(this.this$0, str);
        }

        public void fileRemoved(String str) throws Exception {
            ContextDeployer.access$200(this.this$0, str);
        }

        public String toString() {
            return "ContextDeployer$Scanner";
        }
    }

    static void access$000(ContextDeployer contextDeployer, String str) throws Exception {
        contextDeployer.deploy(str);
    }

    static void access$100(ContextDeployer contextDeployer, String str) throws Exception {
        contextDeployer.redeploy(str);
    }

    static void access$200(ContextDeployer contextDeployer, String str) throws Exception {
        contextDeployer.undeploy(str);
    }

    private ContextHandler createContext(String str) throws Exception {
        Resource newResource = Resource.newResource(str);
        if (!newResource.exists()) {
            return null;
        }
        XmlConfiguration xmlConfiguration = new XmlConfiguration(newResource.getURL());
        Map hashMap = new HashMap();
        hashMap.put("Server", this._contexts.getServer());
        if (this._configMgr != null) {
            hashMap.putAll(this._configMgr.getProperties());
        }
        xmlConfiguration.setProperties(hashMap);
        return (ContextHandler) xmlConfiguration.configure();
    }

    private void deploy(String str) throws Exception {
        Object createContext = createContext(str);
        Log.info(new StringBuffer().append("Deploy ").append(str).append(" -> ").append(createContext).toString());
        this._contexts.addHandler(createContext);
        this._currentDeployments.put(str, createContext);
        if (this._contexts.isStarted()) {
            createContext.start();
        }
    }

    private void redeploy(String str) throws Exception {
        undeploy(str);
        deploy(str);
    }

    private void undeploy(String str) throws Exception {
        ContextHandler contextHandler = (ContextHandler) this._currentDeployments.get(str);
        Log.info(new StringBuffer().append("Undeploy ").append(str).append(" -> ").append(contextHandler).toString());
        if (contextHandler != null) {
            contextHandler.stop();
            this._contexts.removeHandler(contextHandler);
            this._currentDeployments.remove(str);
        }
    }

    protected void doStart() throws Exception {
        if (this._configurationDir == null) {
            throw new IllegalStateException("No configuraition dir specified");
        } else if (this._contexts == null) {
            throw new IllegalStateException("No context handler collection specified for deployer");
        } else {
            this._scanner.setScanDir(this._configurationDir.getFile());
            this._scanner.setScanInterval(getScanInterval());
            this._scanner.setRecursive(this._recursive);
            this._scanner.setFilenameFilter(new C13321(this));
            this._scannerListener = new ScannerListener(this);
            this._scanner.addListener(this._scannerListener);
            this._scanner.scan();
            this._scanner.start();
            this._contexts.getServer().getContainer().addBean(this._scanner);
        }
    }

    protected void doStop() throws Exception {
        this._scanner.removeListener(this._scannerListener);
        this._scanner.stop();
    }

    public Resource getConfigurationDir() {
        return this._configurationDir;
    }

    public ConfigurationManager getConfigurationManager() {
        return this._configMgr;
    }

    public ContextHandlerCollection getContexts() {
        return this._contexts;
    }

    public String getDirectory() {
        return getConfigurationDir().getName();
    }

    public boolean getRecursive() {
        return this._recursive;
    }

    public int getScanInterval() {
        return this._scanInterval;
    }

    public boolean isRecursive() {
        return this._recursive;
    }

    public void setConfigurationDir(File file) throws Exception {
        setConfigurationDir(Resource.newResource(file.toURL()));
    }

    public void setConfigurationDir(String str) throws Exception {
        setConfigurationDir(Resource.newResource(str));
    }

    public void setConfigurationDir(Resource resource) {
        if (isStarted() || isStarting()) {
            throw new IllegalStateException("Cannot change hot deploy dir after deployer start");
        }
        this._configurationDir = resource;
    }

    public void setConfigurationManager(ConfigurationManager configurationManager) {
        this._configMgr = configurationManager;
    }

    public void setContexts(ContextHandlerCollection contextHandlerCollection) {
        if (isStarted() || isStarting()) {
            throw new IllegalStateException("Cannot set Contexts after deployer start");
        }
        this._contexts = contextHandlerCollection;
    }

    public void setDirectory(String str) throws Exception {
        setConfigurationDir(str);
    }

    public void setRecursive(boolean z) {
        this._recursive = z;
    }

    public void setScanInterval(int i) {
        if (isStarted() || isStarting()) {
            throw new IllegalStateException("Cannot change scan interval after deployer start");
        }
        this._scanInterval = i;
    }
}
