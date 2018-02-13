package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

@Beta
public abstract class TypeParameter<T> extends TypeCapture<T> {
    final TypeVariable<?> typeVariable;

    protected TypeParameter() {
        Type capture = capture();
        Preconditions.checkArgument(capture instanceof TypeVariable, "%s should be a type variable.", capture);
        this.typeVariable = (TypeVariable) capture;
    }

    private TypeParameter(TypeVariable<?> typeVariable) {
        this.typeVariable = (TypeVariable) Preconditions.checkNotNull(typeVariable);
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof TypeParameter)) {
            return false;
        }
        return this.typeVariable.equals(((TypeParameter) obj).typeVariable);
    }

    public final int hashCode() {
        return this.typeVariable.hashCode();
    }

    public String toString() {
        return this.typeVariable.toString();
    }
}
