package com.clock;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class WorldClock extends ListActivity {
	
	ClockListAdapter mAdapter;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Tell the list view which view to display when the list is empty
        getListView().setEmptyView(findViewById(R.id.empty));
        
     // Set up our adapter
        mAdapter = new ClockListAdapter(this);
        setListAdapter(mAdapter);
        

        DBAdapter db = new DBAdapter(this); 
        
        db.open(); 
        
        Cursor cursorCities = db.getAllCities();
        Log.i("cityClickListener", "cursorCities = " + cursorCities + " count = " + cursorCities.getCount());
        if(cursorCities==null || (cursorCities!=null && cursorCities.getCount()==0))
        {
        	 long id;
             id = db.insertTimeZone("6d71", "US/Eastern");
             id = db.insertTimeZone("6d72", "America/Los_Angeles");
             id = db.insertCity("Bangalore", "India", "6d71", true);
             id = db.insertCity("Mysore", "India", "6d71", true);
             id = db.insertCity("Chikmagalur", "India", "6d72", true);
             id = db.insertCity("New Delhi", "India", "6d72", true);
             
        }
        cursorCities.close();
        db.close();
    }
    
    public void onPause() 
    {
    	 super.onPause();
    	 mAdapter.disableClock();

    }
    
    public void onStop() 
    {
    	 super.onStop();
    	 mAdapter.disableClock();

    }
    /** Called when the activity is first created. */
    public void onResume() {

    	 super.onResume();
    	 DBAdapter db = new DBAdapter(this); 
		 db.open();
		 Cursor cursorClockList = db.getAllClockList();
		 

		 Log.i("onResume", "onResume CAlled");
		 ArrayList<String> arrCityId = new ArrayList<String>();
		 
		 if(cursorClockList!=null )
		 {
			 cursorClockList.moveToFirst();
			 while (cursorClockList.isAfterLast() == false) 
			 {
				// Log.i("onResume", "here = ");
				 	arrCityId.add(cursorClockList.getString(1));
		        	cursorClockList.moveToNext();
		     }
		 }
		 cursorClockList.close();
		 Log.i("onResume", "City ID = " + arrCityId);
		 ArrayList<String> arrCityName = new ArrayList<String>();
		 ArrayList<String> arrCityTimeZone = new ArrayList<String>();
		 
		 HashMap<Integer,String> hmCityName = new HashMap<Integer, String>();
		 HashMap<Integer,String> hmCityTimeZone = new HashMap<Integer, String>();
			
		 Integer numOfCity = 0;
		 for(String sCityId : arrCityId)
		 {
			 Cursor cursorCityName = db.getCity(new Long(sCityId));
			 
			 cursorCityName.moveToFirst();
			
			 while (cursorCityName.isAfterLast() == false) 
			 {
				 hmCityName.put(numOfCity, cursorCityName.getString(1));
				 arrCityName.add(cursorCityName.getString(1));
				 
				 Cursor cursorTimeZone =  db.getCityTimeZone(cursorCityName.getString(3));
				 
				 cursorTimeZone.moveToFirst();
				 while (cursorTimeZone.isAfterLast() == false) 
				 {
					 hmCityTimeZone.put(numOfCity, cursorTimeZone.getString(1));
					 cursorTimeZone.moveToNext();
				 }
				 cursorTimeZone.close();
				 numOfCity++;
				 cursorCityName.moveToNext();
			 }
			 cursorCityName.close();
		}
		 db.close();
		 Log.i("onResume", "Display City = " + hmCityName);
		 Log.i("onResume", "Display City TimeZone = " + hmCityTimeZone);
		 mAdapter.displayClock(hmCityName,hmCityTimeZone);
		 
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
      return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case R.id.add:
    					addClock();
    					break;
    		case R.id.edit:
    					editClock();
    				break;
    	}
		return true;    	
    }
    
    
    private void editClock()
    {
    	 Intent i = new Intent(getBaseContext(), EditClock.class);
         startActivity(i);
    }
    private void addClock()
    {
        Intent i = new Intent(getBaseContext(), AddClock.class);
        startActivityForResult(i, Constants.ADD_CLOCK);
    }
    
    // Listen for results.
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	switch (requestCode) {
        	case Constants.ADD_CLOCK:
        		 if (resultCode == RESULT_CANCELED){
                    // myMessageboxFunction("Fight cancelled");
                 } 
                 else {
                	 String sCityName = data.getStringExtra("City");
                	
                	 
                	/* Toast.makeText(getApplicationContext(), "City Selected = " + sCityName,
            	             Toast.LENGTH_SHORT).show();*/
                	 
                	// mAdapter.addClock(sCityName);
                 }
        		break;
            default:
                break;
    	}
    }
}