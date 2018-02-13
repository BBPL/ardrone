package org.mortbay.jetty.security;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.Principal;
import org.apache.sanselan.formats.tiff.constants.ExifTagConstants;
import org.mortbay.jetty.HttpTokens;
import org.mortbay.jetty.HttpVersions;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Response;
import org.mortbay.jetty.security.Credential.MD5;
import org.mortbay.log.Log;
import org.mortbay.util.QuotedStringTokenizer;
import org.mortbay.util.StringUtil;
import org.mortbay.util.TypeUtil;
import org.mortbay.util.URIUtil;

public class DigestAuthenticator implements Authenticator {
    protected long maxNonceAge = 0;
    protected long nonceSecret = (((long) hashCode()) ^ System.currentTimeMillis());
    protected boolean useStale = false;

    private static class Digest extends Credential {
        String cnonce = null;
        String method = null;
        String nc = null;
        String nonce = null;
        String qop = null;
        String realm = null;
        String response = null;
        String uri = null;
        String username = null;

        Digest(String str) {
            this.method = str;
        }

        public boolean check(Object obj) {
            String obj2 = obj instanceof String ? (String) obj : obj.toString();
            try {
                byte[] digest;
                MessageDigest instance = MessageDigest.getInstance("MD5");
                if (obj instanceof MD5) {
                    digest = ((MD5) obj).getDigest();
                } else {
                    instance.update(this.username.getBytes(StringUtil.__ISO_8859_1));
                    instance.update(HttpTokens.COLON);
                    instance.update(this.realm.getBytes(StringUtil.__ISO_8859_1));
                    instance.update(HttpTokens.COLON);
                    instance.update(obj2.getBytes(StringUtil.__ISO_8859_1));
                    digest = instance.digest();
                }
                instance.reset();
                instance.update(this.method.getBytes(StringUtil.__ISO_8859_1));
                instance.update(HttpTokens.COLON);
                instance.update(this.uri.getBytes(StringUtil.__ISO_8859_1));
                byte[] digest2 = instance.digest();
                instance.update(TypeUtil.toString(digest, 16).getBytes(StringUtil.__ISO_8859_1));
                instance.update(HttpTokens.COLON);
                instance.update(this.nonce.getBytes(StringUtil.__ISO_8859_1));
                instance.update(HttpTokens.COLON);
                instance.update(this.nc.getBytes(StringUtil.__ISO_8859_1));
                instance.update(HttpTokens.COLON);
                instance.update(this.cnonce.getBytes(StringUtil.__ISO_8859_1));
                instance.update(HttpTokens.COLON);
                instance.update(this.qop.getBytes(StringUtil.__ISO_8859_1));
                instance.update(HttpTokens.COLON);
                instance.update(TypeUtil.toString(digest2, 16).getBytes(StringUtil.__ISO_8859_1));
                return TypeUtil.toString(instance.digest(), 16).equalsIgnoreCase(this.response);
            } catch (Throwable e) {
                Log.warn(e);
                return false;
            }
        }

        public String toString() {
            return new StringBuffer().append(this.username).append(",").append(this.response).toString();
        }
    }

    public Principal authenticate(UserRealm userRealm, String str, Request request, Response response) throws IOException {
        boolean z;
        Principal principal = null;
        String header = request.getHeader("Authorization");
        if (header != null) {
            if (Log.isDebugEnabled()) {
                Log.debug(new StringBuffer().append("Credentials: ").append(header).toString());
            }
            QuotedStringTokenizer quotedStringTokenizer = new QuotedStringTokenizer(header, "=, ", true, false);
            Digest digest = new Digest(request.getMethod());
            header = null;
            Principal principal2 = null;
            while (quotedStringTokenizer.hasMoreTokens()) {
                String nextToken = quotedStringTokenizer.nextToken();
                switch (nextToken.length() == 1 ? nextToken.charAt(0) : '\u0000') {
                    case ' ':
                        break;
                    case ',':
                        header = null;
                        break;
                    case ExifTagConstants.PIXEL_FORMAT_VALUE_32_BIT_RGBE /*61*/:
                        header = principal2;
                        principal2 = nextToken;
                        break;
                    default:
                        if (header == null) {
                            principal2 = nextToken;
                            break;
                        }
                        if ("username".equalsIgnoreCase(header)) {
                            digest.username = nextToken;
                        } else if ("realm".equalsIgnoreCase(header)) {
                            digest.realm = nextToken;
                        } else if ("nonce".equalsIgnoreCase(header)) {
                            digest.nonce = nextToken;
                        } else if ("nc".equalsIgnoreCase(header)) {
                            digest.nc = nextToken;
                        } else if ("cnonce".equalsIgnoreCase(header)) {
                            digest.cnonce = nextToken;
                        } else if ("qop".equalsIgnoreCase(header)) {
                            digest.qop = nextToken;
                        } else if ("uri".equalsIgnoreCase(header)) {
                            digest.uri = nextToken;
                        } else if ("response".equalsIgnoreCase(header)) {
                            digest.response = nextToken;
                        }
                        header = null;
                        principal2 = nextToken;
                        break;
                }
            }
            int checkNonce = checkNonce(digest.nonce, request);
            if (checkNonce > 0) {
                principal = userRealm.authenticate(digest.username, digest, request);
                z = false;
            } else {
                z = checkNonce == 0;
            }
            if (principal == null) {
                Log.warn(new StringBuffer().append("AUTH FAILURE: user ").append(StringUtil.printable(digest.username)).toString());
            } else {
                request.setAuthType(Constraint.__DIGEST_AUTH);
                request.setUserPrincipal(principal);
            }
        } else {
            z = false;
        }
        if (principal == null && response != null) {
            sendChallenge(userRealm, request, response, z);
        }
        return principal;
    }

    public int checkNonce(String str, Request request) {
        try {
            Object decode = B64Code.decode(str.toCharArray());
            if (decode.length != 24) {
                return -1;
            }
            long j = 0;
            long j2 = this.nonceSecret;
            Object obj = new byte[16];
            System.arraycopy(decode, 0, obj, 0, 8);
            for (int i = 0; i < 8; i++) {
                obj[i + 8] = (byte) ((int) (255 & j2));
                j2 >>= 8;
                j = (j << 8) + (255 & ((long) decode[7 - i]));
            }
            j2 = request.getTimeStamp() - j;
            if (Log.isDebugEnabled()) {
                Log.debug(new StringBuffer().append("age=").append(j2).toString());
            }
            byte[] bArr = null;
            try {
                MessageDigest instance = MessageDigest.getInstance("MD5");
                instance.reset();
                instance.update(obj, 0, 16);
                bArr = instance.digest();
            } catch (Throwable e) {
                Log.warn(e);
            }
            for (int i2 = 0; i2 < 16; i2++) {
                if (decode[i2 + 8] != bArr[i2]) {
                    return -1;
                }
            }
            return (this.maxNonceAge <= 0 || (j2 >= 0 && j2 <= this.maxNonceAge)) ? 1 : 0;
        } catch (Throwable e2) {
            Log.ignore(e2);
            return -1;
        }
    }

    public String getAuthMethod() {
        return Constraint.__DIGEST_AUTH;
    }

    public long getMaxNonceAge() {
        return this.maxNonceAge;
    }

    public long getNonceSecret() {
        return this.nonceSecret;
    }

    public boolean getUseStale() {
        return this.useStale;
    }

    public String newNonce(Request request) {
        int i = 0;
        long timeStamp = request.getTimeStamp();
        long j = this.nonceSecret;
        byte[] bArr = new byte[24];
        for (int i2 = 0; i2 < 8; i2++) {
            bArr[i2] = (byte) ((int) (255 & timeStamp));
            timeStamp >>= 8;
            bArr[i2 + 8] = (byte) ((int) (255 & j));
            j >>= 8;
        }
        byte[] bArr2 = null;
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.reset();
            instance.update(bArr, 0, 16);
            bArr2 = instance.digest();
        } catch (Throwable e) {
            Log.warn(e);
        }
        while (i < bArr2.length) {
            bArr[i + 8] = bArr2[i];
            if (i == 23) {
                break;
            }
            i++;
        }
        return new String(B64Code.encode(bArr));
    }

    public void sendChallenge(UserRealm userRealm, Request request, Response response, boolean z) throws IOException {
        String contextPath = request.getContextPath();
        if (contextPath == null) {
            contextPath = URIUtil.SLASH;
        }
        response.setHeader("WWW-Authenticate", new StringBuffer().append("Digest realm=\"").append(userRealm.getName()).append("\", domain=\"").append(contextPath).append("\", nonce=\"").append(newNonce(request)).append("\", algorithm=MD5, qop=\"auth\"").append(this.useStale ? new StringBuffer().append(" stale=").append(z).toString() : HttpVersions.HTTP_0_9).toString());
        response.sendError(401);
    }

    public void setMaxNonceAge(long j) {
        this.maxNonceAge = j;
    }

    public void setNonceSecret(long j) {
        this.nonceSecret = j;
    }

    public void setUseStale(boolean z) {
        this.useStale = z;
    }
}
