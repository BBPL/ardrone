package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.MenuItem;

public class MenuCompat {
    static final MenuVersionImpl IMPL;

    interface MenuVersionImpl {
        boolean setShowAsAction(MenuItem menuItem, int i);
    }

    static class BaseMenuVersionImpl implements MenuVersionImpl {
        BaseMenuVersionImpl() {
        }

        public boolean setShowAsAction(MenuItem menuItem, int i) {
            return false;
        }
    }

    static class HoneycombMenuVersionImpl implements MenuVersionImpl {
        HoneycombMenuVersionImpl() {
        }

        public boolean setShowAsAction(MenuItem menuItem, int i) {
            MenuItemCompatHoneycomb.setShowAsAction(menuItem, i);
            return true;
        }
    }

    static {
        if (VERSION.SDK_INT >= 11) {
            IMPL = new HoneycombMenuVersionImpl();
        } else {
            IMPL = new BaseMenuVersionImpl();
        }
    }

    public static boolean setShowAsAction(MenuItem menuItem, int i) {
        return IMPL.setShowAsAction(menuItem, i);
    }
}
