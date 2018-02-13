package com.google.android.gms.maps.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.C0075b;
import com.google.android.gms.dynamic.C0075b.C0077a;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate.C0292a;
import com.google.android.gms.maps.internal.IMapFragmentDelegate.C0297a;
import com.google.android.gms.maps.internal.IMapViewDelegate.C0299a;
import com.google.android.gms.maps.model.internal.C0340a;
import com.google.android.gms.maps.model.internal.C0340a.C0342a;

public interface C0306c extends IInterface {

    public static abstract class C0308a extends Binder implements C0306c {

        private static class C0307a implements C0306c {
            private IBinder f104a;

            C0307a(IBinder iBinder) {
                this.f104a = iBinder;
            }

            public IMapViewDelegate mo1095a(C0075b c0075b, GoogleMapOptions googleMapOptions) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    obtain.writeStrongBinder(c0075b != null ? c0075b.asBinder() : null);
                    if (googleMapOptions != null) {
                        obtain.writeInt(1);
                        googleMapOptions.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f104a.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    IMapViewDelegate A = C0299a.m1285A(obtain2.readStrongBinder());
                    return A;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo1096a(C0075b c0075b, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    obtain.writeStrongBinder(c0075b != null ? c0075b.asBinder() : null);
                    obtain.writeInt(i);
                    this.f104a.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IBinder asBinder() {
                return this.f104a;
            }

            public ICameraUpdateFactoryDelegate bk() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    this.f104a.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    ICameraUpdateFactoryDelegate t = C0292a.m1282t(obtain2.readStrongBinder());
                    return t;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public C0340a bl() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    this.f104a.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    C0340a N = C0342a.m1374N(obtain2.readStrongBinder());
                    return N;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo1099c(C0075b c0075b) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    obtain.writeStrongBinder(c0075b != null ? c0075b.asBinder() : null);
                    this.f104a.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IMapFragmentDelegate mo1100d(C0075b c0075b) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.ICreator");
                    obtain.writeStrongBinder(c0075b != null ? c0075b.asBinder() : null);
                    this.f104a.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    IMapFragmentDelegate z = C0297a.m1284z(obtain2.readStrongBinder());
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static C0306c m1298v(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.ICreator");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof C0306c)) ? new C0307a(iBinder) : (C0306c) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            IBinder iBinder = null;
            switch (i) {
                case 1:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    mo1099c(C0077a.m171l(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    IMapFragmentDelegate d = mo1100d(C0077a.m171l(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    if (d != null) {
                        iBinder = d.asBinder();
                    }
                    parcel2.writeStrongBinder(iBinder);
                    return true;
                case 3:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    IMapViewDelegate a = mo1095a(C0077a.m171l(parcel.readStrongBinder()), parcel.readInt() != 0 ? GoogleMapOptions.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    if (a != null) {
                        iBinder = a.asBinder();
                    }
                    parcel2.writeStrongBinder(iBinder);
                    return true;
                case 4:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    ICameraUpdateFactoryDelegate bk = bk();
                    parcel2.writeNoException();
                    if (bk != null) {
                        iBinder = bk.asBinder();
                    }
                    parcel2.writeStrongBinder(iBinder);
                    return true;
                case 5:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    C0340a bl = bl();
                    parcel2.writeNoException();
                    if (bl != null) {
                        iBinder = bl.asBinder();
                    }
                    parcel2.writeStrongBinder(iBinder);
                    return true;
                case 6:
                    parcel.enforceInterface("com.google.android.gms.maps.internal.ICreator");
                    mo1096a(C0077a.m171l(parcel.readStrongBinder()), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.gms.maps.internal.ICreator");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    IMapViewDelegate mo1095a(C0075b c0075b, GoogleMapOptions googleMapOptions) throws RemoteException;

    void mo1096a(C0075b c0075b, int i) throws RemoteException;

    ICameraUpdateFactoryDelegate bk() throws RemoteException;

    C0340a bl() throws RemoteException;

    void mo1099c(C0075b c0075b) throws RemoteException;

    IMapFragmentDelegate mo1100d(C0075b c0075b) throws RemoteException;
}
