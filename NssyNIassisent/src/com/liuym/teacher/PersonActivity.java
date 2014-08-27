package com.liuym.teacher;

import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.Navigation;
import com.liuym.nssyniassisent.R;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.*;

public class PersonActivity extends MainActivity {
	private Navigation navi = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person);
		
		navi = (Navigation)findViewById(R.id.navigationView);
		navi.getBtn_left().setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				pop();
			}
		});
		
		findViewById(R.id.confirm_btn).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				pop();
			}
		});		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.person, menu);
		return true;
	}

}
