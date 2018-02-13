package com.parrot.freeflight.tasks;

import android.os.AsyncTask;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MoveFileTask extends AsyncTask<File, Integer, Boolean> {
    private static final String TAG = MoveFileTask.class.getSimpleName();
    private File result;

    private Boolean copyFile(File file, File file2) throws IOException {
        InputStream fileInputStream;
        OutputStream fileOutputStream;
        boolean z;
        boolean z2;
        Throwable e;
        Throwable th;
        OutputStream outputStream;
        Throwable th2;
        OutputStream outputStream2;
        try {
            file2.createNewFile();
            fileInputStream = new FileInputStream(file);
            try {
                fileOutputStream = new FileOutputStream(file2);
                try {
                    long length = file.length();
                    long j = 0;
                    byte[] bArr = new byte[512];
                    do {
                        int read = fileInputStream.read(bArr, 0, bArr.length);
                        if (read == -1) {
                            break;
                        }
                        fileOutputStream.write(bArr, 0, read);
                        j += (long) read;
                        publishProgress(new Integer[]{Integer.valueOf(((int) (((double) j) / ((double) length))) * 100)});
                    } while (!isCancelled());
                    Log.d(TAG, "Copy of the file was canceled");
                    z = !isCancelled();
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            if (z && !isCancelled()) {
                                if (!file.delete()) {
                                    Log.w(TAG, "Cant delete file" + file.getAbsolutePath());
                                }
                                this.result = file2;
                            } else if (!z) {
                                if (isCancelled() && file2.exists()) {
                                    file2.delete();
                                    z2 = z;
                                    return Boolean.valueOf(z2);
                                }
                            }
                        } catch (IOException e3) {
                            e3.printStackTrace();
                            z2 = z;
                        }
                    }
                } catch (IOException e4) {
                    e = e4;
                    try {
                        Log.w("ExternalStorage", "Error writing " + file2, e);
                        z = false;
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e22) {
                                e22.printStackTrace();
                            }
                        }
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.flush();
                                fileOutputStream.close();
                                if (isCancelled() && file2.exists()) {
                                    file2.delete();
                                    z2 = false;
                                    return Boolean.valueOf(z2);
                                }
                            } catch (IOException e32) {
                                e32.printStackTrace();
                                z2 = false;
                            }
                        }
                        z2 = z;
                        return Boolean.valueOf(z2);
                    } catch (Throwable e5) {
                        th = e5;
                        outputStream = fileOutputStream;
                        th2 = th;
                        outputStream2 = outputStream;
                        e5 = th2;
                        fileOutputStream = outputStream2;
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                            }
                        }
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.flush();
                                fileOutputStream.close();
                                file2.delete();
                            } catch (IOException e322) {
                                e322.printStackTrace();
                            }
                        }
                        throw e5;
                    }
                } catch (Throwable th3) {
                    e5 = th3;
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        if (isCancelled() && file2.exists()) {
                            file2.delete();
                        }
                    }
                    throw e5;
                }
            } catch (Throwable th22) {
                e5 = th22;
                fileOutputStream = null;
                Log.w("ExternalStorage", "Error writing " + file2, e5);
                z = false;
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    file2.delete();
                    z2 = false;
                    return Boolean.valueOf(z2);
                }
                z2 = z;
                return Boolean.valueOf(z2);
            } catch (Throwable th222) {
                e5 = th222;
                fileOutputStream = null;
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    file2.delete();
                }
                throw e5;
            }
        } catch (Throwable th2222) {
            fileInputStream = null;
            th = th2222;
            fileOutputStream = null;
            e5 = th;
            Log.w("ExternalStorage", "Error writing " + file2, e5);
            z = false;
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
                file2.delete();
                z2 = false;
                return Boolean.valueOf(z2);
            }
            z2 = z;
            return Boolean.valueOf(z2);
        } catch (Throwable th4) {
            th2222 = th4;
            outputStream = null;
            fileInputStream = null;
            outputStream2 = outputStream;
            e5 = th2222;
            fileOutputStream = outputStream2;
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
                file2.delete();
            }
            throw e5;
        }
        z2 = z;
        return Boolean.valueOf(z2);
    }

    protected Boolean doInBackground(File... fileArr) {
        if (fileArr.length < 2) {
            throw new IllegalArgumentException("Not enough parameters. Shoud have source and destination files");
        }
        File file = fileArr[0];
        File file2 = fileArr[1];
        Log.d(TAG, "Moving file " + file.getAbsolutePath() + " to " + file2.getAbsolutePath());
        if (file.renameTo(file2)) {
            this.result = file2;
            file.getParentFile().delete();
            Log.d(TAG, "File moved successfully");
            return Boolean.valueOf(true);
        }
        Log.d(TAG, "Moving of file failed. Copying...");
        try {
            Boolean copyFile = copyFile(file, file2);
            if (copyFile.equals(Boolean.TRUE)) {
                Log.d(TAG, "Copying of the file " + file.getAbsolutePath() + " completed with success.");
                return copyFile;
            }
            Log.w(TAG, "Copying of the file " + file.getAbsolutePath() + " to " + file2.getAbsolutePath() + " failed");
            return copyFile;
        } catch (IOException e) {
            e.printStackTrace();
            return Boolean.valueOf(false);
        }
    }

    public File getResultFile() {
        return this.result;
    }
}
