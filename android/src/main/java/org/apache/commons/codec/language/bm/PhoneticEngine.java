package org.apache.commons.codec.language.bm;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.codec.language.bm.Languages.LanguageSet;
import org.apache.commons.codec.language.bm.Rule.Phoneme;
import org.apache.commons.codec.language.bm.Rule.PhonemeExpr;
import org.mortbay.jetty.HttpVersions;

public class PhoneticEngine {
    private static final Map<NameType, Set<String>> NAME_PREFIXES = new EnumMap(NameType.class);
    private final boolean concat;
    private final Lang lang;
    private final NameType nameType;
    private final RuleType ruleType;

    static final class PhonemeBuilder {
        private final Set<Phoneme> phonemes;

        private PhonemeBuilder(Set<Phoneme> set) {
            this.phonemes = set;
        }

        public static PhonemeBuilder empty(LanguageSet languageSet) {
            return new PhonemeBuilder(Collections.singleton(new Phoneme(HttpVersions.HTTP_0_9, languageSet)));
        }

        public PhonemeBuilder append(CharSequence charSequence) {
            Set hashSet = new HashSet();
            for (Phoneme append : this.phonemes) {
                hashSet.add(append.append(charSequence));
            }
            return new PhonemeBuilder(hashSet);
        }

        public PhonemeBuilder apply(PhonemeExpr phonemeExpr) {
            Set hashSet = new HashSet();
            for (Phoneme phoneme : this.phonemes) {
                for (Phoneme join : phonemeExpr.getPhonemes()) {
                    Phoneme join2 = phoneme.join(join2);
                    if (!join2.getLanguages().isEmpty()) {
                        hashSet.add(join2);
                    }
                }
            }
            return new PhonemeBuilder(hashSet);
        }

        public Set<Phoneme> getPhonemes() {
            return this.phonemes;
        }

        public String makeString() {
            StringBuilder stringBuilder = new StringBuilder();
            for (Phoneme phoneme : this.phonemes) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append("|");
                }
                stringBuilder.append(phoneme.getPhonemeText());
            }
            return stringBuilder.toString();
        }
    }

    private static final class RulesApplication {
        private final List<Rule> finalRules;
        private boolean found;
        private int f353i;
        private final CharSequence input;
        private PhonemeBuilder phonemeBuilder;

        public RulesApplication(List<Rule> list, CharSequence charSequence, PhonemeBuilder phonemeBuilder, int i) {
            if (list == null) {
                throw new NullPointerException("The finalRules argument must not be null");
            }
            this.finalRules = list;
            this.phonemeBuilder = phonemeBuilder;
            this.input = charSequence;
            this.f353i = i;
        }

        public int getI() {
            return this.f353i;
        }

        public PhonemeBuilder getPhonemeBuilder() {
            return this.phonemeBuilder;
        }

        public RulesApplication invoke() {
            boolean length;
            int i;
            boolean z = false;
            this.found = false;
            for (Rule rule : this.finalRules) {
                length = rule.getPattern().length();
                if (rule.patternAndContextMatches(this.input, this.f353i)) {
                    this.phonemeBuilder = this.phonemeBuilder.apply(rule.getPhoneme());
                    this.found = true;
                    break;
                }
                z = length;
            }
            length = z;
            if (!this.found) {
                i = 1;
            }
            this.f353i += i;
            return this;
        }

        public boolean isFound() {
            return this.found;
        }
    }

    static {
        NAME_PREFIXES.put(NameType.ASHKENAZI, Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[]{"bar", "ben", "da", "de", "van", "von"}))));
        NAME_PREFIXES.put(NameType.SEPHARDIC, Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[]{"al", "el", "da", "dal", "de", "del", "dela", "de la", "della", "des", "di", "do", "dos", "du", "van", "von"}))));
        NAME_PREFIXES.put(NameType.GENERIC, Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[]{"da", "dal", "de", "del", "dela", "de la", "della", "des", "di", "do", "dos", "du", "van", "von"}))));
    }

    public PhoneticEngine(NameType nameType, RuleType ruleType, boolean z) {
        if (ruleType == RuleType.RULES) {
            throw new IllegalArgumentException("ruleType must not be " + RuleType.RULES);
        }
        this.nameType = nameType;
        this.ruleType = ruleType;
        this.concat = z;
        this.lang = Lang.instance(nameType);
    }

    private PhonemeBuilder applyFinalRules(PhonemeBuilder phonemeBuilder, List<Rule> list) {
        if (list == null) {
            throw new NullPointerException("finalRules can not be null");
        } else if (list.isEmpty()) {
            return phonemeBuilder;
        } else {
            Set treeSet = new TreeSet(Phoneme.COMPARATOR);
            for (Phoneme phoneme : phonemeBuilder.getPhonemes()) {
                PhonemeBuilder empty = PhonemeBuilder.empty(phoneme.getLanguages());
                CharSequence cacheSubSequence = cacheSubSequence(phoneme.getPhonemeText());
                PhonemeBuilder phonemeBuilder2 = empty;
                int i = 0;
                while (i < cacheSubSequence.length()) {
                    RulesApplication invoke = new RulesApplication(list, cacheSubSequence, phonemeBuilder2, i).invoke();
                    boolean isFound = invoke.isFound();
                    phonemeBuilder2 = invoke.getPhonemeBuilder();
                    if (!isFound) {
                        phonemeBuilder2 = phonemeBuilder2.append(cacheSubSequence.subSequence(i, i + 1));
                    }
                    i = invoke.getI();
                }
                treeSet.addAll(phonemeBuilder2.getPhonemes());
            }
            return new PhonemeBuilder(treeSet);
        }
    }

    private static CharSequence cacheSubSequence(final CharSequence charSequence) {
        final CharSequence[][] charSequenceArr = (CharSequence[][]) Array.newInstance(CharSequence.class, new int[]{charSequence.length(), charSequence.length()});
        return new CharSequence() {
            public char charAt(int i) {
                return charSequence.charAt(i);
            }

            public int length() {
                return charSequence.length();
            }

            public CharSequence subSequence(int i, int i2) {
                if (i == i2) {
                    return HttpVersions.HTTP_0_9;
                }
                CharSequence charSequence = charSequenceArr[i][i2 - 1];
                if (charSequence != null) {
                    return charSequence;
                }
                charSequence = charSequence.subSequence(i, i2);
                charSequenceArr[i][i2 - 1] = charSequence;
                return charSequence;
            }
        };
    }

    private static String join(Iterable<String> iterable, String str) {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator it = iterable.iterator();
        if (it.hasNext()) {
            stringBuilder.append((String) it.next());
        }
        while (it.hasNext()) {
            stringBuilder.append(str).append((String) it.next());
        }
        return stringBuilder.toString();
    }

    public String encode(String str) {
        return encode(str, this.lang.guessLanguages(str));
    }

    public String encode(String str, LanguageSet languageSet) {
        String substring;
        CharSequence join;
        List instance = Rule.getInstance(this.nameType, RuleType.RULES, languageSet);
        List instance2 = Rule.getInstance(this.nameType, this.ruleType, "common");
        List instance3 = Rule.getInstance(this.nameType, this.ruleType, languageSet);
        String trim = str.toLowerCase(Locale.ENGLISH).replace('-', ' ').trim();
        if (this.nameType == NameType.GENERIC) {
            if (trim.length() < 2 || !trim.substring(0, 2).equals("d'")) {
                for (String substring2 : (Set) NAME_PREFIXES.get(this.nameType)) {
                    if (trim.startsWith(substring2 + " ")) {
                        String substring3 = trim.substring(substring2.length() + 1);
                        return "(" + encode(substring3) + ")-(" + encode(substring2 + substring3) + ")";
                    }
                }
            }
            substring2 = trim.substring(2);
            return "(" + encode(substring2) + ")-(" + encode("d" + substring2) + ")";
        }
        Collection<String> asList = Arrays.asList(trim.split("\\s+"));
        List<String> arrayList = new ArrayList();
        switch (this.nameType) {
            case SEPHARDIC:
                for (String substring22 : asList) {
                    String[] split = substring22.split("'");
                    arrayList.add(split[split.length - 1]);
                }
                arrayList.removeAll((Collection) NAME_PREFIXES.get(this.nameType));
                break;
            case ASHKENAZI:
                arrayList.addAll(asList);
                arrayList.removeAll((Collection) NAME_PREFIXES.get(this.nameType));
                break;
            case GENERIC:
                arrayList.addAll(asList);
                break;
            default:
                throw new IllegalStateException("Unreachable case: " + this.nameType);
        }
        if (this.concat) {
            join = join(arrayList, " ");
        } else if (arrayList.size() == 1) {
            substring22 = (String) asList.iterator().next();
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            for (String substring222 : arrayList) {
                stringBuilder.append("-").append(encode(substring222));
            }
            return stringBuilder.substring(1);
        }
        PhonemeBuilder empty = PhonemeBuilder.empty(languageSet);
        CharSequence cacheSubSequence = cacheSubSequence(join);
        int i = 0;
        PhonemeBuilder phonemeBuilder = empty;
        while (i < cacheSubSequence.length()) {
            RulesApplication invoke = new RulesApplication(instance, cacheSubSequence, phonemeBuilder, i).invoke();
            i = invoke.getI();
            phonemeBuilder = invoke.getPhonemeBuilder();
        }
        return applyFinalRules(applyFinalRules(phonemeBuilder, instance2), instance3).makeString();
    }

    public Lang getLang() {
        return this.lang;
    }

    public NameType getNameType() {
        return this.nameType;
    }

    public RuleType getRuleType() {
        return this.ruleType;
    }

    public boolean isConcat() {
        return this.concat;
    }
}
