package com.eli.poly2.util;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapsWrapper
{
	private GoogleMap map;
	private Context context;
	private SupportMapFragment mapFrag;
	private LatLng currentLoc;
	
	public GoogleMapsWrapper(Context c, SupportMapFragment frag)
	{
		context = c;
		mapFrag = frag;
		currentLoc = new LatLng(0,0);
		
		int checkGooglePlayServices = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
	    if (checkGooglePlayServices != ConnectionResult.SUCCESS)
	    {
	    	//no googleplay services
	    }
		
		//Needs to call MapsInitializer before doing any CameraUpdateFactory calls
		try {
			MapsInitializer.initialize(context);
		} catch (GooglePlayServicesNotAvailableException e) {
			e.printStackTrace();
		}
		
		//Initialize map
		map = mapFrag.getMap();
		map.getUiSettings().setMyLocationButtonEnabled(false);
		map.setMyLocationEnabled(true);
		
		//Update location and zoom of map
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(40.69513082655616, -73.9863073063979), 10);
		map.animateCamera(cameraUpdate);
	}
	
	public void setLocation(double lat, double lng)
	{
		currentLoc = new LatLng(lat, lng);
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLoc, 16);
		map.animateCamera(cameraUpdate);
	}
	
	public void setZoom(float zoom)
	{
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLoc, zoom);
		map.animateCamera(cameraUpdate);
	}
	
	public void addMarker(double lat, double lng, String title)
	{
		map.addMarker(new MarkerOptions()
        .position(new LatLng(lat, lng))
        .title(title));
	}
}
