package org.mortbay.jetty.security;

import java.security.Principal;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Random;
import javax.servlet.http.Cookie;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Response;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.log.Log;
import org.mortbay.util.URIUtil;

public class HashSSORealm implements SSORealm {
    public static final String SSO_COOKIE_NAME = "SSO_ID";
    private transient Random _random = new SecureRandom();
    private HashMap _ssoId2Principal = new HashMap();
    private HashMap _ssoPrincipal2Credential = new HashMap();
    private HashMap _ssoUsername2Id = new HashMap();

    public void clearSingleSignOn(String str) {
        synchronized (this._ssoId2Principal) {
            this._ssoPrincipal2Credential.remove(this._ssoId2Principal.remove(this._ssoUsername2Id.remove(str)));
        }
    }

    public Credential getSingleSignOn(Request request, Response response) {
        String value;
        Cookie[] cookies = request.getCookies();
        int i = 0;
        while (cookies != null && i < cookies.length) {
            if (cookies[i].getName().equals(SSO_COOKIE_NAME)) {
                value = cookies[i].getValue();
                break;
            }
            i++;
        }
        value = null;
        if (Log.isDebugEnabled()) {
            Log.debug(new StringBuffer().append("get ssoID=").append(value).toString());
        }
        synchronized (this._ssoId2Principal) {
            Principal principal = (Principal) this._ssoId2Principal.get(value);
            Credential credential = (Credential) this._ssoPrincipal2Credential.get(principal);
        }
        if (Log.isDebugEnabled()) {
            Log.debug(new StringBuffer().append("SSO principal=").append(principal).toString());
        }
        if (!(principal == null || credential == null)) {
            Principal authenticate = ((WebAppContext) request.getContext().getContextHandler()).getSecurityHandler().getUserRealm().authenticate(principal.getName(), credential, request);
            if (authenticate != null) {
                request.setUserPrincipal(authenticate);
                return credential;
            }
            synchronized (this._ssoId2Principal) {
                this._ssoId2Principal.remove(value);
                this._ssoPrincipal2Credential.remove(principal);
                this._ssoUsername2Id.remove(principal.getName());
            }
        }
        return null;
    }

    public void setSingleSignOn(Request request, Response response, Principal principal, Credential credential) {
        String l;
        synchronized (this._ssoId2Principal) {
            do {
                l = Long.toString(Math.abs(this._random.nextLong()), ((int) (System.currentTimeMillis() % 7)) + 30);
            } while (this._ssoId2Principal.containsKey(l));
            if (Log.isDebugEnabled()) {
                Log.debug(new StringBuffer().append("set ssoID=").append(l).toString());
            }
            this._ssoId2Principal.put(l, principal);
            this._ssoPrincipal2Credential.put(principal, credential);
            this._ssoUsername2Id.put(principal.getName(), l);
        }
        Cookie cookie = new Cookie(SSO_COOKIE_NAME, l);
        cookie.setPath(URIUtil.SLASH);
        response.addCookie(cookie);
    }
}
