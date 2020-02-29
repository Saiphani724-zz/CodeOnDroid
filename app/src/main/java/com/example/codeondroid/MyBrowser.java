package com.example.codeondroid;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyBrowser  extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}
