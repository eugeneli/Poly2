package com.lambdai.poly.util;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lambdai.poly.R;
import com.lambdai.poly.util.MenuStruct;

public class MenuListAdapter extends ArrayAdapter<MenuStruct>
	{
		Context context;
		int layoutResource;
		ArrayList<MenuStruct> menuData;
		
		public MenuListAdapter(Context c, int resource, ArrayList<MenuStruct> data)
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
	            holder.itemTitle = (TextView) row.findViewById(R.id.itemTitle);
	            holder.itemDescription = (TextView) row.findViewById(R.id.itemDescription);
	            		
	            row.setTag(holder);
	        }
	        else
	        	holder = (MenuItemHolder)row.getTag();
	        
	        holder.itemTitle.setText(menuData.get(position).title);
	        holder.itemDescription.setText(menuData.get(position).description);
	      
	        return row;
	    }
		
		class MenuItemHolder
	    {
	        TextView itemTitle;
	        TextView itemDescription;
	    }
	}