package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap.Builder;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
final class ExplicitOrdering<T> extends Ordering<T> implements Serializable {
    private static final long serialVersionUID = 0;
    final ImmutableMap<T, Integer> rankMap;

    ExplicitOrdering(ImmutableMap<T, Integer> immutableMap) {
        this.rankMap = immutableMap;
    }

    ExplicitOrdering(List<T> list) {
        this(buildRankMap(list));
    }

    private static <T> ImmutableMap<T, Integer> buildRankMap(List<T> list) {
        Builder builder = ImmutableMap.builder();
        int i = 0;
        for (T put : list) {
            builder.put(put, Integer.valueOf(i));
            i++;
        }
        return builder.build();
    }

    private int rank(T t) {
        Integer num = (Integer) this.rankMap.get(t);
        if (num != null) {
            return num.intValue();
        }
        throw new IncomparableValueException(t);
    }

    public int compare(T t, T t2) {
        return rank(t) - rank(t2);
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof ExplicitOrdering)) {
            return false;
        }
        return this.rankMap.equals(((ExplicitOrdering) obj).rankMap);
    }

    public int hashCode() {
        return this.rankMap.hashCode();
    }

    public String toString() {
        return "Ordering.explicit(" + this.rankMap.keySet() + ")";
    }
}
