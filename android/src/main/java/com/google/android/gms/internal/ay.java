package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.data.C0051d;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;

public interface ay extends IInterface {

    public static abstract class C0116a extends Binder implements ay {

        private static class C0146a implements ay {
            private IBinder f62a;

            C0146a(IBinder iBinder) {
                this.f62a = iBinder;
            }

            public void mo481B(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    obtain.writeInt(i);
                    this.f62a.transact(5013, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo482C(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    obtain.writeInt(i);
                    this.f62a.transact(5015, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo483D(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    obtain.writeInt(i);
                    this.f62a.transact(5036, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo484E(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    obtain.writeInt(i);
                    this.f62a.transact(5040, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo485a(int i, int i2, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    this.f62a.transact(5033, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo486a(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.f62a.transact(5001, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo487a(int i, String str, boolean z) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    this.f62a.transact(5034, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo488a(C0051d c0051d, C0051d c0051d2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (c0051d2 != null) {
                        obtain.writeInt(1);
                        c0051d2.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f62a.transact(5005, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo489a(C0051d c0051d, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStringArray(strArr);
                    this.f62a.transact(5026, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IBinder asBinder() {
                return this.f62a;
            }

            public void mo490b(C0051d c0051d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f62a.transact(5002, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo491b(C0051d c0051d, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStringArray(strArr);
                    this.f62a.transact(5027, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo492c(C0051d c0051d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f62a.transact(5004, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo493c(C0051d c0051d, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStringArray(strArr);
                    this.f62a.transact(5028, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo494d(C0051d c0051d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f62a.transact(5006, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo495d(C0051d c0051d, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStringArray(strArr);
                    this.f62a.transact(5029, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo496e(C0051d c0051d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f62a.transact(5007, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo497e(C0051d c0051d, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStringArray(strArr);
                    this.f62a.transact(5030, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo498f(C0051d c0051d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f62a.transact(5008, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo499f(C0051d c0051d, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStringArray(strArr);
                    this.f62a.transact(5031, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo500g(C0051d c0051d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f62a.transact(5009, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo501h(C0051d c0051d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f62a.transact(5010, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo502i(C0051d c0051d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f62a.transact(5011, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo503j(C0051d c0051d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f62a.transact(5017, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo504k(C0051d c0051d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f62a.transact(5037, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo505l(C0051d c0051d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f62a.transact(5012, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo506m(C0051d c0051d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f62a.transact(5014, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo507n(C0051d c0051d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f62a.transact(5018, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo508o(C0051d c0051d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f62a.transact(5019, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onAchievementUpdated(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.f62a.transact(5003, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onLeftRoom(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.f62a.transact(5020, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onP2PConnected(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    obtain.writeString(str);
                    this.f62a.transact(GamesClient.STATUS_MULTIPLAYER_ERROR_NOT_TRUSTED_TESTER, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onP2PDisconnected(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    obtain.writeString(str);
                    this.f62a.transact(6002, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onRealTimeMessageReceived(RealTimeMessage realTimeMessage) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (realTimeMessage != null) {
                        obtain.writeInt(1);
                        realTimeMessage.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f62a.transact(5032, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onSignOutComplete() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    this.f62a.transact(5016, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo515p(C0051d c0051d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f62a.transact(5021, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo516q(C0051d c0051d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f62a.transact(5022, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo517r(C0051d c0051d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f62a.transact(5023, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo518s(C0051d c0051d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f62a.transact(5024, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo519t(C0051d c0051d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f62a.transact(5025, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo520u(C0051d c0051d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f62a.transact(5038, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo521v(C0051d c0051d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f62a.transact(5035, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo522w(C0051d c0051d) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (c0051d != null) {
                        obtain.writeInt(1);
                        c0051d.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f62a.transact(5039, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public C0116a() {
            attachInterface(this, "com.google.android.gms.games.internal.IGamesCallbacks");
        }

        public static ay m386n(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.games.internal.IGamesCallbacks");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ay)) ? new C0146a(iBinder) : (ay) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            C0051d c0051d = null;
            switch (i) {
                case 5001:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    mo486a(parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5002:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo490b(c0051d);
                    parcel2.writeNoException();
                    return true;
                case 5003:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    onAchievementUpdated(parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5004:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo492c(c0051d);
                    parcel2.writeNoException();
                    return true;
                case 5005:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    C0051d a = parcel.readInt() != 0 ? C0051d.CREATOR.m56a(parcel) : null;
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo488a(a, c0051d);
                    parcel2.writeNoException();
                    return true;
                case 5006:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo494d(c0051d);
                    parcel2.writeNoException();
                    return true;
                case 5007:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo496e(c0051d);
                    parcel2.writeNoException();
                    return true;
                case 5008:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo498f(c0051d);
                    parcel2.writeNoException();
                    return true;
                case 5009:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo500g(c0051d);
                    parcel2.writeNoException();
                    return true;
                case 5010:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo501h(c0051d);
                    parcel2.writeNoException();
                    return true;
                case 5011:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo502i(c0051d);
                    parcel2.writeNoException();
                    return true;
                case 5012:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo505l(c0051d);
                    parcel2.writeNoException();
                    return true;
                case 5013:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    mo481B(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 5014:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo506m(c0051d);
                    parcel2.writeNoException();
                    return true;
                case 5015:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    mo482C(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 5016:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    onSignOutComplete();
                    parcel2.writeNoException();
                    return true;
                case 5017:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo503j(c0051d);
                    parcel2.writeNoException();
                    return true;
                case 5018:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo507n(c0051d);
                    parcel2.writeNoException();
                    return true;
                case 5019:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo508o(c0051d);
                    parcel2.writeNoException();
                    return true;
                case 5020:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    onLeftRoom(parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5021:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo515p(c0051d);
                    parcel2.writeNoException();
                    return true;
                case 5022:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo516q(c0051d);
                    parcel2.writeNoException();
                    return true;
                case 5023:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo517r(c0051d);
                    parcel2.writeNoException();
                    return true;
                case 5024:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo518s(c0051d);
                    parcel2.writeNoException();
                    return true;
                case 5025:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo519t(c0051d);
                    parcel2.writeNoException();
                    return true;
                case 5026:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo489a(c0051d, parcel.createStringArray());
                    parcel2.writeNoException();
                    return true;
                case 5027:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo491b(c0051d, parcel.createStringArray());
                    parcel2.writeNoException();
                    return true;
                case 5028:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo493c(c0051d, parcel.createStringArray());
                    parcel2.writeNoException();
                    return true;
                case 5029:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo495d(c0051d, parcel.createStringArray());
                    parcel2.writeNoException();
                    return true;
                case 5030:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo497e(c0051d, parcel.createStringArray());
                    parcel2.writeNoException();
                    return true;
                case 5031:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo499f(c0051d, parcel.createStringArray());
                    parcel2.writeNoException();
                    return true;
                case 5032:
                    RealTimeMessage realTimeMessage;
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        realTimeMessage = (RealTimeMessage) RealTimeMessage.CREATOR.createFromParcel(parcel);
                    }
                    onRealTimeMessageReceived(realTimeMessage);
                    parcel2.writeNoException();
                    return true;
                case 5033:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    mo485a(parcel.readInt(), parcel.readInt(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5034:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    mo487a(parcel.readInt(), parcel.readString(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 5035:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo521v(c0051d);
                    parcel2.writeNoException();
                    return true;
                case 5036:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    mo483D(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 5037:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo504k(c0051d);
                    parcel2.writeNoException();
                    return true;
                case 5038:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo520u(c0051d);
                    parcel2.writeNoException();
                    return true;
                case 5039:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    if (parcel.readInt() != 0) {
                        c0051d = C0051d.CREATOR.m56a(parcel);
                    }
                    mo522w(c0051d);
                    parcel2.writeNoException();
                    return true;
                case 5040:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    mo484E(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case GamesClient.STATUS_MULTIPLAYER_ERROR_NOT_TRUSTED_TESTER /*6001*/:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    onP2PConnected(parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 6002:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesCallbacks");
                    onP2PDisconnected(parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.gms.games.internal.IGamesCallbacks");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void mo481B(int i) throws RemoteException;

    void mo482C(int i) throws RemoteException;

    void mo483D(int i) throws RemoteException;

    void mo484E(int i) throws RemoteException;

    void mo485a(int i, int i2, String str) throws RemoteException;

    void mo486a(int i, String str) throws RemoteException;

    void mo487a(int i, String str, boolean z) throws RemoteException;

    void mo488a(C0051d c0051d, C0051d c0051d2) throws RemoteException;

    void mo489a(C0051d c0051d, String[] strArr) throws RemoteException;

    void mo490b(C0051d c0051d) throws RemoteException;

    void mo491b(C0051d c0051d, String[] strArr) throws RemoteException;

    void mo492c(C0051d c0051d) throws RemoteException;

    void mo493c(C0051d c0051d, String[] strArr) throws RemoteException;

    void mo494d(C0051d c0051d) throws RemoteException;

    void mo495d(C0051d c0051d, String[] strArr) throws RemoteException;

    void mo496e(C0051d c0051d) throws RemoteException;

    void mo497e(C0051d c0051d, String[] strArr) throws RemoteException;

    void mo498f(C0051d c0051d) throws RemoteException;

    void mo499f(C0051d c0051d, String[] strArr) throws RemoteException;

    void mo500g(C0051d c0051d) throws RemoteException;

    void mo501h(C0051d c0051d) throws RemoteException;

    void mo502i(C0051d c0051d) throws RemoteException;

    void mo503j(C0051d c0051d) throws RemoteException;

    void mo504k(C0051d c0051d) throws RemoteException;

    void mo505l(C0051d c0051d) throws RemoteException;

    void mo506m(C0051d c0051d) throws RemoteException;

    void mo507n(C0051d c0051d) throws RemoteException;

    void mo508o(C0051d c0051d) throws RemoteException;

    void onAchievementUpdated(int i, String str) throws RemoteException;

    void onLeftRoom(int i, String str) throws RemoteException;

    void onP2PConnected(String str) throws RemoteException;

    void onP2PDisconnected(String str) throws RemoteException;

    void onRealTimeMessageReceived(RealTimeMessage realTimeMessage) throws RemoteException;

    void onSignOutComplete() throws RemoteException;

    void mo515p(C0051d c0051d) throws RemoteException;

    void mo516q(C0051d c0051d) throws RemoteException;

    void mo517r(C0051d c0051d) throws RemoteException;

    void mo518s(C0051d c0051d) throws RemoteException;

    void mo519t(C0051d c0051d) throws RemoteException;

    void mo520u(C0051d c0051d) throws RemoteException;

    void mo521v(C0051d c0051d) throws RemoteException;

    void mo522w(C0051d c0051d) throws RemoteException;
}
