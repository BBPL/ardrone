package com.google.android.gms.maps;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.dynamic.C0066d;
import com.google.android.gms.dynamic.C0074a;
import com.google.android.gms.dynamic.C0078c;
import com.google.android.gms.dynamic.LifecycleDelegate;
import com.google.android.gms.internal.C0242s;
import com.google.android.gms.maps.internal.C0323p;
import com.google.android.gms.maps.internal.IMapViewDelegate;
import com.google.android.gms.maps.model.RuntimeRemoteException;

public class MapView extends FrameLayout {
    private final C0287b gD;
    private GoogleMap gz;

    static class C0286a implements LifecycleDelegate {
        private final ViewGroup gE;
        private final IMapViewDelegate gF;
        private View gG;

        public C0286a(ViewGroup viewGroup, IMapViewDelegate iMapViewDelegate) {
            this.gF = (IMapViewDelegate) C0242s.m1208d(iMapViewDelegate);
            this.gE = (ViewGroup) C0242s.m1208d(viewGroup);
        }

        public IMapViewDelegate bj() {
            return this.gF;
        }

        public void onCreate(Bundle bundle) {
            try {
                this.gF.onCreate(bundle);
                this.gG = (View) C0078c.m172a(this.gF.getView());
                this.gE.removeAllViews();
                this.gE.addView(this.gG);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            throw new UnsupportedOperationException("onCreateView not allowed on MapViewDelegate");
        }

        public void onDestroy() {
            try {
                this.gF.onDestroy();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onDestroyView() {
            throw new UnsupportedOperationException("onDestroyView not allowed on MapViewDelegate");
        }

        public void onInflate(Activity activity, Bundle bundle, Bundle bundle2) {
            throw new UnsupportedOperationException("onInflate not allowed on MapViewDelegate");
        }

        public void onLowMemory() {
            try {
                this.gF.onLowMemory();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onPause() {
            try {
                this.gF.onPause();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onResume() {
            try {
                this.gF.onResume();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onSaveInstanceState(Bundle bundle) {
            try {
                this.gF.onSaveInstanceState(bundle);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
    }

    static class C0287b extends C0074a<C0286a> {
        protected C0066d<C0286a> gC;
        private final ViewGroup gH;
        private final GoogleMapOptions gI;
        private final Context mContext;

        C0287b(ViewGroup viewGroup, Context context, GoogleMapOptions googleMapOptions) {
            this.gH = viewGroup;
            this.mContext = context;
            this.gI = googleMapOptions;
        }

        protected void mo1001a(C0066d<C0286a> c0066d) {
            this.gC = c0066d;
            bi();
        }

        public void bi() {
            if (this.gC != null && at() == null) {
                try {
                    this.gC.mo360a(new C0286a(this.gH, C0323p.m1313i(this.mContext).mo1095a(C0078c.m173f(this.mContext), this.gI)));
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                } catch (GooglePlayServicesNotAvailableException e2) {
                }
            }
        }
    }

    public MapView(Context context) {
        super(context);
        this.gD = new C0287b(this, context, null);
    }

    public MapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.gD = new C0287b(this, context, GoogleMapOptions.createFromAttributes(context, attributeSet));
    }

    public MapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.gD = new C0287b(this, context, GoogleMapOptions.createFromAttributes(context, attributeSet));
    }

    public MapView(Context context, GoogleMapOptions googleMapOptions) {
        super(context);
        this.gD = new C0287b(this, context, googleMapOptions);
    }

    public final GoogleMap getMap() {
        if (this.gz != null) {
            return this.gz;
        }
        this.gD.bi();
        if (this.gD.at() == null) {
            return null;
        }
        try {
            this.gz = new GoogleMap(((C0286a) this.gD.at()).bj().getMap());
            return this.gz;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void onCreate(Bundle bundle) {
        this.gD.onCreate(bundle);
        if (this.gD.at() == null) {
            this.gD.m169a((FrameLayout) this);
        }
    }

    public final void onDestroy() {
        this.gD.onDestroy();
    }

    public final void onLowMemory() {
        this.gD.onLowMemory();
    }

    public final void onPause() {
        this.gD.onPause();
    }

    public final void onResume() {
        this.gD.onResume();
    }

    public final void onSaveInstanceState(Bundle bundle) {
        this.gD.onSaveInstanceState(bundle);
    }
}
