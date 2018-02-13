package com.parrot.freeflight.updater.config;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;
import com.parrot.freeflight.C0984R;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

public class Config {
    private static final String CONFIG_TAG = "updater";
    private static final String ENABLED_ATTR = "enabled";
    private boolean enabled;

    public Config(Context context) {
        try {
            init(context.getResources().getXml(C0984R.xml.updater_config));
        } catch (XmlPullParserException e) {
            Log.e("Config", "Exception e: " + e);
            e.printStackTrace();
        } catch (IOException e2) {
            Log.e("Config", "Exception e: " + e2);
            e2.printStackTrace();
        }
    }

    private void init(XmlResourceParser xmlResourceParser) throws XmlPullParserException, IOException {
        xmlResourceParser.next();
        int eventType = xmlResourceParser.getEventType();
        while (eventType != 1) {
            if (eventType == 2 && xmlResourceParser.getName().equals(CONFIG_TAG)) {
                this.enabled = Boolean.valueOf(xmlResourceParser.getAttributeValue(null, ENABLED_ATTR)).booleanValue();
            }
            eventType = xmlResourceParser.next();
        }
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean z) {
        this.enabled = z;
    }
}
