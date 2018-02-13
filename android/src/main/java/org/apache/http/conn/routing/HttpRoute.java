package org.apache.http.conn.routing;

import java.net.InetAddress;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.routing.RouteInfo.LayerType;
import org.apache.http.conn.routing.RouteInfo.TunnelType;
import org.apache.http.util.LangUtils;

@Immutable
public final class HttpRoute implements RouteInfo, Cloneable {
    private static final HttpHost[] EMPTY_HTTP_HOST_ARRAY = new HttpHost[0];
    private final LayerType layered;
    private final InetAddress localAddress;
    private final HttpHost[] proxyChain;
    private final boolean secure;
    private final HttpHost targetHost;
    private final TunnelType tunnelled;

    private HttpRoute(InetAddress inetAddress, HttpHost httpHost, HttpHost[] httpHostArr, boolean z, TunnelType tunnelType, LayerType layerType) {
        if (httpHost == null) {
            throw new IllegalArgumentException("Target host may not be null.");
        } else if (httpHostArr == null) {
            throw new IllegalArgumentException("Proxies may not be null.");
        } else if (tunnelType == TunnelType.TUNNELLED && httpHostArr.length == 0) {
            throw new IllegalArgumentException("Proxy required if tunnelled.");
        } else {
            if (tunnelType == null) {
                tunnelType = TunnelType.PLAIN;
            }
            if (layerType == null) {
                layerType = LayerType.PLAIN;
            }
            this.targetHost = httpHost;
            this.localAddress = inetAddress;
            this.proxyChain = httpHostArr;
            this.secure = z;
            this.tunnelled = tunnelType;
            this.layered = layerType;
        }
    }

    public HttpRoute(HttpHost httpHost) {
        this(null, httpHost, EMPTY_HTTP_HOST_ARRAY, false, TunnelType.PLAIN, LayerType.PLAIN);
    }

    public HttpRoute(HttpHost httpHost, InetAddress inetAddress, HttpHost httpHost2, boolean z) {
        this(inetAddress, httpHost, toChain(httpHost2), z, z ? TunnelType.TUNNELLED : TunnelType.PLAIN, z ? LayerType.LAYERED : LayerType.PLAIN);
        if (httpHost2 == null) {
            throw new IllegalArgumentException("Proxy host may not be null.");
        }
    }

    public HttpRoute(HttpHost httpHost, InetAddress inetAddress, HttpHost httpHost2, boolean z, TunnelType tunnelType, LayerType layerType) {
        this(inetAddress, httpHost, toChain(httpHost2), z, tunnelType, layerType);
    }

    public HttpRoute(HttpHost httpHost, InetAddress inetAddress, boolean z) {
        this(inetAddress, httpHost, EMPTY_HTTP_HOST_ARRAY, z, TunnelType.PLAIN, LayerType.PLAIN);
    }

    public HttpRoute(HttpHost httpHost, InetAddress inetAddress, HttpHost[] httpHostArr, boolean z, TunnelType tunnelType, LayerType layerType) {
        this(inetAddress, httpHost, toChain(httpHostArr), z, tunnelType, layerType);
    }

    private static HttpHost[] toChain(HttpHost httpHost) {
        if (httpHost == null) {
            return EMPTY_HTTP_HOST_ARRAY;
        }
        return new HttpHost[]{httpHost};
    }

    private static HttpHost[] toChain(HttpHost[] httpHostArr) {
        if (httpHostArr == null || httpHostArr.length < 1) {
            return EMPTY_HTTP_HOST_ARRAY;
        }
        for (HttpHost httpHost : httpHostArr) {
            if (httpHost == null) {
                throw new IllegalArgumentException("Proxy chain may not contain null elements.");
            }
        }
        Object obj = new HttpHost[httpHostArr.length];
        System.arraycopy(httpHostArr, 0, obj, 0, httpHostArr.length);
        return obj;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public final boolean equals(Object obj) {
        if (this != obj) {
            if (!(obj instanceof HttpRoute)) {
                return false;
            }
            HttpRoute httpRoute = (HttpRoute) obj;
            if (this.secure != httpRoute.secure || this.tunnelled != httpRoute.tunnelled || this.layered != httpRoute.layered || !LangUtils.equals(this.targetHost, httpRoute.targetHost) || !LangUtils.equals(this.localAddress, httpRoute.localAddress)) {
                return false;
            }
            if (!LangUtils.equals(this.proxyChain, httpRoute.proxyChain)) {
                return false;
            }
        }
        return true;
    }

    public final int getHopCount() {
        return this.proxyChain.length + 1;
    }

    public final HttpHost getHopTarget(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Hop index must not be negative: " + i);
        }
        int hopCount = getHopCount();
        if (i < hopCount) {
            return i < hopCount + -1 ? this.proxyChain[i] : this.targetHost;
        } else {
            throw new IllegalArgumentException("Hop index " + i + " exceeds route length " + hopCount);
        }
    }

    public final LayerType getLayerType() {
        return this.layered;
    }

    public final InetAddress getLocalAddress() {
        return this.localAddress;
    }

    public final HttpHost getProxyHost() {
        return this.proxyChain.length == 0 ? null : this.proxyChain[0];
    }

    public final HttpHost getTargetHost() {
        return this.targetHost;
    }

    public final TunnelType getTunnelType() {
        return this.tunnelled;
    }

    public final int hashCode() {
        int hashCode = LangUtils.hashCode(LangUtils.hashCode(17, this.targetHost), this.localAddress);
        for (Object hashCode2 : this.proxyChain) {
            hashCode = LangUtils.hashCode(hashCode, hashCode2);
        }
        return LangUtils.hashCode(LangUtils.hashCode(LangUtils.hashCode(hashCode, this.secure), this.tunnelled), this.layered);
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

    public final String toString() {
        StringBuilder stringBuilder = new StringBuilder((getHopCount() * 30) + 50);
        if (this.localAddress != null) {
            stringBuilder.append(this.localAddress);
            stringBuilder.append("->");
        }
        stringBuilder.append('{');
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
        for (Object append : this.proxyChain) {
            stringBuilder.append(append);
            stringBuilder.append("->");
        }
        stringBuilder.append(this.targetHost);
        return stringBuilder.toString();
    }
}
