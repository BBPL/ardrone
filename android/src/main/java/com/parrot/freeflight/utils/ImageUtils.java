package com.parrot.freeflight.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.FloatMath;
import android.util.Log;
import com.parrot.freeflight.track_3d_viewer.utils.Holder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public final class ImageUtils {
    private static final String TAG = ImageUtils.class.getSimpleName();

    private ImageUtils() {
    }

    public static Bitmap changeBitmapContrastBrightness(Bitmap bitmap, float f, float f2) {
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{f, 0.0f, 0.0f, 0.0f, f2, 0.0f, f, 0.0f, 0.0f, f2, 0.0f, 0.0f, f, 0.0f, f2, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return createBitmap;
    }

    public static Bitmap decodeBitmapFromFile(String str, int i, int i2) {
        if (i <= 0 || i2 <= 0) {
            return BitmapFactory.decodeFile(str);
        }
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        int ceil = (int) FloatMath.ceil(((float) options.outHeight) / ((float) i2));
        int ceil2 = (int) FloatMath.ceil(((float) options.outWidth) / ((float) i));
        if (ceil > 1 || ceil2 > 1) {
            if (ceil > ceil2) {
                options.inSampleSize = ceil;
            } else {
                options.inSampleSize = ceil2;
            }
        }
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(str, options);
    }

    public static void getBitmapDims(Resources resources, int i, Holder<Integer> holder, Holder<Integer> holder2) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, i, options);
        float f = ((float) options.inTargetDensity) / ((float) options.inDensity);
        holder.value = Integer.valueOf((int) ((((float) options.outWidth) * f) + 0.5f));
        holder2.value = Integer.valueOf((int) ((((float) options.outHeight) * f) + 0.5f));
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, float f) {
        Matrix matrix = new Matrix();
        matrix.postRotate(f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static boolean saveBitmap(File file, Bitmap bitmap) {
        FileOutputStream fileOutputStream;
        boolean compress;
        FileNotFoundException e;
        Throwable th;
        try {
            fileOutputStream = new FileOutputStream(file);
            try {
                compress = bitmap.compress(CompressFormat.PNG, 100, fileOutputStream);
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.flush();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    try {
                        fileOutputStream.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
            } catch (FileNotFoundException e4) {
                e = e4;
                try {
                    Log.w(TAG, e.toString());
                    if (fileOutputStream == null) {
                        compress = false;
                        return compress;
                    }
                    try {
                        fileOutputStream.flush();
                    } catch (IOException e5) {
                        e5.printStackTrace();
                    }
                    try {
                        fileOutputStream.close();
                        return false;
                    } catch (IOException e52) {
                        e52.printStackTrace();
                        return false;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.flush();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                        try {
                            fileOutputStream.close();
                        } catch (IOException e32) {
                            e32.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                throw th;
            }
        } catch (FileNotFoundException e6) {
            e = e6;
            fileOutputStream = null;
            Log.w(TAG, e.toString());
            if (fileOutputStream == null) {
                fileOutputStream.flush();
                fileOutputStream.close();
                return false;
            }
            compress = false;
            return compress;
        } catch (Throwable th4) {
            th = th4;
            fileOutputStream = null;
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
            throw th;
        }
        return compress;
    }
}
