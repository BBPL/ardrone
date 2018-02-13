package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.C0075b;
import com.google.android.gms.dynamic.C0075b.C0077a;

public interface C0236q extends IInterface {

    public static abstract class C0238a extends Binder implements C0236q {

        private static class C0237a implements C0236q {
            private IBinder f94a;

            C0237a(IBinder iBinder) {
                this.f94a = iBinder;
            }

            public C0075b mo972a(C0075b c0075b, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.common.internal.ISignInButtonCreator");
                    obtain.writeStrongBinder(c0075b != null ? c0075b.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.f94a.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    C0075b l = C0077a.m171l(obtain2.readStrongBinder());
                    return l;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IBinder asBinder() {
                return this.f94a;
            }
        }

        public static C0236q m1198i(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.ISignInButtonCreator");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof C0236q)) ? new C0237a(iBinder) : (C0236q) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    parcel.enforceInterface("com.google.android.gms.common.internal.ISignInButtonCreator");
                    C0075b a = mo972a(C0077a.m171l(parcel.readStrongBinder()), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeStrongBinder(a != null ? a.asBinder() : null);
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.gms.common.internal.ISignInButtonCreator");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    C0075b mo972a(C0075b c0075b, int i, int i2) throws RemoteException;
}
