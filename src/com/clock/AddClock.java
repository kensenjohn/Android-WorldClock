package com.clock;

import java.util.HashMap;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddClock extends ListActivity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
	     
		  createCityListView(this);
		  setUserListener();
		  
		 /* Intent intent = getIntent();

	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      searchCity(query);
	    }*/
		  
	}
	
/*	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchcitymenu, menu);
      return super.onCreateOptionsMenu(menu);
    }
	
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	switch (item.getItemId()) {
	    		case R.id.searchcity:
	    					searchCity();
	    					break;
	    		case R.id.edit:
	    				break;
	    	}
			return true;    	
	    }*/
	 
	    
	    
	    private void searchCity(String query)
		{
			 Toast.makeText(getApplicationContext(), "Search For = " + query,
		             Toast.LENGTH_SHORT).show();
		}

	private void setUserListener() {
		   ListView lv = getListView();
		     lv.setTextFilterEnabled(true);

		     lv.setOnItemClickListener(cityClickListener);
		
	}
	
	 @Override
	    protected void onNewIntent(Intent intent) {
	        // Because this activity has set launchMode="singleTop", the system calls this method
	        // to deliver the intent if this actvity is currently the foreground activity when
	        // invoked again (when the user executes a search from this activity, we don't create
	        // a new instance of this activity, so the system delivers the search intent here)
	        handleIntent(intent);
	    }
	 
	 private void handleIntent(Intent intent) {
	      /*  if (Intent.ACTION_VIEW.equals(intent.getAction())) {
	            // handles a click on a search suggestion; launches activity to show word
	            Intent wordIntent = new Intent(this, WordActivity.class);
	            wordIntent.setData(intent.getData());
	            startActivity(wordIntent);
	            finish();
	        } else */
		 if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			 String query = intent.getStringExtra(SearchManager.QUERY);
	            searchCity("VIWERWE = "+query);
		 }else   if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	            // handles a search query
	            String query = intent.getStringExtra(SearchManager.QUERY);
	            searchCity(query);
	        }
	    }

	 HashMap<Integer,String> hmCity = new HashMap<Integer, String>();
	private void createCityListView(AddClock addClock) {
		
		// String[] countries = getResources().getStringArray(R.array.cities_array);
		
		 DBAdapter db = new DBAdapter(this); 
		 db.open();
		 Cursor cursorCities = db.getAllCities();
		 
		 cursorCities.moveToFirst();
		 Integer iCityLoc = 0;
	        while (cursorCities.isAfterLast() == false) {
	        	hmCity.put(iCityLoc, cursorCities.getString(0));
	        	iCityLoc++;
	        	cursorCities.moveToNext();
	        }
	        startManagingCursor(cursorCities);
		 ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.addclock,cursorCities,new String[] {"cityname"},new int[] {R.id.city});
	     setListAdapter(adapter);

	     db.close();
	}
	
	/*
	 * private void fillDestinations(String startCity){simple_spinner_item
//Cursor consulting database
cdestination = DataBaseHelper.myDataBase.rawQuery("select cities._id, relacions.destination, cities.name from cities, relations where relacions.startCity='" + startCity + "' and cities.code=relacions.destination", null);
 
//Adapter from cursor
addestination = new SimpleCursorAdapter(this,android.R.layout.simple_spinner_item,cdestination,new String[] {"nom"},new int[] {android.R.id.text1});
 
addestination.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 
//Filling the spinner
spndestination.setAdapter(addestination);
}
	 */
	
	OnItemClickListener cityClickListener = new OnItemClickListener() {
	       public void onItemClick(AdapterView<?> parent, View view,
	           int position, long id) {
	         // When clicked, show a toast with the TextView text
	        // Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
	        //     Toast.LENGTH_SHORT).show();
	    	   TextView lv1 = (TextView)findViewById(R.id.city);
	    	   Log.i("cityClickListener", "position= " + position + " hmCity.get() = " + hmCity.get(position) + " hmCity = " + hmCity);
	         
	         String sClockListID = getClockList(hmCity.get(position));
	         
	         if(sClockListID == null || "".equalsIgnoreCase(sClockListID))
	         {
	        	
	        	 putClockListCity(hmCity.get(position));
	        	 
	        	 Intent intent = new Intent();
		         intent.putExtra("City", hmCity.get(position));
		         setResult(RESULT_OK, intent );
		         finish();
	         }
	         else
	         {
	        	 Toast.makeText(getApplicationContext(), "This City is already present in your list.",
			             Toast.LENGTH_SHORT).show();
	         }
	         
	        
	       }
	     };
	     
	     public void putClockListCity(String sCityID)
	     {
	    	 DBAdapter db = new DBAdapter(this); 
			 db.open();
			 
			 long  id = db.insertClockList(sCityID);
             
             db.close();
            
	     }
	     
	     public String getClockList(String sCityID)
	     {
	    	 DBAdapter db = new DBAdapter(this); 
			 db.open();
			 Cursor cursorClockList = db.getClockList(new Long(sCityID));
			 
			 String sClockList = "";
			 
			 if(cursorClockList!=null )
			 {
				 cursorClockList.moveToFirst();
			        while (cursorClockList.isAfterLast() == false) {
			        	sClockList = cursorClockList.getString(0);
			        	cursorClockList.moveToNext();
			        }
			 }
			 cursorClockList.close();
			 db.close();
			 return sClockList;
	     }
	
	
}
