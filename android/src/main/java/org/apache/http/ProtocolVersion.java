package org.apache.http;

import java.io.Serializable;
import org.apache.http.annotation.Immutable;

@Immutable
public class ProtocolVersion implements Serializable, Cloneable {
    private static final long serialVersionUID = 8950662842175091068L;
    protected final int major;
    protected final int minor;
    protected final String protocol;

    public ProtocolVersion(String str, int i, int i2) {
        if (str == null) {
            throw new IllegalArgumentException("Protocol name must not be null.");
        } else if (i < 0) {
            throw new IllegalArgumentException("Protocol major version number must not be negative.");
        } else if (i2 < 0) {
            throw new IllegalArgumentException("Protocol minor version number may not be negative");
        } else {
            this.protocol = str;
            this.major = i;
            this.minor = i2;
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int compareToVersion(ProtocolVersion protocolVersion) {
        if (protocolVersion == null) {
            throw new IllegalArgumentException("Protocol version must not be null.");
        } else if (this.protocol.equals(protocolVersion.protocol)) {
            int major = getMajor() - protocolVersion.getMajor();
            return major == 0 ? getMinor() - protocolVersion.getMinor() : major;
        } else {
            throw new IllegalArgumentException("Versions for different protocols cannot be compared. " + this + " " + protocolVersion);
        }
    }

    public final boolean equals(Object obj) {
        if (this != obj) {
            if (!(obj instanceof ProtocolVersion)) {
                return false;
            }
            ProtocolVersion protocolVersion = (ProtocolVersion) obj;
            if (!this.protocol.equals(protocolVersion.protocol) || this.major != protocolVersion.major) {
                return false;
            }
            if (this.minor != protocolVersion.minor) {
                return false;
            }
        }
        return true;
    }

    public ProtocolVersion forVersion(int i, int i2) {
        return (i == this.major && i2 == this.minor) ? this : new ProtocolVersion(this.protocol, i, i2);
    }

    public final int getMajor() {
        return this.major;
    }

    public final int getMinor() {
        return this.minor;
    }

    public final String getProtocol() {
        return this.protocol;
    }

    public final boolean greaterEquals(ProtocolVersion protocolVersion) {
        return isComparable(protocolVersion) && compareToVersion(protocolVersion) >= 0;
    }

    public final int hashCode() {
        return (this.protocol.hashCode() ^ (this.major * 100000)) ^ this.minor;
    }

    public boolean isComparable(ProtocolVersion protocolVersion) {
        return protocolVersion != null && this.protocol.equals(protocolVersion.protocol);
    }

    public final boolean lessEquals(ProtocolVersion protocolVersion) {
        return isComparable(protocolVersion) && compareToVersion(protocolVersion) <= 0;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.protocol);
        stringBuilder.append('/');
        stringBuilder.append(Integer.toString(this.major));
        stringBuilder.append('.');
        stringBuilder.append(Integer.toString(this.minor));
        return stringBuilder.toString();
    }
}
