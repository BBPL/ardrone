package org.mortbay.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class RolloverFileOutputStream extends FilterOutputStream {
    static final String YYYY_MM_DD = "yyyy_mm_dd";
    private static Timer __rollover;
    static Class class$org$mortbay$util$RolloverFileOutputStream;
    private boolean _append;
    private File _file;
    private SimpleDateFormat _fileBackupFormat;
    private SimpleDateFormat _fileDateFormat;
    private String _filename;
    private int _retainDays;
    private RollTask _rollTask;

    static class C13501 {
    }

    private class RollTask extends TimerTask {
        private final RolloverFileOutputStream this$0;

        private RollTask(RolloverFileOutputStream rolloverFileOutputStream) {
            this.this$0 = rolloverFileOutputStream;
        }

        RollTask(RolloverFileOutputStream rolloverFileOutputStream, C13501 c13501) {
            this(rolloverFileOutputStream);
        }

        public void run() {
            try {
                RolloverFileOutputStream.access$100(this.this$0);
                RolloverFileOutputStream.access$200(this.this$0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public RolloverFileOutputStream(String str) throws IOException {
        this(str, true, Integer.getInteger("ROLLOVERFILE_RETAIN_DAYS", 31).intValue());
    }

    public RolloverFileOutputStream(String str, boolean z) throws IOException {
        this(str, z, Integer.getInteger("ROLLOVERFILE_RETAIN_DAYS", 31).intValue());
    }

    public RolloverFileOutputStream(String str, boolean z, int i) throws IOException {
        this(str, z, i, TimeZone.getDefault());
    }

    public RolloverFileOutputStream(String str, boolean z, int i, TimeZone timeZone) throws IOException {
        this(str, z, i, timeZone, null, null);
    }

    public RolloverFileOutputStream(String str, boolean z, int i, TimeZone timeZone, String str2, String str3) throws IOException {
        super(null);
        if (str2 == null) {
            str2 = System.getProperty("ROLLOVERFILE_DATE_FORMAT", "yyyy_MM_dd");
        }
        this._fileDateFormat = new SimpleDateFormat(str2);
        if (str3 == null) {
            str3 = System.getProperty("ROLLOVERFILE_BACKUP_FORMAT", "HHmmssSSS");
        }
        this._fileBackupFormat = new SimpleDateFormat(str3);
        this._fileBackupFormat.setTimeZone(timeZone);
        this._fileDateFormat.setTimeZone(timeZone);
        if (str != null) {
            str = str.trim();
            if (str.length() == 0) {
                str = null;
            }
        }
        if (str == null) {
            throw new IllegalArgumentException("Invalid filename");
        }
        this._filename = str;
        this._append = z;
        this._retainDays = i;
        setFile();
        Class cls;
        if (class$org$mortbay$util$RolloverFileOutputStream == null) {
            Class class$ = class$("org.mortbay.util.RolloverFileOutputStream");
            class$org$mortbay$util$RolloverFileOutputStream = class$;
            cls = class$;
        } else {
            cls = class$org$mortbay$util$RolloverFileOutputStream;
        }
        synchronized (r6) {
            if (__rollover == null) {
                __rollover = new Timer(true);
            }
            this._rollTask = new RollTask(this, null);
            Calendar instance = Calendar.getInstance();
            instance.setTimeZone(timeZone);
            GregorianCalendar gregorianCalendar = new GregorianCalendar(instance.get(1), instance.get(2), instance.get(5), 23, 0);
            gregorianCalendar.setTimeZone(timeZone);
            gregorianCalendar.add(10, 1);
            __rollover.scheduleAtFixedRate(this._rollTask, gregorianCalendar.getTime(), 86400000);
        }
    }

    static void access$100(RolloverFileOutputStream rolloverFileOutputStream) throws IOException {
        rolloverFileOutputStream.setFile();
    }

    static void access$200(RolloverFileOutputStream rolloverFileOutputStream) {
        rolloverFileOutputStream.removeOldFiles();
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable e) {
            throw new NoClassDefFoundError().initCause(e);
        }
    }

    private void removeOldFiles() {
        int i = 0;
        if (this._retainDays > 0) {
            long currentTimeMillis = System.currentTimeMillis();
            File file = new File(this._filename);
            File file2 = new File(file.getParent());
            String name = file.getName();
            int indexOf = name.toLowerCase().indexOf(YYYY_MM_DD);
            if (indexOf >= 0) {
                String substring = name.substring(0, indexOf);
                name = name.substring(indexOf + YYYY_MM_DD.length());
                String[] list = file2.list();
                while (i < list.length) {
                    String str = list[i];
                    if (str.startsWith(substring) && str.indexOf(name, substring.length()) >= 0) {
                        File file3 = new File(file2, str);
                        if ((currentTimeMillis - file3.lastModified()) / 86400000 > ((long) this._retainDays)) {
                            file3.delete();
                        }
                    }
                    i++;
                }
            }
        }
    }

    private void setFile() throws IOException {
        synchronized (this) {
            this._filename = new File(this._filename).getCanonicalPath();
            File file = new File(this._filename);
            File file2 = new File(file.getParent());
            if (file2.isDirectory() && file2.canWrite()) {
                Date date = new Date();
                String name = file.getName();
                int indexOf = name.toLowerCase().indexOf(YYYY_MM_DD);
                if (indexOf >= 0) {
                    file = new File(file2, new StringBuffer().append(name.substring(0, indexOf)).append(this._fileDateFormat.format(date)).append(name.substring(indexOf + YYYY_MM_DD.length())).toString());
                }
                if (!file.exists() || file.canWrite()) {
                    if (this.out == null || !file.equals(this._file)) {
                        this._file = file;
                        if (!this._append && file.exists()) {
                            file.renameTo(new File(new StringBuffer().append(file.toString()).append(".").append(this._fileBackupFormat.format(date)).toString()));
                        }
                        OutputStream outputStream = this.out;
                        this.out = new FileOutputStream(file.toString(), this._append);
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    }
                } else {
                    throw new IOException(new StringBuffer().append("Cannot write log file ").append(file).toString());
                }
            }
            throw new IOException(new StringBuffer().append("Cannot write log directory ").append(file2).toString());
        }
    }

    public void close() throws IOException {
        Class class$;
        if (class$org$mortbay$util$RolloverFileOutputStream == null) {
            class$ = class$("org.mortbay.util.RolloverFileOutputStream");
            class$org$mortbay$util$RolloverFileOutputStream = class$;
        } else {
            class$ = class$org$mortbay$util$RolloverFileOutputStream;
        }
        synchronized (r0) {
            try {
                super.close();
                this.out = null;
                this._file = null;
                this._rollTask.cancel();
            } catch (Throwable th) {
                this.out = null;
                this._file = null;
            }
        }
    }

    public String getDatedFilename() {
        return this._file == null ? null : this._file.toString();
    }

    public String getFilename() {
        return this._filename;
    }

    public int getRetainDays() {
        return this._retainDays;
    }

    public void write(byte[] bArr) throws IOException {
        this.out.write(bArr);
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        this.out.write(bArr, i, i2);
    }
}
