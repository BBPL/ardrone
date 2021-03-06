package com.google.android.gms.maps;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.dynamic.C0066d;
import com.google.android.gms.dynamic.C0074a;
import com.google.android.gms.dynamic.C0078c;
import com.google.android.gms.dynamic.LifecycleDelegate;
import com.google.android.gms.internal.C0242s;
import com.google.android.gms.maps.internal.C0322o;
import com.google.android.gms.maps.internal.C0323p;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.internal.IMapFragmentDelegate;
import com.google.android.gms.maps.model.RuntimeRemoteException;

public class SupportMapFragment extends Fragment {
    private final C0289b gK = new C0289b(this);
    private GoogleMap gz;

    static class C0288a implements LifecycleDelegate {
        private final IMapFragmentDelegate gB;
        private final Fragment gL;

        public C0288a(Fragment fragment, IMapFragmentDelegate iMapFragmentDelegate) {
            this.gB = (IMapFragmentDelegate) C0242s.m1208d(iMapFragmentDelegate);
            this.gL = (Fragment) C0242s.m1208d(fragment);
        }

        public IMapFragmentDelegate bh() {
            return this.gB;
        }

        public void onCreate(Bundle bundle) {
            if (bundle == null) {
                try {
                    bundle = new Bundle();
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                }
            }
            Bundle arguments = this.gL.getArguments();
            if (arguments != null && arguments.containsKey("MapOptions")) {
                C0322o.m1310a(bundle, "MapOptions", arguments.getParcelable("MapOptions"));
            }
            this.gB.onCreate(bundle);
        }

        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            try {
                return (View) C0078c.m172a(this.gB.onCreateView(C0078c.m173f(layoutInflater), C0078c.m173f(viewGroup), bundle));
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onDestroy() {
            try {
                this.gB.onDestroy();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onDestroyView() {
            try {
                this.gB.onDestroyView();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onInflate(Activity activity, Bundle bundle, Bundle bundle2) {
            try {
                this.gB.onInflate(C0078c.m173f(activity), (GoogleMapOptions) bundle.getParcelable("MapOptions"), bundle2);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onLowMemory() {
            try {
                this.gB.onLowMemory();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onPause() {
            try {
                this.gB.onPause();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onResume() {
            try {
                this.gB.onResume();
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }

        public void onSaveInstanceState(Bundle bundle) {
            try {
                this.gB.onSaveInstanceState(bundle);
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
    }

    static class C0289b extends C0074a<C0288a> {
        private Activity bm;
        protected C0066d<C0288a> gC;
        private final Fragment gL;

        C0289b(Fragment fragment) {
            this.gL = fragment;
        }

        private void setActivity(Activity activity) {
            this.bm = activity;
            bi();
        }

        protected void mo1001a(C0066d<C0288a> c0066d) {
            this.gC = c0066d;
            bi();
        }

        public void bi() {
            if (this.bm != null && this.gC != null && at() == null) {
                try {
                    MapsInitializer.initialize(this.bm);
                    this.gC.mo360a(new C0288a(this.gL, C0323p.m1313i(this.bm).mo1100d(C0078c.m173f(this.bm))));
                } catch (RemoteException e) {
                    throw new RuntimeRemoteException(e);
                } catch (GooglePlayServicesNotAvailableException e2) {
                }
            }
        }
    }

    public static SupportMapFragment newInstance() {
        return new SupportMapFragment();
    }

    public static SupportMapFragment newInstance(GoogleMapOptions googleMapOptions) {
        SupportMapFragment supportMapFragment = new SupportMapFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("MapOptions", googleMapOptions);
        supportMapFragment.setArguments(bundle);
        return supportMapFragment;
    }

    protected IMapFragmentDelegate bh() {
        this.gK.bi();
        return this.gK.at() == null ? null : ((C0288a) this.gK.at()).bh();
    }

    public final GoogleMap getMap() {
        IMapFragmentDelegate bh = bh();
        if (bh != null) {
            try {
                IGoogleMapDelegate map = bh.getMap();
                if (map != null) {
                    if (this.gz == null || this.gz.aY().asBinder() != map.asBinder()) {
                        this.gz = new GoogleMap(map);
                    }
                    return this.gz;
                }
            } catch (RemoteException e) {
                throw new RuntimeRemoteException(e);
            }
        }
        return null;
    }

    public void onActivityCreated(Bundle bundle) {
        if (bundle != null) {
            bundle.setClassLoader(SupportMapFragment.class.getClassLoader());
        }
        super.onActivityCreated(bundle);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.gK.setActivity(activity);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.gK.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return this.gK.onCreateView(layoutInflater, viewGroup, bundle);
    }

    public void onDestroy() {
        this.gK.onDestroy();
        super.onDestroy();
    }

    public void onDestroyView() {
        this.gK.onDestroyView();
        super.onDestroyView();
    }

    public void onInflate(Activity activity, AttributeSet attributeSet, Bundle bundle) {
        super.onInflate(activity, attributeSet, bundle);
        this.gK.setActivity(activity);
        Parcelable createFromAttributes = GoogleMapOptions.createFromAttributes(activity, attributeSet);
        Bundle bundle2 = new Bundle();
        bundle2.putParcelable("MapOptions", createFromAttributes);
        this.gK.onInflate(activity, bundle2, bundle);
    }

    public void onLowMemory() {
        this.gK.onLowMemory();
        super.onLowMemory();
    }

    public void onPause() {
        this.gK.onPause();
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        this.gK.onResume();
    }

    public void onSaveInstanceState(Bundle bundle) {
        if (bundle != null) {
            bundle.setClassLoader(SupportMapFragment.class.getClassLoader());
        }
        super.onSaveInstanceState(bundle);
        this.gK.onSaveInstanceState(bundle);
    }

    public void setArguments(Bundle bundle) {
        super.setArguments(bundle);
    }
}
