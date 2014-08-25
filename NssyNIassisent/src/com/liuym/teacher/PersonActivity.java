package com.liuym.teacher;

import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PersonActivity extends MainActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.person, menu);
		return true;
	}

}
