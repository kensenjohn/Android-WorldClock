package com.clock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter 
{
/*    public static final String KEY_ROWID = "_id";
    public static final String KEY_ISBN = "isbn";
    public static final String KEY_TITLE = "title";
    public static final String KEY_PUBLISHER = "publisher";  */
	
	public static final String KEY_CITY_ID = "_id";
	public static final String KEY_CITY_CITYNAME = "cityname";
	public static final String KEY_CITY_COUNTRY = "country";
	public static final String KEY_CITY_TIMEZONEID = "timezone_id";
	public static final String KEY_CITY_ACTIVE = "active_city";
	
	public static final String KEY_TZ_ID = "_id";
	public static final String KEY_TZ_TIMEZONE = "timezone";
	
	public static final String KEY_CLOCKLIST_ID = "_id";
	public static final String KEY_CLOCKLIST_CITYID = "cityid";
	
    private static final String TAG = "DBAdapter";
    
    private static final String DATABASE_NAME = "CityTimeZone";
    private static final String DATABASE_CITY_TABLE = "city";
    private static final String DATABASE_TIMEZONE_TABLE = "timezones";
    private static final String DATABASE_CLOCKLIST_TABLE = "clocklist";
    private static final int DATABASE_VERSION = 1;

    private static final String CITY_TABLE_CREATE =
        "CREATE TABLE city (_id INTEGER primary key autoincrement, "
        + "cityname VARCHAR NOT NULL, country VARCHAR NOT NULL, "
        + "timezone_id  INTEGER, "
        + "active_city BOOLEAN DEFAULT 1, "
        + "FOREIGN KEY(timezone_id) REFERENCES timezones(_id));";
    
    private static final String CLOCKLIST_TABLE_CREATE =
        "CREATE TABLE clocklist (_id INTEGER primary key autoincrement, "
        + "cityid INTEGER NOT NULL, "
        + "FOREIGN KEY(cityid) REFERENCES city(_id));";
    
    private static final String TIME_ZONE_TABLE_CREATE =
        "CREATE TABLE timezones (_id VARCHAR PRIMARY KEY , "
        + "timezone VARCHAR NOT NULL);";
        
    private final Context context; 
    
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
        
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
            db.execSQL(CITY_TABLE_CREATE);
            db.execSQL(TIME_ZONE_TABLE_CREATE);
            db.execSQL(CLOCKLIST_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion 
                    + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS titles");
            onCreate(db);
        }
    }    
    
    //---opens the database---
    public DBAdapter open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    

    //---insert a title into the database---
    public long insertCity(String cityname, String country, String timezone_id, boolean isActive) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CITY_CITYNAME, cityname);
        initialValues.put(KEY_CITY_COUNTRY, country);
        initialValues.put(KEY_CITY_TIMEZONEID, timezone_id);
        initialValues.put(KEY_CITY_ACTIVE, isActive);
        return db.insert(DATABASE_CITY_TABLE, null, initialValues);
    }
    
    public long insertTimeZone(String id, String timezone) 
    {
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TZ_ID, id);
        initialValues.put(KEY_TZ_TIMEZONE, timezone);
        return db.insert(DATABASE_TIMEZONE_TABLE, null, initialValues);
    }
    
    public long insertClockList(String sCityId)
    {
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CLOCKLIST_CITYID, sCityId);
        return db.insert(DATABASE_CLOCKLIST_TABLE, null, initialValues);
    }

    //---deletes a particular City---
    public boolean deleteCity(long rowId) 
    {
        return db.delete(DATABASE_CITY_TABLE, KEY_CITY_ID + 
        		"=" + rowId, null) > 0;
    }
    
    //---deletes a particular City---
    public boolean deleteTimeZone(String rowId) 
    {
        return db.delete(DATABASE_TIMEZONE_TABLE, KEY_TZ_ID + 
        		"=" + rowId, null) > 0;
    }

    //---retrieves all the Cities---
    public Cursor getAllCities() 
    {
        return db.query(DATABASE_CITY_TABLE, new String[] {
        		KEY_CITY_ID, 
        		KEY_CITY_CITYNAME,
        		KEY_CITY_COUNTRY,
                KEY_CITY_TIMEZONEID}, 
                null, 
                null, 
                null, 
                null, 
                null);
    }
    
    public Cursor getAllClockList()
    {
    	return db.query(DATABASE_CLOCKLIST_TABLE, new String[] {
        		KEY_CLOCKLIST_ID, 
        		KEY_CLOCKLIST_CITYID}, 
                null, 
                null, 
                null, 
                null, 
                null);
    }
    
    //---retrieves all the Cities---
    public Cursor getAllTimeZones() 
    {
        return db.query(DATABASE_TIMEZONE_TABLE, new String[] {
        		KEY_TZ_ID, 
        		KEY_TZ_TIMEZONE}, 
                null, 
                null, 
                null, 
                null, 
                null);
    }
    
    //---retrieves a particular TimeZone---
    public Cursor getCityTimeZone(String timeZoneId) throws SQLException 
    {
    	 Cursor mCursor =
             db.query(true, DATABASE_TIMEZONE_TABLE, new String[] {
             		KEY_TZ_ID, 
            		KEY_TZ_TIMEZONE}, 
            		KEY_TZ_ID + "='" + timeZoneId + "'", 
                     null,
             		null, 
             		null, 
             		null, 
             		null);
     if (mCursor != null) {
         mCursor.moveToFirst();
     }
     return mCursor;
    }

    //---retrieves a particular City---
    public Cursor getCity(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_CITY_TABLE, new String[] {
                		KEY_CITY_ID, 
                		KEY_CITY_CITYNAME,
                		KEY_CITY_COUNTRY,
                        KEY_CITY_TIMEZONEID}, 
                        KEY_CITY_ID + "=" + rowId, 
                        null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor getClockList(long cityId) throws SQLException 
    {
    	 Cursor mCursor =
             db.query(true, DATABASE_CLOCKLIST_TABLE, new String[] {
             		KEY_CLOCKLIST_ID, 
             		KEY_CLOCKLIST_CITYID}, 
             		KEY_CLOCKLIST_CITYID + "=" + cityId, 
                     null,
             		null, 
             		null, 
             		null, 
             		null);
     if (mCursor != null) {
         mCursor.moveToFirst();
     }
     return mCursor;
    }
    
    public Cursor getDataFromDB(String sQuery,String[] selectionArgs) throws SQLException
    {
    	 Cursor mCursor = db.rawQuery(sQuery, selectionArgs);
    	 
         if (mCursor != null) {
             mCursor.moveToFirst();
         }
         return mCursor;
    }

    //---updates a city---
    public boolean updateCity(long rowId, String cityname, 
    String country, String timezoneid) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_CITY_CITYNAME, cityname);
        args.put(KEY_CITY_COUNTRY, country);
        args.put(KEY_CITY_TIMEZONEID, timezoneid);
        return db.update(DATABASE_CITY_TABLE, args, 
        		KEY_CITY_ID + "=" + rowId, null) > 0;
    }
}
/*package com.clock;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter 
{
    public static final String KEY_ROWID = "_id";
    public static final String KEY_CITY = "cityname";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_TIME_ZONE = "timezone";  
    private static final String TAG = "DBAdapter";
    
    private static final String DATABASE_NAME = "CityTimezones";
    private static final String DATABASE_TABLE = "city";
    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_PATH = "/data/data/com.clock/databases/";

    private static final String DATABASE_CREATE =
        "create table titles (_id integer primary key autoincrement, "
        + "isbn text not null, title text not null, " 
        + "publisher text not null);";
        
    private static Context context; 
    
    private DatabaseHelper DBHelper;
    private static SQLiteDatabase db;

    public DBAdapter(Context ctx) 
    {
        context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
    
    public void createDataBase()
    {
    	try {
			DBHelper.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
        
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
    	
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion 
                    + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS titles");
            onCreate(db);
        }
        
        *//**
         * Creates a empty database on the system and rewrites it with your own database.
         * *//*
        public void createDataBase() throws IOException{
     
        	boolean dbExist = checkDataBase();
     
        	if(dbExist){
        		//do nothing - database already exist
        	}else{
     
        		//By calling this method and empty database will be created into the default system path
                   //of your application so we are gonna be able to overwrite that database with our database.
            	this.getReadableDatabase();
     
            	try {
     
        			copyDataBase();
     
        		} catch (IOException e) {
     
            		throw e;
     
            	}
        	}
     
        }
     
        *//**
         * Check if the database already exist to avoid re-copying the file each time you open the application.
         * @return true if it exists, false if it doesn't
         *//*
        private boolean checkDataBase(){
     
        	SQLiteDatabase checkDB = null;
     
        	try{
        		String myPath = DATABASE_PATH + DATABASE_NAME;
        		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
     
        	}catch(SQLiteException e){
     
        		//database does't exist yet.
     
        	}
     
        	if(checkDB != null){
     
        		checkDB.close();
     
        	}
     
        	return checkDB != null ? true : false;
        }
     
        *//**
         * Copies your database from your local assets-folder to the just created empty database in the
         * system folder, from where it can be accessed and handled.
         * This is done by transfering bytestream.
         * *//*
        private void copyDataBase() throws IOException{
     
        	//Open your local db as the input stream
        	//InputStream myInput = context.getAssets().open(DATABASE_NAME);
        	final AssetManager assetMgr = context.getResources().getAssets();
        	InputStream myInput = assetMgr.open(DATABASE_NAME + ".sqlite"); 
        	// Path to the just created empty db
        	String outFileName = DATABASE_PATH + DATABASE_NAME;
     
        	//Open the empty db as the output stream
        	OutputStream myOutput = new FileOutputStream(outFileName);
     
        	//transfer bytes from the inputfile to the outputfile
        	byte[] buffer = new byte[1024];
        	int length;
        	while ((length = myInput.read(buffer))>0){
        		myOutput.write(buffer, 0, length);
        	}
     
        	//Close the streams
        	myOutput.flush();
        	myOutput.close();
        	myInput.close();
     
        }
     
        public void openDataBase() throws SQLException{
     
        	//Open the database
        String myPath = DATABASE_PATH + DATABASE_NAME;
        	db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
     
        }
     
        @Override
    	public synchronized void close() {
     
        	    if(db != null)
        		    db.close();
     
        	    super.close();
     
    	}
     
    	@Override
    	public void onCreate(SQLiteDatabase db) {
     
    	}
     
    	@Override
    	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     
    	}
    }    
    
    //---opens the database---
    public DBAdapter open() throws SQLException 
    {
        DBHelper.openDataBase();
        return this;
    }

    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    
    public long insertCity(String _id, String cityname, String country, String timezone) 
    {
    	  ContentValues initialValues = new ContentValues();
          initialValues.put(KEY_ROWID, _id);
          initialValues.put(KEY_CITY, cityname);
          initialValues.put(KEY_COUNTRY, country);
          initialValues.put(KEY_TIME_ZONE, timezone);
          return db.insert(DATABASE_TABLE, null, initialValues);
    }
    
    //---insert a title into the database---
    public long insertTitle(String isbn, String title, String publisher) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ISBN, isbn);
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_PUBLISHER, publisher);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular title---
    public boolean deleteTitle(long rowId) 
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + 
        		"=" + rowId, null) > 0;
    }

    //---retrieves all the titles---
    public Cursor getAllTitles() 
    {
        return db.query(DATABASE_TABLE, new String[] {
        		KEY_ROWID, 
        		KEY_ISBN,
        		KEY_TITLE,
                KEY_PUBLISHER}, 
                null, 
                null, 
                null, 
                null, 
                null);
    }

    //---retrieves a particular title---
    public Cursor getTitle(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                		KEY_ROWID,
                		KEY_ISBN, 
                		KEY_TITLE,
                		KEY_PUBLISHER
                		}, 
                		KEY_ROWID + "=" + rowId, 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a title---
    public boolean updateTitle(long rowId, String isbn, 
    String title, String publisher) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_ISBN, isbn);
        args.put(KEY_TITLE, title);
        args.put(KEY_PUBLISHER, publisher);
        return db.update(DATABASE_TABLE, args, 
                         KEY_ROWID + "=" + rowId, null) > 0;
    }
}
*/