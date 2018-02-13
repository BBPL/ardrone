package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;

@GwtCompatible(emulated = true, serializable = true)
final class RegularImmutableSet<E> extends ArrayImmutableSet<E> {
    private final transient int hashCode;
    private final transient int mask;
    @VisibleForTesting
    final transient Object[] table;

    RegularImmutableSet(Object[] objArr, int i, Object[] objArr2, int i2) {
        super(objArr);
        this.table = objArr2;
        this.mask = i2;
        this.hashCode = i;
    }

    public boolean contains(Object obj) {
        if (obj != null) {
            int smear = Hashing.smear(obj.hashCode());
            while (true) {
                Object obj2 = this.table[this.mask & smear];
                if (obj2 == null) {
                    break;
                } else if (obj2.equals(obj)) {
                    return true;
                } else {
                    smear++;
                }
            }
        }
        return false;
    }

    public int hashCode() {
        return this.hashCode;
    }

    boolean isHashCodeFast() {
        return true;
    }
}
