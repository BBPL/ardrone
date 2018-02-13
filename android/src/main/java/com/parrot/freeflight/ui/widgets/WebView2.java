package com.parrot.freeflight.ui.widgets;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

public class WebView2 extends WebView {
    private static final String TAG = WebView2.class.getSimpleName();

    public WebView2(Context context) {
        super(context);
    }

    public WebView2(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private static void callWebViewVoidMethod(WebView webView, String str) {
        try {
            WebView.class.getMethod(str, new Class[0]).invoke(webView, new Object[0]);
        } catch (Exception e) {
            Log.e(TAG, String.format("Failed to call %s method. Exception: %s", new Object[]{str, e.toString()}));
        }
    }

    public static void pauseWebView(WebView webView) {
        if (VERSION.SDK_INT < 11) {
            callWebViewVoidMethod(webView, "onPause");
        } else {
            webView.onPause();
        }
    }

    public static void resumeWebView(WebView webView) {
        if (VERSION.SDK_INT < 11) {
            callWebViewVoidMethod(webView, "onResume");
        } else {
            webView.onResume();
        }
    }

    protected void onDetachedFromWindow() {
        pauseWebView(this);
        super.onDetachedFromWindow();
    }
}
