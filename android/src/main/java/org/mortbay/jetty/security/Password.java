package org.mortbay.jetty.security;

import org.mortbay.jetty.security.Credential.Crypt;
import org.mortbay.jetty.security.Credential.MD5;

public class Password extends Credential {
    public static final String __OBFUSCATE = "OBF:";
    private String _pw;

    public Password(String str) {
        this._pw = str;
        while (this._pw != null && this._pw.startsWith(__OBFUSCATE)) {
            this._pw = deobfuscate(this._pw);
        }
    }

    public static String deobfuscate(String str) {
        if (str.startsWith(__OBFUSCATE)) {
            str = str.substring(4);
        }
        byte[] bArr = new byte[(str.length() / 2)];
        int i = 0;
        int i2 = 0;
        while (i < str.length()) {
            int parseInt = Integer.parseInt(str.substring(i, i + 4), 36);
            bArr[i2] = (byte) ((((parseInt % 256) + (parseInt / 256)) - 254) / 2);
            i += 4;
            i2++;
        }
        return new String(bArr, 0, i2);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static org.mortbay.jetty.security.Password getPassword(java.lang.String r5, java.lang.String r6, java.lang.String r7) {
        /*
        r0 = java.lang.System.getProperty(r5, r6);
        if (r0 == 0) goto L_0x000c;
    L_0x0006:
        r1 = r0.length();
        if (r1 != 0) goto L_0x0065;
    L_0x000c:
        r2 = java.lang.System.out;	 Catch:{ IOException -> 0x005e }
        r1 = new java.lang.StringBuffer;	 Catch:{ IOException -> 0x005e }
        r1.<init>();	 Catch:{ IOException -> 0x005e }
        r3 = r1.append(r5);	 Catch:{ IOException -> 0x005e }
        if (r7 == 0) goto L_0x005b;
    L_0x0019:
        r1 = r7.length();	 Catch:{ IOException -> 0x005e }
        if (r1 <= 0) goto L_0x005b;
    L_0x001f:
        r1 = " [dft]";
    L_0x0021:
        r1 = r3.append(r1);	 Catch:{ IOException -> 0x005e }
        r3 = " : ";
        r1 = r1.append(r3);	 Catch:{ IOException -> 0x005e }
        r1 = r1.toString();	 Catch:{ IOException -> 0x005e }
        r2.print(r1);	 Catch:{ IOException -> 0x005e }
        r1 = java.lang.System.out;	 Catch:{ IOException -> 0x005e }
        r1.flush();	 Catch:{ IOException -> 0x005e }
        r1 = 512; // 0x200 float:7.175E-43 double:2.53E-321;
        r1 = new byte[r1];	 Catch:{ IOException -> 0x005e }
        r2 = java.lang.System.in;	 Catch:{ IOException -> 0x005e }
        r2 = r2.read(r1);	 Catch:{ IOException -> 0x005e }
        if (r2 <= 0) goto L_0x004d;
    L_0x0043:
        r3 = new java.lang.String;	 Catch:{ IOException -> 0x005e }
        r4 = 0;
        r3.<init>(r1, r4, r2);	 Catch:{ IOException -> 0x005e }
        r0 = r3.trim();	 Catch:{ IOException -> 0x005e }
    L_0x004d:
        if (r0 == 0) goto L_0x0055;
    L_0x004f:
        r1 = r0.length();
        if (r1 != 0) goto L_0x0065;
    L_0x0055:
        r0 = new org.mortbay.jetty.security.Password;
        r0.<init>(r7);
        return r0;
    L_0x005b:
        r1 = "";
        goto L_0x0021;
    L_0x005e:
        r1 = move-exception;
        r2 = "EXCEPTION ";
        org.mortbay.log.Log.warn(r2, r1);
        goto L_0x004d;
    L_0x0065:
        r7 = r0;
        goto L_0x0055;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.jetty.security.Password.getPassword(java.lang.String, java.lang.String, java.lang.String):org.mortbay.jetty.security.Password");
    }

    public static void main(String[] strArr) {
        int i = 1;
        if (!(strArr.length == 1 || strArr.length == 2)) {
            System.err.println("Usage - java org.mortbay.jetty.security.Password [<user>] <password>");
            System.err.println("If the password is ?, the user will be prompted for the password");
            System.exit(1);
        }
        if (strArr.length == 1) {
            i = 0;
        }
        String str = strArr[i];
        Password password = "?".equals(str) ? new Password(str) : new Password(str);
        System.err.println(password.toString());
        System.err.println(obfuscate(password.toString()));
        System.err.println(MD5.digest(str));
        if (strArr.length == 2) {
            System.err.println(Crypt.crypt(strArr[0], password.toString()));
        }
    }

    public static String obfuscate(String str) {
        String stringBuffer;
        StringBuffer stringBuffer2 = new StringBuffer();
        byte[] bytes = str.getBytes();
        synchronized (stringBuffer2) {
            stringBuffer2.append(__OBFUSCATE);
            for (int i = 0; i < bytes.length; i++) {
                byte b = bytes[i];
                byte b2 = bytes[str.length() - (i + 1)];
                String num = Integer.toString(((b + 127) - b2) + (((b + 127) + b2) * 256), 36);
                switch (num.length()) {
                    case 1:
                        stringBuffer2.append('0');
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
                stringBuffer2.append('0');
                stringBuffer2.append('0');
                continue;
                stringBuffer2.append(num);
            }
            stringBuffer = stringBuffer2.toString();
        }
        return stringBuffer;
    }

    public boolean check(Object obj) {
        return this == obj ? true : obj instanceof Password ? obj.equals(this._pw) : obj instanceof String ? obj.equals(this._pw) : obj instanceof Credential ? ((Credential) obj).check(this._pw) : false;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null) {
            if (obj instanceof Password) {
                Password password = (Password) obj;
                if (password._pw == this._pw) {
                    return true;
                }
                if (this._pw != null && this._pw.equals(password._pw)) {
                    return true;
                }
            } else if (obj instanceof String) {
                return obj.equals(this._pw);
            }
        }
        return false;
    }

    public int hashCode() {
        return this._pw == null ? super.hashCode() : this._pw.hashCode();
    }

    public String toStarString() {
        return "*****************************************************".substring(0, this._pw.length());
    }

    public String toString() {
        return this._pw;
    }
}
