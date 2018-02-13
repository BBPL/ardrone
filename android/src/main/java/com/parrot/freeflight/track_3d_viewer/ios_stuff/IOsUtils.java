package com.parrot.freeflight.track_3d_viewer.ios_stuff;

import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.parrot.freeflight.track_3d_viewer.utils.Holder;

public class IOsUtils {
    private static float MAX_SPAN_LONGITUDE = 0.0025f;
    private static double MERCATOR_OFFSET = 2.68435456E8d;
    private static double MERCATOR_RADIUS = 8.544565944705395E7d;

    public static float GLKMathDegreesToRadians(float f) {
        return (float) (((double) f) * 0.017453292519943295d);
    }

    public static float GLKMathRadiansToDegrees(float f) {
        return (float) (((double) f) * 57.29577951308232d);
    }

    public static float[] GLKMatrix4MakePerspective(float f, float f2, float f3, float f4) {
        float tan = 1.0f / ((float) Math.tan((double) (f / 2.0f)));
        return new float[]{tan / f2, 0.0f, 0.0f, 0.0f, 0.0f, tan, 0.0f, 0.0f, 0.0f, 0.0f, (f4 + f3) / (f3 - f4), GroundOverlayOptions.NO_DIMENSION, 0.0f, 0.0f, ((2.0f * f4) * f3) / (f3 - f4), 0.0f};
    }

    public static void coordinateSpanWithMapView(float f, float f2, double d, int i, int i2, boolean z, Holder<Double> holder, Holder<Double> holder2) {
        double longitudeToPixelSpaceX = longitudeToPixelSpaceX((double) f);
        double latitudeToPixelSpaceY = latitudeToPixelSpaceY((double) f2);
        if (d > ((double) maxZoomTile(z))) {
            d = (double) maxZoomTile(z);
        }
        double pow = Math.pow(2.0d, ((double) (20 - (z ? 1 : 0))) - d);
        double d2 = ((double) i) * pow;
        pow *= (double) i2;
        longitudeToPixelSpaceX -= d2 / 2.0d;
        latitudeToPixelSpaceY -= pow / 2.0d;
        double pixelSpaceXToLongitude = pixelSpaceXToLongitude(longitudeToPixelSpaceX);
        longitudeToPixelSpaceX = pixelSpaceXToLongitude(longitudeToPixelSpaceX + d2);
        holder.value = Double.valueOf((pixelSpaceYToLatitude(pow + latitudeToPixelSpaceY) - pixelSpaceYToLatitude(latitudeToPixelSpaceY)) * -1.0d);
        holder2.value = Double.valueOf(longitudeToPixelSpaceX - pixelSpaceXToLongitude);
    }

    public static double latitudeToPixelSpaceY(double d) {
        return d == 90.0d ? 0.0d : d == -90.0d ? MERCATOR_OFFSET * 2.0d : (double) Math.round(MERCATOR_OFFSET - ((MERCATOR_RADIUS * Math.log((Math.sin((d * 3.141592653589793d) / 180.0d) + 1.0d) / (1.0d - Math.sin((3.141592653589793d * d) / 180.0d)))) / 2.0d));
    }

    public static double longitudeToPixelSpaceX(double d) {
        return (double) Math.round(MERCATOR_OFFSET + (((MERCATOR_RADIUS * d) * 3.141592653589793d) / 180.0d));
    }

    private static int maxZoomTile(boolean z) {
        return z ? 17 : 18;
    }

    public static double pixelSpaceXToLongitude(double d) {
        return (((((double) Math.round(d)) - MERCATOR_OFFSET) / MERCATOR_RADIUS) * 180.0d) / 3.141592653589793d;
    }

    public static double pixelSpaceYToLatitude(double d) {
        return ((1.5707963267948966d - (2.0d * Math.atan(Math.exp((((double) Math.round(d)) - MERCATOR_OFFSET) / MERCATOR_RADIUS)))) * 180.0d) / 3.141592653589793d;
    }

    public static double regionZoom(double d, double d2, int i, boolean z) {
        return ((double) (20 - (z ? 1 : 0))) - (Math.log(((longitudeToPixelSpaceX(d) - longitudeToPixelSpaceX(d - (d2 / 2.0d))) * 2.0d) / ((double) i)) / Math.log(2.0d));
    }

    public static void safeRegion(float f, float f2, float f3, int i, int i2, boolean z, Holder<Double> holder, Holder<Double> holder2, Holder<Double> holder3) {
        if (f < MAX_SPAN_LONGITUDE) {
            f = MAX_SPAN_LONGITUDE;
        }
        holder3.value = Double.valueOf(regionZoom((double) f2, (double) f, i, z));
        coordinateSpanWithMapView(f2, f3, ((Double) holder3.value).doubleValue(), i, i2, z, holder, holder2);
    }
}
