package com.google.api.client.extensions.appengine.http;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.Preconditions;
import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPMethod;
import java.io.IOException;
import java.util.Arrays;

public final class UrlFetchTransport extends HttpTransport {
    private static final String[] SUPPORTED_METHODS = new String[]{"DELETE", "GET", "HEAD", "POST", "PUT"};
    private final CertificateValidationBehavior certificateValidationBehavior;

    public static final class Builder {
        private CertificateValidationBehavior certificateValidationBehavior = CertificateValidationBehavior.DEFAULT;

        public UrlFetchTransport build() {
            return new UrlFetchTransport(this.certificateValidationBehavior);
        }

        public Builder doNotValidateCertificate() {
            this.certificateValidationBehavior = CertificateValidationBehavior.DO_NOT_VALIDATE;
            return this;
        }

        public boolean getDoNotValidateCertificate() {
            return this.certificateValidationBehavior == CertificateValidationBehavior.DO_NOT_VALIDATE;
        }

        public boolean getValidateCertificate() {
            return this.certificateValidationBehavior == CertificateValidationBehavior.VALIDATE;
        }

        public Builder validateCertificate() {
            this.certificateValidationBehavior = CertificateValidationBehavior.VALIDATE;
            return this;
        }
    }

    enum CertificateValidationBehavior {
        DEFAULT,
        VALIDATE,
        DO_NOT_VALIDATE
    }

    static {
        Arrays.sort(SUPPORTED_METHODS);
    }

    public UrlFetchTransport() {
        this(CertificateValidationBehavior.DEFAULT);
    }

    UrlFetchTransport(CertificateValidationBehavior certificateValidationBehavior) {
        this.certificateValidationBehavior = (CertificateValidationBehavior) Preconditions.checkNotNull(certificateValidationBehavior);
    }

    protected UrlFetchRequest buildRequest(String str, String str2) throws IOException {
        Preconditions.checkArgument(supportsMethod(str), "HTTP method %s not supported", str);
        HTTPMethod hTTPMethod = str.equals("DELETE") ? HTTPMethod.DELETE : str.equals("GET") ? HTTPMethod.GET : str.equals("HEAD") ? HTTPMethod.HEAD : str.equals("POST") ? HTTPMethod.POST : HTTPMethod.PUT;
        FetchOptions validateCertificate = com.google.appengine.api.urlfetch.FetchOptions.Builder.doNotFollowRedirects().disallowTruncate().validateCertificate();
        switch (this.certificateValidationBehavior) {
            case VALIDATE:
                validateCertificate.validateCertificate();
                break;
            case DO_NOT_VALIDATE:
                validateCertificate.doNotValidateCertificate();
                break;
        }
        return new UrlFetchRequest(validateCertificate, hTTPMethod, str2);
    }

    public boolean supportsMethod(String str) {
        return Arrays.binarySearch(SUPPORTED_METHODS, str) >= 0;
    }
}
