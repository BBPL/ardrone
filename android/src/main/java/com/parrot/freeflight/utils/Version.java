package com.parrot.freeflight.utils;

import java.util.StringTokenizer;

public class Version {
    private int major;
    private int minor;
    private int release;

    public Version(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, ".");
        int i = 0;
        while (stringTokenizer.hasMoreTokens()) {
            int parseInt = Integer.parseInt(stringTokenizer.nextToken());
            if (i == 0) {
                this.major = parseInt;
            } else if (i == 1) {
                this.minor = parseInt;
            } else if (i == 2) {
                this.release = parseInt;
            }
            i++;
        }
    }

    public boolean isGreater(Version version) {
        if (this.major <= version.major) {
            if (this.major < version.major) {
                return false;
            }
            if (this.minor <= version.minor) {
                if (this.minor < version.minor) {
                    return false;
                }
                if (this.release <= version.release) {
                    return this.release < version.release ? false : false;
                }
            }
        }
        return true;
    }

    public boolean isLower(Version version) {
        if (this.major >= version.major) {
            if (this.major > version.major) {
                return false;
            }
            if (this.minor >= version.minor) {
                if (this.minor > version.minor) {
                    return false;
                }
                if (this.release >= version.release) {
                    return this.release > version.release ? false : false;
                }
            }
        }
        return true;
    }
}
