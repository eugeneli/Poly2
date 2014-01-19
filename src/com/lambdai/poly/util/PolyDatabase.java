package com.lambdai.poly.util;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PolyDatabase extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Ping";
    private static final String CLASS_TABLE_NAME = "classes";
    
    private static final String CLASS_TABLE_ID_COLUMN = "id";
    private static final String CLASS_TABLE_SUBJECT_COLUMN = "subject";
    private static final String CLASS_TABLE_CLASSNUM_COLUMN = "class_num";
    
	public PolyDatabase(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	//Create table
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String CREATE_CLASSES_TABLE = "CREATE TABLE " + CLASS_TABLE_NAME + "("
                + "id" + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + CLASS_TABLE_SUBJECT_COLUMN + " TEXT,"
				+ CLASS_TABLE_CLASSNUM_COLUMN + " TEXT"
				+ ")";

        db.execSQL(CREATE_CLASSES_TABLE);
	}
	
	public void addClass(String subj, String classNum)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		 
        ContentValues classValues = new ContentValues();
        classValues.put(CLASS_TABLE_SUBJECT_COLUMN, subj);
        classValues.put(CLASS_TABLE_CLASSNUM_COLUMN, classNum);

        db.insert(CLASS_TABLE_NAME, null, classValues);
        db.close();
	}
	
	public void deleteClass(int id)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String deleteClass = "DELETE FROM " + CLASS_TABLE_NAME + " WHERE " + CLASS_TABLE_ID_COLUMN + " = " + id;
		db.execSQL(deleteClass);
	}
	
	public ArrayList<ClassStruct> getAllClasses()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<ClassStruct> classes = new ArrayList<ClassStruct>();

        String selectClasses = "SELECT * FROM " + CLASS_TABLE_NAME;
        Cursor cursor = db.rawQuery(selectClasses, null);
 
        if (cursor.moveToFirst())
        {
            do
            {
            	int id = cursor.getInt(0);
            	String className = cursor.getString(1) + cursor.getString(2);
            	
                //Add class to ArrayList
            	classes.add(new ClassStruct(id, className, cursor.getString(1), cursor.getString(2)));
            } 
            while (cursor.moveToNext());
        }
        cursor.close();
 
        db.close();

        //return pings
        return classes;
	}
	
	public class ClassStruct
	{
		public int dbId;
		public String fullName;
		
		public String subject;
		public String courseNum;
		
		public ClassStruct(int id, String name, String subj, String num)
        {
			dbId = id;
			fullName = name;
			
			subject = subj;
			courseNum = num;
        }
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub
		
	}
}
