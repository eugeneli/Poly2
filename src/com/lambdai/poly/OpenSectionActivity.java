package com.lambdai.poly;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.lambdai.poly.models.SectionStruct;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class OpenSectionActivity extends Activity
{
	private ListView sectionsList;
	private ArrayList<SectionStruct> sections = new ArrayList<SectionStruct>();
	private OpenSectionListAdapter sectionListAdapter;
	
	public static final String SECTIONS_DATA = "sections";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sectionsList = (ListView) findViewById(R.id.menuList);
		
		Bundle b = getIntent().getExtras(); 
        ArrayList<String> serializedSections =  b.getStringArrayList(SECTIONS_DATA);
        
        //Deserialize sections
        for(String json : serializedSections)
        {
			try {
				sections.add(new SectionStruct(new JSONObject(json)));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        sectionListAdapter = new OpenSectionListAdapter(this, R.layout.opensections_row, sections);
        sectionsList.setAdapter(sectionListAdapter);
	}
	
	public class OpenSectionListAdapter extends ArrayAdapter<SectionStruct>
	{
		Context context;
		int layoutResource;
		ArrayList<SectionStruct> sectionsData;
		
		public OpenSectionListAdapter(Context c, int resource, ArrayList<SectionStruct> data)
		{
			super(c, resource, data);
			context = c;
			sectionsData = data;
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
	            holder.className = (TextView) row.findViewById(R.id.className);
	            holder.sectionId = (TextView) row.findViewById(R.id.secNumValue);
	            holder.section = (TextView) row.findViewById(R.id.secValue);
	            holder.prof = (TextView) row.findViewById(R.id.profValue);
	            holder.day = (TextView) row.findViewById(R.id.dayValue);
	            holder.time = (TextView) row.findViewById(R.id.timeValue);
	            holder.room = (TextView) row.findViewById(R.id.roomValue);
	            		
	            row.setTag(holder);
	        }
	        else
	        	holder = (ClassHolder)row.getTag();
	        
	        holder.className.setText(sectionsData.get(position).fullClassName);
	        holder.sectionId.setText(sectionsData.get(position).sectionId);
	        holder.section.setText(sectionsData.get(position).sectionLetter);
	        holder.prof.setText(sectionsData.get(position).professor);
	        holder.day.setText(sectionsData.get(position).day);
	        holder.time.setText(sectionsData.get(position).time);
	        holder.room.setText(sectionsData.get(position).room);
	        
	        return row;
	    }
		
		class ClassHolder
	    {
	        TextView className;
	        TextView sectionId;
	        TextView section;
	        TextView prof;
	        TextView day;
	        TextView time;
	        TextView room;
	    }
	}
}
