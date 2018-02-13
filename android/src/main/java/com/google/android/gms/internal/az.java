package com.google.android.gms.internal;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import com.google.android.gms.common.data.C0051d;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.internal.ay.C0116a;

public interface az extends IInterface {

    public static abstract class C0148a extends Binder implements az {

        private static class C0147a implements az {
            private IBinder f63a;

            C0147a(IBinder iBinder) {
                this.f63a = iBinder;
            }

            public int mo541a(ay ayVar, byte[] bArr, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeByteArray(bArr);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.f63a.transact(5033, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo542a(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeLong(j);
                    this.f63a.transact(5001, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo543a(IBinder iBinder, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(iBinder);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f63a.transact(5005, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo544a(ay ayVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    this.f63a.transact(5002, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo545a(ay ayVar, int i, int i2, boolean z, boolean z2) throws RemoteException {
                int i3 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(z ? 1 : 0);
                    if (!z2) {
                        i3 = 0;
                    }
                    obtain.writeInt(i3);
                    this.f63a.transact(5044, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo546a(ay ayVar, int i, boolean z, boolean z2) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeInt(z ? 1 : 0);
                    if (!z2) {
                        i2 = 0;
                    }
                    obtain.writeInt(i2);
                    this.f63a.transact(5015, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo547a(ay ayVar, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeLong(j);
                    this.f63a.transact(5058, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo548a(ay ayVar, Bundle bundle, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.f63a.transact(5021, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo549a(ay ayVar, IBinder iBinder, int i, String[] strArr, Bundle bundle, boolean z, long j) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    obtain.writeStringArray(strArr);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    obtain.writeLong(j);
                    this.f63a.transact(5030, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo550a(ay ayVar, IBinder iBinder, String str, boolean z, long j) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeString(str);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    obtain.writeLong(j);
                    this.f63a.transact(5031, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo551a(ay ayVar, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    this.f63a.transact(5008, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo552a(ay ayVar, String str, int i, int i2, int i3, boolean z) throws RemoteException {
                int i4 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    if (z) {
                        i4 = 1;
                    }
                    obtain.writeInt(i4);
                    this.f63a.transact(5019, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo553a(ay ayVar, String str, int i, IBinder iBinder, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeStrongBinder(iBinder);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f63a.transact(5025, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo554a(ay ayVar, String str, int i, boolean z, boolean z2) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(z ? 1 : 0);
                    if (!z2) {
                        i2 = 0;
                    }
                    obtain.writeInt(i2);
                    this.f63a.transact(5045, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo555a(ay ayVar, String str, int i, boolean z, boolean z2, boolean z3, boolean z4) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(z ? 1 : 0);
                    obtain.writeInt(z2 ? 1 : 0);
                    obtain.writeInt(z3 ? 1 : 0);
                    if (!z4) {
                        i2 = 0;
                    }
                    obtain.writeInt(i2);
                    this.f63a.transact(6501, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo556a(ay ayVar, String str, long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    obtain.writeLong(j);
                    this.f63a.transact(5016, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo557a(ay ayVar, String str, IBinder iBinder, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iBinder);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f63a.transact(5023, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo558a(ay ayVar, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.f63a.transact(5009, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo559a(ay ayVar, String str, String str2, int i, int i2, int i3, boolean z) throws RemoteException {
                int i4 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    if (z) {
                        i4 = 1;
                    }
                    obtain.writeInt(i4);
                    this.f63a.transact(5039, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo560a(ay ayVar, String str, String str2, boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.f63a.transact(6002, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo561a(ay ayVar, String str, boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.f63a.transact(5054, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo562a(ay ayVar, String str, boolean z, long[] jArr) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    obtain.writeLongArray(jArr);
                    this.f63a.transact(5011, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo563a(ay ayVar, boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.f63a.transact(5063, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo564a(String str, String str2, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    this.f63a.transact(5051, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public C0051d aA() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    this.f63a.transact(5502, obtain, obtain2, 0);
                    obtain2.readException();
                    C0051d a = obtain2.readInt() != 0 ? C0051d.CREATOR.m56a(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return a;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IBinder asBinder() {
                return this.f63a;
            }

            public void ax() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    this.f63a.transact(5006, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public C0051d ay() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    this.f63a.transact(5013, obtain, obtain2, 0);
                    obtain2.readException();
                    C0051d a = obtain2.readInt() != 0 ? C0051d.CREATOR.m56a(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return a;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean az() throws RemoteException {
                boolean z = false;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    this.f63a.transact(5067, obtain, obtain2, 0);
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int mo569b(byte[] bArr, String str, String[] strArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeByteArray(bArr);
                    obtain.writeString(str);
                    obtain.writeStringArray(strArr);
                    this.f63a.transact(5034, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public ParcelFileDescriptor mo570b(Uri uri) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    if (uri != null) {
                        obtain.writeInt(1);
                        uri.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f63a.transact(6507, obtain, obtain2, 0);
                    obtain2.readException();
                    ParcelFileDescriptor parcelFileDescriptor = obtain2.readInt() != 0 ? (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return parcelFileDescriptor;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo571b(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeLong(j);
                    this.f63a.transact(5059, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo572b(ay ayVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    this.f63a.transact(5017, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo573b(ay ayVar, int i, boolean z, boolean z2) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeInt(z ? 1 : 0);
                    if (!z2) {
                        i2 = 0;
                    }
                    obtain.writeInt(i2);
                    this.f63a.transact(5046, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo574b(ay ayVar, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    this.f63a.transact(5010, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo575b(ay ayVar, String str, int i, int i2, int i3, boolean z) throws RemoteException {
                int i4 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    if (z) {
                        i4 = 1;
                    }
                    obtain.writeInt(i4);
                    this.f63a.transact(5020, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo576b(ay ayVar, String str, int i, boolean z, boolean z2) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(z ? 1 : 0);
                    if (!z2) {
                        i2 = 0;
                    }
                    obtain.writeInt(i2);
                    this.f63a.transact(5501, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo577b(ay ayVar, String str, IBinder iBinder, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iBinder);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.f63a.transact(5024, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo578b(ay ayVar, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.f63a.transact(5038, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo579b(ay ayVar, String str, String str2, int i, int i2, int i3, boolean z) throws RemoteException {
                int i4 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    if (z) {
                        i4 = 1;
                    }
                    obtain.writeInt(i4);
                    this.f63a.transact(5040, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo580b(ay ayVar, String str, String str2, boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.f63a.transact(6506, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo581b(ay ayVar, String str, boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.f63a.transact(6502, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo582b(ay ayVar, boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.f63a.transact(GamesClient.STATUS_MULTIPLAYER_ERROR_NOT_TRUSTED_TESTER, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo583c(ay ayVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    this.f63a.transact(5022, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo584c(ay ayVar, int i, boolean z, boolean z2) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeInt(z ? 1 : 0);
                    if (!z2) {
                        i2 = 0;
                    }
                    obtain.writeInt(i2);
                    this.f63a.transact(5048, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo585c(ay ayVar, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    this.f63a.transact(5014, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo586c(ay ayVar, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.f63a.transact(5041, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo587c(ay ayVar, String str, boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.f63a.transact(6504, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo588c(ay ayVar, boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.f63a.transact(6503, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void clearNotifications(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeInt(i);
                    this.f63a.transact(5036, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo590d(ay ayVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    this.f63a.transact(5026, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo591d(ay ayVar, int i, boolean z, boolean z2) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeInt(z ? 1 : 0);
                    if (!z2) {
                        i2 = 0;
                    }
                    obtain.writeInt(i2);
                    this.f63a.transact(6003, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo592d(ay ayVar, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    this.f63a.transact(5018, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo593d(ay ayVar, String str, boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.f63a.transact(6505, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo594e(ay ayVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    this.f63a.transact(5027, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo595e(ay ayVar, int i, boolean z, boolean z2) throws RemoteException {
                int i2 = 1;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeInt(z ? 1 : 0);
                    if (!z2) {
                        i2 = 0;
                    }
                    obtain.writeInt(i2);
                    this.f63a.transact(6004, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo596e(ay ayVar, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    this.f63a.transact(5032, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo597e(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.f63a.transact(5065, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo598f(ay ayVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    this.f63a.transact(5047, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo599f(ay ayVar, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    this.f63a.transact(5037, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo600g(ay ayVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    this.f63a.transact(5049, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo601g(ay ayVar, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    this.f63a.transact(5042, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getAppId() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    this.f63a.transact(5003, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getCurrentAccountName() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    this.f63a.transact(5007, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getCurrentPlayerId() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    this.f63a.transact(5012, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo605h(ay ayVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    this.f63a.transact(5056, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo606h(ay ayVar, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    this.f63a.transact(5043, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo607h(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.f63a.transact(5029, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo608i(ay ayVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    this.f63a.transact(5062, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo609i(ay ayVar, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    this.f63a.transact(5052, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo610i(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.f63a.transact(5028, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int mo611j(ay ayVar, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    this.f63a.transact(5053, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo612j(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.f63a.transact(5055, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo613k(ay ayVar, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    this.f63a.transact(5061, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo614l(ay ayVar, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    obtain.writeString(str);
                    this.f63a.transact(5057, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setUseNewPlayerNotificationsFirstParty(boolean z) throws RemoteException {
                int i = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    if (z) {
                        i = 1;
                    }
                    obtain.writeInt(i);
                    this.f63a.transact(5068, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String mo616u(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeString(str);
                    this.f63a.transact(5064, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String mo617v(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeString(str);
                    this.f63a.transact(5035, obtain, obtain2, 0);
                    obtain2.readException();
                    String readString = obtain2.readString();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void mo618w(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeString(str);
                    this.f63a.transact(5050, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int mo619x(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeString(str);
                    this.f63a.transact(5060, obtain, obtain2, 0);
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Uri mo620y(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    obtain.writeString(str);
                    this.f63a.transact(5066, obtain, obtain2, 0);
                    obtain2.readException();
                    Uri uri = obtain2.readInt() != 0 ? (Uri) Uri.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return uri;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle mo621z() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.games.internal.IGamesService");
                    this.f63a.transact(5004, obtain, obtain2, 0);
                    obtain2.readException();
                    Bundle bundle = obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return bundle;
                } catch (Throwable th) {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static az m746o(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.games.internal.IGamesService");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof az)) ? new C0147a(iBinder) : (az) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            Uri uri = null;
            boolean z = false;
            String appId;
            Bundle z2;
            C0051d ay;
            ay n;
            int readInt;
            boolean z3;
            String readString;
            IBinder readStrongBinder;
            int readInt2;
            int a;
            String readString2;
            int readInt3;
            int readInt4;
            ay n2;
            String readString3;
            switch (i) {
                case 5001:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo542a(parcel.readLong());
                    parcel2.writeNoException();
                    return true;
                case 5002:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo544a(C0116a.m386n(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 5003:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    appId = getAppId();
                    parcel2.writeNoException();
                    parcel2.writeString(appId);
                    return true;
                case 5004:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    z2 = mo621z();
                    parcel2.writeNoException();
                    if (z2 != null) {
                        parcel2.writeInt(1);
                        z2.writeToParcel(parcel2, 1);
                        return true;
                    }
                    parcel2.writeInt(0);
                    return true;
                case 5005:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    IBinder readStrongBinder2 = parcel.readStrongBinder();
                    if (parcel.readInt() != 0) {
                        z2 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                    }
                    mo543a(readStrongBinder2, z2);
                    parcel2.writeNoException();
                    return true;
                case 5006:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    ax();
                    parcel2.writeNoException();
                    return true;
                case 5007:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    appId = getCurrentAccountName();
                    parcel2.writeNoException();
                    parcel2.writeString(appId);
                    return true;
                case 5008:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo551a(C0116a.m386n(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5009:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo558a(C0116a.m386n(parcel.readStrongBinder()), parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5010:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo574b(C0116a.m386n(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5011:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo562a(C0116a.m386n(parcel.readStrongBinder()), parcel.readString(), parcel.readInt() != 0, parcel.createLongArray());
                    parcel2.writeNoException();
                    return true;
                case 5012:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    appId = getCurrentPlayerId();
                    parcel2.writeNoException();
                    parcel2.writeString(appId);
                    return true;
                case 5013:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    ay = ay();
                    parcel2.writeNoException();
                    if (ay != null) {
                        parcel2.writeInt(1);
                        ay.writeToParcel(parcel2, 1);
                        return true;
                    }
                    parcel2.writeInt(0);
                    return true;
                case 5014:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo585c(C0116a.m386n(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5015:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    n = C0116a.m386n(parcel.readStrongBinder());
                    readInt = parcel.readInt();
                    z3 = parcel.readInt() != 0;
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    mo546a(n, readInt, z3, z);
                    parcel2.writeNoException();
                    return true;
                case 5016:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo556a(C0116a.m386n(parcel.readStrongBinder()), parcel.readString(), parcel.readLong());
                    parcel2.writeNoException();
                    return true;
                case 5017:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo572b(C0116a.m386n(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 5018:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo592d(C0116a.m386n(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5019:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo552a(C0116a.m386n(parcel.readStrongBinder()), parcel.readString(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 5020:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo575b(C0116a.m386n(parcel.readStrongBinder()), parcel.readString(), parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 5021:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    n = C0116a.m386n(parcel.readStrongBinder());
                    if (parcel.readInt() != 0) {
                        z2 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                    }
                    mo548a(n, z2, parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 5022:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo583c(C0116a.m386n(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 5023:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    n = C0116a.m386n(parcel.readStrongBinder());
                    readString = parcel.readString();
                    readStrongBinder = parcel.readStrongBinder();
                    if (parcel.readInt() != 0) {
                        z2 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                    }
                    mo557a(n, readString, readStrongBinder, z2);
                    parcel2.writeNoException();
                    return true;
                case 5024:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    n = C0116a.m386n(parcel.readStrongBinder());
                    readString = parcel.readString();
                    readStrongBinder = parcel.readStrongBinder();
                    if (parcel.readInt() != 0) {
                        z2 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                    }
                    mo577b(n, readString, readStrongBinder, z2);
                    parcel2.writeNoException();
                    return true;
                case 5025:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo553a(C0116a.m386n(parcel.readStrongBinder()), parcel.readString(), parcel.readInt(), parcel.readStrongBinder(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 5026:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo590d(C0116a.m386n(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 5027:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo594e(C0116a.m386n(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 5028:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo610i(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 5029:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo607h(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 5030:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    ay n3 = C0116a.m386n(parcel.readStrongBinder());
                    readStrongBinder = parcel.readStrongBinder();
                    readInt2 = parcel.readInt();
                    String[] createStringArray = parcel.createStringArray();
                    Bundle bundle = parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null;
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    mo549a(n3, readStrongBinder, readInt2, createStringArray, bundle, z, parcel.readLong());
                    parcel2.writeNoException();
                    return true;
                case 5031:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo550a(C0116a.m386n(parcel.readStrongBinder()), parcel.readStrongBinder(), parcel.readString(), parcel.readInt() != 0, parcel.readLong());
                    parcel2.writeNoException();
                    return true;
                case 5032:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo596e(C0116a.m386n(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5033:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a = mo541a(C0116a.m386n(parcel.readStrongBinder()), parcel.createByteArray(), parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(a);
                    return true;
                case 5034:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a = mo569b(parcel.createByteArray(), parcel.readString(), parcel.createStringArray());
                    parcel2.writeNoException();
                    parcel2.writeInt(a);
                    return true;
                case 5035:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    appId = mo617v(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(appId);
                    return true;
                case 5036:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    clearNotifications(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 5037:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo599f(C0116a.m386n(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5038:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo578b(C0116a.m386n(parcel.readStrongBinder()), parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5039:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    n = C0116a.m386n(parcel.readStrongBinder());
                    readString = parcel.readString();
                    readString2 = parcel.readString();
                    readInt2 = parcel.readInt();
                    readInt3 = parcel.readInt();
                    readInt4 = parcel.readInt();
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    mo559a(n, readString, readString2, readInt2, readInt3, readInt4, z);
                    parcel2.writeNoException();
                    return true;
                case 5040:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    n = C0116a.m386n(parcel.readStrongBinder());
                    readString = parcel.readString();
                    readString2 = parcel.readString();
                    readInt2 = parcel.readInt();
                    readInt3 = parcel.readInt();
                    readInt4 = parcel.readInt();
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    mo579b(n, readString, readString2, readInt2, readInt3, readInt4, z);
                    parcel2.writeNoException();
                    return true;
                case 5041:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo586c(C0116a.m386n(parcel.readStrongBinder()), parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5042:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo601g(C0116a.m386n(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5043:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo606h(C0116a.m386n(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5044:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo545a(C0116a.m386n(parcel.readStrongBinder()), parcel.readInt(), parcel.readInt(), parcel.readInt() != 0, parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 5045:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo554a(C0116a.m386n(parcel.readStrongBinder()), parcel.readString(), parcel.readInt(), parcel.readInt() != 0, parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 5046:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    n = C0116a.m386n(parcel.readStrongBinder());
                    readInt = parcel.readInt();
                    z3 = parcel.readInt() != 0;
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    mo573b(n, readInt, z3, z);
                    parcel2.writeNoException();
                    return true;
                case 5047:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo598f(C0116a.m386n(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 5048:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    n = C0116a.m386n(parcel.readStrongBinder());
                    readInt = parcel.readInt();
                    z3 = parcel.readInt() != 0;
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    mo584c(n, readInt, z3, z);
                    parcel2.writeNoException();
                    return true;
                case 5049:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo600g(C0116a.m386n(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 5050:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo618w(parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5051:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo564a(parcel.readString(), parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 5052:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo609i(C0116a.m386n(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5053:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a = mo611j(C0116a.m386n(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(a);
                    return true;
                case 5054:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    n2 = C0116a.m386n(parcel.readStrongBinder());
                    readString3 = parcel.readString();
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    mo561a(n2, readString3, z);
                    parcel2.writeNoException();
                    return true;
                case 5055:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo612j(parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 5056:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo605h(C0116a.m386n(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 5057:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo614l(C0116a.m386n(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5058:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo547a(C0116a.m386n(parcel.readStrongBinder()), parcel.readLong());
                    parcel2.writeNoException();
                    return true;
                case 5059:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo571b(parcel.readLong());
                    parcel2.writeNoException();
                    return true;
                case 5060:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    a = mo619x(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(a);
                    return true;
                case 5061:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo613k(C0116a.m386n(parcel.readStrongBinder()), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5062:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo608i(C0116a.m386n(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 5063:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    n2 = C0116a.m386n(parcel.readStrongBinder());
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    mo563a(n2, z);
                    parcel2.writeNoException();
                    return true;
                case 5064:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    appId = mo616u(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(appId);
                    return true;
                case 5065:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo597e(parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 5066:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    uri = mo620y(parcel.readString());
                    parcel2.writeNoException();
                    if (uri != null) {
                        parcel2.writeInt(1);
                        uri.writeToParcel(parcel2, 1);
                        return true;
                    }
                    parcel2.writeInt(0);
                    return true;
                case 5067:
                    int i3;
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    z3 = az();
                    parcel2.writeNoException();
                    if (z3) {
                        i3 = 1;
                    }
                    parcel2.writeInt(i3);
                    return true;
                case 5068:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    setUseNewPlayerNotificationsFirstParty(z);
                    parcel2.writeNoException();
                    return true;
                case 5501:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    mo576b(C0116a.m386n(parcel.readStrongBinder()), parcel.readString(), parcel.readInt(), parcel.readInt() != 0, parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 5502:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    ay = aA();
                    parcel2.writeNoException();
                    if (ay != null) {
                        parcel2.writeInt(1);
                        ay.writeToParcel(parcel2, 1);
                        return true;
                    }
                    parcel2.writeInt(0);
                    return true;
                case GamesClient.STATUS_MULTIPLAYER_ERROR_NOT_TRUSTED_TESTER /*6001*/:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    n2 = C0116a.m386n(parcel.readStrongBinder());
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    mo582b(n2, z);
                    parcel2.writeNoException();
                    return true;
                case 6002:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    n2 = C0116a.m386n(parcel.readStrongBinder());
                    readString3 = parcel.readString();
                    readString = parcel.readString();
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    mo560a(n2, readString3, readString, z);
                    parcel2.writeNoException();
                    return true;
                case 6003:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    n = C0116a.m386n(parcel.readStrongBinder());
                    readInt = parcel.readInt();
                    z3 = parcel.readInt() != 0;
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    mo591d(n, readInt, z3, z);
                    parcel2.writeNoException();
                    return true;
                case 6004:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    n = C0116a.m386n(parcel.readStrongBinder());
                    readInt = parcel.readInt();
                    z3 = parcel.readInt() != 0;
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    mo595e(n, readInt, z3, z);
                    parcel2.writeNoException();
                    return true;
                case 6501:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    n = C0116a.m386n(parcel.readStrongBinder());
                    readString = parcel.readString();
                    int readInt5 = parcel.readInt();
                    boolean z4 = parcel.readInt() != 0;
                    boolean z5 = parcel.readInt() != 0;
                    boolean z6 = parcel.readInt() != 0;
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    mo555a(n, readString, readInt5, z4, z5, z6, z);
                    parcel2.writeNoException();
                    return true;
                case 6502:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    n2 = C0116a.m386n(parcel.readStrongBinder());
                    readString3 = parcel.readString();
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    mo581b(n2, readString3, z);
                    parcel2.writeNoException();
                    return true;
                case 6503:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    n2 = C0116a.m386n(parcel.readStrongBinder());
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    mo588c(n2, z);
                    parcel2.writeNoException();
                    return true;
                case 6504:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    n2 = C0116a.m386n(parcel.readStrongBinder());
                    readString3 = parcel.readString();
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    mo587c(n2, readString3, z);
                    parcel2.writeNoException();
                    return true;
                case 6505:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    n2 = C0116a.m386n(parcel.readStrongBinder());
                    readString3 = parcel.readString();
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    mo593d(n2, readString3, z);
                    parcel2.writeNoException();
                    return true;
                case 6506:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    n2 = C0116a.m386n(parcel.readStrongBinder());
                    readString3 = parcel.readString();
                    readString = parcel.readString();
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    mo580b(n2, readString3, readString, z);
                    parcel2.writeNoException();
                    return true;
                case 6507:
                    parcel.enforceInterface("com.google.android.gms.games.internal.IGamesService");
                    if (parcel.readInt() != 0) {
                        uri = (Uri) Uri.CREATOR.createFromParcel(parcel);
                    }
                    ParcelFileDescriptor b = mo570b(uri);
                    parcel2.writeNoException();
                    if (b != null) {
                        parcel2.writeInt(1);
                        b.writeToParcel(parcel2, 1);
                        return true;
                    }
                    parcel2.writeInt(0);
                    return true;
                case 1598968902:
                    parcel2.writeString("com.google.android.gms.games.internal.IGamesService");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    int mo541a(ay ayVar, byte[] bArr, String str, String str2) throws RemoteException;

    void mo542a(long j) throws RemoteException;

    void mo543a(IBinder iBinder, Bundle bundle) throws RemoteException;

    void mo544a(ay ayVar) throws RemoteException;

    void mo545a(ay ayVar, int i, int i2, boolean z, boolean z2) throws RemoteException;

    void mo546a(ay ayVar, int i, boolean z, boolean z2) throws RemoteException;

    void mo547a(ay ayVar, long j) throws RemoteException;

    void mo548a(ay ayVar, Bundle bundle, int i, int i2) throws RemoteException;

    void mo549a(ay ayVar, IBinder iBinder, int i, String[] strArr, Bundle bundle, boolean z, long j) throws RemoteException;

    void mo550a(ay ayVar, IBinder iBinder, String str, boolean z, long j) throws RemoteException;

    void mo551a(ay ayVar, String str) throws RemoteException;

    void mo552a(ay ayVar, String str, int i, int i2, int i3, boolean z) throws RemoteException;

    void mo553a(ay ayVar, String str, int i, IBinder iBinder, Bundle bundle) throws RemoteException;

    void mo554a(ay ayVar, String str, int i, boolean z, boolean z2) throws RemoteException;

    void mo555a(ay ayVar, String str, int i, boolean z, boolean z2, boolean z3, boolean z4) throws RemoteException;

    void mo556a(ay ayVar, String str, long j) throws RemoteException;

    void mo557a(ay ayVar, String str, IBinder iBinder, Bundle bundle) throws RemoteException;

    void mo558a(ay ayVar, String str, String str2) throws RemoteException;

    void mo559a(ay ayVar, String str, String str2, int i, int i2, int i3, boolean z) throws RemoteException;

    void mo560a(ay ayVar, String str, String str2, boolean z) throws RemoteException;

    void mo561a(ay ayVar, String str, boolean z) throws RemoteException;

    void mo562a(ay ayVar, String str, boolean z, long[] jArr) throws RemoteException;

    void mo563a(ay ayVar, boolean z) throws RemoteException;

    void mo564a(String str, String str2, int i) throws RemoteException;

    C0051d aA() throws RemoteException;

    void ax() throws RemoteException;

    C0051d ay() throws RemoteException;

    boolean az() throws RemoteException;

    int mo569b(byte[] bArr, String str, String[] strArr) throws RemoteException;

    ParcelFileDescriptor mo570b(Uri uri) throws RemoteException;

    void mo571b(long j) throws RemoteException;

    void mo572b(ay ayVar) throws RemoteException;

    void mo573b(ay ayVar, int i, boolean z, boolean z2) throws RemoteException;

    void mo574b(ay ayVar, String str) throws RemoteException;

    void mo575b(ay ayVar, String str, int i, int i2, int i3, boolean z) throws RemoteException;

    void mo576b(ay ayVar, String str, int i, boolean z, boolean z2) throws RemoteException;

    void mo577b(ay ayVar, String str, IBinder iBinder, Bundle bundle) throws RemoteException;

    void mo578b(ay ayVar, String str, String str2) throws RemoteException;

    void mo579b(ay ayVar, String str, String str2, int i, int i2, int i3, boolean z) throws RemoteException;

    void mo580b(ay ayVar, String str, String str2, boolean z) throws RemoteException;

    void mo581b(ay ayVar, String str, boolean z) throws RemoteException;

    void mo582b(ay ayVar, boolean z) throws RemoteException;

    void mo583c(ay ayVar) throws RemoteException;

    void mo584c(ay ayVar, int i, boolean z, boolean z2) throws RemoteException;

    void mo585c(ay ayVar, String str) throws RemoteException;

    void mo586c(ay ayVar, String str, String str2) throws RemoteException;

    void mo587c(ay ayVar, String str, boolean z) throws RemoteException;

    void mo588c(ay ayVar, boolean z) throws RemoteException;

    void clearNotifications(int i) throws RemoteException;

    void mo590d(ay ayVar) throws RemoteException;

    void mo591d(ay ayVar, int i, boolean z, boolean z2) throws RemoteException;

    void mo592d(ay ayVar, String str) throws RemoteException;

    void mo593d(ay ayVar, String str, boolean z) throws RemoteException;

    void mo594e(ay ayVar) throws RemoteException;

    void mo595e(ay ayVar, int i, boolean z, boolean z2) throws RemoteException;

    void mo596e(ay ayVar, String str) throws RemoteException;

    void mo597e(String str, String str2) throws RemoteException;

    void mo598f(ay ayVar) throws RemoteException;

    void mo599f(ay ayVar, String str) throws RemoteException;

    void mo600g(ay ayVar) throws RemoteException;

    void mo601g(ay ayVar, String str) throws RemoteException;

    String getAppId() throws RemoteException;

    String getCurrentAccountName() throws RemoteException;

    String getCurrentPlayerId() throws RemoteException;

    void mo605h(ay ayVar) throws RemoteException;

    void mo606h(ay ayVar, String str) throws RemoteException;

    void mo607h(String str, int i) throws RemoteException;

    void mo608i(ay ayVar) throws RemoteException;

    void mo609i(ay ayVar, String str) throws RemoteException;

    void mo610i(String str, int i) throws RemoteException;

    int mo611j(ay ayVar, String str) throws RemoteException;

    void mo612j(String str, int i) throws RemoteException;

    void mo613k(ay ayVar, String str) throws RemoteException;

    void mo614l(ay ayVar, String str) throws RemoteException;

    void setUseNewPlayerNotificationsFirstParty(boolean z) throws RemoteException;

    String mo616u(String str) throws RemoteException;

    String mo617v(String str) throws RemoteException;

    void mo618w(String str) throws RemoteException;

    int mo619x(String str) throws RemoteException;

    Uri mo620y(String str) throws RemoteException;

    Bundle mo621z() throws RemoteException;
}
