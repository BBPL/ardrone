package org.apache.http.impl.auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.protocol.HttpContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

@Deprecated
public class NegotiateScheme extends GGSSchemeBase {
    private static final String KERBEROS_OID = "1.2.840.113554.1.2.2";
    private static final String SPNEGO_OID = "1.3.6.1.5.5.2";
    private final Log log;
    private final SpnegoTokenGenerator spengoGenerator;

    public NegotiateScheme() {
        this(null, false);
    }

    public NegotiateScheme(SpnegoTokenGenerator spnegoTokenGenerator) {
        this(spnegoTokenGenerator, false);
    }

    public NegotiateScheme(SpnegoTokenGenerator spnegoTokenGenerator, boolean z) {
        super(z);
        this.log = LogFactory.getLog(getClass());
        this.spengoGenerator = spnegoTokenGenerator;
    }

    public Header authenticate(Credentials credentials, HttpRequest httpRequest) throws AuthenticationException {
        return authenticate(credentials, httpRequest, null);
    }

    public Header authenticate(Credentials credentials, HttpRequest httpRequest, HttpContext httpContext) throws AuthenticationException {
        return super.authenticate(credentials, httpRequest, httpContext);
    }

    protected byte[] generateToken(byte[] bArr, String str) throws GSSException {
        byte[] generateGSSToken;
        Object obj = null;
        try {
            generateGSSToken = generateGSSToken(bArr, new Oid(SPNEGO_OID), str);
        } catch (GSSException e) {
            if (e.getMajor() == 2) {
                this.log.debug("GSSException BAD_MECH, retry with Kerberos MECH");
                int i = 1;
                generateGSSToken = bArr;
            } else {
                throw e;
            }
        }
        if (obj != null) {
            this.log.debug("Using Kerberos MECH 1.2.840.113554.1.2.2");
            generateGSSToken = generateGSSToken(generateGSSToken, new Oid(KERBEROS_OID), str);
            if (!(generateGSSToken == null || this.spengoGenerator == null)) {
                try {
                    generateGSSToken = this.spengoGenerator.generateSpnegoDERObject(generateGSSToken);
                } catch (Throwable e2) {
                    this.log.error(e2.getMessage(), e2);
                }
            }
        }
        return generateGSSToken;
    }

    public String getParameter(String str) {
        if (str != null) {
            return null;
        }
        throw new IllegalArgumentException("Parameter name may not be null");
    }

    public String getRealm() {
        return null;
    }

    public String getSchemeName() {
        return "Negotiate";
    }

    public boolean isConnectionBased() {
        return true;
    }
}
