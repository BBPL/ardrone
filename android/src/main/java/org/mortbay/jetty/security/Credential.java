package org.mortbay.jetty.security;

import java.security.MessageDigest;
import org.mortbay.log.Log;
import org.mortbay.util.StringUtil;
import org.mortbay.util.TypeUtil;

public abstract class Credential {

    public static class Crypt extends Credential {
        public static final String __TYPE = "CRYPT:";
        private String _cooked;

        Crypt(String str) {
            if (str.startsWith(__TYPE)) {
                str = str.substring(__TYPE.length());
            }
            this._cooked = str;
        }

        public static String crypt(String str, String str2) {
            return new StringBuffer().append(__TYPE).append(UnixCrypt.crypt(str2, str)).toString();
        }

        public boolean check(Object obj) {
            if (!((obj instanceof String) || (obj instanceof Password))) {
                Log.warn(new StringBuffer().append("Can't check ").append(obj.getClass()).append(" against CRYPT").toString());
            }
            return this._cooked.equals(UnixCrypt.crypt(obj.toString(), this._cooked));
        }
    }

    public static class MD5 extends Credential {
        public static final String __TYPE = "MD5:";
        private static MessageDigest __md;
        public static final Object __md5Lock = new Object();
        private byte[] _digest;

        MD5(String str) {
            if (str.startsWith(__TYPE)) {
                str = str.substring(__TYPE.length());
            }
            this._digest = TypeUtil.parseBytes(str, 16);
        }

        public static String digest(String str) {
            String str2 = null;
            try {
                byte[] digest;
                synchronized (__md5Lock) {
                    if (__md == null) {
                        try {
                            __md = MessageDigest.getInstance("MD5");
                        } catch (Throwable e) {
                            Log.warn(e);
                        }
                    }
                    __md.reset();
                    __md.update(str.getBytes(StringUtil.__ISO_8859_1));
                    digest = __md.digest();
                }
                str2 = new StringBuffer().append(__TYPE).append(TypeUtil.toString(digest, 16)).toString();
            } catch (Throwable e2) {
                Log.warn(e2);
            }
            return str2;
        }

        public boolean check(Object obj) {
            try {
                int i;
                if ((obj instanceof Password) || (obj instanceof String)) {
                    byte[] digest;
                    synchronized (__md5Lock) {
                        if (__md == null) {
                            __md = MessageDigest.getInstance("MD5");
                        }
                        __md.reset();
                        __md.update(obj.toString().getBytes(StringUtil.__ISO_8859_1));
                        digest = __md.digest();
                    }
                    if (digest != null) {
                        if (digest.length == this._digest.length) {
                            i = 0;
                            while (i < digest.length) {
                                if (digest[i] == this._digest[i]) {
                                    i++;
                                }
                            }
                            return true;
                        }
                    }
                } else if (obj instanceof MD5) {
                    MD5 md5 = (MD5) obj;
                    if (this._digest.length == md5._digest.length) {
                        i = 0;
                        while (i < this._digest.length) {
                            if (this._digest[i] == md5._digest[i]) {
                                i++;
                            }
                        }
                        return true;
                    }
                } else if (obj instanceof Credential) {
                    return ((Credential) obj).check(this);
                } else {
                    Log.warn(new StringBuffer().append("Can't check ").append(obj.getClass()).append(" against MD5").toString());
                    return false;
                }
                return false;
            } catch (Throwable e) {
                Log.warn(e);
                return false;
            }
        }

        public byte[] getDigest() {
            return this._digest;
        }
    }

    public static Credential getCredential(String str) {
        return str.startsWith(Crypt.__TYPE) ? new Crypt(str) : str.startsWith(MD5.__TYPE) ? new MD5(str) : new Password(str);
    }

    public abstract boolean check(Object obj);
}
