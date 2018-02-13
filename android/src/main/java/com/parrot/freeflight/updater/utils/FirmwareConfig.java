package com.parrot.freeflight.updater.utils;

import android.content.Context;
import com.parrot.ardronetool.PlfFile;
import java.io.IOException;
import java.io.InputStream;
import org.mortbay.util.URIUtil;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class FirmwareConfig {
    private static final String BOOTLDR_FILE_NAME_ATTR = "bootldrFileName";
    private static final String FIRMWARE_FILE_NAME_ATTR = "fileName";
    private static final String FIRMWARE_TAG = "firmware";
    private static final String FIRMWARE_V2_FILE_NAME_ATTR = "fileName2";
    private static final String REPAIR_FILE_NAME_ATTR = "repairFileName";
    private static final String REPAIR_VERSION_ATTR = "repairVersion";
    private String bootldrFileName;
    private String fileName;
    private String fileNameV2;
    private String firmwareVersion;
    private String firmwareVersionV2;
    private String repairFileName;
    private String repairVersion;

    public FirmwareConfig(Context context, String str) throws IOException, XmlPullParserException {
        XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
        InputStream open = context.getAssets().open(str + "/firmware.xml");
        newPullParser.setInput(open, "UTF-8");
        init(newPullParser);
        open.close();
        this.firmwareVersion = new PlfFile(context.getAssets(), str + URIUtil.SLASH + this.fileName).getVersion();
        this.firmwareVersionV2 = new PlfFile(context.getAssets(), str + URIUtil.SLASH + this.fileNameV2).getVersion();
    }

    private void init(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        xmlPullParser.next();
        int eventType = xmlPullParser.getEventType();
        while (eventType != 1) {
            if (eventType == 2 && xmlPullParser.getName().equals(FIRMWARE_TAG)) {
                this.fileName = xmlPullParser.getAttributeValue(null, FIRMWARE_FILE_NAME_ATTR);
                this.fileNameV2 = xmlPullParser.getAttributeValue(null, FIRMWARE_V2_FILE_NAME_ATTR);
                this.bootldrFileName = xmlPullParser.getAttributeValue(null, BOOTLDR_FILE_NAME_ATTR);
                this.repairVersion = xmlPullParser.getAttributeValue(null, REPAIR_VERSION_ATTR);
                this.repairFileName = xmlPullParser.getAttributeValue(null, REPAIR_FILE_NAME_ATTR);
            }
            eventType = xmlPullParser.next();
        }
    }

    public String getBootldrFileName() {
        return this.bootldrFileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getFileNameV2() {
        return this.fileNameV2;
    }

    public String getFirmwareVersion() {
        return this.firmwareVersion;
    }

    public String getFirmwareVersionV2() {
        return this.firmwareVersionV2;
    }

    public String getRepairFileName() {
        return this.repairFileName;
    }

    public String getRepairVersion() {
        return this.repairVersion;
    }
}
