package org.mortbay.util.ajax;

import java.lang.reflect.Method;
import java.util.Map;
import org.mortbay.log.Log;
import org.mortbay.util.Loader;
import org.mortbay.util.ajax.JSON.Convertor;
import org.mortbay.util.ajax.JSON.Output;

public class JSONEnumConvertor implements Convertor {
    static Class class$java$lang$Class;
    static Class class$java$lang$String;
    private boolean _fromJSON;
    private Method _valueOf;

    public JSONEnumConvertor() {
        this(false);
    }

    public JSONEnumConvertor(boolean z) {
        try {
            Class class$;
            Class cls;
            Class loadClass = Loader.loadClass(getClass(), "java.lang.Enum");
            if (class$java$lang$Class == null) {
                class$ = class$("java.lang.Class");
                class$java$lang$Class = class$;
                cls = class$;
            } else {
                cls = class$java$lang$Class;
            }
            if (class$java$lang$String == null) {
                class$ = class$("java.lang.String");
                class$java$lang$String = class$;
            } else {
                class$ = class$java$lang$String;
            }
            this._valueOf = loadClass.getMethod("valueOf", new Class[]{cls, class$});
            this._fromJSON = z;
        } catch (Throwable e) {
            throw new RuntimeException("!Enums", e);
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
        if (this._fromJSON) {
            try {
                Class loadClass = Loader.loadClass(getClass(), (String) map.get("class"));
                return this._valueOf.invoke(null, new Object[]{loadClass, map.get("value")});
            } catch (Throwable e) {
                Log.warn(e);
                return null;
            }
        }
        throw new UnsupportedOperationException();
    }

    public void toJSON(Object obj, Output output) {
        if (this._fromJSON) {
            output.addClass(obj.getClass());
            output.add("value", obj.toString());
            return;
        }
        output.add(obj.toString());
    }
}
