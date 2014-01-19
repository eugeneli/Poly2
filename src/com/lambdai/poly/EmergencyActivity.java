package com.lambdai.poly;

import java.util.ArrayList;

import com.lambdai.poly.util.MenuListAdapter;
import com.lambdai.poly.util.MenuStruct;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class EmergencyActivity extends Activity
{
	private ListView menuList;
	private ArrayList<MenuStruct> menuItems = new ArrayList<MenuStruct>();
	private MenuListAdapter menuListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
        menuList = (ListView) findViewById(R.id.menuList);
        
        menuItems.add(new MenuStruct(getResources().getString(R.string.campusSecurity), getResources().getString(R.string.campusSecurityDescrip)));
        menuItems.add(new MenuStruct(getResources().getString(R.string.wellnessExchange), getResources().getString(R.string.wellnessExchangeDescrip)));
        menuItems.add(new MenuStruct(getResources().getString(R.string.CAPS), getResources().getString(R.string.CAPSDescrip)));
        menuItems.add(new MenuStruct(getResources().getString(R.string.mobileUnit), getResources().getString(R.string.mobileUnitDescrip)));
        menuListAdapter = new MenuListAdapter(this, R.layout.menulist_row, menuItems);
        menuList.setAdapter(menuListAdapter);
        
        menuList.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        	{
        		switch(position)
        		{
        			case 0:
        				startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(getResources().getString(R.string.campusSecurityNum))));
        				break;
        			case 1:
        				startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(getResources().getString(R.string.wellnessExchangeNum))));
        				break;
        			case 2:
        				startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(getResources().getString(R.string.CAPSNum))));
        				break;
        			case 3:
        				startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(getResources().getString(R.string.mobileUnitNum))));
        				break;
        		}
        	}
        });
	}
}
