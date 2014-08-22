package com.liuym.nssy;

//import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
	private Navigation navi = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		DisplayMetrics metrics=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int widthPixels=metrics.widthPixels;
		int heightPixels=metrics.heightPixels;
		System.out.println("width = " + widthPixels + ", height = " + heightPixels);


		navi = (Navigation)findViewById(R.id.navigationView);
		navi.getBtn_left().setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("navigation left button press");
			}
		});

		navi.getBtn_right().setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("navigation right button press");
				push();
			}
		});

		findViewById(R.id.Logon).setOnClickListener(new Button.OnClickListener() {			
			@Override
			public void onClick(View v) {
				push();
			}
		});

	}

	private void push(){
		Intent intent = new Intent(MainActivity.this, TeacherActivity.class);
		//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		navi.pushNavigation(this, intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){   
			//弹出确定退出对话框
			/*ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);  

	        List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);  

	        RunningTaskInfo rti = runningTasks.get(0);  
	        ComponentName component = rti.topActivity;  

	        Log.i("tracy", component.getClassName());  */
			return true;   
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
