package org.apache.http.conn.routing;

import java.net.InetAddress;
import org.apache.http.HttpHost;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.routing.RouteInfo.LayerType;
import org.apache.http.conn.routing.RouteInfo.TunnelType;
import org.apache.http.util.LangUtils;

@NotThreadSafe
public final class RouteTracker implements RouteInfo, Cloneable {
    private boolean connected;
    private LayerType layered;
    private final InetAddress localAddress;
    private HttpHost[] proxyChain;
    private boolean secure;
    private final HttpHost targetHost;
    private TunnelType tunnelled;

    public RouteTracker(HttpHost httpHost, InetAddress inetAddress) {
        if (httpHost == null) {
            throw new IllegalArgumentException("Target host may not be null.");
        }
        this.targetHost = httpHost;
        this.localAddress = inetAddress;
        this.tunnelled = TunnelType.PLAIN;
        this.layered = LayerType.PLAIN;
    }

    public RouteTracker(HttpRoute httpRoute) {
        this(httpRoute.getTargetHost(), httpRoute.getLocalAddress());
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public final void connectProxy(HttpHost httpHost, boolean z) {
        if (httpHost == null) {
            throw new IllegalArgumentException("Proxy host may not be null.");
        } else if (this.connected) {
            throw new IllegalStateException("Already connected.");
        } else {
            this.connected = true;
            this.proxyChain = new HttpHost[]{httpHost};
            this.secure = z;
        }
    }

    public final void connectTarget(boolean z) {
        if (this.connected) {
            throw new IllegalStateException("Already connected.");
        }
        this.connected = true;
        this.secure = z;
    }

    public final boolean equals(Object obj) {
        if (obj != this) {
            if (!(obj instanceof RouteTracker)) {
                return false;
            }
            RouteTracker routeTracker = (RouteTracker) obj;
            if (this.connected != routeTracker.connected || this.secure != routeTracker.secure || this.tunnelled != routeTracker.tunnelled || this.layered != routeTracker.layered || !LangUtils.equals(this.targetHost, routeTracker.targetHost) || !LangUtils.equals(this.localAddress, routeTracker.localAddress)) {
                return false;
            }
            if (!LangUtils.equals(this.proxyChain, routeTracker.proxyChain)) {
                return false;
            }
        }
        return true;
    }

    public final int getHopCount() {
        return this.connected ? this.proxyChain == null ? 1 : this.proxyChain.length + 1 : 0;
    }

    public final HttpHost getHopTarget(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Hop index must not be negative: " + i);
        }
        int hopCount = getHopCount();
        if (i < hopCount) {
            return i < hopCount + -1 ? this.proxyChain[i] : this.targetHost;
        } else {
            throw new IllegalArgumentException("Hop index " + i + " exceeds tracked route length " + hopCount + ".");
        }
    }

    public final LayerType getLayerType() {
        return this.layered;
    }

    public final InetAddress getLocalAddress() {
        return this.localAddress;
    }

    public final HttpHost getProxyHost() {
        return this.proxyChain == null ? null : this.proxyChain[0];
    }

    public final HttpHost getTargetHost() {
        return this.targetHost;
    }

    public final TunnelType getTunnelType() {
        return this.tunnelled;
    }

    public final int hashCode() {
        int hashCode = LangUtils.hashCode(LangUtils.hashCode(17, this.targetHost), this.localAddress);
        if (this.proxyChain != null) {
            for (Object hashCode2 : this.proxyChain) {
                hashCode = LangUtils.hashCode(hashCode, hashCode2);
            }
        }
        return LangUtils.hashCode(LangUtils.hashCode(LangUtils.hashCode(LangUtils.hashCode(hashCode, this.connected), this.secure), this.tunnelled), this.layered);
    }

    public final boolean isConnected() {
        return this.connected;
    }

    public final boolean isLayered() {
        return this.layered == LayerType.LAYERED;
    }

    public final boolean isSecure() {
        return this.secure;
    }

    public final boolean isTunnelled() {
        return this.tunnelled == TunnelType.TUNNELLED;
    }

    public final void layerProtocol(boolean z) {
        if (this.connected) {
            this.layered = LayerType.LAYERED;
            this.secure = z;
            return;
        }
        throw new IllegalStateException("No layered protocol unless connected.");
    }

    public void reset() {
        this.connected = false;
        this.proxyChain = null;
        this.tunnelled = TunnelType.PLAIN;
        this.layered = LayerType.PLAIN;
        this.secure = false;
    }

    public final HttpRoute toRoute() {
        return !this.connected ? null : new HttpRoute(this.targetHost, this.localAddress, this.proxyChain, this.secure, this.tunnelled, this.layered);
    }

    public final String toString() {
        StringBuilder stringBuilder = new StringBuilder((getHopCount() * 30) + 50);
        stringBuilder.append("RouteTracker[");
        if (this.localAddress != null) {
            stringBuilder.append(this.localAddress);
            stringBuilder.append("->");
        }
        stringBuilder.append('{');
        if (this.connected) {
            stringBuilder.append('c');
        }
        if (this.tunnelled == TunnelType.TUNNELLED) {
            stringBuilder.append('t');
        }
        if (this.layered == LayerType.LAYERED) {
            stringBuilder.append('l');
        }
        if (this.secure) {
            stringBuilder.append('s');
        }
        stringBuilder.append("}->");
        if (this.proxyChain != null) {
            for (Object append : this.proxyChain) {
                stringBuilder.append(append);
                stringBuilder.append("->");
            }
        }
        stringBuilder.append(this.targetHost);
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    public final void tunnelProxy(HttpHost httpHost, boolean z) {
        if (httpHost == null) {
            throw new IllegalArgumentException("Proxy host may not be null.");
        } else if (!this.connected) {
            throw new IllegalStateException("No tunnel unless connected.");
        } else if (this.proxyChain == null) {
            throw new IllegalStateException("No proxy tunnel without proxy.");
        } else {
            Object obj = new HttpHost[(this.proxyChain.length + 1)];
            System.arraycopy(this.proxyChain, 0, obj, 0, this.proxyChain.length);
            obj[obj.length - 1] = httpHost;
            this.proxyChain = obj;
            this.secure = z;
        }
    }

    public final void tunnelTarget(boolean z) {
        if (!this.connected) {
            throw new IllegalStateException("No tunnel unless connected.");
        } else if (this.proxyChain == null) {
            throw new IllegalStateException("No tunnel without proxy.");
        } else {
            this.tunnelled = TunnelType.TUNNELLED;
            this.secure = z;
        }
    }
}
