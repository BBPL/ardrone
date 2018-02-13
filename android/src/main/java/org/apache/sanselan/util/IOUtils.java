package org.apache.sanselan.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import org.apache.sanselan.SanselanConstants;

public class IOUtils implements SanselanConstants {
    private IOUtils() {
    }

    public static final boolean copyFileNio(File file, File file2) throws IOException {
        Throwable th;
        FileChannel channel;
        FileChannel channel2;
        try {
            channel = new FileInputStream(file).getChannel();
            try {
                channel2 = new FileOutputStream(file2).getChannel();
                try {
                    for (long j = 0; j < channel.size(); j += channel.transferTo(j, (long) 16777216, channel2)) {
                    }
                    channel.close();
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Throwable th3) {
                th = th3;
                channel2 = null;
                if (channel != null) {
                    try {
                        channel.close();
                    } catch (Throwable e) {
                        Debug.debug(e);
                    }
                }
                if (channel2 != null) {
                    try {
                        channel2.close();
                    } catch (Throwable e2) {
                        Debug.debug(e2);
                    }
                }
                throw th;
            }
            try {
                channel2.close();
                return true;
            } catch (Throwable th4) {
                th = th4;
                channel = null;
                if (channel != null) {
                    channel.close();
                }
                if (channel2 != null) {
                    channel2.close();
                }
                throw th;
            }
        } catch (Throwable th5) {
            th = th5;
            channel2 = null;
            channel = null;
            if (channel != null) {
                channel.close();
            }
            if (channel2 != null) {
                channel2.close();
            }
            throw th;
        }
    }

    public static void copyStreamToStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        copyStreamToStream(inputStream, outputStream, true);
    }

    public static void copyStreamToStream(InputStream inputStream, OutputStream outputStream, boolean z) throws IOException {
        Throwable th;
        BufferedOutputStream bufferedOutputStream;
        BufferedInputStream bufferedInputStream = null;
        try {
            BufferedInputStream bufferedInputStream2 = new BufferedInputStream(inputStream);
            try {
                BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(outputStream);
                try {
                    byte[] bArr = new byte[4096];
                    while (true) {
                        int read = bufferedInputStream2.read(bArr, 0, bArr.length);
                        if (read <= 0) {
                            break;
                        }
                        outputStream.write(bArr, 0, read);
                    }
                    bufferedOutputStream2.flush();
                    if (z) {
                        if (bufferedInputStream2 != null) {
                            try {
                                bufferedInputStream2.close();
                            } catch (Throwable e) {
                                Debug.debug(e);
                            }
                        }
                        if (bufferedOutputStream2 != null) {
                            try {
                                bufferedOutputStream2.close();
                            } catch (Throwable e2) {
                                Debug.debug(e2);
                            }
                        }
                    }
                } catch (Throwable e22) {
                    bufferedInputStream = bufferedInputStream2;
                    BufferedOutputStream bufferedOutputStream3 = bufferedOutputStream2;
                    th = e22;
                    bufferedOutputStream = bufferedOutputStream3;
                }
            } catch (Throwable e222) {
                th = e222;
                bufferedOutputStream = null;
                bufferedInputStream = bufferedInputStream2;
                if (z) {
                    if (bufferedInputStream != null) {
                        try {
                            bufferedInputStream.close();
                        } catch (Throwable e3) {
                            Debug.debug(e3);
                        }
                    }
                    if (bufferedOutputStream != null) {
                        try {
                            bufferedOutputStream.close();
                        } catch (Throwable e2222) {
                            Debug.debug(e2222);
                        }
                    }
                }
                throw th;
            }
        } catch (Throwable e22222) {
            th = e22222;
            bufferedOutputStream = null;
            if (z) {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
            }
            throw th;
        }
    }

    public static byte[] getFileBytes(File file) throws IOException {
        Throwable e;
        InputStream inputStream;
        try {
            InputStream fileInputStream = new FileInputStream(file);
            try {
                byte[] inputStreamBytes = getInputStreamBytes(fileInputStream);
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable e2) {
                        Debug.debug(e2);
                    }
                }
                return inputStreamBytes;
            } catch (Throwable th) {
                InputStream inputStream2 = fileInputStream;
                e2 = th;
                inputStream = inputStream2;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable th2) {
                        Debug.debug(th2);
                    }
                }
                throw e2;
            }
        } catch (Throwable th22) {
            e2 = th22;
            inputStream = null;
            if (inputStream != null) {
                inputStream.close();
            }
            throw e2;
        }
    }

    public static byte[] getInputStreamBytes(InputStream inputStream) throws IOException {
        Throwable th;
        ByteArrayOutputStream byteArrayOutputStream;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream(4096);
            try {
                InputStream bufferedInputStream = new BufferedInputStream(inputStream);
                try {
                    byte[] bArr = new byte[4096];
                    while (true) {
                        int read = bufferedInputStream.read(bArr, 0, 4096);
                        if (read <= 0) {
                            break;
                        }
                        byteArrayOutputStream.write(bArr, 0, read);
                    }
                    byteArrayOutputStream.flush();
                    byte[] toByteArray = byteArrayOutputStream.toByteArray();
                    if (byteArrayOutputStream != null) {
                        try {
                            byteArrayOutputStream.close();
                        } catch (Throwable e) {
                            Debug.debug(e);
                        }
                    }
                    return toByteArray;
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Throwable th3) {
                th = th3;
                if (byteArrayOutputStream != null) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (Throwable e2) {
                        Debug.debug(e2);
                    }
                }
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            byteArrayOutputStream = null;
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
            throw th;
        }
    }

    public static void putInputStreamToFile(InputStream inputStream, File file) throws IOException {
        Throwable th;
        FileOutputStream fileOutputStream;
        try {
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            fileOutputStream = new FileOutputStream(file);
            try {
                copyStreamToStream(inputStream, fileOutputStream);
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (Throwable e) {
                        Debug.debug(e);
                    }
                }
            } catch (Throwable th2) {
                th = th2;
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (Throwable e2) {
                        Debug.debug(e2);
                    }
                }
                throw th;
            }
        } catch (Throwable e22) {
            th = e22;
            fileOutputStream = null;
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            throw th;
        }
    }

    public static void writeToFile(byte[] bArr, File file) throws IOException {
        ByteArrayInputStream byteArrayInputStream;
        Throwable th;
        try {
            byteArrayInputStream = new ByteArrayInputStream(bArr);
            try {
                putInputStreamToFile(byteArrayInputStream, file);
                if (byteArrayInputStream != null) {
                    try {
                        byteArrayInputStream.close();
                    } catch (Throwable e) {
                        Debug.debug(e);
                    }
                }
            } catch (Throwable th2) {
                th = th2;
                if (byteArrayInputStream != null) {
                    try {
                        byteArrayInputStream.close();
                    } catch (Throwable e2) {
                        Debug.debug(e2);
                    }
                }
                throw th;
            }
        } catch (Throwable e22) {
            th = e22;
            byteArrayInputStream = null;
            if (byteArrayInputStream != null) {
                byteArrayInputStream.close();
            }
            throw th;
        }
    }
}
