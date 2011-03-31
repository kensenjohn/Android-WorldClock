package com.clock;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class SearchCity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.list_city);

	    Intent intent = getIntent();

	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      searchCity(query);
	    }
	}
	
	private void searchCity(String query)
	{
		 Toast.makeText(getApplicationContext(), "Search For = " + query,
	             Toast.LENGTH_SHORT).show();
	}
}
