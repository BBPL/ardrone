package android.support.v4.widget;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;

public class SearchViewCompat {
    private static final SearchViewCompatImpl IMPL;

    public static abstract class OnQueryTextListenerCompat {
        final Object mListener = SearchViewCompat.IMPL.newOnQueryTextListener(this);

        public boolean onQueryTextChange(String str) {
            return false;
        }

        public boolean onQueryTextSubmit(String str) {
            return false;
        }
    }

    interface SearchViewCompatImpl {
        Object newOnQueryTextListener(OnQueryTextListenerCompat onQueryTextListenerCompat);

        View newSearchView(Context context);

        void setOnQueryTextListener(Object obj, Object obj2);
    }

    static class SearchViewCompatStubImpl implements SearchViewCompatImpl {
        SearchViewCompatStubImpl() {
        }

        public Object newOnQueryTextListener(OnQueryTextListenerCompat onQueryTextListenerCompat) {
            return null;
        }

        public View newSearchView(Context context) {
            return null;
        }

        public void setOnQueryTextListener(Object obj, Object obj2) {
        }
    }

    static class SearchViewCompatHoneycombImpl extends SearchViewCompatStubImpl {
        SearchViewCompatHoneycombImpl() {
        }

        public Object newOnQueryTextListener(final OnQueryTextListenerCompat onQueryTextListenerCompat) {
            return SearchViewCompatHoneycomb.newOnQueryTextListener(new OnQueryTextListenerCompatBridge() {
                public boolean onQueryTextChange(String str) {
                    return onQueryTextListenerCompat.onQueryTextChange(str);
                }

                public boolean onQueryTextSubmit(String str) {
                    return onQueryTextListenerCompat.onQueryTextSubmit(str);
                }
            });
        }

        public View newSearchView(Context context) {
            return SearchViewCompatHoneycomb.newSearchView(context);
        }

        public void setOnQueryTextListener(Object obj, Object obj2) {
            SearchViewCompatHoneycomb.setOnQueryTextListener(obj, obj2);
        }
    }

    static {
        if (VERSION.SDK_INT >= 11) {
            IMPL = new SearchViewCompatHoneycombImpl();
        } else {
            IMPL = new SearchViewCompatStubImpl();
        }
    }

    private SearchViewCompat(Context context) {
    }

    public static View newSearchView(Context context) {
        return IMPL.newSearchView(context);
    }

    public static void setOnQueryTextListener(View view, OnQueryTextListenerCompat onQueryTextListenerCompat) {
        IMPL.setOnQueryTextListener(view, onQueryTextListenerCompat.mListener);
    }
}
