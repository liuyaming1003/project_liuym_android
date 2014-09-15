package com.liuym.worker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.Navigation;
import com.liuym.nssyniassisent.R;
import com.liuym.nssyniassisent.SerializableMap;
import com.liuym.zxing.CaptureActivity;

public class AssetQueryActivity extends MainActivity{
	private Navigation navi = null;
	private EditText input_query_edittext = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_assetquery);
		
		navi = (Navigation)findViewById(R.id.navigationView);
		navi.getBtn_left().setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				pop();
			}
		});
		
		findViewById(R.id.scan_query_button).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				push(CaptureActivity.class, 100);
			}
		});
		
		input_query_edittext = (EditText)findViewById(R.id.input_query_edittext);
		
	}
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  
    {  
        //可以根据多个请求代码来作相应的操作  
        if(requestCode == 100){  
        		if(resultCode == 0){
        			Bundle bundle = data.getExtras();  
        			SerializableMap serializableMap = (SerializableMap) bundle  
        					.get("map");
        			input_query_edittext.setText(serializableMap.getMap().get("zxingCode").toString());
        		}else{
        			
        		} 
        }  
        super.onActivityResult(requestCode, resultCode, data);  
    } 
}