package com.parrot.freeflight.academy.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.LinearLayout;

public class AnimatedView extends LinearLayout {
    private Animation inAnimation;
    private Animation outAnimation;

    public AnimatedView(Context context) {
        super(context);
    }

    public AnimatedView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setInAnimation(Animation animation) {
        this.inAnimation = animation;
    }

    public void setOutAnimation(Animation animation) {
        this.outAnimation = animation;
    }

    public void setVisibility(int i) {
        if (getVisibility() != i) {
            if (i == 0) {
                if (this.inAnimation != null) {
                    startAnimation(this.inAnimation);
                }
            } else if ((i == 4 || i == 8) && this.outAnimation != null) {
                startAnimation(this.outAnimation);
            }
        }
        super.setVisibility(i);
    }
}
