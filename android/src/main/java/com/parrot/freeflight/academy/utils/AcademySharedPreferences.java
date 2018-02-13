package com.parrot.freeflight.academy.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.provider.Settings.Secure;
import android.util.Base64;
import java.security.Key;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

@TargetApi(11)
public class AcademySharedPreferences implements SharedPreferences {
    private static final char[] SEKRIT = "ardroneparrotandroid".toCharArray();
    protected static final String UTF8 = "utf-8";
    protected Context context;
    protected SharedPreferences delegate;

    public class Editor implements android.content.SharedPreferences.Editor {
        protected android.content.SharedPreferences.Editor delegate;

        public Editor() {
            this.delegate = AcademySharedPreferences.this.delegate.edit();
        }

        public void apply() {
            this.delegate.apply();
        }

        public Editor clear() {
            this.delegate.clear();
            return this;
        }

        public boolean commit() {
            return this.delegate.commit();
        }

        public Editor putBoolean(String str, boolean z) {
            this.delegate.putString(str, AcademySharedPreferences.this.encrypt(Boolean.toString(z)));
            return this;
        }

        public Editor putFloat(String str, float f) {
            this.delegate.putString(str, AcademySharedPreferences.this.encrypt(Float.toString(f)));
            return this;
        }

        public Editor putInt(String str, int i) {
            this.delegate.putString(str, AcademySharedPreferences.this.encrypt(Integer.toString(i)));
            return this;
        }

        public Editor putLong(String str, long j) {
            this.delegate.putString(str, AcademySharedPreferences.this.encrypt(Long.toString(j)));
            return this;
        }

        public Editor putString(String str, String str2) {
            this.delegate.putString(str, AcademySharedPreferences.this.encrypt(str2));
            return this;
        }

        public android.content.SharedPreferences.Editor putStringSet(String str, Set<String> set) {
            Set hashSet = new HashSet();
            for (String encrypt : set) {
                hashSet.add(AcademySharedPreferences.this.encrypt(encrypt));
            }
            this.delegate.putStringSet(str, hashSet);
            return this;
        }

        public Editor remove(String str) {
            this.delegate.remove(str);
            return this;
        }
    }

    public AcademySharedPreferences(Context context, SharedPreferences sharedPreferences) {
        this.delegate = sharedPreferences;
        this.context = context;
    }

    public boolean contains(String str) {
        return this.delegate.contains(str);
    }

    protected String decrypt(String str) {
        byte[] decode;
        if (str != null) {
            try {
                decode = Base64.decode(str, 0);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        decode = new byte[0];
        Key generateSecret = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(new PBEKeySpec(SEKRIT));
        Cipher instance = Cipher.getInstance("PBEWithMD5AndDES");
        instance.init(2, generateSecret, new PBEParameterSpec(Secure.getString(this.context.getContentResolver(), "android_id").getBytes(UTF8), 20));
        return new String(instance.doFinal(decode), UTF8);
    }

    public Editor edit() {
        return new Editor();
    }

    protected String encrypt(String str) {
        byte[] bytes;
        if (str != null) {
            try {
                bytes = str.getBytes(UTF8);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        bytes = new byte[0];
        Key generateSecret = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(new PBEKeySpec(SEKRIT));
        Cipher instance = Cipher.getInstance("PBEWithMD5AndDES");
        instance.init(1, generateSecret, new PBEParameterSpec(Secure.getString(this.context.getContentResolver(), "android_id").getBytes(UTF8), 20));
        return new String(Base64.encode(instance.doFinal(bytes), 2), UTF8);
    }

    public Map<String, ?> getAll() {
        throw new UnsupportedOperationException();
    }

    public boolean getBoolean(String str, boolean z) {
        String string = this.delegate.getString(str, null);
        return string != null ? Boolean.parseBoolean(decrypt(string)) : z;
    }

    public float getFloat(String str, float f) {
        String string = this.delegate.getString(str, null);
        return string != null ? Float.parseFloat(decrypt(string)) : f;
    }

    public int getInt(String str, int i) {
        String string = this.delegate.getString(str, null);
        return string != null ? Integer.parseInt(decrypt(string)) : i;
    }

    public long getLong(String str, long j) {
        String string = this.delegate.getString(str, null);
        return string != null ? Long.parseLong(decrypt(string)) : j;
    }

    public String getString(String str, String str2) {
        String string = this.delegate.getString(str, null);
        return string != null ? decrypt(string) : str2;
    }

    public Set<String> getStringSet(String str, Set<String> set) {
        Set<String> stringSet = this.delegate.getStringSet(str, null);
        for (String decrypt : stringSet) {
            decrypt(decrypt);
        }
        return stringSet != null ? stringSet : set;
    }

    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        this.delegate.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        this.delegate.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }
}
