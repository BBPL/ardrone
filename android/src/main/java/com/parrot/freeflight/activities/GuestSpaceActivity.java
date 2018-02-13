package com.parrot.freeflight.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import com.parrot.ardronetool.DataTracker;
import com.parrot.ardronetool.tracking.TRACK_KEY_ENUM;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.academy.activities.AcademyRegisterActivity;
import com.parrot.freeflight.activities.base.ParrotActivity;
import com.parrot.freeflight.ui.ActionBar;
import com.parrot.freeflight.ui.adapters.InfosAdapter;
import com.parrot.freeflight.ui.widgets.WebView2;
import com.parrot.freeflight.utils.SystemUtils;
import org.mortbay.jetty.HttpVersions;
import org.mortbay.jetty.MimeTypes;

public class GuestSpaceActivity extends ParrotActivity implements OnPageChangeListener, OnClickListener, OnCheckedChangeListener {
    private static final String spare_parts_url = "http://www.youtube.com/playlist?list=PLDB67195526DEEE63";
    private ActionBar actionBar;
    private int from;
    private int[] infosPages = new int[]{C0984R.layout.guest_space_informations_page_fly_record_hd, C0984R.layout.guest_space_informations_page_camera, C0984R.layout.guest_space_informations_page_absolute_control, C0984R.layout.guest_space_informations_page_super_stable, C0984R.layout.guest_space_informations_page_share, C0984R.layout.guest_space_informations_page_video_games, C0984R.layout.guest_space_informations_page_acrobatics, C0984R.layout.guest_space_informations_page_spare_parts};
    private ViewGroup mContentView;
    private View mCustomView;
    private CustomViewCallback mCustomViewCallback;
    private FrameLayout mCustomViewContainer;
    private MyWebChromeClient webChromeClient;
    private ParrotWebViewClient webViewClient;

    private class MyWebChromeClient extends WebChromeClient {
        LayoutParams LayoutParameters;

        private MyWebChromeClient() {
            this.LayoutParameters = new LayoutParams(-1, -1);
        }

        public View getVideoLoadingProgressView() {
            return new ProgressBar(GuestSpaceActivity.this);
        }

        public boolean isShowingVideo() {
            return GuestSpaceActivity.this.mCustomView != null;
        }

        public void onHideCustomView() {
            if (GuestSpaceActivity.this.mCustomView != null) {
                GuestSpaceActivity.this.mCustomView.setVisibility(8);
                GuestSpaceActivity.this.mCustomViewContainer.removeView(GuestSpaceActivity.this.mCustomView);
                GuestSpaceActivity.this.mCustomView = null;
                GuestSpaceActivity.this.mCustomViewContainer.setVisibility(8);
                GuestSpaceActivity.this.mCustomViewCallback.onCustomViewHidden();
                GuestSpaceActivity.this.mContentView.setVisibility(0);
                GuestSpaceActivity.this.setContentView(GuestSpaceActivity.this.mContentView);
            }
        }

        public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
            if (GuestSpaceActivity.this.mCustomView != null) {
                customViewCallback.onCustomViewHidden();
                return;
            }
            GuestSpaceActivity.this.mContentView = (ViewGroup) GuestSpaceActivity.this.findViewById(C0984R.id.guestSpaceRoot);
            GuestSpaceActivity.this.mContentView.setVisibility(8);
            GuestSpaceActivity.this.mCustomViewContainer = new FrameLayout(GuestSpaceActivity.this);
            GuestSpaceActivity.this.mCustomViewContainer.setLayoutParams(this.LayoutParameters);
            GuestSpaceActivity.this.mCustomViewContainer.setBackgroundResource(17170444);
            view.setLayoutParams(this.LayoutParameters);
            GuestSpaceActivity.this.mCustomViewContainer.addView(view);
            GuestSpaceActivity.this.mCustomView = view;
            GuestSpaceActivity.this.mCustomViewCallback = customViewCallback;
            GuestSpaceActivity.this.mCustomViewContainer.setVisibility(0);
            GuestSpaceActivity.this.setContentView(GuestSpaceActivity.this.mCustomViewContainer);
        }
    }

    private class ParrotWebViewClient extends WebViewClient {
        private String youtubeVideoId;

        private ParrotWebViewClient() {
        }

        private void parseVideoId(String str) {
            int indexOf;
            int i;
            Object obj = null;
            int indexOf2;
            if (str.startsWith("http://s.youtube.com/s") && str.indexOf("playback=1") != -1) {
                indexOf2 = str.indexOf("&docid=") + "&docid=".length();
                indexOf = str.indexOf(38, indexOf2 + 1);
                i = indexOf2;
                obj = 1;
            } else if (str.startsWith("http://m.youtube.com/watch")) {
                indexOf2 = str.indexOf("&v=") + "&v=".length();
                indexOf = str.indexOf(38, indexOf2 + 1);
                i = indexOf2;
                indexOf2 = 1;
            } else {
                i = -1;
                indexOf = -1;
            }
            if (obj != null) {
                if (i > -1 && r0 == -1) {
                    indexOf = str.length();
                }
                this.youtubeVideoId = str.substring(i, indexOf);
            }
        }

        public void doUpdateVisitedHistory(WebView webView, String str, boolean z) {
            super.doUpdateVisitedHistory(webView, str, z);
            GuestSpaceActivity.this.actionBar.refreshWebButtonState();
        }

        public String getYoutubeVideoId() {
            return this.youtubeVideoId;
        }

        public void onPageFinished(WebView webView, String str) {
            GuestSpaceActivity.this.showLoadingIndicator(false);
            parseVideoId(str);
            super.onPageFinished(webView, str);
        }

        public void onReceivedError(WebView webView, int i, String str, String str2) {
            webView.loadData(HttpVersions.HTTP_0_9, MimeTypes.TEXT_HTML, null);
            GuestSpaceActivity.this.showLoadingIndicator(false);
            GuestSpaceActivity.this.showErrorMessage(true);
        }

        @TargetApi(11)
        public WebResourceResponse shouldInterceptRequest(WebView webView, String str) {
            parseVideoId(str);
            return super.shouldInterceptRequest(webView, str);
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            if (str.startsWith("vnd.youtube")) {
                GuestSpaceActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
            } else if (!str.equalsIgnoreCase("file:///android_asset/webkit/")) {
                webView.loadUrl(str);
            }
            return true;
        }
    }

    private void initActionBarFromDashBoard() {
        this.actionBar = new ActionBar(this, findViewById(C0984R.id.navigation_bar));
        this.actionBar.setTitle(getString(C0984R.string.ARDRONE_2_0));
        this.actionBar.initHomeButton();
    }

    private void initActionBarFromLogin() {
        this.actionBar = new ActionBar(this, findViewById(C0984R.id.navigation_bar));
        this.actionBar.setTitle(getString(C0984R.string.ARDRONE_2_0));
        this.actionBar.initBackButton();
    }

    private void initStayTunedScreen() {
        this.actionBar.setWebView(null);
        TextView textView = (TextView) findViewById(C0984R.id.textUrlWeb);
        TextView textView2 = (TextView) findViewById(C0984R.id.textWeb);
        ((ImageView) findViewById(C0984R.id.imageWeb)).setOnClickListener(this);
        textView.setOnClickListener(this);
        textView2.setOnClickListener(this);
        textView = (TextView) findViewById(C0984R.id.textSignUp);
        textView2 = (TextView) findViewById(C0984R.id.textNews);
        ((ImageView) findViewById(C0984R.id.imageNews)).setOnClickListener(this);
        textView.setOnClickListener(this);
        textView2.setOnClickListener(this);
        textView = (TextView) findViewById(C0984R.id.textFacebook);
        textView2 = (TextView) findViewById(C0984R.id.textLike);
        ((ImageView) findViewById(C0984R.id.imageFacebook)).setOnClickListener(this);
        textView.setOnClickListener(this);
        textView2.setOnClickListener(this);
        textView = (TextView) findViewById(C0984R.id.textTwitter);
        textView2 = (TextView) findViewById(C0984R.id.textFollowUp);
        ((ImageView) findViewById(C0984R.id.imageTwitter)).setOnClickListener(this);
        textView.setOnClickListener(this);
        textView2.setOnClickListener(this);
    }

    private void initWhereToBuySceen() {
        WebView webView = (WebView) findViewById(C0984R.id.webView);
        webView.clearHistory();
        if (SystemUtils.isNook()) {
            webView.loadUrl(getString(C0984R.string.url_where_to_buy_nook));
        } else {
            webView.loadUrl(getString(C0984R.string.url_where_to_buy));
        }
    }

    private void openBrowserActivity(String str) {
        Intent intent = new Intent(this, BrowserActivity.class);
        intent.putExtra("url", str);
        startActivity(intent);
    }

    private void openInformations() {
        switchToLayout(C0984R.layout.guest_space_informations);
        initInformationsScreen();
    }

    private void openStayTuned() {
        switchToLayout(C0984R.layout.guest_space_stay_tuned);
        initStayTunedScreen();
    }

    private void openUserVideos() {
        showBrowserView();
        initUserVideosScreen();
    }

    private void openWhereToBuy() {
        showBrowserView();
        initWhereToBuySceen();
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void showBrowserView() {
        boolean z;
        switchToLayout(C0984R.layout.browse_web_screen);
        WebView webView = (WebView) findViewById(C0984R.id.webView);
        this.actionBar.setWebView(webView);
        if (this.webViewClient == null) {
            this.webViewClient = new ParrotWebViewClient();
        }
        if (this.webChromeClient == null) {
            this.webChromeClient = new MyWebChromeClient();
        }
        webView.setWebViewClient(this.webViewClient);
        webView.setWebChromeClient(this.webChromeClient);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        try {
            z = getPackageManager().getApplicationInfo("com.adobe.flashplayer", 0) != null;
        } catch (NameNotFoundException e) {
            z = false;
        }
        settings.setPluginState(z ? PluginState.OFF : PluginState.ON);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportMultipleWindows(false);
        settings.setDefaultZoom(ZoomDensity.FAR);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(false);
        settings.setUseWideViewPort(true);
        webView.setInitialScale(0);
    }

    private void switchToLayout(int i) {
        View inflateView = inflateView(i, null, false);
        ViewGroup viewGroup = (ViewGroup) findViewById(C0984R.id.content);
        viewGroup.removeAllViewsInLayout();
        viewGroup.addView(inflateView, new ViewGroup.LayoutParams(-1, -1));
    }

    public void initInformationsScreen() {
        this.actionBar.setWebView(null);
        ViewPager viewPager = (ViewPager) findViewById(C0984R.id.infoPager);
        if (viewPager != null) {
            viewPager.setAdapter(new InfosAdapter(this.infosPages));
            viewPager.setOnPageChangeListener(this);
            if (this.from == 0) {
                viewPager.setCurrentItem(4);
            } else {
                viewPager.setCurrentItem(0);
            }
        }
        findViewById(C0984R.id.btnPrev).setOnClickListener(this);
        findViewById(C0984R.id.btnNext).setOnClickListener(this);
    }

    public void initUserVideosScreen() {
        WebView webView = (WebView) findViewById(C0984R.id.webView);
        webView.clearHistory();
        webView.loadUrl(getString(C0984R.string.url_user_videos));
    }

    public void onBackPressed() {
        if (this.webChromeClient == null || !this.webChromeClient.isShowingVideo()) {
            WebView webView = (WebView) findViewById(C0984R.id.webView);
            if (webView == null) {
                super.onBackPressed();
                return;
            } else if (webView == null || !webView.canGoBack()) {
                super.onBackPressed();
                return;
            } else {
                webView.goBack();
                return;
            }
        }
        this.webChromeClient.onHideCustomView();
    }

    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case C0984R.id.rbInformations /*2131362125*/:
                openInformations();
                return;
            case C0984R.id.rbUserVideos /*2131362126*/:
                openUserVideos();
                return;
            case C0984R.id.rbStayTuned /*2131362127*/:
                openStayTuned();
                return;
            case C0984R.id.rbWheretoBuy /*2131362128*/:
                openWhereToBuy();
                return;
            default:
                return;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0984R.id.sign_up /*2131361945*/:
                startActivity(new Intent(this, AcademyRegisterActivity.class));
                return;
            case C0984R.id.btnPrev /*2131362105*/:
                onPrevPageClicked();
                return;
            case C0984R.id.btnNext /*2131362106*/:
                onNextPageClicked();
                return;
            case C0984R.id.watch_videos /*2131362122*/:
                openBrowserActivity(spare_parts_url);
                return;
            case C0984R.id.imageWeb /*2131362129*/:
            case C0984R.id.textUrlWeb /*2131362130*/:
            case C0984R.id.textWeb /*2131362131*/:
                openBrowserActivity(getString(C0984R.string.url_ardrone_website));
                return;
            case C0984R.id.imageNews /*2131362132*/:
            case C0984R.id.textNews /*2131362133*/:
            case C0984R.id.textSignUp /*2131362134*/:
                openBrowserActivity(getString(C0984R.string.url_newsletter));
                return;
            case C0984R.id.imageFacebook /*2131362136*/:
            case C0984R.id.textFacebook /*2131362137*/:
            case C0984R.id.textLike /*2131362138*/:
                openBrowserActivity(getString(C0984R.string.url_facebook));
                return;
            case C0984R.id.imageTwitter /*2131362139*/:
            case C0984R.id.textTwitter /*2131362140*/:
            case C0984R.id.textFollowUp /*2131362141*/:
                openBrowserActivity(getString(C0984R.string.url_twitter));
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C0984R.layout.guest_space_screen);
        RadioGroup radioGroup = (RadioGroup) findViewById(C0984R.id.bottomBar);
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            ((RadioButton) radioGroup.getChildAt(i)).setText(((RadioButton) radioGroup.getChildAt(i)).getText().toString().toUpperCase());
        }
        radioGroup.setOnCheckedChangeListener(this);
        this.from = getIntent().getExtras().getInt("from", 0);
        switch (this.from) {
            case 0:
                initActionBarFromLogin();
                initInformationsScreen();
                this.from = -1;
                break;
            case 1:
                initActionBarFromDashBoard();
                radioGroup.getChildAt(this.from).performClick();
                this.from = -1;
                break;
            default:
                initActionBarFromDashBoard();
                initInformationsScreen();
                break;
        }
        DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__GET_YOUR_ARDRONE_ZONE_OPEN);
    }

    protected void onDestroy() {
        DataTracker.trackInfoVoid(TRACK_KEY_ENUM.TRACK_KEY_EVENT__GET_YOUR_ARDRONE_ZONE_CLOSE);
        try {
            ((WebView) findViewById(C0984R.id.webView)).goBack();
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    protected void onNextPageClicked() {
        ViewPager viewPager = (ViewPager) findViewById(C0984R.id.infoPager);
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
    }

    public void onPageScrollStateChanged(int i) {
    }

    public void onPageScrolled(int i, float f, int i2) {
    }

    public void onPageSelected(int i) {
        View findViewById;
        if (i == 0) {
            findViewById = findViewById(C0984R.id.btnPrev);
            if (findViewById != null) {
                findViewById.setVisibility(4);
            }
        } else if (i == 1) {
            findViewById = findViewById(C0984R.id.btnPrev);
            if (findViewById != null) {
                findViewById.setVisibility(0);
            }
        }
        if (i == 4) {
            findViewById = findViewById(C0984R.id.sign_up);
            if (findViewById != null) {
                findViewById.setOnClickListener(this);
            }
        }
        if (i == this.infosPages.length - 1) {
            findViewById = findViewById(C0984R.id.btnNext);
            if (findViewById != null) {
                findViewById.setVisibility(4);
            }
            findViewById = findViewById(C0984R.id.watch_videos);
            if (findViewById != null) {
                findViewById.setOnClickListener(this);
            }
        } else if (i == this.infosPages.length - 2) {
            findViewById = findViewById(C0984R.id.btnNext);
            if (findViewById != null) {
                findViewById.setVisibility(0);
            }
        }
    }

    protected void onPause() {
        WebView2 webView2 = (WebView2) findViewById(C0984R.id.webView);
        if (webView2 != null) {
            WebView2.pauseWebView(webView2);
        }
        super.onPause();
    }

    protected void onPrevPageClicked() {
        ViewPager viewPager = (ViewPager) findViewById(C0984R.id.infoPager);
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
    }

    protected void onResume() {
        super.onResume();
        WebView2 webView2 = (WebView2) findViewById(C0984R.id.webView);
        if (webView2 != null) {
            WebView2.resumeWebView(webView2);
        }
    }

    public void showErrorMessage(boolean z) {
        View findViewById = findViewById(C0984R.id.errorIndicator);
        if (findViewById != null) {
            findViewById.setVisibility(z ? 0 : 8);
        }
    }

    public void showLoadingIndicator(boolean z) {
        View findViewById = findViewById(C0984R.id.loadingIndicator);
        if (findViewById != null) {
            findViewById.setVisibility(z ? 0 : 8);
        }
    }
}
