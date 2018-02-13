package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Iterator;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
final class PairwiseEquivalence<T> extends Equivalence<Iterable<T>> implements Serializable {
    private static final long serialVersionUID = 1;
    final Equivalence<? super T> elementEquivalence;

    PairwiseEquivalence(Equivalence<? super T> equivalence) {
        this.elementEquivalence = (Equivalence) Preconditions.checkNotNull(equivalence);
    }

    protected boolean doEquivalent(Iterable<T> iterable, Iterable<T> iterable2) {
        Iterator it = iterable.iterator();
        Iterator it2 = iterable2.iterator();
        while (it.hasNext() && it2.hasNext()) {
            if (!this.elementEquivalence.equivalent(it.next(), it2.next())) {
                break;
            }
        }
        return (it.hasNext() || it2.hasNext()) ? false : true;
    }

    protected int doHash(Iterable<T> iterable) {
        int i = 78721;
        for (T hash : iterable) {
            i = (i * 24943) + this.elementEquivalence.hash(hash);
        }
        return i;
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof PairwiseEquivalence)) {
            return false;
        }
        return this.elementEquivalence.equals(((PairwiseEquivalence) obj).elementEquivalence);
    }

    public int hashCode() {
        return this.elementEquivalence.hashCode() ^ 1185147655;
    }

    public String toString() {
        return this.elementEquivalence + ".pairwise()";
    }
}
