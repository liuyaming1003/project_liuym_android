package com.liuym.nssyniassisent;

import java.util.Map;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        DisplayMetrics metrics=new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int widthPixels=metrics.widthPixels;
		int heightPixels=metrics.heightPixels;
		System.out.println("width = " + widthPixels + ", height = " + heightPixels);        
    }
    
    protected void push(Navigation navigation, Class<?> inClass){
		Intent intent = new Intent(MainActivity.this, inClass);		
		navigation.pushNavigation(this, intent);
	}
    
    protected void push(Navigation navigation, Class<?> inClass, String tag, Map<String, Object> map){
    	Intent intent = new Intent(MainActivity.this, inClass);	
    	
    	Bundle bundle = new Bundle();
    	SerializableMap serializableMap = new SerializableMap();
    	
    	serializableMap.setMap(map);
    	bundle.putSerializable("map", serializableMap);    	
    	intent.putExtra("tag", tag);
    	intent.putExtras(bundle);
		navigation.pushNavigation(this, intent);
    }

    protected void pop(Navigation navigation){
    	navigation.popNavigation(this);
	}

    protected void popRoot(Navigation navigation){
    	navigation.popRootNavigation(this, new Intent(MainActivity.this, LogonActivity.class));
	}
}
