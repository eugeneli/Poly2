package com.lambdai.poly;

import com.google.android.gms.maps.SupportMapFragment;
import com.lambdai.poly.util.GoogleMapsWrapper;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class CampusMapActivity extends FragmentActivity
{
	private GoogleMapsWrapper map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campusmap);
		
		map = new GoogleMapsWrapper(this, (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.gmap));
		
		//Place markers
		map.addMarker(40.69513082655616, -73.9863073063979, "Othmer Residence Hall");
		map.addMarker(40.6946438388287, -73.98685376537982, "Jacobs Building");
		map.addMarker(40.69417, -73.98640580187856, "Rogers Hall");
		map.addMarker(40.69425711774981, -73.98690123239241, "Jacobs Academic Building");
		map.addMarker(40.694616, -73.985612, "Bern Dibner Library");
		map.addMarker(40.69429117947595, -73.9849118943627, "Wunsch Building");
		map.addMarker(40.693427, -73.985809, "2 Metrotech Center");
		map.addMarker(40.703983, -73.986692, "DUMBO Incubator");
		map.addMarker(40.72582, -74.005966, "Varick Street Incubator");
		map.addMarker(40.773017, -73.412666, "Long Island Graduate Center");
		map.addMarker(40.70517, -74.011537, "Manhattan Graduate Center");
		map.setLocation(40.694190, -73.985817);
		map.setZoom(17f);
	}
}
