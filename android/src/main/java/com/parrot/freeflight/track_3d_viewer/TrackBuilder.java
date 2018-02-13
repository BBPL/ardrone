package com.parrot.freeflight.track_3d_viewer;

import android.util.Pair;
import com.parrot.freeflight.academy.model.FlightDataItem;
import com.parrot.freeflight.track_3d_viewer.ios_stuff.GLKVector3;
import com.parrot.freeflight.track_3d_viewer.ios_stuff.OpenGLInterleavedVertex;
import com.parrot.freeflight.track_3d_viewer.utils.MercatorProjection;
import java.util.List;

public class TrackBuilder {
    private static final float EARTH_CIRCUNFERENCY = 4.0075016E7f;
    private static final float EARTH_RADIUS = 6378137.0f;
    private static final float GPS_POS_ERROR_GREEN_MAX = 3.0f;
    private static final float GPS_POS_ERROR_ORANGE_MAX = 8.0f;
    private static final float GPS_POS_ERROR_RED_MAX = 15.0f;
    private static final float GPS_POS_ERROR_YELLOW_MAX = 5.0f;
    private static final int ROAD_BSPLINE_DEGREE = 6;
    private final double altitudeSpanRatio = Math.max(this.latitudeSpanRatio, this.longitudeSpanRatio);
    private final List<FlightDataItem> flightDetails;
    private final int gpsRoadBezierFactor;
    private final double latitudeSpanRatio;
    private final double longitudeSpanRatio;
    private final double mapCenterLatitude;
    private final double mapCenterLongitude;

    public TrackBuilder(List<FlightDataItem> list, int i, int i2, double d, double d2) {
        this.flightDetails = list;
        FlightDataItem flightDataItem = (FlightDataItem) list.get(0);
        Pair mapSpan = MercatorProjection.getMapSpan((double) flightDataItem.gps_latitude_fused, (double) flightDataItem.gps_longitude_fused, i2, i2, i);
        this.latitudeSpanRatio = ((Double) mapSpan.first).doubleValue() / 2.0d;
        this.longitudeSpanRatio = ((Double) mapSpan.second).doubleValue() / 2.0d;
        this.mapCenterLongitude = d;
        this.mapCenterLatitude = d2;
        this.gpsRoadBezierFactor = list.size() * 5;
    }

    private static double blend(int i, int i2, int[] iArr, double d) {
        return i2 == 1 ? (((double) iArr[i]) > d || d >= ((double) iArr[i + 1])) ? 0.0d : 1.0d : (iArr[(i + i2) + -1] == iArr[i] && iArr[i + i2] == iArr[i + 1]) ? 0.0d : iArr[(i + i2) + -1] == iArr[i] ? ((((double) iArr[i + i2]) - d) / ((double) (iArr[i + i2] - iArr[i + 1]))) * blend(i + 1, i2 - 1, iArr, d) : iArr[i + i2] == iArr[i + 1] ? ((d - ((double) iArr[i])) / ((double) (iArr[(i + i2) - 1] - iArr[i]))) * blend(i, i2 - 1, iArr, d) : (((d - ((double) iArr[i])) / ((double) (iArr[(i + i2) - 1] - iArr[i]))) * blend(i, i2 - 1, iArr, d)) + (((((double) iArr[i + i2]) - d) / ((double) (iArr[i + i2] - iArr[i + 1]))) * blend(i + 1, i2 - 1, iArr, d));
    }

    private void bspline(int i, int i2, OpenGLInterleavedVertex[] openGLInterleavedVertexArr, short[] sArr) {
        int[] iArr = new int[((i + i2) + 1)];
        GLKVector3 gLKVector3 = new GLKVector3();
        compute_intervals(iArr, i, i2);
        double d = ((double) ((i - i2) + 2)) / ((double) (this.gpsRoadBezierFactor - 1));
        double d2 = 0.0d;
        float f = 0.0f;
        float f2 = 0.0f;
        int i3 = 0;
        while (i3 < this.gpsRoadBezierFactor - 1) {
            float f3;
            float f4;
            if (i3 % 5 != 0) {
                f3 = f;
                f4 = f2;
            } else if (((FlightDataItem) this.flightDetails.get(i3 / 5)).gps_available) {
                float f5 = ((FlightDataItem) this.flightDetails.get(i3 / 5)).gps_position_error;
                if (f5 < GPS_POS_ERROR_GREEN_MAX) {
                    f3 = 0.0f;
                    f4 = 1.0f;
                } else if (f5 < GPS_POS_ERROR_YELLOW_MAX) {
                    f3 = (f5 - GPS_POS_ERROR_GREEN_MAX) / 2.0f;
                    f4 = 1.0f;
                } else if (f5 < GPS_POS_ERROR_ORANGE_MAX) {
                    f3 = 1.0f;
                    f4 = 1.0f - ((f5 - GPS_POS_ERROR_YELLOW_MAX) / 6.0f);
                } else if (f5 < GPS_POS_ERROR_RED_MAX) {
                    f3 = 1.0f;
                    f4 = 0.5f - ((f5 - GPS_POS_ERROR_ORANGE_MAX) / 14.0f);
                } else {
                    f3 = 1.0f;
                    f4 = 0.0f;
                }
            } else {
                f3 = 1.0f;
                f4 = 0.0f;
            }
            compute_point(iArr, i, i2, d2, gLKVector3);
            openGLInterleavedVertexArr[i3].position[0] = gLKVector3.f344v[0];
            openGLInterleavedVertexArr[i3].position[1] = gLKVector3.f344v[1];
            openGLInterleavedVertexArr[i3].position[2] = gLKVector3.f344v[2];
            openGLInterleavedVertexArr[i3].color[0] = f3;
            openGLInterleavedVertexArr[i3].color[1] = f4;
            openGLInterleavedVertexArr[i3].color[2] = 0.0f;
            openGLInterleavedVertexArr[i3].color[3] = 1.0f;
            openGLInterleavedVertexArr[i3].normal[0] = 0.0f;
            openGLInterleavedVertexArr[i3].normal[1] = 1.0f;
            openGLInterleavedVertexArr[i3].normal[2] = 0.0f;
            sArr[i3] = (short) i3;
            d2 += d;
            i3++;
            f = f3;
            f2 = f4;
        }
        openGLInterleavedVertexArr[this.gpsRoadBezierFactor - 1].position[0] = (float) ((((double) ((FlightDataItem) this.flightDetails.get(i)).gps_longitude_fused) - this.mapCenterLongitude) / this.longitudeSpanRatio);
        openGLInterleavedVertexArr[this.gpsRoadBezierFactor - 1].position[1] = (float) (meterToDegree((double) (((float) ((FlightDataItem) this.flightDetails.get(i)).getAltitude()) / 1000.0f)) / this.altitudeSpanRatio);
        openGLInterleavedVertexArr[this.gpsRoadBezierFactor - 1].position[2] = (float) ((this.mapCenterLatitude - ((double) ((FlightDataItem) this.flightDetails.get(i)).gps_latitude_fused)) / this.latitudeSpanRatio);
        openGLInterleavedVertexArr[this.gpsRoadBezierFactor - 1].color[0] = f;
        openGLInterleavedVertexArr[this.gpsRoadBezierFactor - 1].color[1] = f2;
        openGLInterleavedVertexArr[this.gpsRoadBezierFactor - 1].color[2] = 0.0f;
        openGLInterleavedVertexArr[this.gpsRoadBezierFactor - 1].color[3] = 1.0f;
        openGLInterleavedVertexArr[this.gpsRoadBezierFactor - 1].normal[0] = 0.0f;
        openGLInterleavedVertexArr[this.gpsRoadBezierFactor - 1].normal[1] = 1.0f;
        openGLInterleavedVertexArr[this.gpsRoadBezierFactor - 1].normal[2] = 0.0f;
        sArr[this.gpsRoadBezierFactor - 1] = (short) (this.gpsRoadBezierFactor - 1);
    }

    private static void compute_intervals(int[] iArr, int i, int i2) {
        int i3 = 0;
        while (i3 <= i + i2) {
            if (i3 < i2) {
                iArr[i3] = 0;
            } else if (i2 <= i3 && i3 <= i) {
                iArr[i3] = (i3 - i2) + 1;
            } else if (i3 > i) {
                iArr[i3] = (i - i2) + 2;
            }
            i3++;
        }
    }

    private void compute_point(int[] iArr, int i, int i2, double d, GLKVector3 gLKVector3) {
        gLKVector3.f344v[0] = 0.0f;
        gLKVector3.f344v[1] = 0.0f;
        gLKVector3.f344v[2] = 0.0f;
        GLKVector3 gLKVector32 = new GLKVector3();
        for (int i3 = 0; i3 <= i; i3++) {
            gLKVector32.f344v[0] = (float) ((((double) ((FlightDataItem) this.flightDetails.get(i3)).gps_longitude_fused) - this.mapCenterLongitude) / this.longitudeSpanRatio);
            gLKVector32.f344v[1] = (float) (meterToDegree((double) (((float) ((FlightDataItem) this.flightDetails.get(i3)).getAltitude()) / 1000.0f)) / this.altitudeSpanRatio);
            gLKVector32.f344v[2] = (float) ((this.mapCenterLatitude - ((double) ((FlightDataItem) this.flightDetails.get(i3)).gps_latitude_fused)) / this.latitudeSpanRatio);
            double blend = blend(i3, i2, iArr, d);
            gLKVector3.f344v[0] = (float) (((double) gLKVector3.f344v[0]) + (((double) gLKVector32.f344v[0]) * blend));
            gLKVector3.f344v[1] = (float) (((double) gLKVector3.f344v[1]) + (((double) gLKVector32.f344v[1]) * blend));
            gLKVector3.f344v[2] = (float) ((blend * ((double) gLKVector32.f344v[2])) + ((double) gLKVector3.f344v[2]));
        }
    }

    private static double degreeToMeter(double d) {
        return (4.0075016E7d * d) / 360.0d;
    }

    private static double meterToDegree(double d) {
        return (360.0d * d) / 4.0075016E7d;
    }

    public Pair<OpenGLInterleavedVertex[], short[]> prepareRoad() {
        if (this.flightDetails.size() <= 1) {
            return null;
        }
        Object obj = new OpenGLInterleavedVertex[this.gpsRoadBezierFactor];
        for (int i = 0; i < obj.length; i++) {
            obj[i] = new OpenGLInterleavedVertex();
        }
        Object obj2 = new short[this.gpsRoadBezierFactor];
        bspline(this.flightDetails.size() - 1, 5, obj, obj2);
        return new Pair(obj, obj2);
    }
}
