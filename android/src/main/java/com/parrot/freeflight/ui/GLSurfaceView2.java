package com.parrot.freeflight.ui;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;

public class GLSurfaceView2 extends GLSurfaceView {
    private Renderer renderer;

    public GLSurfaceView2(Context context) {
        super(context);
    }

    public Renderer getRenderer() {
        return this.renderer;
    }

    public void setRenderer(Renderer renderer) {
        super.setRenderer(renderer);
        this.renderer = renderer;
    }
}
