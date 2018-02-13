package org.mortbay.jetty.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.language.bm.Languages;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.HttpVersions;
import org.mortbay.jetty.Request;
import org.mortbay.log.Log;
import org.mortbay.log.Logger;
import org.mortbay.resource.Resource;

public class HTAccessHandler extends SecurityHandler {
    static Class class$org$mortbay$jetty$security$HTAccessHandler;
    private static Logger log;
    String _accessFile = ".htaccess";
    String _default = null;
    transient HashMap _htCache = new HashMap();
    private Handler protegee;

    class DummyPrincipal implements Principal {
        private String _userName;
        private final HTAccessHandler this$0;

        public DummyPrincipal(HTAccessHandler hTAccessHandler, String str) {
            this.this$0 = hTAccessHandler;
            this._userName = str;
        }

        public String getName() {
            return this._userName;
        }

        public String toString() {
            return getName();
        }
    }

    private static class HTAccess {
        static final int ALL = 1;
        static final int ANY = 0;
        static final String GROUP = "group";
        static final String USER = "user";
        static final String VALID_USER = "valid-user";
        ArrayList _allowList = new ArrayList();
        ArrayList _denyList = new ArrayList();
        boolean _forbidden = false;
        String _groupFile;
        long _groupModified;
        Resource _groupResource;
        HashMap _groups = null;
        long _lastModified;
        HashMap _methods = new HashMap();
        String _name;
        int _order;
        HashSet _requireEntities = new HashSet();
        String _requireName;
        int _satisfy = 0;
        String _type;
        String _userFile;
        long _userModified;
        Resource _userResource;
        HashMap _users = null;

        public HTAccess(Resource resource) {
            Throwable e;
            try {
                try {
                    parse(new BufferedReader(new InputStreamReader(resource.getInputStream())));
                    this._lastModified = resource.lastModified();
                    if (this._userFile != null) {
                        this._userResource = Resource.newResource(this._userFile);
                        if (!this._userResource.exists()) {
                            this._forbidden = true;
                            HTAccessHandler.access$000().warn(new StringBuffer().append("Could not find ht user file: ").append(this._userFile).toString(), null, null);
                        } else if (HTAccessHandler.access$000().isDebugEnabled()) {
                            HTAccessHandler.access$000().debug(new StringBuffer().append("user file: ").append(this._userResource).toString(), null, null);
                        }
                    }
                    if (this._groupFile != null) {
                        this._groupResource = Resource.newResource(this._groupFile);
                        if (!this._groupResource.exists()) {
                            this._forbidden = true;
                            HTAccessHandler.access$000().warn(new StringBuffer().append("Could not find ht group file: ").append(this._groupResource).toString(), null, null);
                        } else if (HTAccessHandler.access$000().isDebugEnabled()) {
                            HTAccessHandler.access$000().debug(new StringBuffer().append("group file: ").append(this._groupResource).toString(), null, null);
                        }
                    }
                } catch (IOException e2) {
                    e = e2;
                    this._forbidden = true;
                    HTAccessHandler.access$000().warn("LogSupport.EXCEPTION", e);
                }
            } catch (IOException e3) {
                e = e3;
                this._forbidden = true;
                HTAccessHandler.access$000().warn("LogSupport.EXCEPTION", e);
            }
        }

        private String getUserCode(String str) {
            Throwable e;
            Logger access$000;
            Throwable e2;
            String str2 = null;
            if (this._userResource == null) {
                return null;
            }
            if (this._users == null || this._userModified != this._userResource.lastModified()) {
                if (HTAccessHandler.access$000().isDebugEnabled()) {
                    HTAccessHandler.access$000().debug(new StringBuffer().append("LOAD ").append(this._userResource).toString(), null, null);
                }
                this._users = new HashMap();
                BufferedReader bufferedReader;
                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(this._userResource.getInputStream()));
                    try {
                        this._userModified = this._userResource.lastModified();
                        while (true) {
                            String readLine = bufferedReader.readLine();
                            if (readLine == null) {
                                break;
                            }
                            readLine = readLine.trim();
                            if (!readLine.startsWith("#")) {
                                int indexOf = readLine.indexOf(58);
                                if (indexOf >= 0) {
                                    this._users.put(readLine.substring(0, indexOf).trim(), readLine.substring(indexOf + 1).trim());
                                }
                            }
                        }
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e3) {
                                e = e3;
                                access$000 = HTAccessHandler.access$000();
                                access$000.warn("LogSupport.EXCEPTION", e);
                                return (String) this._users.get(str);
                            }
                        }
                    } catch (IOException e4) {
                        e2 = e4;
                        try {
                            HTAccessHandler.access$000().warn("LogSupport.EXCEPTION", e2);
                            if (bufferedReader != null) {
                                try {
                                    bufferedReader.close();
                                } catch (IOException e5) {
                                    e = e5;
                                    access$000 = HTAccessHandler.access$000();
                                    access$000.warn("LogSupport.EXCEPTION", e);
                                    return (String) this._users.get(str);
                                }
                            }
                            return (String) this._users.get(str);
                        } catch (Throwable th) {
                            e2 = th;
                            Object obj = bufferedReader;
                            bufferedReader = str2;
                            if (bufferedReader != null) {
                                try {
                                    bufferedReader.close();
                                } catch (Throwable e6) {
                                    HTAccessHandler.access$000().warn("LogSupport.EXCEPTION", e6);
                                }
                            }
                            throw e2;
                        }
                    } catch (Throwable th2) {
                        e2 = th2;
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                        throw e2;
                    }
                } catch (IOException e7) {
                    e2 = e7;
                    bufferedReader = null;
                    HTAccessHandler.access$000().warn("LogSupport.EXCEPTION", e2);
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    return (String) this._users.get(str);
                } catch (Throwable th3) {
                    e2 = th3;
                    bufferedReader = str2;
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    throw e2;
                }
            }
            return (String) this._users.get(str);
        }

        private ArrayList getUserGroups(String str) {
            BufferedReader bufferedReader;
            Throwable e;
            Logger access$000;
            Throwable e2;
            ArrayList arrayList = null;
            if (this._groupResource == null) {
                return null;
            }
            if (this._groups == null || this._groupModified != this._groupResource.lastModified()) {
                if (HTAccessHandler.access$000().isDebugEnabled()) {
                    HTAccessHandler.access$000().debug(new StringBuffer().append("LOAD ").append(this._groupResource).toString(), null, null);
                }
                this._groups = new HashMap();
                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(this._groupResource.getInputStream()));
                    try {
                        this._groupModified = this._groupResource.lastModified();
                        while (true) {
                            String readLine = bufferedReader.readLine();
                            if (readLine == null) {
                                break;
                            }
                            readLine = readLine.trim();
                            if (!(readLine.startsWith("#") || readLine.length() == 0)) {
                                StringTokenizer stringTokenizer = new StringTokenizer(readLine, ": \t");
                                if (stringTokenizer.hasMoreTokens()) {
                                    String nextToken = stringTokenizer.nextToken();
                                    if (stringTokenizer.hasMoreTokens()) {
                                        while (stringTokenizer.hasMoreTokens()) {
                                            String nextToken2 = stringTokenizer.nextToken();
                                            ArrayList arrayList2 = (ArrayList) this._groups.get(nextToken2);
                                            if (arrayList2 == null) {
                                                arrayList2 = new ArrayList();
                                                this._groups.put(nextToken2, arrayList2);
                                            }
                                            arrayList2.add(nextToken);
                                        }
                                    }
                                }
                            }
                        }
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e3) {
                                e = e3;
                                access$000 = HTAccessHandler.access$000();
                                access$000.warn("LogSupport.EXCEPTION", e);
                                return (ArrayList) this._groups.get(str);
                            }
                        }
                    } catch (IOException e4) {
                        e2 = e4;
                        try {
                            HTAccessHandler.access$000().warn("LogSupport.EXCEPTION", e2);
                            if (bufferedReader != null) {
                                try {
                                    bufferedReader.close();
                                } catch (IOException e5) {
                                    e = e5;
                                    access$000 = HTAccessHandler.access$000();
                                    access$000.warn("LogSupport.EXCEPTION", e);
                                    return (ArrayList) this._groups.get(str);
                                }
                            }
                            return (ArrayList) this._groups.get(str);
                        } catch (Throwable th) {
                            e2 = th;
                            Object obj = bufferedReader;
                            bufferedReader = arrayList;
                            if (bufferedReader != null) {
                                try {
                                    bufferedReader.close();
                                } catch (Throwable e6) {
                                    HTAccessHandler.access$000().warn("LogSupport.EXCEPTION", e6);
                                }
                            }
                            throw e2;
                        }
                    } catch (Throwable th2) {
                        e2 = th2;
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                        throw e2;
                    }
                } catch (IOException e7) {
                    e2 = e7;
                    bufferedReader = null;
                    HTAccessHandler.access$000().warn("LogSupport.EXCEPTION", e2);
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    return (ArrayList) this._groups.get(str);
                } catch (Throwable th3) {
                    e2 = th3;
                    bufferedReader = arrayList;
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    throw e2;
                }
            }
            return (ArrayList) this._groups.get(str);
        }

        private void parse(BufferedReader bufferedReader) throws IOException {
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    String trim = readLine.trim();
                    if (!trim.startsWith("#")) {
                        if (trim.startsWith("AuthUserFile")) {
                            this._userFile = trim.substring(13).trim();
                        } else if (trim.startsWith("AuthGroupFile")) {
                            this._groupFile = trim.substring(14).trim();
                        } else if (trim.startsWith("AuthName")) {
                            this._name = trim.substring(8).trim();
                        } else if (trim.startsWith("AuthType")) {
                            this._type = trim.substring(8).trim();
                        } else if (trim.startsWith("<Limit")) {
                            int length = trim.length();
                            int indexOf = trim.indexOf(62);
                            if (indexOf >= 0) {
                                length = indexOf;
                            }
                            StringTokenizer stringTokenizer = new StringTokenizer(trim.substring(6, length));
                            while (stringTokenizer.hasMoreTokens()) {
                                this._methods.put(stringTokenizer.nextToken(), Boolean.TRUE);
                            }
                            while (true) {
                                readLine = bufferedReader.readLine();
                                if (readLine == null) {
                                    break;
                                }
                                trim = readLine.trim();
                                if (!trim.startsWith("#")) {
                                    int length2;
                                    if (trim.startsWith("satisfy")) {
                                        length2 = trim.length();
                                        indexOf = 7;
                                        while (indexOf < length2 && trim.charAt(indexOf) <= ' ') {
                                            indexOf++;
                                        }
                                        length = indexOf;
                                        while (length < length2 && trim.charAt(length) > ' ') {
                                            length++;
                                        }
                                        readLine = trim.substring(indexOf, length);
                                        if (readLine.equals("all")) {
                                            this._satisfy = 1;
                                        } else if (readLine.equals(Languages.ANY)) {
                                            this._satisfy = 0;
                                        }
                                    } else if (trim.startsWith("require")) {
                                        length2 = trim.length();
                                        indexOf = 7;
                                        while (indexOf < length2 && trim.charAt(indexOf) <= ' ') {
                                            indexOf++;
                                        }
                                        length = indexOf;
                                        while (length < length2 && trim.charAt(length) > ' ') {
                                            length++;
                                        }
                                        this._requireName = trim.substring(indexOf, length).toLowerCase();
                                        if (USER.equals(this._requireName)) {
                                            this._requireName = USER;
                                        } else if (GROUP.equals(this._requireName)) {
                                            this._requireName = GROUP;
                                        } else if (VALID_USER.equals(this._requireName)) {
                                            this._requireName = VALID_USER;
                                        }
                                        length++;
                                        if (length < length2) {
                                            while (length < length2 && trim.charAt(length) <= ' ') {
                                                length++;
                                            }
                                            stringTokenizer = new StringTokenizer(trim.substring(length));
                                            while (stringTokenizer.hasMoreTokens()) {
                                                this._requireEntities.add(stringTokenizer.nextToken());
                                            }
                                        }
                                    } else if (trim.startsWith("order")) {
                                        if (HTAccessHandler.access$000().isDebugEnabled()) {
                                            HTAccessHandler.access$000().debug(new StringBuffer().append("orderline=").append(trim).append("order=").append(this._order).toString(), null, null);
                                        }
                                        if (trim.indexOf("allow,deny") > 0) {
                                            HTAccessHandler.access$000().debug("==>allow+deny", null, null);
                                            this._order = 1;
                                        } else if (trim.indexOf("deny,allow") > 0) {
                                            HTAccessHandler.access$000().debug("==>deny,allow", null, null);
                                            this._order = -1;
                                        } else if (trim.indexOf("mutual-failure") > 0) {
                                            HTAccessHandler.access$000().debug("==>mutual", null, null);
                                            this._order = 0;
                                        }
                                    } else if (trim.startsWith("allow from")) {
                                        length = 10;
                                        indexOf = trim.length();
                                        while (length < indexOf && trim.charAt(length) <= ' ') {
                                            length++;
                                        }
                                        if (HTAccessHandler.access$000().isDebugEnabled()) {
                                            HTAccessHandler.access$000().debug(new StringBuffer().append("allow process:").append(trim.substring(length)).toString(), null, null);
                                        }
                                        stringTokenizer = new StringTokenizer(trim.substring(length));
                                        while (stringTokenizer.hasMoreTokens()) {
                                            this._allowList.add(stringTokenizer.nextToken());
                                        }
                                    } else if (trim.startsWith("deny from")) {
                                        length = 9;
                                        indexOf = trim.length();
                                        while (length < indexOf && trim.charAt(length) <= ' ') {
                                            length++;
                                        }
                                        if (HTAccessHandler.access$000().isDebugEnabled()) {
                                            HTAccessHandler.access$000().debug(new StringBuffer().append("deny process:").append(trim.substring(length)).toString(), null, null);
                                        }
                                        stringTokenizer = new StringTokenizer(trim.substring(length));
                                        while (stringTokenizer.hasMoreTokens()) {
                                            this._denyList.add(stringTokenizer.nextToken());
                                        }
                                    } else if (trim.startsWith("</Limit>")) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    return;
                }
            }
        }

        public boolean checkAccess(String str, String str2) {
            if (!(this._allowList.size() == 0 && this._denyList.size() == 0)) {
                String str3;
                boolean z;
                boolean z2;
                for (int i = 0; i < this._allowList.size(); i++) {
                    str3 = (String) this._allowList.get(i);
                    if (str3.equals("all")) {
                        z = true;
                        break;
                    }
                    char charAt = str3.charAt(0);
                    if (charAt < '0' || charAt > '9') {
                        if (str.endsWith(str3)) {
                            z = true;
                            break;
                        }
                    } else if (str2.startsWith(str3)) {
                        z = true;
                        break;
                    }
                }
                z = false;
                for (int i2 = 0; i2 < this._denyList.size(); i2++) {
                    str3 = (String) this._denyList.get(i2);
                    if (str3.equals("all")) {
                        z2 = true;
                        break;
                    }
                    char charAt2 = str3.charAt(0);
                    if (charAt2 < '0' || charAt2 > '9') {
                        if (str.endsWith(str3)) {
                            z2 = true;
                            break;
                        }
                    } else if (str2.startsWith(str3)) {
                        z2 = true;
                        break;
                    }
                }
                z2 = false;
                if (this._order < 0) {
                    return !z2 || z;
                } else {
                    if (!z) {
                        return false;
                    }
                    if (z2) {
                        return false;
                    }
                }
            }
            return true;
        }

        public boolean checkAuth(String str, String str2, UserRealm userRealm, Request request) {
            Object obj = null;
            if (this._requireName != null) {
                if ((userRealm == null ? null : userRealm.authenticate(str, str2, request)) == null) {
                    String userCode = getUserCode(str);
                    String substring = userCode != null ? userCode.substring(0, 2) : str;
                    if (!(str == null || str2 == null)) {
                        obj = UnixCrypt.crypt(str2, substring);
                    }
                    if (userCode == null) {
                        return false;
                    }
                    if ((userCode.equals(HttpVersions.HTTP_0_9) && !str2.equals(HttpVersions.HTTP_0_9)) || !userCode.equals(r3)) {
                        return false;
                    }
                }
                if (this._requireName.equalsIgnoreCase(USER)) {
                    if (!this._requireEntities.contains(str)) {
                        return false;
                    }
                } else if (!this._requireName.equalsIgnoreCase(GROUP)) {
                    return this._requireName.equalsIgnoreCase(VALID_USER);
                } else {
                    ArrayList userGroups = getUserGroups(str);
                    if (userGroups == null) {
                        return false;
                    }
                    int size = userGroups.size();
                    while (true) {
                        int i = size - 1;
                        if (size > 0) {
                            if (this._requireEntities.contains(userGroups.get(i))) {
                                break;
                            }
                            size = i;
                        } else {
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        public Resource getGroupResource() {
            return this._groupResource;
        }

        public long getLastModified() {
            return this._lastModified;
        }

        public HashMap getMethods() {
            return this._methods;
        }

        public String getName() {
            return this._name;
        }

        public int getSatisfy() {
            return this._satisfy;
        }

        public String getType() {
            return this._type;
        }

        public Resource getUserResource() {
            return this._userResource;
        }

        public boolean isAccessLimited() {
            return this._allowList.size() > 0 || this._denyList.size() > 0;
        }

        public boolean isAuthLimited() {
            return this._requireName != null;
        }

        public boolean isForbidden() {
            return this._forbidden;
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("AuthUserFile=");
            stringBuffer.append(this._userFile);
            stringBuffer.append(", AuthGroupFile=");
            stringBuffer.append(this._groupFile);
            stringBuffer.append(", AuthName=");
            stringBuffer.append(this._name);
            stringBuffer.append(", AuthType=");
            stringBuffer.append(this._type);
            stringBuffer.append(", Methods=");
            stringBuffer.append(this._methods);
            stringBuffer.append(", satisfy=");
            stringBuffer.append(this._satisfy);
            if (this._order < 0) {
                stringBuffer.append(", order=deny,allow");
            } else if (this._order > 0) {
                stringBuffer.append(", order=allow,deny");
            } else {
                stringBuffer.append(", order=mutual-failure");
            }
            stringBuffer.append(", Allow from=");
            stringBuffer.append(this._allowList);
            stringBuffer.append(", deny from=");
            stringBuffer.append(this._denyList);
            stringBuffer.append(", requireName=");
            stringBuffer.append(this._requireName);
            stringBuffer.append(" ");
            stringBuffer.append(this._requireEntities);
            return stringBuffer.toString();
        }
    }

    static {
        Class class$;
        if (class$org$mortbay$jetty$security$HTAccessHandler == null) {
            class$ = class$("org.mortbay.jetty.security.HTAccessHandler");
            class$org$mortbay$jetty$security$HTAccessHandler = class$;
        } else {
            class$ = class$org$mortbay$jetty$security$HTAccessHandler;
        }
        log = Log.getLogger(class$.getName());
    }

    static Logger access$000() {
        return log;
    }

    private void callWrappedHandler(String str, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException, ServletException {
        Handler handler = getHandler();
        if (handler != null) {
            handler.handle(str, httpServletRequest, httpServletResponse, i);
        }
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    public Principal getPrincipal(String str, UserRealm userRealm) {
        return userRealm == null ? new DummyPrincipal(this, str) : userRealm.getPrincipal(str);
    }

    protected Handler getProtegee() {
        return this.protegee;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handle(java.lang.String r16, javax.servlet.http.HttpServletRequest r17, javax.servlet.http.HttpServletResponse r18, int r19) throws java.io.IOException, javax.servlet.ServletException {
        /*
        r15 = this;
        r0 = r17;
        r2 = r0 instanceof org.mortbay.jetty.Request;
        if (r2 == 0) goto L_0x017a;
    L_0x0006:
        r2 = r17;
        r2 = (org.mortbay.jetty.Request) r2;
        r3 = r2;
    L_0x000b:
        r0 = r18;
        r2 = r0 instanceof org.mortbay.jetty.Response;
        if (r2 == 0) goto L_0x0185;
    L_0x0011:
        r2 = r18;
        r2 = (org.mortbay.jetty.Response) r2;
        r4 = r2;
    L_0x0016:
        r2 = 0;
        r5 = 0;
        r6 = log;
        r6 = r6.isDebugEnabled();
        if (r6 == 0) goto L_0x003c;
    L_0x0020:
        r6 = log;
        r7 = new java.lang.StringBuffer;
        r7.<init>();
        r8 = "HTAccessHandler pathInContext=";
        r7 = r7.append(r8);
        r0 = r16;
        r7 = r7.append(r0);
        r7 = r7.toString();
        r8 = 0;
        r9 = 0;
        r6.debug(r7, r8, r9);
    L_0x003c:
        r6 = "Authorization";
        r0 = r17;
        r6 = r0.getHeader(r6);
        if (r6 == 0) goto L_0x0321;
    L_0x0046:
        r2 = 32;
        r2 = r6.indexOf(r2);
        r2 = r2 + 1;
        r2 = r6.substring(r2);
        r5 = org.mortbay.util.StringUtil.__ISO_8859_1;
        r5 = org.mortbay.jetty.security.B64Code.decode(r2, r5);
        r2 = 58;
        r6 = r5.indexOf(r2);
        r2 = 0;
        r2 = r5.substring(r2, r6);
        r6 = r6 + 1;
        r5 = r5.substring(r6);
        r6 = log;
        r6 = r6.isDebugEnabled();
        if (r6 == 0) goto L_0x031d;
    L_0x0071:
        r6 = log;
        r7 = new java.lang.StringBuffer;
        r7.<init>();
        r8 = "User=";
        r7 = r7.append(r8);
        r7 = r7.append(r2);
        r8 = ", password=";
        r7 = r7.append(r8);
        r8 = "******************************";
        r9 = 0;
        r10 = r5.length();
        r8 = r8.substring(r9, r10);
        r7 = r7.append(r8);
        r7 = r7.toString();
        r8 = 0;
        r9 = 0;
        r6.debug(r7, r8, r9);
        r6 = r5;
        r5 = r2;
    L_0x00a2:
        r8 = 0;
        r7 = 0;
        r2 = "/";
        r0 = r16;
        r2 = r0.endsWith(r2);	 Catch:{ Exception -> 0x0309 }
        if (r2 == 0) goto L_0x0190;
    L_0x00ae:
        r9 = r16;
    L_0x00b0:
        r2 = 0;
        if (r9 == 0) goto L_0x010a;
    L_0x00b3:
        r2 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x0309 }
        r2.<init>();	 Catch:{ Exception -> 0x0309 }
        r2 = r2.append(r9);	 Catch:{ Exception -> 0x0309 }
        r10 = r15._accessFile;	 Catch:{ Exception -> 0x0309 }
        r2 = r2.append(r10);	 Catch:{ Exception -> 0x0309 }
        r10 = r2.toString();	 Catch:{ Exception -> 0x0309 }
        r2 = r15.getProtegee();	 Catch:{ Exception -> 0x0309 }
        r2 = (org.mortbay.jetty.handler.ContextHandler) r2;	 Catch:{ Exception -> 0x0309 }
        r2 = r2.getResource(r10);	 Catch:{ Exception -> 0x0309 }
        r10 = log;	 Catch:{ Exception -> 0x0309 }
        r10 = r10.isDebugEnabled();	 Catch:{ Exception -> 0x0309 }
        if (r10 == 0) goto L_0x00fc;
    L_0x00d8:
        r10 = log;	 Catch:{ Exception -> 0x0309 }
        r11 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x0309 }
        r11.<init>();	 Catch:{ Exception -> 0x0309 }
        r12 = "directory=";
        r11 = r11.append(r12);	 Catch:{ Exception -> 0x0309 }
        r11 = r11.append(r9);	 Catch:{ Exception -> 0x0309 }
        r12 = " resource=";
        r11 = r11.append(r12);	 Catch:{ Exception -> 0x0309 }
        r11 = r11.append(r2);	 Catch:{ Exception -> 0x0309 }
        r11 = r11.toString();	 Catch:{ Exception -> 0x0309 }
        r12 = 0;
        r13 = 0;
        r10.debug(r11, r12, r13);	 Catch:{ Exception -> 0x0309 }
    L_0x00fc:
        if (r2 == 0) goto L_0x0197;
    L_0x00fe:
        r10 = r2.exists();	 Catch:{ Exception -> 0x0309 }
        if (r10 == 0) goto L_0x0197;
    L_0x0104:
        r10 = r2.isDirectory();	 Catch:{ Exception -> 0x0309 }
        if (r10 != 0) goto L_0x0197;
    L_0x010a:
        r9 = 1;
        if (r2 != 0) goto L_0x0318;
    L_0x010d:
        r10 = r15._default;	 Catch:{ Exception -> 0x0309 }
        if (r10 == 0) goto L_0x0318;
    L_0x0111:
        r2 = r15._default;	 Catch:{ Exception -> 0x0309 }
        r2 = org.mortbay.resource.Resource.newResource(r2);	 Catch:{ Exception -> 0x0309 }
        r10 = r2.exists();	 Catch:{ Exception -> 0x0309 }
        if (r10 == 0) goto L_0x0123;
    L_0x011d:
        r10 = r2.isDirectory();	 Catch:{ Exception -> 0x0309 }
        if (r10 == 0) goto L_0x0318;
    L_0x0123:
        r9 = 0;
        r14 = r2;
        r2 = r9;
        r9 = r14;
    L_0x0127:
        if (r9 != 0) goto L_0x012a;
    L_0x0129:
        r2 = 0;
    L_0x012a:
        r10 = r15._accessFile;	 Catch:{ Exception -> 0x0309 }
        r0 = r16;
        r10 = r0.endsWith(r10);	 Catch:{ Exception -> 0x0309 }
        if (r10 != 0) goto L_0x016e;
    L_0x0134:
        r10 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x0309 }
        r10.<init>();	 Catch:{ Exception -> 0x0309 }
        r11 = r15._accessFile;	 Catch:{ Exception -> 0x0309 }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0309 }
        r11 = "~";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0309 }
        r10 = r10.toString();	 Catch:{ Exception -> 0x0309 }
        r0 = r16;
        r10 = r0.endsWith(r10);	 Catch:{ Exception -> 0x0309 }
        if (r10 != 0) goto L_0x016e;
    L_0x0151:
        r10 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x0309 }
        r10.<init>();	 Catch:{ Exception -> 0x0309 }
        r11 = r15._accessFile;	 Catch:{ Exception -> 0x0309 }
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0309 }
        r11 = ".bak";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0309 }
        r10 = r10.toString();	 Catch:{ Exception -> 0x0309 }
        r0 = r16;
        r10 = r0.endsWith(r10);	 Catch:{ Exception -> 0x0309 }
        if (r10 == 0) goto L_0x019e;
    L_0x016e:
        r2 = 403; // 0x193 float:5.65E-43 double:1.99E-321;
        r0 = r18;
        r0.sendError(r2);	 Catch:{ Exception -> 0x0309 }
        r2 = 1;
        r3.setHandled(r2);	 Catch:{ Exception -> 0x0309 }
    L_0x0179:
        return;
    L_0x017a:
        r2 = org.mortbay.jetty.HttpConnection.getCurrentConnection();
        r2 = r2.getRequest();
        r3 = r2;
        goto L_0x000b;
    L_0x0185:
        r2 = org.mortbay.jetty.HttpConnection.getCurrentConnection();
        r2 = r2.getResponse();
        r4 = r2;
        goto L_0x0016;
    L_0x0190:
        r2 = org.mortbay.util.URIUtil.parentPath(r16);	 Catch:{ Exception -> 0x0309 }
        r9 = r2;
        goto L_0x00b0;
    L_0x0197:
        r2 = org.mortbay.util.URIUtil.parentPath(r9);	 Catch:{ Exception -> 0x0309 }
        r9 = r2;
        goto L_0x00b0;
    L_0x019e:
        if (r2 == 0) goto L_0x0316;
    L_0x01a0:
        r2 = log;	 Catch:{ Exception -> 0x0309 }
        r2 = r2.isDebugEnabled();	 Catch:{ Exception -> 0x0309 }
        if (r2 == 0) goto L_0x01c2;
    L_0x01a8:
        r2 = log;	 Catch:{ Exception -> 0x0309 }
        r8 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x0309 }
        r8.<init>();	 Catch:{ Exception -> 0x0309 }
        r10 = "HTACCESS=";
        r8 = r8.append(r10);	 Catch:{ Exception -> 0x0309 }
        r8 = r8.append(r9);	 Catch:{ Exception -> 0x0309 }
        r8 = r8.toString();	 Catch:{ Exception -> 0x0309 }
        r10 = 0;
        r11 = 0;
        r2.debug(r8, r10, r11);	 Catch:{ Exception -> 0x0309 }
    L_0x01c2:
        r2 = r15._htCache;	 Catch:{ Exception -> 0x0309 }
        r2 = r2.get(r9);	 Catch:{ Exception -> 0x0309 }
        r2 = (org.mortbay.jetty.security.HTAccessHandler.HTAccess) r2;	 Catch:{ Exception -> 0x0309 }
        if (r2 == 0) goto L_0x01d8;
    L_0x01cc:
        r10 = r2.getLastModified();	 Catch:{ Exception -> 0x030f }
        r12 = r9.lastModified();	 Catch:{ Exception -> 0x030f }
        r7 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1));
        if (r7 == 0) goto L_0x0205;
    L_0x01d8:
        r7 = new org.mortbay.jetty.security.HTAccessHandler$HTAccess;	 Catch:{ Exception -> 0x030f }
        r7.<init>(r9);	 Catch:{ Exception -> 0x030f }
        r2 = r15._htCache;	 Catch:{ Exception -> 0x030c }
        r2.put(r9, r7);	 Catch:{ Exception -> 0x030c }
        r2 = log;	 Catch:{ Exception -> 0x030c }
        r2 = r2.isDebugEnabled();	 Catch:{ Exception -> 0x030c }
        if (r2 == 0) goto L_0x0204;
    L_0x01ea:
        r2 = log;	 Catch:{ Exception -> 0x030c }
        r8 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x030c }
        r8.<init>();	 Catch:{ Exception -> 0x030c }
        r9 = "HTCache loaded ";
        r8 = r8.append(r9);	 Catch:{ Exception -> 0x030c }
        r8 = r8.append(r7);	 Catch:{ Exception -> 0x030c }
        r8 = r8.toString();	 Catch:{ Exception -> 0x030c }
        r9 = 0;
        r10 = 0;
        r2.debug(r8, r9, r10);	 Catch:{ Exception -> 0x030c }
    L_0x0204:
        r2 = r7;
    L_0x0205:
        r7 = r2.isForbidden();	 Catch:{ Exception -> 0x0232 }
        if (r7 == 0) goto L_0x024b;
    L_0x020b:
        r4 = log;	 Catch:{ Exception -> 0x0232 }
        r5 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x0232 }
        r5.<init>();	 Catch:{ Exception -> 0x0232 }
        r6 = "Mis-configured htaccess: ";
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0232 }
        r5 = r5.append(r2);	 Catch:{ Exception -> 0x0232 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0232 }
        r6 = 0;
        r7 = 0;
        r4.warn(r5, r6, r7);	 Catch:{ Exception -> 0x0232 }
        r4 = 403; // 0x193 float:5.65E-43 double:1.99E-321;
        r0 = r18;
        r0.sendError(r4);	 Catch:{ Exception -> 0x0232 }
        r4 = 1;
        r3.setHandled(r4);	 Catch:{ Exception -> 0x0232 }
        goto L_0x0179;
    L_0x0232:
        r4 = move-exception;
        r7 = r2;
        r2 = r4;
    L_0x0235:
        r4 = log;
        r5 = "Exception";
        r4.warn(r5, r2);
        if (r7 == 0) goto L_0x0179;
    L_0x023e:
        r2 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
        r0 = r18;
        r0.sendError(r2);
        r2 = 1;
        r3.setHandled(r2);
        goto L_0x0179;
    L_0x024b:
        r7 = r2.getMethods();	 Catch:{ Exception -> 0x0232 }
        r8 = r7.size();	 Catch:{ Exception -> 0x0232 }
        if (r8 <= 0) goto L_0x0264;
    L_0x0255:
        r8 = r17.getMethod();	 Catch:{ Exception -> 0x0232 }
        r7 = r7.containsKey(r8);	 Catch:{ Exception -> 0x0232 }
        if (r7 != 0) goto L_0x0264;
    L_0x025f:
        r15.callWrappedHandler(r16, r17, r18, r19);	 Catch:{ Exception -> 0x0232 }
        goto L_0x0179;
    L_0x0264:
        r7 = r2.getSatisfy();	 Catch:{ Exception -> 0x0232 }
        r8 = "";
        r9 = r17.getRemoteAddr();	 Catch:{ Exception -> 0x0232 }
        r8 = r2.checkAccess(r8, r9);	 Catch:{ Exception -> 0x0232 }
        r9 = log;	 Catch:{ Exception -> 0x0232 }
        r9 = r9.isDebugEnabled();	 Catch:{ Exception -> 0x0232 }
        if (r9 == 0) goto L_0x0294;
    L_0x027a:
        r9 = log;	 Catch:{ Exception -> 0x0232 }
        r10 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x0232 }
        r10.<init>();	 Catch:{ Exception -> 0x0232 }
        r11 = "IPValid = ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0232 }
        r10 = r10.append(r8);	 Catch:{ Exception -> 0x0232 }
        r10 = r10.toString();	 Catch:{ Exception -> 0x0232 }
        r11 = 0;
        r12 = 0;
        r9.debug(r10, r11, r12);	 Catch:{ Exception -> 0x0232 }
    L_0x0294:
        r9 = 1;
        if (r8 != r9) goto L_0x029e;
    L_0x0297:
        if (r7 != 0) goto L_0x029e;
    L_0x0299:
        r15.callWrappedHandler(r16, r17, r18, r19);	 Catch:{ Exception -> 0x0232 }
        goto L_0x0179;
    L_0x029e:
        if (r8 != 0) goto L_0x02b0;
    L_0x02a0:
        r8 = 1;
        if (r7 != r8) goto L_0x02b0;
    L_0x02a3:
        r4 = 403; // 0x193 float:5.65E-43 double:1.99E-321;
        r0 = r18;
        r0.sendError(r4);	 Catch:{ Exception -> 0x0232 }
        r4 = 1;
        r3.setHandled(r4);	 Catch:{ Exception -> 0x0232 }
        goto L_0x0179;
    L_0x02b0:
        r7 = r15.getUserRealm();	 Catch:{ Exception -> 0x0232 }
        r6 = r2.checkAuth(r5, r6, r7, r3);	 Catch:{ Exception -> 0x0232 }
        if (r6 != 0) goto L_0x02f1;
    L_0x02ba:
        r5 = log;	 Catch:{ Exception -> 0x0232 }
        r6 = "Auth Failed";
        r7 = 0;
        r8 = 0;
        r5.debug(r6, r7, r8);	 Catch:{ Exception -> 0x0232 }
        r5 = "WWW-Authenticate";
        r6 = new java.lang.StringBuffer;	 Catch:{ Exception -> 0x0232 }
        r6.<init>();	 Catch:{ Exception -> 0x0232 }
        r7 = "basic realm=";
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x0232 }
        r7 = r2.getName();	 Catch:{ Exception -> 0x0232 }
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x0232 }
        r6 = r6.toString();	 Catch:{ Exception -> 0x0232 }
        r0 = r18;
        r0.setHeader(r5, r6);	 Catch:{ Exception -> 0x0232 }
        r5 = 401; // 0x191 float:5.62E-43 double:1.98E-321;
        r0 = r18;
        r0.sendError(r5);	 Catch:{ Exception -> 0x0232 }
        r4.complete();	 Catch:{ Exception -> 0x0232 }
        r4 = 1;
        r3.setHandled(r4);	 Catch:{ Exception -> 0x0232 }
        goto L_0x0179;
    L_0x02f1:
        if (r5 == 0) goto L_0x0314;
    L_0x02f3:
        r4 = "BASIC";
        r3.setAuthType(r4);	 Catch:{ Exception -> 0x0232 }
        r4 = r15.getUserRealm();	 Catch:{ Exception -> 0x0232 }
        r4 = r15.getPrincipal(r5, r4);	 Catch:{ Exception -> 0x0232 }
        r3.setUserPrincipal(r4);	 Catch:{ Exception -> 0x0232 }
        r7 = r2;
    L_0x0304:
        r15.callWrappedHandler(r16, r17, r18, r19);	 Catch:{ Exception -> 0x0309 }
        goto L_0x0179;
    L_0x0309:
        r2 = move-exception;
        goto L_0x0235;
    L_0x030c:
        r2 = move-exception;
        goto L_0x0235;
    L_0x030f:
        r4 = move-exception;
        r7 = r2;
        r2 = r4;
        goto L_0x0235;
    L_0x0314:
        r7 = r2;
        goto L_0x0304;
    L_0x0316:
        r7 = r8;
        goto L_0x0304;
    L_0x0318:
        r14 = r2;
        r2 = r9;
        r9 = r14;
        goto L_0x0127;
    L_0x031d:
        r6 = r5;
        r5 = r2;
        goto L_0x00a2;
    L_0x0321:
        r6 = r5;
        r5 = r2;
        goto L_0x00a2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.security.HTAccessHandler.handle(java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, int):void");
    }

    public void setAccessFile(String str) {
        if (str == null) {
            this._accessFile = ".htaccess";
        } else {
            this._accessFile = str;
        }
    }

    public void setDefault(String str) {
        this._default = str;
    }

    public void setProtegee(Handler handler) {
        this.protegee = handler;
    }
}
