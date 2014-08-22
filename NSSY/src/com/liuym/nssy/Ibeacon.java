package com.liuym.nssy;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class Ibeacon extends Activity {
	private Navigation navi = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ibeacon);
		
		navi = (Navigation)findViewById(R.id.navigationView);
		navi.getBtn_left().setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
			}
		});
		
		findViewById(R.id.logout).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				pop();
			}
		});	
		
		findViewById(R.id.first).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				changedActivity(TeacherActivity.class);
			}
		});
	}
	
	private void changedActivity(Class<?> inClass){
		Intent intent = new Intent(Ibeacon.this, inClass);
		intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		startActivity(intent);
		overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
	}
	
	private void pop(){
		navi.popRootNavigation(this, new Intent(Ibeacon.this, MainActivity.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ibeacon, menu);
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			//pop
			navi.popRootNavigation(this, new Intent(Ibeacon.this, MainActivity.class));
			return true;   
		}
		return super.onKeyDown(keyCode, event);
	}

}
