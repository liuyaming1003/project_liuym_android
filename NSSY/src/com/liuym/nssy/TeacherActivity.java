package com.liuym.nssy;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;;

public class TeacherActivity extends Activity {
	private Navigation navi = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teacher);

		navi = (Navigation)findViewById(R.id.navigationView);
		navi.getBtn_left().setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				pop();
			}
		});
		
		findViewById(R.id.logout).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				pop();
			}
		});
		
		findViewById(R.id.teacherBtn).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				push(TeacherRepair.class);
			}
		});
		
		findViewById(R.id.second).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				changedActivity(Ibeacon.class);
			}
		});
		
		
	}
	
	private void changedActivity(Class<?> inClass){
		startActivity(new Intent(TeacherActivity.this, inClass));
		overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
	}
	
	private void push(Class<?> inClass){
		navi.pushNavigation(this, new Intent(TeacherActivity.this, inClass));
	}

	private void pop(){
		navi.popNavigation(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teacher, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			//pop
			navi.popNavigation(this);
			return true;   
		}
		return super.onKeyDown(keyCode, event);
	}
}
