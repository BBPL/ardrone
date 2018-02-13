package com.parrot.freeflight.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.activities.base.ParrotActivity;
import org.mortbay.jetty.HttpVersions;
import org.mortbay.jetty.MimeTypes;

public class BrowserActivity extends ParrotActivity implements OnClickListener {
    public static final String URL = "url";
    private ImageView imgBack;
    private ImageView imgForward;
    private WebView webView;

    private class BrowserClient extends WebViewClient {
        private BrowserClient() {
        }

        public void onPageFinished(WebView webView, String str) {
            ((LinearLayout) BrowserActivity.this.findViewById(C0984R.id.loadingIndicator)).setVisibility(4);
            BrowserActivity.this.checkButtonState();
            super.onPageFinished(webView, str);
        }

        public void onReceivedError(WebView webView, int i, String str, String str2) {
            webView.loadData(HttpVersions.HTTP_0_9, MimeTypes.TEXT_HTML, null);
            ((LinearLayout) BrowserActivity.this.findViewById(C0984R.id.loadingIndicator)).setVisibility(4);
            ((LinearLayout) BrowserActivity.this.findViewById(C0984R.id.errorIndicator)).setVisibility(0);
        }
    }

    private void checkButtonState() {
        if (this.webView.canGoBack()) {
            this.imgBack.setEnabled(true);
        } else {
            this.imgBack.setEnabled(false);
        }
        if (this.webView.canGoForward()) {
            this.imgForward.setEnabled(true);
        } else {
            this.imgForward.setEnabled(false);
        }
    }

    private void done() {
        finish();
    }

    private void historyBack() {
        if (this.webView.canGoBack()) {
            this.webView.goBack();
        }
    }

    private void historyForward() {
        if (this.webView.canGoForward()) {
            this.webView.goForward();
        }
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void initView() {
        String stringExtra = getIntent().getStringExtra("url");
        this.webView = (WebView) findViewById(C0984R.id.webView);
        this.webView.clearHistory();
        this.webView.loadUrl(stringExtra);
        this.webView.setWebViewClient(new BrowserClient());
        this.webView.setInitialScale(0);
        WebSettings settings = this.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setPluginState(PluginState.ON);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportMultipleWindows(false);
        settings.setDefaultZoom(ZoomDensity.FAR);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        ((Button) findViewById(C0984R.id.btnDone)).setOnClickListener(this);
        this.imgBack = (ImageView) findViewById(C0984R.id.imgBack);
        this.imgForward = (ImageView) findViewById(C0984R.id.imgForward);
        this.imgBack.setOnClickListener(this);
        this.imgForward.setOnClickListener(this);
        checkButtonState();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0984R.id.imgBack /*2131362059*/:
                historyBack();
                return;
            case C0984R.id.imgForward /*2131362060*/:
                historyForward();
                return;
            case C0984R.id.btnDone /*2131362061*/:
                done();
                return;
            default:
                return;
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        overridePendingTransition(C0984R.anim.slide_up_in, C0984R.anim.nothing);
        setContentView(C0984R.layout.browser_screen);
        initView();
    }

    protected void onPause() {
        overridePendingTransition(C0984R.anim.nothing, C0984R.anim.slide_down_out);
        super.onPause();
    }
}
