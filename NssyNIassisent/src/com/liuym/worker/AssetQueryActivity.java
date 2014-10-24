package com.liuym.worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.liuym.adapter.MyListViewAdapter;
import com.liuym.adapter.MySearch;
import com.liuym.adapter.MyListViewAdapter.ListViewInterface;
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
	private boolean isQuery = false;
	public static int asset_query_type = 1;  // 1 默认查询  2 输入查询  3条码查询
	private Navigation navi = null;
	private EditText input_query_edittext = null;
	private EditText code_info_edittext = null;
	private EditText device_name_edittext = null;
	private EditText username_info_edittext = null;
	private String old_Domain_UserName = null;
	private String new_Domain_UserName = null;
	private EditText address_ip_edittext = null;
	private EditText address_mac_edittext = null;
	private EditText address_port_edittext = null;
	private EditText address_edittext = null;

	int selectItem = -1;
	View selectView = null;
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
				waittingDialog.show(AssetQueryActivity.this, "", "设备更新中，请稍等...");
				nssySoap.Update_Device_Info(code_info_edittext.getText().toString(), address_edittext.getText().toString(), old_Domain_UserName, 
						address_mac_edittext.getText().toString(), address_ip_edittext.getText().toString(), device_name_edittext.getText().toString(),
						address_port_edittext.getText().toString(), username_info_edittext.getText().toString(), 10000, new SoapInterface() {
					@Override
					public void soapResult(ArrayList<Object> arrayList) {
						waittingDialog.dismiss();
						String result = arrayList.get(0).toString();
						if(result.equals("s")){
							if(new_Domain_UserName != null && !new_Domain_UserName.equals(old_Domain_UserName)){
								waittingDialog.show(AssetQueryActivity.this, "", "设备重新分配中，请稍等...");
								nssySoap.Device_Redistribute(code_info_edittext.getText().toString(), username_info_edittext.getText().toString(), 10000, new SoapInterface() {
									@Override
									public void soapResult(ArrayList<Object> arrayList) {
										waittingDialog.dismiss();
										String result = arrayList.get(0).toString();
										if(result.equals("s")){
											showMessage("设备更新成功");
											if(AssetQueryActivity.asset_query_type == 3){
												pop(WorkerActivity.class, 2 , null);
											}else{
												pop();
											}
										}else{
											showMessage("设备更新失败" + result);
										}
									}

									@Override
									public void soapError(String error) {
										waittingDialog.dismiss();
										showMessage("错误信息" + error);
									}
								});
							}else{
								showMessage("设备更新成功");
								if(AssetQueryActivity.asset_query_type == 3){
									pop(WorkerActivity.class, 2 , null);
								}else{
									pop();
								}
							}
						}else{
							showMessage("更新新错误" + result);
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
							old_Domain_UserName = device_info.Domain_UserName;
							address_ip_edittext.setText(device_info.Device_IP_Address);
							address_mac_edittext.setText(device_info.Device_MAC_Address);
							address_port_edittext.setText(device_info.Device_Net_UP_Port);
							address_edittext.setText(device_info.Device_Location);
							isQuery = true;
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
				if(isQuery){
					searchList(1);
				}else{
					showMessage("没有查询设备信息");
				}
			}
		});
		address_ip_edittext = (EditText)findViewById(R.id.address_ip_edittext);
		address_ip_edittext.setInputType(InputType.TYPE_NULL);
		address_ip_edittext.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				if(isQuery){
					showIpPicker();
				}else{
					showMessage("没有查询设备信息");
				}
			}
		});
		address_mac_edittext = (EditText)findViewById(R.id.address_mac_edittext);
		address_port_edittext = (EditText)findViewById(R.id.address_port_edittext);
		address_edittext = (EditText)findViewById(R.id.address_edittext);
		address_edittext.setInputType(InputType.TYPE_NULL);
		address_edittext.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				if(isQuery){
					searchList(2);
				}else{
					showMessage("没有查询设备信息");
				}
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
				old_Domain_UserName = device_info.Domain_UserName;
				address_ip_edittext.setText(device_info.Device_IP_Address);
				address_mac_edittext.setText(device_info.Device_MAC_Address);
				address_port_edittext.setText(device_info.Device_Net_UP_Port);
				address_edittext.setText(device_info.Device_Location);
				isQuery = true;
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
							device_name_edittext.setText(device_info.Model_Name);
							username_info_edittext.setText(device_info.Device_User);
							old_Domain_UserName = device_info.Domain_UserName;
							address_ip_edittext.setText(device_info.Device_IP_Address);
							address_mac_edittext.setText(device_info.Device_MAC_Address);
							address_port_edittext.setText(device_info.Device_Net_UP_Port);
							address_edittext.setText(device_info.Device_Location);
							isQuery = true;
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

	private void showIpPicker(){
		selectItem = -1;
		selectView = null;
		final Dialog dialog = new Dialog(AssetQueryActivity.this, R.style.MyDialog);
		waittingDialog.show(AssetQueryActivity.this, "", "获取可用ip段...");
		nssySoap.IP_Section_List(mainData.getUserInfo().DepartID, 10000, new SoapInterface() {
			@Override
			public void soapResult(ArrayList<Object> arrayList) {
				waittingDialog.dismiss();
				String result = arrayList.get(0).toString();
				try {
					JSONArray array = new JSONArray(result);
					final ArrayList<Map<String, Object>> ip_section_array  = new ArrayList<Map<String,Object>>();
					for(int i = 0; i < array.length(); i++){
						JSONObject object = array.getJSONObject(i);
						String IP_Section_D = object.getString("IP_Section_D");
						Map<String, Object>map = new HashMap<String, Object>();
						map.put("ip_section", IP_Section_D);
						ip_section_array.add(map);
					} 
					final View view = inflater.inflate(R.layout.ip_picker_list, null);
					final ListView listView = (ListView)view.findViewById(R.id.ip_listview);
					final MyListViewAdapter ip_adapter = new MyListViewAdapter(AssetQueryActivity.this, ip_section_array, new ListViewInterface() {
						@Override
						public View Cell(MyListViewAdapter adapter, View cellView, int position) {
							if(cellView == null){
								cellView = inflater.inflate(android.R.layout.simple_list_item_1, null);
							}
							TextView text = (TextView)cellView.findViewById(android.R.id.text1);
							Map<String, Object>map = (Map<String, Object>)adapter.getItem(position);
							text.setText(map.get("ip_section").toString());

							if(position == selectItem){
								cellView.setBackgroundColor(Color.WHITE);
							}else{
								cellView.setBackgroundColor(Color.TRANSPARENT);
							}

							return cellView;
						}
					});
					listView.setAdapter(ip_adapter);
					listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
					listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){ 	       
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
								long arg3){
							if(selectItem == arg2){
								return;
							}
							selectItem = arg2;
							ip_adapter.notifyDataSetChanged();
							Map<String, Object> map = (Map<String, Object>)ip_section_array.get(arg2);
							final String ip_section = map.get("ip_section").toString();
							waittingDialog.show(AssetQueryActivity.this, "", "获取可用ip地址...");
							nssySoap.IP_List_Detail(ip_section, 10, 10000, new SoapInterface() {
								@Override
								public void soapResult(ArrayList<Object> arrayList) {
									waittingDialog.dismiss();
									String result = arrayList.get(0).toString();
									try {
										JSONArray array = new JSONArray(result);
										final List<String> ip_list = new ArrayList<String>();
										int length = array.length();
										for(int i = 0; i < (length < 10 ? length : 10); i++){
											JSONObject object = array.getJSONObject(i);
											String IP_address = object.getString("IP_address");
											ip_list.add(IP_address);
										}
										final ListView listView_ip = (ListView)view.findViewById(R.id.ip_listview1);
										listView_ip.setAdapter(new ArrayAdapter<String>(AssetQueryActivity.this, android.R.layout.simple_list_item_1,ip_list));
										listView_ip.setOnItemClickListener(new AdapterView.OnItemClickListener(){ 	       
											public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
													long arg3){
												String ip = ip_list.get(arg2);
												address_ip_edittext.setText(ip_section + "." + ip);
												dialog.dismiss();
											}
										});
									} 
									catch (JSONException e) {
										e.printStackTrace();
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
					dialog.setContentView(view);
					dialog.show();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void soapError(String error) {
				waittingDialog.dismiss();
				showMessage("错误信息" + error);
			}
		});
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
									new_Domain_UserName = userInfo.Domain_UserName;
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