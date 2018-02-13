package com.parrot.freeflight.utils;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.mortbay.jetty.HttpVersions;

public class CacheUtils {
    public static void cacheData(Context context, String str, String str2) {
        IOException e;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput(str, 0);
            fileOutputStream.write(str2.getBytes());
            fileOutputStream.flush();
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e2) {
                    e = e2;
                }
            }
        } catch (IOException e3) {
            e3.printStackTrace();
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e4) {
                    e3 = e4;
                    e3.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
            }
        }
    }

    public static boolean copyFileFromAssetsToStorage(AssetManager assetManager, String str, File file) {
        FileOutputStream fileOutputStream;
        IOException e;
        InputStream inputStream;
        Throwable th;
        InputStream inputStream2;
        FileOutputStream fileOutputStream2 = null;
        boolean z = true;
        InputStream open;
        try {
            open = assetManager.open(str);
            try {
                fileOutputStream = new FileOutputStream(file);
                try {
                    StreamUtils.copyStream(open, fileOutputStream);
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (open != null) {
                        try {
                            open.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                } catch (IOException e3) {
                    e = e3;
                    inputStream = open;
                    try {
                        e.printStackTrace();
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                        }
                        if (inputStream != null) {
                            z = false;
                            return z;
                        }
                        try {
                            inputStream.close();
                            return false;
                        } catch (IOException e42) {
                            e42.printStackTrace();
                            return false;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        open = inputStream;
                        fileOutputStream2 = fileOutputStream;
                        fileOutputStream = fileOutputStream2;
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                            }
                        }
                        if (open != null) {
                            try {
                                open.close();
                            } catch (IOException e2222) {
                                e2222.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    if (open != null) {
                        open.close();
                    }
                    throw th;
                }
            } catch (IOException e5) {
                e42 = e5;
                inputStream2 = open;
                inputStream = inputStream2;
                fileOutputStream = null;
                e42.printStackTrace();
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                    return false;
                }
                z = false;
                return z;
            } catch (Throwable th4) {
                th = th4;
                fileOutputStream = fileOutputStream2;
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (open != null) {
                    open.close();
                }
                throw th;
            }
        } catch (IOException e6) {
            e42 = e6;
            inputStream2 = null;
            inputStream = inputStream2;
            fileOutputStream = null;
            e42.printStackTrace();
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
                return false;
            }
            z = false;
            return z;
        } catch (Throwable th5) {
            th = th5;
            open = null;
            fileOutputStream = fileOutputStream2;
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            if (open != null) {
                open.close();
            }
            throw th;
        }
        return z;
    }

    public static File createTempFile(Context context) {
        if (context == null) {
            throw new IllegalArgumentException();
        }
        File externalCacheDir = context.getExternalCacheDir();
        if (externalCacheDir == null) {
            externalCacheDir = context.getCacheDir();
        }
        try {
            return File.createTempFile("parrot", HttpVersions.HTTP_0_9, externalCacheDir);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCachedData(Context context, String str) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream openFileInput = context.openFileInput(str);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = openFileInput.read(bArr);
                if (read == -1) {
                    return stringBuilder.toString();
                }
                stringBuilder.append(new String(bArr, 0, read));
            }
        } catch (IOException e) {
            return null;
        }
    }

    public static StringBuffer readFromFile(File file) {
        FileInputStream fileInputStream;
        IOException e;
        FileNotFoundException e2;
        Throwable th;
        FileInputStream fileInputStream2 = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            fileInputStream = new FileInputStream(file);
            try {
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = fileInputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    stringBuffer.append(new String(bArr, 0, read));
                }
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
                return stringBuffer;
            } catch (FileNotFoundException e4) {
                e2 = e4;
                try {
                    e2.printStackTrace();
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e5) {
                            e3 = e5;
                            e3.printStackTrace();
                            return stringBuffer;
                        }
                    }
                    return stringBuffer;
                } catch (Throwable th2) {
                    th = th2;
                    fileInputStream2 = fileInputStream;
                    fileInputStream = fileInputStream2;
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e6) {
                            e6.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (IOException e7) {
                e3 = e7;
                e3.printStackTrace();
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                        return stringBuffer;
                    } catch (IOException e8) {
                        e3 = e8;
                        e3.printStackTrace();
                        return stringBuffer;
                    }
                }
                return stringBuffer;
            } catch (Throwable th3) {
                th = th3;
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                throw th;
            }
        } catch (FileNotFoundException e9) {
            e2 = e9;
            fileInputStream = null;
            e2.printStackTrace();
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            return stringBuffer;
        } catch (IOException e10) {
            e3 = e10;
            fileInputStream = null;
            e3.printStackTrace();
            if (fileInputStream != null) {
                fileInputStream.close();
                return stringBuffer;
            }
            return stringBuffer;
        } catch (Throwable th4) {
            th = th4;
            fileInputStream = fileInputStream2;
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            throw th;
        }
    }
}
