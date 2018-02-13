package com.google.android.youtube.player.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.youtube.player.internal.C0408j.C0410a.C0409a;
import com.google.android.youtube.player.internal.C0411k.C0413a;

public interface C0414l extends IInterface {

    public static abstract class C0416a extends Binder implements C0414l {

        private static final class C0415a implements C0414l {
            private IBinder f198a;

            C0415a(IBinder iBinder) {
                this.f198a = iBinder;
            }

            public final IBinder mo1313a() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.youtube.player.internal.IYouTubeService");
                    this.f198a.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    IBinder readStrongBinder = obtain2.readStrongBinder();
                    return readStrongBinder;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final C0411k mo1314a(C0408j c0408j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.youtube.player.internal.IYouTubeService");
                    obtain.writeStrongBinder(c0408j != null ? c0408j.asBinder() : null);
                    this.f198a.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    C0411k a = C0413a.m1632a(obtain2.readStrongBinder());
                    return a;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final void mo1315a(boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.youtube.player.internal.IYouTubeService");
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.f198a.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public final IBinder asBinder() {
                return this.f198a;
            }
        }

        public static C0414l m1639a(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.youtube.player.internal.IYouTubeService");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof C0414l)) ? new C0415a(iBinder) : (C0414l) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    parcel.enforceInterface("com.google.android.youtube.player.internal.IYouTubeService");
                    IBinder a = mo1313a();
                    parcel2.writeNoException();
                    parcel2.writeStrongBinder(a);
                    return true;
                case 2:
                    C0408j c0408j;
                    parcel.enforceInterface("com.google.android.youtube.player.internal.IYouTubeService");
                    IBinder readStrongBinder = parcel.readStrongBinder();
                    if (readStrongBinder == null) {
                        c0408j = null;
                    } else {
                        IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.youtube.player.internal.IThumbnailLoaderClient");
                        c0408j = (queryLocalInterface == null || !(queryLocalInterface instanceof C0408j)) ? new C0409a(readStrongBinder) : (C0408j) queryLocalInterface;
                    }
                    C0411k a2 = mo1314a(c0408j);
                    parcel2.writeNoException();
                    parcel2.writeStrongBinder(a2 != null ? a2.asBinder() : null);
                    return true;
                case 3:
                    parcel.enforceInterface("com.google.android.youtube.player.internal.IYouTubeService");
                    mo1315a(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.youtube.player.internal.IYouTubeService");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    IBinder mo1313a() throws RemoteException;

    C0411k mo1314a(C0408j c0408j) throws RemoteException;

    void mo1315a(boolean z) throws RemoteException;
}
