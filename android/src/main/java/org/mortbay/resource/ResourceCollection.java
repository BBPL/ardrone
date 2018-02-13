package org.mortbay.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;
import org.mortbay.jetty.HttpVersions;

public class ResourceCollection extends Resource {
    private Resource[] _resources;

    public ResourceCollection(String str) {
        setResources(str);
    }

    public ResourceCollection(String[] strArr) {
        setResources(strArr);
    }

    public ResourceCollection(Resource[] resourceArr) {
        setResources(resourceArr);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.mortbay.resource.Resource addPath(java.lang.String r8) throws java.io.IOException, java.net.MalformedURLException {
        /*
        r7 = this;
        r2 = 0;
        r0 = r7._resources;
        if (r0 != 0) goto L_0x000d;
    L_0x0005:
        r0 = new java.lang.IllegalStateException;
        r1 = "*resources* not set.";
        r0.<init>(r1);
        throw r0;
    L_0x000d:
        if (r8 != 0) goto L_0x0015;
    L_0x000f:
        r0 = new java.net.MalformedURLException;
        r0.<init>();
        throw r0;
    L_0x0015:
        r0 = r8.length();
        if (r0 == 0) goto L_0x0023;
    L_0x001b:
        r0 = "/";
        r0 = r0.equals(r8);
        if (r0 == 0) goto L_0x0025;
    L_0x0023:
        r2 = r7;
    L_0x0024:
        return r2;
    L_0x0025:
        r0 = 0;
        r1 = r0;
        r0 = r2;
    L_0x0028:
        r3 = r7._resources;
        r3 = r3.length;
        if (r1 >= r3) goto L_0x0041;
    L_0x002d:
        r0 = r7._resources;
        r0 = r0[r1];
        r0 = r0.addPath(r8);
        r3 = r0.exists();
        if (r3 == 0) goto L_0x0073;
    L_0x003b:
        r3 = r0.isDirectory();
        if (r3 == 0) goto L_0x008e;
    L_0x0041:
        r1 = r1 + 1;
        r3 = r1;
        r1 = r2;
    L_0x0045:
        r4 = r7._resources;
        r4 = r4.length;
        if (r3 >= r4) goto L_0x0076;
    L_0x004a:
        r4 = r7._resources;
        r4 = r4[r3];
        r4 = r4.addPath(r8);
        r5 = r4.exists();
        if (r5 == 0) goto L_0x0094;
    L_0x0058:
        r5 = r4.isDirectory();
        if (r5 == 0) goto L_0x0094;
    L_0x005e:
        if (r0 == 0) goto L_0x0090;
    L_0x0060:
        r1 = new java.util.ArrayList;
        r1.<init>();
        r1.add(r0);
        r0 = r1;
        r1 = r2;
    L_0x006a:
        r0.add(r4);
    L_0x006d:
        r3 = r3 + 1;
        r6 = r0;
        r0 = r1;
        r1 = r6;
        goto L_0x0045;
    L_0x0073:
        r1 = r1 + 1;
        goto L_0x0028;
    L_0x0076:
        if (r0 != 0) goto L_0x008e;
    L_0x0078:
        if (r1 == 0) goto L_0x0024;
    L_0x007a:
        r2 = new org.mortbay.resource.ResourceCollection;
        r0 = r1.size();
        r0 = new org.mortbay.resource.Resource[r0];
        r0 = r1.toArray(r0);
        r0 = (org.mortbay.resource.Resource[]) r0;
        r0 = (org.mortbay.resource.Resource[]) r0;
        r2.<init>(r0);
        goto L_0x0024;
    L_0x008e:
        r7 = r0;
        goto L_0x0023;
    L_0x0090:
        r6 = r0;
        r0 = r1;
        r1 = r6;
        goto L_0x006a;
    L_0x0094:
        r6 = r1;
        r1 = r0;
        r0 = r6;
        goto L_0x006d;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.resource.ResourceCollection.addPath(java.lang.String):org.mortbay.resource.Resource");
    }

    public boolean delete() throws SecurityException {
        throw new UnsupportedOperationException();
    }

    public boolean exists() {
        if (this._resources != null) {
            return true;
        }
        throw new IllegalStateException("*resources* not set.");
    }

    protected Object findResource(String str) throws IOException, MalformedURLException {
        ArrayList arrayList;
        Resource addPath;
        int i = 0;
        Object obj = null;
        while (i < this._resources.length) {
            int i2;
            obj = this._resources[i].addPath(str);
            if (obj.exists()) {
                if (!obj.isDirectory()) {
                    return obj;
                }
                arrayList = null;
                for (i2 = i + 1; i2 < this._resources.length; i2++) {
                    addPath = this._resources[i2].addPath(str);
                    if (addPath.exists() && addPath.isDirectory()) {
                        if (obj != null) {
                            arrayList = new ArrayList();
                            arrayList.add(obj);
                        }
                        arrayList.add(addPath);
                    }
                }
                return obj == null ? obj : arrayList == null ? arrayList : null;
            } else {
                i++;
            }
        }
        arrayList = null;
        for (i2 = i + 1; i2 < this._resources.length; i2++) {
            addPath = this._resources[i2].addPath(str);
            if (obj != null) {
                arrayList = new ArrayList();
                arrayList.add(obj);
            }
            arrayList.add(addPath);
        }
        if (obj == null) {
            if (arrayList == null) {
            }
        }
    }

    public File getFile() throws IOException {
        if (this._resources == null) {
            throw new IllegalStateException("*resources* not set.");
        }
        for (Resource file : this._resources) {
            File file2 = file.getFile();
            if (file2 != null) {
                return file2;
            }
        }
        return null;
    }

    public InputStream getInputStream() throws IOException {
        if (this._resources == null) {
            throw new IllegalStateException("*resources* not set.");
        }
        for (Resource inputStream : this._resources) {
            InputStream inputStream2 = inputStream.getInputStream();
            if (inputStream2 != null) {
                return inputStream2;
            }
        }
        return null;
    }

    public String getName() {
        if (this._resources == null) {
            throw new IllegalStateException("*resources* not set.");
        }
        for (Resource name : this._resources) {
            String name2 = name.getName();
            if (name2 != null) {
                return name2;
            }
        }
        return null;
    }

    public OutputStream getOutputStream() throws IOException, SecurityException {
        if (this._resources == null) {
            throw new IllegalStateException("*resources* not set.");
        }
        for (Resource outputStream : this._resources) {
            OutputStream outputStream2 = outputStream.getOutputStream();
            if (outputStream2 != null) {
                return outputStream2;
            }
        }
        return null;
    }

    public Resource[] getResources() {
        return this._resources;
    }

    public URL getURL() {
        if (this._resources == null) {
            throw new IllegalStateException("*resources* not set.");
        }
        for (Resource url : this._resources) {
            URL url2 = url.getURL();
            if (url2 != null) {
                return url2;
            }
        }
        return null;
    }

    public boolean isDirectory() {
        if (this._resources != null) {
            return true;
        }
        throw new IllegalStateException("*resources* not set.");
    }

    public long lastModified() {
        if (this._resources == null) {
            throw new IllegalStateException("*resources* not set.");
        }
        for (Resource lastModified : this._resources) {
            long lastModified2 = lastModified.lastModified();
            if (lastModified2 != -1) {
                return lastModified2;
            }
        }
        return -1;
    }

    public long length() {
        return -1;
    }

    public String[] list() {
        if (this._resources == null) {
            throw new IllegalStateException("*resources* not set.");
        }
        HashSet hashSet = new HashSet();
        for (Resource list : this._resources) {
            String[] list2 = list.list();
            for (Object add : list2) {
                hashSet.add(add);
            }
        }
        return (String[]) hashSet.toArray(new String[hashSet.size()]);
    }

    public void release() {
        if (this._resources == null) {
            throw new IllegalStateException("*resources* not set.");
        }
        for (Resource release : this._resources) {
            release.release();
        }
    }

    public boolean renameTo(Resource resource) throws SecurityException {
        throw new UnsupportedOperationException();
    }

    public void setResources(String str) {
        if (this._resources != null) {
            throw new IllegalStateException("*resources* already set.");
        } else if (str == null) {
            throw new IllegalArgumentException("*csvResources* must not be null.");
        } else {
            StringTokenizer stringTokenizer = new StringTokenizer(str, ",;");
            int countTokens = stringTokenizer.countTokens();
            if (countTokens == 0) {
                throw new IllegalArgumentException("arg *resources* must be one or more resources.");
            }
            this._resources = new Resource[countTokens];
            countTokens = 0;
            while (stringTokenizer.hasMoreTokens()) {
                try {
                    this._resources[countTokens] = Resource.newResource(stringTokenizer.nextToken().trim());
                    if (this._resources[countTokens].exists() && this._resources[countTokens].isDirectory()) {
                        countTokens++;
                    } else {
                        throw new IllegalArgumentException(new StringBuffer().append(this._resources[countTokens]).append(" is not an existing directory.").toString());
                    }
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void setResources(String[] strArr) {
        if (this._resources != null) {
            throw new IllegalStateException("*resources* already set.");
        } else if (strArr == null) {
            throw new IllegalArgumentException("*resources* must not be null.");
        } else if (strArr.length == 0) {
            throw new IllegalArgumentException("arg *resources* must be one or more resources.");
        } else {
            this._resources = new Resource[strArr.length];
            int i = 0;
            while (i < strArr.length) {
                try {
                    this._resources[i] = Resource.newResource(strArr[i]);
                    if (this._resources[i].exists() && this._resources[i].isDirectory()) {
                        i++;
                    } else {
                        throw new IllegalArgumentException(new StringBuffer().append(this._resources[i]).append(" is not an existing directory.").toString());
                    }
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void setResources(Resource[] resourceArr) {
        if (this._resources != null) {
            throw new IllegalStateException("*resources* already set.");
        } else if (resourceArr == null) {
            throw new IllegalArgumentException("*resources* must not be null.");
        } else if (resourceArr.length == 0) {
            throw new IllegalArgumentException("arg *resources* must be one or more resources.");
        } else {
            this._resources = resourceArr;
            int i = 0;
            while (i < this._resources.length) {
                Resource resource = this._resources[i];
                if (resource.exists() && resource.isDirectory()) {
                    i++;
                } else {
                    throw new IllegalArgumentException(new StringBuffer().append(resource).append(" is not an existing directory.").toString());
                }
            }
        }
    }

    public void setResourcesAsCSV(String str) {
        setResources(str);
    }

    public String toString() {
        if (this._resources == null) {
            return HttpVersions.HTTP_0_9;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (Object obj : this._resources) {
            stringBuffer.append(obj.toString()).append(';');
        }
        return stringBuffer.toString();
    }
}
