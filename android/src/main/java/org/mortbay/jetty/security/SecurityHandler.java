package org.mortbay.jetty.security;

import java.io.IOException;
import java.security.Principal;
import java.util.Map.Entry;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Response;
import org.mortbay.jetty.handler.HandlerWrapper;
import org.mortbay.jetty.servlet.PathMap;
import org.mortbay.log.Log;
import org.mortbay.util.LazyList;
import org.mortbay.util.StringUtil;

public class SecurityHandler extends HandlerWrapper {
    public static Principal __NOBODY = new C13392();
    public static Principal __NO_USER = new C13381();
    private String _authMethod = Constraint.__BASIC_AUTH;
    private Authenticator _authenticator;
    private boolean _checkWelcomeFiles = false;
    private PathMap _constraintMap = new PathMap();
    private ConstraintMapping[] _constraintMappings;
    private NotChecked _notChecked = new NotChecked(this);
    private UserRealm _userRealm;

    final class C13381 implements Principal {
        C13381() {
        }

        public String getName() {
            return null;
        }

        public String toString() {
            return "No User";
        }
    }

    final class C13392 implements Principal {
        C13392() {
        }

        public String getName() {
            return "Nobody";
        }

        public String toString() {
            return getName();
        }
    }

    public class NotChecked implements Principal {
        private final SecurityHandler this$0;

        public NotChecked(SecurityHandler securityHandler) {
            this.this$0 = securityHandler;
        }

        public String getName() {
            return null;
        }

        public SecurityHandler getSecurityHandler() {
            return this.this$0;
        }

        public String toString() {
            return "NOT CHECKED";
        }
    }

    private boolean check(Object obj, Authenticator authenticator, UserRealm userRealm, String str, Request request, Response response) throws IOException {
        Object obj2;
        String str2;
        Object obj3 = null;
        String str3 = null;
        int i = 0;
        int i2 = 0;
        while (i2 < LazyList.size(obj)) {
            Constraint constraint = (Constraint) LazyList.get(obj, i2);
            if (i <= -1 || !constraint.hasDataConstraint()) {
                i = -1;
            } else if (constraint.getDataConstraint() > i) {
                i = constraint.getDataConstraint();
            }
            if (obj3 != null) {
                obj2 = obj3;
            } else if (!constraint.getAuthenticate()) {
                obj2 = 1;
            } else if (constraint.isAnyRole()) {
                str3 = Constraint.ANY_ROLE;
                obj2 = obj3;
            } else {
                String[] roles = constraint.getRoles();
                if (roles == null || roles.length == 0) {
                    obj2 = 1;
                    break;
                }
                if (str3 != Constraint.ANY_ROLE) {
                    String str4 = str3;
                    int length = roles.length;
                    str2 = str4;
                    while (true) {
                        int i3 = length - 1;
                        if (length <= 0) {
                            break;
                        }
                        str2 = LazyList.add(str2, roles[i3]);
                        length = i3;
                    }
                } else {
                    str2 = str3;
                }
                str3 = str2;
                obj2 = obj3;
            }
            i2++;
            obj3 = obj2;
        }
        obj2 = null;
        if (obj2 == null || ((authenticator instanceof FormAuthenticator) && ((FormAuthenticator) authenticator).isLoginOrErrorPage(str))) {
            if (i > 0) {
                Connector connector = HttpConnection.getCurrentConnection().getConnector();
                switch (i) {
                    case 1:
                        if (!connector.isIntegral(request)) {
                            if (connector.getConfidentialPort() > 0) {
                                str2 = new StringBuffer().append(connector.getIntegralScheme()).append("://").append(request.getServerName()).append(":").append(connector.getIntegralPort()).append(request.getRequestURI()).toString();
                                if (request.getQueryString() != null) {
                                    str2 = new StringBuffer().append(str2).append("?").append(request.getQueryString()).toString();
                                }
                                response.setContentLength(0);
                                response.sendRedirect(response.encodeRedirectURL(str2));
                            } else {
                                response.sendError(403, null);
                            }
                            return false;
                        }
                        break;
                    case 2:
                        if (!connector.isConfidential(request)) {
                            if (connector.getConfidentialPort() > 0) {
                                str2 = new StringBuffer().append(connector.getConfidentialScheme()).append("://").append(request.getServerName()).append(":").append(connector.getConfidentialPort()).append(request.getRequestURI()).toString();
                                if (request.getQueryString() != null) {
                                    str2 = new StringBuffer().append(str2).append("?").append(request.getQueryString()).toString();
                                }
                                response.setContentLength(0);
                                response.sendRedirect(response.encodeRedirectURL(str2));
                            } else {
                                response.sendError(403, null);
                            }
                            return false;
                        }
                        break;
                    default:
                        response.sendError(403, null);
                        return false;
                }
            }
            if (obj3 != null || str3 == null) {
                request.setUserPrincipal(this._notChecked);
            } else if (userRealm == null) {
                Log.warn(new StringBuffer().append("Request ").append(request.getRequestURI()).append(" failed - no realm").toString());
                response.sendError(500, "No realm");
                return false;
            } else {
                Principal authenticate;
                if (request.getAuthType() != null && request.getRemoteUser() != null) {
                    Principal userPrincipal = request.getUserPrincipal();
                    if (userPrincipal == null) {
                        userPrincipal = userRealm.authenticate(request.getRemoteUser(), null, request);
                    }
                    authenticate = (userPrincipal != null || authenticator == null) ? userPrincipal : authenticator.authenticate(userRealm, str, request, response);
                } else if (authenticator != null) {
                    authenticate = authenticator.authenticate(userRealm, str, request, response);
                } else {
                    Log.warn(new StringBuffer().append("Mis-configured Authenticator for ").append(request.getRequestURI()).toString());
                    response.sendError(500, "Configuration error");
                    authenticate = null;
                }
                if (authenticate == null) {
                    return false;
                }
                if (authenticate == __NOBODY) {
                    return true;
                }
                if (str3 != Constraint.ANY_ROLE) {
                    int size = LazyList.size(str3);
                    while (true) {
                        i2 = size - 1;
                        if (size <= 0) {
                            obj2 = null;
                        } else if (userRealm.isUserInRole(authenticate, (String) LazyList.get(str3, i2))) {
                            obj2 = 1;
                        } else {
                            size = i2;
                        }
                        if (obj2 == null) {
                            Log.warn(new StringBuffer().append("AUTH FAILURE: incorrect role for ").append(StringUtil.printable(authenticate.getName())).toString());
                            response.sendError(403, "User not in required role");
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        response.sendError(403);
        return false;
    }

    public boolean checkSecurityConstraints(String str, Request request, Response response) throws IOException {
        String str2 = null;
        Object lazyMatches = this._constraintMap.getLazyMatches(str);
        if (lazyMatches != null) {
            Object obj;
            Object obj2 = null;
            loop0:
            for (int i = 0; i < LazyList.size(lazyMatches); i++) {
                Entry entry = (Entry) LazyList.get(lazyMatches, i);
                Object value = entry.getValue();
                String str3 = (String) entry.getKey();
                int i2 = 0;
                while (i2 < LazyList.size(value)) {
                    Object add;
                    String str4;
                    ConstraintMapping constraintMapping = (ConstraintMapping) LazyList.get(value, i2);
                    if (constraintMapping.getMethod() == null || constraintMapping.getMethod().equalsIgnoreCase(request.getMethod())) {
                        if (str2 != null && !str2.equals(str3)) {
                            obj = obj2;
                            break loop0;
                        }
                        add = LazyList.add(obj2, constraintMapping.getConstraint());
                        str4 = str3;
                    } else {
                        str4 = str2;
                        add = obj2;
                    }
                    i2++;
                    obj2 = add;
                    str2 = str4;
                }
            }
            obj = obj2;
            return check(obj, this._authenticator, this._userRealm, str, request, response);
        }
        request.setUserPrincipal(this._notChecked);
        return true;
    }

    public void doStart() throws Exception {
        if (this._authenticator == null) {
            if (Constraint.__BASIC_AUTH.equalsIgnoreCase(this._authMethod)) {
                this._authenticator = new BasicAuthenticator();
            } else if (Constraint.__DIGEST_AUTH.equalsIgnoreCase(this._authMethod)) {
                this._authenticator = new DigestAuthenticator();
            } else if (Constraint.__CERT_AUTH.equalsIgnoreCase(this._authMethod)) {
                this._authenticator = new ClientCertAuthenticator();
            } else if (Constraint.__FORM_AUTH.equalsIgnoreCase(this._authMethod)) {
                this._authenticator = new FormAuthenticator();
            } else {
                Log.warn(new StringBuffer().append("Unknown Authentication method:").append(this._authMethod).toString());
            }
        }
        super.doStart();
    }

    public String getAuthMethod() {
        return this._authMethod;
    }

    public Authenticator getAuthenticator() {
        return this._authenticator;
    }

    public ConstraintMapping[] getConstraintMappings() {
        return this._constraintMappings;
    }

    public UserRealm getUserRealm() {
        return this._userRealm;
    }

    public void handle(String str, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException, ServletException {
        Request request = httpServletRequest instanceof Request ? (Request) httpServletRequest : HttpConnection.getCurrentConnection().getRequest();
        Response response = httpServletResponse instanceof Response ? (Response) httpServletResponse : HttpConnection.getCurrentConnection().getResponse();
        UserRealm userRealm = request.getUserRealm();
        request.setUserRealm(getUserRealm());
        if (i != 1 || checkSecurityConstraints(str, request, response)) {
            if (i == 2) {
                if (this._checkWelcomeFiles && httpServletRequest.getAttribute("org.mortbay.jetty.welcome") != null) {
                    httpServletRequest.removeAttribute("org.mortbay.jetty.welcome");
                    if (!checkSecurityConstraints(str, request, response)) {
                        request.setHandled(true);
                        if (this._userRealm != null && i == 1) {
                            this._userRealm.disassociate(request.getUserPrincipal());
                        }
                        request.setUserRealm(userRealm);
                        return;
                    }
                }
            }
            try {
                if ((this._authenticator instanceof FormAuthenticator) && str.endsWith(FormAuthenticator.__J_SECURITY_CHECK)) {
                    this._authenticator.authenticate(getUserRealm(), str, request, response);
                    request.setHandled(true);
                    if (this._userRealm != null && i == 1) {
                        this._userRealm.disassociate(request.getUserPrincipal());
                    }
                    request.setUserRealm(userRealm);
                    return;
                }
                if (getHandler() != null) {
                    getHandler().handle(str, httpServletRequest, httpServletResponse, i);
                }
                if (this._userRealm != null && i == 1) {
                    this._userRealm.disassociate(request.getUserPrincipal());
                }
                request.setUserRealm(userRealm);
            } finally {
                if (this._userRealm != null && i == 1) {
                    this._userRealm.disassociate(request.getUserPrincipal());
                }
                request.setUserRealm(userRealm);
            }
        } else {
            request.setHandled(true);
        }
    }

    public boolean hasConstraints() {
        return this._constraintMappings != null && this._constraintMappings.length > 0;
    }

    public boolean isCheckWelcomeFiles() {
        return this._checkWelcomeFiles;
    }

    public void setAuthMethod(String str) {
        if (!isStarted() || this._authMethod == null || this._authMethod.equals(str)) {
            this._authMethod = str;
            return;
        }
        throw new IllegalStateException("Handler started");
    }

    public void setAuthenticator(Authenticator authenticator) {
        this._authenticator = authenticator;
    }

    public void setCheckWelcomeFiles(boolean z) {
        this._checkWelcomeFiles = z;
    }

    public void setConstraintMappings(ConstraintMapping[] constraintMappingArr) {
        this._constraintMappings = constraintMappingArr;
        if (this._constraintMappings != null) {
            this._constraintMappings = constraintMappingArr;
            this._constraintMap.clear();
            for (int i = 0; i < this._constraintMappings.length; i++) {
                this._constraintMap.put(this._constraintMappings[i].getPathSpec(), LazyList.add(this._constraintMap.get(this._constraintMappings[i].getPathSpec()), this._constraintMappings[i]));
            }
        }
    }

    public void setUserRealm(UserRealm userRealm) {
        this._userRealm = userRealm;
    }
}
