package com.liuym.worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.liuym.adapter.MyListViewAdapter;
import com.liuym.adapter.MyViewPagerAdapter;
import com.liuym.adapter.MyListViewAdapter.ListViewInterface;
import com.liuym.nssyniassisent.MainActivity;
import com.liuym.nssyniassisent.R;
import com.liuym.nssyniassisent.SerializableMap;
import com.liuym.nssyniassisent.MainData.Repair_Recode;
import com.liuym.soap.Soap.SoapInterface;
import com.liuym.zxing.CaptureActivity;

@SuppressLint("InflateParams") public class WorkerActivity extends MainActivity{
	private LayoutInflater inflater = null;
	private View firstView = null;
	private View secondView = null;
	private boolean secondViewIsFirst = true;
	private boolean isFirstLaunch = true;

	private TextView work_name_textview = null;
	private TextView work_info_textview = null;
	//tableview
	private ViewPager viewPager = null;
	private View order_view = null;
	private List<Map<String, Object>> repair_list = null;
	private MyListViewAdapter repairAdapter = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_worker);

		inflater = LayoutInflater.from(this); 
		firstView = inflater.inflate(R.layout.activity_worker, null);
		setContentView(firstView);
		secondView = inflater.inflate(R.layout.activity_asset_manager, null);

		findViewById(R.id.logout).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				popRoot();
			}
		});

		findViewById(R.id.second).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				setSecondView();
			}
		});

		findViewById(R.id.barcode).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				push(CaptureActivity.class, 100);
			}
		});

		work_name_textview = (TextView)findViewById(R.id.work_name_textview);
		work_info_textview = (TextView)findViewById(R.id.work_info_textview);

		findViewById(R.id.first).setEnabled(false);

		viewPager = (ViewPager)findViewById(R.id.viewPager);
		initReapirListView();
		final ArrayList<View> views = new ArrayList<View>();
		views.add(order_view);
		viewPager.setAdapter(new MyViewPagerAdapter(views));
	}

	private void initReapirListView(){
		order_view = inflater.inflate(R.layout.my_worker_listview, null);
		ListView listView = (ListView)order_view.findViewById(R.id.myListView);
		repair_list = new ArrayList<Map<String,Object>>();
		repairAdapter = new MyListViewAdapter(this, repair_list,  
				new ListViewInterface(){	
			@Override
			public void setCell(MyListViewAdapter adapter, View CellView, final int position) {
				TextView order_name = (TextView)CellView.findViewById(R.id.order_name);
				TextView order_info = (TextView)CellView.findViewById(R.id.order_info);
				final Map<String, Object> map = (Map<String, Object>)adapter.getItem(position);
				Repair_Recode repair = (Repair_Recode)map.get("repair_recode");
				order_name.setText(repair.Repair_RealName);
				order_info.setText(repair.Repair_time + "\n" + repair.Repair_Information);

				Button order_button = (Button)CellView.findViewById(R.id.order_button);
				order_button.setOnClickListener(new OnClickListener() {					
					@Override
					public void onClick(View arg0) {
						mainData.order_select_index = position;
						push(OrderDetailActivity.class);				
					}
				});
			}

			@Override
			public View getCell(MyListViewAdapter adapter, final int position) {
				System.out.println("getCell index = " + position);
				View CellView = inflater.inflate(R.layout.order_cell, null);
				TextView order_name = (TextView)CellView.findViewById(R.id.order_name);
				TextView order_info = (TextView)CellView.findViewById(R.id.order_info);
				Map<String, Object> map = (Map<String, Object>)adapter.getItem(position);
				Repair_Recode repair = (Repair_Recode)map.get("repair_recode");
				order_name.setText(repair.Repair_RealName);
				order_info.setText(repair.Repair_time + "\n" + repair.Repair_Information);
				Button order_button = (Button)CellView.findViewById(R.id.order_button);
				order_button.setOnClickListener(new OnClickListener() {					
					@Override
					public void onClick(View arg0) {
						mainData.order_select_index = position;
						push(OrderDetailActivity.class);				
					}
				});
				return CellView;
			}
		});
		listView.setAdapter(repairAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){ 	       
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
					long arg3){
			}  
		});
	}

	private void setFirstView(){
		setContentView(firstView);
	}

	private void setSecondView(){
		setContentView(secondView);
		if(secondViewIsFirst){
			secondViewIsFirst = false;
			findViewById(R.id.logout).setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					popRoot();
				}
			});

			findViewById(R.id.second).setEnabled(false);

			findViewById(R.id.first).setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					setFirstView();
				}
			});

			findViewById(R.id.asset_input_btn).setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					push(AssetInputActivity.class);
				}
			});

			findViewById(R.id.asset_query_btn).setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					push(AssetQueryActivity.class);
				}
			});
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
				showMessage(serializableMap.getMap().get("zxingCode").toString());
			}else{

			} 
		}  
		super.onActivityResult(requestCode, resultCode, data);  
	} 

	@Override  
	public void onWindowFocusChanged(boolean hasFocus){  
		super.onWindowFocusChanged(hasFocus);
		System.out.println("onWindowFocusChanged**************************");
		if (hasFocus){  
			if(isFirstLaunch){
				isFirstLaunch = false;
				waittingDialog.show(WorkerActivity .this, "", "获取工作人员信息，请等待...");
				nssySoap.Teacher_InfoList(mainData.getUserName(), 10000, new SoapInterface() {					
					@Override
					public void soapResult(ArrayList<Object> arrayList) {
						String info_list = arrayList.get(0).toString();	
						if(mainData.setUserInfoList(info_list) == true){
							String realName = mainData.getUserInfo().RealName;
							String userMail = mainData.getUserInfo().Domain_UserName + "@sznx.com.cn";
							String phone_info = mainData.getUserInfo().Mobile_Tel + "  " + mainData.getUserInfo().Group_Tel;
							work_name_textview.setText(realName);
							work_info_textview.setText(userMail + "\n" + phone_info);
							//showMessage("老师信息 :" + info_list);
							waittingDialog.dismiss();

							nssySoap.Worker_Repair_List(mainData.getUserInfo().DepartID, mainData.getUserName(),  10000, new SoapInterface() {					
								@Override
								public void soapResult(ArrayList<Object> arrayList) {
									String repairInfo = arrayList.get(0).toString();	
									if(mainData.setRepairRecodeList(repairInfo)== true){
										for(int i = 0; i < mainData.getRepairRecodeArrayList().size(); i++){
											Repair_Recode repair = mainData.getRepairRecodeArrayList().get(i);
											Map<String, Object> map=new HashMap<String, Object>();
											map.put("repair_recode", repair);
											repair_list.add(map);			
										}
										repairAdapter.notifyDataSetChanged();
									}else{
										//waittingDialog.dismiss();
										showMessage("无维修信息");
									}						
								}

								@Override
								public void soapError(String error) {
									showMessage("错误信息 :" + error);						
								}
							});

						}else{
							waittingDialog.dismiss();
							showMessage("用户信息 错误");
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
}