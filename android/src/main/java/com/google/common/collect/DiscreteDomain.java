package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.NoSuchElementException;

@GwtCompatible
@Beta
public abstract class DiscreteDomain<C extends Comparable> {
    protected DiscreteDomain() {
    }

    public abstract long distance(C c, C c2);

    public C maxValue() {
        throw new NoSuchElementException();
    }

    public C minValue() {
        throw new NoSuchElementException();
    }

    public abstract C next(C c);

    public abstract C previous(C c);
}
