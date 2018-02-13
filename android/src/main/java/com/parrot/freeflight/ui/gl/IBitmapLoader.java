package com.parrot.freeflight.ui.gl;

import android.graphics.Bitmap;
import com.parrot.freeflight.track_3d_viewer.utils.Holder;

public interface IBitmapLoader {
    void getBitmapDims(Holder<Integer> holder, Holder<Integer> holder2);

    Bitmap loadBitmap();
}
