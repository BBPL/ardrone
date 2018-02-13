package com.google.api.client.extensions.servlet.auth;

import com.google.api.client.extensions.auth.helpers.ThreeLeggedFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import java.io.IOException;
import java.util.logging.Logger;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.jetty.HttpVersions;

public abstract class AbstractCallbackServlet extends HttpServlet {
    private static final String ERROR_PARAM = "error";
    private static final Logger LOG = Logger.getLogger(AbstractCallbackServlet.class.getName());
    private static final long serialVersionUID = 1;
    private String completionCodeQueryParam = getCompletionCodeQueryParam();
    private String deniedRedirectUrl = getDeniedRedirectUrl();
    private Class<? extends ThreeLeggedFlow> flowType = getConcreteFlowType();
    private final HttpTransport httpTransport = newHttpTransportInstance();
    private final JsonFactory jsonFactory = newJsonFactoryInstance();
    private PersistenceManagerFactory pmf = getPersistenceManagerFactory();
    private String redirectUrl = getSuccessRedirectUrl();

    protected final void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {
        String parameter = httpServletRequest.getParameter(this.completionCodeQueryParam);
        String parameter2 = httpServletRequest.getParameter(ERROR_PARAM);
        if ((parameter == null || HttpVersions.HTTP_0_9.equals(parameter)) && (parameter2 == null || HttpVersions.HTTP_0_9.equals(parameter2))) {
            httpServletResponse.setStatus(400);
            httpServletResponse.getWriter().print("Must have a query parameter: " + this.completionCodeQueryParam);
        } else if (parameter2 == null || HttpVersions.HTTP_0_9.equals(parameter2)) {
            String userId = getUserId();
            PersistenceManager persistenceManager = this.pmf.getPersistenceManager();
            try {
                ThreeLeggedFlow threeLeggedFlow = (ThreeLeggedFlow) persistenceManager.getObjectById(this.flowType, userId);
                threeLeggedFlow.setHttpTransport(getHttpTransport());
                threeLeggedFlow.setJsonFactory(getJsonFactory());
                persistenceManager.makePersistent(threeLeggedFlow.complete(parameter));
                persistenceManager.deletePersistent(threeLeggedFlow);
                httpServletResponse.sendRedirect(this.redirectUrl);
            } catch (JDOObjectNotFoundException e) {
                LOG.severe("Unable to locate flow by user: " + userId);
                httpServletResponse.setStatus(404);
                httpServletResponse.getWriter().print("Unable to find flow for user: " + userId);
            } finally {
                persistenceManager.close();
            }
        } else {
            httpServletResponse.sendRedirect(this.deniedRedirectUrl);
        }
    }

    protected abstract String getCompletionCodeQueryParam();

    protected abstract Class<? extends ThreeLeggedFlow> getConcreteFlowType();

    protected abstract String getDeniedRedirectUrl();

    protected final HttpTransport getHttpTransport() {
        return this.httpTransport;
    }

    protected final JsonFactory getJsonFactory() {
        return this.jsonFactory;
    }

    protected abstract PersistenceManagerFactory getPersistenceManagerFactory();

    protected abstract String getSuccessRedirectUrl();

    protected abstract String getUserId();

    protected abstract HttpTransport newHttpTransportInstance();

    protected abstract JsonFactory newJsonFactoryInstance();
}
