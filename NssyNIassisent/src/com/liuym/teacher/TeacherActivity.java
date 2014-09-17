package com.liuym.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.liuym.adapter.MyListViewAdapter;
import com.liuym.adapter.MyListViewAdapter.ListViewInterface;
import com.liuym.adapter.MyViewPagerAdapter;
import com.liuym.nssyniassisent.*;
import com.liuym.nssyniassisent.MainData.Depart_Class;
import com.liuym.nssyniassisent.MainData.System_Infomation;
import com.liuym.soap.Soap.SoapInterface;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams") 
public class TeacherActivity extends MainActivity {
	private LayoutInflater inflater = null;
	private View teackerView = null;
	private TextView teacher_info_TextView = null;
	private View ibeactonView = null;
	private boolean ibViewIsFirst = true;
	private boolean isFirstLaunch;
	//tableview
	private ViewPager viewPager = null;
	private View viewPager_title_img = null;
	private View order_view = null;
	MyListViewAdapter infoAdapter = null;
	List<Map<String, Object>> systemInfo_list = null;
	private int order_select_index = -1;
	private View order_select_view = null;
	private View history_view = null;
	private int history_select_index = -1;
	private View history_select_view = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_teacher);

		//getIntentValues();

		isFirstLaunch = true;
		inflater = LayoutInflater.from(this); 
		teackerView = inflater.inflate(R.layout.activity_teacher, null);
		teacher_info_TextView = (TextView)teackerView.findViewById(R.id.teacher_info);
		setContentView(teackerView);
		ibeactonView = inflater.inflate(R.layout.activity_ibeacon, null);

		findViewById(R.id.logout).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				popRoot();
			}
		});

		findViewById(R.id.first).setEnabled(false);

		/*findViewById(R.id.second).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				setIbeaconView();
			}
		});*/

		findViewById(R.id.teacherBtn).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				push(PersonActivity.class);
			}
		});

		findViewById(R.id.classBtn).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				push(ClassActivity.class);
			}
		});

		viewPager_title_img = (View)findViewById(R.id.viewpager_title_img);
		findViewById(R.id.history_button).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				viewPager.setCurrentItem(1);
				viewPager_title_img.setBackgroundResource(R.drawable.tab_bar_1_selected);
			}
		});

		findViewById(R.id.message_button).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				viewPager.setCurrentItem(0);
				viewPager_title_img.setBackgroundResource(R.drawable.tab_bar_0_selected);
			}
		});


		viewPager = (ViewPager)findViewById(R.id.viewPager);
		initOrderListView();
		initHistoryListView();
		final ArrayList<View> views = new ArrayList<View>();
		views.add(order_view);
		views.add(history_view);
		viewPager.setAdapter(new MyViewPagerAdapter(views));
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				switch(arg0){
				case 0:
					viewPager_title_img.setBackgroundResource(R.drawable.tab_bar_0_selected);
					break;
				case 1:
					viewPager_title_img.setBackgroundResource(R.drawable.tab_bar_1_selected);
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initOrderListView(){
		//init Order ListView
		order_view = inflater.inflate(R.layout.my_listview, null);
		ListView listView = (ListView)order_view.findViewById(R.id.myListView);
		systemInfo_list = new ArrayList<Map<String,Object>>();
		infoAdapter = new MyListViewAdapter(this, systemInfo_list,  
				new ListViewInterface(){	
			@SuppressWarnings("unchecked")
			@Override
			public void setCell(MyListViewAdapter adapter, View CellView, int position) {
				TextView info_time = (TextView)CellView.findViewById(R.id.info_time);
				TextView info_title = (TextView)CellView.findViewById(R.id.info_title);
				TextView info_detail = (TextView)CellView.findViewById(R.id.info_detail);
				if(order_select_index == position){
					System.out.println("setCell ==");
					info_detail.setVisibility(View.VISIBLE);
				}else{
					System.out.println("setCell !=");
					info_detail.setVisibility(View.GONE);
				}
				Map<String, Object> map = (Map<String, Object>)adapter.getItem(position);
				System_Infomation system_info = (System_Infomation)map.get("system_info");
				info_time.setText(system_info.System_Information_Addtime);
				info_title.setText(system_info.System_Information_Title);
				info_detail.setText(system_info.System_Information_Content);
			}

			@Override
			public View getCell(MyListViewAdapter adapter, final int position) {
				System.out.println("getCell index = " + position);
				View CellView = inflater.inflate(R.layout.info_cell, null);
				TextView info_time = (TextView)CellView.findViewById(R.id.info_time);
				TextView info_title = (TextView)CellView.findViewById(R.id.info_title);
				TextView info_detail = (TextView)CellView.findViewById(R.id.info_detail);
				Map<String, Object> map = (Map<String, Object>)adapter.getItem(position);
				System_Infomation system_info = (System_Infomation)map.get("system_info");
				info_time.setText(system_info.System_Information_Addtime);
				info_title.setText(system_info.System_Information_Title);
				info_detail.setVisibility(View.GONE); 
				info_detail.setText(system_info.System_Information_Content);
				return CellView;
			}
		});
		listView.setAdapter(infoAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){ 	       
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
					long arg3){  
				TextView textView = (TextView)arg1.findViewById(R.id.info_detail);
				if(order_select_index != arg2){
					if(order_select_index != -1){
						order_select_view.setVisibility(View.GONE);
					}
					order_select_index = arg2;	    		  
				}
				if(textView.getVisibility() == View.VISIBLE){
					textView.setVisibility(View.GONE);
					order_select_index = -1;
				}else{
					textView.setVisibility(View.VISIBLE);
					/*textView.setMovementMethod(ScrollingMovementMethod.getInstance()); */
				}
				order_select_view = textView;
			}  
		}); 
	}

	private void initHistoryListView(){
		//init Order ListView
		history_view = inflater.inflate(R.layout.my_listview, null);
		ListView listView = (ListView)history_view.findViewById(R.id.myListView);

		final MyListViewAdapter historyAdapter = new MyListViewAdapter(this, getOrderList(), 
				new ListViewInterface() {			
			@Override
			public void setCell(MyListViewAdapter adapter, View view, int position) {
				//设置CellView 里面的数据
				System.out.println("setCell index = " + position);
				View detailView = (View)view.findViewById(R.id.history_info);
				if(history_select_index == position){
					System.out.println("setCell ==");
					detailView.setVisibility(View.VISIBLE);
				}else{
					System.out.println("setCell !=");
					detailView.setVisibility(View.GONE);
				}
				setHistoryViewOnClick(view, position);
			}

			@Override
			public View getCell(MyListViewAdapter adapter, final int position) {
				System.out.println("getCell index = " + position);
				View CellView = inflater.inflate(R.layout.history_cell, null);
				View detailView = (View)CellView.findViewById(R.id.history_info);
				detailView.setVisibility(View.GONE);
				setHistoryViewOnClick(CellView, position);
				return CellView;
			}
		});
		listView.setAdapter(historyAdapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){ 	       
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
					long arg3){
				View detailView = (View)arg1.findViewById(R.id.history_info);
				if(history_select_index != arg2){
					if(history_select_index != -1){
						history_select_view.setVisibility(View.GONE);
					}
					history_select_index = arg2;	    		  
				}
				if(detailView.getVisibility() == View.VISIBLE){
					detailView.setVisibility(View.GONE);
					history_select_index = -1;
				}else{
					detailView.setVisibility(View.VISIBLE);
				}
				history_select_view = detailView;
			}  
		}); 
	}

	private void setHistoryViewOnClick(View view, final int index){
		System.out.println("index = " + index);
		view.findViewById(R.id.history_btn_1).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {	
				showHistoryDialog(1);
			}
		});

		view.findViewById(R.id.history_btn_2).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				showHistoryDialog(2);		
			}
		});

		view.findViewById(R.id.history_btn_3).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				showHistoryDialog(3);
			}
		});

		view.findViewById(R.id.history_btn_4).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				showHistoryDialog(4);	
			}
		});
	}

	private List<Map<String, Object>> getOrderList(){  
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();  
		for (int i = 0; i < 30; i++) {  

			Map<String, Object> map=new HashMap<String, Object>();
			map.put("title", "这是一个标题"+i);  
			map.put("info", "这是一个详细信息"+i);  
			list.add(map);  
		}  
		return list;  
	}

	private void setTeackerView(){
		setContentView(teackerView);
	}

	private void setIbeaconView(){
		setContentView(ibeactonView);
		if(ibViewIsFirst){
			ibViewIsFirst = false;
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
					setTeackerView();
				}
			});
		}		
	}

	private void showHistoryDialog(int index){
		final Dialog dialog = new Dialog(this, R.style.MyDialog);
		View view = null;
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		switch (index) {
		case 1:			
		{
			for (int i = 0; i < 20; i++) {  
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("cell_info", "20140601 09:30:00 张晓颖"); 
				list.add(map);  
			}  
			view = inflater.inflate(R.layout.repair_queue, null);
			TextView repair_queue_textview = (TextView)view.findViewById(R.id.repair_queue_num);
			repair_queue_textview.setText("您前面还有" + list.size() + "位在等待");
			ListView listView = (ListView)view.findViewById(R.id.myListView);
			MyListViewAdapter infoAdapter = new MyListViewAdapter(this, list,  
					new ListViewInterface(){
				@Override
				public void setCell(MyListViewAdapter adapter, View CellView, int position) {
					TextView repair_queue_cell_info = (TextView)CellView.findViewById(R.id.repair_queue_cell_info);
					Map<String, Object> map = (Map<String, Object>)adapter.getItem(position);
					repair_queue_cell_info.setText((String)map.get("cell_info"));
				}

				@Override
				public View getCell(MyListViewAdapter adapter, final int position) {
					System.out.println("getCell index = " + position);
					View CellView = inflater.inflate(R.layout.repair_queue_cell, null);
					TextView repair_queue_cell_info = (TextView)CellView.findViewById(R.id.repair_queue_cell_info);
					Map<String, Object> map = (Map<String, Object>)adapter.getItem(position);
					repair_queue_cell_info.setText((String)map.get("cell_info"));
					return CellView;
				}
			});
			listView.setAdapter(infoAdapter);
			break;
		}
		case 2:
			view = inflater.inflate(R.layout.repair_worker_info, null);
			break;
		case 3:{
			for (int i = 0; i < 20; i++) {  
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("cell_info", "20140601 09:30:00 故障原因已查明,正在排障,请耐心等待!"); 
				list.add(map);  
			}  
			view = inflater.inflate(R.layout.repair_status, null);
			ListView listView = (ListView)view.findViewById(R.id.myListView);
			MyListViewAdapter infoAdapter = new MyListViewAdapter(this, list,  
					new ListViewInterface(){
				@Override
				public void setCell(MyListViewAdapter adapter, View CellView, int position) {
					TextView repair_status_cell_info = (TextView)CellView.findViewById(R.id.repair_status_cell_info);
					Map<String, Object> map = (Map<String, Object>)adapter.getItem(position);
					repair_status_cell_info.setText((String)map.get("cell_info"));
				}

				@Override
				public View getCell(MyListViewAdapter adapter, final int position) {
					System.out.println("getCell index = " + position);
					View CellView = inflater.inflate(R.layout.repair_status_cell, null);
					TextView repair_status_cell_info = (TextView)CellView.findViewById(R.id.repair_status_cell_info);
					Map<String, Object> map = (Map<String, Object>)adapter.getItem(position);
					repair_status_cell_info.setText((String)map.get("cell_info"));
					return CellView;
				}
			});
			listView.setAdapter(infoAdapter);		
			break;
		}
		case 4:
			view = inflater.inflate(R.layout.repair_evaluation, null);
			final RatingBar rating1 = (RatingBar)view.findViewById(R.id.ratingBar_1);
			rating1.setOnRatingBarChangeListener(new OnRatingBarChangeListener(){ 
				@Override 
				public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) { 
					showMessage("star1 is " + rating);
				} 
			});
			final RatingBar rating2 = (RatingBar)view.findViewById(R.id.ratingBar_2);
			rating2.setOnRatingBarChangeListener(new OnRatingBarChangeListener(){ 
				@Override 
				public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) { 
					showMessage("star2 is " + rating);
				} 
			});
			final RatingBar rating3 = (RatingBar)view.findViewById(R.id.ratingBar_3);
			rating3.setOnRatingBarChangeListener(new OnRatingBarChangeListener(){ 
				@Override 
				public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) { 
					showMessage("star3 is " + rating);
				} 
			});
			view.findViewById(R.id.repair_evaluation_submit_button).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if(dialog.isShowing()){
						dialog.dismiss();
					}
				}
			});
			break;
		default:
			return;
		}
		dialog.setContentView(view);
		dialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teacher, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		/*int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}*/
		return super.onOptionsItemSelected(item);
	}

	@Override  
	public void onWindowFocusChanged(boolean hasFocus){  
		super.onWindowFocusChanged(hasFocus);
		System.out.println("onWindowFocusChanged**************************");
		if (hasFocus){  
			if(isFirstLaunch){
				isFirstLaunch = false;
				new Thread(new Runnable() {			
					@Override
					public void run() {
						Looper.prepare(); 
						waittingDialog.show(TeacherActivity.this, "", "获取用户信息，请等待...");
						nssySoap.Teacher_InfoList(mainData.getUserName(), 10000, new SoapInterface() {					
							@Override
							public void soapResult(ArrayList<Object> arrayList) {
								String info_list = arrayList.get(0).toString();	
								if(mainData.setUserInfoList(info_list) == true){
									String realName = mainData.getUserInfo().RealName;
									teacher_info_TextView.setText(realName + ",你的校园网无线网络已连接: 00:30:25");
									//showMessage("老师信息 :" + info_list);
									nssySoap.Deaprt_Room_list(mainData.getUserInfo().DepartID, 10000, new SoapInterface() {

										@Override
										public void soapResult(ArrayList<Object> arrayList) {
											String room_list = arrayList.get(0).toString();	
											if(mainData.setRoomList(room_list) == true){
												waittingDialog.dismiss();//showMessage("房间信息" + room_list);
											}

											nssySoap.System_Information_List(2, 0, 10000, new SoapInterface() {

												@Override
												public void soapResult(ArrayList<Object> arrayList) {
													String info_list = arrayList.get(0).toString();
													if(mainData.setSystemInfoList(info_list)){
														for(int i = 0; i < mainData.getSystemInfoArrayList().size(); i++){
															System_Infomation systemInfo = mainData.getSystemInfoArrayList().get(i);
															Map<String, Object> map=new HashMap<String, Object>();
															map.put("system_info", systemInfo);
															systemInfo_list.add(map);			
														}
														infoAdapter.notifyDataSetChanged();
													}else{
														showMessage("无系统信息");
													}
												}

												@Override
												public void soapError(String error) {

												}
											});
										}

										@Override
										public void soapError(String error) {
											waittingDialog.dismiss();
											showMessage("错误信息:" + error);	
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
						Looper.loop(); 
					}
				}).start();
			}
		}
	}  
}
