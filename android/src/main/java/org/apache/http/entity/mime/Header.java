package org.apache.http.entity.mime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Header implements Iterable<MinimalField> {
    private final Map<String, List<MinimalField>> fieldMap = new HashMap();
    private final List<MinimalField> fields = new LinkedList();

    public void addField(MinimalField minimalField) {
        if (minimalField != null) {
            String toLowerCase = minimalField.getName().toLowerCase(Locale.US);
            List list = (List) this.fieldMap.get(toLowerCase);
            if (list == null) {
                list = new LinkedList();
                this.fieldMap.put(toLowerCase, list);
            }
            list.add(minimalField);
            this.fields.add(minimalField);
        }
    }

    public MinimalField getField(String str) {
        if (str != null) {
            List list = (List) this.fieldMap.get(str.toLowerCase(Locale.US));
            if (!(list == null || list.isEmpty())) {
                return (MinimalField) list.get(0);
            }
        }
        return null;
    }

    public List<MinimalField> getFields() {
        return new ArrayList(this.fields);
    }

    public List<MinimalField> getFields(String str) {
        if (str == null) {
            return null;
        }
        List list = (List) this.fieldMap.get(str.toLowerCase(Locale.US));
        return (list == null || list.isEmpty()) ? Collections.emptyList() : new ArrayList(list);
    }

    public Iterator<MinimalField> iterator() {
        return Collections.unmodifiableList(this.fields).iterator();
    }

    public int removeFields(String str) {
        if (str != null) {
            List list = (List) this.fieldMap.remove(str.toLowerCase(Locale.US));
            if (!(list == null || list.isEmpty())) {
                this.fields.removeAll(list);
                return list.size();
            }
        }
        return 0;
    }

    public void setField(MinimalField minimalField) {
        if (minimalField != null) {
            List list = (List) this.fieldMap.get(minimalField.getName().toLowerCase(Locale.US));
            if (list == null || list.isEmpty()) {
                addField(minimalField);
                return;
            }
            list.clear();
            list.add(minimalField);
            Iterator it = this.fields.iterator();
            int i = -1;
            int i2 = 0;
            while (it.hasNext()) {
                int i3;
                if (((MinimalField) it.next()).getName().equalsIgnoreCase(minimalField.getName())) {
                    it.remove();
                    if (i == -1) {
                        i3 = i2;
                        i2++;
                        i = i3;
                    }
                }
                i3 = i;
                i2++;
                i = i3;
            }
            this.fields.add(i, minimalField);
        }
    }

    public String toString() {
        return this.fields.toString();
    }
}
