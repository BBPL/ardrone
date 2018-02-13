package org.apache.http.protocol;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.mortbay.jetty.security.Constraint;

@ThreadSafe
public class UriPatternMatcher<T> {
    @GuardedBy("this")
    private final Map<String, T> map = new HashMap();

    public Map<String, T> getObjects() {
        Map<String, T> map;
        synchronized (this) {
            map = this.map;
        }
        return map;
    }

    public T lookup(String str) {
        T t;
        synchronized (this) {
            if (str == null) {
                throw new IllegalArgumentException("Request URI may not be null");
            }
            int indexOf = str.indexOf("?");
            if (indexOf != -1) {
                str = str.substring(0, indexOf);
            }
            T t2 = this.map.get(str);
            if (t2 == null) {
                t = t2;
                String str2 = null;
                for (String str3 : this.map.keySet()) {
                    if (matchUriRequestPattern(str3, str) && (r1 == null || r1.length() < str3.length() || (r1.length() == str3.length() && str3.endsWith(Constraint.ANY_ROLE)))) {
                        t = this.map.get(str3);
                        str2 = str3;
                    }
                }
            } else {
                t = t2;
            }
        }
        return t;
    }

    protected boolean matchUriRequestPattern(String str, String str2) {
        boolean z = false;
        if (str.equals(Constraint.ANY_ROLE)) {
            return true;
        }
        if ((str.endsWith(Constraint.ANY_ROLE) && str2.startsWith(str.substring(0, str.length() - 1))) || (str.startsWith(Constraint.ANY_ROLE) && str2.endsWith(str.substring(1, str.length())))) {
            z = true;
        }
        return z;
    }

    public void register(String str, T t) {
        synchronized (this) {
            if (str == null) {
                throw new IllegalArgumentException("URI request pattern may not be null");
            }
            this.map.put(str, t);
        }
    }

    @Deprecated
    public void setHandlers(Map<String, T> map) {
        synchronized (this) {
            if (map == null) {
                throw new IllegalArgumentException("Map of handlers may not be null");
            }
            this.map.clear();
            this.map.putAll(map);
        }
    }

    public void setObjects(Map<String, T> map) {
        synchronized (this) {
            if (map == null) {
                throw new IllegalArgumentException("Map of handlers may not be null");
            }
            this.map.clear();
            this.map.putAll(map);
        }
    }

    public void unregister(String str) {
        synchronized (this) {
            if (str != null) {
                this.map.remove(str);
            }
        }
    }
}
