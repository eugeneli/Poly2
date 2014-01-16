package com.eli.poly2;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity
{
	private ListView mainMenuList;
	private ArrayList<MainMenuStruct> menuItems = new ArrayList<MainMenuStruct>();
	private MainMenuListAdapter menuListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
