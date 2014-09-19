package com.liuym.worker;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.Navigation;
import com.liuym.nssyniassisent.R;
import com.liuym.nssyniassisent.MainData.Repair_Recode;
import com.liuym.soap.Soap.SoapInterface;

public class AssetInputActivity extends MainActivity{
	private Navigation navi = null;
	private EditText code_info_edittext = null;
	private EditText device_type_edittext = null;
	private EditText username_edittext = null;
	private EditText address_ip_edittext = null;
	private EditText address_mac_edittext = null;
	private EditText address_port_edittext = null;
	private EditText school_edittext = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_assetinput);

		code_info_edittext = (EditText)findViewById(R.id.code_info_edittext);
		device_type_edittext = (EditText)findViewById(R.id.device_type_edittext);
		username_edittext = (EditText)findViewById(R.id.username_edittext);
		address_ip_edittext = (EditText)findViewById(R.id.address_ip_edittext);
		address_mac_edittext = (EditText)findViewById(R.id.address_mac_edittext);
		address_port_edittext = (EditText)findViewById(R.id.address_port_edittext);
		school_edittext = (EditText)findViewById(R.id.school_edittext);

		navi = (Navigation)findViewById(R.id.navigationView);
		navi.getBtn_left().setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				pop();
			}
		});

		navi.getBtn_right().setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				waittingDialog.show(AssetInputActivity.this, "", "正在结案，请稍等...");
				nssySoap.Update_Device_Info(code_info_edittext.getText().toString(), 
						"", mainData.getUserInfo().Domain_UserName, address_mac_edittext.getText().toString(),
						address_ip_edittext.getText().toString(), device_type_edittext.getText().toString(), "", username_edittext.getText().toString(), 10000, new SoapInterface() {
					@Override
					public void soapResult(ArrayList<Object> arrayList) {
						waittingDialog.dismiss();
						String result = arrayList.get(0).toString();
						if(result.equals("s")){
							pop();
						}else{
							showMessage("错误信息" + result);
						}
					}

					@Override
					public void soapError(String error) {
						waittingDialog.dismiss();
						showMessage("错误信息" + error);
					}
				});
			}
		});
	}
}