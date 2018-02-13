package com.parrot.freeflight.utils;

import org.mortbay.jetty.HttpVersions;

public class DeviceNameEncoder {
    public static String decode(String str) {
        return str == null ? null : str.replaceAll("_", " ");
    }

    public static String encode(String str) {
        return str == null ? null : str.replaceAll("-", "_").replaceAll("\\s", "_").replaceAll("[^a-zA-Z0-9_]", HttpVersions.HTTP_0_9);
    }
}
