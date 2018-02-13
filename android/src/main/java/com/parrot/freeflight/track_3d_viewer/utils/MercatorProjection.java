package com.parrot.freeflight.track_3d_viewer.utils;

import android.util.Pair;
import com.google.api.client.http.ExponentialBackOffPolicy;

public class MercatorProjection {
    private static final int MERCATOR_RANGE = 256;
    private static final double pixelOriginX = 128.0d;
    private static final double pixelOriginY = 128.0d;
    private static final double pixelsPerLonDegree_ = 0.7111111111111111d;
    private static final double pixelsPerLonRadian_ = 40.74366543152521d;

    private static double bound(double d, double d2, double d3) {
        return Math.min(Math.max(d, d2), d3);
    }

    private static double degreesToRadians(double d) {
        return 0.017453292519943295d * d;
    }

    public static Pair<Double, Double> fromLatLngToPoint(double d, double d2) {
        double bound = bound(Math.sin(degreesToRadians(d)), -0.9999d, 0.9999d);
        return new Pair(Double.valueOf((pixelsPerLonDegree_ * d2) + 128.0d), Double.valueOf(((Math.log((1.0d + bound) / (1.0d - bound)) * ExponentialBackOffPolicy.DEFAULT_RANDOMIZATION_FACTOR) * -40.74366543152521d) + 128.0d));
    }

    public static Pair<Double, Double> fromPointToLatLng(double d, double d2) {
        return new Pair(Double.valueOf(radiansToDegrees((2.0d * Math.atan(Math.exp((d2 - 128.0d) / -40.74366543152521d))) - 1.5707963267948966d)), Double.valueOf((d - 128.0d) / pixelsPerLonDegree_));
    }

    public static Pair<Double, Double> getMapSpan(double d, double d2, int i, int i2, int i3) {
        double pow = Math.pow(2.0d, (double) i3);
        Pair fromLatLngToPoint = fromLatLngToPoint(d, d2);
        Pair fromPointToLatLng = fromPointToLatLng(((Double) fromLatLngToPoint.first).doubleValue() - (((double) (i / 2)) / pow), ((Double) fromLatLngToPoint.second).doubleValue() - (((double) (i2 / 2)) / pow));
        fromLatLngToPoint = fromPointToLatLng(((Double) fromLatLngToPoint.first).doubleValue() + (((double) (i / 2)) / pow), ((Double) fromLatLngToPoint.second).doubleValue() + (((double) (i2 / 2)) / pow));
        return new Pair(Double.valueOf(Math.abs(((Double) fromPointToLatLng.first).doubleValue() - ((Double) fromLatLngToPoint.first).doubleValue())), Double.valueOf(Math.abs(((Double) fromPointToLatLng.second).doubleValue() - ((Double) fromLatLngToPoint.second).doubleValue())));
    }

    private static double radiansToDegrees(double d) {
        return d / 0.017453292519943295d;
    }
}
