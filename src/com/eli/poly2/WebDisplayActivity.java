package com.eli.poly2;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;

public class WebDisplayActivity extends Activity
{
	public static final String HTML_DATA = "HTML_DATA";
    @Override
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        
        Bundle b = getIntent().getExtras(); 
        String html = b.getString(HTML_DATA);

        WebView webView = (WebView) findViewById(R.id.web_engine);
        webView.loadDataWithBaseURL("http://poly.edu", html, "text/html", "UTF-8", "");    
    }
}