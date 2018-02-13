package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
@Beta
public enum BoundType {
    OPEN {
        BoundType flip() {
            return CLOSED;
        }
    },
    CLOSED {
        BoundType flip() {
            return OPEN;
        }
    };

    static BoundType forBoolean(boolean z) {
        return z ? CLOSED : OPEN;
    }

    abstract BoundType flip();
}
