package com.lambdai.poly;

import java.util.ArrayList;

import com.lambdai.poly.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class DirectionsActivity extends FragmentActivity
{
	private FragmentPagerAdapter adapterViewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_directions);
		
		ViewPager vpPager = (ViewPager) findViewById(R.id.viewPager);
		adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
		vpPager.setAdapter(adapterViewPager);
	}
	
	public static class MyPagerAdapter extends FragmentPagerAdapter
	{
		private static int NUM_ITEMS = 3;
		private ArrayList<DirectionsFragmentStruct> directions = new ArrayList<DirectionsFragmentStruct>();
		
		private String subwayDirections = "<html><head><link rel=\"stylesheet\" href=\"file:///android_asset/direc.css\" type=\"text/css\"></head><body>" +
				"<p><a href=\"http://www.mta.info/nyct/service/aline.htm\" target=\"_blank\">A</a>&nbsp;<a href=\"http://www.mta.info/nyct/service/cline.htm\" target=\"_blank\">C</a>&nbsp;<a href=\"http://www.mta.info/nyct/service/fline.htm\" target=\"_blank\">F</a>&nbsp;train to <strong>Jay Street-Borough Hall</strong></p>"+
				"<p><a href=\"http://www.mta.info/nyct/service/twoline.htm\" target=\"_blank\">2</a> <a href=\"http://www.mta.info/nyct/service/threeline.htm\" target=\"_blank\">3</a> <a href=\"http://www.mta.info/nyct/service/fourline.htm\" target=\"_blank\">4</a> <a href=\"http://www.mta.info/nyct/service/fiveline.htm\" target=\"_blank\">5</a> train to <strong>Borough Hall</strong> (walk one block East to Willoughby Street and make a left onto Jay Street)</p>"+
				"<p><a href=\"http://www.mta.info/nyct/service/mline.htm\" target=\"_blank\">M</a>&nbsp;<a href=\"http://www.mta.info/nyct/service/rline.htm\" target=\"_blank\">R</a>&nbsp;train to <strong>Lawrence Street-MetroTech</strong> (walk one block North on Lawrence Street)</p>"+
				"<p><a href=\"http://www.mta.info/nyct/service/qline.htm\" target=\"_blank\">Q</a> <a href=\"http://www.mta.info/nyct/service/bline.htm\" target=\"_blank\">B</a>&nbsp;train to <strong>Dekalb Avenue</strong> (walk two blocks North toward Manhattan Bridge and make a left onto Myrtle Avenue into MetroTech)</p>" +
				"<p>Sourced from: <a href=\"http://www.poly.edu/node/463\">Poly.edu</a></body></html>";
		
		private String trainDirections = "<html><head><link rel=\"stylesheet\" href=\"file:///android_asset/direc.css\" type=\"text/css\"></head><body>" +
				"<p><strong>Take Long Island Railroad</strong> to <strong>Pennsylvania Station,</strong> then transfer to a Brooklyn-bound A, C, 2, 3 train (see subway instructions above).<br /><br />"+
				"<strong>Take Long Island Railroad</strong> to <strong>Flatbush Avenue-Atlantic Terminal</strong> in Brooklyn, then transfer to a Manhattan-bound B, M, Q, R, 2, 3, 4, 5 train (see subway instructions above) or walk North along Flatbush Avenue about 1 mile to Myrtle Avenue and make a left into MetroTech.</p>"+
			"<p><strong>Take Metro North Railroad</strong> to <strong>Grand Central Station</strong> in Manhattan, then transfer to a Brooklyn-bound 4, 5 train (see subway instructions above).</p>"+
			"<p><strong>Take New Jersey Transit</strong> to <strong>Pennsylvania Station </strong>in Manhattan, then transfer to a Brooklyn-bound A, C, 2, 3 train (see subway instructions above).</p>" +
			"<p>Sourced from: <a href=\"http://www.poly.edu/node/463\">Poly.edu</a></body></html>";
		
		private String carDirections = "<html><head><link rel=\"stylesheet\" href=\"file:///android_asset/direc.css\" type=\"text/css\"></head><body>" +
				"<p><strong>From Manhattan:</strong></p><ul>"+
				"<li>Take the FDR Drive to the Brooklyn Bridge (Exit 2).</li>"+
				"<li>Make the first left after traveling over the bridge onto Tillary Street.</li>"+
				"<li>Take a right onto Jay Street.</li>"+
				"<li>Parking is available at the Marriott Hotel on Jay Street.</li></ul>"+
			"<p><strong>From Queens, Brooklyn, Bronx and Staten Island:</strong></p><ul>"+
				"<li>Take I-278 to Tillary Street (Exit 29) in Brooklyn.</li>"+
				"<li>Make a left at the third light onto Jay Street.</li>"+
				"<li>Parking is available at the Marriott Hotel on Jay Street.</li></ul>"+
			"<p><strong>From Long Island:</strong></p><ul>"+
				"<li>Take I-495 West (Long Island Expressway) to I-278 West (Exit 18A - Brooklyn-Queens Expressway) to Tillary Street (Exit 29).</li>"+
				"<li>Make a left at the third light onto Jay Street.</li>"+
				"<li>Parking is available at the Marriott Hotel on Jay Street.</li></ul>"+
			"<p><strong>From New Jersey:</strong></p><ul>"+
				"<li>Take I-78 East to the Holland Tunnel.</li>"+
				"<li>Follow Canal Street East to the Manhattan Bridge to Flatbush Avenue.<br />"+
				"	Or take I-95 (New Jersey Turnpike) to I-278 East (Exit 13) to Tillary Street (Exit 29) in Brooklyn.</li>"+
				"<li>Make a left at the third light onto Jay Street.</li>"+
				"<li>Parking is available at the Marriott Hotel on Jay Street.</li></ul>"+
			"<p><strong>From Westchester, Downstate New York and Connecticut:</strong></p><ul>"+
				"<li>Take either I-87 South (Major Deegan Expressway/New York State Thruway) or I-95 South (New England Thruway) to I-278 West to Tillary Street (Exit 29).</li>"+
				"<li>Make a left at the third light onto Jay Street.</li>"+
				"<li>Parking is available at the Marriott Hotel on Jay Street.</li></ul>"+
				"<p>Sourced from: <a href=\"http://www.poly.edu/node/463\">Poly.edu</a></body></html>";
			
        public MyPagerAdapter(FragmentManager fragmentManager)
        {
            super(fragmentManager);
            
            directions.add(new DirectionsFragmentStruct(DirectionsFragment.newInstance(subwayDirections), "Subway"));
            directions.add(new DirectionsFragmentStruct(DirectionsFragment.newInstance(trainDirections), "Train"));
            directions.add(new DirectionsFragmentStruct(DirectionsFragment.newInstance(carDirections), "Car"));
        }
        
        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
 
        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position)
        {
            return directions.get(position).fragment;
        }
        
        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position)
        {
        	return directions.get(position).title;
        }
        
        private class DirectionsFragmentStruct
        {
        	public DirectionsFragment fragment;
        	public String title;
        	
        	public DirectionsFragmentStruct(DirectionsFragment frag, String tit)
        	{
        		fragment = frag;
        		title = tit;
        	}
        }
	}
	
	public static class DirectionsFragment extends Fragment
	{
		// Store instance variables
		private String html;
		
		public static final String HTML = "html";

		// newInstance constructor for creating fragment with arguments
		public static DirectionsFragment newInstance(String html)
		{
			DirectionsFragment fragmentFirst = new DirectionsFragment();
			Bundle args = new Bundle();
			args.putString(HTML, html);
			fragmentFirst.setArguments(args);
			return fragmentFirst;
		}

		// Store instance variables based on arguments passed
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			html = getArguments().getString(HTML);
		}

		// Inflate the view for the fragment based on layout XML
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			View view = inflater.inflate(R.layout.webview, container, false);

			WebView engine = (WebView) view.findViewById(R.id.web_engine);
	        engine.loadDataWithBaseURL("http://poly.edu", html, "text/html", "UTF-8", "");
			
			return view;
		}
	}
}
