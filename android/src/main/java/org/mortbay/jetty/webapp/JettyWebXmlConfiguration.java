package org.mortbay.jetty.webapp;

public class JettyWebXmlConfiguration implements Configuration {
    private WebAppContext _context;

    public void configureClassLoader() throws Exception {
    }

    public void configureDefaults() throws Exception {
    }

    public void configureWebApp() throws java.lang.Exception {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1439)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1461)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r4 = this;
        r0 = r4._context;
        r0 = r0.isStarted();
        if (r0 == 0) goto L_0x0014;
    L_0x0008:
        r0 = org.mortbay.log.Log.isDebugEnabled();
        if (r0 == 0) goto L_0x0013;
    L_0x000e:
        r0 = "Cannot configure webapp after it is started";
        org.mortbay.log.Log.debug(r0);
    L_0x0013:
        return;
    L_0x0014:
        r0 = org.mortbay.log.Log.isDebugEnabled();
        if (r0 == 0) goto L_0x001f;
    L_0x001a:
        r0 = "Configuring web-jetty.xml";
        org.mortbay.log.Log.debug(r0);
    L_0x001f:
        r0 = r4.getWebAppContext();
        r1 = r0.getWebInf();
        if (r1 == 0) goto L_0x0013;
    L_0x0029:
        r0 = r1.isDirectory();
        if (r0 == 0) goto L_0x0013;
    L_0x002f:
        r0 = "jetty6-web.xml";
        r0 = r1.addPath(r0);
        r2 = r0.exists();
        if (r2 != 0) goto L_0x0041;
    L_0x003b:
        r0 = "jetty-web.xml";
        r0 = r1.addPath(r0);
    L_0x0041:
        r2 = r0.exists();
        if (r2 != 0) goto L_0x004d;
    L_0x0047:
        r0 = "web-jetty.xml";
        r0 = r1.addPath(r0);
    L_0x004d:
        r1 = r0.exists();
        if (r1 == 0) goto L_0x0013;
    L_0x0053:
        r1 = r4._context;
        r1 = r1.getServerClasses();
        r2 = r4._context;	 Catch:{ all -> 0x009a }
        r3 = 0;	 Catch:{ all -> 0x009a }
        r2.setServerClasses(r3);	 Catch:{ all -> 0x009a }
        r2 = org.mortbay.log.Log.isDebugEnabled();	 Catch:{ all -> 0x009a }
        if (r2 == 0) goto L_0x007b;	 Catch:{ all -> 0x009a }
    L_0x0065:
        r2 = new java.lang.StringBuffer;	 Catch:{ all -> 0x009a }
        r2.<init>();	 Catch:{ all -> 0x009a }
        r3 = "Configure: ";	 Catch:{ all -> 0x009a }
        r2 = r2.append(r3);	 Catch:{ all -> 0x009a }
        r2 = r2.append(r0);	 Catch:{ all -> 0x009a }
        r2 = r2.toString();	 Catch:{ all -> 0x009a }
        org.mortbay.log.Log.debug(r2);	 Catch:{ all -> 0x009a }
    L_0x007b:
        r2 = new org.mortbay.xml.XmlConfiguration;	 Catch:{ all -> 0x009a }
        r0 = r0.getURL();	 Catch:{ all -> 0x009a }
        r2.<init>(r0);	 Catch:{ all -> 0x009a }
        r0 = r4.getWebAppContext();	 Catch:{ all -> 0x009a }
        r2.configure(r0);	 Catch:{ all -> 0x009a }
        r0 = r4._context;
        r0 = r0.getServerClasses();
        if (r0 != 0) goto L_0x0013;
    L_0x0093:
        r0 = r4._context;
        r0.setServerClasses(r1);
        goto L_0x0013;
    L_0x009a:
        r0 = move-exception;
        r2 = r4._context;
        r2 = r2.getServerClasses();
        if (r2 != 0) goto L_0x00a8;
    L_0x00a3:
        r2 = r4._context;
        r2.setServerClasses(r1);
    L_0x00a8:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.webapp.JettyWebXmlConfiguration.configureWebApp():void");
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
