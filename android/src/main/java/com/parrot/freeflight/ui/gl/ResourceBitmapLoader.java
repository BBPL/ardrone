package com.parrot.freeflight.ui.gl;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.parrot.freeflight.track_3d_viewer.utils.Holder;
import com.parrot.freeflight.utils.ImageUtils;

public class ResourceBitmapLoader implements IBitmapLoader {
    private final int bitmapResId;
    private final Resources res;

    public ResourceBitmapLoader(Resources resources, int i) {
        this.res = resources;
        this.bitmapResId = i;
    }

    public void getBitmapDims(Holder<Integer> holder, Holder<Integer> holder2) {
        ImageUtils.getBitmapDims(this.res, this.bitmapResId, holder, holder2);
    }

    public Bitmap loadBitmap() {
        return BitmapFactory.decodeResource(this.res, this.bitmapResId);
    }
}
