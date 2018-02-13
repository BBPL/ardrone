package com.parrot.freeflight.transcodeservice.tasks;

import android.os.AsyncTask;
import android.util.Log;
import com.parrot.freeflight.utils.FileUtils;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class CleanupCacheFolderTask extends AsyncTask<File, Integer, Boolean> {
    private static final String TAG = CleanupCacheFolderTask.class.getSimpleName();
    private FileFilter bakFileFilter = new C12021();
    private String[] extentionsToRemove = new String[]{"bak", "tmp"};

    class C12021 implements FileFilter {
        C12021() {
        }

        private boolean endsWith(String str, String[] strArr) {
            for (String endsWith : strArr) {
                if (str.endsWith(endsWith)) {
                    return true;
                }
            }
            return false;
        }

        public boolean accept(File file) {
            return file.isDirectory() || (file.isFile() && endsWith(file.getName(), CleanupCacheFolderTask.this.extentionsToRemove));
        }
    }

    protected Boolean doInBackground(File... fileArr) {
        Log.d(TAG, "Removing temporary files...");
        File file = fileArr[0];
        List arrayList = new ArrayList();
        if (file.isDirectory()) {
            FileUtils.getFileList(arrayList, file, this.bakFileFilter);
        }
        if (arrayList.size() > 0) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                file = (File) arrayList.get(i);
                if (file.exists()) {
                    file.delete();
                }
                publishProgress(new Integer[]{Integer.valueOf(i)});
                if (isCancelled()) {
                    break;
                }
            }
        }
        return Boolean.TRUE;
    }
}
