package com.eli.poly2;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.eli.poly2.NewsActivity.NewsItemStruct;
import com.eli.poly2.util.MenuListAdapter;
import com.eli.poly2.util.MenuStruct;
import com.eli.poly2.util.XMLfunctions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchClassActivity extends Activity
{
	private Context ctx;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchclass);

        ctx = this;
       
    	//Retrieve news in separate thread while spinner is spinning
        new Thread(new Runnable()
        {
            public void run()
            {
            	//Get list of subjects		
            	
            	runOnUiThread(new Runnable()
            	{
            	    public void run()
            	    {
            	    	//Populate spinner with list
            	    }
            	});

            }}).start();
        
        //Get watched classes from database and populate listview

    }
}
