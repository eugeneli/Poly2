package com.lambdai.poly;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lambdai.poly.util.ClassSearchServer;
import com.lambdai.poly.util.PolyDatabase;
import com.lambdai.poly.util.ClassSearchServer.OnResponseListener;
import com.lambdai.poly.util.PolyDatabase.ClassStruct;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SearchClassActivity extends Activity
{
	private Context ctx;
	private ArrayList<String> subjects = new ArrayList<String>();
	private Spinner subjectsSpinner;
	private Button addButton;
	private EditText courseNum;
	private PolyDatabase db;
	
	private ArrayList<ClassStruct> classes = new ArrayList<ClassStruct>();
	private ListView watchedClassesList;
	private ClassListAdapter watchedClassesListAdapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchclass);
        ctx = this;
        
        db = new PolyDatabase(this);
        
        subjectsSpinner = (Spinner) findViewById(R.id.subjectsSpinner);
        addButton = (Button) findViewById(R.id.addClass);
        courseNum = (EditText) findViewById(R.id.courseNum);
        watchedClassesList = (ListView) findViewById(R.id.watchedList);
        
        ClassSearchServer server = new ClassSearchServer();
		server.startGetSubjectsTask(new OnResponseListener(){
			@Override
			public void onResponse(JSONObject response) throws JSONException
			{
				JSONArray array = response.getJSONArray(ClassSearchServer.SUBJECTS);
				for(int i = 0; i < array.length(); i++)
					subjects.add((String)array.get(i));
				
				//populate the spinner
				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ctx,android.R.layout.simple_spinner_item, subjects);
				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				subjectsSpinner.setAdapter(dataAdapter);
			}
		});
		
		addButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				String subject = subjectsSpinner.getSelectedItem().toString();
				String courseNumber = courseNum.getText().toString();

				if(courseNum.getText().length() > 4)
					Toast.makeText(SearchClassActivity.this, "Course number too long!", Toast.LENGTH_LONG).show();
				else if(courseNum.getText().length() == 0)
					Toast.makeText(SearchClassActivity.this, "Please enter a course number!", Toast.LENGTH_LONG).show();
				else
				{
					db.addClass(subject, courseNumber);
					classes = db.getAllClasses();
					watchedClassesListAdapter = new ClassListAdapter(ctx, R.layout.watchedclasses_row, classes);
					watchedClassesList.setAdapter(watchedClassesListAdapter);
				}
			}
		});
		
		//Get watched classes from database and populate listview
		classes = db.getAllClasses();
		watchedClassesListAdapter = new ClassListAdapter(this, R.layout.watchedclasses_row, classes);
		watchedClassesList.setAdapter(watchedClassesListAdapter);
		
		scheduleClassCheckService();
    }
	
	private void scheduleClassCheckService()
	{
		int updateInterval = getSharedPreferences(SettingsActivity.APP_NAME, Context.MODE_PRIVATE).getInt(SettingsActivity.INTERVAL_KEY, SettingsActivity.twoHours);
		Intent intent = new Intent(getBaseContext(), ClassCheckService.class);
		PendingIntent pendingIntent = PendingIntent.getService(ctx, 0, intent, 0);
			
		AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), updateInterval, pendingIntent);
	}
	
	public class ClassListAdapter extends ArrayAdapter<ClassStruct>
	{
		Context context;
		int layoutResource;
		ArrayList<ClassStruct> classes;
		
		public ClassListAdapter(Context c, int resource, ArrayList<ClassStruct> data)
		{
			super(c, resource, data);
			context = c;
			classes = data;
			layoutResource = resource;
		}

		@Override
	    public View getView(int position, View convertView, ViewGroup parent)
		{
	        View row = convertView;
	        ClassHolder holder;
	        
	        if(row == null)
	        {
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            row = inflater.inflate(layoutResource, null);
	            
	            holder = new ClassHolder();
	            holder.className = (TextView) row.findViewById(R.id.fullClassName);
	            holder.deleteButton = (ImageButton) row.findViewById(R.id.deleteClass);
	            		
	            row.setTag(holder);
	        }
	        else
	        	holder = (ClassHolder)row.getTag();
	        
	        holder.dbId = classes.get(position).dbId;
	        holder.className.setText(classes.get(position).fullName);
	        
	        final int id = classes.get(position).dbId;
	        
	        holder.deleteButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View arg0)
				{
					db.deleteClass(id);
					classes = db.getAllClasses();
					watchedClassesListAdapter = new ClassListAdapter(ctx, R.layout.watchedclasses_row, classes);
					watchedClassesList.setAdapter(watchedClassesListAdapter);
				}
			});
	     
	        return row;
	    }
		
		class ClassHolder
	    {
			int dbId;
	        TextView className;
	        ImageButton deleteButton;
	    }
	}
}
