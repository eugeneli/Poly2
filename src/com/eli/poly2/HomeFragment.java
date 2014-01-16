package com.eli.poly2;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.eli.poly2.util.MainMenuListAdapter;

public class HomeFragment extends Fragment
{
	private ListView mainMenuList;
	private ArrayList<String> menuStrings = new ArrayList<String>();
	private MainMenuListAdapter menuListAdapter;
	
	public HomeFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_main,container, false);
		/*TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
		dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));*/
		
		mainMenuList = (ListView) rootView.findViewById(R.id.mainMenuList);
		
		menuStrings.add("Emergency");
		menuStrings.add("News");
		menuStrings.add("Press Releases");
		menuStrings.add("Directions");
		menuStrings.add("Campus Map");
		menuStrings.add("Search Directory");
		menuStrings.add("Search Classes");
		menuStrings.add("About this app");
		menuListAdapter = new MainMenuListAdapter(getActivity(), R.layout.menulist_row, menuStrings);
		mainMenuList.setAdapter(menuListAdapter);
		
		return rootView;
	}
}