package com.parrot.freeflight.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.activities.base.WatchMediaActivity;
import com.parrot.freeflight.ui.widgets.SlideRuleView;
import com.parrot.freeflight.vo.MediaVO;
import org.mortbay.jetty.MimeTypes;
import org.mortbay.util.URIUtil;

@SuppressLint({"SetJavaScriptEnabled"})
public class WatchYoutubeVideoLegacyActivity extends WatchMediaActivity {
    public static final String EXTRA_FLIGHT_OBJ = "flight";
    private TextView batteryIndicator;
    private SlideRuleView currHeightIndicator;
    private RelativeLayout mContentView;
    private View mCustomView;
    private CustomViewCallback mCustomViewCallback;
    private FrameLayout mCustomViewContainer;
    private MediaVO media;
    private TextView speedIndicator;
    private MyWebChromeClient webChromeClient;

    private class MyWebChromeClient extends WebChromeClient {
        LayoutParams LayoutParameters;

        private MyWebChromeClient() {
            this.LayoutParameters = new LayoutParams(-1, -1);
        }

        public void onHideCustomView() {
            if (WatchYoutubeVideoLegacyActivity.this.mCustomView != null) {
                WatchYoutubeVideoLegacyActivity.this.mCustomView.setVisibility(8);
                WatchYoutubeVideoLegacyActivity.this.mCustomViewContainer.removeView(WatchYoutubeVideoLegacyActivity.this.mCustomView);
                WatchYoutubeVideoLegacyActivity.this.mCustomView = null;
                WatchYoutubeVideoLegacyActivity.this.mCustomViewContainer.setVisibility(8);
                WatchYoutubeVideoLegacyActivity.this.mCustomViewCallback.onCustomViewHidden();
                WatchYoutubeVideoLegacyActivity.this.mContentView.setVisibility(0);
                WatchYoutubeVideoLegacyActivity.this.setContentView(WatchYoutubeVideoLegacyActivity.this.mContentView);
            }
        }

        public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
            if (WatchYoutubeVideoLegacyActivity.this.mCustomView != null) {
                customViewCallback.onCustomViewHidden();
                return;
            }
            WatchYoutubeVideoLegacyActivity.this.mContentView = (RelativeLayout) WatchYoutubeVideoLegacyActivity.this.findViewById(C0984R.id.video_holder);
            WatchYoutubeVideoLegacyActivity.this.mContentView.setVisibility(8);
            WatchYoutubeVideoLegacyActivity.this.mCustomViewContainer = new FrameLayout(WatchYoutubeVideoLegacyActivity.this);
            WatchYoutubeVideoLegacyActivity.this.mCustomViewContainer.setLayoutParams(this.LayoutParameters);
            WatchYoutubeVideoLegacyActivity.this.mCustomViewContainer.setBackgroundResource(17170444);
            view.setLayoutParams(this.LayoutParameters);
            WatchYoutubeVideoLegacyActivity.this.mCustomViewContainer.addView(view);
            WatchYoutubeVideoLegacyActivity.this.mCustomView = view;
            WatchYoutubeVideoLegacyActivity.this.mCustomViewCallback = customViewCallback;
            WatchYoutubeVideoLegacyActivity.this.mCustomViewContainer.setVisibility(0);
            WatchYoutubeVideoLegacyActivity.this.setContentView(WatchYoutubeVideoLegacyActivity.this.mCustomViewContainer);
        }
    }

    private void makeOverlayVisible(boolean z) {
        int i = z ? 0 : 4;
        findViewById(C0984R.id.graph1).setVisibility(i);
        this.currHeightIndicator.setVisibility(i);
        this.speedIndicator.setVisibility(i);
        this.batteryIndicator.setVisibility(i);
    }

    public static void start(Context context, MediaVO mediaVO, String str) {
        Intent intent = new Intent(context, WatchYoutubeVideoLegacyActivity.class);
        intent.putExtra(WatchMediaActivity.EXTRA_MEDIA_OBJ, mediaVO);
        intent.putExtra(WatchMediaActivity.EXTRA_TITLE, str);
        context.startActivity(intent);
    }

    public void init() {
        String substring = this.media.getPath().substring(this.media.getPath().lastIndexOf(URIUtil.SLASH) + 1);
        WebView webView = (WebView) findViewById(C0984R.id.activity_watch_video_web_view);
        if (webView != null) {
            if (this.webChromeClient == null) {
                this.webChromeClient = new MyWebChromeClient();
            }
            webView.setWebChromeClient(this.webChromeClient);
            webView.loadUrl(String.format("http://www.youtube.com/embed/%s?rel=0", new Object[]{substring}));
        }
    }

    public void onBackPressed() {
        if (this.mCustomView != null) {
            this.webChromeClient.onHideCustomView();
        } else {
            super.onBackPressed();
        }
    }

    protected void onCreate(Bundle bundle) {
        boolean z = true;
        super.onCreate(bundle);
        setContentView(C0984R.layout.activity_watch_youtube_video_legacy);
        this.media = (MediaVO) getIntent().getParcelableExtra(WatchMediaActivity.EXTRA_MEDIA_OBJ);
        this.currHeightIndicator = (SlideRuleView) findViewById(C0984R.id.activity_watch_video_indicator_current_height);
        this.batteryIndicator = (TextView) findViewById(C0984R.id.activity_watch_video_text_charge);
        this.speedIndicator = (TextView) findViewById(C0984R.id.activity_watch_video_text_speed);
        if (this.media.isRemote()) {
            WebView webView = (WebView) findViewById(C0984R.id.activity_watch_video_web_view);
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            try {
                if (getPackageManager().getApplicationInfo("com.adobe.flashplayer", 0) == null) {
                    z = false;
                }
            } catch (NameNotFoundException e) {
                z = false;
            }
            settings.setPluginState(z ? PluginState.OFF : PluginState.ON);
            webView.setWebChromeClient(new WebChromeClient());
            init();
        }
        makeOverlayVisible(false);
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
        ((WebView) findViewById(C0984R.id.activity_watch_video_web_view)).loadData("<html></html>", MimeTypes.TEXT_HTML, "utf-8");
    }
}
