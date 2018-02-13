package org.mortbay.util.ajax;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.mortbay.log.Log;
import org.mortbay.util.ajax.JSON.Convertor;
import org.mortbay.util.ajax.JSON.Output;

public class JSONPojoConvertor implements Convertor {
    public static final NumberType DOUBLE = new C13605();
    public static final NumberType FLOAT = new C13583();
    public static final Object[] GETTER_ARG = new Object[0];
    public static final NumberType INTEGER = new C13572();
    public static final NumberType LONG = new C13594();
    public static final Object[] NULL_ARG = new Object[]{null};
    public static final NumberType SHORT = new C13561();
    private static final Map __numberTypes = new HashMap();
    static Class class$java$lang$Double;
    static Class class$java$lang$Float;
    static Class class$java$lang$Integer;
    static Class class$java$lang$Long;
    static Class class$java$lang$Object;
    static Class class$java$lang$Short;
    protected Set _excluded;
    protected boolean _fromJSON;
    protected Map _getters;
    protected Class _pojoClass;
    protected Map _setters;

    public interface NumberType {
        Object getActualValue(Number number);
    }

    final class C13561 implements NumberType {
        C13561() {
        }

        public Object getActualValue(Number number) {
            return new Short(number.shortValue());
        }
    }

    final class C13572 implements NumberType {
        C13572() {
        }

        public Object getActualValue(Number number) {
            return new Integer(number.intValue());
        }
    }

    final class C13583 implements NumberType {
        C13583() {
        }

        public Object getActualValue(Number number) {
            return new Float(number.floatValue());
        }
    }

    final class C13594 implements NumberType {
        C13594() {
        }

        public Object getActualValue(Number number) {
            return number instanceof Long ? number : new Long(number.longValue());
        }
    }

    final class C13605 implements NumberType {
        C13605() {
        }

        public Object getActualValue(Number number) {
            return number instanceof Double ? number : new Double(number.doubleValue());
        }
    }

    public static class Setter {
        protected Class _componentType;
        protected Method _method;
        protected NumberType _numberType = ((NumberType) JSONPojoConvertor.access$000().get(this._type));
        protected String _propertyName;
        protected Class _type;

        public Setter(String str, Method method) {
            this._propertyName = str;
            this._method = method;
            this._type = method.getParameterTypes()[0];
            if (this._numberType == null && this._type.isArray()) {
                this._componentType = this._type.getComponentType();
                this._numberType = (NumberType) JSONPojoConvertor.access$000().get(this._componentType);
            }
        }

        public Class getComponentType() {
            return this._componentType;
        }

        public Method getMethod() {
            return this._method;
        }

        public NumberType getNumberType() {
            return this._numberType;
        }

        public String getPropertyName() {
            return this._propertyName;
        }

        public Class getType() {
            return this._type;
        }

        public void invoke(Object obj, Object obj2) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
            if (obj2 == null) {
                this._method.invoke(obj, JSONPojoConvertor.NULL_ARG);
            } else {
                invokeObject(obj, obj2);
            }
        }

        protected void invokeObject(Object obj, Object obj2) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
            if (this._numberType != null && (obj2 instanceof Number)) {
                this._method.invoke(obj, new Object[]{this._numberType.getActualValue((Number) obj2)});
            } else if (this._componentType == null || !obj2.getClass().isArray()) {
                this._method.invoke(obj, new Object[]{obj2});
            } else if (this._numberType == null) {
                int length = Array.getLength(obj2);
                try {
                    System.arraycopy(obj2, 0, Array.newInstance(this._componentType, length), 0, length);
                    this._method.invoke(obj, new Object[]{r1});
                } catch (Throwable e) {
                    Log.ignore(e);
                    this._method.invoke(obj, new Object[]{obj2});
                }
            } else {
                Object[] objArr = (Object[]) obj2;
                Object newInstance = Array.newInstance(this._componentType, objArr.length);
                int i = 0;
                while (i < objArr.length) {
                    try {
                        Array.set(newInstance, i, this._numberType.getActualValue((Number) objArr[i]));
                        i++;
                    } catch (Throwable e2) {
                        Log.ignore(e2);
                        this._method.invoke(obj, new Object[]{obj2});
                        return;
                    }
                }
                this._method.invoke(obj, new Object[]{newInstance});
            }
        }

        public boolean isPropertyNumber() {
            return this._numberType != null;
        }
    }

    static {
        Object class$;
        Map map = __numberTypes;
        if (class$java$lang$Short == null) {
            class$ = class$("java.lang.Short");
            class$java$lang$Short = class$;
        } else {
            class$ = class$java$lang$Short;
        }
        map.put(class$, SHORT);
        __numberTypes.put(Short.TYPE, SHORT);
        map = __numberTypes;
        if (class$java$lang$Integer == null) {
            class$ = class$("java.lang.Integer");
            class$java$lang$Integer = class$;
        } else {
            class$ = class$java$lang$Integer;
        }
        map.put(class$, INTEGER);
        __numberTypes.put(Integer.TYPE, INTEGER);
        map = __numberTypes;
        if (class$java$lang$Long == null) {
            class$ = class$("java.lang.Long");
            class$java$lang$Long = class$;
        } else {
            class$ = class$java$lang$Long;
        }
        map.put(class$, LONG);
        __numberTypes.put(Long.TYPE, LONG);
        map = __numberTypes;
        if (class$java$lang$Float == null) {
            class$ = class$("java.lang.Float");
            class$java$lang$Float = class$;
        } else {
            class$ = class$java$lang$Float;
        }
        map.put(class$, FLOAT);
        __numberTypes.put(Float.TYPE, FLOAT);
        map = __numberTypes;
        if (class$java$lang$Double == null) {
            class$ = class$("java.lang.Double");
            class$java$lang$Double = class$;
        } else {
            class$ = class$java$lang$Double;
        }
        map.put(class$, DOUBLE);
        __numberTypes.put(Double.TYPE, DOUBLE);
    }

    public JSONPojoConvertor(Class cls) {
        this(cls, (Set) null, true);
    }

    public JSONPojoConvertor(Class cls, Set set) {
        this(cls, set, true);
    }

    public JSONPojoConvertor(Class cls, Set set, boolean z) {
        this._getters = new HashMap();
        this._setters = new HashMap();
        this._pojoClass = cls;
        this._excluded = set;
        this._fromJSON = z;
        init();
    }

    public JSONPojoConvertor(Class cls, boolean z) {
        this(cls, (Set) null, z);
    }

    public JSONPojoConvertor(Class cls, String[] strArr) {
        this(cls, new HashSet(Arrays.asList(strArr)), true);
    }

    static Map access$000() {
        return __numberTypes;
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    public static NumberType getNumberType(Class cls) {
        return (NumberType) __numberTypes.get(cls);
    }

    protected void addGetter(String str, Method method) {
        this._getters.put(str, method);
    }

    protected void addSetter(String str, Method method) {
        this._setters.put(str, new Setter(str, method));
    }

    public Object fromJSON(Map map) {
        try {
            Object newInstance = this._pojoClass.newInstance();
            setProps(newInstance, map);
            return newInstance;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    protected int getExcludedCount() {
        return this._excluded == null ? 0 : this._excluded.size();
    }

    protected Setter getSetter(String str) {
        return (Setter) this._setters.get(str);
    }

    protected boolean includeField(String str, Method method) {
        return this._excluded == null || !this._excluded.contains(str);
    }

    protected void init() {
        Method[] methods = this._pojoClass.getMethods();
        for (Method method : methods) {
            if (!Modifier.isStatic(method.getModifiers())) {
                Class class$;
                Class declaringClass = method.getDeclaringClass();
                if (class$java$lang$Object == null) {
                    class$ = class$("java.lang.Object");
                    class$java$lang$Object = class$;
                } else {
                    class$ = class$java$lang$Object;
                }
                if (declaringClass != class$) {
                    String name = method.getName();
                    switch (method.getParameterTypes().length) {
                        case 0:
                            if (method.getReturnType() != null) {
                                if (name.startsWith("is") && name.length() > 2) {
                                    name = new StringBuffer().append(name.substring(2, 3).toLowerCase()).append(name.substring(3)).toString();
                                } else if (name.startsWith("get") && name.length() > 3) {
                                    name = new StringBuffer().append(name.substring(3, 4).toLowerCase()).append(name.substring(4)).toString();
                                }
                                if (!includeField(name, method)) {
                                    break;
                                }
                                addGetter(name, method);
                                break;
                            }
                            break;
                            break;
                        case 1:
                            if (name.startsWith("set") && name.length() > 3) {
                                name = new StringBuffer().append(name.substring(3, 4).toLowerCase()).append(name.substring(4)).toString();
                                if (!includeField(name, method)) {
                                    break;
                                }
                                addSetter(name, method);
                                break;
                            }
                        default:
                            break;
                    }
                }
            }
        }
    }

    protected void log(Throwable th) {
        Log.ignore(th);
    }

    public int setProps(Object obj, Map map) {
        int i = 0;
        for (Entry entry : map.entrySet()) {
            Setter setter = getSetter((String) entry.getKey());
            if (setter != null) {
                try {
                    setter.invoke(obj, entry.getValue());
                    i++;
                } catch (Throwable e) {
                    Log.warn("{} property '{}' not set. (errors)", this._pojoClass.getName(), setter.getPropertyName());
                    log(e);
                }
            }
        }
        return i;
    }

    public void toJSON(Object obj, Output output) {
        if (this._fromJSON) {
            output.addClass(this._pojoClass);
        }
        for (Entry entry : this._getters.entrySet()) {
            try {
                output.add((String) entry.getKey(), ((Method) entry.getValue()).invoke(obj, GETTER_ARG));
            } catch (Throwable e) {
                Log.warn("{} property '{}' excluded. (errors)", this._pojoClass.getName(), entry.getKey());
                log(e);
            }
        }
    }
}
