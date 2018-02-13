package com.google.android.gms.plus;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.http.ExponentialBackOffPolicy;

public class PlusOneDummyView extends FrameLayout {
    public static final String TAG = "PlusOneDummyView";

    private interface C0363d {
        Drawable getDrawable(int i);

        boolean isValid();
    }

    private static class C0364a implements C0363d {
        private Context mContext;

        private C0364a(Context context) {
            this.mContext = context;
        }

        public Drawable getDrawable(int i) {
            return this.mContext.getResources().getDrawable(17301508);
        }

        public boolean isValid() {
            return true;
        }
    }

    private static class C0365b implements C0363d {
        private Context mContext;

        private C0365b(Context context) {
            this.mContext = context;
        }

        public Drawable getDrawable(int i) {
            try {
                String str;
                Resources resources = this.mContext.createPackageContext(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE, 4).getResources();
                switch (i) {
                    case 0:
                        str = "ic_plusone_small";
                        break;
                    case 1:
                        str = "ic_plusone_medium";
                        break;
                    case 2:
                        str = "ic_plusone_tall";
                        break;
                    default:
                        str = "ic_plusone_standard";
                        break;
                }
                return resources.getDrawable(resources.getIdentifier(str, "drawable", GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE));
            } catch (NameNotFoundException e) {
                return null;
            }
        }

        public boolean isValid() {
            try {
                this.mContext.createPackageContext(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE, 4).getResources();
                return true;
            } catch (NameNotFoundException e) {
                return false;
            }
        }
    }

    private static class C0366c implements C0363d {
        private Context mContext;

        private C0366c(Context context) {
            this.mContext = context;
        }

        public Drawable getDrawable(int i) {
            String str;
            switch (i) {
                case 0:
                    str = "ic_plusone_small_off_client";
                    break;
                case 1:
                    str = "ic_plusone_medium_off_client";
                    break;
                case 2:
                    str = "ic_plusone_tall_off_client";
                    break;
                default:
                    str = "ic_plusone_standard_off_client";
                    break;
            }
            return this.mContext.getResources().getDrawable(this.mContext.getResources().getIdentifier(str, "drawable", this.mContext.getPackageName()));
        }

        public boolean isValid() {
            return (this.mContext.getResources().getIdentifier("ic_plusone_small_off_client", "drawable", this.mContext.getPackageName()) == 0 || this.mContext.getResources().getIdentifier("ic_plusone_medium_off_client", "drawable", this.mContext.getPackageName()) == 0 || this.mContext.getResources().getIdentifier("ic_plusone_tall_off_client", "drawable", this.mContext.getPackageName()) == 0 || this.mContext.getResources().getIdentifier("ic_plusone_standard_off_client", "drawable", this.mContext.getPackageName()) == 0) ? false : true;
        }
    }

    public PlusOneDummyView(Context context, int i) {
        super(context);
        View button = new Button(context);
        button.setEnabled(false);
        button.setBackgroundDrawable(bx().getDrawable(i));
        Point T = m1401T(i);
        addView(button, new LayoutParams(T.x, T.y, 17));
    }

    private Point m1401T(int i) {
        int i2;
        int i3 = 24;
        Point point = new Point();
        switch (i) {
            case 0:
                i2 = 24;
                i3 = 14;
                break;
            case 1:
                i2 = 32;
                i3 = 20;
                break;
            case 2:
                i2 = 50;
                i3 = 20;
                break;
            default:
                i2 = 38;
                break;
        }
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float applyDimension = TypedValue.applyDimension(1, (float) i2, displayMetrics);
        float applyDimension2 = TypedValue.applyDimension(1, (float) i3, displayMetrics);
        point.x = (int) (((double) applyDimension) + ExponentialBackOffPolicy.DEFAULT_RANDOMIZATION_FACTOR);
        point.y = (int) (((double) applyDimension2) + ExponentialBackOffPolicy.DEFAULT_RANDOMIZATION_FACTOR);
        return point;
    }

    private C0363d bx() {
        C0363d c0365b = new C0365b(getContext());
        if (!c0365b.isValid()) {
            c0365b = new C0366c(getContext());
        }
        return !c0365b.isValid() ? new C0364a(getContext()) : c0365b;
    }
}
