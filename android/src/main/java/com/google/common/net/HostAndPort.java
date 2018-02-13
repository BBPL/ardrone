package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.concurrent.Immutable;

@Immutable
@Beta
public final class HostAndPort implements Serializable {
    private static final Pattern BRACKET_PATTERN = Pattern.compile("^\\[(.*:.*)\\](?::(\\d*))?$");
    private static final int NO_PORT = -1;
    private static final long serialVersionUID = 0;
    private final boolean hasBracketlessColons;
    private final String host;
    private final int port;

    private HostAndPort(String str, int i, boolean z) {
        this.host = str;
        this.port = i;
        this.hasBracketlessColons = z;
    }

    public static HostAndPort fromParts(String str, int i) {
        Preconditions.checkArgument(isValidPort(i));
        HostAndPort fromString = fromString(str);
        Preconditions.checkArgument(!fromString.hasPort());
        return new HostAndPort(fromString.host, i, fromString.hasBracketlessColons);
    }

    public static HostAndPort fromString(String str) {
        String group;
        String group2;
        boolean z;
        int i = -1;
        Preconditions.checkNotNull(str);
        if (str.startsWith("[")) {
            Matcher matcher = BRACKET_PATTERN.matcher(str);
            Preconditions.checkArgument(matcher.matches(), "Invalid bracketed host/port: %s", str);
            group = matcher.group(1);
            group2 = matcher.group(2);
            z = false;
        } else {
            int indexOf = str.indexOf(58);
            if (indexOf < 0 || str.indexOf(58, indexOf + 1) != -1) {
                z = indexOf >= 0;
                group2 = null;
                group = str;
            } else {
                group = str.substring(0, indexOf);
                group2 = str.substring(indexOf + 1);
                z = false;
            }
        }
        if (group2 != null) {
            Preconditions.checkArgument(!group2.startsWith("+"), "Unparseable port number: %s", str);
            try {
                i = Integer.parseInt(group2);
                Preconditions.checkArgument(isValidPort(i), "Port number out of range: %s", str);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Unparseable port number: " + str);
            }
        }
        return new HostAndPort(group, i, z);
    }

    private static boolean isValidPort(int i) {
        return i >= 0 && i <= 65535;
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            if (!(obj instanceof HostAndPort)) {
                return false;
            }
            HostAndPort hostAndPort = (HostAndPort) obj;
            if (!Objects.equal(this.host, hostAndPort.host) || this.port != hostAndPort.port) {
                return false;
            }
            if (this.hasBracketlessColons != hostAndPort.hasBracketlessColons) {
                return false;
            }
        }
        return true;
    }

    public String getHostText() {
        return this.host;
    }

    public int getPort() {
        Preconditions.checkState(hasPort());
        return this.port;
    }

    public int getPortOrDefault(int i) {
        return hasPort() ? this.port : i;
    }

    public boolean hasPort() {
        return this.port >= 0;
    }

    public int hashCode() {
        return Objects.hashCode(this.host, Integer.valueOf(this.port), Boolean.valueOf(this.hasBracketlessColons));
    }

    public HostAndPort requireBracketsForIPv6() {
        Preconditions.checkArgument(!this.hasBracketlessColons, "Possible bracketless IPv6 literal: %s", this.host);
        return this;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(this.host.length() + 7);
        if (this.host.indexOf(58) >= 0) {
            stringBuilder.append('[').append(this.host).append(']');
        } else {
            stringBuilder.append(this.host);
        }
        if (hasPort()) {
            stringBuilder.append(':').append(this.port);
        }
        return stringBuilder.toString();
    }

    public HostAndPort withDefaultPort(int i) {
        Preconditions.checkArgument(isValidPort(i));
        return (hasPort() || this.port == i) ? this : new HostAndPort(this.host, i, this.hasBracketlessColons);
    }
}
