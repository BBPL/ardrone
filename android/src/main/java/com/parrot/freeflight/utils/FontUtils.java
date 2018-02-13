package com.parrot.freeflight.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import com.parrot.freeflight.ui.widgets.ParrotSeekBar;

public class FontUtils {

    public static final class TYPEFACE {
        private static Typeface helvetica;

        public static final Typeface Helvetica(Context context) {
            if (helvetica == null) {
                helvetica = Typeface.createFromAsset(context.getAssets(), "fonts/helveticaneue-condensedbold.otf");
            }
            return helvetica;
        }
    }

    public static void applyFont(Context context, View view) {
        if (view instanceof ViewGroup) {
            applyFont(context, (ViewGroup) view);
        } else if (view instanceof TextView) {
            applyFont(context, (TextView) view);
        } else if (view instanceof ParrotSeekBar) {
            applyFont(context, (ParrotSeekBar) view);
        } else if (view instanceof RadioButton) {
            applyFont(context, (RadioButton) view);
        }
    }

    public static void applyFont(Context context, ViewGroup viewGroup) {
        Typeface Helvetica = TYPEFACE.Helvetica(context);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt instanceof ViewGroup) {
                applyFont(context, (ViewGroup) childAt);
            } else if (childAt instanceof TextView) {
                ((TextView) childAt).setTypeface(Helvetica);
            }
        }
    }

    public static void applyFont(Context context, RadioButton radioButton) {
        radioButton.setTypeface(TYPEFACE.Helvetica(context));
    }

    public static void applyFont(Context context, TextView textView) {
        textView.setTypeface(TYPEFACE.Helvetica(context));
    }

    public static void applyFont(Context context, ParrotSeekBar parrotSeekBar) {
        parrotSeekBar.setTypeface(TYPEFACE.Helvetica(context));
    }
}
