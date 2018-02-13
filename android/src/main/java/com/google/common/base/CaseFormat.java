package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import org.mortbay.jetty.HttpVersions;

@GwtCompatible
public enum CaseFormat {
    LOWER_HYPHEN(CharMatcher.is('-'), "-"),
    LOWER_UNDERSCORE(CharMatcher.is('_'), "_"),
    LOWER_CAMEL(CharMatcher.inRange('A', 'Z'), HttpVersions.HTTP_0_9),
    UPPER_CAMEL(CharMatcher.inRange('A', 'Z'), HttpVersions.HTTP_0_9),
    UPPER_UNDERSCORE(CharMatcher.is('_'), "_");
    
    private final CharMatcher wordBoundary;
    private final String wordSeparator;

    private CaseFormat(CharMatcher charMatcher, String str) {
        this.wordBoundary = charMatcher;
        this.wordSeparator = str;
    }

    private static String firstCharOnlyToUpper(String str) {
        int length = str.length();
        return length == 0 ? str : Ascii.toUpperCase(str.charAt(0)) + Ascii.toLowerCase(str.substring(1));
    }

    private String normalizeFirstWord(String str) {
        switch (this) {
            case LOWER_CAMEL:
                return Ascii.toLowerCase(str);
            default:
                return normalizeWord(str);
        }
    }

    private String normalizeWord(String str) {
        switch (this) {
            case LOWER_UNDERSCORE:
                return Ascii.toLowerCase(str);
            case UPPER_UNDERSCORE:
                return Ascii.toUpperCase(str);
            case LOWER_HYPHEN:
                return Ascii.toLowerCase(str);
            case LOWER_CAMEL:
                return firstCharOnlyToUpper(str);
            case UPPER_CAMEL:
                return firstCharOnlyToUpper(str);
            default:
                throw new RuntimeException("unknown case: " + this);
        }
    }

    public String to(CaseFormat caseFormat, String str) {
        if (caseFormat == null) {
            throw new NullPointerException();
        } else if (str == null) {
            throw new NullPointerException();
        } else if (caseFormat == this) {
            return str;
        } else {
            switch (this) {
                case LOWER_UNDERSCORE:
                    switch (caseFormat) {
                        case UPPER_UNDERSCORE:
                            return Ascii.toUpperCase(str);
                        case LOWER_HYPHEN:
                            return str.replace('_', '-');
                        default:
                            break;
                    }
                case UPPER_UNDERSCORE:
                    switch (caseFormat) {
                        case LOWER_UNDERSCORE:
                            return Ascii.toLowerCase(str);
                        case LOWER_HYPHEN:
                            return Ascii.toLowerCase(str.replace('_', '-'));
                        default:
                            break;
                    }
                case LOWER_HYPHEN:
                    switch (caseFormat) {
                        case LOWER_UNDERSCORE:
                            return str.replace('-', '_');
                        case UPPER_UNDERSCORE:
                            return Ascii.toUpperCase(str.replace('-', '_'));
                        default:
                            break;
                    }
            }
            StringBuilder stringBuilder = null;
            int i = 0;
            int i2 = -1;
            while (true) {
                i2 = this.wordBoundary.indexIn(str, i2 + 1);
                if (i2 != -1) {
                    if (i == 0) {
                        stringBuilder = new StringBuilder(str.length() + (this.wordSeparator.length() * 4));
                        stringBuilder.append(caseFormat.normalizeFirstWord(str.substring(i, i2)));
                    } else {
                        stringBuilder.append(caseFormat.normalizeWord(str.substring(i, i2)));
                    }
                    stringBuilder.append(caseFormat.wordSeparator);
                    i = this.wordSeparator.length() + i2;
                } else if (i == 0) {
                    return caseFormat.normalizeFirstWord(str);
                } else {
                    stringBuilder.append(caseFormat.normalizeWord(str.substring(i)));
                    return stringBuilder.toString();
                }
            }
        }
    }
}
