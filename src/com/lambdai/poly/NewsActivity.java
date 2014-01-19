package com.lambdai.poly;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.lambdai.poly.util.MenuListAdapter;
import com.lambdai.poly.util.MenuStruct;
import com.lambdai.poly.util.XMLfunctions;

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

public class NewsActivity extends Activity
{
	private ProgressBar progress;
	private ListView newsList;
	private ArrayList<MenuStruct> menuItems = new ArrayList<MenuStruct>();
	private MenuListAdapter newsListAdapter;
	
	protected String polyPage;
	private Context c;
	
	//Handler to update UI from thread
	private Handler completionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
                progress.setVisibility(View.GONE);
        }
	};
	
	//Handler to show toast
	private Handler noItems = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	Toast.makeText(NewsActivity.this, R.string.noNews, Toast.LENGTH_LONG).show();  
        	finish();
        }
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webdatalist);
        
        polyPage = "news";
        c = this;
        
        //Get UI elements
        progress = (ProgressBar) findViewById(R.id.progress_bar);
        progress.setIndeterminate(true);
        progress.setVisibility(View.VISIBLE);
        newsList = (ListView) findViewById(R.id.menuList);
        
    	//Retrieve news in separate thread while spinner is spinning
        new Thread(new Runnable()
        {
            public void run()
            {
            	String xml = XMLfunctions.getXML(polyPage);
            	Document doc = XMLfunctions.XMLfromString(xml);
            	doc.normalize();
            	        
            	int numResults = XMLfunctions.numResults(doc);

            	if((numResults <= 0)){
            		noItems.sendEmptyMessage(0);
            	}

            	NodeList nodes = doc.getElementsByTagName("item");
            				
            	for (int i = 0; i < nodes.getLength(); i++)
            	{								
            		Element e = (Element)nodes.item(i);
            		String title = XMLfunctions.getValue(e, "title");
            		String content = XMLfunctions.getValue(e, "description");
            		
            		String cutOffZeros = XMLfunctions.getValue(e, "pubDate");
            		int index = XMLfunctions.getValue(e, "pubDate").indexOf('+');
            		if(index != -1)
            			cutOffZeros = XMLfunctions.getValue(e, "pubDate").substring(0,index);
            		
            		NewsItemStruct item = new NewsItemStruct(title, cutOffZeros, content, cutOffZeros);
            		menuItems.add(item);			
            	}		
            	
            	runOnUiThread(new Runnable()
            	{
            	    public void run()
            	    {
            	    	newsListAdapter = new MenuListAdapter(c, R.layout.menulist_row, menuItems);
            	    	newsList.setAdapter(newsListAdapter);
            	    }
            	});
            	
            	completionHandler.sendEmptyMessage(0);

            }}).start();

        newsList.setOnItemClickListener(new OnItemClickListener()
        {
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    		{        		
    			NewsItemStruct item = (NewsItemStruct) newsList.getItemAtPosition(position);	        		

    			Intent intent = new Intent(NewsActivity.this, WebDisplayActivity.class);  
    	        Bundle b = new Bundle(); 

    	        String results = "<html><head><meta name=\"viewport\" content=\"user-scalable=false\"/><link rel=\"stylesheet\" href=\"file:///android_asset/reader.css\" type=\"text/css\"></head><body>";
    	        results += "<div class=\"content\" style=\"margin-top:0px;\"><h1>"+ item.title +"</h1><h2>"+ item.date +"</h2><div style=\"font-size:14px;margin-left:5px;\">"+ item.webContent +"</div></div></body></html>";
    	            
    	        b.putString(WebDisplayActivity.HTML_DATA, results);
    	               
    	        //Add HTML string to bundle
    	        intent.putExtras(b);
    	        startActivity(intent);
    		} 
    	});
    }
	
	 class NewsItemStruct extends MenuStruct
     {
     	String webContent;
     	String date;
     	
		public NewsItemStruct(String tit, String descrip, String content, String theDate)
		{
			super(tit, descrip);
			webContent = content;
			date = theDate;
		}
     }
}
