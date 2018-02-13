package com.google.android.youtube.player.internal;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.View;
import java.lang.reflect.InvocationTargetException;

public final class aa extends C0384y {
    private static final Class<?>[] f184a = new Class[]{Context.class, AttributeSet.class};
    private final Resources f185b;
    private final LayoutInflater f186c;
    private final Theme f187d;

    private static final class C0383a implements Factory {
        private final ClassLoader f182a;

        public C0383a(ClassLoader classLoader) {
            this.f182a = (ClassLoader) ac.m1483a((Object) classLoader, (Object) "remoteClassLoader cannot be null");
        }

        public final View onCreateView(String str, Context context, AttributeSet attributeSet) {
            try {
                return (View) this.f182a.loadClass(str).asSubclass(View.class).getConstructor(aa.f184a).newInstance(new Object[]{context, attributeSet});
            } catch (NoClassDefFoundError e) {
                return null;
            } catch (ClassNotFoundException e2) {
                return null;
            } catch (NoSuchMethodException e3) {
                return null;
            } catch (IllegalArgumentException e4) {
                return null;
            } catch (InstantiationException e5) {
                return null;
            } catch (IllegalAccessException e6) {
                return null;
            } catch (InvocationTargetException e7) {
                return null;
            }
        }
    }

    public aa(Activity activity, Resources resources, ClassLoader classLoader, int i) {
        super(activity);
        this.f185b = (Resources) ac.m1483a((Object) resources, (Object) "resources cannot be null");
        LayoutInflater cloneInContext = ((LayoutInflater) super.getSystemService("layout_inflater")).cloneInContext(this);
        cloneInContext.setFactory(new C0383a(classLoader));
        this.f186c = cloneInContext;
        this.f187d = resources.newTheme();
        this.f187d.applyStyle(i, false);
    }

    public final Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public final Context getBaseContext() {
        return super.getBaseContext();
    }

    public final Resources getResources() {
        return this.f185b;
    }

    public final Object getSystemService(String str) {
        return "layout_inflater".equals(str) ? this.f186c : super.getSystemService(str);
    }

    public final Theme getTheme() {
        return this.f187d;
    }
}
