package com.liuym.nssyniassisent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

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
    	
    	Map<String, Object> map1=new HashMap<String, Object>();  
		map1.put("image", R.drawable.ic_launcher);  
		map1.put("title", "这是一个标题");  
		map1.put("info", "这是一个详细信息");
    	
    	serializableMap.setMap(map1);
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
