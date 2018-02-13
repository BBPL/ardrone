package org.mortbay.jetty.security;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.StringTokenizer;
import org.mortbay.component.AbstractLifeCycle;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Response;
import org.mortbay.log.Log;
import org.mortbay.resource.Resource;
import org.mortbay.util.Scanner;
import org.mortbay.util.Scanner.BulkListener;

public class HashUserRealm extends AbstractLifeCycle implements UserRealm, SSORealm {
    public static final String __SSO = "org.mortbay.http.SSO";
    private String _config;
    private Resource _configResource;
    private String _realmName;
    private int _refreshInterval = 0;
    protected HashMap _roles = new HashMap(7);
    private Scanner _scanner;
    private SSORealm _ssoRealm;
    protected HashMap _users = new HashMap();

    class C13361 implements FilenameFilter {
        private final HashUserRealm this$0;

        C13361(HashUserRealm hashUserRealm) {
            this.this$0 = hashUserRealm;
        }

        public boolean accept(File file, String str) {
            try {
                return new File(file, str).compareTo(HashUserRealm.access$200(this.this$0).getFile()) == 0;
            } catch (IOException e) {
                return false;
            }
        }
    }

    class C13372 implements BulkListener {
        private final HashUserRealm this$0;

        C13372(HashUserRealm hashUserRealm) {
            this.this$0 = hashUserRealm;
        }

        public void filesChanged(List list) throws Exception {
            if (list != null && !list.isEmpty() && list.size() == 1 && list.get(0).equals(HashUserRealm.access$300(this.this$0))) {
                this.this$0.loadConfig();
            }
        }

        public String toString() {
            return "HashUserRealm$Scanner";
        }
    }

    private class User implements Principal {
        List roles;
        private final HashUserRealm this$0;

        private User(HashUserRealm hashUserRealm) {
            this.this$0 = hashUserRealm;
            this.roles = null;
        }

        User(HashUserRealm hashUserRealm, C13361 c13361) {
            this(hashUserRealm);
        }

        static UserRealm access$100(User user) {
            return user.getUserRealm();
        }

        private UserRealm getUserRealm() {
            return this.this$0;
        }

        public String getName() {
            return "Anonymous";
        }

        public boolean isAuthenticated() {
            return false;
        }

        public String toString() {
            return getName();
        }
    }

    private class KnownUser extends User {
        private Credential _cred;
        private String _userName;
        private final HashUserRealm this$0;

        KnownUser(HashUserRealm hashUserRealm, String str, Credential credential) {
            this.this$0 = hashUserRealm;
            super(hashUserRealm, null);
            this._userName = str;
            this._cred = credential;
        }

        boolean authenticate(Object obj) {
            return this._cred != null && this._cred.check(obj);
        }

        public String getName() {
            return this._userName;
        }

        public boolean isAuthenticated() {
            return true;
        }
    }

    private class WrappedUser extends User {
        private String role;
        private final HashUserRealm this$0;
        private Principal user;

        WrappedUser(HashUserRealm hashUserRealm, Principal principal, String str) {
            this.this$0 = hashUserRealm;
            super(hashUserRealm, null);
            this.user = principal;
            this.role = str;
        }

        public String getName() {
            return new StringBuffer().append("role:").append(this.role).toString();
        }

        Principal getUserPrincipal() {
            return this.user;
        }

        public boolean isAuthenticated() {
            return true;
        }

        public boolean isUserInRole(String str) {
            return this.role.equals(str);
        }
    }

    public HashUserRealm(String str) {
        this._realmName = str;
    }

    public HashUserRealm(String str, String str2) throws IOException {
        this._realmName = str;
        setConfig(str2);
    }

    static Resource access$200(HashUserRealm hashUserRealm) {
        return hashUserRealm._configResource;
    }

    static String access$300(HashUserRealm hashUserRealm) {
        return hashUserRealm._config;
    }

    public void addUserToRole(String str, String str2) {
        synchronized (this) {
            HashSet hashSet = (HashSet) this._roles.get(str2);
            if (hashSet == null) {
                hashSet = new HashSet(11);
                this._roles.put(str2, hashSet);
            }
            hashSet.add(str);
        }
    }

    public Principal authenticate(String str, Object obj, Request request) {
        synchronized (this) {
            KnownUser knownUser = (KnownUser) this._users.get(str);
        }
        if (knownUser == null) {
            return null;
        }
        if (!knownUser.authenticate(obj)) {
            knownUser = null;
        }
        return knownUser;
    }

    public void clearSingleSignOn(String str) {
        if (this._ssoRealm != null) {
            this._ssoRealm.clearSingleSignOn(str);
        }
    }

    public void disassociate(Principal principal) {
    }

    protected void doStart() throws Exception {
        super.doStart();
        if (this._scanner != null) {
            this._scanner.stop();
        }
        if (getRefreshInterval() > 0) {
            this._scanner = new Scanner();
            this._scanner.setScanInterval(getRefreshInterval());
            List arrayList = new ArrayList(1);
            arrayList.add(this._configResource.getFile());
            this._scanner.setScanDirs(arrayList);
            this._scanner.setFilenameFilter(new C13361(this));
            this._scanner.addListener(new C13372(this));
            this._scanner.setReportExistingFilesOnStartup(false);
            this._scanner.setRecursive(false);
            this._scanner.start();
        }
    }

    protected void doStop() throws Exception {
        super.doStop();
        if (this._scanner != null) {
            this._scanner.stop();
        }
        this._scanner = null;
    }

    public void dump(PrintStream printStream) {
        printStream.println(new StringBuffer().append(this).append(":").toString());
        printStream.println(super.toString());
        printStream.println(this._roles);
    }

    public String getConfig() {
        return this._config;
    }

    public Resource getConfigResource() {
        return this._configResource;
    }

    public String getName() {
        return this._realmName;
    }

    public Principal getPrincipal(String str) {
        return (Principal) this._users.get(str);
    }

    public int getRefreshInterval() {
        return this._refreshInterval;
    }

    public SSORealm getSSORealm() {
        return this._ssoRealm;
    }

    public Credential getSingleSignOn(Request request, Response response) {
        return this._ssoRealm != null ? this._ssoRealm.getSingleSignOn(request, response) : null;
    }

    public boolean isUserInRole(Principal principal, String str) {
        boolean isUserInRole;
        synchronized (this) {
            if (principal instanceof WrappedUser) {
                isUserInRole = ((WrappedUser) principal).isUserInRole(str);
            } else {
                if (principal != null) {
                    if ((principal instanceof User) && User.access$100((User) principal) == this) {
                        HashSet hashSet = (HashSet) this._roles.get(str);
                        isUserInRole = hashSet != null && hashSet.contains(principal.getName());
                    }
                }
                isUserInRole = false;
            }
        }
        return isUserInRole;
    }

    protected void loadConfig() throws IOException {
        synchronized (this) {
            this._users.clear();
            this._roles.clear();
            if (Log.isDebugEnabled()) {
                Log.debug(new StringBuffer().append("Load ").append(this).append(" from ").append(this._config).toString());
            }
            Properties properties = new Properties();
            properties.load(this._configResource.getInputStream());
            for (Entry entry : properties.entrySet()) {
                String trim = entry.getKey().toString().trim();
                String trim2 = entry.getValue().toString().trim();
                String str = null;
                int indexOf = trim2.indexOf(44);
                if (indexOf > 0) {
                    str = trim2.substring(indexOf + 1).trim();
                    trim2 = trim2.substring(0, indexOf).trim();
                }
                if (trim != null && trim.length() > 0 && trim2 != null && trim2.length() > 0) {
                    put(trim, trim2);
                    if (str != null && str.length() > 0) {
                        StringTokenizer stringTokenizer = new StringTokenizer(str, ", ");
                        while (stringTokenizer.hasMoreTokens()) {
                            addUserToRole(trim, stringTokenizer.nextToken());
                        }
                    }
                }
            }
        }
    }

    public void logout(Principal principal) {
    }

    public Principal popRole(Principal principal) {
        return ((WrappedUser) principal).getUserPrincipal();
    }

    public Principal pushRole(Principal principal, String str) {
        if (principal == null) {
            principal = new User(this, null);
        }
        return new WrappedUser(this, principal, str);
    }

    public Object put(Object obj, Object obj2) {
        Object put;
        synchronized (this) {
            put = obj2 instanceof Principal ? this._users.put(obj.toString(), obj2) : obj2 instanceof Password ? this._users.put(obj, new KnownUser(this, obj.toString(), (Password) obj2)) : obj2 != null ? this._users.put(obj, new KnownUser(this, obj.toString(), Credential.getCredential(obj2.toString()))) : null;
        }
        return put;
    }

    public boolean reauthenticate(Principal principal) {
        return ((User) principal).isAuthenticated();
    }

    public void setConfig(String str) throws IOException {
        this._config = str;
        this._configResource = Resource.newResource(this._config);
        loadConfig();
    }

    public void setName(String str) {
        this._realmName = str;
    }

    public void setRefreshInterval(int i) {
        this._refreshInterval = i;
    }

    public void setSSORealm(SSORealm sSORealm) {
        this._ssoRealm = sSORealm;
    }

    public void setSingleSignOn(Request request, Response response, Principal principal, Credential credential) {
        if (this._ssoRealm != null) {
            this._ssoRealm.setSingleSignOn(request, response, principal, credential);
        }
    }

    public String toString() {
        return new StringBuffer().append("Realm[").append(this._realmName).append("]==").append(this._users.keySet()).toString();
    }
}
