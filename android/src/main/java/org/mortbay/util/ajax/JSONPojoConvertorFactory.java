package org.mortbay.util.ajax;

import java.util.Map;
import org.mortbay.util.Loader;
import org.mortbay.util.ajax.JSON.Convertor;
import org.mortbay.util.ajax.JSON.Output;

public class JSONPojoConvertorFactory implements Convertor {
    static Class class$java$lang$Object;
    static Class class$org$mortbay$util$ajax$JSON;
    private final boolean _fromJSON;
    private final JSON _json;

    public JSONPojoConvertorFactory(JSON json) {
        this._json = json;
        this._fromJSON = true;
        if (json == null) {
            throw new IllegalArgumentException();
        }
    }

    public JSONPojoConvertorFactory(JSON json, boolean z) {
        this._json = json;
        this._fromJSON = z;
        if (json == null) {
            throw new IllegalArgumentException();
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
        Class class$;
        ClassNotFoundException e;
        String str = (String) map.get("class");
        if (str == null) {
            return map;
        }
        Convertor jSONPojoConvertor;
        Convertor convertorFor = this._json.getConvertorFor(str);
        if (convertorFor == null) {
            try {
                if (class$org$mortbay$util$ajax$JSON == null) {
                    class$ = class$("org.mortbay.util.ajax.JSON");
                    class$org$mortbay$util$ajax$JSON = class$;
                } else {
                    class$ = class$org$mortbay$util$ajax$JSON;
                }
                jSONPojoConvertor = new JSONPojoConvertor(Loader.loadClass(class$, str));
                try {
                    this._json.addConvertorFor(str, jSONPojoConvertor);
                } catch (ClassNotFoundException e2) {
                    e = e2;
                    e.printStackTrace();
                    if (jSONPojoConvertor != null) {
                        return map;
                    }
                    if (class$java$lang$Object == null) {
                        class$ = class$("java.lang.Object");
                        class$java$lang$Object = class$;
                    } else {
                        class$ = class$java$lang$Object;
                    }
                    return str.equals(class$.getName()) ? jSONPojoConvertor.fromJSON(map) : map;
                }
            } catch (ClassNotFoundException e3) {
                e = e3;
                jSONPojoConvertor = convertorFor;
                e.printStackTrace();
                if (jSONPojoConvertor != null) {
                    return map;
                }
                if (class$java$lang$Object == null) {
                    class$ = class$java$lang$Object;
                } else {
                    class$ = class$("java.lang.Object");
                    class$java$lang$Object = class$;
                }
                if (str.equals(class$.getName())) {
                }
            }
        }
        jSONPojoConvertor = convertorFor;
        if (jSONPojoConvertor != null) {
            return map;
        }
        if (class$java$lang$Object == null) {
            class$ = class$("java.lang.Object");
            class$java$lang$Object = class$;
        } else {
            class$ = class$java$lang$Object;
        }
        if (str.equals(class$.getName())) {
        }
    }

    public void toJSON(Object obj, Output output) {
        Class class$;
        Convertor jSONPojoConvertor;
        ClassNotFoundException e;
        Class cls;
        String name = obj.getClass().getName();
        Convertor convertorFor = this._json.getConvertorFor(name);
        if (convertorFor == null) {
            try {
                if (class$org$mortbay$util$ajax$JSON == null) {
                    class$ = class$("org.mortbay.util.ajax.JSON");
                    class$org$mortbay$util$ajax$JSON = class$;
                } else {
                    class$ = class$org$mortbay$util$ajax$JSON;
                }
                jSONPojoConvertor = new JSONPojoConvertor(Loader.loadClass(class$, name), this._fromJSON);
                try {
                    this._json.addConvertorFor(name, jSONPojoConvertor);
                } catch (ClassNotFoundException e2) {
                    e = e2;
                    e.printStackTrace();
                    if (jSONPojoConvertor != null) {
                        cls = obj.getClass();
                        if (class$java$lang$Object == null) {
                            class$ = class$("java.lang.Object");
                            class$java$lang$Object = class$;
                        } else {
                            class$ = class$java$lang$Object;
                        }
                        if (cls != class$) {
                            jSONPojoConvertor.toJSON(obj, output);
                            return;
                        }
                    }
                    output.add(obj.toString());
                }
            } catch (ClassNotFoundException e3) {
                e = e3;
                jSONPojoConvertor = convertorFor;
                e.printStackTrace();
                if (jSONPojoConvertor != null) {
                    cls = obj.getClass();
                    if (class$java$lang$Object == null) {
                        class$ = class$java$lang$Object;
                    } else {
                        class$ = class$("java.lang.Object");
                        class$java$lang$Object = class$;
                    }
                    if (cls != class$) {
                        jSONPojoConvertor.toJSON(obj, output);
                        return;
                    }
                }
                output.add(obj.toString());
            }
        }
        jSONPojoConvertor = convertorFor;
        if (jSONPojoConvertor != null) {
            cls = obj.getClass();
            if (class$java$lang$Object == null) {
                class$ = class$("java.lang.Object");
                class$java$lang$Object = class$;
            } else {
                class$ = class$java$lang$Object;
            }
            if (cls != class$) {
                jSONPojoConvertor.toJSON(obj, output);
                return;
            }
        }
        output.add(obj.toString());
    }
}
