package org.mortbay.component;

import java.util.EventListener;
import org.mortbay.log.Log;
import org.mortbay.util.LazyList;

public class Container {
    private Object _listeners;

    static class C13201 {
    }

    public interface Listener extends EventListener {
        void add(Relationship relationship);

        void addBean(Object obj);

        void remove(Relationship relationship);

        void removeBean(Object obj);
    }

    public static class Relationship {
        private Object _child;
        private Container _container;
        private Object _parent;
        private String _relationship;

        private Relationship(Container container, Object obj, Object obj2, String str) {
            this._container = container;
            this._parent = obj;
            this._child = obj2;
            this._relationship = str;
        }

        Relationship(Container container, Object obj, Object obj2, String str, C13201 c13201) {
            this(container, obj, obj2, str);
        }

        public boolean equals(Object obj) {
            if (obj != null && (obj instanceof Relationship)) {
                Relationship relationship = (Relationship) obj;
                if (relationship._parent == this._parent && relationship._child == this._child && relationship._relationship.equals(this._relationship)) {
                    return true;
                }
            }
            return false;
        }

        public Object getChild() {
            return this._child;
        }

        public Container getContainer() {
            return this._container;
        }

        public Object getParent() {
            return this._parent;
        }

        public String getRelationship() {
            return this._relationship;
        }

        public int hashCode() {
            return (this._parent.hashCode() + this._child.hashCode()) + this._relationship.hashCode();
        }

        public String toString() {
            return new StringBuffer().append(this._parent).append("---").append(this._relationship).append("-->").append(this._child).toString();
        }
    }

    private void add(Object obj, Object obj2, String str) {
        if (Log.isDebugEnabled()) {
            Log.debug(new StringBuffer().append("Container ").append(obj).append(" + ").append(obj2).append(" as ").append(str).toString());
        }
        if (this._listeners != null) {
            Relationship relationship = new Relationship(this, obj, obj2, str, null);
            for (int i = 0; i < LazyList.size(this._listeners); i++) {
                ((Listener) LazyList.get(this._listeners, i)).add(relationship);
            }
        }
    }

    private void remove(Object obj, Object obj2, String str) {
        if (Log.isDebugEnabled()) {
            Log.debug(new StringBuffer().append("Container ").append(obj).append(" - ").append(obj2).append(" as ").append(str).toString());
        }
        if (this._listeners != null) {
            Relationship relationship = new Relationship(this, obj, obj2, str, null);
            for (int i = 0; i < LazyList.size(this._listeners); i++) {
                ((Listener) LazyList.get(this._listeners, i)).remove(relationship);
            }
        }
    }

    public void addBean(Object obj) {
        if (this._listeners != null) {
            for (int i = 0; i < LazyList.size(this._listeners); i++) {
                ((Listener) LazyList.get(this._listeners, i)).addBean(obj);
            }
        }
    }

    public void addEventListener(Listener listener) {
        synchronized (this) {
            this._listeners = LazyList.add(this._listeners, listener);
        }
    }

    public void removeBean(Object obj) {
        if (this._listeners != null) {
            for (int i = 0; i < LazyList.size(this._listeners); i++) {
                ((Listener) LazyList.get(this._listeners, i)).removeBean(obj);
            }
        }
    }

    public void removeEventListener(Listener listener) {
        synchronized (this) {
            this._listeners = LazyList.remove(this._listeners, (Object) listener);
        }
    }

    public void update(Object obj, Object obj2, Object obj3, String str) {
        synchronized (this) {
            if (obj2 != null) {
                if (!obj2.equals(obj3)) {
                    remove(obj, obj2, str);
                }
            }
            if (!(obj3 == null || obj3.equals(obj2))) {
                add(obj, obj3, str);
            }
        }
    }

    public void update(Object obj, Object obj2, Object obj3, String str, boolean z) {
        synchronized (this) {
            if (obj2 != null) {
                if (!obj2.equals(obj3)) {
                    remove(obj, obj2, str);
                    if (z) {
                        removeBean(obj2);
                    }
                }
            }
            if (!(obj3 == null || obj3.equals(obj2))) {
                if (z) {
                    addBean(obj3);
                }
                add(obj, obj3, str);
            }
        }
    }

    public void update(Object obj, Object[] objArr, Object[] objArr2, String str) {
        synchronized (this) {
            update(obj, objArr, objArr2, str, false);
        }
    }

    public void update(Object obj, Object[] objArr, Object[] objArr2, String str, boolean z) {
        int i = 0;
        synchronized (this) {
            int length;
            int i2;
            Object[] objArr3;
            if (objArr2 != null) {
                Object[] objArr4 = new Object[objArr2.length];
                length = objArr2.length;
                while (true) {
                    i2 = length - 1;
                    if (length <= 0) {
                        break;
                    }
                    if (objArr != null) {
                        int length2 = objArr.length;
                        length = 1;
                        while (true) {
                            int i3 = length2 - 1;
                            if (length2 <= 0) {
                                break;
                            }
                            if (objArr2[i2] != null) {
                                if (objArr2[i2].equals(objArr[i3])) {
                                    objArr[i3] = null;
                                    length2 = i3;
                                    length = 0;
                                }
                            }
                            length2 = i3;
                        }
                    } else {
                        length = 1;
                    }
                    if (length != 0) {
                        objArr4[i2] = objArr2[i2];
                    }
                    length = i2;
                }
                objArr3 = objArr4;
            } else {
                objArr3 = null;
            }
            if (objArr != null) {
                length = objArr.length;
                while (true) {
                    i2 = length - 1;
                    if (length <= 0) {
                        break;
                    }
                    if (objArr[i2] != null) {
                        remove(obj, objArr[i2], str);
                        if (z) {
                            removeBean(objArr[i2]);
                            length = i2;
                        }
                    }
                    length = i2;
                }
            }
            if (objArr3 != null) {
                while (i < objArr3.length) {
                    if (objArr3[i] != null) {
                        if (z) {
                            addBean(objArr3[i]);
                        }
                        add(obj, objArr3[i], str);
                    }
                    i++;
                }
            }
        }
    }
}
