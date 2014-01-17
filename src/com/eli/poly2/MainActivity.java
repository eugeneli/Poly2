package com.eli.poly2;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import com.eli.poly2.util.SearchDialog;
import com.eli.poly2.util.SearchDialog.OnSearchListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity
{
	private ListView mainMenuList;
	private ArrayList<MainMenuStruct> menuItems = new ArrayList<MainMenuStruct>();
	private MainMenuListAdapter menuListAdapter;
	private Context ctx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ctx = this;
		mainMenuList = (ListView) findViewById(R.id.menuList);
		
		menuItems.add(new MainMenuStruct(R.drawable.ic_launcher, R.string.emergency, R.string.emergencyDescription));
		menuItems.add(new MainMenuStruct(R.drawable.ic_launcher, R.string.news, R.string.newsDescription));
		menuItems.add(new MainMenuStruct(R.drawable.ic_launcher, R.string.press, R.string.pressDescription));
		menuItems.add(new MainMenuStruct(R.drawable.ic_launcher, R.string.directions, R.string.directionsDescription));
		menuItems.add(new MainMenuStruct(R.drawable.ic_launcher, R.string.campusmap, R.string.campusmapDescription));
		menuItems.add(new MainMenuStruct(R.drawable.ic_launcher, R.string.searchDirectory, R.string.searchDirectoryDescription));
		menuItems.add(new MainMenuStruct(R.drawable.ic_launcher, R.string.searchClasses, R.string.searchClassesDescription));
		menuItems.add(new MainMenuStruct(R.drawable.ic_launcher, R.string.about, R.string.aboutDescription));
		menuListAdapter = new MainMenuListAdapter(this, R.layout.mainmenulist_row, menuItems);
		mainMenuList.setAdapter(menuListAdapter);
		
		mainMenuList.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        	{
        		switch(position)
        		{
        			case 0:
        				startActivity(new Intent(MainActivity.this, EmergencyActivity.class));
        				break;
        			case 1:
        				startActivity(new Intent(MainActivity.this, NewsActivity.class));
        				break;
        			case 2:
        				startActivity(new Intent(MainActivity.this, PressReleaseActivity.class));
        				break;
        			case 3:
        				startActivity(new Intent(MainActivity.this, DirectionsActivity.class));
        				break;
        			case 4:
        				startActivity(new Intent(MainActivity.this, CampusMapActivity.class));
        				break;
        			case 5:
        				final SearchDialog sd = new SearchDialog();
        				sd.setListener(new OnSearchListener()
        				{
        					@Override
        					public void onSearch(final String query)
        					{
        						if(sd.getInputBox().getText().length() < 2)
				            	{
        							AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        					        builder.setMessage("Your search query was too short!\nPlease enter at least 2 characters.")
        					               .setCancelable(true)
        					               .setPositiveButton("Okay!", new DialogInterface.OnClickListener()
        					               {
        					                   public void onClick(final DialogInterface dialog, final int id)
        					                   {
        					                	   dialog.cancel();
        					                   }
        					               });
        					        AlertDialog alert = builder.create();
        					        alert.show();
				            	}
        						else
        						{
        							//Start running search/scraping results
        		                    new Thread(new Runnable()
        		                    {
        		                        public void run()
        		                        {
        		                       	 Intent intent = new Intent(MainActivity.this, WebDisplayActivity.class);  
        		                            Bundle b = new Bundle(); 
        		                          
        		                            org.jsoup.nodes.Document doc = null;
        		                            try {
        		                            	doc = Jsoup.connect("http://www.poly.edu/directory/index.php?search_term="+query).get();
        		                            	Elements tds = doc.select("ul#results");
        		                            	
        		                                boolean found = tds.toString().indexOf("profile clearfix") >= 0;
        		                                String results = "<html><head><meta name=\"viewport\" content=\"user-scalable=false\"/><link rel=\"stylesheet\" href=\"file:///android_asset/results.css\" type=\"text/css\"></head><body>";
        		                                if(found)
        		                                      results+=tds.toString();
        		                                else
        		                                     results+="<h1>No results found</h1>";
        		                                results+="</body></html>";
        		                                
        		                                b.putString(WebDisplayActivity.HTML_DATA, results);

        		                                intent.putExtras(b);
        		                                startActivity(intent);
        		                            } catch (IOException e) {
        		                            	// TODO Auto-generated catch block
        		                            	e.printStackTrace();
        		                            }
        		                         }
        		                     }).start();
        						}
        					}
        				});
        				sd.show(getSupportFragmentManager(), "Search Dialog");
        				break;
        			case 6:
        				startActivity(new Intent(MainActivity.this, SearchClassActivity.class));
        				break;
        		}
        	}
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public class MainMenuStruct
	{
		public int image;
		public int title;
		public int description;
		
		public MainMenuStruct(int img, int tit, int descrip)
        {
        	image = img;
        	title = tit;
        	description = descrip;
        }
	}
	
	//Custom list adapter to display menus
	public class MainMenuListAdapter extends ArrayAdapter<MainMenuStruct>
	{
		Context context;
		int layoutResource;
		ArrayList<MainMenuStruct> menuData;
		
		public MainMenuListAdapter(Context c, int resource, ArrayList<MainMenuStruct> data)
		{
			super(c, resource, data);
			context = c;
			menuData = data;
			layoutResource = resource;
		}

		@Override
	    public View getView(int position, View convertView, ViewGroup parent)
		{
	        View row = convertView;
	        MenuItemHolder holder;
	        
	        if(row == null)
	        {
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            row = inflater.inflate(layoutResource, null);
	            
	            holder = new MenuItemHolder();
	            holder.itemImage = (ImageView) row.findViewById(R.id.itemImage);
	            holder.itemTitle = (TextView) row.findViewById(R.id.itemTitle);
	            holder.itemDescription = (TextView) row.findViewById(R.id.itemDescription);
	            		
	            row.setTag(holder);
	        }
	        else
	        	holder = (MenuItemHolder)row.getTag();
	        
	        holder.itemImage.setImageResource(menuData.get(position).image);
	        holder.itemTitle.setText(menuData.get(position).title);
	        holder.itemDescription.setText(menuData.get(position).description);
	     
	        return row;
	    }
		
		class MenuItemHolder
	    {
			ImageView itemImage;
	        TextView itemTitle;
	        TextView itemDescription;
	    }
	}

}
