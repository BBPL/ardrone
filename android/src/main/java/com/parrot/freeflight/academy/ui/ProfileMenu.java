package com.parrot.freeflight.academy.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build.VERSION;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.utils.AnimationUtils;

public class ProfileMenu {
    private final Activity activity;
    private Animation animationCurrent;
    private final View view;

    public enum Background {
        ACCENT,
        ACCENT_HALF_TRANSP
    }

    public ProfileMenu(Activity activity, View view) {
        this.view = view;
        this.activity = activity;
    }

    public void changeBackground(Background background) {
        RelativeLayout relativeLayout = (RelativeLayout) this.view;
        switch (background) {
            case ACCENT:
                relativeLayout.setBackgroundResource(C0984R.color.accent);
                return;
            case ACCENT_HALF_TRANSP:
                relativeLayout.setBackgroundResource(C0984R.color.accent_half_transp);
                return;
            default:
                return;
        }
    }

    public View getView() {
        return this.view;
    }

    public void hide(boolean z) {
        if (!z) {
            this.view.setVisibility(4);
        } else if (this.animationCurrent == null || this.animationCurrent.hasEnded()) {
            this.animationCurrent = AnimationUtils.makeInvisibleAnimated(this.view);
        }
    }

    public void initEditButton(OnClickListener onClickListener) {
        Button button = (Button) this.view.findViewById(C0984R.id.editBtn);
        button.setOnClickListener(onClickListener);
        button.setVisibility(0);
    }

    public void initPrivacyButton(OnClickListener onClickListener) {
        Button button = (Button) this.view.findViewById(C0984R.id.privacyBtn);
        button.setOnClickListener(onClickListener);
        button.setVisibility(0);
    }

    @SuppressLint({"NewApi"})
    public boolean isVisible() {
        if (VERSION.SDK_INT >= 11) {
            if (this.view.getVisibility() != 0 || this.view.getAlpha() < 0.0f) {
                return false;
            }
        } else if (this.view.getVisibility() != 0) {
            return false;
        }
        return true;
    }

    public void show(boolean z) {
        if (!z) {
            this.view.setVisibility(0);
        } else if (this.animationCurrent == null || this.animationCurrent.hasEnded()) {
            this.animationCurrent = AnimationUtils.makeVisibleAnimated(this.view);
        }
    }
}
