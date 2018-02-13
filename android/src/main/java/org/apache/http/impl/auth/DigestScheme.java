package org.apache.http.impl.auth;

import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ChallengeState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.auth.params.AuthParams;
import org.apache.http.message.BasicHeaderValueFormatter;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.BufferedHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EncodingUtils;

@NotThreadSafe
public class DigestScheme extends RFC2617Scheme {
    private static final char[] HEXADECIMAL = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final int QOP_AUTH = 2;
    private static final int QOP_AUTH_INT = 1;
    private static final int QOP_MISSING = 0;
    private static final int QOP_UNKNOWN = -1;
    private String a1;
    private String a2;
    private String cnonce;
    private boolean complete;
    private String lastNonce;
    private long nounceCount;

    public DigestScheme() {
        this(null);
    }

    public DigestScheme(ChallengeState challengeState) {
        super(challengeState);
        this.complete = false;
    }

    public static String createCnonce() {
        byte[] bArr = new byte[8];
        new SecureRandom().nextBytes(bArr);
        return encode(bArr);
    }

    private Header createDigestHeader(Credentials credentials, HttpRequest httpRequest) throws AuthenticationException {
        String parameter = getParameter("uri");
        String parameter2 = getParameter("realm");
        String parameter3 = getParameter("nonce");
        String parameter4 = getParameter("opaque");
        String parameter5 = getParameter("methodname");
        String parameter6 = getParameter("algorithm");
        Set hashSet = new HashSet(8);
        int i = -1;
        String parameter7 = getParameter("qop");
        if (parameter7 != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(parameter7, ",");
            while (stringTokenizer.hasMoreTokens()) {
                hashSet.add(stringTokenizer.nextToken().trim().toLowerCase(Locale.US));
            }
            if ((httpRequest instanceof HttpEntityEnclosingRequest) && hashSet.contains("auth-int")) {
                i = 1;
            } else if (hashSet.contains("auth")) {
                i = 2;
            }
        } else {
            i = 0;
        }
        if (i == -1) {
            throw new AuthenticationException("None of the qop methods is supported: " + parameter7);
        }
        if (parameter6 == null) {
            parameter6 = "MD5";
        }
        parameter7 = getParameter("charset");
        if (parameter7 == null) {
            parameter7 = "ISO-8859-1";
        }
        String str = parameter6.equalsIgnoreCase("MD5-sess") ? "MD5" : parameter6;
        try {
            MessageDigest createMessageDigest = createMessageDigest(str);
            String name = credentials.getUserPrincipal().getName();
            str = credentials.getPassword();
            if (parameter3.equals(this.lastNonce)) {
                this.nounceCount++;
            } else {
                this.nounceCount = 1;
                this.cnonce = null;
                this.lastNonce = parameter3;
            }
            Appendable stringBuilder = new StringBuilder(256);
            new Formatter(stringBuilder, Locale.US).format("%08x", new Object[]{Long.valueOf(this.nounceCount)});
            String stringBuilder2 = stringBuilder.toString();
            if (this.cnonce == null) {
                this.cnonce = createCnonce();
            }
            this.a1 = null;
            this.a2 = null;
            if (parameter6.equalsIgnoreCase("MD5-sess")) {
                stringBuilder.setLength(0);
                stringBuilder.append(name).append(':').append(parameter2).append(':').append(str);
                str = encode(createMessageDigest.digest(EncodingUtils.getBytes(stringBuilder.toString(), parameter7)));
                stringBuilder.setLength(0);
                stringBuilder.append(str).append(':').append(parameter3).append(':').append(this.cnonce);
                this.a1 = stringBuilder.toString();
            } else {
                stringBuilder.setLength(0);
                stringBuilder.append(name).append(':').append(parameter2).append(':').append(str);
                this.a1 = stringBuilder.toString();
            }
            String encode = encode(createMessageDigest.digest(EncodingUtils.getBytes(this.a1, parameter7)));
            if (i == 2) {
                this.a2 = parameter5 + ':' + parameter;
            } else if (i == 1) {
                HttpEntity httpEntity = null;
                if (httpRequest instanceof HttpEntityEnclosingRequest) {
                    httpEntity = ((HttpEntityEnclosingRequest) httpRequest).getEntity();
                }
                if (httpEntity == null || httpEntity.isRepeatable()) {
                    OutputStream httpEntityDigester = new HttpEntityDigester(createMessageDigest);
                    if (httpEntity != null) {
                        try {
                            httpEntity.writeTo(httpEntityDigester);
                        } catch (Throwable e) {
                            throw new AuthenticationException("I/O error reading entity content", e);
                        }
                    }
                    httpEntityDigester.close();
                    this.a2 = parameter5 + ':' + parameter + ':' + encode(httpEntityDigester.getDigest());
                } else if (hashSet.contains("auth")) {
                    i = 2;
                    this.a2 = parameter5 + ':' + parameter;
                } else {
                    throw new AuthenticationException("Qop auth-int cannot be used with a non-repeatable entity");
                }
            } else {
                this.a2 = parameter5 + ':' + parameter;
            }
            str = encode(createMessageDigest.digest(EncodingUtils.getBytes(this.a2, parameter7)));
            if (i == 0) {
                stringBuilder.setLength(0);
                stringBuilder.append(encode).append(':').append(parameter3).append(':').append(str);
                parameter7 = stringBuilder.toString();
            } else {
                stringBuilder.setLength(0);
                stringBuilder.append(encode).append(':').append(parameter3).append(':').append(stringBuilder2).append(':').append(this.cnonce).append(':').append(i == 1 ? "auth-int" : "auth").append(':').append(str);
                parameter7 = stringBuilder.toString();
            }
            parameter7 = encode(createMessageDigest.digest(EncodingUtils.getAsciiBytes(parameter7)));
            CharArrayBuffer charArrayBuffer = new CharArrayBuffer(128);
            if (isProxy()) {
                charArrayBuffer.append("Proxy-Authorization");
            } else {
                charArrayBuffer.append("Authorization");
            }
            charArrayBuffer.append(": Digest ");
            List arrayList = new ArrayList(20);
            arrayList.add(new BasicNameValuePair("username", name));
            arrayList.add(new BasicNameValuePair("realm", parameter2));
            arrayList.add(new BasicNameValuePair("nonce", parameter3));
            arrayList.add(new BasicNameValuePair("uri", parameter));
            arrayList.add(new BasicNameValuePair("response", parameter7));
            if (i != 0) {
                arrayList.add(new BasicNameValuePair("qop", i == 1 ? "auth-int" : "auth"));
                arrayList.add(new BasicNameValuePair("nc", stringBuilder2));
                arrayList.add(new BasicNameValuePair("cnonce", this.cnonce));
            }
            if (parameter6 != null) {
                arrayList.add(new BasicNameValuePair("algorithm", parameter6));
            }
            if (parameter4 != null) {
                arrayList.add(new BasicNameValuePair("opaque", parameter4));
            }
            for (i = 0; i < arrayList.size(); i++) {
                NameValuePair nameValuePair = (BasicNameValuePair) arrayList.get(i);
                if (i > 0) {
                    charArrayBuffer.append(", ");
                }
                Object obj = ("nc".equals(nameValuePair.getName()) || "qop".equals(nameValuePair.getName())) ? 1 : null;
                BasicHeaderValueFormatter.DEFAULT.formatNameValuePair(charArrayBuffer, nameValuePair, obj == null);
            }
            return new BufferedHeader(charArrayBuffer);
        } catch (UnsupportedDigestAlgorithmException e2) {
            throw new AuthenticationException("Unsuppported digest algorithm: " + str);
        }
    }

    private static MessageDigest createMessageDigest(String str) throws UnsupportedDigestAlgorithmException {
        try {
            return MessageDigest.getInstance(str);
        } catch (Exception e) {
            throw new UnsupportedDigestAlgorithmException("Unsupported algorithm in HTTP Digest authentication: " + str);
        }
    }

    static String encode(byte[] bArr) {
        int length = bArr.length;
        char[] cArr = new char[(length * 2)];
        for (int i = 0; i < length; i++) {
            byte b = bArr[i];
            cArr[i * 2] = HEXADECIMAL[(bArr[i] & 240) >> 4];
            cArr[(i * 2) + 1] = HEXADECIMAL[b & 15];
        }
        return new String(cArr);
    }

    @Deprecated
    public Header authenticate(Credentials credentials, HttpRequest httpRequest) throws AuthenticationException {
        return authenticate(credentials, httpRequest, new BasicHttpContext());
    }

    public Header authenticate(Credentials credentials, HttpRequest httpRequest, HttpContext httpContext) throws AuthenticationException {
        if (credentials == null) {
            throw new IllegalArgumentException("Credentials may not be null");
        } else if (httpRequest == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        } else if (getParameter("realm") == null) {
            throw new AuthenticationException("missing realm in challenge");
        } else if (getParameter("nonce") == null) {
            throw new AuthenticationException("missing nonce in challenge");
        } else {
            getParameters().put("methodname", httpRequest.getRequestLine().getMethod());
            getParameters().put("uri", httpRequest.getRequestLine().getUri());
            if (getParameter("charset") == null) {
                getParameters().put("charset", AuthParams.getCredentialCharset(httpRequest.getParams()));
            }
            return createDigestHeader(credentials, httpRequest);
        }
    }

    String getA1() {
        return this.a1;
    }

    String getA2() {
        return this.a2;
    }

    String getCnonce() {
        return this.cnonce;
    }

    public String getSchemeName() {
        return "digest";
    }

    public boolean isComplete() {
        return "true".equalsIgnoreCase(getParameter("stale")) ? false : this.complete;
    }

    public boolean isConnectionBased() {
        return false;
    }

    public void overrideParamter(String str, String str2) {
        getParameters().put(str, str2);
    }

    public void processChallenge(Header header) throws MalformedChallengeException {
        super.processChallenge(header);
        this.complete = true;
    }
}
