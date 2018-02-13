package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
@Beta
public final class Equivalences {
    private Equivalences() {
    }

    @Deprecated
    public static Equivalence<Object> equals() {
        return Equals.INSTANCE;
    }

    @Deprecated
    public static Equivalence<Object> identity() {
        return Identity.INSTANCE;
    }
}
