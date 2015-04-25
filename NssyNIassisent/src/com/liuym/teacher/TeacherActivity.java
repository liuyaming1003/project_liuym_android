package com.liuym.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.liuym.adapter.MyListView;
import com.liuym.adapter.MyListView.OnRefreshListener;
import com.liuym.adapter.MyListViewAdapter;
import com.liuym.adapter.MyListViewAdapter.ListViewInterface;
import com.liuym.adapter.MyViewPagerAdapter;
import com.liuym.nssyniassisent.*;
import com.liuym.nssyniassisent.MainData.Repair_Feed_Back;
import com.liuym.nssyniassisent.MainData.Repair_Recode;
import com.liuym.nssyniassisent.MainData.System_Infomation;
import com.liuym.nssyniassisent.MainData.UserRepairRecode;
import com.liuym.soap.Soap.SoapInterface;
import com.liuym.worker.OrderDetailActivity;

import android.R.array;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

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
	private MyListViewAdapter infoAdapter = null;
	private List<Map<String, Object>> systemInfo_list = null;
	private int order_select_index = -1;
	private View order_select_view = null;
	private View history_view = null;
	private int history_select_index = -1;
	private View history_select_view = null;
	private Button history_select_button = null;
	private ImageView history_select_imageview = null;
	private MyListViewAdapter historyAdapter = null;
	private List<Map<String, Object>> repairHistory_list = null;

	//评分值
	float ratingSelect1 = (float) 5.0;
	float ratingSelect2 = (float) 5.0;
	float ratingSelect3 = (float) 5.0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_teacher);

		//getIntentValues();

		isFirstLaunch = true;
		inflater = LayoutInflater.from(this); 
		teackerView = inflater.inflate(R.layout.activity_teacher, null);
		teacher_info_TextView = (TextView)teackerView.findViewById(R.id.teacher_info);
		String realName = mainData.getUserInfo().RealName;
		teacher_info_TextView.setText(realName + ",你的校园网无线网络已连接: 00:30:25");
		setContentView(teackerView);
		ibeactonView = inflater.inflate(R.layout.activity_ibeacon, null);

		findViewById(R.id.logout_radiobutton).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				pop();
			}
		});

		findViewById(R.id.first_radiobutton).setEnabled(false);

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

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void initOrderListView(){
		//init Order ListView
		order_view = inflater.inflate(R.layout.my_listview, null);
		final MyListView listView = (MyListView)order_view.findViewById(R.id.myListView);
		systemInfo_list = new ArrayList<Map<String,Object>>();
		infoAdapter = new MyListViewAdapter(this, systemInfo_list,  
				new ListViewInterface(){	
			@SuppressWarnings("unchecked")
			@Override
			public View Cell(MyListViewAdapter adapter, View cellView, int position) {
				if(cellView == null){
					cellView = inflater.inflate(R.layout.info_cell, null);
				}
				TextView info_time = (TextView)cellView.findViewById(R.id.info_time);
				TextView info_title = (TextView)cellView.findViewById(R.id.info_title);
				TextView info_detail = (TextView)cellView.findViewById(R.id.info_detail);
				if(order_select_index  == position){
					info_detail.setVisibility(View.VISIBLE);
				}else{
					info_detail.setVisibility(View.GONE);
				}
				Map<String, Object> map = (Map<String, Object>)adapter.getItem(position);
				System_Infomation system_info = (System_Infomation)map.get("system_info");
				info_time.setText(system_info.System_Information_Addtime);
				info_title.setText(system_info.System_Information_Title);
				info_detail.setText(system_info.System_Information_Content);
				
				return cellView;
			}
		});
		listView.setAdapter(infoAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){ 	       
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
					long arg3){  
				arg2 = arg2 -1;
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

		listView.setonRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				order_select_view = null;
				order_select_index = -1;
				nssySoap.System_Information_List(2, 0, 10000, new SoapInterface() {

					@Override
					public void soapResult(ArrayList<Object> arrayList) {
						listView.onRefreshComplete();
						String info_list = arrayList.get(0).toString();
						if(mainData.setSystemInfoList(info_list)){
							systemInfo_list.clear();
							for(int i = 0; i < mainData.getSystemInfoArrayList().size(); i++){
								System_Infomation systemInfo = mainData.getSystemInfoArrayList().get(i);
								Map<String, Object> map=new HashMap<String, Object>();
								map.put("system_info", systemInfo);
								systemInfo_list.add(map);			
							}
							infoAdapter.notifyDataSetChanged();
							showMessage("刷新完成");
						}else{
							showMessage("无系统信息");
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

	private void initHistoryListView(){
		//init History ListView
		history_view = inflater.inflate(R.layout.my_listview, null);
		final MyListView listView = (MyListView)history_view.findViewById(R.id.myListView);
		repairHistory_list = new ArrayList<Map<String,Object>>();
		historyAdapter = new MyListViewAdapter(this, repairHistory_list, 
				new ListViewInterface() {			
			@Override
			public View Cell(MyListViewAdapter adapter, View cellView, final int position) {
				//设置CellView 里面的数据
				if(cellView == null){
					cellView = inflater.inflate(R.layout.history_cell, null);
				}
				View detailView = (View)cellView.findViewById(R.id.history_info);
				ImageView imageView = (ImageView)cellView.findViewById(R.id.history_detail_imageview);
				Button button = (Button)cellView.findViewById(R.id.history_detail_button);

				if(history_select_index == position){
					detailView.setVisibility(View.VISIBLE);
					//button.setVisibility(View.VISIBLE);
					//imageView.setVisibility(View.INVISIBLE);
				}else{
					button.setVisibility(View.INVISIBLE);
					imageView.setVisibility(View.VISIBLE);
					detailView.setVisibility(View.GONE);
				}

				cellView.findViewById(R.id.history_btn_1).setEnabled(false);
				cellView.findViewById(R.id.history_btn_2).setEnabled(false);
				cellView.findViewById(R.id.history_btn_3).setEnabled(false);
				cellView.findViewById(R.id.history_btn_4).setEnabled(false);

				Map<String, Object> map = (Map<String, Object>)adapter.getItem(position);
				final Repair_Recode repair = (Repair_Recode)map.get("repair_history");
				TextView history_info_textview = (TextView)cellView.findViewById(R.id.history_info_textview);
				String info = "单号: " + repair.Repair_Recode_Num + "  报修人: " + mainData.getUserInfo().Domain_UserName/*repair.Repair_RealName*/ + "  时间: " + repair.Repair_time;
				history_info_textview.setText(info);
				
				if(repair.Repair_State == 1){
					button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							AlertDialog.Builder builder = new AlertDialog.Builder(TeacherActivity.this);
							builder.setMessage("是否撤销此单？");
							builder.setTitle("撤销确认");
							builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int arg1) {
									dialog.dismiss();
									waittingDialog.show(TeacherActivity.this, "", "正在撤销中...");
									nssySoap.Cancel_Repair_Recode(repair.Repair_Recode_Num, 10000, new SoapInterface() {
										@Override
										public void soapResult(ArrayList<Object> arrayList) {
											waittingDialog.dismiss();
											String result = arrayList.get(0).toString();
											if(result.equals("s")){
												history_select_index = -1;
												repairHistory_list.remove(position);
												historyAdapter.notifyDataSetChanged();
												listView.invalidate();
												showMessage("撤销完成");
											}else{
												showMessage("撤销失败" + result);
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
							builder.setNegativeButton("取消", new AlertDialog.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
							builder.create().show();							
						}
					});
				}

				setHistoryViewOnClick(adapter, cellView, position);
				return cellView;
			}
		});
		listView.setAdapter(historyAdapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){ 	       
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
					long arg3){
				//由于使用了下拉刷新，arg2多了一个Cell，所有需要减1
				arg2 = arg2 -1;
				View detailView = (View)arg1.findViewById(R.id.history_info);
				ImageView imageView = (ImageView)arg1.findViewById(R.id.history_detail_imageview);
				Button button = (Button)arg1.findViewById(R.id.history_detail_button);

				Map<String, Object> map = (Map<String, Object>)historyAdapter.getItem(arg2);
				Repair_Recode repair = (Repair_Recode)map.get("repair_history");

				if(history_select_index != arg2){
					if(history_select_index != -1){
						history_select_view.setVisibility(View.GONE);
						history_select_button.setVisibility(View.INVISIBLE);
						history_select_imageview.setVisibility(View.VISIBLE);
					}
					history_select_index = arg2;	    		  
				}
				if(detailView.getVisibility() == View.VISIBLE){
					detailView.setVisibility(View.GONE);
					button.setVisibility(View.INVISIBLE);
					imageView.setVisibility(View.VISIBLE);
					history_select_index = -1;
				}else{
					detailView.setVisibility(View.VISIBLE);
					if(repair.Repair_State == 1){
						button.setVisibility(View.VISIBLE);
						imageView.setVisibility(View.INVISIBLE);
					}
				}
				history_select_view = detailView;
				history_select_button = button;
				history_select_imageview = imageView;
			}  
		}); 

		listView.setonRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				history_select_index = -1;
				history_select_view = null;
				history_select_button = null;
				history_select_imageview = null;
				nssySoap.User_Repair_Recode(mainData.getUserName(), 0, 10000, new SoapInterface() {
					@Override
					public void soapResult(ArrayList<Object> arrayList) {
						listView.onRefreshComplete();
						String repair_history = arrayList.get(0).toString();
						if(mainData.setRepairHistoryList(repair_history)){
							repairHistory_list.clear();
							for(int i = 0; i < mainData.getRepairHistoryArrayList().size(); i++){
								Repair_Recode repair = mainData.getRepairHistoryArrayList().get(i);
								Map<String, Object> map=new HashMap<String, Object>();
								map.put("repair_history", repair);
								repairHistory_list.add(map);			
							}
							historyAdapter.notifyDataSetChanged();
							showMessage("刷新完成");
						}else{
							showMessage("无报修记录");
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

	private void setHistoryViewOnClick(MyListViewAdapter adapter, View view, final int index){
		System.out.println("index = " + index);
		Map<String, Object> map = (Map<String, Object>)adapter.getItem(index);
		final Repair_Recode repair = (Repair_Recode)map.get("repair_history");
		Button button = null;
		switch(repair.Repair_State){
		case 1:
			button = (Button)view.findViewById(R.id.history_btn_1);
			button.setEnabled(true);
			button.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {	
					showHistoryDialog(1, repair);
				}
			});
			break;
		case 2:
			button = (Button)view.findViewById(R.id.history_btn_2);
			button.setEnabled(true);
			button.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {	
					showHistoryDialog(2, repair);
				}
			});
			break;
		case 3:
			button = (Button)view.findViewById(R.id.history_btn_3);
			button.setEnabled(true);
			button.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {	
					showHistoryDialog(3, repair);
				}
			});
			break;
		case 4:
			break;
		case 5:
			button = (Button)view.findViewById(R.id.history_btn_4);
			button.setEnabled(true);
			button.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {	
					showHistoryDialog(5, repair);
				}
			});
			break;
		}
	}

	/*private void setTeackerView(){
		setContentView(teackerView);
	}*/

	/*private void setIbeaconView(){
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
	}*/

	private void showHistoryDialog(int index, final Repair_Recode repair){
		final Dialog dialog = new Dialog(this, R.style.MyDialog);
		//final View view = null;
		final List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		switch (index) {
		case 1:			
		{
			waittingDialog.show(TeacherActivity.this, "", "获取报修队列中, 请稍等...");
			nssySoap.No_Finish_Repair_Record(repair.DepartID, 0, 10000, new SoapInterface() {
				@Override
				public void soapResult(ArrayList<Object> arrayList) {
					waittingDialog.dismiss();
					String repair_list = arrayList.get(0).toString();
					if(mainData.setRepairHistoryList(repair_list)){
						for(int i = 0; i < mainData.getRepairHistoryArrayList().size(); i++){
							Repair_Recode repair = mainData.getRepairHistoryArrayList().get(i);
							Map<String, Object> map=new HashMap<String, Object>();
							map.put("repair_list", repair);
							list.add(map);			
						}

						View view = inflater.inflate(R.layout.repair_queue, null);
						TextView repair_queue_textview = (TextView)view.findViewById(R.id.repair_queue_num);
						repair_queue_textview.setText("您前面还有" + list.size() + "位在等待");
						ListView listView = (ListView)view.findViewById(R.id.myListView);
						MyListViewAdapter infoAdapter = new MyListViewAdapter(TeacherActivity.this, list,  
								new ListViewInterface(){
							@Override
							public View Cell(MyListViewAdapter adapter, View cellView, int position) {
								if(cellView == null){
									cellView = inflater.inflate(R.layout.repair_queue_cell, null);
								}
								TextView repair_queue_cell_info = (TextView)cellView.findViewById(R.id.repair_queue_cell_info);
								Map<String, Object> map = (Map<String, Object>)adapter.getItem(position);
								final Repair_Recode repair = (Repair_Recode)map.get("repair_list");
								String cell_info = repair.Repair_time + "  " + repair.Repair_RealName;
								repair_queue_cell_info.setText(cell_info);
								return cellView;
							}
						});
						listView.setAdapter(infoAdapter);
						dialog.setContentView(view);
						dialog.show();
					}else{
						showMessage("无记录");
					}
				}

				@Override
				public void soapError(String error) {
					waittingDialog.dismiss();
					showMessage("信息错误" + error);
				}
			});  

			break;
		}
		case 2:
		{
			System.out.println("*****维修人用户名******" + repair.Repair_Domain_UserName);
			waittingDialog.show(TeacherActivity.this, "", "获取维修人员信息，请等待...");
			nssySoap.Teacher_InfoList(repair.Service_UserName, 10000, new SoapInterface() {
				@Override
				public void soapResult(ArrayList<Object> arrayList) {
					waittingDialog.dismiss();
					String info_list = arrayList.get(0).toString();	
					if(mainData.setRepairInfoList(info_list) == true){
						View view = inflater.inflate(R.layout.repair_worker_info, null);
						ImageView photoView = (ImageView)view.findViewById(R.id.repair_worker_photo);
						TextView nameTextView = (TextView)view.findViewById(R.id.repair_worker_name);
						RatingBar ratingBar = (RatingBar)view.findViewById(R.id.ratingBar);

						if(!mainData.getRepairInfoList().pic.equals("null")){
							//去网络图片然后保存本地
						}
						nameTextView.setText(mainData.getRepairInfoList().Domain_UserName);
						ratingBar.setRating(mainData.getRepairInfoList().Score);
						dialog.setContentView(view);
						dialog.show();
					}else{
						showMessage("获取信息错误" + info_list);
					}
				}

				@Override
				public void soapError(String error) {
					waittingDialog.dismiss();
					showMessage("错误信息" + error);
				}
			});
		}

		break;
		case 3:{
			waittingDialog.show(TeacherActivity.this, "", "查询维修信息，请等待...");
			nssySoap.Repair_Feedback_List(repair.Repair_Recode_Num, 1, 0, 10000, new SoapInterface() {
				@Override
				public void soapResult(ArrayList<Object> arrayList) {
					waittingDialog.dismiss();
					String result = arrayList.get(0).toString();
					if(mainData.setRepairFeedInfoList(result) == true){
						for(int i = 0; i < mainData.getRepairFeedArrayList().size(); i++){
							Repair_Feed_Back repair_feed = mainData.getRepairFeedArrayList().get(i);
							Map<String, Object> map=new HashMap<String, Object>();
							map.put("repair_feed", repair_feed);
							list.add(map);			
						}

						View view = inflater.inflate(R.layout.repair_status, null);
						ListView listView = (ListView)view.findViewById(R.id.myListView);
						MyListViewAdapter infoAdapter = new MyListViewAdapter(TeacherActivity.this, list,  
								new ListViewInterface(){
							@Override
							public View Cell(MyListViewAdapter adapter, View cellView, int position) {
								if(cellView == null){
									cellView = inflater.inflate(R.layout.repair_status_cell, null);
								}
								TextView repair_status_cell_info = (TextView)cellView.findViewById(R.id.repair_status_cell_info);
								Map<String, Object> map = (Map<String, Object>)adapter.getItem(position);
								Repair_Feed_Back repair_feed = (Repair_Feed_Back)map.get("repair_feed");
								String str = repair_feed.Addtime + "  " + repair_feed.Feed_Content;
								repair_status_cell_info.setText(str);
								return cellView;
							}
						});
						listView.setAdapter(infoAdapter);	
						dialog.setContentView(view);
						dialog.show();
					}else{
						showMessage("无记录");
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
		case 5:
		{

			View view = inflater.inflate(R.layout.repair_evaluation, null);
			final RatingBar rating1 = (RatingBar)view.findViewById(R.id.ratingBar_1);
			rating1.setOnRatingBarChangeListener(new OnRatingBarChangeListener(){ 
				@Override 
				public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) { 
					if(rating == 0){
						ratingBar.setRating(1);
						rating = 1;
					}
					ratingSelect1 = rating;
					//showMessage("star1 is " + rating);
				} 
			});
			final RatingBar rating2 = (RatingBar)view.findViewById(R.id.ratingBar_2);
			rating2.setOnRatingBarChangeListener(new OnRatingBarChangeListener(){ 
				@Override 
				public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) { 
					if(rating == 0){
						ratingBar.setRating(1);
						rating = 2;
					}
					ratingSelect2 = rating;
					//showMessage("star2 is " + rating);
				} 
			});
			final RatingBar rating3 = (RatingBar)view.findViewById(R.id.ratingBar_3);
			rating3.setOnRatingBarChangeListener(new OnRatingBarChangeListener(){ 
				@Override 
				public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) { 
					if(rating == 0){
						ratingBar.setRating(1);
						rating = 3;
					}
					ratingSelect3 = rating;
					//showMessage("star3 is " + rating);
				} 
			});
			view.findViewById(R.id.repair_evaluation_submit_button).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					waittingDialog.show(TeacherActivity.this, "", "正在评分，请稍等...");
					nssySoap.Edit_Feedback_info(repair.Repair_Recode_Num, 2, (int)ratingSelect1, (int)ratingSelect2, "", mainData.getUserInfo().Domain_UserName, 1, 10000, new SoapInterface() {
						@Override
						public void soapResult(ArrayList<Object> arrayList) {
							waittingDialog.dismiss();
							String result = arrayList.get(0).toString();
							if(result.equals("s")){
								showMessage("评价完成");
								if(dialog.isShowing()){
									dialog.dismiss();
								}
							}else{
								showMessage("评价错误" + result);
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
			break;
		}
		default:
			return;
		}
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
				nssySoap.Deaprt_Room_list(mainData.getUserInfo().DepartID, 1, 10000, new SoapInterface() {
					@Override
					public void soapResult(ArrayList<Object> arrayList) {
						String room_list = arrayList.get(0).toString();	
						if(mainData.setRoomList(room_list) == true){
							//showMessage("房间信息" + room_list);
						}
					}
					@Override
					public void soapError(String error) {
						showMessage("错误信息:" + error);	
					}
				});

				nssySoap.System_Information_List(2, 0, 10000, new SoapInterface() {

					@Override
					public void soapResult(ArrayList<Object> arrayList) {
						String info_list = arrayList.get(0).toString();
						if(mainData.setSystemInfoList(info_list)){
							systemInfo_list.clear();
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
						showMessage("错误信息 :" + error);
					}
				});

				nssySoap.User_Repair_Recode(mainData.getUserName(), 0, 10000, new SoapInterface() {

					@Override
					public void soapResult(ArrayList<Object> arrayList) {
						String repair_history = arrayList.get(0).toString();
						if(mainData.setRepairHistoryList(repair_history)){
							repairHistory_list.clear();
							for(int i = 0; i < mainData.getRepairHistoryArrayList().size(); i++){
								Repair_Recode repair = mainData.getRepairHistoryArrayList().get(i);
								Map<String, Object> map=new HashMap<String, Object>();
								map.put("repair_history", repair);
								repairHistory_list.add(map);			
							}
							historyAdapter.notifyDataSetChanged();
						}else{
							showMessage("无报修记录");
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
