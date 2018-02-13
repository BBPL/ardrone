package org.mortbay.jetty.webapp;

public class TagLibConfiguration implements Configuration {
    static Class class$org$mortbay$jetty$webapp$TagLibConfiguration;
    WebAppContext _context;

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    public void configureClassLoader() throws Exception {
    }

    public void configureDefaults() throws Exception {
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void configureWebApp() throws java.lang.Exception {
        /*
        r13 = this;
        r8 = new java.util.HashSet;
        r8.<init>();
        r9 = new java.util.HashSet;
        r9.<init>();
        r0 = r13._context;
        r0 = r0.getResourceAliases();
        if (r0 == 0) goto L_0x0077;
    L_0x0012:
        r0 = r13._context;
        r0 = r0.getBaseResource();
        if (r0 == 0) goto L_0x0077;
    L_0x001a:
        r0 = r13._context;
        r0 = r0.getBaseResource();
        r0 = r0.exists();
        if (r0 == 0) goto L_0x0077;
    L_0x0026:
        r0 = r13._context;
        r0 = r0.getResourceAliases();
        r0 = r0.values();
        r1 = r0.iterator();
    L_0x0034:
        r0 = r1.hasNext();
        if (r0 == 0) goto L_0x0077;
    L_0x003a:
        r0 = r1.next();
        r0 = (java.lang.String) r0;
        if (r0 == 0) goto L_0x0034;
    L_0x0042:
        r2 = r0.toLowerCase();
        r3 = ".tld";
        r2 = r2.endsWith(r3);
        if (r2 == 0) goto L_0x0034;
    L_0x004e:
        r2 = "/";
        r2 = r0.startsWith(r2);
        if (r2 != 0) goto L_0x0069;
    L_0x0056:
        r2 = new java.lang.StringBuffer;
        r2.<init>();
        r3 = "/WEB-INF/";
        r2 = r2.append(r3);
        r0 = r2.append(r0);
        r0 = r0.toString();
    L_0x0069:
        r2 = r13._context;
        r2 = r2.getBaseResource();
        r0 = r2.addPath(r0);
        r8.add(r0);
        goto L_0x0034;
    L_0x0077:
        r0 = r13._context;
        r0 = r0.getWebInf();
        if (r0 == 0) goto L_0x00ad;
    L_0x007f:
        r1 = r0.list();
        r0 = 0;
    L_0x0084:
        if (r1 == 0) goto L_0x00ad;
    L_0x0086:
        r2 = r1.length;
        if (r0 >= r2) goto L_0x00ad;
    L_0x0089:
        r2 = r1[r0];
        if (r2 == 0) goto L_0x00aa;
    L_0x008d:
        r2 = r1[r0];
        r2 = r2.toLowerCase();
        r3 = ".tld";
        r2 = r2.endsWith(r3);
        if (r2 == 0) goto L_0x00aa;
    L_0x009b:
        r2 = r13._context;
        r2 = r2.getWebInf();
        r3 = r1[r0];
        r2 = r2.addPath(r3);
        r8.add(r2);
    L_0x00aa:
        r0 = r0 + 1;
        goto L_0x0084;
    L_0x00ad:
        r0 = r13._context;
        r1 = "org.mortbay.jetty.webapp.NoTLDJarPattern";
        r0 = r0.getInitParameter(r1);
        if (r0 != 0) goto L_0x011b;
    L_0x00b7:
        r0 = 0;
        r2 = r0;
    L_0x00b9:
        r0 = java.lang.Thread.currentThread();
        r1 = r0.getContextClassLoader();
        r0 = 0;
        r7 = r0;
    L_0x00c3:
        if (r1 == 0) goto L_0x01d2;
    L_0x00c5:
        r0 = r1 instanceof java.net.URLClassLoader;
        if (r0 == 0) goto L_0x01ca;
    L_0x00c9:
        r0 = r1;
        r0 = (java.net.URLClassLoader) r0;
        r10 = r0.getURLs();
        if (r10 == 0) goto L_0x01ca;
    L_0x00d2:
        r0 = 0;
        r5 = r0;
    L_0x00d4:
        r0 = r10.length;
        if (r5 >= r0) goto L_0x01ca;
    L_0x00d7:
        r0 = r10[r5];
        r0 = r0.toString();
        r0 = r0.toLowerCase();
        r3 = ".jar";
        r0 = r0.endsWith(r3);
        if (r0 == 0) goto L_0x0117;
    L_0x00e9:
        r0 = r10[r5];
        r0 = r0.toString();
        r3 = 47;
        r3 = r0.lastIndexOf(r3);
        r3 = r3 + 1;
        r0 = r0.substring(r3);
        if (r7 == 0) goto L_0x0121;
    L_0x00fd:
        r3 = r13._context;
        r3 = r3.isParentLoaderPriority();
        if (r3 != 0) goto L_0x010b;
    L_0x0105:
        r3 = r9.contains(r0);
        if (r3 != 0) goto L_0x0117;
    L_0x010b:
        if (r2 == 0) goto L_0x0121;
    L_0x010d:
        r3 = r2.matcher(r0);
        r3 = r3.matches();
        if (r3 == 0) goto L_0x0121;
    L_0x0117:
        r0 = r5 + 1;
        r5 = r0;
        goto L_0x00d4;
    L_0x011b:
        r0 = java.util.regex.Pattern.compile(r0);
        r2 = r0;
        goto L_0x00b9;
    L_0x0121:
        r9.add(r0);
        r0 = "TLD search of {}";
        r3 = r10[r5];
        org.mortbay.log.Log.debug(r0, r3);
        r0 = r10[r5];
        r0 = org.mortbay.resource.Resource.newResource(r0);
        r11 = r0.getFile();
        if (r11 == 0) goto L_0x0117;
    L_0x0137:
        r0 = r11.exists();
        if (r0 == 0) goto L_0x0117;
    L_0x013d:
        r0 = r11.canRead();
        if (r0 == 0) goto L_0x0117;
    L_0x0143:
        r6 = 0;
        r4 = 0;
        r3 = new java.util.jar.JarFile;	 Catch:{ Exception -> 0x0375, all -> 0x037e }
        r3.<init>(r11);	 Catch:{ Exception -> 0x0375, all -> 0x037e }
        r4 = r3.entries();	 Catch:{ Exception -> 0x019e, all -> 0x01c3 }
    L_0x014e:
        r0 = r4.hasMoreElements();	 Catch:{ Exception -> 0x019e, all -> 0x01c3 }
        if (r0 == 0) goto L_0x01bc;
    L_0x0154:
        r0 = r4.nextElement();	 Catch:{ Exception -> 0x019e, all -> 0x01c3 }
        r0 = (java.util.zip.ZipEntry) r0;	 Catch:{ Exception -> 0x019e, all -> 0x01c3 }
        r0 = r0.getName();	 Catch:{ Exception -> 0x019e, all -> 0x01c3 }
        r6 = "META-INF/";
        r6 = r0.startsWith(r6);	 Catch:{ Exception -> 0x019e, all -> 0x01c3 }
        if (r6 == 0) goto L_0x014e;
    L_0x0166:
        r6 = r0.toLowerCase();	 Catch:{ Exception -> 0x019e, all -> 0x01c3 }
        r12 = ".tld";
        r6 = r6.endsWith(r12);	 Catch:{ Exception -> 0x019e, all -> 0x01c3 }
        if (r6 == 0) goto L_0x014e;
    L_0x0172:
        r6 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x019e, all -> 0x01c3 }
        r6.<init>();	 Catch:{ Exception -> 0x019e, all -> 0x01c3 }
        r12 = "jar:";
        r6 = r6.append(r12);	 Catch:{ Exception -> 0x019e, all -> 0x01c3 }
        r12 = r10[r5];	 Catch:{ Exception -> 0x019e, all -> 0x01c3 }
        r6 = r6.append(r12);	 Catch:{ Exception -> 0x019e, all -> 0x01c3 }
        r12 = "!/";
        r6 = r6.append(r12);	 Catch:{ Exception -> 0x019e, all -> 0x01c3 }
        r0 = r6.append(r0);	 Catch:{ Exception -> 0x019e, all -> 0x01c3 }
        r0 = r0.toString();	 Catch:{ Exception -> 0x019e, all -> 0x01c3 }
        r0 = org.mortbay.resource.Resource.newResource(r0);	 Catch:{ Exception -> 0x019e, all -> 0x01c3 }
        r8.add(r0);	 Catch:{ Exception -> 0x019e, all -> 0x01c3 }
        r6 = "TLD found {}";
        org.mortbay.log.Log.debug(r6, r0);	 Catch:{ Exception -> 0x019e, all -> 0x01c3 }
        goto L_0x014e;
    L_0x019e:
        r0 = move-exception;
    L_0x019f:
        r4 = new java.lang.StringBuffer;	 Catch:{ all -> 0x0382 }
        r4.<init>();	 Catch:{ all -> 0x0382 }
        r6 = "Failed to read file: ";
        r4 = r4.append(r6);	 Catch:{ all -> 0x0382 }
        r4 = r4.append(r11);	 Catch:{ all -> 0x0382 }
        r4 = r4.toString();	 Catch:{ all -> 0x0382 }
        org.mortbay.log.Log.warn(r4, r0);	 Catch:{ all -> 0x0382 }
        if (r3 == 0) goto L_0x0117;
    L_0x01b7:
        r3.close();
        goto L_0x0117;
    L_0x01bc:
        if (r3 == 0) goto L_0x0117;
    L_0x01be:
        r3.close();
        goto L_0x0117;
    L_0x01c3:
        r0 = move-exception;
    L_0x01c4:
        if (r3 == 0) goto L_0x01c9;
    L_0x01c6:
        r3.close();
    L_0x01c9:
        throw r0;
    L_0x01ca:
        r1 = r1.getParent();
        r0 = 1;
        r7 = r0;
        goto L_0x00c3;
    L_0x01d2:
        r3 = new org.mortbay.xml.XmlParser;
        r0 = 0;
        r3.<init>(r0);
        r0 = class$org$mortbay$jetty$webapp$TagLibConfiguration;
        if (r0 != 0) goto L_0x02af;
    L_0x01dc:
        r0 = "org.mortbay.jetty.webapp.TagLibConfiguration";
        r0 = class$(r0);
        class$org$mortbay$jetty$webapp$TagLibConfiguration = r0;
    L_0x01e4:
        r1 = "web-jsptaglib_1_1.dtd";
        r2 = "javax/servlet/jsp/resources/web-jsptaglibrary_1_1.dtd";
        r4 = 0;
        r0 = org.mortbay.util.Loader.getResource(r0, r2, r4);
        r3.redirectEntity(r1, r0);
        r0 = class$org$mortbay$jetty$webapp$TagLibConfiguration;
        if (r0 != 0) goto L_0x02b3;
    L_0x01f4:
        r0 = "org.mortbay.jetty.webapp.TagLibConfiguration";
        r0 = class$(r0);
        class$org$mortbay$jetty$webapp$TagLibConfiguration = r0;
    L_0x01fc:
        r1 = "web-jsptaglib_1_2.dtd";
        r2 = "javax/servlet/jsp/resources/web-jsptaglibrary_1_2.dtd";
        r4 = 0;
        r0 = org.mortbay.util.Loader.getResource(r0, r2, r4);
        r3.redirectEntity(r1, r0);
        r0 = class$org$mortbay$jetty$webapp$TagLibConfiguration;
        if (r0 != 0) goto L_0x02b7;
    L_0x020c:
        r0 = "org.mortbay.jetty.webapp.TagLibConfiguration";
        r0 = class$(r0);
        class$org$mortbay$jetty$webapp$TagLibConfiguration = r0;
    L_0x0214:
        r1 = "web-jsptaglib_2_0.xsd";
        r2 = "javax/servlet/jsp/resources/web-jsptaglibrary_2_0.xsd";
        r4 = 0;
        r0 = org.mortbay.util.Loader.getResource(r0, r2, r4);
        r3.redirectEntity(r1, r0);
        r0 = class$org$mortbay$jetty$webapp$TagLibConfiguration;
        if (r0 != 0) goto L_0x02bb;
    L_0x0224:
        r0 = "org.mortbay.jetty.webapp.TagLibConfiguration";
        r0 = class$(r0);
        class$org$mortbay$jetty$webapp$TagLibConfiguration = r0;
    L_0x022c:
        r1 = "web-jsptaglibrary_1_1.dtd";
        r2 = "javax/servlet/jsp/resources/web-jsptaglibrary_1_1.dtd";
        r4 = 0;
        r0 = org.mortbay.util.Loader.getResource(r0, r2, r4);
        r3.redirectEntity(r1, r0);
        r0 = class$org$mortbay$jetty$webapp$TagLibConfiguration;
        if (r0 != 0) goto L_0x02bf;
    L_0x023c:
        r0 = "org.mortbay.jetty.webapp.TagLibConfiguration";
        r0 = class$(r0);
        class$org$mortbay$jetty$webapp$TagLibConfiguration = r0;
    L_0x0244:
        r1 = "web-jsptaglibrary_1_2.dtd";
        r2 = "javax/servlet/jsp/resources/web-jsptaglibrary_1_2.dtd";
        r4 = 0;
        r0 = org.mortbay.util.Loader.getResource(r0, r2, r4);
        r3.redirectEntity(r1, r0);
        r0 = class$org$mortbay$jetty$webapp$TagLibConfiguration;
        if (r0 != 0) goto L_0x02c2;
    L_0x0254:
        r0 = "org.mortbay.jetty.webapp.TagLibConfiguration";
        r0 = class$(r0);
        class$org$mortbay$jetty$webapp$TagLibConfiguration = r0;
    L_0x025c:
        r1 = "web-jsptaglibrary_2_0.xsd";
        r2 = "javax/servlet/jsp/resources/web-jsptaglibrary_2_0.xsd";
        r4 = 0;
        r0 = org.mortbay.util.Loader.getResource(r0, r2, r4);
        r3.redirectEntity(r1, r0);
        r0 = "/taglib/listener/listener-class";
        r3.setXpath(r0);
        r4 = r8.iterator();
    L_0x0271:
        r0 = r4.hasNext();
        if (r0 == 0) goto L_0x037d;
    L_0x0277:
        r0 = r4.next();	 Catch:{ Exception -> 0x02aa }
        r0 = (org.mortbay.resource.Resource) r0;	 Catch:{ Exception -> 0x02aa }
        r1 = org.mortbay.log.Log.isDebugEnabled();	 Catch:{ Exception -> 0x02aa }
        if (r1 == 0) goto L_0x0299;
    L_0x0283:
        r1 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x02aa }
        r1.<init>();	 Catch:{ Exception -> 0x02aa }
        r2 = "TLD=";
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x02aa }
        r1 = r1.append(r0);	 Catch:{ Exception -> 0x02aa }
        r1 = r1.toString();	 Catch:{ Exception -> 0x02aa }
        org.mortbay.log.Log.debug(r1);	 Catch:{ Exception -> 0x02aa }
    L_0x0299:
        r1 = r0.getInputStream();	 Catch:{ Exception -> 0x02c5 }
        r1 = r3.parse(r1);	 Catch:{ Exception -> 0x02c5 }
        r2 = r1;
    L_0x02a2:
        if (r2 != 0) goto L_0x0379;
    L_0x02a4:
        r1 = "No TLD root in {}";
        org.mortbay.log.Log.warn(r1, r0);	 Catch:{ Exception -> 0x02aa }
        goto L_0x0271;
    L_0x02aa:
        r0 = move-exception;
        org.mortbay.log.Log.warn(r0);
        goto L_0x0271;
    L_0x02af:
        r0 = class$org$mortbay$jetty$webapp$TagLibConfiguration;
        goto L_0x01e4;
    L_0x02b3:
        r0 = class$org$mortbay$jetty$webapp$TagLibConfiguration;
        goto L_0x01fc;
    L_0x02b7:
        r0 = class$org$mortbay$jetty$webapp$TagLibConfiguration;
        goto L_0x0214;
    L_0x02bb:
        r0 = class$org$mortbay$jetty$webapp$TagLibConfiguration;
        goto L_0x022c;
    L_0x02bf:
        r0 = class$org$mortbay$jetty$webapp$TagLibConfiguration;
        goto L_0x0244;
    L_0x02c2:
        r0 = class$org$mortbay$jetty$webapp$TagLibConfiguration;
        goto L_0x025c;
    L_0x02c5:
        r1 = move-exception;
        r1 = r0.getURL();	 Catch:{ Exception -> 0x02aa }
        r1 = r1.toString();	 Catch:{ Exception -> 0x02aa }
        r1 = r3.parse(r1);	 Catch:{ Exception -> 0x02aa }
        r2 = r1;
        goto L_0x02a2;
    L_0x02d4:
        r0 = r2.size();	 Catch:{ Exception -> 0x02aa }
        if (r1 >= r0) goto L_0x0271;
    L_0x02da:
        r0 = r2.get(r1);	 Catch:{ Exception -> 0x02aa }
        r5 = r0 instanceof org.mortbay.xml.XmlParser.Node;	 Catch:{ Exception -> 0x02aa }
        if (r5 == 0) goto L_0x0327;
    L_0x02e2:
        r0 = (org.mortbay.xml.XmlParser.Node) r0;	 Catch:{ Exception -> 0x02aa }
        r5 = "listener";
        r6 = r0.getTag();	 Catch:{ Exception -> 0x02aa }
        r5 = r5.equals(r6);	 Catch:{ Exception -> 0x02aa }
        if (r5 == 0) goto L_0x0327;
    L_0x02f0:
        r5 = "listener-class";
        r6 = 0;
        r7 = 1;
        r5 = r0.getString(r5, r6, r7);	 Catch:{ Exception -> 0x02aa }
        r0 = org.mortbay.log.Log.isDebugEnabled();	 Catch:{ Exception -> 0x02aa }
        if (r0 == 0) goto L_0x0314;
    L_0x02fe:
        r0 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x02aa }
        r0.<init>();	 Catch:{ Exception -> 0x02aa }
        r6 = "listener=";
        r0 = r0.append(r6);	 Catch:{ Exception -> 0x02aa }
        r0 = r0.append(r5);	 Catch:{ Exception -> 0x02aa }
        r0 = r0.toString();	 Catch:{ Exception -> 0x02aa }
        org.mortbay.log.Log.debug(r0);	 Catch:{ Exception -> 0x02aa }
    L_0x0314:
        r0 = r13.getWebAppContext();	 Catch:{ Exception -> 0x032b, Error -> 0x0350 }
        r0 = r0.loadClass(r5);	 Catch:{ Exception -> 0x032b, Error -> 0x0350 }
        r0 = r0.newInstance();	 Catch:{ Exception -> 0x032b, Error -> 0x0350 }
        r0 = (java.util.EventListener) r0;	 Catch:{ Exception -> 0x032b, Error -> 0x0350 }
        r6 = r13._context;	 Catch:{ Exception -> 0x032b, Error -> 0x0350 }
        r6.addEventListener(r0);	 Catch:{ Exception -> 0x032b, Error -> 0x0350 }
    L_0x0327:
        r0 = r1 + 1;
        r1 = r0;
        goto L_0x02d4;
    L_0x032b:
        r0 = move-exception;
        r6 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x02aa }
        r6.<init>();	 Catch:{ Exception -> 0x02aa }
        r7 = "Could not instantiate listener ";
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x02aa }
        r5 = r6.append(r5);	 Catch:{ Exception -> 0x02aa }
        r6 = ": ";
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x02aa }
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x02aa }
        r5 = r5.toString();	 Catch:{ Exception -> 0x02aa }
        org.mortbay.log.Log.warn(r5);	 Catch:{ Exception -> 0x02aa }
        org.mortbay.log.Log.debug(r0);	 Catch:{ Exception -> 0x02aa }
        goto L_0x0327;
    L_0x0350:
        r0 = move-exception;
        r6 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x02aa }
        r6.<init>();	 Catch:{ Exception -> 0x02aa }
        r7 = "Could not instantiate listener ";
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x02aa }
        r5 = r6.append(r5);	 Catch:{ Exception -> 0x02aa }
        r6 = ": ";
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x02aa }
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x02aa }
        r5 = r5.toString();	 Catch:{ Exception -> 0x02aa }
        org.mortbay.log.Log.warn(r5);	 Catch:{ Exception -> 0x02aa }
        org.mortbay.log.Log.debug(r0);	 Catch:{ Exception -> 0x02aa }
        goto L_0x0327;
    L_0x0375:
        r0 = move-exception;
        r3 = r4;
        goto L_0x019f;
    L_0x0379:
        r0 = 0;
        r1 = r0;
        goto L_0x02d4;
    L_0x037d:
        return;
    L_0x037e:
        r0 = move-exception;
        r3 = r6;
        goto L_0x01c4;
    L_0x0382:
        r0 = move-exception;
        goto L_0x01c4;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.webapp.TagLibConfiguration.configureWebApp():void");
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
