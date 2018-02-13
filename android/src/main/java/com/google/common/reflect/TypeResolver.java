package com.google.common.reflect;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

class TypeResolver {
    private final ImmutableMap<TypeVariable<?>, Type> typeTable;

    private static final class TypeMappingIntrospector {
        private static final WildcardCapturer wildcardCapturer = new WildcardCapturer();
        private final Set<Type> introspectedTypes = Sets.newHashSet();
        private final Map<TypeVariable<?>, Type> mappings = Maps.newHashMap();

        private TypeMappingIntrospector() {
        }

        static ImmutableMap<TypeVariable<?>, Type> getTypeMappings(Type type) {
            TypeMappingIntrospector typeMappingIntrospector = new TypeMappingIntrospector();
            typeMappingIntrospector.introspect(wildcardCapturer.capture(type));
            return ImmutableMap.copyOf(typeMappingIntrospector.mappings);
        }

        private void introspect(Type type) {
            int i = 0;
            if (!this.introspectedTypes.add(type)) {
                return;
            }
            if (type instanceof ParameterizedType) {
                introspectParameterizedType((ParameterizedType) type);
            } else if (type instanceof Class) {
                introspectClass((Class) type);
            } else if (type instanceof TypeVariable) {
                r1 = ((TypeVariable) type).getBounds();
                r2 = r1.length;
                while (i < r2) {
                    introspect(r1[i]);
                    i++;
                }
            } else if (type instanceof WildcardType) {
                r1 = ((WildcardType) type).getUpperBounds();
                r2 = r1.length;
                while (i < r2) {
                    introspect(r1[i]);
                    i++;
                }
            }
        }

        private void introspectClass(Class<?> cls) {
            introspect(cls.getGenericSuperclass());
            for (Type introspect : cls.getGenericInterfaces()) {
                introspect(introspect);
            }
        }

        private void introspectParameterizedType(ParameterizedType parameterizedType) {
            int i = 0;
            Class cls = (Class) parameterizedType.getRawType();
            TypeVariable[] typeParameters = cls.getTypeParameters();
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            Preconditions.checkState(typeParameters.length == actualTypeArguments.length);
            while (i < typeParameters.length) {
                map(typeParameters[i], actualTypeArguments[i]);
                i++;
            }
            introspectClass(cls);
            introspect(parameterizedType.getOwnerType());
        }

        private void map(TypeVariable<?> typeVariable, Type type) {
            if (!this.mappings.containsKey(typeVariable)) {
                Object obj = type;
                while (obj != null) {
                    if (typeVariable.equals(obj)) {
                        while (r4 != null) {
                            type = (Type) this.mappings.remove(r4);
                        }
                        return;
                    }
                    Type type2 = (Type) this.mappings.get(obj);
                }
                this.mappings.put(typeVariable, type);
            }
        }
    }

    private static final class WildcardCapturer {
        private final AtomicInteger id;

        private WildcardCapturer() {
            this.id = new AtomicInteger();
        }

        private Type[] capture(Type[] typeArr) {
            Type[] typeArr2 = new Type[typeArr.length];
            for (int i = 0; i < typeArr.length; i++) {
                typeArr2[i] = capture(typeArr[i]);
            }
            return typeArr2;
        }

        private Type captureNullable(@Nullable Type type) {
            return type == null ? null : capture(type);
        }

        Type capture(Type type) {
            Preconditions.checkNotNull(type);
            if ((type instanceof Class) || (type instanceof TypeVariable)) {
                return type;
            }
            if (type instanceof GenericArrayType) {
                return Types.newArrayType(capture(((GenericArrayType) type).getGenericComponentType()));
            }
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                return Types.newParameterizedTypeWithOwner(captureNullable(parameterizedType.getOwnerType()), (Class) parameterizedType.getRawType(), capture(parameterizedType.getActualTypeArguments()));
            } else if (type instanceof WildcardType) {
                WildcardType wildcardType = (WildcardType) type;
                if (wildcardType.getLowerBounds().length != 0) {
                    return type;
                }
                return Types.newTypeVariable(WildcardCapturer.class, "capture#" + this.id.incrementAndGet() + "-of ? extends " + Joiner.on('&').join(wildcardType.getUpperBounds()), wildcardType.getUpperBounds());
            } else {
                throw new AssertionError("must have been one of the known types");
            }
        }
    }

    public TypeResolver() {
        this.typeTable = ImmutableMap.of();
    }

    private TypeResolver(ImmutableMap<TypeVariable<?>, Type> immutableMap) {
        this.typeTable = immutableMap;
    }

    static TypeResolver accordingTo(Type type) {
        return new TypeResolver().where(TypeMappingIntrospector.getTypeMappings(type));
    }

    private static <T> T checkNonNullArgument(T t, String str, Object... objArr) {
        Preconditions.checkArgument(t != null, str, objArr);
        return t;
    }

    private static <T> T expectArgument(Class<T> cls, Object obj) {
        try {
            return cls.cast(obj);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(obj + " is not a " + cls.getSimpleName());
        }
    }

    private static void populateTypeMappings(Map<TypeVariable<?>, Type> map, Type type, Type type2) {
        int i = 0;
        if (!type.equals(type2)) {
            if (type instanceof TypeVariable) {
                map.put((TypeVariable) type, type2);
            } else if (type instanceof GenericArrayType) {
                populateTypeMappings(map, ((GenericArrayType) type).getGenericComponentType(), (Type) checkNonNullArgument(Types.getComponentType(type2), "%s is not an array type.", type2));
            } else if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                ParameterizedType parameterizedType2 = (ParameterizedType) expectArgument(ParameterizedType.class, type2);
                Preconditions.checkArgument(parameterizedType.getRawType().equals(parameterizedType2.getRawType()), "Inconsistent raw type: %s vs. %s", type, type2);
                r4 = parameterizedType.getActualTypeArguments();
                r1 = parameterizedType2.getActualTypeArguments();
                Preconditions.checkArgument(r4.length == r1.length);
                while (i < r4.length) {
                    populateTypeMappings(map, r4[i], r1[i]);
                    i++;
                }
            } else if (type instanceof WildcardType) {
                WildcardType wildcardType = (WildcardType) type;
                WildcardType wildcardType2 = (WildcardType) expectArgument(WildcardType.class, type2);
                r4 = wildcardType.getUpperBounds();
                Type[] upperBounds = wildcardType2.getUpperBounds();
                Type[] lowerBounds = wildcardType.getLowerBounds();
                r1 = wildcardType2.getLowerBounds();
                boolean z = r4.length == upperBounds.length && lowerBounds.length == r1.length;
                Preconditions.checkArgument(z, "Incompatible type: %s vs. %s", type, type2);
                for (int i2 = 0; i2 < r4.length; i2++) {
                    populateTypeMappings(map, r4[i2], upperBounds[i2]);
                }
                while (i < lowerBounds.length) {
                    populateTypeMappings(map, lowerBounds[i], r1[i]);
                    i++;
                }
            } else {
                throw new IllegalArgumentException("No type mapping from " + type);
            }
        }
    }

    private Type resolveGenericArrayType(GenericArrayType genericArrayType) {
        return Types.newArrayType(resolveType(genericArrayType.getGenericComponentType()));
    }

    private ParameterizedType resolveParameterizedType(ParameterizedType parameterizedType) {
        Type ownerType = parameterizedType.getOwnerType();
        Type resolveType = ownerType == null ? null : resolveType(ownerType);
        ownerType = resolveType(parameterizedType.getRawType());
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Type[] typeArr = new Type[actualTypeArguments.length];
        for (int i = 0; i < actualTypeArguments.length; i++) {
            typeArr[i] = resolveType(actualTypeArguments[i]);
        }
        return Types.newParameterizedTypeWithOwner(resolveType, (Class) ownerType, typeArr);
    }

    private Type resolveTypeVariable(final TypeVariable<?> typeVariable) {
        return resolveTypeVariable(typeVariable, new TypeResolver(this.typeTable) {
            Type resolveTypeVariable(TypeVariable<?> typeVariable, TypeResolver typeResolver) {
                return typeVariable.getGenericDeclaration().equals(typeVariable.getGenericDeclaration()) ? typeVariable : this.resolveTypeVariable(typeVariable, typeResolver);
            }
        });
    }

    private Type[] resolveTypes(Type[] typeArr) {
        Type[] typeArr2 = new Type[typeArr.length];
        for (int i = 0; i < typeArr.length; i++) {
            typeArr2[i] = resolveType(typeArr[i]);
        }
        return typeArr2;
    }

    public final Type resolveType(Type type) {
        if (type instanceof TypeVariable) {
            return resolveTypeVariable((TypeVariable) type);
        }
        if (type instanceof ParameterizedType) {
            return resolveParameterizedType((ParameterizedType) type);
        }
        if (type instanceof GenericArrayType) {
            return resolveGenericArrayType((GenericArrayType) type);
        }
        if (!(type instanceof WildcardType)) {
            return type;
        }
        WildcardType wildcardType = (WildcardType) type;
        return new WildcardTypeImpl(resolveTypes(wildcardType.getLowerBounds()), resolveTypes(wildcardType.getUpperBounds()));
    }

    Type resolveTypeVariable(TypeVariable<?> typeVariable, TypeResolver typeResolver) {
        Type type = (Type) this.typeTable.get(typeVariable);
        if (type != null) {
            return typeResolver.resolveType(type);
        }
        Type[] bounds = typeVariable.getBounds();
        return bounds.length == 0 ? typeVariable : Types.newTypeVariable(typeVariable.getGenericDeclaration(), typeVariable.getName(), typeResolver.resolveTypes(bounds));
    }

    public final TypeResolver where(Type type, Type type2) {
        Map newHashMap = Maps.newHashMap();
        populateTypeMappings(newHashMap, type, type2);
        return where(newHashMap);
    }

    final TypeResolver where(Map<? extends TypeVariable<?>, ? extends Type> map) {
        Builder builder = ImmutableMap.builder();
        builder.putAll(this.typeTable);
        for (Entry entry : map.entrySet()) {
            TypeVariable typeVariable = (TypeVariable) entry.getKey();
            Type type = (Type) entry.getValue();
            Preconditions.checkArgument(!typeVariable.equals(type), "Type variable %s bound to itself", typeVariable);
            builder.put(typeVariable, type);
        }
        return new TypeResolver(builder.build());
    }
}
