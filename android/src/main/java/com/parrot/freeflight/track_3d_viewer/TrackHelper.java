package com.parrot.freeflight.track_3d_viewer;

import com.parrot.ardronetool.video.VideoPrrt;
import com.parrot.freeflight.academy.model.FlightDataItem;
import com.parrot.freeflight.track_3d_viewer.utils.Holder;
import java.util.ArrayList;
import java.util.List;

public class TrackHelper {
    private static final float MAP_SPAN_DELTA_COEF = 4.0f;

    public static boolean isGpsAvailable(List<FlightDataItem> list) {
        for (FlightDataItem isGps_available : list) {
            if (isGps_available.isGps_available()) {
                return true;
            }
        }
        return false;
    }

    public static void repairBlankCoordinatesAndNegativeAltitude(ArrayList<FlightDataItem> arrayList, Holder<Float> holder, Holder<Float> holder2, Holder<Float> holder3, Holder<Float> holder4) {
        int i;
        ArrayList arrayList2 = (ArrayList) arrayList.clone();
        FlightDataItem flightDataItem = null;
        int i2 = -1;
        int i3 = 0;
        while (i3 < arrayList.size()) {
            FlightDataItem flightDataItem2 = (FlightDataItem) arrayList.get(i3);
            if (flightDataItem2.isGps_available()) {
                i = i2;
            } else if (flightDataItem == null) {
                flightDataItem2 = flightDataItem;
                i = i3;
            } else {
                flightDataItem2.setGps_latitude(flightDataItem.getGps_latitude());
                flightDataItem2.setGps_latitude_fused(flightDataItem.getGps_latitude_fused());
                flightDataItem2.setGps_longitude(flightDataItem.getGps_longitude());
                flightDataItem2.setGps_longitude_fused(flightDataItem.getGps_longitude_fused());
                flightDataItem2 = flightDataItem;
                i = i2;
            }
            i3++;
            i2 = i;
            flightDataItem = flightDataItem2;
        }
        if (!(i2 == -1 || i2 == arrayList.size() - 1)) {
            flightDataItem2 = (FlightDataItem) arrayList.get(i2 + 1);
            while (i2 >= 0) {
                flightDataItem = (FlightDataItem) arrayList.get(i2);
                flightDataItem.setGps_latitude(flightDataItem2.getGps_latitude());
                flightDataItem.setGps_latitude_fused(flightDataItem2.getGps_latitude_fused());
                flightDataItem.setGps_longitude(flightDataItem2.getGps_longitude());
                flightDataItem.setGps_longitude_fused(flightDataItem2.getGps_longitude_fused());
                i2--;
            }
        }
        i = ((FlightDataItem) arrayList2.get(0)).getAltitude();
        float gps_latitude_fused = ((FlightDataItem) arrayList2.get(0)).getGps_latitude_fused();
        float gps_longitude_fused = ((FlightDataItem) arrayList2.get(0)).getGps_longitude_fused();
        float f = gps_latitude_fused;
        float f2 = gps_latitude_fused;
        float f3 = gps_longitude_fused;
        float f4 = gps_longitude_fused;
        i2 = 1;
        int i4 = 1;
        i3 = i;
        while (i2 < arrayList2.size()) {
            flightDataItem2 = (FlightDataItem) arrayList2.get(i2 - 1);
            flightDataItem = (FlightDataItem) arrayList2.get(i2);
            long time = flightDataItem2.getTime() / 1000;
            long time2 = (flightDataItem.getTime() / 1000) - time;
            float f5 = flightDataItem2.gps_latitude_fused;
            float f6 = flightDataItem.gps_latitude_fused;
            float f7 = flightDataItem2.gps_longitude_fused;
            float f8 = flightDataItem.gps_longitude_fused;
            int altitude = flightDataItem2.getAltitude();
            int altitude2 = flightDataItem.getAltitude();
            f = Math.min(f, f6);
            f2 = Math.max(f2, f6);
            f4 = Math.min(f4, f8);
            f3 = Math.max(f3, f8);
            i = Math.min(i3, altitude2);
            for (i3 = 1; ((long) i3) < time2; i3++) {
                float f9 = (float) i3;
                float f10 = (f6 - f5) / ((float) time2);
                float f11 = (float) i3;
                float f12 = (f8 - f7) / ((float) time2);
                int i5 = (int) (((float) altitude) + (((float) i3) * (((float) (altitude2 - altitude)) / ((float) time2))));
                FlightDataItem flightDataItem3 = new FlightDataItem(flightDataItem2);
                flightDataItem3.setTime(((long) i3) + time);
                flightDataItem3.gps_latitude_fused = (f9 * f10) + f5;
                flightDataItem3.gps_longitude_fused = (f11 * f12) + f7;
                flightDataItem3.setAltitude(i5);
                arrayList.add(i4, flightDataItem3);
                i4++;
            }
            i2++;
            i4++;
            i3 = i;
        }
        if (i3 < 0) {
            for (int i6 = 0; i6 < arrayList.size(); i6++) {
                FlightDataItem flightDataItem4 = (FlightDataItem) arrayList.get(i6);
                flightDataItem4.setAltitude(flightDataItem4.getAltitude() - i3);
            }
        }
        float f13 = (f3 - f4) / 2.0f;
        holder2.value = Float.valueOf(((f2 - f) / 2.0f) + f);
        holder.value = Float.valueOf(f4 + f13);
        holder4.value = Float.valueOf((f2 - f) * MAP_SPAN_DELTA_COEF);
        holder3.value = Float.valueOf((f3 - f4) * MAP_SPAN_DELTA_COEF);
    }

    public static void synchronizeAltitudeWithPlayer(List<FlightDataItem> list, VideoPrrt videoPrrt) {
        long time = ((FlightDataItem) list.get(0)).getTime() / 1000;
        int i = 0;
        for (FlightDataItem flightDataItem : list) {
            long time2 = flightDataItem.getTime() / 1000;
            while (i < videoPrrt.getFrames() && (videoPrrt.getTimestamp(i) - videoPrrt.getTimestamp(0)) + ((double) time) < ((double) time2)) {
                i++;
            }
            if (i < videoPrrt.getFrames()) {
                int altitude = videoPrrt.getAltitude(i);
                if (altitude != Integer.MAX_VALUE) {
                    flightDataItem.setAltitude(altitude);
                }
            }
        }
    }

    public static void synchronizeAltitudeWithPlayer(List<FlightDataItem> list, List<String> list2) {
        for (String videoPrrt : list2) {
            VideoPrrt videoPrrt2 = new VideoPrrt(videoPrrt);
            try {
                synchronizeAltitudeWithPlayer((List) list, videoPrrt2);
                videoPrrt2.release();
            } catch (Throwable th) {
                videoPrrt2.release();
            }
        }
    }
}
