package org.mortbay.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.StringTokenizer;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.mortbay.jetty.HttpVersions;
import org.mortbay.log.Log;
import org.mortbay.util.LazyList;
import org.mortbay.util.URIUtil;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class XmlParser {
    private String _dtd;
    private Map _observerMap;
    private Stack _observers = new Stack();
    private SAXParser _parser;
    private Map _redirectMap = new HashMap();
    private String _xpath;
    private Object _xpaths;

    public static class Attribute {
        private String _name;
        private String _value;

        Attribute(String str, String str2) {
            this._name = str;
            this._value = str2;
        }

        public String getName() {
            return this._name;
        }

        public String getValue() {
            return this._value;
        }
    }

    private class Handler extends DefaultHandler {
        private Node _context = this._top;
        SAXParseException _error;
        private NoopHandler _noop;
        Node _top = new Node(null, null, null);
        private final XmlParser this$0;

        Handler(XmlParser xmlParser) {
            this.this$0 = xmlParser;
            this._noop = new NoopHandler(xmlParser, this);
        }

        private String getLocationString(SAXParseException sAXParseException) {
            return new StringBuffer().append(sAXParseException.getSystemId()).append(" line:").append(sAXParseException.getLineNumber()).append(" col:").append(sAXParseException.getColumnNumber()).toString();
        }

        public void characters(char[] cArr, int i, int i2) throws SAXException {
            this._context.add(new String(cArr, i, i2));
            for (int i3 = 0; i3 < XmlParser.access$300(this.this$0).size(); i3++) {
                if (XmlParser.access$300(this.this$0).get(i3) != null) {
                    ((ContentHandler) XmlParser.access$300(this.this$0).get(i3)).characters(cArr, i, i2);
                }
            }
        }

        void clear() {
            this._top = null;
            this._error = null;
            this._context = null;
        }

        public void endElement(String str, String str2, String str3) throws SAXException {
            this._context = this._context._parent;
            for (int i = 0; i < XmlParser.access$300(this.this$0).size(); i++) {
                if (XmlParser.access$300(this.this$0).get(i) != null) {
                    ((ContentHandler) XmlParser.access$300(this.this$0).get(i)).endElement(str, str2, str3);
                }
            }
            XmlParser.access$300(this.this$0).pop();
        }

        public void error(SAXParseException sAXParseException) throws SAXException {
            if (this._error == null) {
                this._error = sAXParseException;
            }
            Log.debug(Log.EXCEPTION, sAXParseException);
            Log.warn(new StringBuffer().append("ERROR@").append(getLocationString(sAXParseException)).append(" : ").append(sAXParseException.toString()).toString());
        }

        public void fatalError(SAXParseException sAXParseException) throws SAXException {
            this._error = sAXParseException;
            Log.debug(Log.EXCEPTION, sAXParseException);
            Log.warn(new StringBuffer().append("FATAL@").append(getLocationString(sAXParseException)).append(" : ").append(sAXParseException.toString()).toString());
            throw sAXParseException;
        }

        public void ignorableWhitespace(char[] cArr, int i, int i2) throws SAXException {
            for (int i3 = 0; i3 < XmlParser.access$300(this.this$0).size(); i3++) {
                if (XmlParser.access$300(this.this$0).get(i3) != null) {
                    ((ContentHandler) XmlParser.access$300(this.this$0).get(i3)).ignorableWhitespace(cArr, i, i2);
                }
            }
        }

        public InputSource resolveEntity(String str, String str2) {
            if (Log.isDebugEnabled()) {
                Log.debug(new StringBuffer().append("resolveEntity(").append(str).append(", ").append(str2).append(")").toString());
            }
            if (str2 != null && str2.endsWith(".dtd")) {
                XmlParser.access$402(this.this$0, str2);
            }
            URL url = str != null ? (URL) XmlParser.access$500(this.this$0).get(str) : null;
            if (url == null) {
                url = (URL) XmlParser.access$500(this.this$0).get(str2);
            }
            if (url == null) {
                String substring = str2.lastIndexOf(47) >= 0 ? str2.substring(str2.lastIndexOf(47) + 1) : str2;
                if (Log.isDebugEnabled()) {
                    Log.debug(new StringBuffer().append("Can't exact match entity in redirect map, trying ").append(substring).toString());
                }
                url = (URL) XmlParser.access$500(this.this$0).get(substring);
            }
            if (url != null) {
                try {
                    InputStream openStream = url.openStream();
                    if (Log.isDebugEnabled()) {
                        Log.debug(new StringBuffer().append("Redirected entity ").append(str2).append(" --> ").append(url).toString());
                    }
                    InputSource inputSource = new InputSource(openStream);
                    inputSource.setSystemId(str2);
                    return inputSource;
                } catch (Throwable e) {
                    Log.ignore(e);
                }
            }
            return null;
        }

        public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
            int i = 0;
            String str4 = (str == null || str.equals(HttpVersions.HTTP_0_9)) ? str3 : str2;
            Node node = new Node(this._context, str4, attributes);
            if (XmlParser.access$100(this.this$0) != null) {
                String path = node.getPath();
                int size = LazyList.size(XmlParser.access$100(this.this$0));
                int i2 = 0;
                while (i2 == 0) {
                    int i3 = size - 1;
                    if (size <= 0) {
                        break;
                    }
                    String str5 = (String) LazyList.get(XmlParser.access$100(this.this$0), i3);
                    size = (path.equals(str5) || (str5.startsWith(path) && str5.length() > path.length() && str5.charAt(path.length()) == '/')) ? 1 : 0;
                    i2 = size;
                    size = i3;
                }
                if (i2 != 0) {
                    this._context.add(node);
                    this._context = node;
                } else {
                    XmlParser.access$000(this.this$0).getXMLReader().setContentHandler(this._noop);
                }
            } else {
                this._context.add(node);
                this._context = node;
            }
            Object obj = null;
            if (XmlParser.access$200(this.this$0) != null) {
                obj = (ContentHandler) XmlParser.access$200(this.this$0).get(str4);
            }
            XmlParser.access$300(this.this$0).push(obj);
            while (i < XmlParser.access$300(this.this$0).size()) {
                if (XmlParser.access$300(this.this$0).get(i) != null) {
                    ((ContentHandler) XmlParser.access$300(this.this$0).get(i)).startElement(str, str2, str3, attributes);
                }
                i++;
            }
        }

        public void warning(SAXParseException sAXParseException) {
            Log.debug(Log.EXCEPTION, sAXParseException);
            Log.warn(new StringBuffer().append("WARNING@").append(getLocationString(sAXParseException)).append(" : ").append(sAXParseException.toString()).toString());
        }
    }

    public static class Node extends AbstractList {
        private Attribute[] _attrs;
        private boolean _lastString = false;
        private ArrayList _list;
        Node _parent;
        private String _path;
        private String _tag;

        class C13611 implements Iterator {
            Node _node;
            int f372c = 0;
            private final Node this$0;
            private final String val$tag;

            C13611(Node node, String str) {
                this.this$0 = node;
                this.val$tag = str;
            }

            public boolean hasNext() {
                if (this._node != null) {
                    return true;
                }
                while (Node.access$600(this.this$0) != null && this.f372c < Node.access$600(this.this$0).size()) {
                    Object obj = Node.access$600(this.this$0).get(this.f372c);
                    if (obj instanceof Node) {
                        Node node = (Node) obj;
                        if (this.val$tag.equals(Node.access$700(node))) {
                            this._node = node;
                            return true;
                        }
                    }
                    this.f372c++;
                }
                return false;
            }

            public Object next() {
                try {
                    if (hasNext()) {
                        Object obj = this._node;
                        return obj;
                    }
                    throw new NoSuchElementException();
                } finally {
                    this._node = null;
                    this.f372c++;
                }
            }

            public void remove() {
                throw new UnsupportedOperationException("Not supported");
            }
        }

        Node(Node node, String str, Attributes attributes) {
            this._parent = node;
            this._tag = str;
            if (attributes != null) {
                this._attrs = new Attribute[attributes.getLength()];
                for (int i = 0; i < attributes.getLength(); i++) {
                    String localName = attributes.getLocalName(i);
                    if (localName == null || localName.equals(HttpVersions.HTTP_0_9)) {
                        localName = attributes.getQName(i);
                    }
                    this._attrs[i] = new Attribute(localName, attributes.getValue(i));
                }
            }
        }

        static ArrayList access$600(Node node) {
            return node._list;
        }

        static String access$700(Node node) {
            return node._tag;
        }

        private void toString(StringBuffer stringBuffer, boolean z) {
            synchronized (this) {
                int i;
                if (z) {
                    stringBuffer.append("<");
                    stringBuffer.append(this._tag);
                    if (this._attrs != null) {
                        for (i = 0; i < this._attrs.length; i++) {
                            stringBuffer.append(' ');
                            stringBuffer.append(this._attrs[i].getName());
                            stringBuffer.append("=\"");
                            stringBuffer.append(this._attrs[i].getValue());
                            stringBuffer.append("\"");
                        }
                    }
                }
                if (this._list != null) {
                    if (z) {
                        stringBuffer.append(">");
                    }
                    for (i = 0; i < this._list.size(); i++) {
                        Object obj = this._list.get(i);
                        if (obj != null) {
                            if (obj instanceof Node) {
                                ((Node) obj).toString(stringBuffer, z);
                            } else {
                                stringBuffer.append(obj.toString());
                            }
                        }
                    }
                    if (z) {
                        stringBuffer.append("</");
                        stringBuffer.append(this._tag);
                        stringBuffer.append(">");
                    }
                } else if (z) {
                    stringBuffer.append("/>");
                }
            }
        }

        public void add(int i, Object obj) {
            if (this._list == null) {
                this._list = new ArrayList();
            }
            if (obj instanceof String) {
                if (this._lastString) {
                    int size = this._list.size() - 1;
                    this._list.set(size, new StringBuffer().append((String) this._list.get(size)).append(obj).toString());
                } else {
                    this._list.add(i, obj);
                }
                this._lastString = true;
                return;
            }
            this._lastString = false;
            this._list.add(i, obj);
        }

        public void clear() {
            if (this._list != null) {
                this._list.clear();
            }
            this._list = null;
        }

        public Object get(int i) {
            return this._list != null ? this._list.get(i) : null;
        }

        public Node get(String str) {
            if (this._list != null) {
                for (int i = 0; i < this._list.size(); i++) {
                    Object obj = this._list.get(i);
                    if (obj instanceof Node) {
                        Node node = (Node) obj;
                        if (str.equals(node._tag)) {
                            return node;
                        }
                    }
                }
            }
            return null;
        }

        public String getAttribute(String str) {
            return getAttribute(str, null);
        }

        public String getAttribute(String str, String str2) {
            if (this._attrs == null || str == null) {
                return str2;
            }
            for (int i = 0; i < this._attrs.length; i++) {
                if (str.equals(this._attrs[i].getName())) {
                    return this._attrs[i].getValue();
                }
            }
            return str2;
        }

        public Attribute[] getAttributes() {
            return this._attrs;
        }

        public Node getParent() {
            return this._parent;
        }

        public String getPath() {
            if (this._path == null) {
                if (getParent() == null || getParent().getTag() == null) {
                    this._path = new StringBuffer().append(URIUtil.SLASH).append(this._tag).toString();
                } else {
                    this._path = new StringBuffer().append(getParent().getPath()).append(URIUtil.SLASH).append(this._tag).toString();
                }
            }
            return this._path;
        }

        public String getString(String str, boolean z, boolean z2) {
            Node node = get(str);
            if (node == null) {
                return null;
            }
            String node2 = node.toString(z);
            return (node2 == null || !z2) ? node2 : node2.trim();
        }

        public String getTag() {
            return this._tag;
        }

        public Iterator iterator(String str) {
            return new C13611(this, str);
        }

        public int size() {
            return this._list != null ? this._list.size() : 0;
        }

        public String toString() {
            String node;
            synchronized (this) {
                node = toString(true);
            }
            return node;
        }

        public String toString(boolean z) {
            String stringBuffer;
            synchronized (this) {
                StringBuffer stringBuffer2 = new StringBuffer();
                synchronized (stringBuffer2) {
                    toString(stringBuffer2, z);
                    stringBuffer = stringBuffer2.toString();
                }
            }
            return stringBuffer;
        }

        public String toString(boolean z, boolean z2) {
            String node;
            synchronized (this) {
                node = toString(z);
                if (node != null && z2) {
                    node = node.trim();
                }
            }
            return node;
        }
    }

    private class NoopHandler extends DefaultHandler {
        int _depth;
        Handler _next;
        private final XmlParser this$0;

        NoopHandler(XmlParser xmlParser, Handler handler) {
            this.this$0 = xmlParser;
            this._next = handler;
        }

        public void endElement(String str, String str2, String str3) throws SAXException {
            if (this._depth == 0) {
                XmlParser.access$000(this.this$0).getXMLReader().setContentHandler(this._next);
            } else {
                this._depth--;
            }
        }

        public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
            this._depth++;
        }
    }

    public XmlParser() {
        boolean z = !Boolean.getBoolean("org.mortbay.xml.XmlParser.NotValidating") && Boolean.valueOf(System.getProperty("org.mortbay.xml.XmlParser.Validating", SAXParserFactory.newInstance().getClass().toString().startsWith("org.apache.xerces.") ? "true" : "false")).booleanValue();
        setValidating(z);
    }

    public XmlParser(boolean z) {
        setValidating(z);
    }

    static SAXParser access$000(XmlParser xmlParser) {
        return xmlParser._parser;
    }

    static Object access$100(XmlParser xmlParser) {
        return xmlParser._xpaths;
    }

    static Map access$200(XmlParser xmlParser) {
        return xmlParser._observerMap;
    }

    static Stack access$300(XmlParser xmlParser) {
        return xmlParser._observers;
    }

    static String access$402(XmlParser xmlParser, String str) {
        xmlParser._dtd = str;
        return str;
    }

    static Map access$500(XmlParser xmlParser) {
        return xmlParser._redirectMap;
    }

    public void addContentHandler(String str, ContentHandler contentHandler) {
        synchronized (this) {
            if (this._observerMap == null) {
                this._observerMap = new HashMap();
            }
            this._observerMap.put(str, contentHandler);
        }
    }

    public String getDTD() {
        return this._dtd;
    }

    public String getXpath() {
        return this._xpath;
    }

    public Node parse(File file) throws IOException, SAXException {
        Node parse;
        synchronized (this) {
            if (Log.isDebugEnabled()) {
                Log.debug(new StringBuffer().append("parse: ").append(file).toString());
            }
            parse = parse(new InputSource(file.toURL().toString()));
        }
        return parse;
    }

    public Node parse(InputStream inputStream) throws IOException, SAXException {
        Node node;
        synchronized (this) {
            this._dtd = null;
            Handler handler = new Handler(this);
            XMLReader xMLReader = this._parser.getXMLReader();
            xMLReader.setContentHandler(handler);
            xMLReader.setErrorHandler(handler);
            xMLReader.setEntityResolver(handler);
            this._parser.parse(new InputSource(inputStream), handler);
            if (handler._error != null) {
                throw handler._error;
            }
            node = (Node) handler._top.get(0);
            handler.clear();
        }
        return node;
    }

    public Node parse(String str) throws IOException, SAXException {
        Node parse;
        synchronized (this) {
            if (Log.isDebugEnabled()) {
                Log.debug(new StringBuffer().append("parse: ").append(str).toString());
            }
            parse = parse(new InputSource(str));
        }
        return parse;
    }

    public Node parse(InputSource inputSource) throws IOException, SAXException {
        Node node;
        synchronized (this) {
            this._dtd = null;
            Handler handler = new Handler(this);
            XMLReader xMLReader = this._parser.getXMLReader();
            xMLReader.setContentHandler(handler);
            xMLReader.setErrorHandler(handler);
            xMLReader.setEntityResolver(handler);
            if (Log.isDebugEnabled()) {
                Log.debug(new StringBuffer().append("parsing: sid=").append(inputSource.getSystemId()).append(",pid=").append(inputSource.getPublicId()).toString());
            }
            this._parser.parse(inputSource, handler);
            if (handler._error != null) {
                throw handler._error;
            }
            node = (Node) handler._top.get(0);
            handler.clear();
        }
        return node;
    }

    public void redirectEntity(String str, URL url) {
        synchronized (this) {
            if (url != null) {
                this._redirectMap.put(str, url);
            }
        }
    }

    public void setValidating(boolean z) {
        try {
            SAXParserFactory newInstance = SAXParserFactory.newInstance();
            newInstance.setValidating(z);
            this._parser = newInstance.newSAXParser();
            if (z) {
                try {
                    this._parser.getXMLReader().setFeature("http://apache.org/xml/features/validation/schema", z);
                } catch (Throwable e) {
                    if (z) {
                        Log.warn("Schema validation may not be supported: ", e);
                    } else {
                        Log.ignore(e);
                    }
                }
            }
            this._parser.getXMLReader().setFeature("http://xml.org/sax/features/validation", z);
            this._parser.getXMLReader().setFeature("http://xml.org/sax/features/namespaces", true);
            this._parser.getXMLReader().setFeature("http://xml.org/sax/features/namespace-prefixes", false);
        } catch (Throwable e2) {
            Log.warn(Log.EXCEPTION, e2);
            throw new Error(e2.toString());
        }
    }

    public void setXpath(String str) {
        this._xpath = str;
        StringTokenizer stringTokenizer = new StringTokenizer(str, "| ");
        while (stringTokenizer.hasMoreTokens()) {
            this._xpaths = LazyList.add(this._xpaths, stringTokenizer.nextToken());
        }
    }
}
