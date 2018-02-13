package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.MenuItem;
import android.view.View;

public class MenuItemCompat {
    static final MenuVersionImpl IMPL;
    public static final int SHOW_AS_ACTION_ALWAYS = 2;
    public static final int SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW = 8;
    public static final int SHOW_AS_ACTION_IF_ROOM = 1;
    public static final int SHOW_AS_ACTION_NEVER = 0;
    public static final int SHOW_AS_ACTION_WITH_TEXT = 4;

    interface MenuVersionImpl {
        MenuItem setActionView(MenuItem menuItem, View view);

        boolean setShowAsAction(MenuItem menuItem, int i);
    }

    static class BaseMenuVersionImpl implements MenuVersionImpl {
        BaseMenuVersionImpl() {
        }

        public MenuItem setActionView(MenuItem menuItem, View view) {
            return menuItem;
        }

        public boolean setShowAsAction(MenuItem menuItem, int i) {
            return false;
        }
    }

    static class HoneycombMenuVersionImpl implements MenuVersionImpl {
        HoneycombMenuVersionImpl() {
        }

        public MenuItem setActionView(MenuItem menuItem, View view) {
            return MenuItemCompatHoneycomb.setActionView(menuItem, view);
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

    public static MenuItem setActionView(MenuItem menuItem, View view) {
        return IMPL.setActionView(menuItem, view);
    }

    public static boolean setShowAsAction(MenuItem menuItem, int i) {
        return IMPL.setShowAsAction(menuItem, i);
    }
}
