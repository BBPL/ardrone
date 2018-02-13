package com.parrot.freeflight.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class FileUtils {
    public static final String MEDIA_PUBLIC_FOLDER_NAME = "AR.Drone";
    public static final String NO_MEDIA_FILE = ".no_media";
    private static final String TAG = "FileUtils";
    public static final String THUMBNAILS_FOLDER = ".thumbnails";

    static final class C12531 implements Comparator<File> {
        C12531() {
        }

        public int compare(File file, File file2) {
            return Long.valueOf(file.lastModified()).compareTo(Long.valueOf(file2.lastModified()));
        }
    }

    private static class FileFilterImpl implements FileFilter {
        private String ext;

        public FileFilterImpl(String str) {
            this.ext = str;
        }

        public boolean accept(File file) {
            return file.isDirectory() || file.getName().endsWith(this.ext);
        }
    }

    private FileUtils() {
    }

    public static File convertFormat(File file, String str) {
        StringBuilder stringBuilder = new StringBuilder(file.getAbsolutePath());
        stringBuilder.delete(stringBuilder.lastIndexOf(".") + 1, stringBuilder.length());
        stringBuilder.append(str);
        return new File(stringBuilder.toString());
    }

    public static void copyFileToDir(File file, File file2) {
        InputStream fileInputStream;
        IOException e;
        Throwable th;
        OutputStream outputStream = null;
        OutputStream fileOutputStream;
        try {
            fileInputStream = new FileInputStream(file);
            try {
                fileOutputStream = new FileOutputStream(file2);
                try {
                    byte[] bArr = new byte[1024];
                    while (true) {
                        int read = fileInputStream.read(bArr);
                        if (read <= 0) {
                            break;
                        }
                        fileOutputStream.write(bArr, 0, read);
                    }
                    Log.d(TAG, "File copied: " + file2);
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e2) {
                            Log.d(TAG, e2.toString());
                        }
                    }
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e3) {
                            Log.d(TAG, e3.toString());
                        }
                    }
                } catch (IOException e4) {
                    e2 = e4;
                    try {
                        Log.w(TAG, e2.toString());
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e22) {
                                Log.d(TAG, e22.toString());
                            }
                        }
                        if (fileOutputStream == null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e32) {
                                Log.d(TAG, e32.toString());
                            }
                        }
                    } catch (Throwable th2) {
                        Throwable th3 = th2;
                        outputStream = fileOutputStream;
                        th = th3;
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e5) {
                                Log.d(TAG, e5.toString());
                            }
                        }
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (IOException e222) {
                                Log.d(TAG, e222.toString());
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th22) {
                    OutputStream outputStream2 = fileOutputStream;
                    th = th22;
                    outputStream = outputStream2;
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    throw th;
                }
            } catch (IOException e322) {
                e222 = e322;
                fileOutputStream = null;
                Log.w(TAG, e222.toString());
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileOutputStream == null) {
                    fileOutputStream.close();
                }
            } catch (Throwable th4) {
                th = th4;
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                throw th;
            }
        } catch (IOException e3222) {
            fileInputStream = null;
            IOException iOException = e3222;
            fileOutputStream = null;
            e222 = iOException;
            Log.w(TAG, e222.toString());
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (fileOutputStream == null) {
                fileOutputStream.close();
            }
        } catch (Throwable th5) {
            th = th5;
            fileInputStream = null;
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            throw th;
        }
    }

    private static void createNoMediaFile(File file) {
        try {
            File file2 = new File(file, NO_MEDIA_FILE);
            if (!file2.exists()) {
                file2.createNewFile();
            }
        } catch (IOException e) {
            Log.w(TAG, e.toString());
        }
    }

    public static void deleteFile(File file) {
        if (file.exists()) {
            file.delete();
            Log.d(TAG, "File deleted: " + file);
        }
    }

    public static void deleteFile(String str) {
        deleteFile(new File(str));
    }

    public static String getFileExt(String str) {
        return str.substring(str.lastIndexOf(".") + 1, str.length());
    }

    public static void getFileList(List<File> list, File file, FileFilter fileFilter) {
        File[] listFiles = file.listFiles(fileFilter);
        if (listFiles != null) {
            for (File file2 : listFiles) {
                if (file2.isDirectory()) {
                    list.add(file2);
                    getFileList((List) list, file2, fileFilter);
                } else {
                    list.add(file2);
                }
            }
        }
    }

    public static void getFileList(List<File> list, File file, File[] fileArr) {
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                if (!file2.isDirectory() || isIgnored(fileArr, file2)) {
                    String fileExt = getFileExt(file2.getName());
                    if (fileExt.equalsIgnoreCase("jpg") || fileExt.equalsIgnoreCase("png") || fileExt.equalsIgnoreCase("mp4")) {
                        list.add(file2);
                    }
                } else {
                    getFileList((List) list, file2, fileArr);
                }
            }
        }
    }

    public static File getMediaCopyFolder(Context context) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), MEDIA_PUBLIC_FOLDER_NAME);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File getMediaFolder(Context context) {
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (externalStoragePublicDirectory == null) {
            Log.w(TAG, "Looks like sd card is not available.");
            return null;
        }
        File file = new File(externalStoragePublicDirectory, MEDIA_PUBLIC_FOLDER_NAME);
        if (file.exists()) {
            return file;
        }
        file.mkdirs();
        Log.d(TAG, "Root media folder created " + file);
        return file;
    }

    public static File getMediaThumbFolder(Context context) {
        File file = new File(getMediaFolder(context), THUMBNAILS_FOLDER);
        if (file != null) {
            if (!file.exists()) {
                file.mkdirs();
                Log.d(TAG, "Thumbnails folder created " + file);
            }
            createNoMediaFile(file);
        }
        return file;
    }

    public static String getNextFile(File file, String str) {
        File[] listFiles = file.listFiles(new FileFilterImpl(str));
        if (listFiles == null) {
            return null;
        }
        for (File file2 : listFiles) {
            String nextFile;
            if (file2.isDirectory()) {
                nextFile = getNextFile(file2, str);
                if (nextFile != null) {
                    return nextFile;
                }
            } else {
                nextFile = file2.getAbsolutePath();
                if (nextFile != null) {
                    return nextFile;
                }
            }
        }
        return null;
    }

    public static boolean isExtStorgAvailable() {
        return Environment.getExternalStorageState().equalsIgnoreCase("mounted");
    }

    private static boolean isIgnored(File[] fileArr, File file) {
        boolean z = false;
        for (File name : fileArr) {
            if (name.getName().equalsIgnoreCase(file.getName())) {
                z = true;
            }
        }
        return z;
    }

    public static boolean isVideo(File file) {
        return isVideo(file.getName());
    }

    public static boolean isVideo(String str) {
        return str.endsWith("mp4");
    }

    public static void sortFileByDate(List<File> list) {
        File[] fileArr = new File[list.size()];
        for (int i = 0; i < list.size(); i++) {
            fileArr[i] = (File) list.get(i);
        }
        Arrays.sort(fileArr, new C12531());
        list.clear();
        list.addAll(Arrays.asList(fileArr));
    }
}
