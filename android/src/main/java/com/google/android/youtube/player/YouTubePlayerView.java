package com.google.android.youtube.player;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.internal.C0385t.C0375a;
import com.google.android.youtube.player.internal.C0385t.C0377b;
import com.google.android.youtube.player.internal.C0386b;
import com.google.android.youtube.player.internal.C0418n;
import com.google.android.youtube.player.internal.C0436s;
import com.google.android.youtube.player.internal.C0442w.C0441a;
import com.google.android.youtube.player.internal.ab;
import com.google.android.youtube.player.internal.ac;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class YouTubePlayerView extends ViewGroup implements Provider {
    private final C0379a f161a;
    private final Set<View> f162b;
    private final C0369b f163c;
    private C0386b f164d;
    private C0436s f165e;
    private View f166f;
    private C0418n f167g;
    private Provider f168h;
    private Bundle f169i;
    private OnInitializedListener f170j;
    private boolean f171k;

    interface C0369b {
        void mo1221a(YouTubePlayerView youTubePlayerView);

        void mo1222a(YouTubePlayerView youTubePlayerView, String str, OnInitializedListener onInitializedListener);
    }

    final class C03782 implements C0377b {
        final /* synthetic */ YouTubePlayerView f159a;

        C03782(YouTubePlayerView youTubePlayerView) {
            this.f159a = youTubePlayerView;
        }

        public final void mo1226a(YouTubeInitializationResult youTubeInitializationResult) {
            this.f159a.m1439a(youTubeInitializationResult);
            this.f159a.f164d = null;
        }
    }

    private final class C0379a implements OnGlobalFocusChangeListener {
        final /* synthetic */ YouTubePlayerView f160a;

        private C0379a(YouTubePlayerView youTubePlayerView) {
            this.f160a = youTubePlayerView;
        }

        public final void onGlobalFocusChanged(View view, View view2) {
            if (this.f160a.f165e != null && this.f160a.f162b.contains(view2) && !this.f160a.f162b.contains(view)) {
                this.f160a.f165e.m1721g();
            }
        }
    }

    public YouTubePlayerView(Context context) {
        this(context, null);
    }

    public YouTubePlayerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public YouTubePlayerView(Context context, AttributeSet attributeSet, int i) {
        if (context instanceof YouTubeBaseActivity) {
            this(context, attributeSet, i, ((YouTubeBaseActivity) context).m1417a());
            return;
        }
        throw new IllegalStateException("A YouTubePlayerView can only be created with an Activity  which extends YouTubeBaseActivity as its context.");
    }

    YouTubePlayerView(Context context, AttributeSet attributeSet, int i, C0369b c0369b) {
        super((Context) ac.m1483a((Object) context, (Object) "context cannot be null"), attributeSet, i);
        this.f163c = (C0369b) ac.m1483a((Object) c0369b, (Object) "listener cannot be null");
        if (getBackground() == null) {
            setBackgroundColor(-16777216);
        }
        setClipToPadding(false);
        this.f167g = new C0418n(context);
        requestTransparentRegion(this.f167g);
        addView(this.f167g);
        this.f162b = new HashSet();
        this.f161a = new C0379a();
    }

    private void m1438a(View view) {
        Object obj = (view == this.f167g || (this.f165e != null && view == this.f166f)) ? 1 : null;
        if (obj == null) {
            throw new UnsupportedOperationException("No views can be added on top of the player");
        }
    }

    private void m1439a(YouTubeInitializationResult youTubeInitializationResult) {
        this.f165e = null;
        this.f167g.m1642c();
        if (this.f170j != null) {
            this.f170j.onInitializationFailure(this.f168h, youTubeInitializationResult);
            this.f170j = null;
        }
    }

    static /* synthetic */ void m1440a(YouTubePlayerView youTubePlayerView, Activity activity) {
        try {
            youTubePlayerView.f165e = new C0436s(youTubePlayerView.f164d, ab.m1477a().mo1244a(activity, youTubePlayerView.f164d));
            youTubePlayerView.f166f = youTubePlayerView.f165e.m1709a();
            youTubePlayerView.addView(youTubePlayerView.f166f);
            youTubePlayerView.removeView(youTubePlayerView.f167g);
            youTubePlayerView.f163c.mo1221a(youTubePlayerView);
            if (youTubePlayerView.f170j != null) {
                boolean z = false;
                if (youTubePlayerView.f169i != null) {
                    z = youTubePlayerView.f165e.m1713a(youTubePlayerView.f169i);
                    youTubePlayerView.f169i = null;
                }
                youTubePlayerView.f170j.onInitializationSuccess(youTubePlayerView.f168h, youTubePlayerView.f165e, z);
                youTubePlayerView.f170j = null;
            }
        } catch (C0441a e) {
            youTubePlayerView.m1439a(YouTubeInitializationResult.INTERNAL_ERROR);
        }
    }

    final void m1450a() {
        if (this.f165e != null) {
            this.f165e.m1714b();
        }
    }

    final void m1451a(final Activity activity, Provider provider, String str, OnInitializedListener onInitializedListener, Bundle bundle) {
        if (this.f165e == null && this.f170j == null) {
            ac.m1483a((Object) activity, (Object) "activity cannot be null");
            this.f168h = (Provider) ac.m1483a((Object) provider, (Object) "provider cannot be null");
            this.f170j = (OnInitializedListener) ac.m1483a((Object) onInitializedListener, (Object) "listener cannot be null");
            this.f169i = bundle;
            this.f167g.m1641b();
            this.f164d = ab.m1477a().mo1243a(getContext(), str, new C0375a(this) {
                final /* synthetic */ YouTubePlayerView f158b;

                public final void mo1224a() {
                    if (this.f158b.f164d != null) {
                        YouTubePlayerView.m1440a(this.f158b, activity);
                    }
                    this.f158b.f164d = null;
                }

                public final void mo1225b() {
                    if (!(this.f158b.f171k || this.f158b.f165e == null)) {
                        this.f158b.f165e.m1720f();
                    }
                    this.f158b.f167g.m1640a();
                    if (this.f158b.indexOfChild(this.f158b.f167g) < 0) {
                        this.f158b.addView(this.f158b.f167g);
                        this.f158b.removeView(this.f158b.f166f);
                    }
                    this.f158b.f166f = null;
                    this.f158b.f165e = null;
                    this.f158b.f164d = null;
                }
            }, new C03782(this));
            this.f164d.mo1317e();
        }
    }

    final void m1452a(boolean z) {
        if (this.f165e != null) {
            this.f165e.m1715b(z);
            m1454b(z);
        }
    }

    public final void addFocusables(ArrayList<View> arrayList, int i) {
        Collection arrayList2 = new ArrayList();
        super.addFocusables(arrayList2, i);
        arrayList.addAll(arrayList2);
        this.f162b.clear();
        this.f162b.addAll(arrayList2);
    }

    public final void addFocusables(ArrayList<View> arrayList, int i, int i2) {
        Collection arrayList2 = new ArrayList();
        super.addFocusables(arrayList2, i, i2);
        arrayList.addAll(arrayList2);
        this.f162b.clear();
        this.f162b.addAll(arrayList2);
    }

    public final void addView(View view) {
        m1438a(view);
        super.addView(view);
    }

    public final void addView(View view, int i) {
        m1438a(view);
        super.addView(view, i);
    }

    public final void addView(View view, int i, int i2) {
        m1438a(view);
        super.addView(view, i, i2);
    }

    public final void addView(View view, int i, LayoutParams layoutParams) {
        m1438a(view);
        super.addView(view, i, layoutParams);
    }

    public final void addView(View view, LayoutParams layoutParams) {
        m1438a(view);
        super.addView(view, layoutParams);
    }

    final void m1453b() {
        if (this.f165e != null) {
            this.f165e.m1717c();
        }
    }

    final void m1454b(boolean z) {
        this.f171k = true;
        if (this.f165e != null) {
            this.f165e.m1711a(z);
        }
    }

    final void m1455c() {
        if (this.f165e != null) {
            this.f165e.m1718d();
        }
    }

    public final void clearChildFocus(View view) {
        if (hasFocusable()) {
            requestFocus();
        } else {
            super.clearChildFocus(view);
        }
    }

    final void m1456d() {
        if (this.f165e != null) {
            this.f165e.m1719e();
        }
    }

    public final boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (this.f165e != null) {
            if (keyEvent.getAction() == 0) {
                return this.f165e.m1712a(keyEvent.getKeyCode(), keyEvent) || super.dispatchKeyEvent(keyEvent);
            } else {
                if (keyEvent.getAction() == 1) {
                    return this.f165e.m1716b(keyEvent.getKeyCode(), keyEvent) || super.dispatchKeyEvent(keyEvent);
                }
            }
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    final Bundle m1457e() {
        return this.f165e == null ? this.f169i : this.f165e.m1722h();
    }

    public final void focusableViewAvailable(View view) {
        super.focusableViewAvailable(view);
        this.f162b.add(view);
    }

    public final void initialize(String str, OnInitializedListener onInitializedListener) {
        ac.m1484a(str, (Object) "Developer key cannot be null or empty");
        this.f163c.mo1222a(this, str, onInitializedListener);
    }

    protected final void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalFocusChangeListener(this.f161a);
    }

    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.f165e != null) {
            this.f165e.m1710a(configuration);
        }
    }

    protected final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalFocusChangeListener(this.f161a);
    }

    protected final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (getChildCount() > 0) {
            getChildAt(0).layout(0, 0, i3 - i, i4 - i2);
        }
    }

    protected final void onMeasure(int i, int i2) {
        if (getChildCount() > 0) {
            View childAt = getChildAt(0);
            childAt.measure(i, i2);
            setMeasuredDimension(childAt.getMeasuredWidth(), childAt.getMeasuredHeight());
            return;
        }
        setMeasuredDimension(0, 0);
    }

    public final boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        return true;
    }

    public final void requestChildFocus(View view, View view2) {
        super.requestChildFocus(view, view2);
        this.f162b.add(view2);
    }

    public final void setClipToPadding(boolean z) {
    }

    public final void setPadding(int i, int i2, int i3, int i4) {
    }
}
