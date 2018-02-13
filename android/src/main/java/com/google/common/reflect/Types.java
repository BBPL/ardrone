package com.google.common.reflect;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Iterables;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nullable;

final class Types {
    private static final Joiner COMMA_JOINER = Joiner.on(", ").useForNull("null");
    private static final Function<Type, String> TYPE_TO_STRING = new C08291();

    static final class C08291 implements Function<Type, String> {
        C08291() {
        }

        public String apply(Type type) {
            return Types.toString(type);
        }
    }

    private enum ClassOwnership {
        OWNED_BY_ENCLOSING_CLASS {
            @Nullable
            Class<?> getOwnerType(Class<?> cls) {
                return cls.getEnclosingClass();
            }
        },
        LOCAL_CLASS_HAS_NO_OWNER {
            @Nullable
            Class<?> getOwnerType(Class<?> cls) {
                return cls.isLocalClass() ? null : cls.getEnclosingClass();
            }
        };
        
        static final ClassOwnership JVM_BEHAVIOR = null;

        static final class C08323 extends AnonymousClass1LocalClass<String> {
            C08323() {
            }
        }

        static {
            JVM_BEHAVIOR = detectJvmBehavior();
        }

        private static ClassOwnership detectJvmBehavior() {
            ParameterizedType parameterizedType = (ParameterizedType) new C08323().getClass().getGenericSuperclass();
            for (ClassOwnership classOwnership : values()) {
                if (classOwnership.getOwnerType(AnonymousClass1LocalClass.class) == parameterizedType.getOwnerType()) {
                    return classOwnership;
                }
            }
            throw new AssertionError();
        }

        @Nullable
        abstract Class<?> getOwnerType(Class<?> cls);
    }

    private static final class GenericArrayTypeImpl implements GenericArrayType, Serializable {
        private static final long serialVersionUID = 0;
        private final Type componentType;

        GenericArrayTypeImpl(Type type) {
            this.componentType = JavaVersion.CURRENT.usedInGenericType(type);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof GenericArrayType)) {
                return false;
            }
            return Objects.equal(getGenericComponentType(), ((GenericArrayType) obj).getGenericComponentType());
        }

        public Type getGenericComponentType() {
            return this.componentType;
        }

        public int hashCode() {
            return this.componentType.hashCode();
        }

        public String toString() {
            return Types.toString(this.componentType) + "[]";
        }
    }

    enum JavaVersion {
        JAVA6 {
            GenericArrayType newArrayType(Type type) {
                return new GenericArrayTypeImpl(type);
            }

            Type usedInGenericType(Type type) {
                Preconditions.checkNotNull(type);
                if (!(type instanceof Class)) {
                    return type;
                }
                Class cls = (Class) type;
                return cls.isArray() ? new GenericArrayTypeImpl(cls.getComponentType()) : type;
            }
        },
        JAVA7 {
            Type newArrayType(Type type) {
                return type instanceof Class ? Types.getArrayClass((Class) type) : new GenericArrayTypeImpl(type);
            }

            Type usedInGenericType(Type type) {
                return (Type) Preconditions.checkNotNull(type);
            }
        };
        
        static final JavaVersion CURRENT = null;

        static final class C08331 extends TypeCapture<int[]> {
            C08331() {
            }
        }

        abstract Type newArrayType(Type type);

        final ImmutableList<Type> usedInGenericType(Type[] typeArr) {
            Builder builder = ImmutableList.builder();
            for (Type usedInGenericType : typeArr) {
                builder.add(usedInGenericType(usedInGenericType));
            }
            return builder.build();
        }

        abstract Type usedInGenericType(Type type);
    }

    private static final class ParameterizedTypeImpl implements ParameterizedType, Serializable {
        private static final long serialVersionUID = 0;
        private final ImmutableList<Type> argumentsList;
        private final Type ownerType;
        private final Class<?> rawType;

        ParameterizedTypeImpl(@Nullable Type type, Class<?> cls, Type[] typeArr) {
            Preconditions.checkNotNull(cls);
            Preconditions.checkArgument(typeArr.length == cls.getTypeParameters().length);
            Types.disallowPrimitiveType(typeArr, "type parameter");
            this.ownerType = type;
            this.rawType = cls;
            this.argumentsList = JavaVersion.CURRENT.usedInGenericType(typeArr);
        }

        public boolean equals(Object obj) {
            if (obj instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) obj;
                if (getRawType().equals(parameterizedType.getRawType()) && Objects.equal(getOwnerType(), parameterizedType.getOwnerType()) && Arrays.equals(getActualTypeArguments(), parameterizedType.getActualTypeArguments())) {
                    return true;
                }
            }
            return false;
        }

        public Type[] getActualTypeArguments() {
            return Types.toArray(this.argumentsList);
        }

        public Type getOwnerType() {
            return this.ownerType;
        }

        public Type getRawType() {
            return this.rawType;
        }

        public int hashCode() {
            return ((this.ownerType == null ? 0 : this.ownerType.hashCode()) ^ this.argumentsList.hashCode()) ^ this.rawType.hashCode();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            if (this.ownerType != null) {
                stringBuilder.append(Types.toString(this.ownerType)).append('.');
            }
            stringBuilder.append(this.rawType.getName()).append('<').append(Types.COMMA_JOINER.join(Iterables.transform(this.argumentsList, Types.TYPE_TO_STRING))).append('>');
            return stringBuilder.toString();
        }
    }

    private static final class TypeVariableImpl<D extends GenericDeclaration> implements TypeVariable<D> {
        private final ImmutableList<Type> bounds;
        private final D genericDeclaration;
        private final String name;

        TypeVariableImpl(D d, String str, Type[] typeArr) {
            Types.disallowPrimitiveType(typeArr, "bound for type variable");
            this.genericDeclaration = (GenericDeclaration) Preconditions.checkNotNull(d);
            this.name = (String) Preconditions.checkNotNull(str);
            this.bounds = ImmutableList.copyOf((Object[]) typeArr);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof TypeVariable)) {
                return false;
            }
            TypeVariable typeVariable = (TypeVariable) obj;
            return this.name.equals(typeVariable.getName()) && this.genericDeclaration.equals(typeVariable.getGenericDeclaration());
        }

        public Type[] getBounds() {
            return Types.toArray(this.bounds);
        }

        public D getGenericDeclaration() {
            return this.genericDeclaration;
        }

        public String getName() {
            return this.name;
        }

        public int hashCode() {
            return this.genericDeclaration.hashCode() ^ this.name.hashCode();
        }

        public String toString() {
            return this.name;
        }
    }

    static final class WildcardTypeImpl implements WildcardType, Serializable {
        private static final long serialVersionUID = 0;
        private final ImmutableList<Type> lowerBounds;
        private final ImmutableList<Type> upperBounds;

        WildcardTypeImpl(Type[] typeArr, Type[] typeArr2) {
            Types.disallowPrimitiveType(typeArr, "lower bound for wildcard");
            Types.disallowPrimitiveType(typeArr2, "upper bound for wildcard");
            this.lowerBounds = JavaVersion.CURRENT.usedInGenericType(typeArr);
            this.upperBounds = JavaVersion.CURRENT.usedInGenericType(typeArr2);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof WildcardType)) {
                return false;
            }
            WildcardType wildcardType = (WildcardType) obj;
            return this.lowerBounds.equals(Arrays.asList(wildcardType.getLowerBounds())) && this.upperBounds.equals(Arrays.asList(wildcardType.getUpperBounds()));
        }

        public Type[] getLowerBounds() {
            return Types.toArray(this.lowerBounds);
        }

        public Type[] getUpperBounds() {
            return Types.toArray(this.upperBounds);
        }

        public int hashCode() {
            return this.lowerBounds.hashCode() ^ this.upperBounds.hashCode();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("?");
            Iterator it = this.lowerBounds.iterator();
            while (it.hasNext()) {
                stringBuilder.append(" super ").append(Types.toString((Type) it.next()));
            }
            for (Type types : Types.filterUpperBounds(this.upperBounds)) {
                stringBuilder.append(" extends ").append(Types.toString(types));
            }
            return stringBuilder.toString();
        }
    }

    private Types() {
    }

    static IllegalArgumentException buildUnexpectedTypeException(Type type, Class<?>... clsArr) {
        StringBuilder stringBuilder = new StringBuilder("Unexpected type. Expected one of: ");
        for (Class name : clsArr) {
            stringBuilder.append(name.getName()).append(", ");
        }
        stringBuilder.append("but got: ").append(type.getClass().getName()).append(", for type: ").append(toString(type)).append('.');
        return new IllegalArgumentException(stringBuilder.toString());
    }

    static boolean containsTypeVariable(@Nullable Type type) {
        if (type instanceof TypeVariable) {
            return true;
        }
        if (type instanceof GenericArrayType) {
            return containsTypeVariable(((GenericArrayType) type).getGenericComponentType());
        }
        if (type instanceof ParameterizedType) {
            return containsTypeVariable(((ParameterizedType) type).getActualTypeArguments());
        }
        if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            if (containsTypeVariable(wildcardType.getUpperBounds()) || containsTypeVariable(wildcardType.getLowerBounds())) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsTypeVariable(Type[] typeArr) {
        for (Type containsTypeVariable : typeArr) {
            if (containsTypeVariable(containsTypeVariable)) {
                return true;
            }
        }
        return false;
    }

    private static void disallowPrimitiveType(Type[] typeArr, String str) {
        for (Type type : typeArr) {
            if (type instanceof Class) {
                Preconditions.checkArgument(!((Class) type).isPrimitive(), "Primitive type '%s' used as %s", (Class) type, str);
            }
        }
    }

    private static Iterable<Type> filterUpperBounds(Iterable<Type> iterable) {
        return Iterables.filter((Iterable) iterable, Predicates.not(Predicates.equalTo(Object.class)));
    }

    static Class<?> getArrayClass(Class<?> cls) {
        return Array.newInstance(cls, 0).getClass();
    }

    @Nullable
    static Type getComponentType(Type type) {
        Preconditions.checkNotNull(type);
        return type instanceof Class ? ((Class) type).getComponentType() : type instanceof GenericArrayType ? ((GenericArrayType) type).getGenericComponentType() : type instanceof WildcardType ? subtypeOfComponentType(((WildcardType) type).getUpperBounds()) : type instanceof TypeVariable ? subtypeOfComponentType(((TypeVariable) type).getBounds()) : null;
    }

    static Type newArrayType(Type type) {
        boolean z = true;
        if (!(type instanceof WildcardType)) {
            return JavaVersion.CURRENT.newArrayType(type);
        }
        WildcardType wildcardType = (WildcardType) type;
        Type[] lowerBounds = wildcardType.getLowerBounds();
        Preconditions.checkArgument(lowerBounds.length <= 1, "Wildcard cannot have more than one lower bounds.");
        if (lowerBounds.length == 1) {
            return supertypeOf(newArrayType(lowerBounds[0]));
        }
        Type[] upperBounds = wildcardType.getUpperBounds();
        if (upperBounds.length != 1) {
            z = false;
        }
        Preconditions.checkArgument(z, "Wildcard should have only one upper bound.");
        return subtypeOf(newArrayType(upperBounds[0]));
    }

    static ParameterizedType newParameterizedType(Class<?> cls, Type... typeArr) {
        return new ParameterizedTypeImpl(ClassOwnership.JVM_BEHAVIOR.getOwnerType(cls), cls, typeArr);
    }

    static ParameterizedType newParameterizedTypeWithOwner(@Nullable Type type, Class<?> cls, Type... typeArr) {
        if (type == null) {
            return newParameterizedType(cls, typeArr);
        }
        Preconditions.checkNotNull(typeArr);
        Preconditions.checkArgument(cls.getEnclosingClass() != null, "Owner type for unenclosed %s", cls);
        return new ParameterizedTypeImpl(type, cls, typeArr);
    }

    static <D extends GenericDeclaration> TypeVariable<D> newTypeVariable(D d, String str, Type... typeArr) {
        if (typeArr.length == 0) {
            typeArr = new Type[]{Object.class};
        }
        return new TypeVariableImpl(d, str, typeArr);
    }

    @VisibleForTesting
    static WildcardType subtypeOf(Type type) {
        return new WildcardTypeImpl(new Type[0], new Type[]{type});
    }

    @Nullable
    private static Type subtypeOfComponentType(Type[] typeArr) {
        for (Type componentType : typeArr) {
            Type componentType2 = getComponentType(componentType2);
            if (componentType2 != null) {
                if (componentType2 instanceof Class) {
                    Class cls = (Class) componentType2;
                    if (cls.isPrimitive()) {
                        return cls;
                    }
                }
                return subtypeOf(componentType2);
            }
        }
        return null;
    }

    @VisibleForTesting
    static WildcardType supertypeOf(Type type) {
        return new WildcardTypeImpl(new Type[]{type}, new Type[]{Object.class});
    }

    private static Type[] toArray(Collection<Type> collection) {
        return (Type[]) collection.toArray(new Type[collection.size()]);
    }

    static String toString(Type type) {
        return type instanceof Class ? ((Class) type).getName() : type.toString();
    }
}
