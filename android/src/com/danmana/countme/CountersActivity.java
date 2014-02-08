package com.danmana.countme;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

/**
 * Main activity for displaying a list of counters.
 * 
 * @author Dan Manastireanu
 * 
 */
public class CountersActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_counters);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.counters, menu);
		return true;
	}

}
