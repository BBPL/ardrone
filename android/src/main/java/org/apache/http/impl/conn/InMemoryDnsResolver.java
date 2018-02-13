package org.apache.http.impl.conn;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.DnsResolver;

public class InMemoryDnsResolver implements DnsResolver {
    private Map<String, InetAddress[]> dnsMap = new ConcurrentHashMap();
    private final Log log = LogFactory.getLog(InMemoryDnsResolver.class);

    public void add(String str, InetAddress... inetAddressArr) {
        if (str == null) {
            throw new IllegalArgumentException("Host name may not be null");
        } else if (inetAddressArr == null) {
            throw new IllegalArgumentException("Array of IP addresses may not be null");
        } else {
            this.dnsMap.put(str, inetAddressArr);
        }
    }

    public InetAddress[] resolve(String str) throws UnknownHostException {
        InetAddress[] inetAddressArr = (InetAddress[]) this.dnsMap.get(str);
        if (this.log.isInfoEnabled()) {
            this.log.info("Resolving " + str + " to " + Arrays.deepToString(inetAddressArr));
        }
        if (inetAddressArr != null) {
            return inetAddressArr;
        }
        throw new UnknownHostException(str + " cannot be resolved");
    }
}
