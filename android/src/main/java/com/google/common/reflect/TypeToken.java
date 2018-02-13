package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import java.io.Serializable;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public abstract class TypeToken<T> extends TypeCapture<T> implements Serializable {
    private final Type runtimeType;
    private transient TypeResolver typeResolver;

    public class TypeSet extends ForwardingSet<TypeToken<? super T>> implements Serializable {
        private static final long serialVersionUID = 0;
        private transient ImmutableSet<TypeToken<? super T>> types;

        TypeSet() {
        }

        public TypeSet classes() {
            return new ClassSet();
        }

        protected Set<TypeToken<? super T>> delegate() {
            Set set = this.types;
            if (set != null) {
                return set;
            }
            set = FluentIterable.from(TypeCollector.FOR_GENERIC_TYPE.collectTypes(TypeToken.this)).filter(TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toImmutableSet();
            this.types = set;
            return set;
        }

        public TypeSet interfaces() {
            return new InterfaceSet(this);
        }

        public Set<Class<? super T>> rawTypes() {
            return ImmutableSet.copyOf(TypeCollector.FOR_RAW_TYPE.collectTypes(TypeToken.this.getImmediateRawTypes()));
        }
    }

    private final class ClassSet extends TypeSet {
        private static final long serialVersionUID = 0;
        private transient ImmutableSet<TypeToken<? super T>> classes;

        private ClassSet() {
            super();
        }

        private Object readResolve() {
            return TypeToken.this.getTypes().classes();
        }

        public TypeSet classes() {
            return this;
        }

        protected Set<TypeToken<? super T>> delegate() {
            Set set = this.classes;
            if (set != null) {
                return set;
            }
            set = FluentIterable.from(TypeCollector.FOR_GENERIC_TYPE.classesOnly().collectTypes(TypeToken.this)).filter(TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toImmutableSet();
            this.classes = set;
            return set;
        }

        public TypeSet interfaces() {
            throw new UnsupportedOperationException("classes().interfaces() not supported.");
        }

        public Set<Class<? super T>> rawTypes() {
            return ImmutableSet.copyOf(TypeCollector.FOR_RAW_TYPE.classesOnly().collectTypes(TypeToken.this.getImmediateRawTypes()));
        }
    }

    private final class InterfaceSet extends TypeSet {
        private static final long serialVersionUID = 0;
        private final transient TypeSet allTypes;
        private transient ImmutableSet<TypeToken<? super T>> interfaces;

        class C08221 implements Predicate<Class<?>> {
            C08221() {
            }

            public boolean apply(Class<?> cls) {
                return cls.isInterface();
            }
        }

        InterfaceSet(TypeSet typeSet) {
            super();
            this.allTypes = typeSet;
        }

        private Object readResolve() {
            return TypeToken.this.getTypes().interfaces();
        }

        public TypeSet classes() {
            throw new UnsupportedOperationException("interfaces().classes() not supported.");
        }

        protected Set<TypeToken<? super T>> delegate() {
            Set set = this.interfaces;
            if (set != null) {
                return set;
            }
            set = FluentIterable.from(this.allTypes).filter(TypeFilter.INTERFACE_ONLY).toImmutableSet();
            this.interfaces = set;
            return set;
        }

        public TypeSet interfaces() {
            return this;
        }

        public Set<Class<? super T>> rawTypes() {
            return FluentIterable.from(TypeCollector.FOR_RAW_TYPE.collectTypes(TypeToken.this.getImmediateRawTypes())).filter(new C08221()).toImmutableSet();
        }
    }

    private static final class SimpleTypeToken<T> extends TypeToken<T> {
        private static final long serialVersionUID = 0;

        SimpleTypeToken(Type type) {
            super(type);
        }
    }

    private static abstract class TypeCollector<K> {
        static final TypeCollector<TypeToken<?>> FOR_GENERIC_TYPE = new C08231();
        static final TypeCollector<Class<?>> FOR_RAW_TYPE = new C08242();

        static final class C08231 extends TypeCollector<TypeToken<?>> {
            C08231() {
                super();
            }

            Iterable<? extends TypeToken<?>> getInterfaces(TypeToken<?> typeToken) {
                return typeToken.getGenericInterfaces();
            }

            Class<?> getRawType(TypeToken<?> typeToken) {
                return typeToken.getRawType();
            }

            @Nullable
            TypeToken<?> getSuperclass(TypeToken<?> typeToken) {
                return typeToken.getGenericSuperclass();
            }
        }

        static final class C08242 extends TypeCollector<Class<?>> {
            C08242() {
                super();
            }

            Iterable<? extends Class<?>> getInterfaces(Class<?> cls) {
                return Arrays.asList(cls.getInterfaces());
            }

            Class<?> getRawType(Class<?> cls) {
                return cls;
            }

            @Nullable
            Class<?> getSuperclass(Class<?> cls) {
                return cls.getSuperclass();
            }
        }

        private static class ForwardingTypeCollector<K> extends TypeCollector<K> {
            private final TypeCollector<K> delegate;

            ForwardingTypeCollector(TypeCollector<K> typeCollector) {
                super();
                this.delegate = typeCollector;
            }

            Iterable<? extends K> getInterfaces(K k) {
                return this.delegate.getInterfaces(k);
            }

            Class<?> getRawType(K k) {
                return this.delegate.getRawType(k);
            }

            K getSuperclass(K k) {
                return this.delegate.getSuperclass(k);
            }
        }

        private TypeCollector() {
        }

        private int collectTypes(K k, Map<? super K, Integer> map) {
            Integer num = (Integer) map.get(this);
            if (num != null) {
                return num.intValue();
            }
            int i = getRawType(k).isInterface() ? 1 : 0;
            for (Object collectTypes : getInterfaces(k)) {
                i = Math.max(i, collectTypes(collectTypes, map));
            }
            Object superclass = getSuperclass(k);
            if (superclass != null) {
                i = Math.max(i, collectTypes(superclass, map));
            }
            map.put(k, Integer.valueOf(i + 1));
            return i + 1;
        }

        private static <K, V> ImmutableList<K> sortKeysByValue(final Map<K, V> map, final Comparator<? super V> comparator) {
            return new Ordering<K>() {
                public int compare(K k, K k2) {
                    return comparator.compare(map.get(k), map.get(k2));
                }
            }.immutableSortedCopy(map.keySet());
        }

        final TypeCollector<K> classesOnly() {
            return new ForwardingTypeCollector<K>(this) {
                ImmutableList<K> collectTypes(Iterable<? extends K> iterable) {
                    Builder builder = ImmutableList.builder();
                    for (Object next : iterable) {
                        if (!getRawType(next).isInterface()) {
                            builder.add(next);
                        }
                    }
                    return super.collectTypes(builder.build());
                }

                Iterable<? extends K> getInterfaces(K k) {
                    return ImmutableSet.of();
                }
            };
        }

        ImmutableList<K> collectTypes(Iterable<? extends K> iterable) {
            Map newHashMap = Maps.newHashMap();
            for (Object collectTypes : iterable) {
                collectTypes(collectTypes, newHashMap);
            }
            return sortKeysByValue(newHashMap, Ordering.natural().reverse());
        }

        final ImmutableList<K> collectTypes(K k) {
            return collectTypes(ImmutableList.of(k));
        }

        abstract Iterable<? extends K> getInterfaces(K k);

        abstract Class<?> getRawType(K k);

        @Nullable
        abstract K getSuperclass(K k);
    }

    private enum TypeFilter implements Predicate<TypeToken<?>> {
        IGNORE_TYPE_VARIABLE_OR_WILDCARD {
            public boolean apply(TypeToken<?> typeToken) {
                return ((typeToken.runtimeType instanceof TypeVariable) || (typeToken.runtimeType instanceof WildcardType)) ? false : true;
            }
        },
        INTERFACE_ONLY {
            public boolean apply(TypeToken<?> typeToken) {
                return typeToken.getRawType().isInterface();
            }
        }
    }

    protected TypeToken() {
        this.runtimeType = capture();
        Preconditions.checkState(!(this.runtimeType instanceof TypeVariable), "Cannot construct a TypeToken for a type variable.\nYou probably meant to call new TypeToken<%s>(getClass()) that can resolve the type variable for you.\nIf you do need to create a TypeToken of a type variable, please use TypeToken.of() instead.", this.runtimeType);
    }

    protected TypeToken(Class<?> cls) {
        Type capture = super.capture();
        if (capture instanceof Class) {
            this.runtimeType = capture;
        } else {
            this.runtimeType = of((Class) cls).resolveType(capture).runtimeType;
        }
    }

    private TypeToken(Type type) {
        this.runtimeType = (Type) Preconditions.checkNotNull(type);
    }

    @Nullable
    private TypeToken<? super T> boundAsSuperclass(Type type) {
        TypeToken<? super T> of = of(type);
        return of.getRawType().isInterface() ? null : of;
    }

    private ImmutableList<TypeToken<? super T>> boundsAsInterfaces(Type[] typeArr) {
        Builder builder = ImmutableList.builder();
        for (Type of : typeArr) {
            Object of2 = of(of);
            if (of2.getRawType().isInterface()) {
                builder.add(of2);
            }
        }
        return builder.build();
    }

    private TypeToken<? extends T> getArraySubtype(Class<?> cls) {
        return of(newArrayClassOrGenericArrayType(getComponentType().getSubtype(cls.getComponentType()).runtimeType));
    }

    private TypeToken<? super T> getArraySupertype(Class<? super T> cls) {
        return of(newArrayClassOrGenericArrayType(((TypeToken) Preconditions.checkNotNull(getComponentType(), "%s isn't a super type of %s", cls, this)).getSupertype(cls.getComponentType()).runtimeType));
    }

    private ImmutableSet<Class<? super T>> getImmediateRawTypes() {
        return getRawTypes(this.runtimeType);
    }

    @VisibleForTesting
    static Class<?> getRawType(Type type) {
        return (Class) getRawTypes(type).iterator().next();
    }

    @VisibleForTesting
    static ImmutableSet<Class<?>> getRawTypes(Type type) {
        if (type instanceof Class) {
            return ImmutableSet.of((Class) type);
        }
        if (type instanceof ParameterizedType) {
            return ImmutableSet.of((Class) ((ParameterizedType) type).getRawType());
        }
        if (type instanceof GenericArrayType) {
            return ImmutableSet.of(Types.getArrayClass(getRawType(((GenericArrayType) type).getGenericComponentType())));
        }
        if (type instanceof TypeVariable) {
            return getRawTypes(((TypeVariable) type).getBounds());
        }
        if (type instanceof WildcardType) {
            return getRawTypes(((WildcardType) type).getUpperBounds());
        }
        throw new AssertionError(type + " unsupported");
    }

    private static ImmutableSet<Class<?>> getRawTypes(Type[] typeArr) {
        ImmutableSet.Builder builder = ImmutableSet.builder();
        for (Type rawTypes : typeArr) {
            builder.addAll(getRawTypes(rawTypes));
        }
        return builder.build();
    }

    private TypeToken<? extends T> getSubtypeFromLowerBounds(Class<?> cls, Type[] typeArr) {
        if (typeArr.length < 0) {
            return of(typeArr[0]).getSubtype(cls);
        }
        throw new IllegalArgumentException(cls + " isn't a subclass of " + this);
    }

    private TypeToken<? super T> getSupertypeFromUpperBounds(Class<? super T> cls, Type[] typeArr) {
        for (Type of : typeArr) {
            TypeToken of2 = of(of);
            if (of((Class) cls).isAssignableFrom(of2)) {
                return of2.getSupertype(cls);
            }
        }
        throw new IllegalArgumentException(cls + " isn't a super type of " + this);
    }

    private static boolean isAssignable(Type type, Type type2) {
        return type2.equals(type) ? true : type2 instanceof WildcardType ? isAssignableToWildcardType(type, (WildcardType) type2) : type instanceof TypeVariable ? isAssignableFromAny(((TypeVariable) type).getBounds(), type2) : type instanceof WildcardType ? isAssignableFromAny(((WildcardType) type).getUpperBounds(), type2) : type instanceof GenericArrayType ? isAssignableFromGenericArrayType((GenericArrayType) type, type2) : type2 instanceof Class ? isAssignableToClass(type, (Class) type2) : type2 instanceof ParameterizedType ? isAssignableToParameterizedType(type, (ParameterizedType) type2) : type2 instanceof GenericArrayType ? isAssignableToGenericArrayType(type, (GenericArrayType) type2) : false;
    }

    private static boolean isAssignableBySubtypeBound(Type type, WildcardType wildcardType) {
        Type subtypeBound = subtypeBound(wildcardType);
        if (subtypeBound == null) {
            return true;
        }
        Type subtypeBound2 = subtypeBound(type);
        return subtypeBound2 == null ? false : isAssignable(subtypeBound, subtypeBound2);
    }

    private static boolean isAssignableFromAny(Type[] typeArr, Type type) {
        for (Type isAssignable : typeArr) {
            if (isAssignable(isAssignable, type)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isAssignableFromGenericArrayType(GenericArrayType genericArrayType, Type type) {
        if (type instanceof Class) {
            Class cls = (Class) type;
            return !cls.isArray() ? cls == Object.class : isAssignable(genericArrayType.getGenericComponentType(), cls.getComponentType());
        } else if (!(type instanceof GenericArrayType)) {
            return false;
        } else {
            return isAssignable(genericArrayType.getGenericComponentType(), ((GenericArrayType) type).getGenericComponentType());
        }
    }

    private static boolean isAssignableToClass(Type type, Class<?> cls) {
        return cls.isAssignableFrom(getRawType(type));
    }

    private static boolean isAssignableToGenericArrayType(Type type, GenericArrayType genericArrayType) {
        if (type instanceof Class) {
            Class cls = (Class) type;
            if (cls.isArray()) {
                return isAssignable(cls.getComponentType(), genericArrayType.getGenericComponentType());
            }
        } else if (type instanceof GenericArrayType) {
            return isAssignable(((GenericArrayType) type).getGenericComponentType(), genericArrayType.getGenericComponentType());
        }
        return false;
    }

    private static boolean isAssignableToParameterizedType(Type type, ParameterizedType parameterizedType) {
        Class rawType = getRawType(parameterizedType);
        if (!rawType.isAssignableFrom(getRawType(type))) {
            return false;
        }
        TypeVariable[] typeParameters = rawType.getTypeParameters();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        TypeToken of = of(type);
        for (int i = 0; i < typeParameters.length; i++) {
            if (!matchTypeArgument(of.resolveType(typeParameters[i]).runtimeType, actualTypeArguments[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean isAssignableToWildcardType(Type type, WildcardType wildcardType) {
        return isAssignable(type, supertypeBound(wildcardType)) && isAssignableBySubtypeBound(type, wildcardType);
    }

    private static boolean matchTypeArgument(Type type, Type type2) {
        return type.equals(type2) ? true : type2 instanceof WildcardType ? isAssignableToWildcardType(type, (WildcardType) type2) : false;
    }

    private static Type newArrayClassOrGenericArrayType(Type type) {
        return JavaVersion.JAVA7.newArrayType(type);
    }

    public static <T> TypeToken<T> of(Class<T> cls) {
        return new SimpleTypeToken(cls);
    }

    public static TypeToken<?> of(Type type) {
        return new SimpleTypeToken(type);
    }

    private TypeToken<?> resolveSupertype(Type type) {
        TypeToken<?> resolveType = resolveType(type);
        resolveType.typeResolver = this.typeResolver;
        return resolveType;
    }

    private Type resolveTypeArgsForSubclass(Class<?> cls) {
        if (this.runtimeType instanceof Class) {
            return cls;
        }
        TypeToken toGenericType = toGenericType(cls);
        return new TypeResolver().where(toGenericType.getSupertype(getRawType()).runtimeType, this.runtimeType).resolveType(toGenericType.runtimeType);
    }

    @Nullable
    private static Type subtypeBound(Type type) {
        return type instanceof WildcardType ? subtypeBound((WildcardType) type) : type;
    }

    @Nullable
    private static Type subtypeBound(WildcardType wildcardType) {
        Type[] lowerBounds = wildcardType.getLowerBounds();
        if (lowerBounds.length == 1) {
            return subtypeBound(lowerBounds[0]);
        }
        if (lowerBounds.length == 0) {
            return null;
        }
        throw new AssertionError("Wildcard should have at most one lower bound: " + wildcardType);
    }

    private static Type supertypeBound(Type type) {
        return type instanceof WildcardType ? supertypeBound((WildcardType) type) : type;
    }

    private static Type supertypeBound(WildcardType wildcardType) {
        Type[] upperBounds = wildcardType.getUpperBounds();
        if (upperBounds.length == 1) {
            return supertypeBound(upperBounds[0]);
        }
        if (upperBounds.length == 0) {
            return Object.class;
        }
        throw new AssertionError("There should be at most one upper bound for wildcard type: " + wildcardType);
    }

    @VisibleForTesting
    static <T> TypeToken<? extends T> toGenericType(Class<T> cls) {
        if (cls.isArray()) {
            return of(Types.newArrayType(toGenericType(cls.getComponentType()).runtimeType));
        }
        Type[] typeParameters = cls.getTypeParameters();
        return typeParameters.length > 0 ? of(Types.newParameterizedType(cls, typeParameters)) : of((Class) cls);
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof TypeToken)) {
            return false;
        }
        return this.runtimeType.equals(((TypeToken) obj).runtimeType);
    }

    @Nullable
    public final TypeToken<?> getComponentType() {
        Type componentType = Types.getComponentType(this.runtimeType);
        return componentType == null ? null : of(componentType);
    }

    final ImmutableList<TypeToken<? super T>> getGenericInterfaces() {
        if (this.runtimeType instanceof TypeVariable) {
            return boundsAsInterfaces(((TypeVariable) this.runtimeType).getBounds());
        }
        if (this.runtimeType instanceof WildcardType) {
            return boundsAsInterfaces(((WildcardType) this.runtimeType).getUpperBounds());
        }
        Builder builder = ImmutableList.builder();
        for (Type resolveSupertype : getRawType().getGenericInterfaces()) {
            builder.add(resolveSupertype(resolveSupertype));
        }
        return builder.build();
    }

    @Nullable
    final TypeToken<? super T> getGenericSuperclass() {
        if (this.runtimeType instanceof TypeVariable) {
            return boundAsSuperclass(((TypeVariable) this.runtimeType).getBounds()[0]);
        }
        if (this.runtimeType instanceof WildcardType) {
            return boundAsSuperclass(((WildcardType) this.runtimeType).getUpperBounds()[0]);
        }
        Type genericSuperclass = getRawType().getGenericSuperclass();
        return genericSuperclass == null ? null : resolveSupertype(genericSuperclass);
    }

    public final Class<? super T> getRawType() {
        return getRawType(this.runtimeType);
    }

    public final TypeToken<? extends T> getSubtype(Class<?> cls) {
        Preconditions.checkArgument(!(this.runtimeType instanceof TypeVariable), "Cannot get subtype of type variable <%s>", this);
        if (this.runtimeType instanceof WildcardType) {
            return getSubtypeFromLowerBounds(cls, ((WildcardType) this.runtimeType).getLowerBounds());
        }
        Preconditions.checkArgument(getRawType().isAssignableFrom(cls), "%s isn't a subclass of %s", cls, this);
        return isArray() ? getArraySubtype(cls) : of(resolveTypeArgsForSubclass(cls));
    }

    public final TypeToken<? super T> getSupertype(Class<? super T> cls) {
        Preconditions.checkArgument(cls.isAssignableFrom(getRawType()), "%s is not a super class of %s", cls, this);
        return this.runtimeType instanceof TypeVariable ? getSupertypeFromUpperBounds(cls, ((TypeVariable) this.runtimeType).getBounds()) : this.runtimeType instanceof WildcardType ? getSupertypeFromUpperBounds(cls, ((WildcardType) this.runtimeType).getUpperBounds()) : cls.isArray() ? getArraySupertype(cls) : resolveSupertype(toGenericType(cls).runtimeType);
    }

    public final Type getType() {
        return this.runtimeType;
    }

    public final TypeSet getTypes() {
        return new TypeSet();
    }

    public int hashCode() {
        return this.runtimeType.hashCode();
    }

    public final boolean isArray() {
        return getComponentType() != null;
    }

    public final boolean isAssignableFrom(TypeToken<?> typeToken) {
        return isAssignableFrom(typeToken.runtimeType);
    }

    public final boolean isAssignableFrom(Type type) {
        return isAssignable((Type) Preconditions.checkNotNull(type), this.runtimeType);
    }

    final TypeToken<T> rejectTypeVariables() {
        Preconditions.checkArgument(!Types.containsTypeVariable(this.runtimeType), "%s contains a type variable and is not safe for the operation");
        return this;
    }

    public final TypeToken<?> resolveType(Type type) {
        Preconditions.checkNotNull(type);
        TypeResolver typeResolver = this.typeResolver;
        if (typeResolver == null) {
            typeResolver = TypeResolver.accordingTo(this.runtimeType);
            this.typeResolver = typeResolver;
        }
        return of(typeResolver.resolveType(type));
    }

    public String toString() {
        return Types.toString(this.runtimeType);
    }

    public final <X> TypeToken<T> where(TypeParameter<X> typeParameter, TypeToken<X> typeToken) {
        return new SimpleTypeToken(new TypeResolver().where(ImmutableMap.of(typeParameter.typeVariable, typeToken.runtimeType)).resolveType(this.runtimeType));
    }

    public final <X> TypeToken<T> where(TypeParameter<X> typeParameter, Class<X> cls) {
        return where((TypeParameter) typeParameter, of((Class) cls));
    }

    protected Object writeReplace() {
        return of(new TypeResolver().resolveType(this.runtimeType));
    }
}
