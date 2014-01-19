package com.lambdai.poly.models;

import org.json.JSONException;
import org.json.JSONObject;

public class SectionStruct
{
	//JSON constants
	public static final String CLASS_NAME = "class_name";
	public static final String SECTION_NUM = "section_num";
	public static final String SECTION_LETTER = "section_letter";
	public static final String PROFESSOR = "professor";
	public static final String DAY = "day";
	public static final String TIME = "time";
	public static final String ROOM = "room";
	
	public String fullClassName;
	public String sectionId;
	public String sectionLetter;
	public String professor;
	public String day;
	public String time;
	public String room;
	
	public SectionStruct()
	{
	}
	
	public SectionStruct(JSONObject json) throws JSONException
	{
		fromJSON(json);
	}
	
	//Serialization methods
	public JSONObject toJSON() throws JSONException
	{
		JSONObject json = new JSONObject();
		json.put(CLASS_NAME, fullClassName);
		json.put(SECTION_NUM, sectionId);
		json.put(SECTION_LETTER, sectionLetter);
		json.put(PROFESSOR, professor);
		json.put(DAY, day);
		json.put(TIME, time);
		json.put(ROOM, room);
		
		return json;
	}
	
	public void fromJSON(JSONObject json) throws JSONException
	{
		fullClassName = json.optString(CLASS_NAME);
		sectionId = json.getString(SECTION_NUM);
		sectionLetter = json.getString(SECTION_LETTER);
		professor = json.getString(PROFESSOR);
		day = json.getString(DAY);
		time = json.getString(TIME);
		room = json.getString(ROOM);
	}
}
