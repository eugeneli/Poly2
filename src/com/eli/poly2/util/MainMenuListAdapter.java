package com.eli.poly2.util;

import java.util.ArrayList;

import com.eli.poly2.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

//Custom list adapter to display menus
public class MainMenuListAdapter extends ArrayAdapter<String>
{
	Context context;
	int layoutResource;
	ArrayList<String> menuData;
	
	public MainMenuListAdapter(Context c, int resource, ArrayList<String> data)
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
            holder.itemInfo = (TextView) row.findViewById(R.id.menuItem);
            
            row.setTag(holder);
        }
        else
        	holder = (MenuItemHolder)row.getTag();
        
        holder.itemInfo.setText(menuData.get(position));
        
        /*
        final Trip trip = tripData.get(position);
        
        holder.tripInfo.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v)
			{
				try {
					Intent i = new Intent(getBaseContext(), ViewTripActivity.class);
					i.putExtra(TRIP_DATA, trip.toJSON().toString());
					i.putExtra(USER_DATA,  user.toJSON().toString());
					startActivity(i);
				} catch (JSONException e) {
					Log.e(TAG, ETAError.MAIN_MENU_JSON_EXCEPTION);
					e.printStackTrace();
				}
			}
        });
        
        if(System.currentTimeMillis() > trip.getDate().getTime()) //If time right now is > the trip's date, mark as old
        	holder.tripInfo.setText("[OLD] " + trip.getDestination() + " - " + trip.getDateString());
        else
        	holder.tripInfo.setText(trip.getDestination() + " - " + trip.getDateString());
*/
        return row;
    }
	
	class MenuItemHolder
    {
        TextView itemInfo;
    }
}