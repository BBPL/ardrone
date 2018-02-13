package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Ascii;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
@Beta
public final class InternetDomainName {
    private static final CharMatcher DASH_MATCHER = CharMatcher.anyOf("-_");
    private static final CharMatcher DOTS_MATCHER = CharMatcher.anyOf(".。．｡");
    private static final Joiner DOT_JOINER = Joiner.on('.');
    private static final String DOT_REGEX = "\\.";
    private static final Splitter DOT_SPLITTER = Splitter.on('.');
    private static final int MAX_DOMAIN_PART_LENGTH = 63;
    private static final int MAX_LENGTH = 253;
    private static final int MAX_PARTS = 127;
    private static final int NO_PUBLIC_SUFFIX_FOUND = -1;
    private static final CharMatcher PART_CHAR_MATCHER = CharMatcher.JAVA_LETTER_OR_DIGIT.or(DASH_MATCHER);
    private final String name;
    private final ImmutableList<String> parts;
    private final int publicSuffixIndex;

    InternetDomainName(String str) {
        CharSequence toLowerCase = Ascii.toLowerCase(DOTS_MATCHER.replaceFrom((CharSequence) str, '.'));
        if (toLowerCase.endsWith(".")) {
            toLowerCase = toLowerCase.substring(0, toLowerCase.length() - 1);
        }
        Preconditions.checkArgument(toLowerCase.length() <= MAX_LENGTH, "Domain name too long: '%s':", toLowerCase);
        this.name = toLowerCase;
        this.parts = ImmutableList.copyOf(DOT_SPLITTER.split(toLowerCase));
        Preconditions.checkArgument(this.parts.size() <= MAX_PARTS, "Domain has too many parts: '%s'", toLowerCase);
        Preconditions.checkArgument(validateSyntax(this.parts), "Not a valid domain name: '%s'", toLowerCase);
        this.publicSuffixIndex = findPublicSuffix();
    }

    private InternetDomainName ancestor(int i) {
        return from(DOT_JOINER.join(this.parts.subList(i, this.parts.size())));
    }

    private int findPublicSuffix() {
        int size = this.parts.size();
        for (int i = 0; i < size; i++) {
            String join = DOT_JOINER.join(this.parts.subList(i, size));
            if (TldPatterns.EXACT.contains(join)) {
                return i;
            }
            if (TldPatterns.EXCLUDED.contains(join)) {
                return i + 1;
            }
            if (matchesWildcardPublicSuffix(join)) {
                return i;
            }
        }
        return -1;
    }

    public static InternetDomainName from(String str) {
        return new InternetDomainName((String) Preconditions.checkNotNull(str));
    }

    @Deprecated
    public static InternetDomainName fromLenient(String str) {
        return from(str);
    }

    public static boolean isValid(String str) {
        try {
            from(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Deprecated
    public static boolean isValidLenient(String str) {
        return isValid(str);
    }

    private static boolean matchesWildcardPublicSuffix(String str) {
        String[] split = str.split(DOT_REGEX, 2);
        return split.length == 2 && TldPatterns.UNDER.contains(split[1]);
    }

    private static boolean validatePart(String str, boolean z) {
        if (str.length() >= 1 && str.length() <= 63) {
            if (!(!PART_CHAR_MATCHER.matchesAllOf(CharMatcher.ASCII.retainFrom(str)) || DASH_MATCHER.matches(str.charAt(0)) || DASH_MATCHER.matches(str.charAt(str.length() - 1)))) {
                if (!z) {
                    return true;
                }
                if (!CharMatcher.DIGIT.matches(str.charAt(0))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean validateSyntax(List<String> list) {
        int size = list.size() - 1;
        if (!validatePart((String) list.get(size), true)) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!validatePart((String) list.get(i), false)) {
                return false;
            }
        }
        return true;
    }

    public InternetDomainName child(String str) {
        return from(((String) Preconditions.checkNotNull(str)) + "." + this.name);
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof InternetDomainName)) {
            return false;
        }
        return this.name.equals(((InternetDomainName) obj).name);
    }

    public boolean hasParent() {
        return this.parts.size() > 1;
    }

    public boolean hasPublicSuffix() {
        return this.publicSuffixIndex != -1;
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public boolean isPublicSuffix() {
        return this.publicSuffixIndex == 0;
    }

    public boolean isTopPrivateDomain() {
        return this.publicSuffixIndex == 1;
    }

    public boolean isUnderPublicSuffix() {
        return this.publicSuffixIndex > 0;
    }

    public String name() {
        return this.name;
    }

    public InternetDomainName parent() {
        Preconditions.checkState(hasParent(), "Domain '%s' has no parent", this.name);
        return ancestor(1);
    }

    public ImmutableList<String> parts() {
        return this.parts;
    }

    public InternetDomainName publicSuffix() {
        return hasPublicSuffix() ? ancestor(this.publicSuffixIndex) : null;
    }

    public String toString() {
        return Objects.toStringHelper((Object) this).add("name", this.name).toString();
    }

    public InternetDomainName topPrivateDomain() {
        if (isTopPrivateDomain()) {
            return this;
        }
        Preconditions.checkState(isUnderPublicSuffix(), "Not under a public suffix: %s", this.name);
        return ancestor(this.publicSuffixIndex - 1);
    }
}
