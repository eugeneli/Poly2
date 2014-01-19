package com.lambdai.poly;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends Activity
{
	private SharedPreferences prefs;
	private CheckBox notif;
	private CheckBox service;
	private Spinner intervalSpinner;
	
	private ArrayList<String> intervals;
	
	private final String oneHourString = "1 Hour";
	private final String twoHoursString = "2 Hours";
	private final String threeHoursString = "3 Hours";
	private final String fiveHoursString = "5 Hours";
	private final String twelveHoursString = "12 Hours";
	private final String twentyFourHoursString = "24 Hours";
	
	public static final int oneHour = 1000*60*60;
	public static final int twoHours = 2000*60*60;
	public static final int threeHours = 3000*60*60;
	public static final int fiveHours = 5000*60*60;
	public static final int twelveHours = 12000*60*60;
	public static final int twentyFourHours = 24000*60*60;
	
	public static final String APP_NAME = "com.eli.poly2";
	public static final String NOTIF_KEY = APP_NAME+".notifications";
	public static final String SERVICE_KEY = APP_NAME+".service";
	public static final String INTERVAL_KEY = APP_NAME+".interval";
	
	private Context ctx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		ctx = this;
		
		prefs = getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
		
		notif = (CheckBox) findViewById(R.id.notif_checkbox);
		service = (CheckBox) findViewById(R.id.service_checkbox);
		intervalSpinner = (Spinner) findViewById(R.id.interval_spinner);
		
		notif.setChecked(prefs.getBoolean(NOTIF_KEY, false));
		service.setChecked(prefs.getBoolean(SERVICE_KEY, false));
		
		notif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
		   @Override
		   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
			   prefs.edit().putBoolean(NOTIF_KEY, isChecked).commit();
			   
			   if(isChecked)
				   Toast.makeText(getApplicationContext(), "Class notifications enabled", Toast.LENGTH_LONG).show();
			   else
				   Toast.makeText(getApplicationContext(), "Class notifications disabled", Toast.LENGTH_LONG).show();
		   }
		});
		service.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
		   @Override
		   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
			   prefs.edit().putBoolean(SERVICE_KEY, isChecked).commit();
			   
			   if(isChecked)
			   {
				   Toast.makeText(getApplicationContext(), "Automatic class checking enabled", Toast.LENGTH_LONG).show();
				   rescheduleService();
			   }
			   else
			   {
				   Toast.makeText(getApplicationContext(), "Automatic class checking disabled", Toast.LENGTH_LONG).show();
				   
				   //Stop the schedule
				   Intent intent = new Intent(getBaseContext(), ClassCheckService.class);
				   PendingIntent pendingIntent = PendingIntent.getService(ctx, 0, intent, 0);
					
				   AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
				   alarm.cancel(pendingIntent);
				   
				   //Stop the service
				   Intent stopServiceIntent = new Intent(getBaseContext(), ClassCheckService.class);
				   getBaseContext().stopService(stopServiceIntent );
			   }
		   }
		});

		intervals = new ArrayList<String>();
		intervals.add(oneHourString);
		intervals.add(twoHoursString);
		intervals.add(threeHoursString);
		intervals.add(fiveHoursString);
		intervals.add(twelveHoursString);
		intervals.add(twentyFourHoursString);
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, intervals);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		intervalSpinner.setAdapter(dataAdapter);
		
		//Set default interval
		int currentInterval = prefs.getInt(INTERVAL_KEY, 0);
		switch(currentInterval)
		{
			case oneHour:
				intervalSpinner.setSelection(0);
				break;
			case twoHours:
				intervalSpinner.setSelection(1);
				break;
			case threeHours:
				intervalSpinner.setSelection(2);
				break;
			case fiveHours:
				intervalSpinner.setSelection(3);
				break;
			case twelveHours:
				intervalSpinner.setSelection(4);
				break;
			case twentyFourHours:
				intervalSpinner.setSelection(5);
				break;
		}
		
		intervalSpinner.post(new Runnable() {
		    public void run()
		    {
		    	intervalSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
					{
						switch(position)
						{
							case 0:
								prefs.edit().putInt(INTERVAL_KEY, oneHour).commit();
								rescheduleService();
								Toast.makeText(getApplicationContext(), "Interval set to 1 hour", Toast.LENGTH_LONG).show();
								break;
							case 1:
								prefs.edit().putInt(INTERVAL_KEY, twoHours).commit();
								rescheduleService();
								Toast.makeText(getApplicationContext(), "Interval set to 2 hours", Toast.LENGTH_LONG).show();
								break;
							case 2:
								prefs.edit().putInt(INTERVAL_KEY, threeHours).commit();
								rescheduleService();
								Toast.makeText(getApplicationContext(), "Interval set to 3 hours", Toast.LENGTH_LONG).show();
								break;
							case 3:
								prefs.edit().putInt(INTERVAL_KEY, fiveHours).commit();
								rescheduleService();
								Toast.makeText(getApplicationContext(), "Interval set to 5 hours", Toast.LENGTH_LONG).show();
								break;
							case 4:
								prefs.edit().putInt(INTERVAL_KEY, twelveHours).commit();
								rescheduleService();
								Toast.makeText(getApplicationContext(), "Interval set to 12 hours", Toast.LENGTH_LONG).show();
								break;
							case 5:
								prefs.edit().putInt(INTERVAL_KEY, twentyFourHours).commit();
								rescheduleService();
								Toast.makeText(getApplicationContext(), "Interval set to 24 hours", Toast.LENGTH_LONG).show();
								break;
						}
					}
				    @Override
				    public void onNothingSelected(AdapterView<?> parentView) {
				        // your code here
				    }
				});
		    } 
		});
	}
	
	private void rescheduleService()
	{
	   int updateInterval = prefs.getInt(INTERVAL_KEY, twoHours);
	   Intent intent = new Intent(getBaseContext(), ClassCheckService.class);
	   PendingIntent pendingIntent = PendingIntent.getService(ctx, 0, intent, 0);
		
	   AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
	   alarm.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), updateInterval, pendingIntent);
	}
}
