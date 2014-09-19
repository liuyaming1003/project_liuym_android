package com.liuym.worker;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.MainData.Device_Info;
import com.liuym.nssyniassisent.Navigation;
import com.liuym.nssyniassisent.R;
import com.liuym.nssyniassisent.SerializableMap;
import com.liuym.soap.Soap.SoapInterface;
import com.liuym.zxing.CaptureActivity;

public class AssetQueryActivity extends MainActivity{
	private Navigation navi = null;
	private EditText input_query_edittext = null;
	private EditText code_info_edittext = null;
	private EditText username_info_edittext = null;
	private EditText address_ip_edittext = null;
	private EditText address_mac_edittext = null;
	private EditText address_port_edittext = null;
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
		
		findViewById(R.id.input_query_button).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				waittingDialog.show(AssetQueryActivity.this, "", "正在查询，请稍等...");
				nssySoap.Device_Info_List(input_query_edittext.getText().toString(), 2, 10000, new SoapInterface() {
					@Override
					public void soapResult(ArrayList<Object> arrayList) {
						waittingDialog.dismiss();
						String result = arrayList.get(0).toString();
						if(mainData.setDeviceInfoList(result)){
							Device_Info device_info = mainData.getDeviceInfoArrayList().get(0);
							code_info_edittext.setText(device_info.Device_Barcode);
							username_info_edittext.setText(device_info.Device_User);
							address_ip_edittext.setText(device_info.Device_IP_Address);
							address_mac_edittext.setText(device_info.Device_MAC_Address);
							address_port_edittext.setText(device_info.Device_Net_UP_Port);
						}else{
							showMessage("错误信息" + result);
							return;
						}
						showMessage("查询完成");
					}

					@Override
					public void soapError(String error) {
						waittingDialog.dismiss();
						showMessage("错误信息" + error);
					}
				});
			}
		});
		
		input_query_edittext = (EditText)findViewById(R.id.input_query_edittext);
		code_info_edittext = (EditText)findViewById(R.id.code_info_edittext);;
		username_info_edittext = (EditText)findViewById(R.id.username_info_edittext);;
		address_ip_edittext = (EditText)findViewById(R.id.address_ip_edittext);;
		address_mac_edittext = (EditText)findViewById(R.id.address_mac_edittext);;
		address_port_edittext = (EditText)findViewById(R.id.address_port_edittext);;
		
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
        			//input_query_edittext.setText(serializableMap.getMap().get("zxingCode").toString());
        			waittingDialog.show(AssetQueryActivity.this, "", "正在查询，请稍等...");
    				nssySoap.Device_Info_List(serializableMap.getMap().get("zxingCode").toString(), 1, 10000, new SoapInterface() {
    					@Override
    					public void soapResult(ArrayList<Object> arrayList) {
    						waittingDialog.dismiss();
    						String result = arrayList.get(0).toString();
    						if(mainData.setDeviceInfoList(result)){
    							Device_Info device_info = mainData.getDeviceInfoArrayList().get(0);
    							code_info_edittext.setText(device_info.Device_Barcode);
    							username_info_edittext.setText(device_info.Device_User);
    							address_ip_edittext.setText(device_info.Device_IP_Address);
    							address_mac_edittext.setText(device_info.Device_MAC_Address);
    							address_port_edittext.setText(device_info.Device_Net_UP_Port);
    						}else{
    							showMessage("错误信息" + result);
    							return;
    						}
    						showMessage("查询完成");
    					}

    					@Override
    					public void soapError(String error) {
    						waittingDialog.dismiss();
    						showMessage("错误信息" + error);
    					}
    				});
        		}else{
        			
        		} 
        }  
        super.onActivityResult(requestCode, resultCode, data);  
    } 
}