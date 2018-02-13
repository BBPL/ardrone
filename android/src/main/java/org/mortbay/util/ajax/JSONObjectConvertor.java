package org.mortbay.util.ajax;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.mortbay.util.ajax.JSON.Convertor;
import org.mortbay.util.ajax.JSON.Output;

public class JSONObjectConvertor implements Convertor {
    static Class class$java$lang$Object;
    private Set _excluded;
    private boolean _fromJSON;

    public JSONObjectConvertor() {
        this._excluded = null;
        this._fromJSON = false;
    }

    public JSONObjectConvertor(boolean z) {
        this._excluded = null;
        this._fromJSON = z;
    }

    public JSONObjectConvertor(boolean z, String[] strArr) {
        this._excluded = null;
        this._fromJSON = z;
        if (strArr != null) {
            this._excluded = new HashSet(Arrays.asList(strArr));
        }
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    public Object fromJSON(Map map) {
        if (!this._fromJSON) {
            return map;
        }
        throw new UnsupportedOperationException();
    }

    protected boolean includeField(String str, Object obj, Method method) {
        return this._excluded == null || !this._excluded.contains(str);
    }

    public void toJSON(Object obj, Output output) {
        try {
            obj.getClass();
            if (this._fromJSON) {
                output.addClass(obj.getClass());
            }
            Method[] methods = obj.getClass().getMethods();
            for (Method method : methods) {
                if (!(Modifier.isStatic(method.getModifiers()) || method.getParameterTypes().length != 0 || method.getReturnType() == null)) {
                    Class class$;
                    Class declaringClass = method.getDeclaringClass();
                    if (class$java$lang$Object == null) {
                        class$ = class$("java.lang.Object");
                        class$java$lang$Object = class$;
                    } else {
                        class$ = class$java$lang$Object;
                    }
                    if (declaringClass != class$) {
                        String stringBuffer;
                        String name = method.getName();
                        if (name.startsWith("is")) {
                            stringBuffer = new StringBuffer().append(name.substring(2, 3).toLowerCase()).append(name.substring(3)).toString();
                        } else if (name.startsWith("get")) {
                            stringBuffer = new StringBuffer().append(name.substring(3, 4).toLowerCase()).append(name.substring(4)).toString();
                        }
                        if (includeField(stringBuffer, obj, method)) {
                            output.add(stringBuffer, method.invoke(obj, (Object[]) null));
                        }
                    }
                }
            }
        } catch (Throwable th) {
            RuntimeException runtimeException = new RuntimeException("Illegal argument", th);
        }
    }
}
