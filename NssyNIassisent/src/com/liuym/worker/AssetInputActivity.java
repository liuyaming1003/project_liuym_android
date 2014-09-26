package com.liuym.worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.liuym.adapter.MyListViewAdapter;
import com.liuym.adapter.MyListViewAdapter.ListViewInterface;
import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.Navigation;
import com.liuym.nssyniassisent.R;
import com.liuym.nssyniassisent.SerializableMap;
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
	private EditText school_edittext = null;
	private String Domain_UserName = null;
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
				teackerList();
			}
		});
		address_ip_edittext = (EditText)findViewById(R.id.address_ip_edittext);
		address_ip_edittext.setInputType(InputType.TYPE_NULL);
		address_ip_edittext.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				//teackerList();
			}
		});
		address_mac_edittext = (EditText)findViewById(R.id.address_mac_edittext);
		address_port_edittext = (EditText)findViewById(R.id.address_port_edittext);
		school_edittext = (EditText)findViewById(R.id.school_edittext);
		school_edittext.setInputType(InputType.TYPE_NULL);
		school_edittext.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				//teackerList();
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
	
	private void teackerList(){
		waittingDialog.show(AssetInputActivity.this, "", "获取使用人列表中...");
		nssySoap.Teacher_InfoList_Depart(mainData.getUserInfo().DepartID, 10000, new SoapInterface() {
			@Override
			public void soapResult(ArrayList<Object> arrayList) {
				waittingDialog.dismiss();
				String result = arrayList.get(0).toString();
				ArrayList<Map<String, Object>> teacherList = new ArrayList<Map<String,Object>>();
				if(mainData.setTeacherList(result)){
					for(int i = 0; i < mainData.getTeacherList().size(); i++){
						UserInfoList teacher = mainData.getTeacherList().get(i);
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("teacher", teacher);
						teacherList.add(map);
					}
					showSearch(teacherList);
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
	}

	private void showSearch(final ArrayList<Map<String, Object>> arrayList){
		View view = inflater.inflate(R.layout.search_view, null);
		final Handler myhandler = new Handler();
		final EditText search_edittext = (EditText)view.findViewById(R.id.search_edittext);
		final ImageView delete_imageview = (ImageView)view.findViewById(R.id.delete_imageview);

		final PopupWindow window = new PopupWindow(view,  
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true); // 实例化PopupWindow
		window.setAnimationStyle(R.style.popwin_anim_style);
		window.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
		window.setFocusable(true);
		window.setOutsideTouchable(true);

		//listView显示数据
		final ArrayList<Map<String, Object>> listData = arrayList;
		//查找内容数据
		final ArrayList<Map<String, Object>> findData = new ArrayList<Map<String,Object>>();
		final ListView listView = (ListView)view.findViewById(R.id.listView);
		
		final MyListViewAdapter myAdapter = new MyListViewAdapter(this, listData, new ListViewInterface() {
			@Override
			public View Cell(MyListViewAdapter adapter, View cellView, int position) {
				if(cellView == null){
					cellView = inflater.inflate(android.R.layout.simple_list_item_1, null);
				}
				Map<String, Object> map = (Map<String, Object>) adapter.getItem(position);
				UserInfoList teacher = (UserInfoList)map.get("teacher");
				TextView name = (TextView)cellView.findViewById(android.R.id.text1);
				name.setText(teacher.RealName);
				return cellView;
			}
		});
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){ 	       
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
					long arg3){
				Map<String, Object> map = (Map<String, Object>) myAdapter.getItem(arg2);
				UserInfoList teacher = (UserInfoList)map.get("teacher");
				Domain_UserName = teacher.Domain_UserName;
				username_edittext.setText(teacher.RealName);
				window.dismiss();
			}
		});
		listView.setAdapter(myAdapter);



		final Runnable eChanged = new Runnable() {

			@Override
			public void run() {
				String data = search_edittext.getText().toString();

				listData.clear();//先要清空，不然会叠加
				//if(!data.equals("")){
					for(int i = 0; i < arrayList.size(); i ++){
						Map<String, Object> tMap = arrayList.get(i);
						UserInfoList teacher = (UserInfoList) tMap.get("teacher");
						if(teacher.RealName.contains(data)){
							Map<String, Object> map=new HashMap<String, Object>();
							map.put("teacher", teacher);
							listData.add(map);
						}
					}
				//}
				myAdapter.notifyDataSetChanged();//更新
			}
		};

		search_edittext.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				//这个应该是在改变的时候会做的动作吧，具体还没用到过。
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				//这是文本框改变之前会执行的动作
			}

			@Override
			public void afterTextChanged(Editable s) {
				/**这是文本框改变之后 会执行的动作
				 * 因为我们要做的就是，在文本框改变的同时，我们的listview的数据也进行相应的变动，并且如一的显示在界面上。
				 * 所以这里我们就需要加上数据的修改的动作了。
				 */
				if(s.length() == 0){
					delete_imageview.setVisibility(View.GONE);//当文本框为空时，则叉叉消失
				}
				else {
					delete_imageview.setVisibility(View.VISIBLE);//当文本框不为空时，出现叉叉
				}

				myhandler.post(eChanged);
			}
		});



		delete_imageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				search_edittext.setText("");
			}
		});


		view.findViewById(R.id.cancel_button).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				window.dismiss();
			}
		});
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