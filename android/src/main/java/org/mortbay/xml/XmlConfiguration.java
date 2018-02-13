package org.mortbay.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.mortbay.component.LifeCycle;
import org.mortbay.jetty.HttpVersions;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.log.Log;
import org.mortbay.resource.Resource;
import org.mortbay.util.LazyList;
import org.mortbay.util.Loader;
import org.mortbay.util.TypeUtil;
import org.mortbay.xml.XmlParser.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlConfiguration {
    private static final Integer ZERO = new Integer(0);
    private static XmlParser __parser;
    private static Class[] __primitiveHolders;
    private static Class[] __primitives = new Class[]{Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Void.TYPE};
    static Class class$java$lang$Boolean;
    static Class class$java$lang$Byte;
    static Class class$java$lang$Character;
    static Class class$java$lang$Double;
    static Class class$java$lang$Float;
    static Class class$java$lang$Integer;
    static Class class$java$lang$Long;
    static Class class$java$lang$Object;
    static Class class$java$lang$Short;
    static Class class$java$lang$String;
    static Class class$java$lang$Void;
    static Class class$java$net$InetAddress;
    static Class class$java$net$URL;
    static Class class$org$mortbay$xml$XmlConfiguration;
    private Node _config;
    private Map _idMap = new HashMap();
    private Map _propertyMap = new HashMap();

    static {
        Class class$;
        Class class$2;
        Class class$3;
        Class class$4;
        Class class$5;
        Class class$6;
        Class class$7;
        Class class$8;
        Class class$9;
        if (class$java$lang$Boolean == null) {
            class$ = class$("java.lang.Boolean");
            class$java$lang$Boolean = class$;
        } else {
            class$ = class$java$lang$Boolean;
        }
        if (class$java$lang$Character == null) {
            class$2 = class$("java.lang.Character");
            class$java$lang$Character = class$2;
        } else {
            class$2 = class$java$lang$Character;
        }
        if (class$java$lang$Byte == null) {
            class$3 = class$("java.lang.Byte");
            class$java$lang$Byte = class$3;
        } else {
            class$3 = class$java$lang$Byte;
        }
        if (class$java$lang$Short == null) {
            class$4 = class$("java.lang.Short");
            class$java$lang$Short = class$4;
        } else {
            class$4 = class$java$lang$Short;
        }
        if (class$java$lang$Integer == null) {
            class$5 = class$("java.lang.Integer");
            class$java$lang$Integer = class$5;
        } else {
            class$5 = class$java$lang$Integer;
        }
        if (class$java$lang$Long == null) {
            class$6 = class$("java.lang.Long");
            class$java$lang$Long = class$6;
        } else {
            class$6 = class$java$lang$Long;
        }
        if (class$java$lang$Float == null) {
            class$7 = class$("java.lang.Float");
            class$java$lang$Float = class$7;
        } else {
            class$7 = class$java$lang$Float;
        }
        if (class$java$lang$Double == null) {
            class$8 = class$("java.lang.Double");
            class$java$lang$Double = class$8;
        } else {
            class$8 = class$java$lang$Double;
        }
        if (class$java$lang$Void == null) {
            class$9 = class$("java.lang.Void");
            class$java$lang$Void = class$9;
        } else {
            class$9 = class$java$lang$Void;
        }
        __primitiveHolders = new Class[]{class$, class$2, class$3, class$4, class$5, class$6, class$7, class$8, class$9};
    }

    public XmlConfiguration(InputStream inputStream) throws SAXException, IOException {
        initParser();
        InputSource inputSource = new InputSource(inputStream);
        synchronized (__parser) {
            this._config = __parser.parse(inputSource);
        }
    }

    public XmlConfiguration(String str) throws SAXException, IOException {
        initParser();
        InputSource inputSource = new InputSource(new StringReader(new StringBuffer().append("<?xml version=\"1.0\"  encoding=\"ISO-8859-1\"?>\n<!DOCTYPE Configure PUBLIC \"-//Mort Bay Consulting//DTD Configure 1.2//EN\" \"http://jetty.mortbay.org/configure_1_2.dtd\">").append(str).toString()));
        synchronized (__parser) {
            this._config = __parser.parse(inputSource);
        }
    }

    public XmlConfiguration(URL url) throws SAXException, IOException {
        initParser();
        synchronized (__parser) {
            this._config = __parser.parse(url.toString());
        }
    }

    private Object call(Object obj, Node node) throws Exception {
        Class cls;
        String attribute = node.getAttribute("id");
        Class nodeClass = nodeClass(node);
        if (nodeClass != null) {
            cls = nodeClass;
            obj = null;
        } else {
            cls = obj != null ? obj.getClass() : nodeClass;
        }
        if (cls == null) {
            throw new IllegalArgumentException(node.toString());
        }
        int i;
        int size = node.size();
        int i2 = 0;
        int i3 = 0;
        while (i3 < node.size()) {
            Object obj2 = node.get(i3);
            if (obj2 instanceof String) {
                i = i2;
            } else if (!((Node) obj2).getTag().equals("Arg")) {
                break;
            } else {
                i = i2 + 1;
            }
            i3++;
            i2 = i;
        }
        i3 = size;
        Object[] objArr = new Object[i2];
        int i4 = 0;
        int i5 = 0;
        while (i5 < i2) {
            obj2 = node.get(i4);
            if (obj2 instanceof String) {
                i = i5;
            } else {
                size = i5 + 1;
                objArr[i5] = value(obj, (Node) obj2);
                i = size;
            }
            i4++;
            i5 = i;
        }
        String attribute2 = node.getAttribute("name");
        if (Log.isDebugEnabled()) {
            Log.debug(new StringBuffer().append("XML call ").append(attribute2).toString());
        }
        Method[] methods = cls.getMethods();
        i5 = 0;
        while (methods != null && i5 < methods.length) {
            if (methods[i5].getName().equals(attribute2) && methods[i5].getParameterTypes().length == i2) {
                if (Modifier.isStatic(methods[i5].getModifiers()) == (obj == null) && (obj != null || methods[i5].getDeclaringClass() == cls)) {
                    Object invoke;
                    try {
                        invoke = methods[i5].invoke(obj, objArr);
                        obj2 = 1;
                    } catch (Throwable e) {
                        Log.ignore(e);
                        obj2 = null;
                        invoke = null;
                    } catch (Throwable e2) {
                        Log.ignore(e2);
                        obj2 = null;
                        invoke = null;
                    }
                    if (obj2 != null) {
                        if (attribute != null) {
                            this._idMap.put(attribute, invoke);
                        }
                        configure(invoke, node, i3);
                        return invoke;
                    }
                }
            }
            i5++;
        }
        throw new IllegalStateException(new StringBuffer().append("No Method: ").append(node).append(" on ").append(cls).toString());
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    private void configure(Object obj, Node node, int i) throws Exception {
        String attribute = node.getAttribute("id");
        if (attribute != null) {
            this._idMap.put(attribute, obj);
        }
        while (i < node.size()) {
            Object obj2 = node.get(i);
            if (!(obj2 instanceof String)) {
                Node node2 = (Node) obj2;
                try {
                    String tag = node2.getTag();
                    if ("Set".equals(tag)) {
                        set(obj, node2);
                    } else if ("Put".equals(tag)) {
                        put(obj, node2);
                    } else if ("Call".equals(tag)) {
                        call(obj, node2);
                    } else if ("Get".equals(tag)) {
                        get(obj, node2);
                    } else if ("New".equals(tag)) {
                        newObj(obj, node2);
                    } else if ("Array".equals(tag)) {
                        newArray(obj, node2);
                    } else if ("Ref".equals(tag)) {
                        refObj(obj, node2);
                    } else if ("Property".equals(tag)) {
                        propertyObj(obj, node2);
                    } else {
                        throw new IllegalStateException(new StringBuffer().append("Unknown tag: ").append(tag).toString());
                    }
                } catch (Exception e) {
                    Log.warn(new StringBuffer().append("Config error at ").append(node2).toString(), e.toString());
                    throw e;
                }
            }
            i++;
        }
    }

    private Object get(Object obj, Node node) throws Exception {
        Class cls;
        Class nodeClass = nodeClass(node);
        if (nodeClass != null) {
            obj = null;
            cls = nodeClass;
        } else {
            cls = obj.getClass();
        }
        String attribute = node.getAttribute("name");
        String attribute2 = node.getAttribute("id");
        if (Log.isDebugEnabled()) {
            Log.debug(new StringBuffer().append("XML get ").append(attribute).toString());
        }
        try {
            obj = cls.getMethod(new StringBuffer().append("get").append(attribute.substring(0, 1).toUpperCase()).append(attribute.substring(1)).toString(), (Class[]) null).invoke(obj, (Object[]) null);
            configure(obj, node, 0);
        } catch (NoSuchMethodException e) {
            try {
                obj = cls.getField(attribute).get(obj);
                configure(obj, node, 0);
            } catch (NoSuchFieldException e2) {
                throw e;
            }
        }
        if (attribute2 != null) {
            this._idMap.put(attribute2, obj);
        }
        return obj;
    }

    private static void initParser() throws IOException {
        synchronized (XmlConfiguration.class) {
            try {
                if (__parser == null) {
                    Class class$;
                    __parser = new XmlParser();
                    if (class$org$mortbay$xml$XmlConfiguration == null) {
                        class$ = class$("org.mortbay.xml.XmlConfiguration");
                        class$org$mortbay$xml$XmlConfiguration = class$;
                    } else {
                        class$ = class$org$mortbay$xml$XmlConfiguration;
                    }
                    URL resource = Loader.getResource(class$, "org/mortbay/xml/configure_6_0.dtd", true);
                    __parser.redirectEntity("configure.dtd", resource);
                    __parser.redirectEntity("configure_1_3.dtd", resource);
                    __parser.redirectEntity("http://jetty.mortbay.org/configure.dtd", resource);
                    __parser.redirectEntity("-//Mort Bay Consulting//DTD Configure//EN", resource);
                    __parser.redirectEntity("http://jetty.mortbay.org/configure_1_3.dtd", resource);
                    __parser.redirectEntity("-//Mort Bay Consulting//DTD Configure 1.3//EN", resource);
                    __parser.redirectEntity("configure_1_2.dtd", resource);
                    __parser.redirectEntity("http://jetty.mortbay.org/configure_1_2.dtd", resource);
                    __parser.redirectEntity("-//Mort Bay Consulting//DTD Configure 1.2//EN", resource);
                    __parser.redirectEntity("configure_1_1.dtd", resource);
                    __parser.redirectEntity("http://jetty.mortbay.org/configure_1_1.dtd", resource);
                    __parser.redirectEntity("-//Mort Bay Consulting//DTD Configure 1.1//EN", resource);
                    __parser.redirectEntity("configure_1_0.dtd", resource);
                    __parser.redirectEntity("http://jetty.mortbay.org/configure_1_0.dtd", resource);
                    __parser.redirectEntity("-//Mort Bay Consulting//DTD Configure 1.0//EN", resource);
                }
            } catch (Throwable e) {
                Log.warn(e.toString());
                Log.debug(e);
            } catch (Throwable th) {
                Class cls = XmlConfiguration.class;
            }
        }
    }

    private Object itemValue(Object obj, Object obj2) throws Exception {
        if (obj2 instanceof String) {
            return obj2;
        }
        Node node = (Node) obj2;
        String tag = node.getTag();
        if ("Call".equals(tag)) {
            return call(obj, node);
        }
        if ("Get".equals(tag)) {
            return get(obj, node);
        }
        if ("New".equals(tag)) {
            return newObj(obj, node);
        }
        if ("Ref".equals(tag)) {
            return refObj(obj, node);
        }
        if ("Array".equals(tag)) {
            return newArray(obj, node);
        }
        if ("Map".equals(tag)) {
            return newMap(obj, node);
        }
        if ("Property".equals(tag)) {
            return propertyObj(obj, node);
        }
        if ("SystemProperty".equals(tag)) {
            return System.getProperty(node.getAttribute("name"), node.getAttribute(ServletHandler.__DEFAULT_SERVLET));
        }
        Log.warn(new StringBuffer().append("Unknown value tag: ").append(node).toString(), new Throwable());
        return null;
    }

    public static void main(String[] strArr) {
        try {
            Map properties = new Properties();
            XmlConfiguration xmlConfiguration = null;
            Object[] objArr = new Object[strArr.length];
            for (int i = 0; i < strArr.length; i++) {
                if (strArr[i].toLowerCase().endsWith(".properties")) {
                    properties.load(Resource.newResource(strArr[i]).getInputStream());
                } else {
                    XmlConfiguration xmlConfiguration2 = new XmlConfiguration(Resource.newResource(strArr[i]).getURL());
                    if (xmlConfiguration != null) {
                        xmlConfiguration2.getIdMap().putAll(xmlConfiguration.getIdMap());
                    }
                    if (properties.size() > 0) {
                        xmlConfiguration2.setProperties(properties);
                    }
                    objArr[i] = xmlConfiguration2.configure();
                    xmlConfiguration = xmlConfiguration2;
                }
            }
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if (objArr[i2] instanceof LifeCycle) {
                    LifeCycle lifeCycle = (LifeCycle) objArr[i2];
                    if (!lifeCycle.isRunning()) {
                        lifeCycle.start();
                    }
                }
            }
        } catch (Throwable e) {
            Log.warn(Log.EXCEPTION, e);
        }
    }

    private Object newArray(Object obj, Node node) throws Exception {
        Class class$;
        Class cls;
        Iterator it;
        Object obj2;
        String attribute;
        Object value;
        Object obj3;
        if (class$java$lang$Object == null) {
            class$ = class$("java.lang.Object");
            class$java$lang$Object = class$;
        } else {
            class$ = class$java$lang$Object;
        }
        String attribute2 = node.getAttribute("type");
        String attribute3 = node.getAttribute("id");
        if (attribute2 != null) {
            class$ = TypeUtil.fromName(attribute2);
            if (class$ == null) {
                if ("String".equals(attribute2)) {
                    if (class$java$lang$String == null) {
                        class$ = class$("java.lang.String");
                        class$java$lang$String = class$;
                    } else {
                        cls = class$java$lang$String;
                    }
                } else if ("URL".equals(attribute2)) {
                    if (class$java$net$URL == null) {
                        class$ = class$("java.net.URL");
                        class$java$net$URL = class$;
                    } else {
                        class$ = class$java$net$URL;
                    }
                    cls = class$;
                } else if ("InetAddress".equals(attribute2)) {
                    if (class$java$net$InetAddress == null) {
                        class$ = class$("java.net.InetAddress");
                        class$java$net$InetAddress = class$;
                    } else {
                        class$ = class$java$net$InetAddress;
                    }
                    cls = class$;
                } else {
                    if (class$org$mortbay$xml$XmlConfiguration == null) {
                        class$ = class$("org.mortbay.xml.XmlConfiguration");
                        class$org$mortbay$xml$XmlConfiguration = class$;
                    } else {
                        class$ = class$org$mortbay$xml$XmlConfiguration;
                    }
                    cls = Loader.loadClass(class$, attribute2, true);
                }
                it = node.iterator("Item");
                obj2 = null;
                while (it.hasNext()) {
                    Node node2 = (Node) it.next();
                    attribute = node2.getAttribute("id");
                    value = value(obj, node2);
                    obj3 = (value == null || !cls.isPrimitive()) ? value : ZERO;
                    obj3 = LazyList.add(obj2, obj3);
                    if (attribute == null) {
                        this._idMap.put(attribute, value);
                        obj2 = obj3;
                    } else {
                        obj2 = obj3;
                    }
                }
                obj3 = LazyList.toArray(obj2, cls);
                if (attribute3 != null) {
                    this._idMap.put(attribute3, obj3);
                }
                return obj3;
            }
        }
        cls = class$;
        it = node.iterator("Item");
        obj2 = null;
        while (it.hasNext()) {
            Node node22 = (Node) it.next();
            attribute = node22.getAttribute("id");
            value = value(obj, node22);
            if (value == null) {
            }
            obj3 = LazyList.add(obj2, obj3);
            if (attribute == null) {
                obj2 = obj3;
            } else {
                this._idMap.put(attribute, value);
                obj2 = obj3;
            }
        }
        obj3 = LazyList.toArray(obj2, cls);
        if (attribute3 != null) {
            this._idMap.put(attribute3, obj3);
        }
        return obj3;
    }

    private Object newMap(Object obj, Node node) throws Exception {
        String attribute = node.getAttribute("id");
        Map hashMap = new HashMap();
        if (attribute != null) {
            this._idMap.put(attribute, hashMap);
        }
        for (int i = 0; i < node.size(); i++) {
            Object obj2 = node.get(i);
            if (!(obj2 instanceof String)) {
                Node node2 = (Node) obj2;
                if (node2.getTag().equals("Entry")) {
                    Node node3 = null;
                    Node node4 = null;
                    int i2 = 0;
                    while (i2 < node2.size()) {
                        Node node5;
                        Object obj3 = node2.get(i2);
                        if (obj3 instanceof String) {
                            node5 = node3;
                            node3 = node4;
                        } else {
                            node5 = (Node) obj3;
                            if (!node5.getTag().equals("Item")) {
                                throw new IllegalStateException("Not an Item");
                            } else if (node3 == null) {
                                node3 = node4;
                            } else {
                                Node node6 = node3;
                                node3 = node5;
                                node5 = node6;
                            }
                        }
                        i2++;
                        node4 = node3;
                        node3 = node5;
                    }
                    if (node3 == null || node4 == null) {
                        throw new IllegalStateException("Missing Item in Entry");
                    }
                    attribute = node3.getAttribute("id");
                    String attribute2 = node4.getAttribute("id");
                    Object value = value(obj, node3);
                    Object value2 = value(obj, node4);
                    hashMap.put(value, value2);
                    if (attribute != null) {
                        this._idMap.put(attribute, value);
                    }
                    if (attribute2 != null) {
                        this._idMap.put(attribute2, value2);
                    }
                } else {
                    throw new IllegalStateException("Not an Entry");
                }
            }
        }
        return hashMap;
    }

    private Object newObj(Object obj, Node node) throws Exception {
        int i;
        Class nodeClass = nodeClass(node);
        String attribute = node.getAttribute("id");
        int size = node.size();
        int i2 = 0;
        int i3 = 0;
        while (i3 < node.size()) {
            Object obj2 = node.get(i3);
            if (obj2 instanceof String) {
                i = i2;
            } else if (!((Node) obj2).getTag().equals("Arg")) {
                break;
            } else {
                i = i2 + 1;
            }
            i3++;
            i2 = i;
        }
        i3 = size;
        Object[] objArr = new Object[i2];
        int i4 = 0;
        int i5 = 0;
        while (i5 < i2) {
            obj2 = node.get(i4);
            if (obj2 instanceof String) {
                i = i5;
            } else {
                size = i5 + 1;
                objArr[i5] = value(obj, (Node) obj2);
                i = size;
            }
            i4++;
            i5 = i;
        }
        if (Log.isDebugEnabled()) {
            Log.debug(new StringBuffer().append("XML new ").append(nodeClass).toString());
        }
        Constructor[] constructors = nodeClass.getConstructors();
        size = 0;
        while (constructors != null && size < constructors.length) {
            if (constructors[size].getParameterTypes().length == i2) {
                Object obj3;
                try {
                    obj2 = constructors[size].newInstance(objArr);
                    obj3 = 1;
                } catch (Throwable e) {
                    Log.ignore(e);
                    obj2 = null;
                    obj3 = null;
                } catch (Throwable e2) {
                    Log.ignore(e2);
                    obj2 = null;
                    obj3 = null;
                } catch (Throwable e22) {
                    Log.ignore(e22);
                    obj2 = null;
                    obj3 = null;
                }
                if (obj3 != null) {
                    if (attribute != null) {
                        this._idMap.put(attribute, obj2);
                    }
                    configure(obj2, node, i3);
                    return obj2;
                }
            }
            size++;
        }
        throw new IllegalStateException(new StringBuffer().append("No Constructor: ").append(node).append(" on ").append(obj).toString());
    }

    private Class nodeClass(Node node) throws ClassNotFoundException {
        String attribute = node.getAttribute("class");
        if (attribute == null) {
            return null;
        }
        Class class$;
        if (class$org$mortbay$xml$XmlConfiguration == null) {
            class$ = class$("org.mortbay.xml.XmlConfiguration");
            class$org$mortbay$xml$XmlConfiguration = class$;
        } else {
            class$ = class$org$mortbay$xml$XmlConfiguration;
        }
        return Loader.loadClass(class$, attribute, true);
    }

    private Object propertyObj(Object obj, Node node) throws Exception {
        String attribute = node.getAttribute("id");
        String attribute2 = node.getAttribute("name");
        Object attribute3 = node.getAttribute(ServletHandler.__DEFAULT_SERVLET);
        if (this._propertyMap != null && this._propertyMap.containsKey(attribute2)) {
            attribute3 = this._propertyMap.get(attribute2);
        } else if (attribute3 == null) {
            attribute3 = null;
        }
        if (attribute != null) {
            this._idMap.put(attribute, attribute3);
        }
        if (attribute3 != null) {
            configure(attribute3, node, 0);
        }
        return attribute3;
    }

    private void put(Object obj, Node node) throws Exception {
        if (obj instanceof Map) {
            Map map = (Map) obj;
            String attribute = node.getAttribute("name");
            Object value = value(obj, node);
            map.put(attribute, value);
            if (Log.isDebugEnabled()) {
                Log.debug(new StringBuffer().append("XML ").append(obj).append(".put(").append(attribute).append(",").append(value).append(")").toString());
                return;
            }
            return;
        }
        throw new IllegalArgumentException(new StringBuffer().append("Object for put is not a Map: ").append(obj).toString());
    }

    private Object refObj(Object obj, Node node) throws Exception {
        String attribute = node.getAttribute("id");
        Object obj2 = this._idMap.get(attribute);
        if (obj2 == null) {
            throw new IllegalStateException(new StringBuffer().append("No object for id=").append(attribute).toString());
        }
        configure(obj2, node, 0);
        return obj2;
    }

    private void set(Object obj, Node node) throws Exception {
        Class cls;
        Field field;
        Method[] methods;
        Method method;
        int i;
        Class cls2;
        int i2;
        Class cls3;
        String attribute = node.getAttribute("name");
        String stringBuffer = new StringBuffer().append("set").append(attribute.substring(0, 1).toUpperCase()).append(attribute.substring(1)).toString();
        Object value = value(obj, node);
        Object[] objArr = new Object[]{value};
        Class nodeClass = nodeClass(node);
        if (nodeClass != null) {
            cls = nodeClass;
            obj = null;
        } else {
            cls = obj.getClass();
        }
        Class[] clsArr = new Class[1];
        if (class$java$lang$Object == null) {
            nodeClass = class$("java.lang.Object");
            class$java$lang$Object = nodeClass;
        } else {
            nodeClass = class$java$lang$Object;
        }
        clsArr[0] = nodeClass;
        if (value != null) {
            clsArr[0] = value.getClass();
        }
        if (Log.isDebugEnabled()) {
            Log.debug(new StringBuffer().append("XML ").append(obj != null ? obj.toString() : cls.getName()).append(".").append(stringBuffer).append("(").append(value).append(")").toString());
        }
        try {
            cls.getMethod(stringBuffer, clsArr).invoke(obj, objArr);
            return;
        } catch (Throwable e) {
            Log.ignore(e);
        } catch (Throwable e2) {
            Log.ignore(e2);
        } catch (Throwable e22) {
            Log.ignore(e22);
        }
        try {
            clsArr[0] = (Class) clsArr[0].getField("TYPE").get(null);
            cls.getMethod(stringBuffer, clsArr).invoke(obj, objArr);
        } catch (Throwable e222) {
            Log.ignore(e222);
            try {
                field = cls.getField(attribute);
                if (Modifier.isPublic(field.getModifiers())) {
                    field.set(obj, value);
                    return;
                }
            } catch (Throwable e2222) {
                Log.ignore(e2222);
            }
            methods = cls.getMethods();
            method = null;
            i = 0;
            while (methods != null && i < methods.length) {
                if (stringBuffer.equals(methods[i].getName()) && methods[i].getParameterTypes().length == 1) {
                    method = methods[i];
                    try {
                        methods[i].invoke(obj, objArr);
                        return;
                    } catch (Throwable e3) {
                        Log.ignore(e3);
                    } catch (Throwable e32) {
                        Log.ignore(e32);
                    }
                }
                i++;
            }
            if (method != null) {
                try {
                    cls2 = method.getParameterTypes()[0];
                    if (cls2.isPrimitive()) {
                        for (i2 = 0; i2 < __primitives.length; i2++) {
                            if (!cls2.equals(__primitives[i2])) {
                                cls3 = __primitiveHolders[i2];
                                break;
                            }
                        }
                    }
                    cls3 = cls2;
                    objArr[0] = cls3.getConstructor(clsArr).newInstance(objArr);
                    method.invoke(obj, objArr);
                    return;
                } catch (Throwable e22222) {
                    Log.ignore(e22222);
                    throw new NoSuchMethodException(new StringBuffer().append(cls).append(".").append(stringBuffer).append("(").append(clsArr[0]).append(")").toString());
                } catch (Throwable e222222) {
                    Log.ignore(e222222);
                    throw new NoSuchMethodException(new StringBuffer().append(cls).append(".").append(stringBuffer).append("(").append(clsArr[0]).append(")").toString());
                } catch (Throwable e2222222) {
                    Log.ignore(e2222222);
                    throw new NoSuchMethodException(new StringBuffer().append(cls).append(".").append(stringBuffer).append("(").append(clsArr[0]).append(")").toString());
                }
            }
            throw new NoSuchMethodException(new StringBuffer().append(cls).append(".").append(stringBuffer).append("(").append(clsArr[0]).append(")").toString());
        } catch (Throwable e22222222) {
            Log.ignore(e22222222);
            field = cls.getField(attribute);
            if (Modifier.isPublic(field.getModifiers())) {
                field.set(obj, value);
                return;
            }
            methods = cls.getMethods();
            method = null;
            i = 0;
            while (methods != null) {
                method = methods[i];
                methods[i].invoke(obj, objArr);
                return;
            }
            if (method != null) {
                cls2 = method.getParameterTypes()[0];
                if (cls2.isPrimitive()) {
                    for (i2 = 0; i2 < __primitives.length; i2++) {
                        if (!cls2.equals(__primitives[i2])) {
                            cls3 = __primitiveHolders[i2];
                            break;
                        }
                    }
                }
                cls3 = cls2;
                objArr[0] = cls3.getConstructor(clsArr).newInstance(objArr);
                method.invoke(obj, objArr);
                return;
            }
            throw new NoSuchMethodException(new StringBuffer().append(cls).append(".").append(stringBuffer).append("(").append(clsArr[0]).append(")").toString());
        } catch (Throwable e222222222) {
            Log.ignore(e222222222);
            field = cls.getField(attribute);
            if (Modifier.isPublic(field.getModifiers())) {
                field.set(obj, value);
                return;
            }
            methods = cls.getMethods();
            method = null;
            i = 0;
            while (methods != null) {
                method = methods[i];
                methods[i].invoke(obj, objArr);
                return;
            }
            if (method != null) {
                cls2 = method.getParameterTypes()[0];
                if (cls2.isPrimitive()) {
                    for (i2 = 0; i2 < __primitives.length; i2++) {
                        if (!cls2.equals(__primitives[i2])) {
                            cls3 = __primitiveHolders[i2];
                            break;
                        }
                    }
                }
                cls3 = cls2;
                objArr[0] = cls3.getConstructor(clsArr).newInstance(objArr);
                method.invoke(obj, objArr);
                return;
            }
            throw new NoSuchMethodException(new StringBuffer().append(cls).append(".").append(stringBuffer).append("(").append(clsArr[0]).append(")").toString());
        } catch (Throwable e2222222222) {
            Log.ignore(e2222222222);
            field = cls.getField(attribute);
            if (Modifier.isPublic(field.getModifiers())) {
                field.set(obj, value);
                return;
            }
            methods = cls.getMethods();
            method = null;
            i = 0;
            while (methods != null) {
                method = methods[i];
                methods[i].invoke(obj, objArr);
                return;
            }
            if (method != null) {
                cls2 = method.getParameterTypes()[0];
                if (cls2.isPrimitive()) {
                    for (i2 = 0; i2 < __primitives.length; i2++) {
                        if (!cls2.equals(__primitives[i2])) {
                            cls3 = __primitiveHolders[i2];
                            break;
                        }
                    }
                }
                cls3 = cls2;
                objArr[0] = cls3.getConstructor(clsArr).newInstance(objArr);
                method.invoke(obj, objArr);
                return;
            }
            throw new NoSuchMethodException(new StringBuffer().append(cls).append(".").append(stringBuffer).append("(").append(clsArr[0]).append(")").toString());
        }
    }

    private Object value(Object obj, Node node) throws Exception {
        Object obj2;
        int i = 0;
        String attribute = node.getAttribute("type");
        String attribute2 = node.getAttribute("ref");
        if (attribute2 != null) {
            obj2 = this._idMap.get(attribute2);
        } else if (node.size() == 0) {
            return "String".equals(attribute) ? HttpVersions.HTTP_0_9 : null;
        } else {
            int size = node.size() - 1;
            if (attribute == null || !"String".equals(attribute)) {
                int i2 = 0;
                while (i2 <= size) {
                    obj2 = node.get(i2);
                    if (!(obj2 instanceof String) || ((String) obj2).trim().length() > 0) {
                        break;
                    }
                    i2++;
                }
                while (i2 < size) {
                    obj2 = node.get(size);
                    if (!(obj2 instanceof String) || ((String) obj2).trim().length() > 0) {
                        break;
                    }
                    size--;
                }
                if (i2 > size) {
                    return null;
                }
                i = i2;
            }
            if (i == size) {
                obj2 = itemValue(obj, node.get(i));
            } else {
                StringBuffer stringBuffer = new StringBuffer();
                synchronized (stringBuffer) {
                    while (i <= size) {
                        stringBuffer.append(itemValue(obj, node.get(i)));
                        i++;
                    }
                    obj2 = stringBuffer.toString();
                }
            }
        }
        if (obj2 == null) {
            return "String".equals(attribute) ? HttpVersions.HTTP_0_9 : null;
        } else {
            if (attribute == null) {
                return (obj2 == null || !(obj2 instanceof String)) ? obj2 : ((String) obj2).trim();
            } else {
                if ("String".equals(attribute) || "java.lang.String".equals(attribute)) {
                    return obj2.toString();
                }
                Class fromName = TypeUtil.fromName(attribute);
                if (fromName != null) {
                    return TypeUtil.valueOf(fromName, obj2.toString());
                }
                if ("URL".equals(attribute) || "java.net.URL".equals(attribute)) {
                    if (obj2 instanceof URL) {
                        return obj2;
                    }
                    try {
                        return new URL(obj2.toString());
                    } catch (Throwable e) {
                        throw new InvocationTargetException(e);
                    }
                } else if (!"InetAddress".equals(attribute) && !"java.net.InetAddress".equals(attribute)) {
                    throw new IllegalStateException(new StringBuffer().append("Unknown type ").append(attribute).toString());
                } else if (obj2 instanceof InetAddress) {
                    return obj2;
                } else {
                    try {
                        return InetAddress.getByName(obj2.toString());
                    } catch (Throwable e2) {
                        throw new InvocationTargetException(e2);
                    }
                }
            }
        }
    }

    public Object configure() throws Exception {
        Class nodeClass = nodeClass(this._config);
        String attribute = this._config.getAttribute("id");
        Object obj = attribute == null ? null : this._idMap.get(attribute);
        if (obj == null && nodeClass != null) {
            obj = nodeClass.newInstance();
        }
        if (nodeClass == null || nodeClass.isInstance(obj)) {
            configure(obj, this._config, 0);
            return obj;
        }
        throw new ClassCastException(nodeClass.toString());
    }

    public void configure(Object obj) throws Exception {
        Class nodeClass = nodeClass(this._config);
        if (nodeClass.isInstance(obj)) {
            configure(obj, this._config, 0);
            return;
        }
        throw new IllegalArgumentException(new StringBuffer().append("Object is not of type ").append(nodeClass).toString());
    }

    public Map getIdMap() {
        return this._idMap;
    }

    public Map getProperties() {
        return this._propertyMap;
    }

    public void setIdMap(Map map) {
        this._idMap = map;
    }

    public void setProperties(Map map) {
        this._propertyMap = map;
    }
}
