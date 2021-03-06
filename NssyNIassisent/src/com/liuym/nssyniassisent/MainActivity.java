package com.liuym.nssyniassisent;

import java.util.Map;

import com.liuym.soap.NssySoapMethod;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.widget.Toast;

public class MainActivity extends Activity {
	protected MainData mainData = null;
	protected NssySoapMethod nssySoap = null;
	protected WaittingDialog waittingDialog = null;
	private Toast mToast = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DisplayMetrics metrics=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int widthPixels=metrics.widthPixels;
		int heightPixels=metrics.heightPixels;
		System.out.println("width = " + widthPixels + ", height = " + heightPixels);        


		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		waittingDialog = WaittingDialog.getDefault();
		mainData = MainData.GetDefault();
		nssySoap = NssySoapMethod.getDefault();
	}

	protected void push(Class<?> inClass){
		Intent intent = new Intent(MainActivity.this, inClass);	
		startActivity(intent);
		overridePendingTransition(R.anim.navigation_push_in,R.anim.navigation_push_out);
	}

	protected void push(Class<?> inClass, String tag, Map<String, Object> map){
		Intent intent = new Intent(MainActivity.this, inClass);	

		Bundle bundle = new Bundle();
		SerializableMap serializableMap = new SerializableMap();

		serializableMap.setMap(map);
		bundle.putSerializable("map", serializableMap);    	
		intent.putExtra("tag", tag);
		intent.putExtras(bundle);
		startActivity(intent);
		overridePendingTransition(R.anim.navigation_push_in,R.anim.navigation_push_out);
	}

	protected void push(Class<?> inClass, int requestCode){
		Intent intent = new Intent(MainActivity.this, inClass);	
		startActivityForResult(intent, requestCode);
		overridePendingTransition(R.anim.navigation_push_in,R.anim.navigation_push_out);
	}


	protected void pop(){
		finish();
		overridePendingTransition(R.anim.navigation_pop_in, R.anim.navigation_pop_out);
	}

//	protected void pop(Class<?> inClass){
//		startActivity(new Intent(MainActivity.this, inClass));
//		overridePendingTransition(R.anim.navigation_pop_in, R.anim.navigation_pop_out);
//	}

	protected void pop(int resultCode, Map<String, Object> map){
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		if(map != null){
			SerializableMap serializableMap = new SerializableMap();
			serializableMap.setMap(map);
			bundle.putSerializable("map", serializableMap); 
			intent.putExtras(bundle);
		}
		setResult(resultCode, intent);
		finish();
		overridePendingTransition(R.anim.navigation_pop_in, R.anim.navigation_pop_out);
	}
	
	protected void pop(Class<?> inClass){
		Intent intent = new Intent(MainActivity.this, inClass);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		overridePendingTransition(R.anim.navigation_pop_in, R.anim.navigation_pop_out);
	}
	
	protected void pop(Class<?> inClass, int resultCode, Map<String, Object> map){
		Intent intent = new Intent(MainActivity.this, inClass);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Bundle bundle = new Bundle();
		if(map != null){
			SerializableMap serializableMap = new SerializableMap();
			serializableMap.setMap(map);
			bundle.putSerializable("map", serializableMap); 
			intent.putExtras(bundle);
		}
		setResult(resultCode, intent);
		startActivity(intent);
		overridePendingTransition(R.anim.navigation_pop_in, R.anim.navigation_pop_out);
	}
	
	

//	protected void popRoot(){
//		Intent intent = new Intent(MainActivity.this, LogonActivity.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(intent);
//		overridePendingTransition(R.anim.navigation_pop_in, R.anim.navigation_pop_out);
//	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){  
			pop();
			return true;   
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * ��ʾ��Ϣ
	 * @param msg
	 */
	protected void showMessage(String msg){
		if(mToast != null){
			mToast.setText(msg);
		}else{
			mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		}
		mToast.show();
	}
}
