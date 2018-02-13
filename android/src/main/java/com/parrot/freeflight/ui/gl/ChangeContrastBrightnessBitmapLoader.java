package com.parrot.freeflight.ui.gl;

import android.content.res.Resources;
import android.graphics.Bitmap;
import com.parrot.freeflight.utils.ImageUtils;

public class ChangeContrastBrightnessBitmapLoader extends ResourceBitmapLoader {
    private final float brightness;
    private final float contrast;

    public ChangeContrastBrightnessBitmapLoader(Resources resources, int i, float f, float f2) {
        super(resources, i);
        this.contrast = f;
        this.brightness = f2;
    }

    public Bitmap loadBitmap() {
        Bitmap loadBitmap = super.loadBitmap();
        Bitmap changeBitmapContrastBrightness = ImageUtils.changeBitmapContrastBrightness(loadBitmap, this.contrast, this.brightness);
        loadBitmap.recycle();
        return changeBitmapContrastBrightness;
    }
}
