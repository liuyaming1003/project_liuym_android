package com.liuym.worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.liuym.adapter.MyListView;
import com.liuym.adapter.MyListViewAdapter;
import com.liuym.adapter.MyViewPagerAdapter;
import com.liuym.adapter.MyListView.OnRefreshListener;
import com.liuym.adapter.MyListViewAdapter.ListViewInterface;
import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.Navigation;
import com.liuym.nssyniassisent.R;
import com.liuym.nssyniassisent.SerializableMap;
import com.liuym.nssyniassisent.MainData.Device_Info;
import com.liuym.nssyniassisent.MainData.Repair_Recode;
import com.liuym.soap.Soap.SoapInterface;

public class DeviceList extends MainActivity{
	private LayoutInflater inflater = null;
	private Navigation navi = null;
	private boolean isFirstLaunch = true;
	//tableview
	private ViewPager viewPager = null;
	private View device_view = null;
	private int  select_cell_index = -1;
	private List<Map<String, Object>> device_list = null;
	private MyListViewAdapter deviceAdapter = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_device_list);
		inflater = LayoutInflater.from(this); 
		navi = (Navigation)findViewById(R.id.navigationView);
		navi.getBtn_left().setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				pop();
			}
		});
		
		TextView work_name_textview = (TextView)findViewById(R.id.work_name_textview);
		TextView work_info_textview = (TextView)findViewById(R.id.work_info_textview);
		String realName = mainData.getUserInfo().RealName;
		String userMail = mainData.getUserInfo().Domain_UserName + "@sznx.com.cn";
		String phone_info = mainData.getUserInfo().Mobile_Tel + "  " + mainData.getUserInfo().Group_Tel;
		work_name_textview.setText(realName);
		work_info_textview.setText(userMail + "\n" + phone_info);
		
		viewPager = (ViewPager)findViewById(R.id.viewPager);
		initDeviceListView();
		final ArrayList<View> views = new ArrayList<View>();
		views.add(device_view);
		viewPager.setAdapter(new MyViewPagerAdapter(views));
	}
	
	private void initDeviceListView(){
		device_view = inflater.inflate(R.layout.my_worker_listview, null);
		final MyListView listView = (MyListView)device_view.findViewById(R.id.myListView);
		device_list = new ArrayList<Map<String,Object>>();
		deviceAdapter = new MyListViewAdapter(this, device_list,  
				new ListViewInterface(){	
			@Override
			public void setCell(final MyListViewAdapter adapter, View CellView, final int position) {
				TextView device_name = (TextView)CellView.findViewById(R.id.device_name);
				TextView device_info = (TextView)CellView.findViewById(R.id.device_info);
				
				Map<String, Object> map = (Map<String, Object>)adapter.getItem(position);
				final Device_Info device = (Device_Info)map.get("device");
				device_name.setText(device.Model_Name);
				String info = "单号:" + device.Device_Barcode + "    " + "购买时间:" + device.Device_Buy_Time;
				device_info.setText(info);
				CellView.findViewById(R.id.device_button).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						mainData.device_Info = device;
						select_cell_index = position;
						push(AssetInputActivity.class, 101);
					}
				});
			}

			@Override
			public View getCell(final MyListViewAdapter adapter, final int position) {
				System.out.println("getCell index = " + position);
				View CellView = inflater.inflate(R.layout.device_cell, null);
				TextView device_name = (TextView)CellView.findViewById(R.id.device_name);
				TextView device_info = (TextView)CellView.findViewById(R.id.device_info);
				
				Map<String, Object> map = (Map<String, Object>)adapter.getItem(position);
				final Device_Info device = (Device_Info)map.get("device");
				device_name.setText(device.Model_Name);
				String info = "单号:" + device.Device_Barcode + "    " + "购买时间:" + device.Device_Buy_Time;
				device_info.setText(info);
				
				CellView.findViewById(R.id.device_button).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						select_cell_index = position;
						mainData.device_Info = device;
						push(AssetInputActivity.class);
					}
				});
				
				return CellView;
			}
		});
		listView.setAdapter(deviceAdapter);

		listView.setonRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				nssySoap.Device_Info_List("", 3, 10000, new SoapInterface() {					
					@Override
					public void soapResult(ArrayList<Object> arrayList) {
						listView.onRefreshComplete();
						String result = arrayList.get(0).toString();	
						device_list.clear();
						if(mainData.setDeviceInfoList(result)== true){
							for(int i = 0; i < mainData.getDeviceInfoArrayList().size(); i++){
								Device_Info device = mainData.getDeviceInfoArrayList().get(i);
								Map<String, Object> map=new HashMap<String, Object>();
								map.put("device", device);
								device_list.add(map);			
							}
							showMessage("刷新完成");
							deviceAdapter.notifyDataSetChanged();
						}else{
							//waittingDialog.dismiss();
							showMessage("无设备信息");
						}						
					}

					@Override
					public void soapError(String error) {
						listView.onRefreshComplete();
						showMessage("错误信息 :" + error);						
					}
				});
			}
		});
	}
	
	@Override  
	public void onWindowFocusChanged(boolean hasFocus){  
		super.onWindowFocusChanged(hasFocus);
		System.out.println("onWindowFocusChanged**************************");
		if (hasFocus){  
			if(isFirstLaunch){
				isFirstLaunch = false;
				
				nssySoap.Device_Info_List("", 3, 10000, new SoapInterface() {					
					@Override
					public void soapResult(ArrayList<Object> arrayList) {
						String result = arrayList.get(0).toString();	
						device_list.clear();
						if(mainData.setDeviceInfoList(result)== true){
							for(int i = 0; i < mainData.getDeviceInfoArrayList().size(); i++){
								Device_Info device = mainData.getDeviceInfoArrayList().get(i);
								Map<String, Object> map=new HashMap<String, Object>();
								map.put("device", device);
								device_list.add(map);			
							}
							deviceAdapter.notifyDataSetChanged();
						}else{
							//waittingDialog.dismiss();
							showMessage("无设备信息");
						}						
					}

					@Override
					public void soapError(String error) {
						showMessage("错误信息 :" + error);						
					}
				});
			}
		}
	}
	
	
	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  
	{  
		//可以根据多个请求代码来作相应的操作  
		if(requestCode == 101){  
			if(resultCode == 0){
				//更新成功
				device_list.remove(select_cell_index);
				deviceAdapter.notifyDataSetChanged();
			}else{

			} 
		}  
		device_view = null;
		select_cell_index = -1;
		super.onActivityResult(requestCode, resultCode, data);  
	}
}