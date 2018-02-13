package com.parrot.freeflight.academy.utils;

import android.net.Uri;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.util.List;
import org.mortbay.jetty.HttpVersions;
import org.mortbay.util.URIUtil;

public class AcademyFormatUtils {
    public static String academyThumbUrlFromPicasaThumbUrl(String str) {
        Uri parse = Uri.parse(str);
        List pathSegments = parse.getPathSegments();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(parse.getScheme());
        stringBuilder.append("://");
        stringBuilder.append(parse.getHost());
        stringBuilder.append(URIUtil.SLASH);
        int size = pathSegments.size();
        for (int i = 0; i < size; i++) {
            if (i == size - 2) {
                stringBuilder.append("kPhotoThumbSize/");
            } else {
                stringBuilder.append((String) pathSegments.get(i));
                if (i < size - 1) {
                    stringBuilder.append(URIUtil.SLASH);
                }
            }
        }
        return stringBuilder.toString();
    }

    public static String academyThumbUrlFromYouTubeThumbUrl(String str) {
        return str.substring(0, str.lastIndexOf(URIUtil.SLASH));
    }

    public static String timeFromSeconds(int i) {
        String str = HttpVersions.HTTP_0_9;
        if (i >= 3600) {
            int floor = (int) Math.floor((double) (((float) i) / 3600.0f));
            i %= 3600;
            str = HttpVersions.HTTP_0_9 + String.format("%02dh ", new Object[]{Integer.valueOf(floor)});
        }
        int i2 = i % 60;
        if (((int) Math.floor((double) (((float) i) / BitmapDescriptorFactory.HUE_YELLOW))) != 0) {
            str = str + String.format("%02dm ", new Object[]{Integer.valueOf(r1)});
        }
        if (str.length() != 0 && i2 == 0) {
            return str;
        }
        return str + String.format("%02ds", new Object[]{Integer.valueOf(i2)});
    }
}
