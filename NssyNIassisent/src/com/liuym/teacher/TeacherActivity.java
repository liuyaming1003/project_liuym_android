package com.liuym.teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.liuym.adapter.MyListViewAdapter;
import com.liuym.adapter.MyListViewAdapter.ListViewInterface;
import com.liuym.adapter.MyViewPagerAdapter;
import com.liuym.nssyniassisent.*;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("InflateParams") 
public class TeacherActivity extends MainActivity {
	private LayoutInflater inflater = null;
	private Navigation navi = null;
	private View teackerView = null;
	private View ibeactonView = null;
	private boolean ibViewIsFirst = true;

	//tableview
	private ViewPager viewPager = null;
	private View order_view = null;
	private View history_view = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teacher);

		inflater = LayoutInflater.from(this); 
		teackerView = inflater.inflate(R.layout.activity_teacher, null);
		ibeactonView = inflater.inflate(R.layout.activity_ibeacon, null);
		setTeackerView();
		navi = (Navigation)findViewById(R.id.navigationView);

		findViewById(R.id.logout).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				popRoot(navi);
			}
		});

		findViewById(R.id.first).setEnabled(false);

		findViewById(R.id.second).setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				setIbeaconView();
			}
		});

		viewPager = (ViewPager)findViewById(R.id.viewPager);
		initOrderListView();
		initHistoryListView();
		final ArrayList<View> views = new ArrayList<View>();
		views.add(order_view);
		views.add(history_view);
		viewPager.setAdapter(new MyViewPagerAdapter(views));
	}

	private void initOrderListView(){
		//init Order ListView
		order_view = inflater.inflate(R.layout.my_listview, null);
		ListView listView = (ListView)order_view.findViewById(R.id.myListView);
		final MyListViewAdapter infoAdapter = new MyListViewAdapter(this, getOrderList(),  
				new ListViewInterface(){	
			@Override
			public void setCell(View view, int position) {
				//设置CellView 里面的数据
				System.out.println("setCell index = " + position);
				/*Button button = (Button)view.findViewById(R.id.cell_button);*/
			}

			@Override
			public View getCell(final int position) {
				System.out.println("getCell index = " + position);
				View CellView = inflater.inflate(R.layout.info_cell, null);
				/*Button button = (Button)CellView.findViewById(R.id.cell_button);
				button.setOnClickListener(new OnClickListener() {					
					@Override
					public void onClick(View arg0) {
						System.out.println("button index = " + position);					
					}
				});*/
				return CellView;
			}
		});
		listView.setAdapter(infoAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){ 	       
	      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
	          long arg3){  
	    	  Map<String, Object>map = (Map<String, Object>)infoAdapter.getItem(arg2);
	    	  map.get("CellView");
	    	  View cellView = (View)map.get("CellView");
	    	  TextView textView = (TextView)cellView.findViewById(R.id.info_detail);
	    	  if(textView.getVisibility() == View.VISIBLE){
	    		  textView.setVisibility(View.GONE);
	    	  }else{
	    		  textView.setVisibility(View.VISIBLE);
	    	  }
	    	  System.out.println("select = " + infoAdapter.getItem(arg2) +"arg2 = " + arg2);
	      }  
	    }); 
	}

	private void initHistoryListView(){
		//init Order ListView
		history_view = inflater.inflate(R.layout.my_listview, null);
		ListView listView = (ListView)history_view.findViewById(R.id.myListView);
		listView.setAdapter(new MyListViewAdapter(this, getOrderList(), 
				new ListViewInterface() {			
			@Override
			public void setCell(View view, int position) {
				//设置CellView 里面的数据
				System.out.println("setCell index = " + position);
				Button button = (Button)view.findViewById(R.id.cell_button);
				button.setText("按钮 " + position);
			}

			@Override
			public View getCell(final int position) {
				System.out.println("getCell index = " + position);
				View CellView = inflater.inflate(R.layout.order_cell, null);
				Button button = (Button)CellView.findViewById(R.id.cell_button);
				button.setText("按钮 " + position);
				button.setOnClickListener(new OnClickListener() {					
					@Override
					public void onClick(View arg0) {
						System.out.println("button index = " + position);					
					}
				});
				return CellView;
			}
		}));
	}

	private List<Map<String, Object>> getOrderList(){  
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();  
		for (int i = 0; i < 30; i++) {  
			Map<String, Object> map=new HashMap<String, Object>();  
			map.put("image", R.drawable.ic_launcher);  
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
					popRoot(navi);
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
}
