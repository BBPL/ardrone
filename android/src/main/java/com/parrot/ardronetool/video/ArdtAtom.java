package com.parrot.ardronetool.video;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ArdtAtom {
    private String flightName;
    private String mediaName;
    private final SimpleDateFormat userboxDateFormatter = new SimpleDateFormat("'media_'yyyyMMdd'_'HHmmss", Locale.US);
    private int version;
    private final SimpleDateFormat videoDateFormatter = new SimpleDateFormat("'video_'yyyyMMdd'_'HHmmss", Locale.US);

    ArdtAtom(String str) {
        String[] split = str.split("[|/]");
        boolean contains = str.contains("|");
        if (split.length == 3 || contains) {
            try {
                this.version = Integer.parseInt(split[0]);
                if (split.length < 3) {
                    this.flightName = split[1];
                    return;
                }
                this.flightName = split[1];
                this.mediaName = split[2];
                return;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid version format of ardt atom data: " + this.version);
            }
        }
        throw new IllegalArgumentException("Invalid ardt atom data. Should match pattern <version|flight_name/media_name>: " + str);
    }

    public Date getMediaDate() {
        try {
            if (this.mediaName != null) {
                return this.videoDateFormatter.parse(this.mediaName);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date getUserboxDate() {
        try {
            if (this.flightName != null) {
                return this.userboxDateFormatter.parse(this.flightName);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getVersion() {
        return this.version;
    }
}
