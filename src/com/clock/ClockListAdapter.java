package com.clock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Set;
import java.util.TimeZone;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ClockListAdapter extends BaseAdapter {

	private ArrayList<String> mClocks = new ArrayList<String>();
	
	HashMap<Integer,String> hmClocks = new HashMap<Integer, String>();
	HashMap<Integer,String> hmTimeZone = new HashMap<Integer, String>();
	HashMap<Integer,String> hmClockTime = new HashMap<Integer, String>();
	
	private Context mContext;

    private LayoutInflater mInflater;

	public ClockListAdapter(Context c) {
        mContext = c;
        mInflater = LayoutInflater.from(c);
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return hmClocks.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		// Log.i("getView", "Entere GetView");
	       
		ViewHolder holder;       

        if (convertView == null) {
        	
        	holder = new ViewHolder();
        	
        	convertView = mInflater.inflate(R.layout.clock_list, null);
        	convertView.setTag(holder); 
        	

            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.timeText = (TextView) convertView.findViewById(R.id.timetext);
        }
        else
        {
        	holder = (ViewHolder) convertView.getTag();
        }
       
        //Log.i("getView", "position = " + position);
        holder.text.setText(hmClocks.get(position));
        holder.timeText.setText(hmClockTime.get(position));
		
		
        return convertView;
	}
	
	public void displayClock(ArrayList<String> arrCityId)
	{
		if(arrCityId!=null && arrCityId.size()>0)
		{
			mClocks.clear();
			for(String sCityId : arrCityId)
			{
				mClocks.add(sCityId);
			}
		}
		notifyDataSetChanged();
	}
	
	 public void addClock(String sCity) {
		 
		 //Toast.makeText(getApplicationContext(), "City Selected = " + sCity,
	       //      Toast.LENGTH_SHORT).show();
		 
		 mClocks.add(sCity);
            notifyDataSetChanged();
	}
	 
	 static class ViewHolder {
         TextView text;
         TextView timeText;
     }
	 
	 public void disableClock()
	 {

		 mHandler.removeCallbacks(mUpdateTimeTask);
		 updateClockTask.cancel(true);
	 }
	 
	 UpdateClockTask updateClockTask ;

	public void displayClock(HashMap<Integer, String> hmCityName,
			HashMap<Integer, String> hmCityTimeZone) {
		
		if(hmCityName!=null && hmCityName.size()>0)
		{
			hmClocks.clear();
			Set<Integer> setCityId = hmCityName.keySet();
			for(Integer iCityId : setCityId)
			{
				hmClocks.put(iCityId, hmCityName.get(iCityId));
			}
		}
		
		this.hmTimeZone = hmCityTimeZone;
		
		//Log.i("displayClock", " = " + this.hmClocks + " -- \n" + this.hmTimeZone);
		 notifyDataSetChanged();
		 updateClockTask = new UpdateClockTask();
		 updateClockTask.execute();

	}
	
	private Handler mHandler = new Handler();
	
	private class UpdateClockTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... arg0) {
			Log.i("doInBackground", "UpdateClockTask doInBackground");

            mHandler.removeCallbacks(mUpdateTimeTask);
            mHandler.postDelayed(mUpdateTimeTask, 100);
            
            return null;

		}
		
	}
	
	private Runnable mUpdateTimeTask = new Runnable() {
		
			String[] sDayOfWeek = {"Sun","Mon","Tue", "Wed", "Thu", "Fri", "Sat"};
		   public void run() {
			  /* final long start = mStartTime;
		       long millis = SystemClock.uptimeMillis() - start;
		       int seconds = (int) (millis / 1000);
		       int minutes = seconds / 60;
		       seconds     = seconds % 60;

		       if (seconds < 10) {
		           mTimeLabel.setText("" + minutes + ":0" + seconds);
		       } else {
		           mTimeLabel.setText("" + minutes + ":" + seconds);            
		       }
		     
		       mHandler.postAtTime(this,
		               start + (((minutes * 60) + seconds + 1) * 1000));*/
			   
			   Set<Integer> setTimeZone = hmTimeZone.keySet();
			   for(Integer iTimeZone:setTimeZone)
			   {
				   String sTimeZone = hmTimeZone.get(iTimeZone);
				   

				   Calendar cal = new GregorianCalendar(TimeZone.getTimeZone(sTimeZone));

				   int hour12 = cal.get(Calendar.HOUR);         // 0..11
				   int minutes = cal.get(Calendar.MINUTE);      // 0..59
				   int seconds = cal.get(Calendar.SECOND);      // 0..59
				   String ampm = (cal.get(Calendar.AM_PM) == Calendar.AM)?"AM":"PM";

				   int day = cal.get(Calendar.DAY_OF_WEEK);      // 1..7
				   
				   TimeZone tz = cal.getTimeZone();      // 0..59
				   
				   String sHr = (hour12<10) ? "0"+hour12 : ""+hour12;
				   String sMin = (minutes<10) ? "0"+minutes : ""+minutes;
				   String sSec = (seconds<10) ? "0"+seconds : ""+seconds;
				   
				   hmClockTime.put(iTimeZone, sHr+":"+sMin+":"+sSec+" " + ampm + " " + sDayOfWeek[day-1] + " " + tz.getDisplayName(true, TimeZone.SHORT ));
			   }
			   	Calendar nowCal = Calendar.getInstance();
			   	
			   	TimeZone tza = nowCal.getTimeZone();
			   	
				//Log.i("run", "mUpdateTimeTask = " + tza.getDisplayName() + " - " + tza.getDSTSavings() + " - " + tza.inDaylightTime(nowCal.getTime()) );
				notifyDataSetChanged();
				
				mHandler.postDelayed(this, 900);
		   }
		   
		   
	};
}
