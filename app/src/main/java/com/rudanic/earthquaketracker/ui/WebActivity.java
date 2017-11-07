package com.rudanic.earthquaketracker.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rudanic.earthquaketracker.R;

public class WebActivity extends AppCompatActivity {
    WebView detailWebPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        detailWebPage = (WebView) findViewById(R.id.web_view);
        detailWebPage.setWebViewClient(new myWebViewClient());
        detailWebPage.getSettings().setJavaScriptEnabled(true);

        Intent webIntent = getIntent();
        String url = webIntent.getStringExtra("url");
        detailWebPage.loadUrl(url);
    }

    public class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }
    }
}
