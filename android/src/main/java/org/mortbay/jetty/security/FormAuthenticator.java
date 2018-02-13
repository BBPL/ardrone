package org.mortbay.jetty.security;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Response;
import org.mortbay.log.Log;
import org.mortbay.util.StringUtil;
import org.mortbay.util.URIUtil;

public class FormAuthenticator implements Authenticator {
    public static final String __J_AUTHENTICATED = "org.mortbay.jetty.Auth";
    public static final String __J_PASSWORD = "j_password";
    public static final String __J_SECURITY_CHECK = "/j_security_check";
    public static final String __J_URI = "org.mortbay.jetty.URI";
    public static final String __J_USERNAME = "j_username";
    private String _formErrorPage;
    private String _formErrorPath;
    private String _formLoginPage;
    private String _formLoginPath;

    static class C13351 {
    }

    private static class FormCredential implements Serializable, HttpSessionBindingListener {
        String _jPassword;
        String _jUserName;
        transient UserRealm _realm;
        transient Principal _userPrincipal;

        private FormCredential() {
        }

        FormCredential(C13351 c13351) {
            this();
        }

        void authenticate(UserRealm userRealm, String str, String str2, Request request) {
            this._jUserName = str;
            this._jPassword = str2;
            this._userPrincipal = userRealm.authenticate(str, str2, request);
            if (this._userPrincipal != null) {
                this._realm = userRealm;
                return;
            }
            Log.warn("AUTH FAILURE: user {}", StringUtil.printable(str));
            request.setUserPrincipal(null);
        }

        void authenticate(UserRealm userRealm, Request request) {
            this._userPrincipal = userRealm.authenticate(this._jUserName, this._jPassword, request);
            if (this._userPrincipal != null) {
                this._realm = userRealm;
                return;
            }
            Log.warn("AUTH FAILURE: user {}", StringUtil.printable(this._jUserName));
            request.setUserPrincipal(null);
        }

        public boolean equals(Object obj) {
            if (obj instanceof FormCredential) {
                FormCredential formCredential = (FormCredential) obj;
                if (this._jUserName.equals(formCredential._jUserName) && this._jPassword.equals(formCredential._jPassword)) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return this._jUserName.hashCode() + this._jPassword.hashCode();
        }

        public String toString() {
            return new StringBuffer().append("Cred[").append(this._jUserName).append("]").toString();
        }

        public void valueBound(HttpSessionBindingEvent httpSessionBindingEvent) {
        }

        public void valueUnbound(HttpSessionBindingEvent httpSessionBindingEvent) {
            if (Log.isDebugEnabled()) {
                Log.debug(new StringBuffer().append("Logout ").append(this._jUserName).toString());
            }
            if (this._realm instanceof SSORealm) {
                ((SSORealm) this._realm).clearSingleSignOn(this._jUserName);
            }
            if (this._realm != null && this._userPrincipal != null) {
                this._realm.logout(this._userPrincipal);
            }
        }
    }

    public Principal authenticate(UserRealm userRealm, String str, Request request, Response response) throws IOException {
        HttpSession session = request.getSession(response != null);
        if (session == null) {
            return null;
        }
        if (isJSecurityCheck(str)) {
            FormCredential formCredential = new FormCredential(null);
            formCredential.authenticate(userRealm, request.getParameter(__J_USERNAME), request.getParameter(__J_PASSWORD), request);
            String str2 = (String) session.getAttribute(__J_URI);
            if (str2 == null || str2.length() == 0) {
                str2 = request.getContextPath();
                if (str2.length() == 0) {
                    str2 = URIUtil.SLASH;
                }
            }
            if (formCredential._userPrincipal != null) {
                if (Log.isDebugEnabled()) {
                    Log.debug(new StringBuffer().append("Form authentication OK for ").append(formCredential._jUserName).toString());
                }
                session.removeAttribute(__J_URI);
                request.setAuthType(Constraint.__FORM_AUTH);
                request.setUserPrincipal(formCredential._userPrincipal);
                session.setAttribute(__J_AUTHENTICATED, formCredential);
                if (userRealm instanceof SSORealm) {
                    ((SSORealm) userRealm).setSingleSignOn(request, response, formCredential._userPrincipal, new Password(formCredential._jPassword));
                }
                if (response != null) {
                    response.setContentLength(0);
                    response.sendRedirect(response.encodeRedirectURL(str2));
                }
            } else {
                if (Log.isDebugEnabled()) {
                    Log.debug(new StringBuffer().append("Form authentication FAILED for ").append(StringUtil.printable(formCredential._jUserName)).toString());
                }
                if (response != null) {
                    if (this._formErrorPage == null) {
                        response.sendError(403);
                    } else {
                        response.setContentLength(0);
                        response.sendRedirect(response.encodeRedirectURL(URIUtil.addPaths(request.getContextPath(), this._formErrorPage)));
                    }
                }
            }
            return null;
        }
        FormCredential formCredential2 = (FormCredential) session.getAttribute(__J_AUTHENTICATED);
        if (formCredential2 != null) {
            if (formCredential2._userPrincipal == null) {
                formCredential2.authenticate(userRealm, request);
                if (formCredential2._userPrincipal != null && (userRealm instanceof SSORealm)) {
                    ((SSORealm) userRealm).setSingleSignOn(request, response, formCredential2._userPrincipal, new Password(formCredential2._jPassword));
                }
            } else if (!userRealm.reauthenticate(formCredential2._userPrincipal)) {
                formCredential2._userPrincipal = null;
            }
            if (formCredential2._userPrincipal != null) {
                if (Log.isDebugEnabled()) {
                    Log.debug(new StringBuffer().append("FORM Authenticated for ").append(formCredential2._userPrincipal.getName()).toString());
                }
                request.setAuthType(Constraint.__FORM_AUTH);
                request.setUserPrincipal(formCredential2._userPrincipal);
                return formCredential2._userPrincipal;
            }
            session.setAttribute(__J_AUTHENTICATED, null);
        } else if (userRealm instanceof SSORealm) {
            Credential singleSignOn = ((SSORealm) userRealm).getSingleSignOn(request, response);
            if (request.getUserPrincipal() != null) {
                FormCredential formCredential3 = new FormCredential(null);
                formCredential3._userPrincipal = request.getUserPrincipal();
                formCredential3._jUserName = formCredential3._userPrincipal.getName();
                if (singleSignOn != null) {
                    formCredential3._jPassword = singleSignOn.toString();
                }
                if (Log.isDebugEnabled()) {
                    Log.debug(new StringBuffer().append("SSO for ").append(formCredential3._userPrincipal).toString());
                }
                request.setAuthType(Constraint.__FORM_AUTH);
                session.setAttribute(__J_AUTHENTICATED, formCredential3);
                return formCredential3._userPrincipal;
            }
        }
        if (isLoginOrErrorPage(str)) {
            return SecurityHandler.__NOBODY;
        }
        if (response != null) {
            if (request.getQueryString() != null) {
                str = new StringBuffer().append(str).append("?").append(request.getQueryString()).toString();
            }
            session.setAttribute(__J_URI, new StringBuffer().append(request.getScheme()).append("://").append(request.getServerName()).append(":").append(request.getServerPort()).append(URIUtil.addPaths(request.getContextPath(), str)).toString());
            response.setContentLength(0);
            response.sendRedirect(response.encodeRedirectURL(URIUtil.addPaths(request.getContextPath(), this._formLoginPage)));
        }
        return null;
    }

    public String getAuthMethod() {
        return Constraint.__FORM_AUTH;
    }

    public String getErrorPage() {
        return this._formErrorPage;
    }

    public String getLoginPage() {
        return this._formLoginPage;
    }

    public boolean isJSecurityCheck(String str) {
        int indexOf = str.indexOf(__J_SECURITY_CHECK);
        if (indexOf >= 0) {
            indexOf += __J_SECURITY_CHECK.length();
            if (indexOf == str.length()) {
                return true;
            }
            char charAt = str.charAt(indexOf);
            if (charAt == ';' || charAt == '#' || charAt == '/') {
                return true;
            }
            if (charAt == '?') {
                return true;
            }
        }
        return false;
    }

    public boolean isLoginOrErrorPage(String str) {
        return str != null && (str.equals(this._formErrorPath) || str.equals(this._formLoginPath));
    }

    public void setErrorPage(String str) {
        if (str == null || str.trim().length() == 0) {
            this._formErrorPath = null;
            this._formErrorPage = null;
            return;
        }
        if (!str.startsWith(URIUtil.SLASH)) {
            Log.warn("form-error-page must start with /");
            str = new StringBuffer().append(URIUtil.SLASH).append(str).toString();
        }
        this._formErrorPage = str;
        this._formErrorPath = str;
        if (this._formErrorPath != null && this._formErrorPath.indexOf(63) > 0) {
            this._formErrorPath = this._formErrorPath.substring(0, this._formErrorPath.indexOf(63));
        }
    }

    public void setLoginPage(String str) {
        if (!str.startsWith(URIUtil.SLASH)) {
            Log.warn("form-login-page must start with /");
            str = new StringBuffer().append(URIUtil.SLASH).append(str).toString();
        }
        this._formLoginPage = str;
        this._formLoginPath = str;
        if (this._formLoginPath.indexOf(63) > 0) {
            this._formLoginPath = this._formLoginPath.substring(0, this._formLoginPath.indexOf(63));
        }
    }
}
