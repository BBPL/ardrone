package com.parrot.freeflight.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.academy.activities.AcademyProfileActivity;
import com.parrot.freeflight.academy.utils.AcademyUtils;
import com.parrot.freeflight.activities.DashboardActivity;
import com.parrot.freeflight.utils.AnimationUtils;
import org.mortbay.jetty.HttpVersions;

public class ActionBar implements OnClickListener {
    private final Activity activity;
    private Animation animationCurrent;
    private final OnClickListener backBtnCLickListener = new C12042();
    private ImageButton btnSettings;
    private final OnClickListener homeBtnCLickListener = new C12031();
    private final OnClickListener logoutBtnClickListener = new C12053();
    private final OnClickListener plusBtnClickListener = new C12075();
    private final OnClickListener settingsBtnClickListener = new C12064();
    private final View view;
    private WebView webView;

    class C12031 implements OnClickListener {
        C12031() {
        }

        public void onClick(View view) {
            ActionBar.this.startDashboardActivity();
        }
    }

    class C12042 implements OnClickListener {
        C12042() {
        }

        public void onClick(View view) {
            ActionBar.this.activity.finish();
        }
    }

    class C12053 implements OnClickListener {
        C12053() {
        }

        public void onClick(View view) {
            AcademyUtils.clearCredentials(view.getContext());
            ActionBar.this.startDashboardActivity();
        }
    }

    class C12064 implements OnClickListener {
        C12064() {
        }

        public void onClick(View view) {
            ActionBar.this.showSettings();
        }
    }

    class C12075 implements OnClickListener {
        C12075() {
        }

        public void onClick(View view) {
        }
    }

    public enum Background {
        ACCENT,
        ACCENT_HALF_TRANSP
    }

    public ActionBar(Activity activity, View view) {
        this.view = view;
        this.activity = activity;
        this.btnSettings = (ImageButton) view.findViewById(C0984R.id.btn_right);
    }

    private void startDashboardActivity() {
        this.activity.startActivity(new Intent(this.activity, DashboardActivity.class));
        this.activity.finish();
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

    public String getTitle() {
        return ((TextView) this.view.findViewById(C0984R.id.txt_title)).getText().toString();
    }

    public View getView() {
        return this.view;
    }

    public void hide(boolean z) {
        if (!z || this.view.getVisibility() != 0) {
            this.view.setVisibility(4);
        } else if (this.animationCurrent == null || this.animationCurrent.hasEnded()) {
            this.animationCurrent = AnimationUtils.makeInvisibleAnimated(this.view);
        }
    }

    public void hideBackButton() {
        ((ImageButton) this.view.findViewById(C0984R.id.btn_back_home)).setVisibility(8);
    }

    public void hideDelayed(int i, final boolean z) {
        if (i < 0) {
            throw new IllegalArgumentException("Delay should be positive");
        }
        this.view.postDelayed(new Runnable() {
            public void run() {
                ActionBar.this.hide(z);
            }
        }, 3000);
    }

    public void hideSettingsButton() {
        this.btnSettings.setVisibility(4);
    }

    public void hideShareButton() {
        ((Button) this.view.findViewById(C0984R.id.btnShare)).setVisibility(8);
    }

    public void initBackButton() {
        ImageButton imageButton = (ImageButton) this.view.findViewById(C0984R.id.btn_back_home);
        imageButton.setOnClickListener(this.backBtnCLickListener);
        imageButton.setVisibility(0);
        imageButton.setImageDrawable(this.activity.getResources().getDrawable(C0984R.drawable.btn_back_arrow));
    }

    public void initBackButton(OnClickListener onClickListener) {
        ImageButton imageButton = (ImageButton) this.view.findViewById(C0984R.id.btn_back_home);
        imageButton.setOnClickListener(onClickListener);
        imageButton.setVisibility(0);
        imageButton.setImageDrawable(this.activity.getResources().getDrawable(C0984R.drawable.btn_back_arrow));
    }

    public void initHomeButton() {
        ImageButton imageButton = (ImageButton) this.view.findViewById(C0984R.id.btn_back_home);
        imageButton.setOnClickListener(this.homeBtnCLickListener);
        imageButton.setVisibility(0);
        imageButton.setImageDrawable(this.activity.getResources().getDrawable(C0984R.drawable.btn_home));
    }

    public void initLogoutButton() {
        ImageButton imageButton = (ImageButton) this.view.findViewById(C0984R.id.btn_right);
        imageButton.setOnClickListener(this.logoutBtnClickListener);
        imageButton.setVisibility(0);
        imageButton.setImageDrawable(this.activity.getResources().getDrawable(C0984R.drawable.ff2_1_logout));
    }

    public void initMapSettingsButton(OnClickListener onClickListener) {
        this.btnSettings.setOnClickListener(onClickListener);
        this.btnSettings.setVisibility(0);
        this.btnSettings.setImageDrawable(this.activity.getResources().getDrawable(C0984R.drawable.ff2_2_plus_button));
    }

    public void initPlusToggleButton(OnCheckedChangeListener onCheckedChangeListener) {
        CompoundButton compoundButton = (CompoundButton) this.view.findViewById(C0984R.id.action_bar_togglebutton_plus);
        compoundButton.setOnCheckedChangeListener(onCheckedChangeListener);
        compoundButton.setVisibility(0);
    }

    public void initProfileSettingsButton() {
        this.btnSettings.setOnClickListener(this.settingsBtnClickListener);
        this.btnSettings.setVisibility(0);
        this.btnSettings.setImageDrawable(this.activity.getResources().getDrawable(C0984R.drawable.settings_white));
    }

    public void initSettingsButton(OnClickListener onClickListener) {
        this.btnSettings.setOnClickListener(onClickListener);
        this.btnSettings.setVisibility(0);
        this.btnSettings.setImageDrawable(this.activity.getResources().getDrawable(C0984R.drawable.settings_white));
    }

    public void initShareButton(OnClickListener onClickListener) {
        Button button = (Button) this.view.findViewById(C0984R.id.btnShare);
        button.setOnClickListener(onClickListener);
        button.setVisibility(0);
    }

    public void initUserProfileButton(String str, OnClickListener onClickListener) {
        TextView textView = (TextView) this.view.findViewById(C0984R.id.action_bar_button_profile);
        textView.setText(str);
        textView.setOnClickListener(onClickListener);
        textView.setVisibility(0);
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

    public void onClick(View view) {
        if (this.webView != null) {
            switch (view.getId()) {
                case C0984R.id.btnGoBack /*2131362326*/:
                    this.webView.goBack();
                    view.setEnabled(this.webView.canGoBack());
                    return;
                case C0984R.id.btnGoForward /*2131362327*/:
                    this.webView.goForward();
                    view.setEnabled(this.webView.canGoForward());
                    return;
                default:
                    return;
            }
        }
    }

    public void refreshWebButtonState() {
        if (this.webView != null) {
            View findViewById = this.view.findViewById(C0984R.id.btnGoForward);
            this.view.findViewById(C0984R.id.btnGoBack).setEnabled(this.webView.canGoBack());
            findViewById.setEnabled(this.webView.canGoForward());
        }
    }

    public void setTitle(String str) {
        TextView textView = (TextView) this.view.findViewById(C0984R.id.txt_title);
        textView.setText(str);
        textView.setVisibility(0);
    }

    public void setWebView(WebView webView) {
        this.webView = webView;
        this.view.findViewById(C0984R.id.webButtons).setVisibility(webView != null ? 0 : 8);
        View findViewById = this.view.findViewById(C0984R.id.btnGoForward);
        View findViewById2 = this.view.findViewById(C0984R.id.btnGoBack);
        findViewById.setOnClickListener(this);
        findViewById2.setOnClickListener(this);
        if (webView != null) {
            findViewById2.setEnabled(webView.canGoBack());
            findViewById.setEnabled(webView.canGoForward());
        }
    }

    public void show(boolean z) {
        if (!isVisible()) {
            if (!z) {
                this.view.setVisibility(0);
            } else if (this.animationCurrent == null || this.animationCurrent.hasEnded()) {
                this.animationCurrent = AnimationUtils.makeVisibleAnimated(this.view);
            }
        }
    }

    public void showBackButton() {
        ((ImageButton) this.view.findViewById(C0984R.id.btn_back_home)).setVisibility(0);
    }

    public void showSettings() {
        if (this.activity instanceof AcademyProfileActivity) {
            Log.i("SHOW SETTINGS", HttpVersions.HTTP_0_9);
            View findViewById = this.activity.findViewById(C0984R.id.profile_menu);
            Animation loadAnimation = android.view.animation.AnimationUtils.loadAnimation(findViewById.getContext(), C0984R.anim.slide_left_in);
            Animation loadAnimation2 = android.view.animation.AnimationUtils.loadAnimation(findViewById.getContext(), C0984R.anim.slide_right_out);
            if (findViewById.getVisibility() == 4) {
                loadAnimation2 = loadAnimation;
            }
            findViewById.setAnimation(loadAnimation2);
            findViewById.setVisibility(findViewById.getVisibility() == 4 ? 0 : 4);
            if (findViewById.getVisibility() == 0) {
                findViewById.bringToFront();
            }
        }
    }

    public void showSettingsButton() {
        this.btnSettings.setVisibility(0);
    }
}
