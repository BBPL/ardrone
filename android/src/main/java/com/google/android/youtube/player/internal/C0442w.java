package com.google.android.youtube.player.internal;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import com.google.android.youtube.player.internal.C0390d.C0392a;

public final class C0442w {

    public static final class C0441a extends Exception {
        public C0441a(String str) {
            super(str);
        }

        public C0441a(String str, Throwable th) {
            super(str, th);
        }
    }

    private static IBinder m1726a(Class<?> cls, IBinder iBinder, IBinder iBinder2) throws C0441a {
        try {
            return (IBinder) cls.getConstructor(new Class[]{IBinder.class, IBinder.class}).newInstance(new Object[]{iBinder, iBinder2});
        } catch (Throwable e) {
            throw new C0441a("Could not find the right constructor for " + cls.getName(), e);
        } catch (Throwable e2) {
            throw new C0441a("Exception thrown by invoked constructor in " + cls.getName(), e2);
        } catch (Throwable e22) {
            throw new C0441a("Unable to instantiate the dynamic class " + cls.getName(), e22);
        } catch (Throwable e222) {
            throw new C0441a("Unable to call the default constructor of " + cls.getName(), e222);
        }
    }

    private static IBinder m1727a(ClassLoader classLoader, String str, IBinder iBinder, IBinder iBinder2) throws C0441a {
        try {
            return C0442w.m1726a(classLoader.loadClass(str), iBinder, iBinder2);
        } catch (Throwable e) {
            throw new C0441a("Unable to find dynamic class " + str, e);
        }
    }

    public static C0390d m1728a(Activity activity, IBinder iBinder) throws C0441a {
        ac.m1483a((Object) activity, (Object) "activity cannot be null");
        ac.m1483a((Object) iBinder, (Object) "serviceBinder cannot be null");
        Context b = C0444z.m1736b((Context) activity);
        if (b == null) {
            throw new C0441a("Could not create remote context");
        }
        return C0392a.m1582a(C0442w.m1727a(b.getClassLoader(), "com.google.android.youtube.api.jar.client.RemoteEmbeddedPlayer", C0440v.m1724a(new aa(activity, b.getResources(), b.getClassLoader(), C0444z.m1731a((Context) activity, b))).asBinder(), iBinder));
    }
}
