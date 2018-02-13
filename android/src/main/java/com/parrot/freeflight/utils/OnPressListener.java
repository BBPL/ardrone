package com.parrot.freeflight.utils;

import android.view.View;

public interface OnPressListener {
    void onClick(View view);

    void onRelease(View view);

    void onRepeated(View view);
}
