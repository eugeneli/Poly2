package com.lambdai.poly;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import com.lambdai.poly.util.SearchDialog;
import com.lambdai.poly.util.SearchDialog.OnSearchListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
	
	private SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Set default preferences if they haven't been set
		prefs = getSharedPreferences(SettingsActivity.APP_NAME, Context.MODE_PRIVATE);
		if(!prefs.contains(SettingsActivity.NOTIF_KEY))
			prefs.edit().putBoolean(SettingsActivity.NOTIF_KEY, true).commit();
		if(!prefs.contains(SettingsActivity.SERVICE_KEY))
			prefs.edit().putBoolean(SettingsActivity.SERVICE_KEY, true).commit();
		if(!prefs.contains(SettingsActivity.INTERVAL_KEY))
			prefs.edit().putInt(SettingsActivity.INTERVAL_KEY, SettingsActivity.twoHours).commit();
			
		
		ctx = this;
		mainMenuList = (ListView) findViewById(R.id.menuList);
		
		menuItems.add(new MainMenuStruct(R.drawable.support, R.string.emergency, R.string.emergencyDescription));
		menuItems.add(new MainMenuStruct(R.drawable.newspaper, R.string.news, R.string.newsDescription));
		menuItems.add(new MainMenuStruct(R.drawable.file, R.string.press, R.string.pressDescription));
		menuItems.add(new MainMenuStruct(R.drawable.airplane, R.string.directions, R.string.directionsDescription));
		menuItems.add(new MainMenuStruct(R.drawable.map, R.string.campusmap, R.string.campusmapDescription));
		menuItems.add(new MainMenuStruct(R.drawable.notebook, R.string.searchDirectory, R.string.searchDirectoryDescription));
		menuItems.add(new MainMenuStruct(R.drawable.bullhorn, R.string.searchClasses, R.string.searchClassesDescription));
		menuItems.add(new MainMenuStruct(R.drawable.bubble, R.string.about, R.string.aboutDescription));
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
        			case 7:
        				Intent aboutIntent = new Intent(MainActivity.this, WebDisplayActivity.class);  
                        Bundle bundle = new Bundle();
                        
                        bundle.putBoolean(WebDisplayActivity.LOAD_URL, true);
                        bundle.putString(WebDisplayActivity.HTML_DATA, "file:///android_asset/aboutUs.html");

                        aboutIntent.putExtras(bundle);
                        startActivity(aboutIntent);
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId() == R.id.action_settings)
		{
			startActivity(new Intent(MainActivity.this, SettingsActivity.class));
		}
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
