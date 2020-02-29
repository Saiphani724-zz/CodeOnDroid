package com.example.codeondroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class webview extends AppCompatActivity {
    private WebView wv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        wv1=(WebView)findViewById(R.id.webView);
        wv1.setWebViewClient(new MyBrowser());
        SharedPreferences sf=getSharedPreferences("myfile", Context.MODE_PRIVATE);
        String url=sf.getString("url","NA");
        wv1.getSettings().setLoadsImagesAutomatically(true);
        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.getSettings().setUseWideViewPort(true);
        wv1.getSettings().setBuiltInZoomControls(true);
        wv1.getSettings().setDisplayZoomControls(false);

        wv1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv1.loadUrl(url);


    }

    @Override
    public void onBackPressed() {
        if (wv1.canGoBack()) {
            wv1.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
