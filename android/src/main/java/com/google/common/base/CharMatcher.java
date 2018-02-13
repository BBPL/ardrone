package com.google.common.base;

import android.support.v4.view.accessibility.AccessibilityEventCompat;
import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.Arrays;
import javax.annotation.CheckReturnValue;
import org.mortbay.jetty.HttpVersions;

@GwtCompatible
@Beta
public abstract class CharMatcher implements Predicate<Character> {
    public static final CharMatcher ANY = new CharMatcher("CharMatcher.ANY") {
        public CharMatcher and(CharMatcher charMatcher) {
            return (CharMatcher) Preconditions.checkNotNull(charMatcher);
        }

        public /* bridge */ /* synthetic */ boolean apply(Object obj) {
            return super.apply((Character) obj);
        }

        public String collapseFrom(CharSequence charSequence, char c) {
            return charSequence.length() == 0 ? HttpVersions.HTTP_0_9 : String.valueOf(c);
        }

        public int countIn(CharSequence charSequence) {
            return charSequence.length();
        }

        public int indexIn(CharSequence charSequence) {
            return charSequence.length() == 0 ? -1 : 0;
        }

        public int indexIn(CharSequence charSequence, int i) {
            int length = charSequence.length();
            Preconditions.checkPositionIndex(i, length);
            return i == length ? -1 : i;
        }

        public int lastIndexIn(CharSequence charSequence) {
            return charSequence.length() - 1;
        }

        public boolean matches(char c) {
            return true;
        }

        public boolean matchesAllOf(CharSequence charSequence) {
            Preconditions.checkNotNull(charSequence);
            return true;
        }

        public boolean matchesNoneOf(CharSequence charSequence) {
            return charSequence.length() == 0;
        }

        public CharMatcher negate() {
            return NONE;
        }

        public CharMatcher or(CharMatcher charMatcher) {
            Preconditions.checkNotNull(charMatcher);
            return this;
        }

        public CharMatcher precomputed() {
            return this;
        }

        public String removeFrom(CharSequence charSequence) {
            Preconditions.checkNotNull(charSequence);
            return HttpVersions.HTTP_0_9;
        }

        public String replaceFrom(CharSequence charSequence, char c) {
            char[] cArr = new char[charSequence.length()];
            Arrays.fill(cArr, c);
            return new String(cArr);
        }

        public String replaceFrom(CharSequence charSequence, CharSequence charSequence2) {
            StringBuilder stringBuilder = new StringBuilder(charSequence.length() * charSequence2.length());
            for (int i = 0; i < charSequence.length(); i++) {
                stringBuilder.append(charSequence2);
            }
            return stringBuilder.toString();
        }

        public String trimFrom(CharSequence charSequence) {
            Preconditions.checkNotNull(charSequence);
            return HttpVersions.HTTP_0_9;
        }
    };
    public static final CharMatcher ASCII = inRange('\u0000', Ascii.MAX, "CharMatcher.ASCII");
    public static final CharMatcher BREAKING_WHITESPACE = anyOf("\t\n\u000b\f\r     　").or(inRange(' ', ' ')).or(inRange(' ', ' ')).withToString("CharMatcher.BREAKING_WHITESPACE").precomputed();
    public static final CharMatcher DIGIT;
    public static final CharMatcher INVISIBLE = inRange('\u0000', ' ').or(inRange(Ascii.MAX, ' ')).or(is('­')).or(inRange('؀', '؄')).or(anyOf("۝܏ ᠎")).or(inRange(' ', '‏')).or(inRange(' ', ' ')).or(inRange(' ', '⁤')).or(inRange('⁪', '⁯')).or(is('　')).or(inRange('?', '')).or(anyOf("﻿￹￺￻")).withToString("CharMatcher.INVISIBLE").precomputed();
    public static final CharMatcher JAVA_DIGIT = new CharMatcher("CharMatcher.JAVA_DIGIT") {
        public /* bridge */ /* synthetic */ boolean apply(Object obj) {
            return super.apply((Character) obj);
        }

        public boolean matches(char c) {
            return Character.isDigit(c);
        }
    };
    public static final CharMatcher JAVA_ISO_CONTROL = inRange('\u0000', '\u001f').or(inRange(Ascii.MAX, '')).withToString("CharMatcher.JAVA_ISO_CONTROL");
    public static final CharMatcher JAVA_LETTER = new CharMatcher("CharMatcher.JAVA_LETTER") {
        public /* bridge */ /* synthetic */ boolean apply(Object obj) {
            return super.apply((Character) obj);
        }

        public boolean matches(char c) {
            return Character.isLetter(c);
        }

        public CharMatcher precomputed() {
            return this;
        }
    };
    public static final CharMatcher JAVA_LETTER_OR_DIGIT = new CharMatcher("CharMatcher.JAVA_LETTER_OR_DIGIT") {
        public /* bridge */ /* synthetic */ boolean apply(Object obj) {
            return super.apply((Character) obj);
        }

        public boolean matches(char c) {
            return Character.isLetterOrDigit(c);
        }
    };
    public static final CharMatcher JAVA_LOWER_CASE = new CharMatcher("CharMatcher.JAVA_LOWER_CASE") {
        public /* bridge */ /* synthetic */ boolean apply(Object obj) {
            return super.apply((Character) obj);
        }

        public boolean matches(char c) {
            return Character.isLowerCase(c);
        }
    };
    public static final CharMatcher JAVA_UPPER_CASE = new CharMatcher("CharMatcher.JAVA_UPPER_CASE") {
        public /* bridge */ /* synthetic */ boolean apply(Object obj) {
            return super.apply((Character) obj);
        }

        public boolean matches(char c) {
            return Character.isUpperCase(c);
        }
    };
    public static final CharMatcher NONE = new CharMatcher("CharMatcher.NONE") {
        public CharMatcher and(CharMatcher charMatcher) {
            Preconditions.checkNotNull(charMatcher);
            return this;
        }

        public /* bridge */ /* synthetic */ boolean apply(Object obj) {
            return super.apply((Character) obj);
        }

        public String collapseFrom(CharSequence charSequence, char c) {
            return charSequence.toString();
        }

        public int countIn(CharSequence charSequence) {
            Preconditions.checkNotNull(charSequence);
            return 0;
        }

        public int indexIn(CharSequence charSequence) {
            Preconditions.checkNotNull(charSequence);
            return -1;
        }

        public int indexIn(CharSequence charSequence, int i) {
            Preconditions.checkPositionIndex(i, charSequence.length());
            return -1;
        }

        public int lastIndexIn(CharSequence charSequence) {
            Preconditions.checkNotNull(charSequence);
            return -1;
        }

        public boolean matches(char c) {
            return false;
        }

        public boolean matchesAllOf(CharSequence charSequence) {
            return charSequence.length() == 0;
        }

        public boolean matchesNoneOf(CharSequence charSequence) {
            Preconditions.checkNotNull(charSequence);
            return true;
        }

        public CharMatcher negate() {
            return ANY;
        }

        public CharMatcher or(CharMatcher charMatcher) {
            return (CharMatcher) Preconditions.checkNotNull(charMatcher);
        }

        public CharMatcher precomputed() {
            return this;
        }

        public String removeFrom(CharSequence charSequence) {
            return charSequence.toString();
        }

        public String replaceFrom(CharSequence charSequence, char c) {
            return charSequence.toString();
        }

        public String replaceFrom(CharSequence charSequence, CharSequence charSequence2) {
            Preconditions.checkNotNull(charSequence2);
            return charSequence.toString();
        }

        void setBits(LookupTable lookupTable) {
        }

        public String trimFrom(CharSequence charSequence) {
            return charSequence.toString();
        }
    };
    public static final CharMatcher SINGLE_WIDTH = inRange('\u0000', 'ӹ').or(is('־')).or(inRange('א', 'ת')).or(is('׳')).or(is('״')).or(inRange('؀', 'ۿ')).or(inRange('ݐ', 'ݿ')).or(inRange('฀', '๿')).or(inRange('Ḁ', '₯')).or(inRange('℀', '℺')).or(inRange('ﭐ', '﷿')).or(inRange('ﹰ', '﻿')).or(inRange('｡', 'ￜ')).withToString("CharMatcher.SINGLE_WIDTH").precomputed();
    public static final CharMatcher WHITESPACE = new CharMatcher("CharMatcher.WHITESPACE") {
        private final char[] table = new char[]{'\u0001', '\u0000', ' ', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\t', '\n', '\u000b', '\f', '\r', '\u0000', '\u0000', ' ', ' ', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', ' ', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', ' ', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '　', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', ' ', ' ', '\u0000', '\u0000', '᠎', '\u0000', '\u0000', '\u0000'};

        public /* bridge */ /* synthetic */ boolean apply(Object obj) {
            return super.apply((Character) obj);
        }

        public boolean matches(char c) {
            return this.table[c % 79] == c;
        }

        public CharMatcher precomputed() {
            return this;
        }
    };
    final String description;

    private static class And extends CharMatcher {
        final CharMatcher first;
        final CharMatcher second;

        And(CharMatcher charMatcher, CharMatcher charMatcher2) {
            this(charMatcher, charMatcher2, "CharMatcher.and(" + charMatcher + ", " + charMatcher2 + ")");
        }

        And(CharMatcher charMatcher, CharMatcher charMatcher2, String str) {
            super(str);
            this.first = (CharMatcher) Preconditions.checkNotNull(charMatcher);
            this.second = (CharMatcher) Preconditions.checkNotNull(charMatcher2);
        }

        public CharMatcher and(CharMatcher charMatcher) {
            return new And(this, charMatcher);
        }

        public /* bridge */ /* synthetic */ boolean apply(Object obj) {
            return super.apply((Character) obj);
        }

        public boolean matches(char c) {
            return this.first.matches(c) && this.second.matches(c);
        }

        CharMatcher withToString(String str) {
            return new And(this.first, this.second, str);
        }
    }

    private static final class LookupTable {
        int[] data;

        private LookupTable() {
            this.data = new int[2048];
        }

        boolean get(char c) {
            return (this.data[c >> 5] & (1 << c)) != 0;
        }

        void set(char c) {
            int[] iArr = this.data;
            int i = c >> 5;
            iArr[i] = iArr[i] | (1 << c);
        }
    }

    private static class Or extends CharMatcher {
        final CharMatcher first;
        final CharMatcher second;

        Or(CharMatcher charMatcher, CharMatcher charMatcher2) {
            this(charMatcher, charMatcher2, "CharMatcher.or(" + charMatcher + ", " + charMatcher2 + ")");
        }

        Or(CharMatcher charMatcher, CharMatcher charMatcher2, String str) {
            super(str);
            this.first = (CharMatcher) Preconditions.checkNotNull(charMatcher);
            this.second = (CharMatcher) Preconditions.checkNotNull(charMatcher2);
        }

        public /* bridge */ /* synthetic */ boolean apply(Object obj) {
            return super.apply((Character) obj);
        }

        public boolean matches(char c) {
            return this.first.matches(c) || this.second.matches(c);
        }

        public CharMatcher or(CharMatcher charMatcher) {
            return new Or(this, (CharMatcher) Preconditions.checkNotNull(charMatcher));
        }

        CharMatcher withToString(String str) {
            return new Or(this.first, this.second, str);
        }
    }

    static {
        CharMatcher inRange = inRange('0', '9');
        for (char c : "٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".toCharArray()) {
            inRange = inRange.or(inRange(c, (char) (c + 9)));
        }
        DIGIT = inRange.withToString("CharMatcher.DIGIT").precomputed();
    }

    protected CharMatcher() {
        this.description = "UnknownCharMatcher";
    }

    CharMatcher(String str) {
        this.description = str;
    }

    public static CharMatcher anyOf(CharSequence charSequence) {
        switch (charSequence.length()) {
            case 0:
                return NONE;
            case 1:
                return is(charSequence.charAt(0));
            case 2:
                final char charAt = charSequence.charAt(0);
                final char charAt2 = charSequence.charAt(1);
                return new CharMatcher("CharMatcher.anyOf(\"" + charSequence + "\")") {
                    public /* bridge */ /* synthetic */ boolean apply(Object obj) {
                        return super.apply((Character) obj);
                    }

                    public boolean matches(char c) {
                        return c == charAt || c == charAt2;
                    }

                    public CharMatcher precomputed() {
                        return this;
                    }

                    void setBits(LookupTable lookupTable) {
                        lookupTable.set(charAt);
                        lookupTable.set(charAt2);
                    }
                };
            default:
                final char[] toCharArray = charSequence.toString().toCharArray();
                Arrays.sort(toCharArray);
                return new CharMatcher("CharMatcher.anyOf(\"" + toCharArray + "\")") {
                    public /* bridge */ /* synthetic */ boolean apply(Object obj) {
                        return super.apply((Character) obj);
                    }

                    public boolean matches(char c) {
                        return Arrays.binarySearch(toCharArray, c) >= 0;
                    }
                };
        }
    }

    public static CharMatcher forPredicate(final Predicate<? super Character> predicate) {
        Preconditions.checkNotNull(predicate);
        return predicate instanceof CharMatcher ? (CharMatcher) predicate : new CharMatcher("CharMatcher.forPredicate(" + predicate + ')') {
            public boolean apply(Character ch) {
                return predicate.apply(Preconditions.checkNotNull(ch));
            }

            public boolean matches(char c) {
                return predicate.apply(Character.valueOf(c));
            }
        };
    }

    public static CharMatcher inRange(char c, char c2) {
        Preconditions.checkArgument(c2 >= c);
        return inRange(c, c2, "CharMatcher.inRange(" + Integer.toHexString(c) + ", " + Integer.toHexString(c2) + ")");
    }

    static CharMatcher inRange(final char c, final char c2, String str) {
        return new CharMatcher(str) {
            public /* bridge */ /* synthetic */ boolean apply(Object obj) {
                return super.apply((Character) obj);
            }

            public boolean matches(char c) {
                return c <= c && c <= c2;
            }

            public CharMatcher precomputed() {
                return this;
            }

            void setBits(LookupTable lookupTable) {
                char c = c;
                while (true) {
                    lookupTable.set(c);
                    char c2 = (char) (c + 1);
                    if (c != c2) {
                        c = c2;
                    } else {
                        return;
                    }
                }
            }
        };
    }

    public static CharMatcher is(final char c) {
        return new CharMatcher("CharMatcher.is(" + Integer.toHexString(c) + ")") {
            public CharMatcher and(CharMatcher charMatcher) {
                return charMatcher.matches(c) ? this : NONE;
            }

            public /* bridge */ /* synthetic */ boolean apply(Object obj) {
                return super.apply((Character) obj);
            }

            public boolean matches(char c) {
                return c == c;
            }

            public CharMatcher negate() {
                return CharMatcher.isNot(c);
            }

            public CharMatcher or(CharMatcher charMatcher) {
                return charMatcher.matches(c) ? charMatcher : super.or(charMatcher);
            }

            public CharMatcher precomputed() {
                return this;
            }

            public String replaceFrom(CharSequence charSequence, char c) {
                return charSequence.toString().replace(c, c);
            }

            void setBits(LookupTable lookupTable) {
                lookupTable.set(c);
            }
        };
    }

    public static CharMatcher isNot(final char c) {
        return new CharMatcher("CharMatcher.isNot(" + Integer.toHexString(c) + ")") {
            public CharMatcher and(CharMatcher charMatcher) {
                return charMatcher.matches(c) ? super.and(charMatcher) : charMatcher;
            }

            public /* bridge */ /* synthetic */ boolean apply(Object obj) {
                return super.apply((Character) obj);
            }

            public boolean matches(char c) {
                return c != c;
            }

            public CharMatcher negate() {
                return CharMatcher.is(c);
            }

            public CharMatcher or(CharMatcher charMatcher) {
                return charMatcher.matches(c) ? ANY : this;
            }
        };
    }

    public static CharMatcher noneOf(CharSequence charSequence) {
        return anyOf(charSequence).negate();
    }

    public CharMatcher and(CharMatcher charMatcher) {
        return new And(this, (CharMatcher) Preconditions.checkNotNull(charMatcher));
    }

    public boolean apply(Character ch) {
        return matches(ch.charValue());
    }

    @CheckReturnValue
    public String collapseFrom(CharSequence charSequence, char c) {
        int indexIn = indexIn(charSequence);
        if (indexIn == -1) {
            return charSequence.toString();
        }
        StringBuilder append = new StringBuilder(charSequence.length()).append(charSequence.subSequence(0, indexIn)).append(c);
        indexIn = 1;
        for (int i = indexIn + 1; i < charSequence.length(); i++) {
            char charAt = charSequence.charAt(i);
            if (!matches(charAt)) {
                append.append(charAt);
                indexIn = 0;
            } else if (indexIn == 0) {
                append.append(c);
                indexIn = 1;
            }
        }
        return append.toString();
    }

    public int countIn(CharSequence charSequence) {
        int i = 0;
        for (int i2 = 0; i2 < charSequence.length(); i2++) {
            if (matches(charSequence.charAt(i2))) {
                i++;
            }
        }
        return i;
    }

    public int indexIn(CharSequence charSequence) {
        int length = charSequence.length();
        for (int i = 0; i < length; i++) {
            if (matches(charSequence.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    public int indexIn(CharSequence charSequence, int i) {
        int length = charSequence.length();
        Preconditions.checkPositionIndex(i, length);
        for (int i2 = i; i2 < length; i2++) {
            if (matches(charSequence.charAt(i2))) {
                return i2;
            }
        }
        return -1;
    }

    public int lastIndexIn(CharSequence charSequence) {
        for (int length = charSequence.length() - 1; length >= 0; length--) {
            if (matches(charSequence.charAt(length))) {
                return length;
            }
        }
        return -1;
    }

    public abstract boolean matches(char c);

    public boolean matchesAllOf(CharSequence charSequence) {
        for (int length = charSequence.length() - 1; length >= 0; length--) {
            if (!matches(charSequence.charAt(length))) {
                return false;
            }
        }
        return true;
    }

    public boolean matchesAnyOf(CharSequence charSequence) {
        return !matchesNoneOf(charSequence);
    }

    public boolean matchesNoneOf(CharSequence charSequence) {
        return indexIn(charSequence) == -1;
    }

    public CharMatcher negate() {
        return new CharMatcher(this + ".negate()") {
            public /* bridge */ /* synthetic */ boolean apply(Object obj) {
                return super.apply((Character) obj);
            }

            public int countIn(CharSequence charSequence) {
                return charSequence.length() - this.countIn(charSequence);
            }

            public boolean matches(char c) {
                return !this.matches(c);
            }

            public boolean matchesAllOf(CharSequence charSequence) {
                return this.matchesNoneOf(charSequence);
            }

            public boolean matchesNoneOf(CharSequence charSequence) {
                return this.matchesAllOf(charSequence);
            }

            public CharMatcher negate() {
                return this;
            }
        };
    }

    public CharMatcher or(CharMatcher charMatcher) {
        return new Or(this, (CharMatcher) Preconditions.checkNotNull(charMatcher));
    }

    public CharMatcher precomputed() {
        return Platform.precomputeCharMatcher(this);
    }

    CharMatcher precomputedInternal() {
        char[] slowGetChars = slowGetChars();
        int length = slowGetChars.length;
        if (length == 0) {
            return NONE;
        }
        if (length == 1) {
            return is(slowGetChars[0]);
        }
        if (length < 63) {
            return SmallCharMatcher.from(slowGetChars, toString());
        }
        if (length < 1023) {
            return MediumCharMatcher.from(slowGetChars, toString());
        }
        final LookupTable lookupTable = new LookupTable();
        setBits(lookupTable);
        return new CharMatcher(toString()) {
            public /* bridge */ /* synthetic */ boolean apply(Object obj) {
                return super.apply((Character) obj);
            }

            public boolean matches(char c) {
                return lookupTable.get(c);
            }

            public CharMatcher precomputed() {
                return this;
            }
        };
    }

    @CheckReturnValue
    public String removeFrom(CharSequence charSequence) {
        String obj = charSequence.toString();
        int indexIn = indexIn(obj);
        if (indexIn == -1) {
            return obj;
        }
        char[] toCharArray = obj.toCharArray();
        int i = 1;
        while (true) {
            indexIn++;
            while (indexIn != toCharArray.length) {
                if (matches(toCharArray[indexIn])) {
                    i++;
                } else {
                    toCharArray[indexIn - i] = toCharArray[indexIn];
                    indexIn++;
                }
            }
            return new String(toCharArray, 0, indexIn - i);
        }
    }

    @CheckReturnValue
    public String replaceFrom(CharSequence charSequence, char c) {
        String obj = charSequence.toString();
        int indexIn = indexIn(obj);
        if (indexIn == -1) {
            return obj;
        }
        char[] toCharArray = obj.toCharArray();
        toCharArray[indexIn] = c;
        for (int i = indexIn + 1; i < toCharArray.length; i++) {
            if (matches(toCharArray[i])) {
                toCharArray[i] = c;
            }
        }
        return new String(toCharArray);
    }

    @CheckReturnValue
    public String replaceFrom(CharSequence charSequence, CharSequence charSequence2) {
        int i = 0;
        int length = charSequence2.length();
        if (length == 0) {
            return removeFrom(charSequence);
        }
        if (length == 1) {
            return replaceFrom(charSequence, charSequence2.charAt(0));
        }
        Object obj = charSequence.toString();
        int indexIn = indexIn(obj);
        if (indexIn == -1) {
            return obj;
        }
        int length2 = obj.length();
        StringBuilder stringBuilder = new StringBuilder(((length2 * 3) / 2) + 16);
        do {
            stringBuilder.append(obj, i, indexIn);
            stringBuilder.append(charSequence2);
            i = indexIn + 1;
            indexIn = indexIn(obj, i);
        } while (indexIn != -1);
        stringBuilder.append(obj, i, length2);
        return stringBuilder.toString();
    }

    @CheckReturnValue
    public String retainFrom(CharSequence charSequence) {
        return negate().removeFrom(charSequence);
    }

    void setBits(LookupTable lookupTable) {
        char c = '\u0000';
        while (true) {
            if (matches(c)) {
                lookupTable.set(c);
            }
            char c2 = (char) (c + 1);
            if (c != '￿') {
                c = c2;
            } else {
                return;
            }
        }
    }

    char[] slowGetChars() {
        Object obj = new char[AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED];
        int i = 0;
        int i2 = 0;
        while (i <= 65535) {
            int i3;
            if (matches((char) i)) {
                i3 = i2 + 1;
                obj[i2] = (char) i;
            } else {
                i3 = i2;
            }
            i++;
            i2 = i3;
        }
        Object obj2 = new char[i2];
        System.arraycopy(obj, 0, obj2, 0, i2);
        return obj2;
    }

    public String toString() {
        return this.description;
    }

    @CheckReturnValue
    public String trimAndCollapseFrom(CharSequence charSequence, char c) {
        int indexIn = negate().indexIn(charSequence);
        if (indexIn == -1) {
            return HttpVersions.HTTP_0_9;
        }
        StringBuilder stringBuilder = new StringBuilder(charSequence.length());
        Object obj = null;
        while (indexIn < charSequence.length()) {
            char charAt = charSequence.charAt(indexIn);
            if (matches(charAt)) {
                obj = 1;
            } else {
                if (obj != null) {
                    stringBuilder.append(c);
                    obj = null;
                }
                stringBuilder.append(charAt);
            }
            indexIn++;
        }
        return stringBuilder.toString();
    }

    @CheckReturnValue
    public String trimFrom(CharSequence charSequence) {
        int length = charSequence.length();
        int i = 0;
        while (i < length && matches(charSequence.charAt(i))) {
            i++;
        }
        int i2 = length - 1;
        while (i2 > i && matches(charSequence.charAt(i2))) {
            i2--;
        }
        return charSequence.subSequence(i, i2 + 1).toString();
    }

    @CheckReturnValue
    public String trimLeadingFrom(CharSequence charSequence) {
        int length = charSequence.length();
        int i = 0;
        while (i < length && matches(charSequence.charAt(i))) {
            i++;
        }
        return charSequence.subSequence(i, length).toString();
    }

    @CheckReturnValue
    public String trimTrailingFrom(CharSequence charSequence) {
        int length = charSequence.length() - 1;
        while (length >= 0 && matches(charSequence.charAt(length))) {
            length--;
        }
        return charSequence.subSequence(0, length + 1).toString();
    }

    CharMatcher withToString(String str) {
        throw new UnsupportedOperationException();
    }
}
