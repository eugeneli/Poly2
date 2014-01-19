package com.lambdai.poly;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lambdai.poly.models.SectionStruct;
import com.lambdai.poly.util.ClassSearchServer;
import com.lambdai.poly.util.PolyDatabase;
import com.lambdai.poly.util.ClassSearchServer.OnResponseListener;
import com.lambdai.poly.util.PolyDatabase.ClassStruct;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class ClassCheckService extends Service
{
	private final String TAG = "ClassCheckService";
	
	private ClassSearchServer server;
	private PolyDatabase db;
	
	private SharedPreferences prefs;
	private Context ctx;
	
	private ArrayList<ClassStruct> classes;
	private ArrayList<String> openSections;
	
	@Override
    public void onCreate()
	{
        super.onCreate();
        server = new ClassSearchServer();
        db = new PolyDatabase(this);
        ctx = this;
        prefs = getSharedPreferences(SettingsActivity.APP_NAME, Context.MODE_PRIVATE);
    }
	
	private void handleIntent(Intent intent)
	{
		Log.i(TAG, "ClassCheckService started");
		
        // check the global background data setting
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (!cm.getBackgroundDataSetting()) {
            stopSelf();
            return;
        }
		
		classes = new ArrayList<ClassStruct>();
		classes = db.getAllClasses();
		
		openSections = new ArrayList<String>();
		
		//Only check if there are watched classes
	    if(classes.size() > 0)
	    {
	    	for(final ClassStruct aClass : classes)
	    	{
	    		server.startGetClassSectionsTask(aClass.subject, aClass.courseNum, new OnResponseListener()
	    		{
	    			@Override
	    			public void onResponse(JSONObject response) throws JSONException
	    			{
    					JSONArray array = response.getJSONArray(ClassSearchServer.SECTIONS);
	    				for(int i = 0; i < array.length(); i++)
	    				{
	    					try
	    					{
		    					JSONObject section = array.getJSONObject(i);

		    					if(((String)section.get("status")).equals("open"))
		    					{
		    						SectionStruct ss = new SectionStruct();
		    						ss.fullClassName = aClass.fullName;
		    						ss.sectionId = section.getString(SectionStruct.SECTION_NUM);
		    						ss.sectionLetter = section.getString(SectionStruct.SECTION_LETTER);
		    						ss.professor = section.getString(SectionStruct.PROFESSOR);
		    						ss.day = section.getString(SectionStruct.DAY);
		    						ss.time = section.getString(SectionStruct.TIME);
		    						ss.room = section.getString(SectionStruct.ROOM);
		    						
		    						openSections.add(ss.toJSON().toString());
		    					}
	    					}
		    				catch(JSONException e)
		    				{}
	    				}
	    				
	    				if(openSections.size() > 0 && prefs.getBoolean(SettingsActivity.NOTIF_KEY, false))
	    				{
	    					NotificationCompat.Builder mBuilder =
    						        new NotificationCompat.Builder(ctx)
    						        .setSmallIcon(R.drawable.ic_launcher)
    						        .setContentTitle("There are open sections!")
    						        .setContentText("Tap to view");
    						// Creates an explicit intent for an Activity in your app
    						Intent resultIntent = new Intent(ctx, OpenSectionActivity.class);
    						resultIntent.putStringArrayListExtra(OpenSectionActivity.SECTIONS_DATA, openSections);

    						// The stack builder object will contain an artificial back stack for the
    						// started Activity.
    						// This ensures that navigating backward from the Activity leads out of
    						// your application to the Home screen.
    						TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
    						// Adds the back stack for the Intent (but not the Intent itself)
    						stackBuilder.addParentStack(OpenSectionActivity.class);
    						// Adds the Intent that starts the Activity to the top of the stack
    						stackBuilder.addNextIntent(resultIntent);
    						PendingIntent resultPendingIntent =
    						        stackBuilder.getPendingIntent(
    						            0,
    						            PendingIntent.FLAG_UPDATE_CURRENT
    						        );
    						mBuilder.setContentIntent(resultPendingIntent);
    						mBuilder.setAutoCancel(true);
    						NotificationManager mNotificationManager =
    						    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    						// mId allows you to update the notification later on.
    						mNotificationManager.notify(0, mBuilder.build());
	    				}
	    			}
	    		});
	    	}
	    }
	    stopSelf();
	}
	
	@Override
    public void onStart(Intent intent, int startId) {
        handleIntent(intent);
    }
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		handleIntent(intent);
	    return Service.START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
