package com.liuym.worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.liuym.adapter.MyListViewAdapter;
import com.liuym.adapter.MySearch;
import com.liuym.adapter.MyListViewAdapter.ListViewInterface;
import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.Navigation;
import com.liuym.nssyniassisent.R;
import com.liuym.nssyniassisent.SerializableMap;
import com.liuym.nssyniassisent.MainData.Depart_Class;
import com.liuym.nssyniassisent.MainData.Device_Info;
import com.liuym.nssyniassisent.MainData.Repair_Recode;
import com.liuym.nssyniassisent.MainData.UserInfoList;
import com.liuym.soap.Soap.SoapInterface;
import com.liuym.teacher.PersonActivity;
import com.liuym.zxing.CaptureActivity;

public class AssetInputActivity extends MainActivity{
	private LayoutInflater inflater = null;
	private View rootView = null;
	private Navigation navi = null;
	private EditText code_info_edittext = null;
	private EditText device_type_edittext = null;
	private EditText username_edittext = null;
	private EditText address_ip_edittext = null;
	private EditText address_mac_edittext = null;
	private EditText address_port_edittext = null;
	private EditText address_edittext = null;
	private String Domain_UserName = null;

	int selectItem = -1;
	View selectView = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		inflater = LayoutInflater.from(this); 
		rootView = inflater.inflate(R.layout.activity_assetinput, null);
		setContentView(rootView);

		TextView asset_input_account_name = (TextView)findViewById(R.id.asset_input_account_name);
		TextView asset_input_account_info = (TextView)findViewById(R.id.asset_input_account_info);
		String realName = mainData.getUserInfo().RealName;
		String userMail = mainData.getUserInfo().Domain_UserName + "@sznx.com.cn";
		String phone_info = mainData.getUserInfo().Mobile_Tel + "  " + mainData.getUserInfo().Group_Tel;
		asset_input_account_name.setText(realName);
		asset_input_account_info.setText(userMail + "\n" + phone_info);

		code_info_edittext = (EditText)findViewById(R.id.code_info_edittext);
		code_info_edittext.setInputType(InputType.TYPE_NULL);
		code_info_edittext.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				AssetQueryActivity.asset_query_type = 1;
				push(CaptureActivity.class, 100);
			}
		});

		device_type_edittext = (EditText)findViewById(R.id.device_type_edittext);
		device_type_edittext.setInputType(InputType.TYPE_NULL);

		username_edittext = (EditText)findViewById(R.id.username_edittext);
		username_edittext.setInputType(InputType.TYPE_NULL);
		username_edittext.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				searchList(1);
			}
		});
		address_ip_edittext = (EditText)findViewById(R.id.address_ip_edittext);
		address_ip_edittext.setInputType(InputType.TYPE_NULL);
		address_ip_edittext.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				showIpPicker();
			}
		});
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

		if(mainData.device_Info != null){
			Device_Info device = mainData.device_Info;
			//code_info_edittext.setText(device.Device_Barcode);
			device_type_edittext.setText(device.Model_Name);
			//username_edittext.setText(device.Device_Name);
			//address_ip_edittext.setText(device.Device_IP_Address);
			//address_mac_edittext.setText(device.Device_MAC_Address);
			//address_port_edittext.setText(device.Device_Net_UP_Port);
			//school_edittext.setText(device.DepartName);
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
						address_edittext.getText().toString(), mainData.getUserInfo().Domain_UserName, address_mac_edittext.getText().toString(),
						address_ip_edittext.getText().toString(), device_type_edittext.getText().toString(), address_port_edittext.getText().toString(), username_edittext.getText().toString(), 10000, new SoapInterface() {
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

	private void showIpPicker(){
		selectItem = -1;
		selectView = null;
		final Dialog dialog = new Dialog(AssetInputActivity.this, R.style.MyDialog);
		waittingDialog.show(AssetInputActivity.this, "", "获取可用ip段...");
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
					final MyListViewAdapter ip_adapter = new MyListViewAdapter(AssetInputActivity.this, ip_section_array, new ListViewInterface() {
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
							waittingDialog.show(AssetInputActivity.this, "", "获取可用ip地址...");
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
										listView_ip.setAdapter(new ArrayAdapter<String>(AssetInputActivity.this, android.R.layout.simple_list_item_1,ip_list));
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
			waittingDialog.show(AssetInputActivity.this, "", "获取使用人列表中...");
			nssySoap.Teacher_InfoList_Depart(mainData.getUserInfo().DepartID, 10000, new SoapInterface() {
				@Override
				public void soapResult(ArrayList<Object> arrayList) {
					waittingDialog.dismiss();
					String result = arrayList.get(0).toString();
					ArrayList<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
					if(mainData.setTeacherList(result)){
						for(int i = 0; i < mainData.getTeacherList().size(); i++){
							UserInfoList teacher = mainData.getTeacherList().get(i);
							Map<String, Object> map=new HashMap<String, Object>();
							map.put("object", teacher);
							list.add(map);
						}
						new MySearch().showSearch(AssetInputActivity.this, rootView, list, new MySearch.SearchInterface() {

							@Override
							public void selectCell(Object object) {
								if(object instanceof UserInfoList){
									UserInfoList userInfo = (UserInfoList)object;
									Domain_UserName = userInfo.Domain_UserName;
									username_edittext.setText(userInfo.RealName);
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
						showMessage("无老师列表信息");
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
			waittingDialog.show(AssetInputActivity.this, "", "获取存放地点列表中...");
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
						new MySearch().showSearch(AssetInputActivity.this, rootView, list, new MySearch.SearchInterface() {

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

	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  
	{  
		//可以根据多个请求代码来作相应的操作  
		if(requestCode == 100){  
			if(resultCode == 0){
				Bundle bundle = data.getExtras();  
				SerializableMap serializableMap = (SerializableMap) bundle  
						.get("map");
				code_info_edittext.setText(serializableMap.getMap().get("zxingCode").toString());
			}else{

			} 
		}  
		super.onActivityResult(requestCode, resultCode, data);  
	} 
}