package com.google.android.gms.dynamic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.common.GooglePlayServicesUtil;
import java.util.Iterator;
import java.util.LinkedList;

public abstract class C0074a<T extends LifecycleDelegate> {
    private T cP;
    private Bundle cQ;
    private LinkedList<C0068a> cR;
    private final C0066d<T> cS = new C00671(this);

    class C00671 implements C0066d<T> {
        final /* synthetic */ C0074a cT;

        C00671(C0074a c0074a) {
            this.cT = c0074a;
        }

        public void mo360a(T t) {
            this.cT.cP = t;
            Iterator it = this.cT.cR.iterator();
            while (it.hasNext()) {
                ((C0068a) it.next()).mo361b(this.cT.cP);
            }
            this.cT.cR.clear();
            this.cT.cQ = null;
        }
    }

    private interface C0068a {
        void mo361b(LifecycleDelegate lifecycleDelegate);

        int getState();
    }

    class C00736 implements C0068a {
        final /* synthetic */ C0074a cT;

        C00736(C0074a c0074a) {
            this.cT = c0074a;
        }

        public void mo361b(LifecycleDelegate lifecycleDelegate) {
            this.cT.cP.onResume();
        }

        public int getState() {
            return 3;
        }
    }

    private void m166a(Bundle bundle, C0068a c0068a) {
        if (this.cP != null) {
            c0068a.mo361b(this.cP);
            return;
        }
        if (this.cR == null) {
            this.cR = new LinkedList();
        }
        this.cR.add(c0068a);
        if (bundle != null) {
            if (this.cQ == null) {
                this.cQ = (Bundle) bundle.clone();
            } else {
                this.cQ.putAll(bundle);
            }
        }
        mo1001a(this.cS);
    }

    private void m168y(int i) {
        while (!this.cR.isEmpty() && ((C0068a) this.cR.getLast()).getState() >= i) {
            this.cR.removeLast();
        }
    }

    public void m169a(FrameLayout frameLayout) {
        final Context context = frameLayout.getContext();
        final int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        CharSequence b = GooglePlayServicesUtil.m23b(context, isGooglePlayServicesAvailable, -1);
        CharSequence a = GooglePlayServicesUtil.m19a(context, isGooglePlayServicesAvailable);
        View linearLayout = new LinearLayout(frameLayout.getContext());
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(new LayoutParams(-2, -2));
        frameLayout.addView(linearLayout);
        View textView = new TextView(frameLayout.getContext());
        textView.setLayoutParams(new LayoutParams(-2, -2));
        textView.setText(b);
        linearLayout.addView(textView);
        if (a != null) {
            View button = new Button(context);
            button.setLayoutParams(new LayoutParams(-2, -2));
            button.setText(a);
            linearLayout.addView(button);
            button.setOnClickListener(new OnClickListener(this) {
                final /* synthetic */ C0074a cT;

                public void onClick(View view) {
                    context.startActivity(GooglePlayServicesUtil.m18a(context, isGooglePlayServicesAvailable, -1));
                }
            });
        }
    }

    protected abstract void mo1001a(C0066d<T> c0066d);

    public T at() {
        return this.cP;
    }

    public void onCreate(final Bundle bundle) {
        m166a(bundle, new C0068a(this) {
            final /* synthetic */ C0074a cT;

            public void mo361b(LifecycleDelegate lifecycleDelegate) {
                this.cT.cP.onCreate(bundle);
            }

            public int getState() {
                return 1;
            }
        });
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        final FrameLayout frameLayout = new FrameLayout(layoutInflater.getContext());
        final LayoutInflater layoutInflater2 = layoutInflater;
        final ViewGroup viewGroup2 = viewGroup;
        final Bundle bundle2 = bundle;
        m166a(bundle, new C0068a(this) {
            final /* synthetic */ C0074a cT;

            public void mo361b(LifecycleDelegate lifecycleDelegate) {
                frameLayout.removeAllViews();
                frameLayout.addView(this.cT.cP.onCreateView(layoutInflater2, viewGroup2, bundle2));
            }

            public int getState() {
                return 2;
            }
        });
        if (this.cP == null) {
            m169a(frameLayout);
        }
        return frameLayout;
    }

    public void onDestroy() {
        if (this.cP != null) {
            this.cP.onDestroy();
        } else {
            m168y(1);
        }
    }

    public void onDestroyView() {
        if (this.cP != null) {
            this.cP.onDestroyView();
        } else {
            m168y(2);
        }
    }

    public void onInflate(final Activity activity, final Bundle bundle, final Bundle bundle2) {
        m166a(bundle2, new C0068a(this) {
            final /* synthetic */ C0074a cT;

            public void mo361b(LifecycleDelegate lifecycleDelegate) {
                this.cT.cP.onInflate(activity, bundle, bundle2);
            }

            public int getState() {
                return 0;
            }
        });
    }

    public void onLowMemory() {
        if (this.cP != null) {
            this.cP.onLowMemory();
        }
    }

    public void onPause() {
        if (this.cP != null) {
            this.cP.onPause();
        } else {
            m168y(3);
        }
    }

    public void onResume() {
        m166a(null, new C00736(this));
    }

    public void onSaveInstanceState(Bundle bundle) {
        if (this.cP != null) {
            this.cP.onSaveInstanceState(bundle);
        } else if (this.cQ != null) {
            bundle.putAll(this.cQ);
        }
    }
}
