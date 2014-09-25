package com.liuym.worker;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.Navigation;
import com.liuym.nssyniassisent.R;
import com.liuym.nssyniassisent.MainData.Device_Info;
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
		
		TextView asset_input_account_name = (TextView)findViewById(R.id.asset_input_account_name);
		TextView asset_input_account_info = (TextView)findViewById(R.id.asset_input_account_info);
		String realName = mainData.getUserInfo().RealName;
		String userMail = mainData.getUserInfo().Domain_UserName + "@sznx.com.cn";
		String phone_info = mainData.getUserInfo().Mobile_Tel + "  " + mainData.getUserInfo().Group_Tel;
		asset_input_account_name.setText(realName);
		asset_input_account_info.setText(userMail + "\n" + phone_info);

		code_info_edittext = (EditText)findViewById(R.id.code_info_edittext);
		device_type_edittext = (EditText)findViewById(R.id.device_type_edittext);
		username_edittext = (EditText)findViewById(R.id.username_edittext);
		address_ip_edittext = (EditText)findViewById(R.id.address_ip_edittext);
		address_mac_edittext = (EditText)findViewById(R.id.address_mac_edittext);
		address_port_edittext = (EditText)findViewById(R.id.address_port_edittext);
		school_edittext = (EditText)findViewById(R.id.school_edittext);
		
		if(mainData.device_Info != null){
			Device_Info device = mainData.device_Info;
			code_info_edittext.setText(device.Device_Barcode);
			device_type_edittext.setText(device.Model_Name);
			username_edittext.setText(device.Device_Name);
			address_ip_edittext.setText(device.Device_IP_Address);
			address_mac_edittext.setText(device.Device_MAC_Address);
			address_port_edittext.setText(device.Device_Net_UP_Port);
			school_edittext.setText(device.DepartName);
		}

		navi = (Navigation)findViewById(R.id.navigationView);
		navi.getBtn_left().setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				pop(1, null);
			}
		});

		navi.getBtn_right().setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				waittingDialog.show(AssetInputActivity.this, "", "正在分配，请稍等...");
				nssySoap.Update_Device_Info(code_info_edittext.getText().toString(), 
						"", mainData.getUserInfo().Domain_UserName, address_mac_edittext.getText().toString(),
						address_ip_edittext.getText().toString(), device_type_edittext.getText().toString(), "", username_edittext.getText().toString(), 10000, new SoapInterface() {
					@Override
					public void soapResult(ArrayList<Object> arrayList) {
						waittingDialog.dismiss();
						String result = arrayList.get(0).toString();
						if(result.equals("s")){
							pop(0, null);
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