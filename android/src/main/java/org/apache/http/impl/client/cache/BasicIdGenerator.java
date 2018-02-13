package org.apache.http.impl.client.cache;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Formatter;
import java.util.Locale;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;

@ThreadSafe
class BasicIdGenerator {
    @GuardedBy("this")
    private long count;
    private final String hostname;
    private final SecureRandom rnd;

    public BasicIdGenerator() {
        String hostName;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            hostName = "localhost";
        }
        this.hostname = hostName;
        try {
            this.rnd = SecureRandom.getInstance("SHA1PRNG");
            this.rnd.setSeed(System.currentTimeMillis());
        } catch (Throwable e2) {
            throw new Error(e2);
        }
    }

    public String generate() {
        StringBuilder stringBuilder = new StringBuilder();
        generate(stringBuilder);
        return stringBuilder.toString();
    }

    public void generate(StringBuilder stringBuilder) {
        synchronized (this) {
            this.count++;
            int nextInt = this.rnd.nextInt();
            stringBuilder.append(System.currentTimeMillis());
            stringBuilder.append('.');
            new Formatter(stringBuilder, Locale.US).format("%1$016x-%2$08x", new Object[]{Long.valueOf(this.count), Integer.valueOf(nextInt)});
            stringBuilder.append('.');
            stringBuilder.append(this.hostname);
        }
    }
}
