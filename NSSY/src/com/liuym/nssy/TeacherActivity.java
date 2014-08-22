package com.liuym.nssy;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;;

public class TeacherActivity extends Activity {
	private Navigation navi = null;
	private View teackerView = null;
	private View ibeactonView = null;
	private boolean ibViewIsFirst = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_teacher);

		LayoutInflater inflater = LayoutInflater.from(this); 
		teackerView = inflater.inflate(R.layout.activity_teacher, null);
		ibeactonView = inflater.inflate(R.layout.activity_ibeacon, null);
		
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
				popRoot();
			}
		});
		
		findViewById(R.id.first).setEnabled(true);
		
		findViewById(R.id.teacherBtn).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				push(TeacherRepair.class);
			}
		});
		
		findViewById(R.id.second).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				setIbeaconView();
			}
		});
	}
	
	private void setTeackerView(){
		setContentView(teackerView);
	}
	
	private void setIbeaconView(){
		if(ibViewIsFirst){
			ibViewIsFirst = false;
			findViewById(R.id.logout).setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					popRoot();
				}
			});
			
			findViewById(R.id.second).setEnabled(true);
			
			findViewById(R.id.first).setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					setTeackerView();
				}
			});
		}
	}
	
	private void changedActivity(Class<?> inClass){
		Intent intent = new Intent(TeacherActivity.this, inClass);
		intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		startActivity(intent);
		overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
	}
	
	private void push(Class<?> inClass){
		Intent intent = new Intent(TeacherActivity.this, inClass);		
		navi.pushNavigation(this, intent);
	}

	private void pop(){
		navi.popNavigation(this);
	}
	
	private void popRoot(){
		navi.popRootNavigation(this, new Intent(TeacherActivity.this, MainActivity.class));
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
			navi.popRootNavigation(this, new Intent(TeacherActivity.this, MainActivity.class));
			return true;   
		}
		return super.onKeyDown(keyCode, event);
	}
}
