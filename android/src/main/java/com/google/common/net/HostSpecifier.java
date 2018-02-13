package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.net.InetAddress;
import java.text.ParseException;
import javax.annotation.Nullable;

@Beta
public final class HostSpecifier {
    private final String canonicalForm;

    private HostSpecifier(String str) {
        this.canonicalForm = str;
    }

    public static HostSpecifier from(String str) throws ParseException {
        try {
            return fromValid(str);
        } catch (Throwable e) {
            ParseException parseException = new ParseException("Invalid host specifier: " + str, 0);
            parseException.initCause(e);
            throw parseException;
        }
    }

    public static HostSpecifier fromValid(String str) {
        InetAddress forString;
        HostAndPort fromString = HostAndPort.fromString(str);
        Preconditions.checkArgument(!fromString.hasPort());
        String hostText = fromString.getHostText();
        InetAddress inetAddress = null;
        try {
            forString = InetAddresses.forString(hostText);
        } catch (IllegalArgumentException e) {
            forString = inetAddress;
        }
        if (forString != null) {
            return new HostSpecifier(InetAddresses.toUriString(forString));
        }
        InternetDomainName from = InternetDomainName.from(hostText);
        if (from.hasPublicSuffix()) {
            return new HostSpecifier(from.name());
        }
        throw new IllegalArgumentException("Domain name does not have a recognized public suffix: " + hostText);
    }

    public static boolean isValid(String str) {
        try {
            fromValid(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HostSpecifier)) {
            return false;
        }
        return this.canonicalForm.equals(((HostSpecifier) obj).canonicalForm);
    }

    public int hashCode() {
        return this.canonicalForm.hashCode();
    }

    public String toString() {
        return this.canonicalForm;
    }
}
