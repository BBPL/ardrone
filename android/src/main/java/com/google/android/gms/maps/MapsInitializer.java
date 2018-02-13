package com.google.android.gms.maps;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.internal.C0242s;
import com.google.android.gms.maps.internal.C0306c;
import com.google.android.gms.maps.internal.C0323p;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.RuntimeRemoteException;

public final class MapsInitializer {
    private MapsInitializer() {
    }

    public static void initialize(Context context) throws GooglePlayServicesNotAvailableException {
        C0242s.m1208d(context);
        C0306c i = C0323p.m1313i(context);
        try {
            CameraUpdateFactory.m1245a(i.bk());
            BitmapDescriptorFactory.m1317a(i.bl());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}
