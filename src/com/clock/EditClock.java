package com.clock;

import java.util.HashMap;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class EditClock extends ListActivity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  

		  createClockListView(this);
		  //setUserListener();
	}
	
	HashMap<Integer,String> hmClockList = new HashMap<Integer, String>();
	
	private void createClockListView(EditClock addClock) {
		 DBAdapter db = new DBAdapter(this); 
		 db.open();
		 Cursor cursorClockList = db.getAllClockList();
		 
		 cursorClockList.moveToFirst();

		 Integer iClockLoc = 0;
		 while (cursorClockList.isAfterLast() == false) {
			 hmClockList.put(iClockLoc, cursorClockList.getString(1));
			 iClockLoc++;
			 cursorClockList.moveToNext();
		 }
		 cursorClockList.close();
		 String sCityId = "";
		 boolean isFirst = true;
		 for(Integer i = 0; i<hmClockList.size(); i++)
		 {
			 if(!isFirst)
			 {
				 sCityId = sCityId + ",";
			 }
			 sCityId = sCityId +"'"+ hmClockList.get(i) + "'";
			 isFirst = false;
		 }
		 
		 String sQuery = "select _id, cityname from city where  _id in ("+sCityId+")";
		 String[] colSel =new String[]{"_id", "cityname"};
		 
		 Cursor cursorClockCity = db.getDataFromDB(sQuery, null);
		 
		 
		 //"select * from clocklist cl, city ct where cl.cityid=ct._id and cl.cityid in ('2','4')";

	     startManagingCursor(cursorClockCity);
		 ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.delete_clock_list,cursorClockCity,new String[] {"cityname"},new int[] {R.id.clockcity});
	     setListAdapter(adapter);

	     db.close();
	}

}
