package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

@Beta
public final class CacheBuilderSpec {
    private static final Splitter KEYS_SPLITTER = Splitter.on(',').trimResults();
    private static final Splitter KEY_VALUE_SPLITTER = Splitter.on('=').trimResults();
    private static final ImmutableMap<String, ValueParser> VALUE_PARSERS = ImmutableMap.builder().put("initialCapacity", new InitialCapacityParser()).put("maximumSize", new MaximumSizeParser()).put("maximumWeight", new MaximumWeightParser()).put("concurrencyLevel", new ConcurrencyLevelParser()).put("weakKeys", new KeyStrengthParser(Strength.WEAK)).put("softValues", new ValueStrengthParser(Strength.SOFT)).put("weakValues", new ValueStrengthParser(Strength.WEAK)).put("expireAfterAccess", new AccessDurationParser()).put("expireAfterWrite", new WriteDurationParser()).put("refreshAfterWrite", new RefreshDurationParser()).put("refreshInterval", new RefreshDurationParser()).build();
    @VisibleForTesting
    long accessExpirationDuration;
    @VisibleForTesting
    TimeUnit accessExpirationTimeUnit;
    @VisibleForTesting
    Integer concurrencyLevel;
    @VisibleForTesting
    Integer initialCapacity;
    @VisibleForTesting
    Strength keyStrength;
    @VisibleForTesting
    Long maximumSize;
    @VisibleForTesting
    Long maximumWeight;
    @VisibleForTesting
    long refreshDuration;
    @VisibleForTesting
    TimeUnit refreshTimeUnit;
    private final String specification;
    @VisibleForTesting
    Strength valueStrength;
    @VisibleForTesting
    long writeExpirationDuration;
    @VisibleForTesting
    TimeUnit writeExpirationTimeUnit;

    private interface ValueParser {
        void parse(CacheBuilderSpec cacheBuilderSpec, String str, @Nullable String str2);
    }

    static abstract class DurationParser implements ValueParser {
        DurationParser() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void parse(com.google.common.cache.CacheBuilderSpec r13, java.lang.String r14, java.lang.String r15) {
            /*
            r12 = this;
            r10 = 60;
            r4 = 1;
            r8 = 2;
            r1 = 1;
            r2 = 0;
            if (r15 == 0) goto L_0x004e;
        L_0x0009:
            r0 = r15.length();
            if (r0 <= 0) goto L_0x004e;
        L_0x000f:
            r0 = r1;
        L_0x0010:
            r3 = "value of key %s omitted";
            r6 = new java.lang.Object[r1];
            r6[r2] = r14;
            com.google.common.base.Preconditions.checkArgument(r0, r3, r6);
            r0 = r15.length();	 Catch:{ NumberFormatException -> 0x003b }
            r0 = r0 + -1;
            r0 = r15.charAt(r0);	 Catch:{ NumberFormatException -> 0x003b }
            switch(r0) {
                case 100: goto L_0x0050;
                case 104: goto L_0x0053;
                case 109: goto L_0x0054;
                case 115: goto L_0x0055;
                default: goto L_0x0026;
            };	 Catch:{ NumberFormatException -> 0x003b }
        L_0x0026:
            r0 = new java.lang.IllegalArgumentException;	 Catch:{ NumberFormatException -> 0x003b }
            r3 = "key %s invalid format.  was %s, must end with one of [dDhHmMsS]";
            r4 = 2;
            r4 = new java.lang.Object[r4];	 Catch:{ NumberFormatException -> 0x003b }
            r5 = 0;
            r4[r5] = r14;	 Catch:{ NumberFormatException -> 0x003b }
            r5 = 1;
            r4[r5] = r15;	 Catch:{ NumberFormatException -> 0x003b }
            r3 = java.lang.String.format(r3, r4);	 Catch:{ NumberFormatException -> 0x003b }
            r0.<init>(r3);	 Catch:{ NumberFormatException -> 0x003b }
            throw r0;	 Catch:{ NumberFormatException -> 0x003b }
        L_0x003b:
            r0 = move-exception;
            r0 = new java.lang.IllegalArgumentException;
            r3 = "key %s value set to %s, must be integer";
            r4 = new java.lang.Object[r8];
            r4[r2] = r14;
            r4[r1] = r15;
            r1 = java.lang.String.format(r3, r4);
            r0.<init>(r1);
            throw r0;
        L_0x004e:
            r0 = r2;
            goto L_0x0010;
        L_0x0050:
            r6 = 24;
            r4 = r4 * r6;
        L_0x0053:
            r4 = r4 * r10;
        L_0x0054:
            r4 = r4 * r10;
        L_0x0055:
            r0 = java.util.concurrent.TimeUnit.SECONDS;	 Catch:{ NumberFormatException -> 0x003b }
            r3 = 0;
            r6 = r15.length();	 Catch:{ NumberFormatException -> 0x003b }
            r6 = r6 + -1;
            r3 = r15.substring(r3, r6);	 Catch:{ NumberFormatException -> 0x003b }
            r6 = java.lang.Long.parseLong(r3);	 Catch:{ NumberFormatException -> 0x003b }
            r4 = r4 * r6;
            r12.parseDuration(r13, r4, r0);	 Catch:{ NumberFormatException -> 0x003b }
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.cache.CacheBuilderSpec.DurationParser.parse(com.google.common.cache.CacheBuilderSpec, java.lang.String, java.lang.String):void");
        }

        protected abstract void parseDuration(CacheBuilderSpec cacheBuilderSpec, long j, TimeUnit timeUnit);
    }

    static class AccessDurationParser extends DurationParser {
        AccessDurationParser() {
        }

        protected void parseDuration(CacheBuilderSpec cacheBuilderSpec, long j, TimeUnit timeUnit) {
            Preconditions.checkArgument(cacheBuilderSpec.accessExpirationTimeUnit == null, "expireAfterAccess already set");
            cacheBuilderSpec.accessExpirationDuration = j;
            cacheBuilderSpec.accessExpirationTimeUnit = timeUnit;
        }
    }

    static abstract class IntegerParser implements ValueParser {
        IntegerParser() {
        }

        public void parse(CacheBuilderSpec cacheBuilderSpec, String str, String str2) {
            boolean z = str2 != null && str2.length() > 0;
            Preconditions.checkArgument(z, "value of key %s omitted", str);
            try {
                parseInteger(cacheBuilderSpec, Integer.parseInt(str2));
            } catch (Throwable e) {
                throw new IllegalArgumentException(String.format("key %s value set to %s, must be integer", new Object[]{str, str2}), e);
            }
        }

        protected abstract void parseInteger(CacheBuilderSpec cacheBuilderSpec, int i);
    }

    static class ConcurrencyLevelParser extends IntegerParser {
        ConcurrencyLevelParser() {
        }

        protected void parseInteger(CacheBuilderSpec cacheBuilderSpec, int i) {
            Preconditions.checkArgument(cacheBuilderSpec.concurrencyLevel == null, "concurrency level was already set to ", cacheBuilderSpec.concurrencyLevel);
            cacheBuilderSpec.concurrencyLevel = Integer.valueOf(i);
        }
    }

    static class InitialCapacityParser extends IntegerParser {
        InitialCapacityParser() {
        }

        protected void parseInteger(CacheBuilderSpec cacheBuilderSpec, int i) {
            Preconditions.checkArgument(cacheBuilderSpec.initialCapacity == null, "initial capacity was already set to ", cacheBuilderSpec.initialCapacity);
            cacheBuilderSpec.initialCapacity = Integer.valueOf(i);
        }
    }

    static class KeyStrengthParser implements ValueParser {
        private final Strength strength;

        public KeyStrengthParser(Strength strength) {
            this.strength = strength;
        }

        public void parse(CacheBuilderSpec cacheBuilderSpec, String str, @Nullable String str2) {
            Preconditions.checkArgument(str2 == null, "key %s does not take values", str);
            Preconditions.checkArgument(cacheBuilderSpec.keyStrength == null, "%s was already set to %s", str, cacheBuilderSpec.keyStrength);
            cacheBuilderSpec.keyStrength = this.strength;
        }
    }

    static abstract class LongParser implements ValueParser {
        LongParser() {
        }

        public void parse(CacheBuilderSpec cacheBuilderSpec, String str, String str2) {
            boolean z = str2 != null && str2.length() > 0;
            Preconditions.checkArgument(z, "value of key %s omitted", str);
            try {
                parseLong(cacheBuilderSpec, Long.parseLong(str2));
            } catch (Throwable e) {
                throw new IllegalArgumentException(String.format("key %s value set to %s, must be integer", new Object[]{str, str2}), e);
            }
        }

        protected abstract void parseLong(CacheBuilderSpec cacheBuilderSpec, long j);
    }

    static class MaximumSizeParser extends LongParser {
        MaximumSizeParser() {
        }

        protected void parseLong(CacheBuilderSpec cacheBuilderSpec, long j) {
            Preconditions.checkArgument(cacheBuilderSpec.maximumSize == null, "maximum size was already set to ", cacheBuilderSpec.maximumSize);
            Preconditions.checkArgument(cacheBuilderSpec.maximumWeight == null, "maximum weight was already set to ", cacheBuilderSpec.maximumWeight);
            cacheBuilderSpec.maximumSize = Long.valueOf(j);
        }
    }

    static class MaximumWeightParser extends LongParser {
        MaximumWeightParser() {
        }

        protected void parseLong(CacheBuilderSpec cacheBuilderSpec, long j) {
            Preconditions.checkArgument(cacheBuilderSpec.maximumWeight == null, "maximum weight was already set to ", cacheBuilderSpec.maximumWeight);
            Preconditions.checkArgument(cacheBuilderSpec.maximumSize == null, "maximum size was already set to ", cacheBuilderSpec.maximumSize);
            cacheBuilderSpec.maximumWeight = Long.valueOf(j);
        }
    }

    static class RefreshDurationParser extends DurationParser {
        RefreshDurationParser() {
        }

        protected void parseDuration(CacheBuilderSpec cacheBuilderSpec, long j, TimeUnit timeUnit) {
            Preconditions.checkArgument(cacheBuilderSpec.refreshTimeUnit == null, "refreshAfterWrite already set");
            cacheBuilderSpec.refreshDuration = j;
            cacheBuilderSpec.refreshTimeUnit = timeUnit;
        }
    }

    static class ValueStrengthParser implements ValueParser {
        private final Strength strength;

        public ValueStrengthParser(Strength strength) {
            this.strength = strength;
        }

        public void parse(CacheBuilderSpec cacheBuilderSpec, String str, @Nullable String str2) {
            Preconditions.checkArgument(str2 == null, "key %s does not take values", str);
            Preconditions.checkArgument(cacheBuilderSpec.valueStrength == null, "%s was already set to %s", str, cacheBuilderSpec.valueStrength);
            cacheBuilderSpec.valueStrength = this.strength;
        }
    }

    static class WriteDurationParser extends DurationParser {
        WriteDurationParser() {
        }

        protected void parseDuration(CacheBuilderSpec cacheBuilderSpec, long j, TimeUnit timeUnit) {
            Preconditions.checkArgument(cacheBuilderSpec.writeExpirationTimeUnit == null, "expireAfterWrite already set");
            cacheBuilderSpec.writeExpirationDuration = j;
            cacheBuilderSpec.writeExpirationTimeUnit = timeUnit;
        }
    }

    private CacheBuilderSpec(String str) {
        this.specification = str;
    }

    public static CacheBuilderSpec disableCaching() {
        return parse("maximumSize=0");
    }

    @Nullable
    private static Long durationInNanos(long j, @Nullable TimeUnit timeUnit) {
        return timeUnit == null ? null : Long.valueOf(timeUnit.toNanos(j));
    }

    public static CacheBuilderSpec parse(String str) {
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec(str);
        if (str.length() > 0) {
            for (String split : KEYS_SPLITTER.split(str)) {
                List copyOf = ImmutableList.copyOf(KEY_VALUE_SPLITTER.split(split));
                Preconditions.checkArgument(!copyOf.isEmpty(), "blank key-value pair");
                Preconditions.checkArgument(copyOf.size() <= 2, "key-value pair %s with more than one equals sign", split);
                String split2 = (String) copyOf.get(0);
                ValueParser valueParser = (ValueParser) VALUE_PARSERS.get(split2);
                Preconditions.checkArgument(valueParser != null, "unknown key %s", split2);
                valueParser.parse(cacheBuilderSpec, split2, copyOf.size() == 1 ? null : (String) copyOf.get(1));
            }
        }
        return cacheBuilderSpec;
    }

    public boolean equals(Object obj) {
        if (this != obj) {
            if (!(obj instanceof CacheBuilderSpec)) {
                return false;
            }
            CacheBuilderSpec cacheBuilderSpec = (CacheBuilderSpec) obj;
            if (!Objects.equal(this.initialCapacity, cacheBuilderSpec.initialCapacity) || !Objects.equal(this.maximumSize, cacheBuilderSpec.maximumSize) || !Objects.equal(this.maximumWeight, cacheBuilderSpec.maximumWeight) || !Objects.equal(this.concurrencyLevel, cacheBuilderSpec.concurrencyLevel) || !Objects.equal(this.keyStrength, cacheBuilderSpec.keyStrength) || !Objects.equal(this.valueStrength, cacheBuilderSpec.valueStrength) || !Objects.equal(durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), durationInNanos(cacheBuilderSpec.writeExpirationDuration, cacheBuilderSpec.writeExpirationTimeUnit)) || !Objects.equal(durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), durationInNanos(cacheBuilderSpec.accessExpirationDuration, cacheBuilderSpec.accessExpirationTimeUnit))) {
                return false;
            }
            if (!Objects.equal(durationInNanos(this.refreshDuration, this.refreshTimeUnit), durationInNanos(cacheBuilderSpec.refreshDuration, cacheBuilderSpec.refreshTimeUnit))) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return Objects.hashCode(this.initialCapacity, this.maximumSize, this.maximumWeight, this.concurrencyLevel, this.keyStrength, this.valueStrength, durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), durationInNanos(this.refreshDuration, this.refreshTimeUnit));
    }

    CacheBuilder<Object, Object> toCacheBuilder() {
        CacheBuilder<Object, Object> newBuilder = CacheBuilder.newBuilder();
        if (this.initialCapacity != null) {
            newBuilder.initialCapacity(this.initialCapacity.intValue());
        }
        if (this.maximumSize != null) {
            newBuilder.maximumSize(this.maximumSize.longValue());
        }
        if (this.maximumWeight != null) {
            newBuilder.maximumWeight(this.maximumWeight.longValue());
        }
        if (this.concurrencyLevel != null) {
            newBuilder.concurrencyLevel(this.concurrencyLevel.intValue());
        }
        if (this.keyStrength != null) {
            switch (this.keyStrength) {
                case WEAK:
                    newBuilder.weakKeys();
                    break;
                default:
                    throw new AssertionError();
            }
        }
        if (this.valueStrength != null) {
            switch (this.valueStrength) {
                case WEAK:
                    newBuilder.weakValues();
                    break;
                case SOFT:
                    newBuilder.softValues();
                    break;
                default:
                    throw new AssertionError();
            }
        }
        if (this.writeExpirationTimeUnit != null) {
            newBuilder.expireAfterWrite(this.writeExpirationDuration, this.writeExpirationTimeUnit);
        }
        if (this.accessExpirationTimeUnit != null) {
            newBuilder.expireAfterAccess(this.accessExpirationDuration, this.accessExpirationTimeUnit);
        }
        if (this.refreshTimeUnit != null) {
            newBuilder.refreshAfterWrite(this.refreshDuration, this.refreshTimeUnit);
        }
        return newBuilder;
    }

    public String toParsableString() {
        return this.specification;
    }

    public String toString() {
        return Objects.toStringHelper((Object) this).addValue(toParsableString()).toString();
    }
}
