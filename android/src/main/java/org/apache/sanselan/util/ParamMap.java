package org.apache.sanselan.util;

import java.util.Map;

public class ParamMap {
    public static boolean getParamBoolean(Map map, Object obj, boolean z) {
        Object obj2 = map == null ? null : map.get(obj);
        return (obj2 == null || !(obj2 instanceof Boolean)) ? z : ((Boolean) obj2).booleanValue();
    }
}
