package com.parrot.freeflight.utils;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

@SuppressLint({"NewApi"})
public final class AnimationUtils {
    private AnimationUtils() {
    }

    private static AnimationListener getFadeInListener(final View view) {
        return new AnimationListener() {
            public void onAnimationEnd(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
                view.setVisibility(0);
            }
        };
    }

    private static AnimationListener getFadeOutListener(final View view) {
        return new AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(4);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        };
    }

    public static Animation makeInvisibleAnimated(View view) {
        Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setAnimationListener(getFadeOutListener(view));
        view.startAnimation(alphaAnimation);
        return alphaAnimation;
    }

    @SuppressLint({"NewApi"})
    public static Animation makeVisibleAnimated(View view) {
        Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setAnimationListener(getFadeInListener(view));
        view.startAnimation(alphaAnimation);
        return alphaAnimation;
    }
}
