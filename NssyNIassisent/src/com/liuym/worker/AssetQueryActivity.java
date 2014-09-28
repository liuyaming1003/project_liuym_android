package com.liuym.worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.liuym.adapter.MySearch;
import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.MainData.Depart_Class;
import com.liuym.nssyniassisent.MainData.Device_Info;
import com.liuym.nssyniassisent.MainData.UserInfoList;
import com.liuym.nssyniassisent.Navigation;
import com.liuym.nssyniassisent.R;
import com.liuym.nssyniassisent.SerializableMap;
import com.liuym.soap.Soap.SoapInterface;
import com.liuym.zxing.CaptureActivity;

public class AssetQueryActivity extends MainActivity{
	private LayoutInflater inflater = null;
	private View rootView = null;
	public static int asset_query_type = 1;  // 1 默认查询  2 输入查询  3条码查询
	private Navigation navi = null;
	private EditText input_query_edittext = null;
	private EditText code_info_edittext = null;
	private EditText device_name_edittext = null;
	private EditText username_info_edittext = null;
	private EditText address_ip_edittext = null;
	private EditText address_mac_edittext = null;
	private EditText address_port_edittext = null;
	private EditText address_edittext = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		inflater = LayoutInflater.from(this); 
		rootView = inflater.inflate(R.layout.activity_assetquery, null);
		setContentView(rootView);

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
				if(input_query_edittext.getText().toString().equals("")){
					showMessage("请输入查询信息");
					return;
				}

				waittingDialog.show(AssetQueryActivity.this, "", "正在查询，请稍等...");
				nssySoap.Device_Info_List(input_query_edittext.getText().toString(), 2, 10000, new SoapInterface() {
					@Override
					public void soapResult(ArrayList<Object> arrayList) {
						waittingDialog.dismiss();
						String result = arrayList.get(0).toString();
						if(mainData.setDeviceInfoList(result)){
							Device_Info device_info = mainData.getDeviceInfoArrayList().get(0);
							code_info_edittext.setText(device_info.Device_Barcode);
							device_name_edittext.setText(device_info.Model_Name);
							username_info_edittext.setText(device_info.Device_User);
							address_ip_edittext.setText(device_info.Device_IP_Address);
							address_mac_edittext.setText(device_info.Device_MAC_Address);
							address_port_edittext.setText(device_info.Device_Net_UP_Port);
							address_edittext.setText(device_info.Device_Location);
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
		code_info_edittext = (EditText)findViewById(R.id.code_info_edittext);
		code_info_edittext.setInputType(InputType.TYPE_NULL);
		device_name_edittext = (EditText)findViewById(R.id.device_name_edittext);
		device_name_edittext.setInputType(InputType.TYPE_NULL);
		username_info_edittext = (EditText)findViewById(R.id.username_info_edittext);
		username_info_edittext.setInputType(InputType.TYPE_NULL);
		username_info_edittext.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				searchList(1);
			}
		});
		address_ip_edittext = (EditText)findViewById(R.id.address_ip_edittext);
		address_mac_edittext = (EditText)findViewById(R.id.address_mac_edittext);
		address_port_edittext = (EditText)findViewById(R.id.address_port_edittext);
		address_edittext = (EditText)findViewById(R.id.address_edittext);
		address_edittext.setInputType(InputType.TYPE_NULL);
		address_edittext.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				searchList(2);
			}
		});


		//
		if(AssetQueryActivity.asset_query_type == 1){

		}else if(AssetQueryActivity.asset_query_type == 2){
			findViewById(R.id.zxing_query_relativelayout).setVisibility(View.GONE);
		}else if(AssetQueryActivity.asset_query_type == 3){
			findViewById(R.id.zxing_query_relativelayout).setVisibility(View.GONE);
			findViewById(R.id.input_query_relativelayout).setVisibility(View.GONE);
			Device_Info device_info = mainData.device_Info;
			if(device_info != null){
				code_info_edittext.setText(device_info.Device_Barcode);
				device_name_edittext.setText(device_info.Model_Name);
				username_info_edittext.setText(device_info.Device_User);
				address_ip_edittext.setText(device_info.Device_IP_Address);
				address_mac_edittext.setText(device_info.Device_MAC_Address);
				address_port_edittext.setText(device_info.Device_Net_UP_Port);
				address_edittext.setText(device_info.Device_Location);
			}
		}else{

		}
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

	private void searchList(int index){
		switch(index){
		case 1:
			waittingDialog.show(AssetQueryActivity.this, "", "获取使用人列表中...");
			nssySoap.Teacher_InfoList_Depart(mainData.getUserInfo().DepartID, 10000, new SoapInterface() {
				@Override
				public void soapResult(ArrayList<Object> arrayList) {
					waittingDialog.dismiss();
					String result = arrayList.get(0).toString();
					ArrayList<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
					if(mainData.setTeacherList(result)){
						for(int i = 0; i < mainData.getTeacherList().size(); i++){
							Object object = mainData.getTeacherList().get(i);
							Map<String, Object> map=new HashMap<String, Object>();
							map.put("object", object);
							list.add(map);
						}
						//showSearch(list);
						new MySearch().showSearch(AssetQueryActivity.this, rootView, list, new MySearch.SearchInterface() {

							@Override
							public void selectCell(Object object) {
								if(object instanceof UserInfoList){
									UserInfoList userInfo = (UserInfoList)object;
									//Domain_UserName = userInfo.Domain_UserName;
									username_info_edittext.setText(userInfo.RealName);
								}
							}

							@Override
							public boolean changedText(Object object, String data) {
								if(object instanceof UserInfoList){
									UserInfoList userInfo = (UserInfoList) object;
									if(userInfo.RealName.contains(data)){
										return true;
									}
								}
								return false;
							}

							@Override
							public View Cell(View cellView, Object object) {
								if(cellView == null){
									cellView = inflater.inflate(android.R.layout.simple_list_item_1, null);
								}
								TextView name = (TextView)cellView.findViewById(android.R.id.text1);
								if(object instanceof UserInfoList){
									UserInfoList userInfo = (UserInfoList)object;
									name.setText(userInfo.RealName);
								}
								return cellView;
							}
						});
					}else{
						showMessage("无使用人列表信息");
					}
				}

				@Override
				public void soapError(String error) {
					waittingDialog.dismiss();
					showMessage("错误信息" + error);
				}
			});
			break;
		case 2:
			waittingDialog.show(AssetQueryActivity.this, "", "获取存放地点列表中...");
			nssySoap.Deaprt_Room_list(mainData.getUserInfo().DepartID, 2, 10000, new SoapInterface() {
				@Override
				public void soapResult(ArrayList<Object> arrayList) {
					waittingDialog.dismiss();
					String result = arrayList.get(0).toString();
					ArrayList<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
					if(mainData.setRoomList(result)){
						for(int i = 0; i < mainData.getRoomArrayList().size(); i++){
							Object object = mainData.getRoomArrayList().get(i);
							Map<String, Object> map=new HashMap<String, Object>();
							map.put("object", object);
							list.add(map);
						}
						//showSearch(list);
						new MySearch().showSearch(AssetQueryActivity.this, rootView, list, new MySearch.SearchInterface() {

							@Override
							public void selectCell(Object object) {
								if(object instanceof Depart_Class){
									Depart_Class room = (Depart_Class)object;
									String text = room.Room_Name;
									if(!room.Room_Num.equals("null")){
										text = text + room.Room_Num;
									}
									address_edittext.setText(text);
								}
							}

							@Override
							public boolean changedText(Object object, String data) {
								if(object instanceof Depart_Class){
									Depart_Class room = (Depart_Class)object;
									String text = room.Room_Name;
									if(!room.Room_Num.equals("null")){
										text = text + room.Room_Num;
									}
									if(text.contains(data)){
										return true;
									}
								}
								return false;
							}

							@Override
							public View Cell(View cellView, Object object) {
								if(cellView == null){
									cellView = inflater.inflate(android.R.layout.simple_list_item_1, null);
								}
								TextView name = (TextView)cellView.findViewById(android.R.id.text1);
								if(object instanceof Depart_Class){
									Depart_Class room = (Depart_Class)object;
									String text = room.Room_Name;
									if(!room.Room_Num.equals("null")){
										text = text + room.Room_Num;
									}
									name.setText(text);
								}
								return cellView;
							}
						});
					}else{
						showMessage("无存放地点列表信息");
					}
				}

				@Override
				public void soapError(String error) {
					waittingDialog.dismiss();
					showMessage("错误信息" + error);
				}
			});
			break;
		}

	}
}