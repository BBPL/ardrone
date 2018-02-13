package org.apache.http.conn.ssl;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.util.InetAddressUtils;
import org.mortbay.jetty.security.Constraint;

@Immutable
public abstract class AbstractVerifier implements X509HostnameVerifier {
    private static final String[] BAD_COUNTRY_2LDS = new String[]{"ac", "co", "com", "ed", "edu", "go", "gouv", "gov", "info", "lg", "ne", "net", "or", "org"};

    static {
        Arrays.sort(BAD_COUNTRY_2LDS);
    }

    public static boolean acceptableCountryWildcard(String str) {
        String[] split = str.split("\\.");
        return (split.length == 3 && split[2].length() == 2 && Arrays.binarySearch(BAD_COUNTRY_2LDS, split[1]) >= 0) ? false : true;
    }

    public static int countDots(String str) {
        int i = 0;
        for (int i2 = 0; i2 < str.length(); i2++) {
            if (str.charAt(i2) == '.') {
                i++;
            }
        }
        return i;
    }

    public static String[] getCNs(X509Certificate x509Certificate) {
        LinkedList linkedList = new LinkedList();
        StringTokenizer stringTokenizer = new StringTokenizer(x509Certificate.getSubjectX500Principal().toString(), ",");
        while (stringTokenizer.hasMoreTokens()) {
            String nextToken = stringTokenizer.nextToken();
            int indexOf = nextToken.indexOf("CN=");
            if (indexOf >= 0) {
                linkedList.add(nextToken.substring(indexOf + 3));
            }
        }
        if (linkedList.isEmpty()) {
            return null;
        }
        String[] strArr = new String[linkedList.size()];
        linkedList.toArray(strArr);
        return strArr;
    }

    public static String[] getDNSSubjectAlts(X509Certificate x509Certificate) {
        return getSubjectAlts(x509Certificate, null);
    }

    private static String[] getSubjectAlts(X509Certificate x509Certificate, String str) {
        int i = isIPAddress(str) ? 7 : 2;
        LinkedList linkedList = new LinkedList();
        Collection subjectAlternativeNames;
        try {
            subjectAlternativeNames = x509Certificate.getSubjectAlternativeNames();
        } catch (Throwable e) {
            Logger.getLogger(AbstractVerifier.class.getName()).log(Level.FINE, "Error parsing certificate.", e);
            subjectAlternativeNames = null;
        }
        if (r0 != null) {
            for (List list : r0) {
                if (((Integer) list.get(0)).intValue() == i) {
                    linkedList.add((String) list.get(1));
                }
            }
        }
        if (linkedList.isEmpty()) {
            return null;
        }
        String[] strArr = new String[linkedList.size()];
        linkedList.toArray(strArr);
        return strArr;
    }

    private static boolean isIPAddress(String str) {
        return str != null && (InetAddressUtils.isIPv4Address(str) || InetAddressUtils.isIPv6Address(str));
    }

    public final void verify(String str, X509Certificate x509Certificate) throws SSLException {
        verify(str, getCNs(x509Certificate), getSubjectAlts(x509Certificate, str));
    }

    public final void verify(String str, SSLSocket sSLSocket) throws IOException {
        if (str == null) {
            throw new NullPointerException("host to verify is null");
        }
        SSLSession session = sSLSocket.getSession();
        if (session == null) {
            sSLSocket.getInputStream().available();
            session = sSLSocket.getSession();
            if (session == null) {
                sSLSocket.startHandshake();
                session = sSLSocket.getSession();
            }
        }
        verify(str, (X509Certificate) session.getPeerCertificates()[0]);
    }

    public final void verify(String str, String[] strArr, String[] strArr2, boolean z) throws SSLException {
        int i;
        LinkedList linkedList = new LinkedList();
        if (!(strArr == null || strArr.length <= 0 || strArr[0] == null)) {
            linkedList.add(strArr[0]);
        }
        if (strArr2 != null) {
            for (Object obj : strArr2) {
                if (obj != null) {
                    linkedList.add(obj);
                }
            }
        }
        if (linkedList.isEmpty()) {
            throw new SSLException("Certificate for <" + str + "> doesn't contain CN or DNS subjectAlt");
        }
        StringBuilder stringBuilder = new StringBuilder();
        String toLowerCase = str.trim().toLowerCase(Locale.ENGLISH);
        Iterator it = linkedList.iterator();
        boolean z2 = false;
        while (it.hasNext()) {
            String toLowerCase2 = ((String) it.next()).toLowerCase(Locale.ENGLISH);
            stringBuilder.append(" <");
            stringBuilder.append(toLowerCase2);
            stringBuilder.append('>');
            if (it.hasNext()) {
                stringBuilder.append(" OR");
            }
            String[] split = toLowerCase2.split("\\.");
            i = (split.length < 3 || !split[0].endsWith(Constraint.ANY_ROLE) || !acceptableCountryWildcard(toLowerCase2) || isIPAddress(str)) ? 0 : 1;
            if (i != 0) {
                if (split[0].length() > 1) {
                    String substring = split[0].substring(0, split.length - 2);
                    z2 = toLowerCase.startsWith(substring) && toLowerCase.substring(substring.length()).endsWith(toLowerCase2.substring(split[0].length()));
                } else {
                    z2 = toLowerCase.endsWith(toLowerCase2.substring(1));
                }
                if (z2 && z) {
                    if (countDots(toLowerCase) == countDots(toLowerCase2)) {
                        z2 = true;
                        continue;
                    } else {
                        z2 = false;
                        continue;
                    }
                }
            } else {
                z2 = toLowerCase.equals(toLowerCase2);
                continue;
            }
            if (z2) {
                break;
            }
        }
        if (!z2) {
            throw new SSLException("hostname in certificate didn't match: <" + str + "> !=" + stringBuilder);
        }
    }

    public final boolean verify(String str, SSLSession sSLSession) {
        try {
            verify(str, (X509Certificate) sSLSession.getPeerCertificates()[0]);
            return true;
        } catch (SSLException e) {
            return false;
        }
    }
}
