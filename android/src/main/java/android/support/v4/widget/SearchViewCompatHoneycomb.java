package android.support.v4.widget;

import android.content.Context;
import android.view.View;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

class SearchViewCompatHoneycomb {

    interface OnQueryTextListenerCompatBridge {
        boolean onQueryTextChange(String str);

        boolean onQueryTextSubmit(String str);
    }

    SearchViewCompatHoneycomb() {
    }

    public static Object newOnQueryTextListener(final OnQueryTextListenerCompatBridge onQueryTextListenerCompatBridge) {
        return new OnQueryTextListener() {
            public boolean onQueryTextChange(String str) {
                return onQueryTextListenerCompatBridge.onQueryTextChange(str);
            }

            public boolean onQueryTextSubmit(String str) {
                return onQueryTextListenerCompatBridge.onQueryTextSubmit(str);
            }
        };
    }

    public static View newSearchView(Context context) {
        return new SearchView(context);
    }

    public static void setOnQueryTextListener(Object obj, Object obj2) {
        ((SearchView) obj).setOnQueryTextListener((OnQueryTextListener) obj2);
    }
}
