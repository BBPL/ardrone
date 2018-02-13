package com.parrot.freeflight.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.parrot.freeflight.service.DroneConfig;
import com.parrot.freeflight.updater.utils.FirmwareConfig;
import com.parrot.freeflight.utils.FTPUtils;
import com.parrot.freeflight.utils.Version;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

public class CheckFirmwareTask extends AsyncTask<Object, Integer, Boolean> {
    private static final String TAG = CheckFirmwareTask.class.getSimpleName();
    private Context context;

    public CheckFirmwareTask(Context context) {
        this.context = context;
    }

    protected Boolean doInBackground(Object... objArr) {
        try {
            FirmwareConfig firmwareConfig = new FirmwareConfig(this.context, "firmware");
            String downloadFile = FTPUtils.downloadFile(this.context, DroneConfig.getDroneHost(), DroneConfig.getFtpPort(), "version.txt");
            if (downloadFile == null) {
                Log.w(TAG, "Can't determine drone version");
                return Boolean.FALSE;
            }
            Version version;
            Version version2 = new Version(downloadFile.trim());
            if (downloadFile.startsWith("1")) {
                version = new Version(firmwareConfig.getFirmwareVersion());
            } else if (downloadFile.startsWith("2")) {
                version = new Version(firmwareConfig.getFirmwareVersionV2());
            } else {
                Log.w(TAG, "Can't determine drone version");
                return Boolean.FALSE;
            }
            return version.isGreater(version2) ? Boolean.TRUE : Boolean.FALSE;
        } catch (IOException e) {
            e.printStackTrace();
            return Boolean.FALSE;
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
            return Boolean.FALSE;
        }
    }
}
