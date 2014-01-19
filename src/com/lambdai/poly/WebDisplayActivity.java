package com.lambdai.poly;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebDisplayActivity extends Activity
{
	public static final String HTML_DATA = "HTML_DATA";
	public static final String LOAD_URL = "LOAD_URL";
	
    @Override
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        
        Bundle b = getIntent().getExtras(); 
        String html = b.getString(HTML_DATA);
        
        WebView webView = (WebView) findViewById(R.id.web_engine);
        
        if(b.containsKey(LOAD_URL))
        	webView.loadUrl(html);
        else
            webView.loadDataWithBaseURL("http://poly.edu", html, "text/html", "UTF-8", "");  
    }
}